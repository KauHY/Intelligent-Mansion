package com.crlink.smartpark.enterprise.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 投标记录
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TenderRecord请求对象", description="投标记录")
public class TenderRecordReqDTO implements Serializable {

    private static final long serialVersionUID=1L;

      private String id;

    @ApiModelProperty(value = "招标id", required = true)
    private String purchaseId;

    @ApiModelProperty(value = "企业名称", required = true)
    @NotBlank
    @Length(max = 30)
    private String companyName;

    @ApiModelProperty(value = "联系人", required = true)
    @NotBlank
    @Length(max = 30)
    private String contact;

    @ApiModelProperty(value = "电话", required = true)
    @NotBlank
    private String tel;

    @ApiModelProperty(value = "邮箱")
    private String mail;

    @ApiModelProperty(value = "备注")
    @Length(max = 100)
    private String remark;

    @ApiModelProperty(value = "支付招标文件费：0-未支付；1-已支付")
    private Integer biddingDocumentsPaid;

    @ApiModelProperty(value = "保证金缴纳：0未缴纳；1-已缴纳")
    private Integer marginPaid;

}
