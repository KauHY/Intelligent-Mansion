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
 * 多级分类关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MultistageCategoryRelation查询对象", description="多级分类关联表")
public class MultistageCategoryRelationQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;


    @ApiModelProperty(value = "多级分类表id")
    private String categoryId;

    @ApiModelProperty(value = "业务id")
    private String businessId;

}
