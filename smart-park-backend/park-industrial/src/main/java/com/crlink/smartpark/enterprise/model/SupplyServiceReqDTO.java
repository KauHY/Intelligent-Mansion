package com.crlink.smartpark.enterprise.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 供应服务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SupplyService请求对象", description="供应服务")
public class SupplyServiceReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "园区Id", required = true)
    @NotBlank
    private String parkOrgId;

    @ApiModelProperty(value = "公司Id", required = true)
    @NotBlank
    private String companyId;

    @ApiModelProperty(value = "服务名", required = true)
    @NotBlank
    @Length(max = 30)
    private String name;

    @ApiModelProperty(value = "供应类型：0-产品供应；1-服务供应；2-其他供应", required = true)
    @NotNull
    private Integer supplyType;

    @ApiModelProperty(value = "详情", required = true)
    @NotBlank
    @Length(max = 3000)
    private String detail;

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

    @ApiModelProperty(value = "状态：0-待发布；1-已发布；2-下架")
    private Integer state;

}
