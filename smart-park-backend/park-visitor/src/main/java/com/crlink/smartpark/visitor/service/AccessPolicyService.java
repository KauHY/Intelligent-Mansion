package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.controller.request.StatusReqDTO;
import com.crlink.smartpark.visitor.dto.AccessPolicySaveDTO;
import com.crlink.smartpark.visitor.dto.AccessPolicyUpdateDTO;
import com.crlink.smartpark.visitor.entity.AccessPolicy;
import com.crlink.smartpark.visitor.query.AccessPolicyQuery;
import com.crlink.smartpark.visitor.vo.AccessPolicyListVO;
import com.crlink.smartpark.visitor.vo.AccessPolicyVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 人行通行策略表 服务类
 * </p>
 * */
public interface AccessPolicyService extends IService<AccessPolicy> {


    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    AccessPolicyVO getAccessPolicyById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<AccessPolicyListVO> listPage(AccessPolicyQuery condition);

    /**
     * 新增
     *
     * @param dto
     */
    void save(AccessPolicySaveDTO dto);

    /**
     * 新增通行策略
     * 跟产品确认，新增时默认禁用状态，所以这里不做授权操作
     *
     * @param dto
     * @date 2022-11-01     */
    void save2(AccessPolicySaveDTO dto);

    /**
     * 更新
     *
     * @param dto
     */
    void update(AccessPolicyUpdateDTO dto);

    /**
     * 编辑通行策略
     * 区别：
     * 1、相比以前比较整个园区的通行策略，该方法只比较当前通行策略；
     * 2、使用增量授权方式授权；
     *
     * @param dto
     * @date 2022-11-01     */
    void update2(AccessPolicyUpdateDTO dto);

    /**
     * 获取员工卡号集合
     *
     * @param companyIds
     * @param tagIds
     * @date 2022-11-01     */
    Set<String> getEmployeeCardNos(Set<String> companyIds, Set<String> tagIds);

    /**
     * 删除授权
     *
     * @param deviceIds
     * @param cardNos
     * @date 2022-11-01     */
    void removeAuth(Set<String> deviceIds, Set<String> cardNos);

    /**
     * 刷新策略员工信息
     *
     * @param accessPolicy
     * @date 2022-11-25     */
    void refreshPolicyEmployee(AccessPolicy accessPolicy);

    /**
     * 根据公司ID获取访客通行策略
     *
     * @return
     */
    List<DictVO> listAccessPolicyByCompany(String companyId);

    /**
     * 通过员工信息获取可通行设备ID（人行/车行）
     *
     * @param employeeCompanyId 员工公司ID
     * @param employeeTagIds    员工标签ID集合
     * @return
     */
    Set<String> getAccessDeviceIdsByEmployeeInfo(String employeeCompanyId, Collection<String> employeeTagIds);

    Boolean getTagIdsExist(String employeeTagId);

    Boolean delAccessPolicy(String id);

    Boolean updateStatus(StatusReqDTO dto);

    List<AccessPolicy> getAllByParkOrgId(String parkOrgId);

    /**
     * 原AccessPolicyDeviceService的方法
     *
     * @param beforeAccessPolicies
     * @param parkOrgId
     */
    void refreshAccessPolicy(List<AccessPolicy> beforeAccessPolicies, String parkOrgId);

    boolean existsDevice(String deviceId);

}
