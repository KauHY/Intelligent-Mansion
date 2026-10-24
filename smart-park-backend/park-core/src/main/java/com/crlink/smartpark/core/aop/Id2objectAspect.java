package com.crlink.smartpark.core.aop;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.annotations.Id2objectNestingField;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.id2obj.Id2objectHandlerManager;
import com.crlink.smartpark.core.id2obj.Id2objectSingleHandler;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Aspect
@Order
@Component
public class Id2objectAspect {

    private final static String DEFAULT_NAME_SUFFIX = "Name";

    private final static String DEFAULT_TEXT_SUFFIX = "Text";

    private final static String DEFAULT_ID_SUFFIX = "Id";

    /**
     * 内存缓存
     */
    private ThreadLocal<Map<String, Map<Object, Object>>> id2ObjectCache = new ThreadLocal<>();

    /**
     * 类型映射ids缓存
     */
    private ThreadLocal<Map<String, Set<Object>>> codeIdsCache = new ThreadLocal<>();

    @Pointcut("@annotation(com.crlink.smartpark.common.annotations.Id2objHandle)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object translation(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        if (result == null) {
            return null;
        }
        //类型id集合map缓存
        codeIdsCache.set(MapUtil.newHashMap());
        //id转名字map缓存
        id2ObjectCache.set(MapUtil.newHashMap());
        ResponseResult res = null;
        if (result instanceof ResponseResult) {
            res = (ResponseResult) result;
            result = res.getData();
        }
        //判断响应数据类型
        if (result instanceof Collection) {
            handleCollection((Collection) result);
        } else if (result instanceof PageInfo) {
            handleCollection(((PageInfo) result).getList());
        } else {
            handleSingle(result);
        }
        //移除缓存防止内存泄露
        id2ObjectCache.remove();
        codeIdsCache.remove();
        if (res != null) {
            res.setData(result);
            return res;
        }
        return result;
    }

    /**
     * 处理单个实体
     *
     * @param result
     */
    private void handleSingle(Object result) {
        if (result == null) {
            return;
        }
        List<FieldInfo> fieldInfoList = findFieldInfo(result);
        createTypeAndIdsMap(fieldInfoList, result);
        handleDict(fieldInfoList, result);
    }

    /**
     * 处理集合类型实体（循环处理）
     *
     * @param result
     */
    private void handleCollection(Collection result) {
        if (CollUtil.isEmpty(result)) {
            return;
        }
        //取第一个实体获取属性信息
        Object firstBean = getOneObj(result);
        List<FieldInfo> fieldInfoList = findFieldInfo(firstBean);
        for (Object bean : result) {
            //获取需要查询的所有类型的数据和id集合
            createTypeAndIdsMap(fieldInfoList, bean);
        }
        for (Object bean : result) {
            handleDict(fieldInfoList, bean);
        }

    }

    /**
     * 单个设置值
     *
     * @param obj
     */
    private void handleDict(List<FieldInfo> fieldInfoList, Object obj) {
        if (CollUtil.isEmpty(fieldInfoList)) {
            return;
        }
        for (FieldInfo fieldInfo : fieldInfoList) {
            //嵌套对象
            if (fieldInfo.isNestingField) {
                Object objResult = ReflectUtil.getFieldValue(obj, fieldInfo.targetFieldName);
                //判断响应数据类型
                if (objResult instanceof Collection) {
                    handleCollection((Collection) objResult);
                } else {
                    handleSingle(objResult);
                }
                continue;
            }
            //获取id值
            Object idValue = ReflectUtil.getFieldValue(obj, fieldInfo.idFieldName);
            if (idValue == null) {
                continue;
            }
            //通过id获取到的对象
            Object targetFieldValue = null;
            if (!fieldInfo.isCollection) {
                Object objById = getByCodeAndId(fieldInfo.code, idValue);
                if (objById != null) {
                    //目标值
                    targetFieldValue = isBaseObject(objById) ? objById : ReflectUtil.getFieldValue(objById, fieldInfo.objFieldName);
                }
            } else {
                targetFieldValue = idValue;
                Collection collection = (Collection) idValue;
                Object first = CollectionUtil.getFirst(collection);
                if (first instanceof DictVO) {
                    for (Object o : collection) {
                        setLabelName(fieldInfo.code, o);
                    }
                } else if (isBaseObject(first)) {
                    Collection targetCollection = (Collection) ReflectUtil.newInstance(idValue.getClass());
                    for (Object o : collection) {
                        setName(targetCollection, fieldInfo.code, o);
                    }
                    //直接
                    targetFieldValue = targetCollection;
                }
            }
            //设置值
            ReflectUtil.setFieldValue(obj, fieldInfo.targetFieldName, targetFieldValue);
        }
    }
    //
    //private void setIdValue(String code, Object idValue) {
    //    Collection collection = (Collection) idValue;
    //    Object first = CollectionUtil.getFirst(collection);
    //    if (first instanceof DictVO) {
    //        for (Object o : collection) {
    //            setLabelName(code, o);
    //        }
    //    } else if (isBaseObject(first)) {
    //        Collection targetCollection = (Collection) ReflectUtil.newInstance(idValue.getClass());
    //
    //    }
    //}


    private void setLabelName(String code, Object idValue) {
        DictVO dictVO = (DictVO) idValue;
        String label = (String) getByCodeAndId(code, dictVO.getValue());
        dictVO.setLabel(label);
    }

    private void setName(Collection collection, String code, Object idValue) {
        String name = (String) getByCodeAndId(code, idValue);
        collection.add(name);
    }

    private Object getOneObj(Collection collection) {
        if (collection.stream().findFirst().isPresent()) {
            return collection.stream().findFirst().get();
        }
        return null;
    }

    private void createTypeAndIdsMap(List<FieldInfo> fieldInfoList, Collection collection) {
        if (collection == null) {
            return;
        }
        for (Object object : collection) {
            createTypeAndIdsMap(fieldInfoList, object);
        }
    }

    /**
     * 单个设置值
     *
     * @param obj
     */
    private void createTypeAndIdsMap(List<FieldInfo> fieldInfoList, Object obj) {
        if (CollUtil.isEmpty(fieldInfoList)) {
            return;
        }
        for (FieldInfo fieldInfo : fieldInfoList) {
            //递归捞出所有目标id
            if (fieldInfo.isNestingField) {
                Object nestObject = ReflectUtil.getFieldValue(obj, fieldInfo.targetFieldName);
                if (nestObject instanceof Collection) {
                    List<FieldInfo> fieldInfos = findFieldInfo(getOneObj((Collection) nestObject));
                    createTypeAndIdsMap(fieldInfos, (Collection) nestObject);
                } else {
                    List<FieldInfo> fieldInfos = findFieldInfo(nestObject);
                    createTypeAndIdsMap(fieldInfos, nestObject);
                }
                continue;
            }
            Object idValue = ReflectUtil.getFieldValue(obj, fieldInfo.idFieldName);
            if (idValue != null) {
                if (fieldInfo.isCollection) {
                    Collection collection = (Collection) idValue;
                    for (Object o : collection) {
                        Object id = null;
                        if (o instanceof DictVO) {
                            DictVO dictVO = (DictVO) o;
                            id = dictVO.getValue();
                        } else if (isBaseObject(o)) {
                            id = o;
                        }
                        setCodesCache(fieldInfo.code, id);
                    }
                } else {
                    setCodesCache(fieldInfo.code, idValue);
                }
            }
        }
    }

    private void setCodesCache(String code, Object id) {
        if (codeIdsCache.get().containsKey(code)) {
            codeIdsCache.get().get(code).add(id);
        } else {
            codeIdsCache.get().put(code, Sets.newHashSet(id));
        }
    }


    /**
     * 通过类型和值获取文案（缓存）
     *
     * @param code
     * @param value
     * @return
     */
    private Object getByCodeAndId(String code, Object value) {
        if (id2ObjectCache.get().containsKey(code)) {
            return id2ObjectCache.get().get(code).get(value);
        }
        Id2objectSingleHandler handler = Id2objectHandlerManager.getHandler(code);
        if (handler != null) {
            Map<Object, Object> sourceDictMap = handler.ids2Objects(codeIdsCache.get().get(code));
            id2ObjectCache.get().put(code, sourceDictMap);
            return sourceDictMap.get(value);
        }
        return null;
    }

    /**
     * 获取类中需要翻译字典的属性值
     *
     * @param obj
     * @return
     */
    private List<FieldInfo> findFieldInfo(Object obj) {
        if (obj == null) {
            return Lists.newArrayList();
        }
        List<FieldInfo> result = CollUtil.newArrayList();
        List<Field> fields = getClassAllField(obj.getClass());
        for (Field field : fields) {
            FieldInfo fieldInfo = new FieldInfo();
            Id2objectField dataDict = AnnotationUtil.getAnnotation(field, Id2objectField.class);
            if (dataDict != null) {
                Object fieldValue = ReflectUtil.getFieldValue(obj, field);
                //数据编码
                String code = dataDict.code();
                //当前赋值的目标字段名
                String targetFieldName = field.getName();
                //ID值字段名
                String idFieldName = dataDict.idField();
                //ID获取到的对象的对应字段名
                String objFieldName = dataDict.objField();
                fieldInfo.code = code;
                fieldInfo.targetFieldName = targetFieldName;
                fieldInfo.objFieldName = objFieldName;
                if (null == fieldValue && ClassUtil.isSimpleValueType(field.getType())) {
                    //如果不设值字典文案字段名添加默认后缀
                    if (StrUtil.isBlank(idFieldName)) {
                        idFieldName = convertToDefaultIdFieldName(targetFieldName);
                    }
                }
                if (null != fieldValue && isCollection(fieldValue)) {
                    fieldInfo.isCollection = true;
                    idFieldName = targetFieldName;
                }
                fieldInfo.idFieldName = idFieldName;
                //当Id2objectField注解标注在id字段上
                if (StrUtil.isNotBlank(dataDict.targetFieldName())) {
                    fieldInfo.targetFieldName = dataDict.targetFieldName();
                    fieldInfo.idFieldName = field.getName();
                }
                result.add(fieldInfo);
            } else if (AnnotationUtil.getAnnotation(field, Id2objectNestingField.class) != null) {
                fieldInfo.targetFieldName = field.getName();
                fieldInfo.isNestingField = true;
                result.add(fieldInfo);
            }
        }
        return result;
    }

    private static boolean isCollection(Object fieldValue) {
        return fieldValue instanceof Collection;
    }

    private List<Field> getClassAllField(Class<?> clazz) {
        List<Field> fields = Lists.newArrayList();
        while (clazz != null && !clazz.getName().toLowerCase().equals("java.lang.object")) {
            CollectionUtil.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }


    /**
     * 判断一个对象是否是基本类型或基本类型的封装类型
     */
    private static boolean isBaseObject(Object obj) {
        return obj instanceof Number ||
                obj instanceof String ||
                obj instanceof Boolean;
    }

    private static String convertToDefaultIdFieldName(String targetFieldName) {
        if (StrUtil.endWith(targetFieldName, DEFAULT_NAME_SUFFIX)) {
            return StrUtil.subBefore(targetFieldName, DEFAULT_NAME_SUFFIX, true) + DEFAULT_ID_SUFFIX;
        }
        if (StrUtil.endWith(targetFieldName, DEFAULT_TEXT_SUFFIX)) {
            return StrUtil.subBefore(targetFieldName, DEFAULT_TEXT_SUFFIX, true) + DEFAULT_ID_SUFFIX;
        }
        return StrUtil.EMPTY;
    }


    public static class FieldInfo {
        /**
         * 数据类型编码
         */
        public String code;
        /**
         * id所在字段名
         */
        public String idFieldName;
        /**
         * id获取到的对象所需的字段名
         */
        public String objFieldName;
        /**
         * 当前目标需要翻译的字段名
         */
        public String targetFieldName;

        public boolean isCollection;

        /**
         * 是否嵌套字段
         */
        public boolean isNestingField;

    }

}
