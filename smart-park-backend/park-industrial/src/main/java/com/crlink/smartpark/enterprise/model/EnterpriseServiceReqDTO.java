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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 企业服务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EnterpriseService请求对象", description="企业服务")
public class EnterpriseServiceReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "服务名", required = true)
    @NotBlank
    @Length(max = 30)
    private String name;

    @ApiModelProperty(value = "推送范围", required = true)
    @NotEmpty
    private List<String> pushRange;

    @ApiModelProperty(value = "所在分组：0-服务指南；1-政策申报；2-其他", required = true)
    @NotNull
    private Integer groupType;

    @ApiModelProperty(value = "备注")
    @Length(max = 200)
    private String remark;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "banner")
    private String banner;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "详情")
    private String detail;

    @ApiModelProperty(value = "链接")
    private String url;

    @ApiModelProperty(value = "咨询电话")
    private String tel;

    @ApiModelProperty(value = "附件")
    private String att;

    @ApiModelProperty(value = "状态：0-待发布；1-已发布；2-停用")
    private Integer state;

    private LocalDateTime updateTime;

}
