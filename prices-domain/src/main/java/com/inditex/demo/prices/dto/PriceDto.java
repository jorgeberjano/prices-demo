package com.inditex.demo.prices.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceDto {
    private String productId;
    private String brandId;
    private String priceList;
    private String startDate;
    private String endDate;
    private BigDecimal price;
}
