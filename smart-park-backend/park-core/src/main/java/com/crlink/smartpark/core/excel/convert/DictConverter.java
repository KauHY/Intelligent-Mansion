package com.crlink.smartpark.core.excel.convert;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.DictUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class DictConverter implements Converter<Integer> {


    private Map<Integer, String> value2labelsMap;
    private Map<String, Integer> label2ValuesMap;

    @Override
    public Class supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        String data = Optional.ofNullable(cellData.getStringValue()).orElse("");
        Field field = excelContentProperty.getField();
        if (!field.isAnnotationPresent(DictField.class)) {
            throw new ResolinkRuntimeException("10006");
        }
        String dictType = field.getAnnotation(DictField.class).dictType();
        if (StringUtils.isBlank(dictType)) {
            throw new ResolinkRuntimeException("10006");
        }
        if (ObjectUtil.isEmpty(label2ValuesMap)) {
            label2ValuesMap = DictUtil.getValues(dictType);
        }
        if (!label2ValuesMap.containsKey(data)) {
            throw new ResolinkRuntimeException("10006");
        }
        return label2ValuesMap.get(data);
    }

    @Override
    public CellData convertToExcelData(Integer value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new CellData();
        }
        Field field = excelContentProperty.getField();
        if (!field.isAnnotationPresent(DictField.class)) {
            return new CellData<>("未知字典文案");
        }
        String dictType = field.getAnnotation(DictField.class).dictType();
        if (StringUtils.isBlank(dictType)) {
            return new CellData<>("未知字典文案");
        }
        if (ObjectUtil.isEmpty(value2labelsMap)) {
            value2labelsMap = DictUtil.getLabels(dictType);
        }
        return new CellData(value2labelsMap.getOrDefault(value, ""));
    }
}
