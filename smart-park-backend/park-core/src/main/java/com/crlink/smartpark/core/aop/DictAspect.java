package com.crlink.smartpark.core.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.DictUtil;
import com.github.pagehelper.PageInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class DictAspect {

    private final static String DEFAULT_DICT_SUFFIX = "Label";

    @Pointcut("@annotation(com.crlink.smartpark.common.annotations.DictTranslate)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object translation(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        if (result == null) {
            return null;
        }
        ResponseResult responseResult = null;
        if (result instanceof ResponseResult) {
            responseResult = (ResponseResult) result;
            result = responseResult.getData();
        }
        if (result instanceof Collection) {
            handleCollection((Collection) result);
        } else if (result instanceof PageInfo) {
            PageInfo pageInfo = (PageInfo) result;
            handleCollection(pageInfo.getList());
        } else {
            handleDict(result);
        }
        if (responseResult != null) {
            responseResult.setData(result);
            return responseResult;
        }
        return result;
    }

    private void handleCollection(Collection result) {
        if (CollUtil.isEmpty(result)) {
            return;
        }
        for (Object bean : result) {
            handleDict(bean);
        }
    }

    /**
     * 单个设置值
     *
     * @param obj
     */
    public void handleDict(Object obj) {
        Map<Field, DictField> dictParams = findDictField(obj.getClass());
        if (CollUtil.isEmpty(dictParams)) {
            return;
        }
        for (Map.Entry<Field, DictField> dictParam : dictParams.entrySet()) {
            //字典类型
            String type = dictParam.getValue().dictType();
            if (StrUtil.isBlank(type)) {
                continue;
            }
            //当前被注解的字段名
            String curFieldName = dictParam.getKey().getName();
            //字典值字段名
            String dictValFieldName = dictParam.getValue().dictValField();
            //如果没有指定则默认为当前字段
            if (StrUtil.isBlank(dictValFieldName)) {
                dictValFieldName = curFieldName;
            }
            //字典文案字段名
            String textFieldName = dictParam.getValue().textField();
            //如果不设值字典文案字段名添加默认后缀
            if (StrUtil.isBlank(textFieldName)) {
                textFieldName = dictValFieldName + DEFAULT_DICT_SUFFIX;
            }
            //获取字典值
            Integer dictValue = (Integer) getBeanFieldValue(obj, dictValFieldName);
            //字典文案
            String dictLabel = DictUtil.getLabelByTypeAndValue(type, dictValue);
            //设置值
            setBeanField(obj, textFieldName, dictLabel);
        }
    }

    /**
     * 获取类中需要翻译字典的属性值
     *
     * @param clazz
     * @return
     */
    private Map<Field, DictField> findDictField(Class clazz) {
        Map<Field, DictField> map = new HashMap<>();
        if (clazz == null) {
            return map;
        }
        Field[] fields = clazz.getDeclaredFields();
        DictField dataDict;
        for (Field field : fields) {
            if (field.isAnnotationPresent(DictField.class)) {
                dataDict = field.getAnnotation(DictField.class);
                map.put(field, dataDict);
            }
        }
        return map;
    }

    private void setBeanField(Object obj, String fieldName, String value) {
        try {
            if (hasField(obj.getClass(), fieldName)) {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(obj, value);
                return;
            }
        } catch (Exception e) {
        }
    }

    private Object getBeanFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
        }
        return null;
    }

    private boolean hasField(Class clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (fieldName.equals(f.getName())) {
                return true;
            }
        }
        return false;
    }


}
