package com.crlink.smartpark.device.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 卡片管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Card响应对象", description = "卡片管理")
public class CardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @ExcelIgnore
    private String id;

    @ApiModelProperty(value = "卡号")
    @ExcelProperty("卡号")
    @ColumnWidth(20)
    private String cardNo;

    @ApiModelProperty(value = "园区组织Id")
    @ExcelIgnore
    private String parkOrgId;

    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    @ExcelProperty("所属园区")
    @ColumnWidth(20)
    private String parkOrgName;

    @ApiModelProperty(value = "卡片介质:CARD_MEDIUM：1 IC/ID卡")
    @ExcelProperty("卡介质")
    @ColumnWidth(20)
    private Integer cardMedium;

    @ApiModelProperty(value = "卡片类型:CARD_TYPE：1普通卡、2管理员卡、3其他")
    @ExcelProperty("卡类型")
    @ColumnWidth(20)
    private Integer cardType;

    @ApiModelProperty(value = "关联员工Id")
    @ExcelIgnore
    private String employeeId;

    @ApiModelProperty(value = "关联员工/持卡人")
    @Id2objectField(code = Id2objCodeConstant.EMPLOYEE_NAME_CODE)
    @ExcelProperty("持卡人")
    @ColumnWidth(20)
    private String employeeName;

    @ApiModelProperty(value = "联系电话")
    private String mobile;

    @ApiModelProperty(value = "所属企业Id")
    @ExcelIgnore
    private String companyId;

    @Id2objectField(idField = "companyId", code = COMPANY_NAME_CODE)
    @ExcelProperty("关联企业")
    @ColumnWidth(20)
    private String companyName;

    @ApiModelProperty(value = "有效期开始日期")
    @ExcelProperty("有效期")
    @ColumnWidth(20)
    private LocalDate effectStartDate;

    @ApiModelProperty(value = "有效期结束日期")
    @ExcelProperty("有效期")
    @ColumnWidth(20)
    private LocalDate effectEndDate;

    @ApiModelProperty(value = "备注")
    @ExcelIgnore
    private String remark;

    @ApiModelProperty(value = "状态：CARD_STATUS：1已绑定、2已挂失、3已注销、4已解绑")
    @ExcelProperty("状态")
    @ColumnWidth(20)
    private Integer status;

}
