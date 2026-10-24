package com.crlink.smartpark.common.model.query;

import com.crlink.smartpark.BaseQuery;
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
import java.util.List;

/**
 * <p>
 * 多级分类表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MultistageCategory查询对象", description="多级分类表")
public class MultistageCategoryQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "分类")
    private String parentId;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "层级，顶级为0")
    private Integer hierarchy;

    @ApiModelProperty(value = "是否能扩展：0-否；1-是")
    private Boolean expand;

    @ApiModelProperty(value = "是否可选：0-否；1-是")
    private Boolean selectable;

    @ApiModelProperty(value = "禁用：0-否；1-是")
    private Boolean disabled;

    @ApiModelProperty(value = "是否叶子节点：0-否；1-是")
    private Boolean leaf;

    @ApiModelProperty(value = "名称")
    private String label;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除：0否1是")
    private Boolean deleted;


}
