package com.crlink.smartpark.customer.model.query;

import com.crlink.smartpark.BaseQuery;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.util.List;

/**
 * <p>
 * 报事报修处理流程表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderProcessRecord对象", description="报事报修处理流程表")
public class OrderProcessRecordQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
      private Long id;

    @ApiModelProperty(value = "报修no")
    private String orderNo;

    @ApiModelProperty(value = "流程no")
    private String processNo;

    @ApiModelProperty(value = "上一节点处理NO")
    private String preProNo;

    @ApiModelProperty(value = "处理状态：1待派单,2待处理,8已完成,9已撤销")
    private Integer orderState;

    @ApiModelProperty(value = "处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty(value = "处理备注")
    private String remark;

    @ApiModelProperty(value = "当前处理员工")
    private String staffId;

    @ApiModelProperty(value = "当前处理员工名称")
    private String staffName;

    @ApiModelProperty(value = "当前处理员工手机号")
    private String staffMoblie;

    @ApiModelProperty(value = "上一节点处理员工")
    private String preStaffId;

    @ApiModelProperty(value = "上一节点处理员工名称")
    private String preStaffName;

    @ApiModelProperty(value = "处理状态")
    private Integer state;

    @ApiModelProperty(value = "是否删除：0-否(默认),1-是")
    private Integer deleted;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建者名字")
    private String createByName;

    @ApiModelProperty(value = "修改者")
    private String updateBy;

    @ApiModelProperty(value = "修改者名字")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间	")
    private LocalDateTime updateTime;
    @ApiModelProperty(value = "ids")
    private List<String> ids;


}
