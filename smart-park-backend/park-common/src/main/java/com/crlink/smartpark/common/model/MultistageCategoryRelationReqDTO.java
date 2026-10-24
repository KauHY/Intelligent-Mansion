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
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 多级分类关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MultistageCategoryRelation请求对象", description="多级分类关联表")
public class MultistageCategoryRelationReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "多级分类表id", required = true)
    private String categoryId;

    @ApiModelProperty(value = "业务id队列")
    private Set<String> businessIds;

    @ApiModelProperty(value = "业务id")
    private String businessId;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;


    @ApiModelProperty(value = "备注")
    private String remark;


}
