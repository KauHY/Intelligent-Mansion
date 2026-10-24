package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentLoopMerge;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 企业或房屋水电用量
 * </p>
 * */
@Data
@ApiModel(description = "企业或房屋水电用量导出")
@Slf4j
public class MeterConsumptionExportDTO implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;


    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty(value = "所属园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty(value = "结算月份")
    private String settledMonth;

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty(value = "企业名称")
    private String companyName;
    //private String companyId;

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty(value = "合同类型", converter = DictConverter.class)
    @DictField(dictType = "CONTRACT_TYPE")
    private Integer contractType;

    @ContentLoopMerge(eachRow = 2)
    @ExcelProperty(value = "房源名称", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 9)
    private List<String> houseIds;

    @ExcelProperty(value = "能耗类别", converter = DictConverter.class)
    @DictField(dictType = "ENERGY_CONSUMPTION_CATEGORY")
    private Integer deviceType;

    @ExcelProperty("本月用量")
    private BigDecimal totalAmount = BigDecimal.ZERO;


    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("对象负责失败！", e);
        }
        return null;
    }
}
