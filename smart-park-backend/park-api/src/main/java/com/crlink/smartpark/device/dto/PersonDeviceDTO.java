package com.crlink.smartpark.device.dto;

import com.crlink.smartpark.common.constants.enums.DevicePersonTypeEnum;
import com.crlink.smartpark.system.dto.employee.PersonFingerData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/** * @desc
 */
@Data
public class PersonDeviceDTO {

    /**
     * 人员或访客记录的id
     */
    private String id;

    @ApiModelProperty(value = "设备人员ID")
    private Long devicePersonId;

    @ApiModelProperty(value = "员工姓名")
    private String personName;

    @ApiModelProperty(value = "手机号")
    private String mobilePhone;

    @ApiModelProperty(value = "照片字节数组")
    private byte[] picBytes;

    @ApiModelProperty(value = "照片文件对象")
    private String picImgUrl;

    @ApiModelProperty(value = "照片文件名")
    private String picFileName;

    @ApiModelProperty(value = "人员卡号（用于大华设备推送）")
    private String cardNo;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ApiModelProperty(value = "人员类型（用于大华设备推送）")
    private DevicePersonTypeEnum personType;

    @ApiModelProperty(value = "指纹数据")
    private List<PersonFingerData> fingers;

    private String companyId;

    private List<String> tagIds;
}
