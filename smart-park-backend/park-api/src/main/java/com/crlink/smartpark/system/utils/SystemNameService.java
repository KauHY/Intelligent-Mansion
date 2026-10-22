package com.crlink.smartpark.system.utils;

import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SystemNameService {

    private static CommonProvider commonProvider;

    @Autowired
    public void setCommonProvider(CommonProvider commonProvider) {
        SystemNameService.commonProvider = commonProvider;
    }

    /**
     * 园区名集合
     *
     * @param list
     * @param mapper
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> parkNamesMap(List<T> list, Function<T, String> mapper) {
        Set<String> parkIds = list.stream().map(mapper).collect(Collectors.toSet());
        Map<String, String> parkNameMap = commonProvider.getNamesByIdsAndType(parkIds, SimpleNode.TypeEnum.PARK.getCode());
        return parkNameMap;
    }

    /**
     * 入驻企业名集合
     *
     * @param list
     * @param mapper
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> companyNamesMap(List<T> list, Function<T, String> mapper) {
        Set<String> companyIds = list.stream().map(mapper).collect(Collectors.toSet());
        Map<String, String> companyNameMap = commonProvider.getNamesByIdsAndType(companyIds, SimpleNode.TypeEnum.COMPANY.getCode());
        return companyNameMap;
    }

    /**
     * 入驻企业名集合
     *
     * @param list
     * @param mapper
     * @param <T>
     * @return
     */
    public static <T> Map<String, String> employeeNamesMap(List<T> list, Function<T, String> mapper) {
        Set<String> employeeIds = list.stream().map(mapper).collect(Collectors.toSet());
        Map<String, String> employeeNameMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
        return employeeNameMap;
    }

}
