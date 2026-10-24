package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.LateFeeDetail;
import com.crlink.smartpark.contract.entity.MeterConsumptionBill;
import com.crlink.smartpark.contract.model.query.CompanyPortBillQuery;
import com.crlink.smartpark.contract.model.query.RentContractBillQuery;
import com.crlink.smartpark.contract.model.request.BillUploadFileReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillCheckoutReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillDeductionFeeDTO;
import com.crlink.smartpark.contract.model.request.RentBillUpdateReqDTO;
import com.crlink.smartpark.contract.model.vo.CompanyPortBillListVO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionBillListVO;
import com.crlink.smartpark.contract.model.vo.MeterConsumptionBillVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 租赁合同账单 服务类
 * </p>
 * */
public interface MeterConsumptionBillService extends IService<MeterConsumptionBill> {

//    /**
//     * 根据Id查询值
//     */
//     Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionBill, ?> sValue);
//
//    /**
//     * 保存或更新数据
//     *
//     * @return 实体对象
//     */
//    Boolean saveOrUpdateMeterConsumptionBill(MeterConsumptionBillReqDTO reqDTO);
//
//    /**
//     *  根据id查询实体对象
//     *
//     * @param id 查询条件
//     * @return   实体对象
//     */
//    MeterConsumptionBillDTO getMeterConsumptionBillById(String id);
//
//    /**
//     * 分页查询
//     *
//     * @param query 查询对象
//     * @return 分页结果
//     */
//    PageInfo<MeterConsumptionBillDTO> listMeterConsumptionBillPage(MeterConsumptionBillQuery query);

    /**
     * 根据Id查询值
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<MeterConsumptionBill, T> sValue);

    /**
     * 修改账单的所在账期
     *
     * @param reqDTO
     * @return
     */
    Boolean updateMeterConsumptionBill(RentBillUpdateReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    MeterConsumptionBillVO getMeterConsumptionBillById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<MeterConsumptionBillListVO> listMeterConsumptionBillPage(RentContractBillQuery query);

    Boolean deductionFee(RentBillDeductionFeeDTO dto);

    void export(RentContractBillQuery query);

    Boolean checkout(RentBillCheckoutReqDTO reqDTO);

    /**
     * 批量收款操作前获取滞纳金数据
     *
     * @param ids
     * @return
     */
    LateFeeDetail findLateFeeByBillIds(Set<String> ids);

    /**
     * 收款操作前获取滞纳金明细及实收总金额，非批量
     *
     * @param id
     * @return
     */
    LateFeeDetail findLateFeeByBillId(String id);

//    /**
//     * 获取账单编号
//     */
//    String getBillNo();

    PageInfo<CompanyPortBillListVO> listCompanyPortBillPage(CompanyPortBillQuery query);

    Boolean uploadFiles(BillUploadFileReqDTO reqDTO);
}
