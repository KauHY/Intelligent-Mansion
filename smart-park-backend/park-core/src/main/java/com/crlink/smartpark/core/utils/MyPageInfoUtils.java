package com.crlink.smartpark.core.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.crlink.smartpark.common.message.EmptyPageException;
import com.github.pagehelper.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

public class MyPageInfoUtils {
    public static void isEmptyPage(String id) {
        if (StringUtils.isBlank(id)) {
            throw new EmptyPageException();
        }
    }

    public static <T extends Iterable> void isEmptyPage(T collections) {
        if (CollectionUtil.isEmpty(collections)) {
            throw new EmptyPageException();
        }
    }

    public static <T extends Page> void isEmptyPage(T collections) {
        if (CollectionUtil.isEmpty(collections)) {
            throw new EmptyPageException();
        }
    }

    public static <T extends Map> void isEmptyPage(T map) {
        if (CollectionUtil.isEmpty(map)) {
            throw new EmptyPageException();
        }
    }

    public static void isEmptyPage(Object obj) {
        if (Objects.isNull(obj)) {
            throw new EmptyPageException();
        }
    }


}
