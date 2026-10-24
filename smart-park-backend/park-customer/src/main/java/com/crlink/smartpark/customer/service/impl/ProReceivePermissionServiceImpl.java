package com.crlink.smartpark.customer.service.impl;

import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.controller.request.ProReceivePermissionAddDTO;
import com.crlink.smartpark.customer.entity.ProReceivePermission;
import com.crlink.smartpark.customer.controller.request.query.ProReceivePermissionQuery;
import com.crlink.smartpark.customer.dto.ProReceivePermissionDTO;
import com.crlink.smartpark.customer.model.dto.ReceivePermission;
import com.crlink.smartpark.customer.model.vo.ReceivePermissionVo;
import com.crlink.smartpark.customer.service.ProReceivePermissionService;
import com.crlink.smartpark.customer.mapper.ProReceivePermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import cn.hutool.core.collection.CollectionUtil;

import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 领用权限设置 服务实现类
 * */
@Service
public class ProReceivePermissionServiceImpl extends ServiceImpl<ProReceivePermissionMapper, ProReceivePermission> implements ProReceivePermissionService {

    @Autowired
    private CommonProvider commonProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProReceivePermission, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ProReceivePermission> lambdaQuery = this.lambdaQuery().in(ProReceivePermission::getId, ids);
        if (flag) {
            lambdaQuery.select(ProReceivePermission::getId, sValue);
        }
        Stream<ProReceivePermission> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ProReceivePermission::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ProReceivePermission::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateProReceivePermission(ProReceivePermissionAddDTO reqDTO) {
        ProReceivePermission proReceivePermission = this.lambdaQuery().eq(ProReceivePermission::getParkOrgId, reqDTO.getParkOrgId()).one();
        if (proReceivePermission == null) {
            proReceivePermission = MyBeanUtils.copy(reqDTO, ProReceivePermission::new);
        } else {
            if (StringUtils.isNotBlank(reqDTO.getCompanyId())) {
                proReceivePermission.setCompanyId(reqDTO.getCompanyId());
            } else {
                proReceivePermission.setCompanyId(null);
            }
            if (StringUtils.isNotBlank(reqDTO.getEmployeeId())) {
                proReceivePermission.setEmployeeId(reqDTO.getEmployeeId());
            } else {
                proReceivePermission.setEmployeeId(null);
            }
        }
        EntityUserUtil.setAllDynamic(proReceivePermission);
        return this.saveOrUpdate(proReceivePermission);
    }

    @Override
    public Boolean auth(String parkOrgId) {
        ProReceivePermission proReceivePermission = this.lambdaQuery().eq(ProReceivePermission::getParkOrgId, parkOrgId).one();
        if (proReceivePermission != null) {
            if (StringUtils.isNotBlank(proReceivePermission.getEmployeeId())) {
                if (proReceivePermission.getEmployeeId().contains(SystemUtil.getInstance().getEmployeeId())) {
                    return true;
                }
            }
            if (StringUtils.isNotBlank(proReceivePermission.getCompanyId())) {
                if (proReceivePermission.getCompanyId().contains(SystemUtil.getInstance().getEmployeeCompanyId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    public PageInfo<ProReceivePermissionDTO> listProReceivePermissionPage(ProReceivePermissionQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            return new PageInfo<>();
        }
        Map<String, ReceivePermission> receivePermissionMap = this.lambdaQuery().in(ProReceivePermission::getParkOrgId, parkOrgIds).list()
                .parallelStream().collect(Collectors.toMap(ProReceivePermission::getParkOrgId, t -> new ReceivePermission(t.getCompanyId(), t.getEmployeeId())));
        Map<String, String> parkOrgNames = commonProvider.getOrgNamesByOrgIds(parkOrgIds);
        List<ReceivePermissionVo> permissionVos = new ArrayList<>();
        parkOrgIds.forEach(parkOrgId -> {
            ReceivePermissionVo permissionVo = new ReceivePermissionVo();
            permissionVo.setParkOrgId(parkOrgId);
            permissionVo.setParkOrgName(parkOrgNames.get(parkOrgId));
            ReceivePermission receivePermission = receivePermissionMap.get(parkOrgId);
            if (receivePermission != null) {
                if (StringUtils.isNotBlank(receivePermission.getCompanyId())) {
                    permissionVo.setCompanyId(receivePermission.getCompanyId());
                    Map<String, String> companyNameMap = commonProvider.getNamesByIdsAndType(new HashSet<>(Arrays.asList(receivePermission.getCompanyId().split(","))), SimpleNode.TypeEnum.COMPANY.getCode());
                    permissionVo.setCompanyName(StringUtils.join(companyNameMap.values().toArray(), ","));
                }
                if (StringUtils.isNotBlank(receivePermission.getEmployeeId())) {
                    permissionVo.setEmployeeId(receivePermission.getEmployeeId());
                    Set<String> ids = new HashSet<>(Arrays.asList(receivePermission.getEmployeeId().split(",")));
                    Map<String, String> employeeNameMap = commonProvider.getNamesByIdsAndType(ids, SimpleNode.TypeEnum.EMPLOYEE.getCode());
                    permissionVo.setEmployeeName(StringUtils.join(employeeNameMap.values().toArray(), ","));
                    List<DictVO> list = ids.stream().map(e -> new DictVO(employeeNameMap.get(e), e)).collect(Collectors.toList());
                    permissionVo.setEmployeeIdVo(list);
                }
            }
            permissionVos.add(permissionVo);
        });
        query.startPage();
        MyPageInfoUtils.isEmptyPage(permissionVos);
        return query.createPageInfo(permissionVos);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ProReceivePermission> getConditionWrapper(ProReceivePermissionQuery query) {
        LambdaQueryWrapper<ProReceivePermission> queryWrapper = new LambdaQueryWrapper<ProReceivePermission>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ProReceivePermissionDTO> entitiesToDTOs(List<ProReceivePermission> entities) {
        return MyBeanUtils.copyList(entities, ProReceivePermissionDTO::new, (s, t) -> {

        });
    }
}
