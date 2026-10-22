package com.crlink.smartpark.enterprise.model.query;

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
 * 企业服务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EnterpriseService查询对象", description="企业服务")
public class EnterpriseServiceQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "服务名")
    private String name;

    @ApiModelProperty(value = "园区")
    private String parkOrgId;

    @ApiModelProperty(value = "所在分组：0-服务指南；1-政策申报；2-其他")
    private Integer groupType;

}
