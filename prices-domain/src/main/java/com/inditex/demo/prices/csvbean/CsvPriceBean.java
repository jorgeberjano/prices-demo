package com.inditex.demo.prices.csvbean;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CsvPriceBean {

    @CsvBindByName(column = "BrandId")
    private String brandId;

    @CsvBindByName(column = "StartDate")
    private String startDate;

    @CsvBindByName(column = "EndDate")
    private String endDate;

    @CsvBindByName(column = "PriceList")
    private String priceList;

    @CsvBindByName(column = "ProductId")
    private String productId;

    @CsvBindByName(column = "Priority")
    private Integer priority;

    @CsvBindByName(column = "Price")
    private BigDecimal price;

    @CsvBindByName(column = "Currency")
    private String currency;

    @CsvBindByName(column = "LastUpdate")
    private String lastUpdate;

    @CsvBindByName(column = "LastUpdateBy")
    private String lastUpdateBy;
}
