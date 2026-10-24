package com.crlink.smartpark.fee.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.fee.controller.request.BillingSchemeReqDTO;
import com.crlink.smartpark.fee.controller.request.CalculationReqDTO;
import com.crlink.smartpark.fee.dto.BillingSchemeQuery;
import com.crlink.smartpark.fee.dto.BillingSchemeDTO;
import com.crlink.smartpark.fee.dto.SchemeItemDTO;
import com.crlink.smartpark.fee.entity.BillingScheme;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 计费方案 服务类
 * </p>
 * */
public interface BillingSchemeService extends IService<BillingScheme> {

    /**
     * 根据Id查询值
     */
    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<BillingScheme, ?> sValue);

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateBillingScheme(BillingSchemeReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    BillingSchemeDTO getBillingSchemeById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<BillingSchemeDTO> listBillingSchemePage(BillingSchemeQuery query);

    BigDecimal calculation(CalculationReqDTO calculationReqDTO);

    Map<String, SchemeItemDTO> getSchemeNameAndItemBySchemeIds(Set<String> set);

    Map<String, BillingSchemeDTO> getBillingSchemeDTOByIds(Set<String> set);

    List<DictVO> getCalculationListByFeeType(int type);

    List<DictVO> getCalculationListByFeeId(String id);

    Map<String, List<BillingSchemeDTO>> listBillingScheme(Set<String> parkOrgIds);
}
