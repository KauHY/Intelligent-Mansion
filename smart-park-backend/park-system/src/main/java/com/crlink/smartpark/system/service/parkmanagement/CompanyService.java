package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.system.dto.UpdateStatusReqDTO;
import com.crlink.smartpark.system.dto.company.CompanyBindUpdateDTO;
import com.crlink.smartpark.system.dto.company.CompanyQueryDTO;
import com.crlink.smartpark.system.dto.company.CompanySaveDTO;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.query.CompanyBindListQuery;
import com.crlink.smartpark.system.query.CompanyQuery;
import com.crlink.smartpark.system.vo.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 入驻企业表 服务类
 * </p>
 * */
public interface CompanyService extends IService<Company> {

    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<Company, ?> sValue);

    //Map<String, String> id2NameMap(Set<String> ids);


    Map<String, Company> id2CompanyMap(Set<String> ids, SFunction<Company, ?>... sValue);

    Map<String, Company> code2CompanyMap(Set<String> ids, SFunction<Company, ?>... sValue);


    PageInfo<CompanyListVO> listCompanyPage(CompanyQuery companyQuery);

    /**
     * 保存公司
     *
     * @param companySaveDTO
     */
    void saveOrUpdateCompany(CompanySaveDTO companySaveDTO);

    /**
     * 根据id获取详情
     *
     * @param id
     * @return
     */
    CompanyVO getCompanyById(String id);

    /**
     * 更新入驻企业房间绑定数量
     *
     * @param companyId
     * @param count
     */
    void updateHouseCount(String companyId, Integer count);

    /**
     * 根据园区ID获取公司名列表
     *
     * @param parkOrgId
     * @return
     */

    List<SimpleNode> listCompanyDictByParkOrgId(String parkOrgId, boolean filterUnBindClientInfo);

    /**
     * 企业绑定小程序或H5列表查询
     *
     * @param query
     * @return
     */
    PageInfo<CompanyBindListVO> pageBindList(CompanyBindListQuery query);

    /**
     * 修改企业客户端绑定相关信息
     *
     * @param dto
     */
    void updateClientInfo(CompanyBindUpdateDTO dto);

    /**
     * 客户端绑定关系删除
     *
     * @param companyId
     */
    void cleanClientBindInfo(String companyId);

    /**
     * 获取物业公司
     *
     * @param parkOrgId
     * @return
     */
    List<DictVO> listCompanyDictByType(String parkOrgId, Integer companyType);

    /**
     * 查询企业入驻的园区
     *
     * @param code
     * @return
     */
    List<DictVO> listParkDictByCode(String code);

    /**
     * 获取物业公司
     *
     * @param parkOrgId
     * @return
     */
    List<DictVO> listPropertyCompanyDictByParkOrgId(String parkOrgId);

    /**
     * 当前用户的物业公司
     *
     * @return
     */
    List<DictVO> curUserPropertyCompanyDict();

    /**
     * 通过名字查ID集合
     *
     * @param companyName
     * @return
     */
    Set<String> findIdsByName(String companyName);

    Set<String> findIdsByBusinessUserId(String businessUserId);

    CompanyClientInfoVO getClientBindInfo(String id);

    Map<String, Map<String, Object>> queryHouseColumns(CompanyQueryDTO dto);

    /**
     * 查询-导出
     * @param query:
     * @return  void
     * @MethodName export
     */
    void export(CompanyQuery query);

    /**
     * 运营大屏 企业总览
     *
     * @param parkOrgId
     * @return
     */
    CompanyOverviewDTO getCompanyOverview(String parkOrgId);

    BigDecimal getHouseUseRate();

    FileImportErrorInfoDTO importCompany(MultipartFile file, String parkOrgId);

    Boolean removeCompanyById(String id);

    Boolean updateCompanyStatus(UpdateStatusReqDTO statusReqDTO);
}
