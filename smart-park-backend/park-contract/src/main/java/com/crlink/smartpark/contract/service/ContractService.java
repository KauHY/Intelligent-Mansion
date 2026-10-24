package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.ContractDTO;
import com.crlink.smartpark.contract.dto.ContractHouseStatusDTO;
import com.crlink.smartpark.contract.entity.Contract;
import com.crlink.smartpark.contract.model.query.ContractListQuery;
import com.crlink.smartpark.contract.model.query.ContractRentedHouseQuery;
import com.crlink.smartpark.contract.model.request.AuditReqDto;
import com.crlink.smartpark.contract.model.request.ContractReqDTO;
import com.crlink.smartpark.contract.model.request.WithdrawReqDto;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 合同 服务类
 * </p>
 * */
public interface ContractService extends IService<Contract> {

    /**
     * 保存/更新数据
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean saveOrUpdateContract(ContractReqDTO dto);

    /**
     * 合同变更
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean changeContract(ContractReqDTO dto);

    /**
     * 合同续约
     *
     * @param dto 存储对象
     * @return 实体对象
     */
    Boolean extendContract(ContractReqDTO dto);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    ContractDTO getContractById(String id);

    /**
     * 根据id作废合同
     *
     * @param id 查询条件
     * @return 实体对象
     */
    Boolean nullifyById(String id);

    /**
     * 根据id终止合同
     *
     * @param id 查询条件
     * @return 实体对象
     */
    Boolean terminationById(String id);

    //TODO 合同退约：等审批流完成

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<ContractDTO> listPage(ContractListQuery condition);


    void export(ContractListQuery contractListQuery);

    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<Contract, T> sValue);

    Boolean checkNoConflict(String no, String id);

    /**
     * 获取合同日期内待生效和生效中所有的房屋id，作为关联房屋时查询房屋条件noIds
     *
     * @param contractRentedHouseQuery
     * @return
     */
    Set<String> getUnavailableHouse(ContractRentedHouseQuery contractRentedHouseQuery);

    String getNoByType(int type);

    Boolean withdraw(WithdrawReqDto withdrawReqDto);

    Boolean audit(AuditReqDto auditReqDto);

    Boolean submitAudit(String id);

    List<DictVO> getCalculationListByType(int type);

    List<DictVO> getDictListByType(Integer type, String parkOrgId, String companyId);

    PageInfo<BillingSchemeDTO> listBillingSchemePage(ContractListQuery contractListQuery);

    Set<String> getEffectiveHouseIds();


    List<ContractDTO> getEffectiveContracts();

    List<DictVO> getHouseListByContract(String contractIds);

    Map<String, ContractHouseStatusDTO> getContractHouseRelationFor3D(Collection<String> houseIds);
}
