package com.crlink.smartpark.contract.model.query;

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
 * 模板字段表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TemplateLibrary查询对象", description="模板字段表")
public class TemplateLibraryQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
      private String id;

    @ApiModelProperty(value = "父级")
    private String parentId;

    @ApiModelProperty(value = "分组ID")
    private String groupIds;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "描述说明")
    private String description;

    @ApiModelProperty(value = "value对应的编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String label;

    @ApiModelProperty(value = "值（前端）")
    private String value;

    @ApiModelProperty(value = "匹配的字段名")
    private String fieldName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除：0否1是")
    private Boolean deleted;


}
