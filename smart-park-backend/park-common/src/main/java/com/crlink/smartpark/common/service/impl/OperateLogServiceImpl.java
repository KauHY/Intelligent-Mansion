package com.crlink.smartpark.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.dto.OperateLogDTO;
import com.crlink.smartpark.common.entity.OperateLog;
import com.crlink.smartpark.common.mapper.OperateLogMapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.model.query.OperateLogQuery;
import com.crlink.smartpark.common.service.OperateLogService;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * 操作日志 服务实现类
 * */
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {


    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOperateLog(OperateLogDTO reqDTO) {
        OperateLog entity = MyBeanUtils.copy(reqDTO, OperateLog::new);
        return this.save(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public OperateLogDTO getOperateLogById(String id) {
        OperateLog entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<OperateLogDTO> listOperateLogPage(OperateLogQuery query) {
        query.startPage();
        List<OperateLog> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<OperateLogDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<OperateLog> getConditionWrapper(OperateLogQuery query) {
        LambdaQueryWrapper<OperateLog> queryWrapper = new LambdaQueryWrapper<OperateLog>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<OperateLogDTO> entitiesToDTOs(List<OperateLog> entities) {
        return MyBeanUtils.copyList(entities, OperateLogDTO::new, (s, t) -> {

        });
    }
}
