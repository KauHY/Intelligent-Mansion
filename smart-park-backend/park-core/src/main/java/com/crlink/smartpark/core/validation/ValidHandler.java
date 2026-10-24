package com.crlink.smartpark.core.validation;

public interface ValidHandler<T> {
    boolean valid(CustomValid customValid, T data);
}
