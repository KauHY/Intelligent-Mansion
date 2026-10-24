package com.crlink.smartpark.industrial.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.COMPANY_NAME_CODE;
import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.PARK_ORG_NAME_CODE;

/**
 * <p>
 * 采购服务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PurchaseService响应对象", description="采购服务")
public class PurchaseServiceDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "所属园区")
    @Id2objectField(idField = "parkOrgId", code = PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "公司Id")
    private String companyId;
    @ApiModelProperty(value = "企业名称")
    @Id2objectField(idField = "companyId", code = COMPANY_NAME_CODE)
    private String companyName;

    @ApiModelProperty(value = "服务名")
    private String name;

    @ApiModelProperty(value = "采购类型：0-货物；1-服务；2-其他")
    private Integer purchaseType;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "有效截止时间")
    private LocalDateTime effectiveEndTime;

    @ApiModelProperty(value = "附件")
    private String attKey;

    @ApiModelProperty(value = "跳转类型：0-无；1-URL；2-小程序")
    private Integer jumpType;

    @ApiModelProperty(value = "跳转地址")
    private String url;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String tel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态：0-待发布；1-进行中；2-已下架；3-已过期；")
    private Integer state;

    private LocalDateTime createTime;

    private LocalDateTime publishTime;

    @ApiModelProperty(value = "数据来源：0-企业新增；1-管理员新增")
    private Integer source;

}
