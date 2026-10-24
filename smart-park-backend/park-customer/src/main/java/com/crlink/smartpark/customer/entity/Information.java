package com.crlink.smartpark.customer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 资讯信息表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "cus_information", autoResultMap = true)
public class Information implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;


    private String title;

    @TableField(exist = false)
    private List<String> parkOrgIds;

    private Integer type;

    /**
     * 投放类型：1 C端，2 运营端，3 C端 运营端
     */
    @ApiModelProperty(value = "投放类型")
    private Integer putIntoType;


    @ApiModelProperty(value = "浏览量")
    private Long views;

    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effectTimeStart;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容类型：0 内容, 1超链接'
     */
    private Integer contentType;

    /**
     * 图片
     */
    private String picture;

    private Integer status;

    /**
     * 0,未删除，1删除
     */
    private Boolean deleted;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

}
