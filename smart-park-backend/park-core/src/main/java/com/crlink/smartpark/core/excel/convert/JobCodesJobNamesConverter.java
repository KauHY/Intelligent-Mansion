package com.crlink.smartpark.core.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.crlink.smartpark.core.utils.DictUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JobCodesJobNamesConverter implements Converter<List<Integer>> {
    public static final String JOB_CODE_TYPE = "JOBCODE";

    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public List<Integer> convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(List<Integer> jobCodes, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (jobCodes != null && jobCodes.size() > 0) {
            Map<Integer, String> jobCode2LabelMap = DictUtil.getLabels(JOB_CODE_TYPE);
            String data = jobCodes.parallelStream().map(t -> jobCode2LabelMap.get(t)).collect(Collectors.joining(","));
            return new CellData(data);
        }
        return new CellData("-");
    }
}
