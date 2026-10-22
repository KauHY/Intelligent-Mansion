package com.crlink.smartpark.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.entity.NumberSeq;
import com.crlink.smartpark.common.mapper.NumberSeqMapper;
import com.crlink.smartpark.common.service.NumberSeqService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import org.springframework.stereotype.Service;


/**
 * 编号生成器 服务实现类
 * */
@Service
public class NumberSeqServiceImpl extends ServiceImpl<NumberSeqMapper, NumberSeq> implements NumberSeqService {

    @Override
    public NumberSeqDTO getNumberSeq(String numberName) {
        NumberSeq numberSeq = baseMapper.getNumberSeq(numberName);
        NumberSeqDTO numberSeqDTO = MyBeanUtils.copy(numberSeq, NumberSeqDTO::new);
        return numberSeqDTO;
    }
}
