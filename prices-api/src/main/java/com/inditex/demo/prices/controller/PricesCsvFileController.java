package com.inditex.demo.prices.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Api(value = "Prices CSV File API resource")
public interface PricesCsvFileController {

    @ApiOperation(value = "Upload CSV file containing product price information")
    ResponseEntity<Void> uploadCsvFile(
            @ApiParam(value = "The CSV File", required = true, example = "prices.csv")
            MultipartFile file);

    @ApiOperation(value = "Download CSV file from server")
    ResponseEntity<FileSystemResource> downloadCsvFile();

    @ApiOperation(value = "Delete CSV file from server")
    ResponseEntity<Void> deleteCsvFile();
}
