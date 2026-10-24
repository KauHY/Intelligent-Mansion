package com.crlink.smartpark.system.service;

import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.CompanyAudit;
import com.crlink.smartpark.system.model.CompanyAuditDetailVo;
import com.crlink.smartpark.system.model.query.CompanyAuditQuery;
import com.crlink.smartpark.system.model.CompanyAuditReqDTO;
import com.crlink.smartpark.system.dto.CompanyAuditDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.model.query.CompanyInfoAuditDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 企业入驻审核 服务类
 * </p>
 * */
public interface CompanyAuditService extends IService<CompanyAudit> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<CompanyAudit, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateCompanyAudit(CompanyAuditReqDTO reqDTO);

    /**
     * 取消申请
     *
     * @return 实体对象
     */
    Boolean cancel(String id);

    /**
     * 是否入驻企业
     *
     * @return 实体对象
     */
    List<Company> settled();

    /**
     * 取消申请
     *
     * @return 实体对象
     */
    Boolean getFlag();

    /**
     * 审核
     *
     * @return 实体对象
     */
    Boolean audit(CompanyInfoAuditDTO auditDTO);

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    CompanyAuditDetailVo getCompanyAuditById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<CompanyAuditDTO> listCompanyAuditPage(CompanyAuditQuery query);
}
