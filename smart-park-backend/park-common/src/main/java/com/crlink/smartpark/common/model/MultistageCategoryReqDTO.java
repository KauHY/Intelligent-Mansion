package com.crlink.smartpark.common.model;

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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 多级分类表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MultistageCategory请求对象", description="多级分类表")
public class MultistageCategoryReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "园区id", required = true)
    @NotEmpty(message = "园区Id不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "类型", required = true)
    @NotEmpty(message = "类型不能为空")
    private String type;

//    @ApiModelProperty(value = "层级,后端处理")
//    private Integer hierarchy;

    @ApiModelProperty(value = "是否能扩展：false-否；true-是")
    private Boolean expand;

    @ApiModelProperty(value = "是否可选：false-否；true-是")
    private Boolean selectable;

    @ApiModelProperty(value = "禁用：false-否；true-是")
    private Boolean disabled;

    @ApiModelProperty(value = "名称", required = true)
    private String label;

}
