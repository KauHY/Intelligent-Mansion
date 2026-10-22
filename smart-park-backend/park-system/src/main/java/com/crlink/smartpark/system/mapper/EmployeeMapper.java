package com.crlink.smartpark.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 入住企业员工信息表 Mapper 接口
 * </p>
 * */
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * 查询物业公司员工
     */
    List<Employee> listEmployeeByLikeName(@Param("parkOrgIds") Collection<String> parkOrgIds,
                                          @Param("likeName") String likeName,
                                          @Param("companyType") Integer companyType,
                                          @Param("companyId") String companyId);

    /**
     * 投诉、报修使用
     *
     * @param employeeIds
     * @return
     */
    List<Employee> orderRecordQueryEmployeeColumns(@Param("employeeIds") Collection<String> employeeIds);
}
