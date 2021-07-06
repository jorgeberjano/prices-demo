package com.inditex.demo.prices.controller;

import com.inditex.demo.prices.dto.PriceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Api(value = "Prices API resource")
public interface PriceController {

    @ApiOperation(value = "Get price of a product", response = PriceDto.class)
    ResponseEntity<PriceDto> getPrice(
            @ApiParam(value = "Product identifier", required = true, example = "35455") String productId,
            @ApiParam(value = "Brand identifier", required = true, example = "1") String brandId,
            @ApiParam(value = "Date the price is in effect (ISO date time format)", required = false,
                    format = "textFormat", example = "2020-06-14T10:00:00")
                    LocalDateTime date);

    @ApiOperation(value = "Get all product prices")
    Flux<PriceDto> getAllPrices();
}
