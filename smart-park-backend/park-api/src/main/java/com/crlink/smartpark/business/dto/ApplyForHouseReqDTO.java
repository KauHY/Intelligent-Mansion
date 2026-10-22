package com.crlink.smartpark.business.dto;

import com.crlink.smartpark.common.constants.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * <p>
 * *
 * </p> */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ApplyForHouseReqDTO implements Serializable {

    @ApiModelProperty(value = "招商信息Id")
    @NotBlank(message = "招商信息Id不能为空")
    private String businessInfoId;

    @ApiModelProperty(value = "用户姓名")
    @Length(max = 30, message = "最多输入30个字")
    private String username;

    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = Constants.REGEX_MOBILE_EXACT, message = "请输入正确的手机号码！")
    private String mobile;

    @ApiModelProperty(value = "意向房源:房源招商Id")
    private String houseInfoId;

    @ApiModelProperty(value = "房屋功能：0办公、1商铺、2住宿、3仓库、4其他")
    private Integer houseFunction;

    @ApiModelProperty(value = "企业/个人名称：：取值当前用户已关联的企业，如果关联多个公司，默认按拼音取值第一个关联企业")
    @NotBlank(message = "企业/个人名称不能为空")
    @Length(max = 30, message = "最多输入30个字")
    private String companyName;

    @ApiModelProperty(value = "地点：默认意向房源所属楼栋")
    private String address;
}
