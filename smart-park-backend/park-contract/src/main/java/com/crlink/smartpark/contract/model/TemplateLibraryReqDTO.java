package com.crlink.smartpark.contract.model;

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
@ApiModel(value="TemplateLibrary请求对象", description="模板字段表")
public class TemplateLibraryReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id", required = true)
      private String id;

    @ApiModelProperty(value = "父级", required = true)
    private String parentId;

    @ApiModelProperty(value = "分组ID", required = true)
    private String groupIds;

    @ApiModelProperty(value = "类型", required = true)
    private String type;

    @ApiModelProperty(value = "描述说明", required = true)
    private String description;

    @ApiModelProperty(value = "value对应的编码", required = true)
    private String code;

    @ApiModelProperty(value = "名称", required = true)
    private String label;

    @ApiModelProperty(value = "值（前端）", required = true)
    private String value;

    @ApiModelProperty(value = "匹配的字段名", required = true)
    private String fieldName;

    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    @ApiModelProperty(value = "创建时间", required = true)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除：0否1是", required = true)
    private Boolean deleted;


}
