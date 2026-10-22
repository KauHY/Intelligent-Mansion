package com.crlink.smartpark.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 合同模板管理
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("con_template")
@ApiModel(value = "Template数据库对象", description = "合同模板管理")
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "园区Id")
    private String parkOrgId;

    @ApiModelProperty(value = "合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；")
    private Integer type;

    @ApiModelProperty(value = "word模板key")
    private String templateKey;

    @ApiModelProperty(value = "word模板名字")
    private String templateName;

    @ApiModelProperty(value = "生成的合同key")
    private String contractKey;

    @ApiModelProperty(value = "生成的合同时间")
    private LocalDateTime contractKeyTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除状态")
    private Boolean deleted;


}
