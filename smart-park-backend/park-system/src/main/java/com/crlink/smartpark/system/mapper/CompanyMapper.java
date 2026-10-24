package com.crlink.smartpark.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.query.CompanyQuery;
import com.crlink.smartpark.system.vo.CompanyListVO;

import java.util.List;

/**
 * <p>
 * 入驻企业表 Mapper 接口
 * </p>
 * */
public interface CompanyMapper extends BaseMapper<Company> {

    /**
     * 列表查询入驻企业
     *
     * @param companyQuery
     * @return
     */
    List<CompanyListVO> listCompany(CompanyQuery companyQuery);
}
