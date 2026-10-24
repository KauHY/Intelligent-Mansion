package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.excel.convert.LocalDateTimeConverter;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 租赁合同交易明细
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RentContractLeaveExcelExportDTO", description = "离场结算记录")
public class RentContractLeaveExcelExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ExcelProperty(value = "所属园区")
    @ColumnWidth(20)
    private String parkOrgName;


    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String companyName;

    @ExcelProperty(value = "结算房间")
    @ColumnWidth(20)
    private String houseName;

    @ExcelProperty(value = "计费类型", converter = DictConverter.class)
    @DictField(dictType = "RENT_FEE_LEAVE_TYPE")
    @ColumnWidth(20)
    private Integer type;

    @ExcelProperty(value = "实收总金额（元）")
    @ColumnWidth(20)
    private BigDecimal totalAmount;

    @ExcelProperty(value = "提交时间", converter = LocalDateTimeConverter.class)
    @ColumnWidth(20)
    private LocalDateTime createTime;

    @ExcelIgnore
    private String createBy;

    @ExcelProperty(value = "操作人")
    @Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    @ColumnWidth(20)
    private String createByName;

}
