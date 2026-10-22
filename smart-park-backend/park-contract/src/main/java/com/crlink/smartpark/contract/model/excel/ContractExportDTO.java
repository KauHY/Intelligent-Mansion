package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 合同
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ContractExcelDTO对象", description = "合同")
public class ContractExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "所属园区")
    private String parkOrgId;

    @ExcelProperty(value = "合同编号")
    private String no;

    @ExcelProperty(value = "入驻企业名称")
    private String nameB;

    /**
     * 特殊处理
     */
    @ExcelProperty(value = "房源名称")
    private String houseMessagesStr;
    @ExcelProperty(value = "房源楼层")
    private String totalFloorList;

    @ExcelProperty(value = "租期（年）")
    private Integer leaseTermYear;



    @ExcelProperty(value = "总面积（㎡）")
    private Double totalArea;

    @ExcelProperty(value = "物业费（元）")
    private BigDecimal propertyCosts;
    /**
     * 2021年11月1号
     */
    @ExcelProperty(value = "起租日期")
    private LocalDate startDate;
    /**
     * 2021年11月1号
     */
    @ExcelProperty(value = "截止日期")
    private LocalDate endDate;
    /**
     * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
     */
    @ExcelProperty(value = "合同状态", converter = DictConverter.class)
    @DictField(dictType = "CONTRACT_STATE")
    private Integer state;

    /**
     * todo 经办人id，多个时；分隔
     */
    @ExcelProperty(value = "经办人")
    private String employeeId;

    /**
     * 2021年11月1号
     */
    @ExcelProperty(value = "办理日期")
    private LocalDate contractedDate;

    @ExcelProperty(value = "承租人姓名")
    private String contactB;

    @ExcelProperty(value = "租金（元）")
    private BigDecimal rentAmount;
    @ExcelProperty(value = "押金（元）")
    private BigDecimal deposit;

    @ExcelProperty(value = "履约保证金（元）")
    private BigDecimal performanceBond;

    // 公寓合同
    @ExcelProperty(value = "租期（天）")
    private Integer leaseTermDay;
    @ExcelProperty(value = "客房数(间)")
    private Integer numberOfRooms;

    // 租赁合同\物业合同
    /**
     * 2021年12月
     */
    @ExcelProperty(value = "装修日期（月）")
    private LocalDate renovationDate;

    // @ApiModelProperty(value = "合同编号")
    // @ExcelProperty("合同编号")
    // @ColumnWidth(20)
    // private String no;
    //
    // @ApiModelProperty(value = "合同名称")
    // @ExcelProperty("合同名称")
    // @ColumnWidth(20)
    // private String name;
    //
    // @ExcelProperty("园区")
    // @ColumnWidth(20)
    // private String parkOrgName;
    //
    // @ExcelProperty("合同属性")
    // @ColumnWidth(20)
    // private String attributesName;
    //
    // @ExcelProperty("合同类型")
    // @ColumnWidth(20)
    // private String typeName;
    //
    // @ApiModelProperty(value = "合同开始日期")
    // @ExcelProperty(value = "合同开始日期")
    // @ColumnWidth(20)
    // private LocalDate startDate;
    //
    // @ApiModelProperty(value = "合同结束日期")
    // @ExcelProperty(value = "合同结束日期")
    // @ColumnWidth(20)
    // private LocalDate endDate;
    //
    // @ApiModelProperty(value = "签约日期")
    // @ExcelIgnore
    // private LocalDate contractedDate;
    //
    // @ApiModelProperty(value = "备注")
    // @ExcelIgnore
    // private String remark;
    //
    // @ApiModelProperty(value = "甲方统一社会信用代码")
    // @ExcelIgnore
    // private String creditCodeA;
    //
    // @ApiModelProperty(value = "甲方名称")
    // @ExcelIgnore
    // private String nameA;
    //
    // @ApiModelProperty(value = "甲方联系人")
    // @ExcelIgnore
    // private String contactA;
    //
    // @ApiModelProperty(value = "甲方联系电话")
    // @ExcelIgnore
    // private String contactTelA;
    //
    // @ApiModelProperty(value = "甲方地址")
    // @ExcelIgnore
    // private String addressA;
    //
    // @ApiModelProperty(value = "乙方企业名称")
    // @ExcelIgnore
    // private String nameB;
    //
    // @ApiModelProperty(value = "乙方统一社会信用代码")
    // @ExcelIgnore
    // private String creditCodeB;
    //
    // @ApiModelProperty(value = "乙方联系人")
    // @ExcelIgnore
    // private String contactB;
    //
    // @ApiModelProperty(value = "乙方联系电话")
    // @ExcelIgnore
    // private String contactTelB;
    //
    // @ApiModelProperty(value = "乙方地址")
    // @ExcelIgnore
    // private String addressB;
    //
    // @ExcelProperty("合同状态")
    // @ColumnWidth(20)
    // private String stateName;
    //
    // @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    // private String parkOrgId;
    //
    //
    //
    // /**
    // * todo 特殊处理
    // */
    // @ExcelProperty(value = "房源名称")
    // private String houseMessagesStr;
    // private List<HouseMessage> houseMessages;
    //
    // @ExcelProperty(value = "租期（年）")
    // private Integer leaseTermYear;
    //
    // @ExcelProperty(value = "总面积（㎡）")
    // private Double totalArea;
    // private BigDecimal rentAmount;
    // @ApiModelProperty(value = "履约保证金（元）")
    // private BigDecimal deposit;
    //
    // //租赁合同\物业合同
    // /**
    // * 2021年12月
    // */
    // @ExcelProperty(value = "装修日期（月）")
    // private LocalDate renovationDate;
    // /**
    // * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
    // */
    // @ExcelProperty(value = "合同状态", converter = DictConverter.class)
    // @DictField(dictType = "CONTRACT_STATE")
    // private Integer state;
    //
    // /**
    // * todo 经办人id，多个时；分隔
    // */
    // @ExcelProperty(value = "经办人")
    // private String employeeId;

}
