package com.crlink.smartpark.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 多级分类关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("com_multistage_category_relation")
@ApiModel(value="MultistageCategoryRelation数据库对象", description="多级分类关联表")
@AllArgsConstructor
@NoArgsConstructor
public class MultistageCategoryRelation implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "多级分类表id")
    private String categoryId;

    @ApiModelProperty(value = "业务id")
    private String businessId;

    @ApiModelProperty(value = "园区id")
    private String parkOrgId;

    @ApiModelProperty(value = "备注")
    private String remark;

    private Boolean deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;



    public MultistageCategoryRelation(String categoryId, String businessId, String parkOrgId, String remark, String createBy, String updateBy) {
        this.categoryId = categoryId;
        this.businessId = businessId;
        this.parkOrgId = parkOrgId;
        this.remark = remark;
        this.createBy = createBy;
        this.updateBy = updateBy;
    }
}
