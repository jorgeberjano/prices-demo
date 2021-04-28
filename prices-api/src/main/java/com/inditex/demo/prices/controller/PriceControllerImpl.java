package com.inditex.demo.prices.controller;


import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.service.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/brand/{brandId}/product/{productId}/price")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class PriceControllerImpl implements PriceController {

    private final PriceService priceService;

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
    public PriceDto getPrice(@PathVariable String productId,
                             @PathVariable String brandId,
                             @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd-HH.mm.ss") LocalDateTime date) {
        PriceDto price;
        try {
            price = priceService.getPriceForProduct(productId, brandId, date);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get price", ex);
        }
        return price;
    }
}