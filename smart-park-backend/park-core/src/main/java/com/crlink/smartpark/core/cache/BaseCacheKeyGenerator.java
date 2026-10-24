package com.crlink.smartpark.core.cache;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

//@Component
public class BaseCacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        //char dot = '.';
        //StringBuilder strBuilder = new StringBuilder(64);
        //// 类名
        //strBuilder.append(target.getClass().getSimpleName());
        //strBuilder.append(dot);
        //// 方法名
        //strBuilder.append(method.getName());
        //strBuilder.append(dot);
        //addUserId(strBuilder, target, method);
        //if (params.length > 0) {
        //    // 参数值
        //    for (Object object : params) {
        //        Class<?> objectClass = object.getClass();
        //        if (ClassUtil.isSimpleValueType(objectClass)) {
        //            strBuilder.append(object);
        //        } else if (Collection.class.isAssignableFrom(objectClass) || objectClass.isArray()) {
        //            strBuilder.append(JSONUtil.toJsonStr(object));
        //        } else if (objectClass == BaseQuery.class) {
        //            if (!isAddUserId(target, method)) {
        //                strBuilder.append("userId:").append(SystemUtil.getInstance().getCurrentUserId()).append(":");
        //            }
        //            BaseQuery object1 = (BaseQuery) object;
        //            strBuilder.append("pageNum:").append(object1.getPageNum()).append(comma)
        //                    .append("pageSize:").append(object1.getPageSize()).append(comma)
        //                    .append("param:").append(object1.getParam());
        //        } else {
        //            String bytes = DigestUtil.md5Hex(JSONUtil.toJsonStr(object));
        //            strBuilder.append(JSONUtil.toJsonStr(object).hashCode());
        //        }
        //        strBuilder.append(comma);
        //    }
        //} else {
        //    strBuilder.append("userId:").append(SystemUtil.getInstance().getCurrentUserId());
        //}
        //return strBuilder.toString();
        return null;
    }
}

