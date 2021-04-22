package com.inditex.demo.prices;

import com.inditex.demo.prices.csvbean.CsvReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableJpaRepositories
@EnableScheduling
class ApplicationConfig {

    @Bean
    CsvReader getCsvReader() {
        return new CsvReader();
    }


}