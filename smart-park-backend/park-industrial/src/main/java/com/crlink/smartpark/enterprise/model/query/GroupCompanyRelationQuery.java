package com.crlink.smartpark.enterprise.model.query;

import com.crlink.smartpark.BaseQuery;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 公司分组关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="GroupCompanyRelation查询对象", description="公司分组关联表")
public class GroupCompanyRelationQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "群组Id，对应多级分类表id")
    private String categoryId;

    @ApiModelProperty(value = "公司id")
    private String businessId;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;




}
