package com.crlink.smartpark.system.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.entity.Dictionary;
import com.crlink.smartpark.system.vo.DictVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 * */
public interface DictionaryService extends IService<Dictionary> {


    /**
     * 根据类型查询字典数据
     *
     * @param types
     * @return
     */
    Map<String, Object> listDictionaryByTypes(List<String> types);

    Map<String, Map<String, DictVO>> initDictionary();
}
