package com.crlink.smartpark.common.utils;

import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.web.ResponseResult;

public class AssertUtil {

    public static void requireTrue(Boolean condition, RuntimeException e) {
        if (condition) {
            return;
        }
        throw e;
    }

    public static void requireTrue(Boolean condition, String code, Object... params) {
        if (condition) {
            return;
        }
        throw new ResolinkRuntimeException(code, params);
    }

    public static void requireNotNull(Object object, String code) {
        requireTrue(object != null, new ResolinkRuntimeException(code));
    }

    public static void requireNull(Object object, String code) {
        requireTrue(object == null, new ResolinkRuntimeException(code));
    }


    public static void requireNotNull(Object object, RuntimeException e) {
        requireTrue(object != null, e);
    }

    public static void requireNotNullOrParamExp(Object object) {
        requireNotNull(object, new ResolinkRuntimeException("10003"));
    }

    public static void requireTrueOrParamExp(Boolean condition) {
        requireTrue(condition, new ResolinkRuntimeException("10003"));
    }

    public static void requireTrueOrDataExp(Boolean condition) {
        requireTrue(condition, new ResolinkRuntimeException("10002"));
    }

    public static void requireApiSuccess(ResponseResult result) {
        requireTrue(result != null && result.isSuccess(), "10005");
    }
}
