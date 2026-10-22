package com.crlink.smartpark.common.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.crlink.smartpark.common.constants.Constants.DATE_PATTERN;

public class LocalDateConverter implements Converter<LocalDate> {

    @Override
    public Class supportJavaTypeKey() {
        return LocalDate.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDate convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        String format = Optional.ofNullable(excelContentProperty.getDateTimeFormatProperty()).map(t -> t.getFormat()).
                orElse(DATE_PATTERN);
        return LocalDate.parse(cellData.getStringValue(), DateTimeFormatter.ofPattern(format));
    }

    @Override
    public CellData convertToExcelData(LocalDate value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        String format = Optional.ofNullable(excelContentProperty.getDateTimeFormatProperty()).map(t -> t.getFormat()).
                orElse(DATE_PATTERN);
        return new CellData<>(value.format(DateTimeFormatter.ofPattern(format)));
    }
}
