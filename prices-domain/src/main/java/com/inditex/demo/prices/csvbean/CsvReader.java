package com.inditex.demo.prices.csvbean;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvReader {

    public List<CsvPriceBean> readPricesFromPath(Path path) throws IOException {
        MappingStrategy<CsvPriceBean> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(CsvPriceBean.class);

        Reader reader = Files.newBufferedReader(path);
        CsvToBean<CsvPriceBean> cb = new CsvToBeanBuilder<CsvPriceBean>(reader)
                .withType(CsvPriceBean.class)
                .withMappingStrategy(mappingStrategy)
                .build();
        List<CsvPriceBean> list = cb.parse();
        reader.close();

        return list;
    }
}
