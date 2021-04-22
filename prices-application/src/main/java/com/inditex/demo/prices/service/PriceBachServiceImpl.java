package com.inditex.demo.prices.service;

import com.inditex.demo.prices.csvbean.CsvPriceBean;
import com.inditex.demo.prices.entity.PriceEntity;
import com.inditex.demo.prices.mapper.PricesMapper;
import com.inditex.demo.prices.repository.PriceRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Slf4j
public class PriceBachServiceImpl implements PriceBatchService {

    @Autowired
    private PricesMapper pricesMapper;

    @Autowired
    private PriceRepository priceRepository;

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
        List<CsvPriceBean> csvPriceBeans;
        try {
            csvPriceBeans = readPricesFromCsv();
        } catch (IOException e) {
            log.error("Failed to read CSV file", e);
            return;
        }

        List<PriceEntity> prices = pricesMapper.mapCsvToEntityList(csvPriceBeans);

        persistPrices(prices);
    }

    public List<CsvPriceBean> readPricesFromCsv() throws IOException {

        MappingStrategy<CsvPriceBean> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(CsvPriceBean.class);

        Reader reader = Files.newBufferedReader(csvFilePath);
        CsvToBean<CsvPriceBean> cb = new CsvToBeanBuilder<CsvPriceBean>(reader)
                .withType(CsvPriceBean.class)
                .withMappingStrategy(mappingStrategy)
                .build();
        List<CsvPriceBean> list = cb.parse();
        reader.close();

        return list;
    }

    private void persistPrices(List<PriceEntity> prices) {
        priceRepository.saveAll(prices);
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
