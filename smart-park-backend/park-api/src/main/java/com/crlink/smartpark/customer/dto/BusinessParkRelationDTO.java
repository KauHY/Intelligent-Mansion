package com.crlink.smartpark.customer.dto;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 业务和园区多对多关系表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BusinessParkRelation对象", description="业务和园区多对多关系表")
public class BusinessParkRelationDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    private String businessId;

    private Integer parkOrgId;

    private Integer businessType;

    private Integer status;

    private Boolean deleted;


}
