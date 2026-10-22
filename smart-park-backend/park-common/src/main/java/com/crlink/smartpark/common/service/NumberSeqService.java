package com.crlink.smartpark.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.entity.NumberSeq;

/**
 * <p>
 * 编号生成器 服务类
 * </p>
 * */
public interface NumberSeqService extends IService<NumberSeq> {

    NumberSeqDTO getNumberSeq(String numberName);
}
