package com.inditex.demo.prices.controller;

import com.inditex.demo.prices.dto.PriceDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "Prices Demo API")
@Validated
public interface PriceController {

    PriceDto getPrice(
            @ApiParam(value = "Producto identificator", required = true, example = "35455")
            @RequestParam String productId,
            @ApiParam(value = "Brand identificator", required = true, example = "1")
            @RequestParam String brandId,
            @ApiParam(value = "Date the price is in effect (YYYY-MM-DD-hh.mm.ss)", required = true, example = "2020-06-14-10.00.00")
            @RequestParam String date);
}
