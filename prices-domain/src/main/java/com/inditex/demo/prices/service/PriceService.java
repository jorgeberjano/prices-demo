package com.inditex.demo.prices.service;

import com.inditex.demo.prices.dto.PriceDto;

public interface PriceService {
    PriceDto getPriceForProduct(String productId, String brandId, String applicationDateText) throws Exception;
}
