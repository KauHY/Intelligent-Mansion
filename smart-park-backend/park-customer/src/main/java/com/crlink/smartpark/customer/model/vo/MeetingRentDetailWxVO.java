package com.crlink.smartpark.customer.model.vo;

import com.crlink.smartpark.common.annotations.Id2objectNestingField;
import com.crlink.smartpark.customer.dto.MeetingGoodsDTO;
import com.crlink.smartpark.customer.dto.MeetingPermissionDTO;
import com.crlink.smartpark.customer.dto.MeetingRentDTO;
import com.crlink.smartpark.customer.dto.MeetingScheduleTimeDTO;
import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: MeetingRentDetailWxVO
 * @Package com.crlink.smartpark.customer.model.vo
 * @Description: MeetingRentDetailWxVO视图
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MeetingRentWx响应对象", description="会议室租赁管理")
public class MeetingRentDetailWxVO  implements Serializable {

    @ApiModelProperty(value = "会议室基础信息")
    @Id2objectNestingField()
    private MeetingRentDTO meetingRentDTO;

    @ApiModelProperty(value = "物资集合")
    @Id2objectNestingField()
    private List<MeetingGoodsDTO> goodsList;

    @ApiModelProperty(value = "可预订时段集合")
    private List<MeetingScheduleTimeDTO> timeList;


}
