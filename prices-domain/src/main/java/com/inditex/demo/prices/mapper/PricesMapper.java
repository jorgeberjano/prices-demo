package com.inditex.demo.prices.mapper;

import com.inditex.demo.prices.csvbean.CsvPriceBean;
import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.entity.PriceEntity;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public abstract class PricesMapper {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");

    public abstract PriceEntity mapCsvToEntity(CsvPriceBean priceCsvBean);

    public LocalDateTime mapStringToDateTime(String dateTimeText) {
        return LocalDateTime.parse(dateTimeText, dateTimeFormatter);
    }

    public String mapDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }

    public abstract PriceDto mapEntityToDto(PriceEntity priceEntity);
}
