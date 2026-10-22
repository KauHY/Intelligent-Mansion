package com.crlink.smartpark.contract.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.contract.dto.ApartmentDataForScreen;
import com.crlink.smartpark.contract.dto.BillForScreen;
import com.crlink.smartpark.contract.dto.BillListForScreen;
import com.crlink.smartpark.contract.dto.LateFeeDetail;
import com.crlink.smartpark.contract.entity.RentContractBill;
import com.crlink.smartpark.contract.model.ChargeStaReqDTO;
import com.crlink.smartpark.contract.model.ChargeStaTrendDTO;
import com.crlink.smartpark.contract.model.ChargeTopStatisticDTO;
import com.crlink.smartpark.contract.model.query.CompanyPortBillQuery;
import com.crlink.smartpark.contract.model.query.RentContractBillQuery;
import com.crlink.smartpark.contract.model.request.BillUploadFileReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillCheckoutReqDTO;
import com.crlink.smartpark.contract.model.request.RentBillDeductionFeeDTO;
import com.crlink.smartpark.contract.model.request.RentBillUpdateReqDTO;
import com.crlink.smartpark.contract.model.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租赁合同账单 服务类
 * </p>
 * */
public interface RentContractBillService extends IService<RentContractBill> {

    /**
     * 根据Id查询值
     */
    <T> Map<String, T> id2ValueMap(Collection<String> ids, SFunction<RentContractBill, T> sValue);

    /**
     * 修改账单的所在账期
     *
     * @param reqDTO
     * @return
     */
    Boolean updateRentContractBill(RentBillUpdateReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    RentContractBillVO getRentContractBillById(String id);

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<RentContractBillListVO> listRentContractBillPage(RentContractBillQuery query);

    Boolean deductionFee(RentBillDeductionFeeDTO dto);

    void export(RentContractBillQuery query);

    Boolean checkout(RentBillCheckoutReqDTO reqDTO);

    /**
     * 批量收款操作前获取滞纳金数据
     *
     * @param ids
     * @return
     */
    LateFeeDetail findLateFeeByBillIds(String[] ids);

    /**
     * 收款操作前获取滞纳金明细及实收总金额，非批量
     *
     * @param id
     * @return
     */
    LateFeeDetail findLateFeeByBillId(String id);

    /**
     * 获取账单编号
     */
    String getBillNo();

    PageInfo<CompanyPortBillListVO> listCompanyPortBillPage(CompanyPortBillQuery query);

    Boolean uploadFiles(BillUploadFileReqDTO reqDTO);

    List<BillListForScreen> getBillListForScreen(String id);

    BillForScreen getBillForScreen(String parkOrgId);

    ApartmentDataForScreen getApartmentDataForScreen(String parkOrgId);

    List<PayTrendVO> paymentTrend(ChargeStaTrendDTO reqDTO);

    IncomeDistributionVO incomeDistribution(ChargeStaReqDTO reqDTO);

    FeeTopStatisticVO topStatistic(ChargeTopStatisticDTO reqDTO);
}
