package com.inditex.demo.prices.service;

import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.exception.PriceNotFoundException;
import com.inditex.demo.prices.mapper.PricesMapper;
import com.inditex.demo.prices.repository.PriceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    private final PricesMapper pricesMapper;

    @Override
    public PriceDto getPriceForProduct(String productId, String brandId, LocalDateTime date)
            throws PriceNotFoundException {

        return priceRepository.findFirstByProductBrandAndDate(productId, brandId, date)
                .map(pricesMapper::mapEntityToDto)
                .orElseThrow(() -> new PriceNotFoundException(getNotFoundMessage(productId, brandId, date)));
    }

    private String getNotFoundMessage(String productId, String brandId, LocalDateTime date) {
        return "No price found for product " + productId +
                " of brand " + brandId +
                " for date " + pricesMapper.mapDateTimeToString(date);
    }
}
