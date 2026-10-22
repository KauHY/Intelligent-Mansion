package com.crlink.smartpark.meeting.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "FaceCheckinPersonDTO", description = "人脸识别签到人员登记请求")
public class FaceCheckinPersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id（编辑时传入）")
    private String id;

    @ApiModelProperty(value = "人员姓名", required = true)
    @NotBlank(message = "人员姓名不能为空")
    private String personName;

    @ApiModelProperty(value = "工号（内部员工）")
    private String employeeNo;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "人员类型 1内部员工 2访客", required = true)
    @javax.validation.constraints.NotNull(message = "人员类型不能为空")
    private Integer personType;

    @ApiModelProperty(value = "人脸照片base64编码")
    private String photoBase64;
}
