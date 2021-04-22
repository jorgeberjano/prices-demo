package com.inditex.demo.prices.csvbean;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class CsvFileReader extends CsvReader {
    public List<CsvPriceBean> readPrices() throws IOException {
        return readPricesFromPath(Paths.get("prices.csv"));
    }
}
