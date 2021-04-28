package com.inditex.demo.prices.controller;

import com.inditex.demo.prices.dto.PriceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.time.LocalDateTime;

@Api(value = "Prices API resource")
public interface PriceController {

    @ApiOperation(value = "Get price of a product", response = PriceDto.class)
    PriceDto getPrice(
            @ApiParam(value = "Product identifier", required = true, example = "35455") String productId,
            @ApiParam(value = "Brand identifier", required = true, example = "1") String brandId,
            @ApiParam(value = "Date the price is in effect (YYYY-MM-DD-hh.mm.ss)", required = true,
                    example = "2020-06-14-10.00.00") LocalDateTime date);
}
