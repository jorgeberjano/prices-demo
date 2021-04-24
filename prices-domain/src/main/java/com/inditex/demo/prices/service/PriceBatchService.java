package com.inditex.demo.prices.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PriceBatchService {
    void storeFile(MultipartFile file) throws IOException;

    void deleteFile() throws IOException;

    void updatePricesReactive();

    @Deprecated
    void updatePricesNotReactive();
}
