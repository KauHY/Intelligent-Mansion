package com.crlink.smartpark.contract.model.request;

import java.time.LocalDate;

import java.io.Serializable;

import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.contract.dto.Extension;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 合同
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ContractReqDTO对象", description = "合同")
public class ContractReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号", required = true)
//    @Pattern(regexp = Constants.REGEX_CONTRACT_NO, message = "合同编号格式错误")
    @NotBlank(message = "合同编号不能为空")
    private String no;

    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    @Length(max = 30, message = "合同名称字数长度不超过30")
    private String name;

    /**
     * 园区Id
     */
    @ApiModelProperty(value = "园区Id", required = true)
    @NotBlank(message = "所属园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名", required = true)
    @NotBlank(message = "所属园区不能为空")
    private String parkOrgName;

    @ApiModelProperty(value = "总面积，公寓合同无需传，其他全传", required = true)
    private Double totalArea;
    /**
     * 经办人id，多个时,分隔
     */
    @ApiModelProperty(value = "经办人id，多个时,分隔")
    private String employeeId;

    /**
     * 合同/协议属性：1-新签；2-续签；3-变更；4-退约；5-历史
     */
    @ApiModelProperty(value = "合同/协议属性：1-新签；2-续签；3-变更；4-退约；5-历史", required = true)
    @CustomValid(value = {"1", "2", "3", "4", "5"}, message = "合同属性值错误")
    private Integer attributes;

    /**
     * 合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；
     */
    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；", required = true)
    @CustomValid(value = {"1", "2", "3", "4", "5"}, message = "合同类型值错误")
    private Integer type;

    /**
     * 合同开始日期
     */
    @ApiModelProperty(value = "合同开始日期", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    @ApiModelProperty(value = "合同结束日期", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    /**
     * 签约日期
     */
    @ApiModelProperty(value = "签约日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractedDate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 200, message = "备注字数不超过200")
    private String remark;

    /**
     * 甲方公司id
     */
    @ApiModelProperty(value = "甲方公司id", required = true)
    @NotBlank(message = "甲方公司id不能为空")
    private String companyIdA;

    /**
     * 甲方统一社会信用代码
     */
    @ApiModelProperty(value = "甲方统一社会信用代码", required = true)
//    @Pattern(regexp = Constants.REGEX_ENTERPRISE_CREDIT_CODE, message = "甲方社会信用代码错误")
    @Length(min = 18, max = 18, message = "甲方统一社会信用代码长度不为18")
    @NotBlank(message = "甲方统一社会信用代码不能为空")
    private String creditCodeA;

    /**
     * 甲方名称
     */
    @ApiModelProperty(value = "甲方名称", required = true)
    @NotBlank(message = "甲方名称不能为空")
    private String nameA;

    /**
     * 甲方联系人
     */
    @ApiModelProperty(value = "甲方联系人")
    private String contactA;

    /**
     * 甲方联系电话
     */
    @ApiModelProperty(value = "甲方联系电话")
    private String contactTelA;

    /**
     * 甲方地址
     */
    @ApiModelProperty(value = "甲方地址")
    private String addressA;

    /**
     * 乙方公司id
     */
    @ApiModelProperty(value = "乙方公司id", required = true)
    @NotBlank(message = "乙方公司id不能为空")
    private String customerIdB;

    /**
     * 乙方企业名称
     */
    @ApiModelProperty(value = "乙方企业名称", required = true)
    @NotBlank(message = "乙方名称不能为空")
    private String nameB;

    /**
     * 乙方统一社会信用代码
     */
    @ApiModelProperty(value = "乙方统一社会信用代码", required = true)
//    @Pattern(regexp = Constants.REGEX_ENTERPRISE_CREDIT_CODE, message = "乙方社会信用代码错误")
    @NotBlank(message = "乙方统一社会信用代码不能为空")
    @Length(min = 18, max = 18, message = "乙方统一社会信用代码长度不为18")
    private String creditCodeB;

    /**
     * 乙方联系人
     */
    @ApiModelProperty(value = "乙方联系人")
    private String contactB;

    /**
     * 乙方联系电话
     */
    @ApiModelProperty(value = "乙方联系电话")
    private String contactTelB;

    /**
     * 乙方地址
     */
    @ApiModelProperty(value = "乙方地址")
    private String addressB;

    /**
     * 附件key，逗号隔开
     */
    @ApiModelProperty(value = "附件key，逗号隔开")
    private String attachmentKey;

    /**
     * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
     */
    @ApiModelProperty(value = "合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中", required = true)
//    @CustomValid(value = {"1", "2", "3", "4", "5", "6", "7", "8"}, message = "合同状态值错误")
    private Integer state;

    /**
     * 当前流程节点
     */
    @ApiModelProperty(value = "当前流程节点")
    private String processNo;

    @ApiModelProperty(value = "拓展字段")
    private Extension extension;

    @ApiModelProperty(value = "关联房屋信息", required = true)
    @NotEmpty(message = "关联房屋信息不能为空")
    private List<HouseMessage> houseMessages;

    @ApiModelProperty(value = "退约日期")
    private LocalDate withdrawDate;

    @ApiModelProperty(value = "退约原因")
    private String withdrawReason;

//    /**
//     * 关联房屋ids
//     */
//    @ApiModelProperty(value = "关联房屋ids", required = true)
//    @NotEmpty(message = "关联房屋不能为空")
//    private Set<String> houseIds;

    /**
     * 计费方案
     */
    @ApiModelProperty(value = "计费方案")
    private List<ContractBillingPlanReqDTO> contractBillingPlanReqDTOS;

}
