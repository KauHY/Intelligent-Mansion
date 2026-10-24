package com.crlink.smartpark.enterprise.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 投标记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ind_tender_record")
@ApiModel(value="TenderRecord数据库对象", description="投标记录")
public class TenderRecord implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "招标id")
    private String purchaseId;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "支付招标文件费：0-未支付；1-已支付")
    private Integer biddingDocumentsPaid;

    @ApiModelProperty(value = "保证金缴纳：0未缴纳；1-已缴纳")
    private Integer marginPaid;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;

    private Boolean deleted;


}
