/*
 * @Description:
 * @Version: 2.0
 * @Autor: GUOCHAO82
 * @Date: 2022-01-17 11:14:05
 * @LastEditors: GUOCHAO82
 * @LastEditTime: 2022-04-28 15:17:42
 */
enum CollectFee {
    chargeItemListPage = '/park-fee/charge-item/listChargeItemPage', // 收费项分页
    chargeItemAdd = '/park-fee/charge-item/create', // 收费项新增
    chargeItemDel = '/park-fee/charge-item/remove/', // 收费项分页
    chargeItemModify = '/park-fee/charge-item/modify', // 收费项分页
    chargeItemSetState = '/park-fee/charge-item/setStatus', // 收费项状态编辑
    chargeItemDict = '/park-fee/charge-item/selectChargeItemDict', // 收费项下拉
    chargeItemFindById = '/park-fee/charge-item/findById/', // 收费项详情
    calculation = '/park-fee/billing-scheme/calculation', // 计费

    // -------------------计费方案分页---------------------
    billingSchemeListPage = '/park-fee/billing-scheme/listBillingSchemePage', // 计费方案分页
    billingSchemeAdd = '/park-fee/billing-scheme/create', // 计费方案新增
    billingSchemeDel = '/park-fee/billing-scheme/remove/', // 计费方案分页
    billingSchemeModify = '/park-fee/billing-scheme/modify', // 计费方案分页
    billingSchemeSetState = '/park-fee/billing-scheme/setStatus', // 计费方案状态编辑
    billingSchemeFindById = '/park-fee/billing-scheme/findById/', // 计费方案详情

    // -----------------------------------租赁费用----------------------------------------------
    // 费用
    rentFeeListPage = '/park-contract/rentContractFee/listRentContractFeePage', // 未出费用用分页
    rentFeeTransform = '/park-contract/rentContractFee/transform', // 批量转为已出
    rentFeeExport = '/park-contract/rentContractFee/export', // 租赁合同费用导出
    rentFeeModifyDate = '/park-contract/rentContractFee/modifyDate', // 租赁批量修改日期
    rentFeeFindById = '/park-contract/rentContractFee/findById/', // 租赁费用根据id查询

    getCalculationListByFeeType='/park-fee/billing-scheme/getCalculationListByFeeId', // 计费方案-根据收费类型获取计算标准下拉
    leaveSettlement='/park-contract/rentContractLeave/leaveSettlement', // 离场结算记录-离场结算-计算费用
    findByIdleaveSettlement='/park-contract/rentContractLeave/findById/', // 离场结算记录-根据ID查询
    listRentContractLeavePage='/park-contract/rentContractLeave/listRentContractLeavePage', // 离场结算记录-分页查询
    settlementPay='/park-contract/rentContractLeave/settlementPay', // 离场结算记录-结算缴费
    exportsettlement='/park-contract/rentContractLeave/export', // 离场结算记录-导出
    getDictListByType='/park-contract/contract/getDictListByType', // 合同-费用模块根据类型获取合同下拉列表

    //  租赁账单
    rentFeeBillListPage = '/park-contract/rentContractBill/listRentContractBillPage', // 租赁费用账单分页
    rentFeeBillModify = '/park-contract/rentContractBill/modify', // 租赁费用账单编辑
    rentFeeBillCheckout = '/park-contract/rentContractBill/checkout', // 租赁费用账单收款
    rentFeeBillDeduction = '/park-contract/rentContractBill/deductionFee', // 租赁费用账单减免
    rentFeeBillDetails = '/park-contract/rentContractBill/findById/', // 租赁费用账单明细
    rentFeeBillExport = '/park-contract/rentContractBill/export', // 账单导出
    beforeCheckoutRentFee = '/park-contract/rentContractBill/findLateFeeByBillId', // 租赁费用收款总金额
    beforeBatchCheckoutRentFee = '/park-contract/rentContractBill/findLateFeeByBillIds', // 租赁费批量收款总金额

    // 交易明细
    rentFeeTransactionDetailListPage = '/park-contract/rentContractTrade/listRentContractTradePage', // 租赁费用交易明细分页
    rentFeeReturnMoney = '/park-contract/rentContractTrade/returnMoney', // 租赁费退款
    rentFeeTransactionDetailExport = '/park-contract/rentContractTrade/export', // 租赁费交易明细导出
    rentFeeTransactionDetailFindById= '/park-contract/rentContractTrade/findById/', // 租赁费交易明细根据id查询详情

