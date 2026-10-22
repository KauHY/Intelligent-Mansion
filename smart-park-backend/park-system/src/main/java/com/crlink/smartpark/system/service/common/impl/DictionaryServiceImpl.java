package com.crlink.smartpark.system.service.common.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.system.entity.Dictionary;
import com.crlink.smartpark.system.mapper.DictionaryMapper;
import com.crlink.smartpark.system.service.common.DictionaryService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SortDictVO;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.Lint;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 * */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    public static final String GROUP_MAP = "GROUP_";

    @Override
    public Map<String, Object> listDictionaryByTypes(List<String> types) {
        if (CollectionUtil.isNotEmpty(types)) {
            return queryDictionary(types);
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Map<String, DictVO>> initDictionary() {

        Map<String, Map<String, DictVO>> result = queryDictionaryList(null)
                .parallelStream().collect(Collectors.groupingBy(Dictionary::getType, LinkedHashMap::new,
                        Collectors.toMap(Dictionary::getCode, t -> new DictVO(t.getLabel(), t.getValue()))));
        return result;
    }


    private List<Dictionary> queryDictionaryList(List<String> types) {
        List<Dictionary> list = this.lambdaQuery()
                .select(Dictionary::getLabel, Dictionary::getValue, Dictionary::getCode, Dictionary::getType, Dictionary::getParentId, Dictionary::getId, Dictionary::getSort)
                .in(ObjectUtils.isNotEmpty(types), Dictionary::getType, types).orderByAsc(Dictionary::getSort).list();
        return list;
    }

    private Map<String, Object> queryDictionary(List<String> types) {
        List<Dictionary> list = queryDictionaryList(types);
        Map<String, Object> result = Maps.newLinkedHashMap();
        Map<String, List<Dictionary>> typeGroup = list.parallelStream().collect(Collectors.groupingBy(Dictionary::getType, LinkedHashMap::new, Collectors.toList()));
        Map<String, Dictionary> idMap = list.parallelStream().collect(Collectors.toMap(Dictionary::getId, Function.identity()));

        typeGroup.forEach((k, v) -> {
            Map<String, List<Dictionary>> parentGroup = v.parallelStream().filter(t -> StringUtils.isNotBlank(t.getParentId()))
                    .collect(Collectors.groupingBy(Dictionary::getParentId, LinkedHashMap::new, Collectors.toList()));
            if (ObjectUtils.isNotEmpty(parentGroup)) {
                Map<Object, Object> subTypes = Maps.newHashMap();
                parentGroup.forEach((pid, children) -> {
                    Dictionary parentDictionary = idMap.get(pid);
                    List<DictVO> childList = children.parallelStream().sorted(Comparator.comparing(Dictionary::getSort)).map(c -> new SortDictVO(c.getValue(), c.getLabel(),c.getSort ())).collect(Collectors.toList());
                    DictVO parentNode = new DictVO(parentDictionary.getLabel(), childList);
                    subTypes.put(idMap.get(pid).getValue(), parentNode);
                });
                result.put(GROUP_MAP + k, subTypes);
            }
        });
//        Map<String, List<Dictionary>> valueList = list.parallelStream ().collect(Collectors.groupingBy(Dictionary::getType));
//        LinkedHashMap<String, LinkedHashMap<Object, DictVO>> valueMap = new LinkedHashMap<>();
//        valueList.forEach ((k,v)->{
//            LinkedHashMap<Object, DictVO> newMap = new LinkedHashMap<>();
//            v.forEach (e->{
//                newMap.put (e.getValue (), new DictVO (e.getLabel (),e.getValue ()));
//            });
//            valueMap.put(k, newMap);
//        });

        LinkedHashMap<String, Map<Object, SortDictVO>> valueMap = list.parallelStream().collect(Collectors.groupingBy(Dictionary::getType, LinkedHashMap::new,
                Collectors.mapping(t -> new SortDictVO (t.getValue(),t.getLabel(),t.getSort ()), Collectors.toMap(DictVO::getValue, Function.identity()))));
        result.putAll(valueMap);
        return result;
    }


}
