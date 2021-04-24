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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class PriceBachServiceImpl implements PriceBatchService {

    private static final int BATCH_SIZE = 1000;

    private final PricesMapper pricesMapper;

    private final PriceRepository priceRepository;

    @PostConstruct
    public void init() {
        updatePrices();
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleAtMidnight() {
        updatePrices();
    }

    private final Path csvFilePath = Paths.get("prices.csv");

    public void updatePrices() {
        Stream<CsvPriceBean> csvPriceBeans;
        try {
            csvPriceBeans = readPricesFromCsv();
        } catch (IOException e) {
            log.error("Failed to read CSV file", e);
            return;
        }

        // Primero se borran todos los precios. En una implementación real se debería evitar no disponer de precios
        // durante la carga de los mismos. Se podría implementar fácilmente haciendo que las filas de la tabla tuviesen
        // un flag que indicase si son antiguos o nuevos y se borrasen los precios antiguos al final de la actualización
        priceRepository.deleteAll();

        // Se persisten las entidades en trozos de esta manera no se carga completamente el archivo CVS en memoria
        // pero, por otra parte, se realiza la grabación de las entidades por lotes, lo cual es mas eficiente que hacerlo
        // de uno en uno
        Flux.fromStream(csvPriceBeans.map(pricesMapper::mapCsvToEntity))
                .buffer(BATCH_SIZE)
                .subscribe(priceRepository::saveAll);
    }

    @Deprecated
    public void updatePricesNotEfficient() {
        Stream<CsvPriceBean> csvPriceBeans;
        try {
            csvPriceBeans = readPricesFromCsv();
        } catch (IOException e) {
            log.error("Failed to read CSV file", e);
            return;
        }
        List<PriceEntity> priceEntities = csvPriceBeans.map(pricesMapper::mapCsvToEntity).collect(Collectors.toList());
        priceRepository.saveAll(priceEntities);
    }

    private Stream<CsvPriceBean> readPricesFromCsv() throws IOException {

        MappingStrategy<CsvPriceBean> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(CsvPriceBean.class);

        Reader reader = Files.newBufferedReader(csvFilePath);
        CsvToBean<CsvPriceBean> csvToBean = new CsvToBeanBuilder<CsvPriceBean>(reader)
                .withType(CsvPriceBean.class)
                .withMappingStrategy(mappingStrategy)
                .withIgnoreEmptyLine(true)
                .build();

        return csvToBean.stream();
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
        FileOutputStream out = new FileOutputStream(file);
        multipartFile.getInputStream().transferTo(out);
        out.close();
    }

    @Override
    public void deleteFile() throws IOException {
        Files.delete(csvFilePath);
    }
}
