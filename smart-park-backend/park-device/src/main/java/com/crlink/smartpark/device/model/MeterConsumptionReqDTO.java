package com.crlink.smartpark.device.model;

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
 * 企业或房屋水电用量
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeterConsumption请求对象", description="企业或房屋水电用量")
public class MeterConsumptionReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "0房屋用量记录，1企业用量记录", required = true)
    private Boolean type;

    @ApiModelProperty(value = "园区Id", required = true)
    private String parkOrgId;

    @ApiModelProperty(value = "结算月份", required = true)
    private String settledMonth;

    @ApiModelProperty(value = "企业Id", required = true)
    private String companyId;

    @ApiModelProperty(value = "合同类型", required = true)
    private Boolean contractType;

    @ApiModelProperty(value = "楼栋id", required = true)
    private String buildingId;

    @ApiModelProperty(value = "房屋id集合", required = true)
    private String houseIds;

    @ApiModelProperty(value = "用量", required = true)
    private String houseConsumption;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updateBy;

    private Boolean deleted;


}