    // 押金管理
    rentDepositFeeReturnMoney = '/park-contract/rentContractFee/refund', // 租赁费押金退款

    // 滞纳金
    rentLateFeeListPage = '/park-contract/rentLateFee/listRentLateFeePage', // 租赁费用滞纳金分页查询
    rentLateFeePrevent = '/park-contract/rentLateFee/prevent', // 租赁费用滞纳金免除
    rentLateFeeExport = '/park-contract/rentLateFee/export', // 租赁费用滞纳导出

    // 水电-费用
    listMeterFeePage='/park-contract/meterConsumptionFee/listMeterFeePage', // 水电费用-分页查询
    findByIdFeePage='/park-contract/meterConsumptionFee/findById/', // 水电费用-根据ID查询
    exportFeePage='/park-contract/meterConsumptionFee/export', // 水电费用-导出
    modifyDateFee='/park-contract/meterConsumptionFee/modifyDate', // 水电费用-批量修改收款日期
    transformFee='/park-contract/meterConsumptionFee/transform', // 水电费用-批量转为已出

    listMeterLateFeePage='/park-contract/meterConsumptionLateFee/listMeterLateFeePage', // 水电滞纳金费用-分页查询
    exportMeterLateFee='/park-contract/meterConsumptionLateFee/export', // 水电滞纳金费用-导出
    preventLateFee='/park-contract/meterConsumptionLateFee/prevent', // 水电滞纳金费用-免除

    listMeterConsumptionBillPage='/park-contract/meterConsumptionBill/listMeterConsumptionBillPage', // 企业水电账单管理-分页查询
    modifyConsumptionBill='/park-contract/meterConsumptionBill/modify', // 企业水电账单管理-编辑
    findLateFeeByBillIds='/park-contract/meterConsumptionBill/findLateFeeByBillIds', // 企业水电账单管理-根据账单ID查询数据(批量)
    findLateFeeByBillId='/park-contract/meterConsumptionBill/findLateFeeByBillId', // 企业水电账单管理-根据账单ID查询滞纳金
    findByIdConsumptionBill='/park-contract/meterConsumptionBill/findById/', // 企业水电账单管理-根据ID查询
    exportConsumptionBill='/park-contract/meterConsumptionBill/export', // 企业水电账单管理-导出
    deductionFee='/park-contract/meterConsumptionBill/deductionFee', // 企业水电账单管理-减免
    checkout='/park-contract/meterConsumptionBill/checkout', // 企业水电账单管理-收款

    listMeterConsumptionPage='/park-contract/meterConsumptionTrade/listMeterConsumptionPage', // 水电费交易明细-分页查询
    returnMoney='/park-contract/meterConsumptionTrade/returnMoney', // 水电费交易明细-退款
    exportConsumptionPage='/park-contract/meterConsumptionTrade/export', // 水电费交易明细-导出
    findByIdConsumptionPage='/park-contract/meterConsumptionTrade/findById/', // 水电费交易明细-根据ID查询

    findByIdmeterConsumptionLeave='/park-contract/meterConsumptionLeave/findById/', // 水电费离场结算记录-根据ID查询
    exportmeterConsumptionLeave='/park-contract/meterConsumptionLeave/export', // 水电费离场结算记录-导出
    leaveSettlementmeterConsumptionLeave='/park-contract/meterConsumptionLeave/leaveSettlement', // 水电费离场结算记录-离场结算-计算费用
    listRentContractLeaveConsumptionLeave='/park-contract/meterConsumptionLeave/listRentContractLeavePage', // 水电费离场结算记录-分页查询
    settlementPaymeterLeave='/park-contract/meterConsumptionLeave/settlementPay', // 水电费离场结算记录-结算缴费

    getHouseListByContract='/park-contract/contract/getHouseListByContract/', // 合同-费用模块根据合同Id获取合同所关
    computeAmount='/park-contract/meterConsumptionLeave/computeAmount', // 水电费离场结算记录-离场结算-计算金额
}
export default CollectFee;
