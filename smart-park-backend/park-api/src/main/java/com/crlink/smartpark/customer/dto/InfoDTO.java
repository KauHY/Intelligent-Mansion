package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "资讯")
public class InfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "标题", required = true)
    @NotNull(message = "标题不能为空")
    @Length(max = 100, message = "标题最多输入最多100个字")
    private String title;

    @ApiModelProperty(value = "类型:字典 INFOMATION_TYPE", required = true)
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "推送范围", required = true)
    @NotEmpty
    private List<String> parkOrgIds;

    @ApiModelProperty(value = "投放类型：1 C端，2 运营端", required = true)
    @NotEmpty
    @Size(min = 1, max = 2)
    private List<Integer> putIntoType;

    @ApiModelProperty(value = "图片", required = true)
    @NotNull
    private String picture;

    @ApiModelProperty(value = "生效开始时间", required = true)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.TIMEZONE)
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "内容", required = true)
    @NotNull(message = "内容不能为空")
//    @Length(max = 3000, message = "内容超长")
    private String content;

    @ApiModelProperty(value = "内容类型: 0 内容, 1超链接", required = true)
    @NotNull(message = "内容类型不能为空")
    private Integer contentType;
}
