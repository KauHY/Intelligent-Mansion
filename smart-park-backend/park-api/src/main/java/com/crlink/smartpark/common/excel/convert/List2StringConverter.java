package com.crlink.smartpark.common.excel.convert;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.List;
import java.util.Objects;

public class List2StringConverter implements Converter<List> {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public List convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(List ts, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (ts != null && ts.size() > 0) {
            StringBuilder data = new StringBuilder();
            for (Object t : ts) {
                if (Objects.nonNull(t)) {
                    if (t instanceof String) {
                        data.append(t);
                    } else {
                        //todo
                        data.append(t.toString());
                    }
                    data.append(",");
                    int length = data.length();
                    //去掉最后一个逗号
                    data.delete(length - 1, length);
                    return new CellData(data.toString());
//            String data = ts.stream().map(t-> t.toString()).collect(Collectors.joining(","));
//            return new CellData(data);
                } else {
//                    return new CellData("-");
                    return new CellData("");
                }
            }
        }
        return new CellData("");
    }
}
