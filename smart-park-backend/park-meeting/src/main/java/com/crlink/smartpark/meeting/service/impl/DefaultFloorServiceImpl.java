package com.crlink.smartpark.meeting.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.meeting.entity.DefaultFloor;
import com.crlink.smartpark.meeting.controller.request.query.DefaultFloorQuery;
import com.crlink.smartpark.meeting.dto.DefaultFloorDTO;
import com.crlink.smartpark.meeting.model.dto.DefaultFloorReqDTO;
import com.crlink.smartpark.meeting.service.DefaultFloorService;
import com.crlink.smartpark.meeting.mapper.DefaultFloorMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collections;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 会议室默认楼层 服务实现类
 * */
@Service
public class DefaultFloorServiceImpl extends ServiceImpl<DefaultFloorMapper, DefaultFloor>
    implements DefaultFloorService {

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<DefaultFloor, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<DefaultFloor> lambdaQuery = this.lambdaQuery().in(DefaultFloor::getId, ids);
        if (flag) {
            lambdaQuery.select(DefaultFloor::getId, sValue);
        }
        Stream<DefaultFloor> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(DefaultFloor::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(DefaultFloor::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateDefaultFloor(DefaultFloorReqDTO reqDTO) {
        DefaultFloor entity = MyBeanUtils.copy(reqDTO, DefaultFloor::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     *
     */
    @Override
    public DefaultFloorDTO getDefaultFloorById(String id) {
        DefaultFloor entity =
            Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<DefaultFloorDTO> listDefaultFloorPage(DefaultFloorQuery query) {
        query.startPage();
        List<DefaultFloor> list = null;
        MyPageInfoUtils.isEmptyPage(list);
        List<DefaultFloorDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public DefaultFloorDTO getDefaultFloor() {
        // 根据公司ID加员工ID拉取对应默认楼栋楼层
        String parkOrgId = SystemUtil.getInstance().getEmployeeParkOrgId();
        String curEmployeeId = SystemUtil.getInstance().getEmployeeId();
        DefaultFloor defaultFloor = this.lambdaQuery().eq(DefaultFloor::getParkOrgId, parkOrgId)
                .eq(DefaultFloor::getEmployeeId, curEmployeeId).one();

        if (ObjectUtil.isNotNull(defaultFloor)) {
            DefaultFloorDTO defaultFloorDTO = MyBeanUtils.copy (defaultFloor,DefaultFloorDTO::new);
            return defaultFloorDTO;
        }
        return null;
    }

    @Override
    public String settingDefaultFloor(DefaultFloorReqDTO reqDTO) {
        // 设置默认楼层 根据员工ID校验唯一性
        String curParkOrgId = SystemUtil.getInstance().getEmployeeParkOrgId();
        String curEmployeeId = SystemUtil.getInstance().getEmployeeId();
        String curCompanyId = SystemUtil.getInstance().getCurCompanyId ();
        DefaultFloor entity = MyBeanUtils.copy(reqDTO, DefaultFloor::new);

        DefaultFloor defaultFloor = this.lambdaQuery().eq(DefaultFloor::getParkOrgId, curParkOrgId)
            .eq(DefaultFloor::getEmployeeId, curEmployeeId).one();
        if (ObjectUtil.isNotNull(defaultFloor)) {
            entity.setId(defaultFloor.getId());
        }
        entity.setEmployeeId(curEmployeeId);
        entity.setParkOrgId (curParkOrgId);
        entity.setCompanyId (curCompanyId);
        EntityUserUtil.setAllDynamic(entity);
        this.saveOrUpdate(entity);
        return entity.getId();
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<DefaultFloor> getConditionWrapper(DefaultFloorQuery query) {
        LambdaQueryWrapper<DefaultFloor> queryWrapper = new LambdaQueryWrapper<DefaultFloor>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * 
     * @return DTO集合
     */
    private List<DefaultFloorDTO> entitiesToDTOs(List<DefaultFloor> entities) {
        return MyBeanUtils.copyList(entities, DefaultFloorDTO::new, (s, t) -> {

        });
    }
}
