package com.crlink.smartpark.core.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;

import java.util.Map;

public abstract class ExcelImportFirstListener extends AnalysisEventListener {
    @Override
    public void invoke(Object data, AnalysisContext context) {
        //对象转换前的操作
        Map<Integer, CellData> dataMap = ((Map<Integer, CellData>) data);
        //ExcelReadHeadProperty excelReadHeadProperty = context.currentReadHolder().excelReadHeadProperty();
        //Map<Integer, Head> headMap = excelReadHeadProperty.getHeadMap();
        //dataMap.forEach((key, CellData) -> {
        //    Field field = ReflectUtil.getField(excelReadHeadProperty.getHeadClazz(), headMap.get(key).getFieldName());
        //});
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
