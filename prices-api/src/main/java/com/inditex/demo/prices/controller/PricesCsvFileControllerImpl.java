package com.inditex.demo.prices.controller;


import com.inditex.demo.prices.service.PriceBatchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/prices/csv")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
public class PricesCsvFileControllerImpl implements PricesCsvFileController {

    private final PriceBatchService priceBatchService;

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Void> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            priceBatchService.storeFile(file);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", ex);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCsvFile() {
        try {
            priceBatchService.deleteFile();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file", ex);
        }
        return ResponseEntity.ok().build();
    }


}