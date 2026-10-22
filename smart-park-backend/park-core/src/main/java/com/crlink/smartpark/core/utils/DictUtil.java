package com.crlink.smartpark.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.system.vo.DictVO;
import com.google.common.base.CaseFormat;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DictUtil {


    public static final LoadingCache<String, Map<String, DictVO>> DICT_CACHE = CacheBuilder
            .newBuilder()
            .maximumSize(4096)
            .build(new CacheLoader<String, Map<String, DictVO>>() {
                @Override
                public Map<String, DictVO> load(String s) {
                    return new HashMap(2);
                }
            });

    private static RedisUtil<Map<String, DictVO>> redisUtil;
    //private static RedisUtil<List<DictBusinessVO>> redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        DictUtil.redisUtil = redisUtil;
    }

    /**
     * 通过类型和值获取文案
     *
     * @param dictEnum DictConstant枚举类
     * @return
     */
    public static <T extends Enum<T>> Integer getValue(Enum<T> dictEnum) {
        String type = camelCaseUtil(dictEnum.getClass().getSimpleName());
        String code = dictEnum.name();
        return getValueByTypeAndCode(type, code);
    }

    /**
     * 通过类型和code获取文案
     *
     * @param clazz DictConstant枚举类名
     * @param value 值
     * @param <T>
     * @return
     */
    public static <T extends Enum<T>> String getLabel(Class<T> clazz, Integer value) {
        String type = camelCaseUtil(clazz.getSimpleName());
        return getLabelByTypeAndValue(type, value);
    }

    /**
     * 通过类型获取字典列表
     *
     * @param clazz
     * @return
     */
    public static <T extends Enum<T>> Map<String, DictVO> getByType(Class<T> clazz) {
        String type = camelCaseUtil(clazz.getSimpleName());
        return getByType(type);
    }

    /**
     * 通过类型和编码获取字典值
     *
     * @param type
     * @param code
     * @return
     */
    public static Integer getValueByTypeAndCode(String type, String code) {
        try {
            Map<String, DictVO> dictVOMap = getByType(type);
            return (Integer) dictVOMap.get(code).getValue();
        } catch (Exception e) {
            log.error("字典获取异常！type:{},code:{},异常信息:", type, code, e);
            throw new ResolinkRuntimeException("10006");
        }
    }

    /**
     * 通过类型和值获取文案
     *
     * @param type
     * @param value
     * @return
     */
    public static String getLabelByTypeAndValue(String type, Integer value) {
        try {
            if (value == null) {
                return StrUtil.EMPTY;
            }
            Map<String, DictVO> dictVOMap = getByType(type);
            return dictVOMap.values().stream().filter(e -> e.getValue().equals(value)).findFirst().get().getLabel();
        } catch (Exception e) {
            log.error("字典获取异常！type:{},value:{}，异常信息:", type, value, e);
            throw new ResolinkRuntimeException("10006");
        }
    }

    public static Map<Integer, String> getLabels(String type) {
        Map<String, DictVO> dictVOMap = null;
        try {
            dictVOMap = getByType(type);
            Map<Integer, String> result = dictVOMap.values().stream().collect(Collectors.toMap(t -> (Integer) t.getValue(), DictVO::getLabel));
            return result;
        } catch (Exception e) {
            log.error("字典获取异常！type:{},dictVOMap:{}，异常信息:", type, dictVOMap, e);
            throw new ResolinkRuntimeException("10006");
        }
    }

    public static Map<String, Integer> getValues(String type) {
        Map<String, DictVO> dictVOMap = null;
        try {
            dictVOMap = getByType(type);
            Map<String, Integer> result = dictVOMap.values().stream().collect(Collectors.toMap(DictVO::getLabel, t -> (Integer) t.getValue()));
            return result;
        } catch (Exception e) {
            log.error("字典获取异常！type:{},dictVOMap:{}，异常信息:", type, dictVOMap, e);
            throw new ResolinkRuntimeException("10006");
        }
    }

    /**
     * 通过类型获取字典列表
     * Map<code,DictVO>
     *
     * @param type
     * @return map 的key 为code
     */


    public static Map<String, DictVO> getByType(String type) {
        try {
            Map<String, DictVO> dictMap = DICT_CACHE.get(type);
            if (CollUtil.isEmpty(dictMap)) {
                dictMap = redisUtil.getObject(Constants.Redis.DICTIONARY_TYPE_PREFIX.getBuildCode(type));
                Map<String, DictVO> finalDictMap = dictMap;
                CompletableFuture.runAsync(() -> {
                    DICT_CACHE.put(type, finalDictMap);
                });
            }
            if (CollUtil.isEmpty(dictMap)) {
                throw new ResolinkRuntimeException("10006");
            }
            return dictMap;
        } catch (Exception e) {
            log.error("字典获取异常！type:{},code:{},异常信息:", type, e);
            throw new ResolinkRuntimeException("10006");
        }
    }

    //驼峰转下划线并大写
    private static String camelCaseUtil(String str) {
        String resultStr = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, str);
        return resultStr;
    }

}
