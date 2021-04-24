package com.inditex.demo.prices.test;

import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.service.PriceBatchService;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PricesApiTest extends BaseTest {

    private static final String PRICES_PATH = "/price";

    @Autowired
    private PriceBatchService priceBatchService;

    @Before
    public void initialize() throws Exception {
        super.initialize();
    }

    @Test
    public void testProductPrices() throws Exception {
        // Se sube el archivo CSV de la carpeta de recursos de prueba
        byte[] cvsFileData = PricesApiTest.class.getResourceAsStream("/prices.csv").readAllBytes();
        MockMultipartFile file = new MockMultipartFile("file", cvsFileData);
        mvc.perform(MockMvcRequestBuilders.multipart(PRICES_PATH).file(file)).andExpect(status().isOk());
        priceBatchService.updatePricesReactive();

        // Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)
        checkPrice("35455", "1", "2020-06-14-10.00.00", "35.50", "1");

        // Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
        checkPrice("35455", "1", "2020-06-14-16.00.00", "25.45", "2");

        // Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
        checkPrice("35455", "1", "2020-06-14-21.00.00", "35.50", "1");

        // Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
        checkPrice("35455", "1", "2020-06-15-10.00.00", "30.50", "3");

        // Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)
        checkPrice("35455", "1", "2020-06-16-21.00.00", "38.95", "4");
    }

    private void checkPrice(String productId, String brandId, String date, String expectedPrice, String expectedPriceList) throws Exception {
        UriComponents uri = UriComponentsBuilder.newInstance()
                .path(PRICES_PATH)
                .queryParam("productId", productId)
                .queryParam("brandId", brandId)
                .queryParam("date", date)
                .build();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri.toUriString()).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        PriceDto price = super.jsonToObject(content, PriceDto.class);
        assertNotNull(price);
        assertEquals(new BigDecimal(expectedPrice), price.getPrice());
        assertEquals(expectedPriceList, price.getPriceList());
        assertEquals(productId, price.getProductId());
        assertEquals(brandId, price.getBrandId());
    }


    @Test
    public void testReactivePerformance() throws Exception {
        // Se copia el archivo con datos masivos de la carpeta de recursos de prueba a la carpeta de trabajo
        InputStream src = getClass().getResourceAsStream("/massive_prices.csv");
        Files.copy(src, Paths.get("prices.csv"), StandardCopyOption.REPLACE_EXISTING);

        StopWatch watch = new StopWatch();
        watch.start();
        priceBatchService.updatePricesReactive();
        watch.stop();
        System.out.println("Update prices reactively in " + watch.getTime() + " ms");
        System.out.println("Used memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 + " Kb");
    }

    @Test
    public void testNonReactivePerformance() throws Exception {
        // Se copia el archivo con datos masivos de la carpeta de recursos de prueba a la carpeta de trabajo
        InputStream src = getClass().getResourceAsStream("/massive_prices.csv");
        Files.copy(src, Paths.get("prices.csv"), StandardCopyOption.REPLACE_EXISTING);

        StopWatch watch = new StopWatch();
        watch.start();
        priceBatchService.updatePricesNotReactive();
        watch.stop();
        System.out.println("Update prices non reactively in " + watch.getTime() + " ms");
        System.out.println("Used memory: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 + " Kb");
    }
}