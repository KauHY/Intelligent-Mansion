package com.crlink.smartpark.system.service;

import com.crlink.smartpark.system.entity.CompanyInformation;
import com.crlink.smartpark.system.entity.CompanyInformationAudit;
import com.crlink.smartpark.system.model.query.CompanyInfoAuditDTO;
import com.crlink.smartpark.system.model.query.CompanyInformationAuditQuery;
import com.crlink.smartpark.system.model.CompanyInformationAuditReqDTO;
import com.crlink.smartpark.system.dto.CompanyInformationAuditDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.Collection;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <p>
 * 企业信息审核 服务类
 * </p>
 * */
public interface CompanyInformationAuditService extends IService<CompanyInformationAudit> {

    /**
     * 根据Id查询值
     */
     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<CompanyInformationAudit, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateCompanyInformationAudit(CompanyInformationAuditReqDTO reqDTO);

    /**
     *  企业信息详情查询
     *
     * @return   实体对象
     */
    CompanyInformation getDetail();

    /**
     *  获取当前最新的一条审核记录
     *
     * @return   实体对象
     */
    CompanyInformationAudit getLastAudit();

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    CompanyInformationAuditDTO getCompanyInformationAuditById(String id);

    /**
     *  取消申请
     *
     * @param id 查询条件
     * @return   实体对象
     */
    Boolean cancel(String id);

    /**
     *  审核
     *
     * @param auditDTO 查询条件
     * @return   实体对象
     */
    Boolean audit(CompanyInfoAuditDTO auditDTO);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<CompanyInformationAuditDTO> listCompanyInformationAuditPage(CompanyInformationAuditQuery query);
}
