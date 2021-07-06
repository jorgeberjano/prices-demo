package com.inditex.demo.prices.service;

import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.exception.PriceNotFoundException;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface PriceService {
    PriceDto getPriceForProduct(String productId, String brandId, LocalDateTime date) throws PriceNotFoundException;
    Flux<PriceDto> getAllPrices();
}
