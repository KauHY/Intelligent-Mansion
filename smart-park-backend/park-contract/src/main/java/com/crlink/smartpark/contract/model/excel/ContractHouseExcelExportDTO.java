package com.crlink.smartpark.contract.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.annotations.Id2objectField;
import com.crlink.smartpark.common.constants.enums.Id2objCodeConstant;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import com.crlink.smartpark.core.excel.convert.ParkOrgNameConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @ClassName: ContractHouseExcelExportDTO
 * @Package com.crlink.smartpark.contract.model.excel
 * @Description: 合同房屋视图导出
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContractHouseExcelExportDTO {
    private static final long serialVersionUID = 1L;

    @ColumnWidth(20)
    @ExcelProperty(value = "楼层房号")
    private String houseName;

    @ColumnWidth(20)
    @ExcelProperty(value = "所属园区", converter = ParkOrgNameConverter.class)
    private String parkOrgId;


    @ColumnWidth(20)
    @ExcelProperty(value = "楼栋ID", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 2)
    private String buildingId;


    @ColumnWidth(20)
    @ExcelProperty(value = "所在楼层")
    private Integer floor;


    @ColumnWidth(20)
    @ExcelProperty(value = "房屋状态", converter = DictConverter.class)
    @DictField(dictType = "HOUSE_STATUS")
    private Integer houseStatus;



    @ColumnWidth(20)
    @ExcelProperty(value = "合同编号")
    private String contractNo;


    @ColumnWidth(20)
    @ExcelProperty(value = "合同类型", converter = DictConverter.class)
    @DictField(dictType = "CONTRACT_TYPE")
    private Integer type;

    @ColumnWidth(20)
    @ExcelProperty(value = "合同状态", converter = DictConverter.class)
    @DictField(dictType = "CONTRACT_STATE")
    private Integer state;

}
