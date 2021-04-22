package com.inditex.demo.prices.service;


import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.mapper.PricesMapper;
import com.inditex.demo.prices.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@Slf4j
//@AllArgsConstructor
public class PriceServiceImpl implements PriceService {

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PricesMapper pricesMapper;

    @Override
    public PriceDto getPriceForProduct(String productId, String brandId, String applicationDateText) throws ParseException {
        Date applicationDate = pricesMapper.mapStringToDateTime(applicationDateText);

        Pageable pageable = PageRequest.of(0, 1, Sort.by("priority").descending());
        return priceRepository.findByProductBrandAndDate(productId, brandId, applicationDate, pageable)
                .stream()
                .findFirst()
                .map(pricesMapper::mapEntityToDto)
                .orElse(null);
    }
}
