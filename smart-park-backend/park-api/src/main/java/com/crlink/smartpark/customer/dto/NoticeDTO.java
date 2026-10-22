package com.crlink.smartpark.customer.dto;

import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.system.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import static com.crlink.smartpark.common.constants.enums.Id2objCodeConstant.USER_NAME_CODE;

/**
 * <p>
 * 公告信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Notice响应对象", description = "公告信息表")
public class NoticeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

    @ApiModelProperty(value = "公告类型")
    private Integer type;

    @ApiModelProperty(value = "园区组织Id")
    private String parkOrgId;

    @ApiModelProperty(value = "园区名")
    @Id2objectField(code = Id2objCodeConstant.PARK_ORG_NAME_CODE)
    private String parkOrgName;

    @ApiModelProperty(value = "推送范围：0 不限， 1 指定公司/人员")
    private Boolean withoutLimit;

    @ApiModelProperty(value = "推送范围指定公司id")
    private List<String> companyIds;

    @ApiModelProperty(value = "推送范围指定员工id")
    private List<DictVO> employeeIds;

    @ApiModelProperty(value = "投放类型：1 C端，2 运营端")
    private List<Integer> putIntoType;

    @ApiModelProperty(value = "是否弹窗提醒")
    private Boolean popuped;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeStart;

    @ApiModelProperty(value = "生效开始时间")
    private LocalDateTime effectTimeEnd;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "附件")
    private String files;

    @ApiModelProperty(value = "浏览量")
    private Long views;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    private String createBy;

    @ApiModelProperty(value = "发布人")
    @Id2objectField(code = USER_NAME_CODE, idField = "createBy")
    private String createByName;

}
