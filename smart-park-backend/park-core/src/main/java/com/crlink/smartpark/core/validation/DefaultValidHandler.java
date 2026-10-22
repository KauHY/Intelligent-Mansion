package com.crlink.smartpark.core.validation;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Set;

public class DefaultValidHandler implements ValidHandler {
    @Override
    public boolean valid(CustomValid customValid, Object data) {
        Set<String> validValues = Sets.newHashSet(customValid.value());
        if (ObjectUtils.isEmpty(validValues) || validValues.contains(data.toString())) {
            return true;
        }
        return false;
    }


}
