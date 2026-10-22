package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 资讯信息
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "资讯", description = "资讯")
public class InformationDTO implements Serializable {

    private String id;

    @ApiModelProperty(value = "标题", required = true)
    @Length(max = 100, message = "标题最多输入最多100个字")
    private String title;

    @ApiModelProperty(value = "类型", required = true)
    private Integer type;

    @ApiModelProperty(value = "推送范围", required = true)
    @NotEmpty
    private List<String> parkOrgIds;
    private List<String> parkOrgNames;

    @ApiModelProperty(value = "投放类型", required = true)
    private List<Integer> putIntoType;


    @ApiModelProperty(value = "生效时间", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "图片", required = true)
    @NotNull
    private String picture;

    @ApiModelProperty(value = "内容", required = true)
    @Length(max = 3000, message = "内容超长")
    private String content;

    @ApiModelProperty(value = "内容类型: 0 内容, 1超链接", required = true)
    private Integer contentType;

    @ApiModelProperty(value = "浏览量")
    private Long views;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发布人")
    @Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    private String createByName;

    private String createBy;
}
