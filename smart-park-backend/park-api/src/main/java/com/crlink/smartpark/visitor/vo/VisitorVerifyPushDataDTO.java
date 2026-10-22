package com.crlink.smartpark.visitor.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** * @desc
 */
@Data
public class VisitorVerifyPushDataDTO {

    @ApiModelProperty(value = "预约信息")
    private VisitorRecordVO visitorRecord;

    @ApiModelProperty(value = "设备访客人脸图")
    private String deviceVisitorFaceImg;

    @ApiModelProperty(value = "设备访客身份证号")
    private String visitorIdCard;

}
