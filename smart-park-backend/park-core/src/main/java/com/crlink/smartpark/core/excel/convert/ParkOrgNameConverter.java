package com.crlink.smartpark.core.excel.convert;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.crlink.smartpark.core.utils.SpringContextUtils;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class ParkOrgNameConverter implements Converter<String> {
    private Map<String, String> name2IdMap;

    private Map<String, String> id2NameMap;

    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String parkOrgId = Optional.ofNullable(cellData.getStringValue()).map(t ->
        {
            if (ObjectUtil.isEmpty(name2IdMap)) {
                name2IdMap = SpringContextUtils.getBean(CommonProvider.class).getIdsByNamesAndType(null, SimpleNode.TypeEnum.PARK.getCode(), null);
            }
            String id = name2IdMap.getOrDefault(t, "");
            return id;
        }).orElse("");
        return parkOrgId;
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String parkOrgName = Optional.ofNullable(value).map(t ->
        {
            if (ObjectUtil.isEmpty(id2NameMap) || Objects.isNull(id2NameMap.get(t))) {
                id2NameMap = SpringContextUtils.getBean(CommonProvider.class).getNamesByIdsAndType(Sets.newHashSet(t), SimpleNode.TypeEnum.PARK.getCode());
            }
            String name = id2NameMap.getOrDefault(t, "");
            return name;
        }).orElse("");
        return new CellData(parkOrgName);
    }
}
