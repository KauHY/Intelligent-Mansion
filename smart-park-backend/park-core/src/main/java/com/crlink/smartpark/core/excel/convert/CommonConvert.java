package com.crlink.smartpark.core.excel.convert;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.utils.SpringContextUtils;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.crlink.smartpark.common.constants.Constants.TIME_PATTERN;

public class CommonConvert implements Converter<Object> {

    private Map<String, String> id2NameMap;

    @Override
    public Class supportJavaTypeKey() {
        return Object.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Object convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        if (StringUtils.isBlank(stringValue) && cellData.getNumberValue() == null) {
            return null;
        }
        Field field = contentProperty.getField();
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Class classGeneric = (Class) parameterizedType.getActualTypeArguments()[0];
            if (Collection.class.isAssignableFrom((Class<?>) parameterizedType.getRawType())) {
                List result = Lists.newArrayList();
                for (String s : StringUtils.split(stringValue, ",")) {
                    result.add(ReflectUtil.newInstance(classGeneric, s));
                }
                return result;
            }
        } else {
            //基本类型及其包装类型已经自动转换
            Class fieldClass = ((Class<?>) genericType);
            if (fieldClass == LocalDateTime.class) {
                LocalDateTime dateTime = null;
                if (stringValue != null) {
                    String format = Optional.ofNullable(contentProperty.getDateTimeFormatProperty()).map(t -> t.getFormat()).
                            orElse(TIME_PATTERN);
                    dateTime = LocalDateTime.parse(stringValue, DateTimeFormatter.ofPattern(format));
                } else {
                    Date date = DateUtil.getJavaDate(cellData.getNumberValue().doubleValue(),
                            false, null);
                    dateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }
                return dateTime;
            }
            return null;
        }
        return null;
    }

    @Override
    public CellData convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String result = null;
        if (Objects.nonNull(value) && value instanceof Collection) {
            if (needIdToName(contentProperty)) {
                result = CollectionUtil.join(idToNameConvert(contentProperty, ((Collection<String>) value)).values(), ",");
            } else {
                result = CollectionUtil.join((Collection<?>) value, ",");
            }
            if (result != null){
                return new CellData(result);
            }
        }
        if (Objects.nonNull(value) && value instanceof String && needIdToName(contentProperty)) {
            Map<String, String> id2NameMap = idToNameConvert(contentProperty, Sets.newHashSet((String) value));
            if (CollectionUtil.isNotEmpty(id2NameMap)) {
                return new CellData(id2NameMap.get((String) value));
            }
        }
        if (Objects.nonNull(value) && value instanceof LocalDateTime) {
            String format = Optional.ofNullable(contentProperty.getDateTimeFormatProperty()).map(t -> t.getFormat()).orElse(TIME_PATTERN);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            ((LocalDateTime) value).format(dateTimeFormatter);
            return new CellData(((LocalDateTime) value).format(dateTimeFormatter));
        }
        return new CellData("");
    }

    private Map<String, String> idToNameConvert(ExcelContentProperty contentProperty, Collection<String> ids) {
        Field field = contentProperty.getField();
        ExcelJavaType annotation = field.getAnnotation(ExcelJavaType.class);
        int i = annotation.idToNameType();
        if (ObjectUtil.isEmpty(id2NameMap) || isAnyNull(ids)) {
            id2NameMap = SpringContextUtils.getBean(CommonProvider.class).getNamesByIdsAndType(Sets.newHashSet(ids), annotation.idToNameType());
        }
        return id2NameMap;
    }

    private boolean needIdToName(ExcelContentProperty contentProperty) {
        Field field = contentProperty.getField();
        ExcelJavaType annotation = field.getAnnotation(ExcelJavaType.class);
        return (Objects.nonNull(annotation) && annotation.needIdToName());
    }

    private boolean isAnyNull(Collection<String> ids) {
        for (String id : ids) {
            if (Objects.isNull(id2NameMap.get(id))) {
                return true;
            }
        }
        return false;
    }
}
