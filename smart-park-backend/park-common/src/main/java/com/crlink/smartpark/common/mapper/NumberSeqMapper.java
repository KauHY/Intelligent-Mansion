package com.crlink.smartpark.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.common.entity.NumberSeq;

/**
 * <p>
 * 编号生成器 Mapper 接口
 * </p>
 * */
public interface NumberSeqMapper extends BaseMapper<NumberSeq> {
    NumberSeq getNumberSeq(String getNumberName);
}
