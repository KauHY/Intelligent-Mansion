package com.crlink.smartpark.system.vo;

import lombok.Data;

/**
 * @ClassName: SortDictVO
 * @Package com.crlink.smartpark.system.vo
 * @Description: 字典表-顺序版
 * @Version 1.0
 */
@Data
public class SortDictVO extends DictVO {
    /**
     * 排序
     */
    private Integer sort;

    public SortDictVO() {
    }

    public SortDictVO(Object value, String label, Integer sort) {
        super(label, value);
        this.sort = sort;
    }
}
