package com.crlink.smartpark.business.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BusinessInfoReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "园区id")
    @NotBlank(message = "园区id不能为空")
    private String parkOrgId;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    @Length(max = 30, message = "标题最多输入最多30个字")
    private String title;

    @ApiModelProperty(value = "招商时效类型,0限时有效，1:长期有效")
    @NotNull(message = "招商类型不能为空")
    private Integer effectType;

    private LocalDateTime deadline;

    @ApiModelProperty(value = "租售属性，1出租，2出售，3出租出售")
    @NotEmpty(message = "租售属性不能为空")
    private List<Integer> rentOrSale;

    @ApiModelProperty(value = "亮点描述:字典HIGHLIGHT ,0智慧办公,1限时入住,2配套齐全,3交通便利,4临近商圈")
    @NotEmpty(message = "亮点描述不能为空!")
    @Size(max = 5)
    private List<Integer> highlight;

    @ApiModelProperty(value = "咨询顾问（员工）id")
    private String employeeId;

    @ApiModelProperty(value = "咨询电话")
    private String employeePhone;

    @ApiModelProperty(value = "标题图片")
    private String titlePicture;

    @ApiModelProperty(value = "内容详情")
    @NotBlank(message = "详情描述不能为空")
//    @Length(max = 999, message = "详情描述不能超过999个字符")
    private String content;

    @ApiModelProperty(value = "地址")
    private AddressDTO address;

    @ApiModelProperty(value = "招商房屋ID列表")
    private List<String> houseInfoIds;

}
