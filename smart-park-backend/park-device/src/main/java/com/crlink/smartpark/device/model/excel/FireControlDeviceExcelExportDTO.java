package com.crlink.smartpark.device.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.crlink.smartpark.common.annotations.DictField;
import com.crlink.smartpark.common.excel.convert.ExcelJavaType;
import com.crlink.smartpark.core.excel.convert.CommonConvert;
import com.crlink.smartpark.core.excel.convert.DictConverter;
import lombok.Data;

import java.io.Serializable;

import static com.crlink.smartpark.device.common.FireControlConstant.SYSTEM_TYPE;

/**
 * <p>
 * 消防设备
 * </p>
 * */
@Data
public class FireControlDeviceExcelExportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty(value = "设备编号")
    private String deviceNo;

    @ExcelProperty(value = "所属园区", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 1)
    private String parkOrgId;


    @ExcelProperty(value = "所属系统", converter = DictConverter.class)
    @DictField(dictType = SYSTEM_TYPE)
    private Integer systemType;

    @ExcelProperty(value = "设备名称")
    private String name;

    @ExcelProperty(value = "所属楼栋", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 2)
    private String buildingId;

    @ExcelProperty(value = "所属楼层")
    private Integer floor;

    @ExcelProperty(value = "所属房屋", converter = CommonConvert.class)
    @ExcelJavaType(needIdToName = true, idToNameType = 3)
    private String houseId;

    @ExcelProperty(value = "安装位置")
    private String installationLocation;

    //@ApiModelProperty(value = "SN号码")
    //private String serialNumber;
    //
    //@ApiModelProperty(value = "设备id")
    //private String deviceId;
    //
    //@ApiModelProperty(value = "ip地址")
    //private String ip;
    //
    //@ApiModelProperty(value = "端口号")
    //private Integer port;
    //
    //@ApiModelProperty(value = "通信端口")
    //private String communicationPort;


    //@ApiModelProperty(value = "状态")
    //private Integer status;

    //private String createBy;
    //
    //private LocalDateTime createTime;
    //
    //private String updateBy;
    //
    //private LocalDateTime updateTime;
    //
    //private Boolean deleted;


}
