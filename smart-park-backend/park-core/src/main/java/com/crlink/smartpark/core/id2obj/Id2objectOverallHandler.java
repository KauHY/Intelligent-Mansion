package com.crlink.smartpark.core.id2obj;

import java.util.Map;
import java.util.Set;

public abstract class Id2objectOverallHandler {

    /**
     * 通过数据编码和id集合查询文案集合
     *
     * @param code
     * @param ids
     * @return
     */
    protected abstract Map<Object, Object> ids2objsByCode(String code, Set<Object> ids);

    /**
     * 封装成单个数据处理器
     *
     * @param code
     * @return
     */
    public Id2objectSingleHandler buildId2objSingleHandler(String code) {
        return new Id2objectSingleHandler() {
            @Override
            public String code() {
                return code;
            }

            @Override
            public Map<Object, Object> ids2Objects(Set<Object> ids) {
                return ids2objsByCode(code(), ids);
            }
        };
    }
}
