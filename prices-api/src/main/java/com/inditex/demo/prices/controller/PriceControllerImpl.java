package com.inditex.demo.prices.controller;


import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.service.PriceBatchService;
import com.inditex.demo.prices.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/price")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PriceControllerImpl implements PriceController {

    @Autowired
    private PriceService priceService;

    @Autowired
    private PriceBatchService priceBatchService;

    @PostMapping(value = "", consumes = { "multipart/form-data" })
    public ResponseEntity<Void> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            priceBatchService.storeFile(file);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", ex);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "")
    public ResponseEntity<Void> deleteCsvFile() {
        try {
            priceBatchService.deleteFile();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file", ex);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
    public PriceDto getPrice(@RequestParam String productId,
                             @RequestParam String brandId,
                             @RequestParam String date) {
        PriceDto price;
        try {
            price = priceService.getPriceForProduct(productId, brandId, date);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get price", ex);
        }

        if (price == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product price not found");
        }
        return price;
    }
}