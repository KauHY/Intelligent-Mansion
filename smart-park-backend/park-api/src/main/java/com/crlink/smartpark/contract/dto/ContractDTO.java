package com.crlink.smartpark.contract.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.List;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.system.dto.house.HouseVO;
import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;


/**
 * <p>
 * 合同
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ContractDTO对象", description = "合同")
public class ContractDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String id;

    @ApiModelProperty(value = "合同编号")
    private String no;

    @ApiModelProperty(value = "合同名称")
    private String name;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;
    private String parkOrgName;

    @ApiModelProperty(value = "总面积")
    private Double totalArea;

    @ApiModelProperty(value = "经办人id，多个时,分隔")
    private String employeeId;
    @ApiModelProperty(value = "经办人")
    private List<DictVO> employeeDicts;

    @ApiModelProperty(value = "合同/协议属性：1-新签；2-续签；3-变更；4-退约；5-历史")
    private Integer attributes;

    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer type;

    @ApiModelProperty(value = "合同开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "合同结束日期")
    private LocalDate endDate;

    @ApiModelProperty(value = "签约日期")
    private LocalDate contractedDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "甲方公司id")
    private String companyIdA;

    @ApiModelProperty(value = "甲方统一社会信用代码")
    private String creditCodeA;

    @ApiModelProperty(value = "甲方名称")
    private String nameA;

    @ApiModelProperty(value = "甲方联系人")
    private String contactA;

    @ApiModelProperty(value = "甲方联系电话")
    private String contactTelA;

    @ApiModelProperty(value = "甲方地址")
    private String addressA;

    @ApiModelProperty(value = "乙方公司id")
    private String customerIdB;

    @ApiModelProperty(value = "乙方企业名称")
    private String nameB;

    @ApiModelProperty(value = "乙方统一社会信用代码")
    private String creditCodeB;

    @ApiModelProperty(value = "乙方联系人")
    private String contactB;

    @ApiModelProperty(value = "乙方联系电话")
    private String contactTelB;

    @ApiModelProperty(value = "乙方地址")
    private String addressB;

    @ApiModelProperty(value = "附件key，逗号隔开")
    private String attachmentKey;

    @ApiModelProperty(value = "合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中")
    private Integer state;

    @ApiModelProperty(value = "当前流程节点")
    private String processNo;

    @ApiModelProperty(value = "拓展字段")
    private Extension extension;

    @ApiModelProperty(value = "关联房屋信息")
    @NotEmpty
    private List<HouseMessage> houseMessages;
//
//    @ApiModelProperty(value = "关联房屋")
//    private List<HouseVO> houseVOS;

    @ApiModelProperty(value = "退约日期")
    private LocalDate withdrawDate;

    @ApiModelProperty(value = "退约原因")
    private String withdrawReason;

    @ApiModelProperty(value = "计费方案")
    private List<ContractBillingPlanDTO> contractBillingPlanDTOS;


}
