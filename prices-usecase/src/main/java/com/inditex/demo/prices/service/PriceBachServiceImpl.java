package com.inditex.demo.prices.service;

import com.inditex.demo.prices.csvbean.CsvPriceBean;
import com.inditex.demo.prices.entity.PriceEntity;
import com.inditex.demo.prices.mapper.PricesMapper;
import com.inditex.demo.prices.repository.PriceRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@AllArgsConstructor
public class PriceBachServiceImpl implements PriceBatchService {

    private static final int BATCH_SIZE = 1000;

    private final PricesMapper pricesMapper;

    private final PriceRepository priceRepository;

    @PostConstruct
    public void init() {
        updatePricesReactive();
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAtMidnight() {
        updatePricesReactive();
    }

    private final Path csvFilePath = Paths.get("prices.csv");

    public void updatePricesReactive() {

        priceRepository.deleteAll();

        try {
            Stream<CsvPriceBean> stream = getCsvPricesBeanStream();

            Flux.fromStream(stream.map(pricesMapper::mapCsvToEntity))
                .buffer(BATCH_SIZE)
                .subscribe(priceRepository::saveAll);
        } catch (IOException e) {
            log.error("Failed to update CSV", e);
        }
    }


    @Deprecated
    public void updatePricesNotReactive() {

        priceRepository.deleteAll();

        List<CsvPriceBean> csvPriceBeans;
        try {
            csvPriceBeans = readPricesFromCsv();
        } catch (IOException e) {
            log.error("Failed to read CSV file", e);
            return;
        }
        List<PriceEntity> priceEntities = csvPriceBeans
                .stream()
                .map(pricesMapper::mapCsvToEntity)
                .collect(Collectors.toList());
        priceRepository.saveAll(priceEntities);
    }

    @Override
    public FileSystemResource getCVSFile() {
        return new FileSystemResource(csvFilePath.toFile());
    }

    private List<CsvPriceBean> readPricesFromCsv() throws IOException {
        return getCsvPriceBeans().parse();
    }

    /**
     * Obtiene el stream de beans leídos del CVS
     */
    private Stream<CsvPriceBean> getCsvPricesBeanStream() throws IOException {
        return StreamSupport.stream(getCsvPriceBeans().spliterator(), false);
    }

    /**
     * Obtiene el lector de beans del archivo CVS
     */
    private CsvToBean<CsvPriceBean> getCsvPriceBeans() throws IOException {
        MappingStrategy<CsvPriceBean> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(CsvPriceBean.class);

        Reader reader = Files.newBufferedReader(csvFilePath);
        return new CsvToBeanBuilder<CsvPriceBean>(reader)
                .withType(CsvPriceBean.class)
                .withMappingStrategy(mappingStrategy)
                .withIgnoreEmptyLine(true)
                .build();
    }


    @Override
    public void storeFile(MultipartFile multipartFile) throws IOException {
        File file = csvFilePath.toFile();
        if (!file.exists()) {
            boolean ok = file.createNewFile();
            if (!ok) {
                throw new RuntimeException("Failed to create file " + multipartFile.getName());
            }
        }
        try (FileOutputStream out = new FileOutputStream(file)) {
            multipartFile.getInputStream().transferTo(out);
        }
    }

    @Override
    public void deleteFile() throws IOException {
        if (!Files.exists(csvFilePath)) {
            throw new FileNotFoundException("File not found: " + csvFilePath);
        }
        Files.delete(csvFilePath);
    }
}
