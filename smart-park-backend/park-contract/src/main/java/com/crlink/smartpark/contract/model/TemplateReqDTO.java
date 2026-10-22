package com.crlink.smartpark.contract.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.crlink.smartpark.core.validation.CustomValid;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 合同模板管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Template请求对象", description="合同模板管理")
public class TemplateReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "园区Id", required = true)
    @NotBlank(message = "所属园区不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；", required = true)
    @CustomValid(value = {"1", "2", "3", "4", "5"}, message = "合同类型值错误")
    private Integer type;

    @NotBlank(message = "work模板不能为空")
    @ApiModelProperty(value = "word模板key", required = true)
    private String templateKey;

    @ApiModelProperty(value = "word模板名字", required = true)
    @NotBlank(message = "work模板不能为空")
    private String templateName;

}
