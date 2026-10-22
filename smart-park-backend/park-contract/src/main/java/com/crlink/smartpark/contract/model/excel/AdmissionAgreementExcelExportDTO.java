package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.LocalDateCNConverter;
import com.crlink.smartpark.common.excel.convert.LocalDateConverter;
import com.crlink.smartpark.contract.dto.ContractBillingPlanDTO;
import com.crlink.smartpark.contract.dto.Extension;
import com.crlink.smartpark.contract.dto.HouseMessage;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import com.crlink.smartpark.system.dto.house.HouseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName: AdmissionAgreementExcelExportDTO
 * @Package com.crlink.smartpark.contract.model.excel
 * @Description: 入园协议导出DTO type=1
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdmissionAgreementExcelExportDTO {

    @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    @ColumnWidth(20)
    private String parkOrgId;

    @ExcelProperty(value = "协议编号")
    @ColumnWidth(20)
    private String no;

    @ExcelProperty(value = "入驻企业名称")
    @ColumnWidth(20)
    private String nameB;


    /**
     * todo 特殊处理
     */
    @ExcelProperty(value = "房源名称")
    @ColumnWidth(20)
    private String houseMessagesStr;



    @ExcelProperty(value = "总面积（㎡）")
    @ColumnWidth(20)
    private Double totalArea;

    /**
     * todo 2021年11月1号
     */
    @ExcelProperty(value = "起租日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate startDate;
    /**
     * 2021年11月1号
     */
    @ExcelProperty(value = "截止日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate endDate;
    /**
     * 合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中
     */
    @ExcelProperty(value = "协议状态", converter = DictConverter.class)
    @DictField(dictType = "CONTRACT_STATE")
    @ColumnWidth(20)
    private Integer state;

    /**
     * todo 经办人id，多个时；分隔
     */
    @ExcelProperty(value = "经办人")
    @ColumnWidth(20)
    private String employeeId;

    /**
     * 2021年11月1号
     */
    @ExcelProperty(value = "办理日期",converter = LocalDateCNConverter.class)
    @ColumnWidth(20)
    private LocalDate contractedDate;

    @ExcelProperty(value = "承租人姓名")
    @ColumnWidth(20)
    private String contactB;


}
