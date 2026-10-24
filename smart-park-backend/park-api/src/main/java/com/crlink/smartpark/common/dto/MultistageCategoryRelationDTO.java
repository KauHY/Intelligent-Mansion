package com.crlink.smartpark.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 多级分类关联表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MultistageCategoryRelation响应对象", description="多级分类关联表")
public class MultistageCategoryRelationDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "多级分类id")
    @Id2objectField(code = Id2objCodeConstant.CATEGORY_NAME_CODE, targetFieldName = "categoryName")
    private String categoryId;

    @ApiModelProperty(value = "多级分类id")
    private String categoryName;

    @ApiModelProperty(value = "多级分类ids")
    @Id2objectField(code = Id2objCodeConstant.CATEGORY_NAME_CODE, targetFieldName = "categoryNames")
    private List<String> categoryIds;

    @ApiModelProperty(value = "多级分类名")
    private List<String> categoryNames;

    @ApiModelProperty(value = "园区id")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE, targetFieldName = "parkOrgName")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    private String parkOrgName;

    @ApiModelProperty(value = "业务id")
    private String businessId;

    @ApiModelProperty(value = "业务ids")
    private List<String> businessIds;

    @ApiModelProperty(value = "备注")
    private String remark;


}
