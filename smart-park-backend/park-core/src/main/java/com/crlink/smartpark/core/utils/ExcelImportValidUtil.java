package com.crlink.smartpark.core.utils;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public class ExcelImportValidUtil {

    //public static String valid(Object object) {
    //    List<String> errors = new ArrayList<>();
    //    Field[] fields = object.getClass().getDeclaredFields();
    //    for (Field field : fields) {
    //        //设置可访问
    //        field.setAccessible(true);
    //        //属性的值
    //        Object fieldValue = null;
    //        try {
    //            fieldValue = field.get(object);
    //        } catch (IllegalAccessException e) {
    //        }
    //        //是否包含必填校验注解
    //        boolean isNotNull = field.isAnnotationPresent(NotNull.class);
    //        if (isNotNull && Objects.isNull(fieldValue)) {
    //            errors.add(field.getAnnotation(NotNull.class).message());
    //        }
    //        //是否包含字符串必填校验注解
    //        boolean isNotBlank = field.isAnnotationPresent(NotBlank.class);
    //        if (isNotBlank && (fieldValue == null || StrUtil.isBlank(fieldValue.toString()))) {
    //            errors.add(field.getAnnotation(NotBlank.class).message());
    //        }
    //        //是否包含字符串长度校验注解
    //        boolean isExcelStrValid = field.isAnnotationPresent(Length.class);
    //        if (fieldValue != null && isExcelStrValid) {
    //            String cellStr = fieldValue.toString();
    //            int minLength = field.getAnnotation(Length.class).min();
    //            int maxLength = field.getAnnotation(Length.class).max();
    //            if (StringUtils.isNotBlank(cellStr) && !(minLength <= cellStr.length() && cellStr.length() <= maxLength)) {
    //                errors.add(field.getAnnotation(Length.class).message());
    //            }
    //        }
    //        //是否包含数字类型校验注解
    //        boolean isExcelIntValid = field.isAnnotationPresent(Range.class);
    //        if (fieldValue != null && isExcelIntValid) {
    //            long min = field.getAnnotation(Range.class).min();
    //            long max = field.getAnnotation(Range.class).max();
    //            String msg = field.getAnnotation(Range.class).message();
    //            if (fieldValue instanceof Integer) {
    //                Integer number = (Integer) fieldValue;
    //                if (number < min || number > max) {
    //                    errors.add(msg);
    //                }
    //            } else if (fieldValue instanceof Long) {
    //                Long number = (Long) fieldValue;
    //                if (number < min || number > max) {
    //                    errors.add(msg);
    //                }
    //            } else if (fieldValue instanceof Float) {
    //                Float number = (Float) fieldValue;
    //                if (number < min || number > max) {
    //                    errors.add(msg);
    //                }
    //            } else if (fieldValue instanceof Double) {
    //                Double number = (Double) fieldValue;
    //                if (number < min || number > max) {
    //                    errors.add(msg);
    //                }
    //            }
    //        }
    //    }
    //    if (CollUtil.isEmpty(errors)) {
    //        return null;
    //    }
    //    return StrUtil.join(",", errors);
    //}

    public static String validateValue(Object object) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Object>> validates = validator.validate(object);
        List<String> errors = Lists.newArrayList();
        for (ConstraintViolation<Object> validate : validates) {
            errors.add(validate.getMessage());
        }
        if (CollUtil.isEmpty(errors)) {
            return null;
        }
        return StringUtils.join(errors, ",");
    }

}
