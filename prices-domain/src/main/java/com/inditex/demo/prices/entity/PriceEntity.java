package com.inditex.demo.prices.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Entity
public class PriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "BRAND_ID")
    private String brandId;

    @Column(name = "START_DATE", columnDefinition = "TIMESTAMP")
    private LocalDateTime startDate;

    @Column(name = "END_DATE", columnDefinition = "TIMESTAMP")
    private LocalDateTime endDate;

    @Column(name = "PRICE_LIST")
    private String priceList;

    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "CURR")
    private String currency;
}
