package com.inditex.demo.prices.service;

import com.inditex.demo.prices.dto.PriceDto;

import java.time.LocalDateTime;

public interface PriceService {
    PriceDto getPriceForProduct(String productId, String brandId, LocalDateTime applicationDateText) throws Exception;
}
