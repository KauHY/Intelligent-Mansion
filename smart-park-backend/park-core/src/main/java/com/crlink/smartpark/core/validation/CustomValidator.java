package com.crlink.smartpark.core.validation;

import com.baomidou.mybatisplus.core.toolkit.ClassUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class CustomValidator implements ConstraintValidator<CustomValid, Object> {

    private CustomValid customValid;

    @Override
    public void initialize(CustomValid constraintAnnotation) {
        this.customValid = constraintAnnotation;
    }


    @Override
    public boolean isValid(Object data, ConstraintValidatorContext context) {
        Class<? extends ValidHandler> handler = customValid.handler();
        if (null != data) {
            return Optional.ofNullable(handler).map(c -> {
                ValidHandler instance = ClassUtils.newInstance(c);
                return instance.valid(customValid, data);
            }).orElse(false);
        }
        return false;
    }
}
