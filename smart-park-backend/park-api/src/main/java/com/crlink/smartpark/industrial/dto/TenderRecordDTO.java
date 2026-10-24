package com.crlink.smartpark.industrial.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
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
@ApiModel(value="TenderRecord响应对象", description="投标记录")
public class TenderRecordDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ExcelIgnore
      private String id;

    @ApiModelProperty(value = "招标id")
    @ExcelIgnore
    private String purchaseId;

    @ApiModelProperty(value = "企业名称")
    @ExcelProperty("企业名称")
    @ColumnWidth(20)
    private String companyName;

    @ApiModelProperty(value = "联系人")
    @ExcelProperty("联系人")
    @ColumnWidth(20)
    private String contact;

    @ApiModelProperty(value = "电话")
    @ExcelProperty("联系电话")
    @ColumnWidth(20)
    private String tel;

    @ApiModelProperty(value = "邮箱")
    @ExcelProperty("邮箱")
    @ColumnWidth(20)
    private String mail;

    @ApiModelProperty(value = "备注")
    @ExcelProperty("备注")
    @ColumnWidth(20)
    private String remark;

    @ApiModelProperty(value = "支付招标文件费：0-未支付；1-已支付")
    @ExcelIgnore
    private Integer biddingDocumentsPaid;
    @ExcelProperty("支付招标文件费")
    @ColumnWidth(20)
    private String biddingDocumentsPaidName;

    @ApiModelProperty(value = "保证金缴纳：0未缴纳；1-已缴纳")
    @ExcelIgnore
    private Integer marginPaid;
    @ExcelProperty("保证金缴纳情况")
    @ColumnWidth(20)
    private String marginPaidName;

}
