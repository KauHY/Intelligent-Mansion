package com.crlink.smartpark.customer.controller.request.query;

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
 * 领用权限设置
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ProReceivePermission查询对象", description="领用权限设置")
public class ProReceivePermissionQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;


}
