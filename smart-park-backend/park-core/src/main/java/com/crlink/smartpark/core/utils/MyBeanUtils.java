package com.crlink.smartpark.core.utils;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @description: 基于BeanUtils业务场景下的bean复制工具
 */
public class MyBeanUtils {

    public static <S, T> List<T> copyList(List<S> sourceList, Supplier<T> target, BiConsumer<S, T> handler) {
        List<T> targetList = new ArrayList<>();
        sourceList.stream().forEach(e -> {
            T t = target.get();
            BeanUtils.copyProperties(e, t);
            if (handler != null) {
                handler.accept(e, t);
            }
            targetList.add(t);
        });
        return targetList;
    }

    public static <S, T> List<T> copyList(List<S> sourceList, Supplier<T> target) {
        return copyList(sourceList, target, null);
    }

    public static <T> T copy(Object source, Supplier<T> target, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        T t = target.get();
        BeanUtils.copyProperties(source, t, ignoreProperties);
        return t;
    }

    public static <S, T> T copy(S source, Supplier<T> target, BiConsumer<S, T> handler, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        T t = target.get();
        BeanUtils.copyProperties(source, t, ignoreProperties);
        if (handler != null) {
            handler.accept(source, t);
        }
        return t;
    }

    public static <S, T> Collection<T> copyCollection(Collection<S> sourceList, Supplier<T> target, BiConsumer<S, T> handler) {
        Collection<T> targetList = ReflectUtil.newInstance(sourceList.getClass());
        sourceList.stream().forEach(e -> {
            T t = target.get();
            BeanUtils.copyProperties(e, t);
            if (handler != null) {
                handler.accept(e, t);
            }
            targetList.add(t);
        });
        return targetList;
    }


}
