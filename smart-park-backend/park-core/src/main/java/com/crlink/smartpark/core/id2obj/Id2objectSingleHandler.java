package com.crlink.smartpark.core.id2obj;

import java.util.Map;
import java.util.Set;

public interface Id2objectSingleHandler {

    /**
     * 数据编码
     *
     * @return
     */
    String code();

    /**
     * 通过id集合查询目标对象集合
     *
     * @param ids
     * @return
     */
    Map<Object, Object> ids2Objects(Set<Object> ids);
}
