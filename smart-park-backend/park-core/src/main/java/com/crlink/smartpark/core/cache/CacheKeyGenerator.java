package com.crlink.smartpark.core.cache;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.core.utils.SystemUtil;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component
public class CacheKeyGenerator implements KeyGenerator {
    char comma = ',';

    @Override
    public Object generate(Object target, Method method, Object... params) {
        char dot = '.';
        StringBuilder strBuilder = new StringBuilder(64);
        // 类名
        strBuilder.append(target.getClass().getSimpleName());
        strBuilder.append(dot);
        // 方法名
        strBuilder.append(method.getName());
        strBuilder.append(dot);
        addUserId(strBuilder, target, method);
        if (params.length > 0) {
            // 参数值
            for (Object object : params) {
                if (object == null) {
                    strBuilder.append("null").append(comma);
                    continue;
                }
                Class<?> objectClass = object.getClass();
                if (ClassUtil.isSimpleValueType(objectClass)) {
                    strBuilder.append(object);
                } else if (Collection.class.isAssignableFrom(objectClass) || objectClass.isArray()) {
                    strBuilder.append(JSONUtil.toJsonStr(object));
                } else if (objectClass == BaseQuery.class) {
                    if (!isAddUserId(target, method)) {
                        strBuilder.append("userId:").append(SystemUtil.getInstance().getCurrentUserId()).append(":");
                    }
                    BaseQuery object1 = (BaseQuery) object;
                    strBuilder.append("pageNum:").append(object1.getPageNum()).append(comma)
                            .append("pageSize:").append(object1.getPageSize()).append(comma)
                            .append("param:").append(object1.getParam());
                } else {
                    String bytes = DigestUtil.md5Hex(JSONUtil.toJsonStr(object));
                    strBuilder.append(bytes);
                }
                strBuilder.append(comma);
            }
        } else {
            strBuilder.append("userId:").append(SystemUtil.getInstance().getCurrentUserId());
        }
        return strBuilder.toString();
    }

    private boolean isAddUserId(Object target, Method method) {
        CacheKeyUserId cacheKeyUserId = target.getClass().getAnnotation(CacheKeyUserId.class);
        if (Objects.isNull(cacheKeyUserId)) {
            cacheKeyUserId = method.getAnnotation(CacheKeyUserId.class);
            return Optional.ofNullable(cacheKeyUserId).map(t -> true).orElse(false);
        }
        return true;
    }

    private void addUserId(StringBuilder strBuilder, Object target, Method method) {
        if (isAddUserId(target, method)) {
            strBuilder.append("userId:").append(SystemUtil.getInstance().getCurrentUserId()).append(":");
        }
    }
}
