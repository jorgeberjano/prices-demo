package com.inditex.demo.prices.mapper;

import com.inditex.demo.prices.csvbean.CsvPriceBean;
import com.inditex.demo.prices.dto.PriceDto;
import com.inditex.demo.prices.entity.PriceEntity;
import org.mapstruct.Mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PricesMapper {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");

    public abstract List<PriceEntity> mapCsvToEntityList(List<CsvPriceBean> priceCsvBean);

    public abstract PriceEntity mapCsvToEntity(CsvPriceBean priceCsvBean);

    public Date mapStringToDateTime(String dateTimeText) throws ParseException {
        return dateFormat.parse(dateTimeText);
    }

    public String mapDateTimeToString(Date dateTime) throws ParseException {
        return dateFormat.format(dateTime);
    }

    public abstract PriceDto mapEntityToDto(PriceEntity priceEntity);
}
