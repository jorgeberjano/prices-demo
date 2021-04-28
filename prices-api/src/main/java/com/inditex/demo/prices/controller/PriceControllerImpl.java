package com.inditex.demo.prices.controller;


import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.exception.PriceNotFoundException;
import com.inditex.demo.prices.service.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/brand/{brandId}/product/{productId}/price")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@Validated
public class PriceControllerImpl implements PriceController {

    private final PriceService priceService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PriceDto> getPrice(@PathVariable String productId,
                                             @PathVariable String brandId,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }

        try {
            PriceDto price = priceService.getPriceForProduct(productId, brandId, date);
            return ResponseEntity.<PriceDto>ok(price);
        } catch (PriceNotFoundException ex) {
            return ResponseEntity.notFound().header("message", ex.getMessage()).build();
        }
    }
}