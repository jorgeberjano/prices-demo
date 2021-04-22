package com.inditex.demo.prices.csvbean;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class CsvResourceReader extends CsvReader {
    public List<CsvPriceBean> readPricesFromCsv() throws IOException, URISyntaxException {
        URL url = getClass().getResource("prices.csv");
        return readPricesFromPath(Paths.get(url.toURI()));
    }
}
