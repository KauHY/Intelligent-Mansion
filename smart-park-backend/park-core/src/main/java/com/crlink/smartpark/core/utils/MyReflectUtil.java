package com.crlink.smartpark.core.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

public class MyReflectUtil {

    public static Map<Integer, String> buildFieldMap(Class clazz, Class ann) {
        Map<Integer, String> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (ann != null && !field.isAnnotationPresent(ann)) {
                continue;
            }
            map.put(index++, field.getName());
        }
        return map;
    }

    public static Integer getTotalAnn(Class clazz, Class ann) {
        Integer total = 0;
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (ann != null && !field.isAnnotationPresent(ann)) {
                continue;
            }
            total++;
        }
        return total;
    }

    public static List<List<String>> getExcelHeads(Class clazz, Collection<String> excludeHeads, String... headPropertyNames) {
        List<List<String>> heads = new ArrayList<>();
        Map<String, List<String>> map = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.isAnnotationPresent(ExcelProperty.class)) {
                continue;
            }
//            heads.add(Arrays.asList(field.getAnnotation(ExcelProperty.class).value()));
            String[] values = field.getAnnotation(ExcelProperty.class).value();
            String key = StringUtils.join(values, "_");
            if (!ObjectUtil.contains(excludeHeads, key)) {
                map.put(key, Arrays.asList(values));
            }
        }
        if (ArrayUtil.isNotEmpty(headPropertyNames)) {
            for (String headName : headPropertyNames) {
//                heads.add(Arrays.asList(headName));
                map.put(headName, Arrays.asList(headName));
            }
        }
        map.forEach((k, v) -> heads.add(v));
        return heads;
    }
}
