package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 会议室物资管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingGoods查询对象", description="会议室物资管理")
public class MeetingGoodsQuery extends BaseQuery implements Serializable {


    @ApiModelProperty(value = "所属园区")
    private String parkOrgId;

    @ApiModelProperty(value = "物资类型 1购买型、2租用型")
    private Integer goodsType;

    @ApiModelProperty(value = "物资名称")
    private String goodsName;

    @ApiModelProperty(value = "选择物资 传0")
    private Integer queryType;


}
