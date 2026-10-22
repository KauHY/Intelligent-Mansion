package com.crlink.smartpark.system.mapper;

import com.crlink.smartpark.system.entity.HouseRentRecord;
import com.crlink.smartpark.system.query.CompanyHouseQuery;
import com.crlink.smartpark.system.query.HouseBindRecordQuery;
import com.crlink.smartpark.system.query.HouseRentRenewRecordQuery;
import com.crlink.smartpark.system.vo.HouseBindListVO;
import com.crlink.smartpark.system.vo.HouseBindRecordListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.crlink.smartpark.system.vo.HouseBindRenewListVO;
import com.crlink.smartpark.system.vo.HouseBindVO;

import java.util.List;

/**
 * <p>
 * 房屋入驻记录表 Mapper 接口
 * </p>
 * */
public interface HouseRentRecordMapper extends BaseMapper<HouseRentRecord> {

    /**
     * 查询企业的房屋已绑定的房屋
     *
     * @param companyHouseQuery
     * @return
     */
    List<HouseBindListVO> pageCompanyHouseBind(CompanyHouseQuery companyHouseQuery);

    /**
     * 查询房屋的绑定记录
     *
     * @param query
     * @return
     */
    List<HouseBindRecordListVO> pageHouseBindRecord(HouseBindRecordQuery query);

    /**
     * 根据id获取房屋绑定详情
     *
     * @param id
     * @return
     */
    HouseBindVO getCompanyHouseBindById(String id);

    /**
     * 获取房屋租赁详情
     *
     * @param query
     * @return
     */
    List<HouseBindRenewListVO> pageHouseRenewBindRecord(HouseRentRenewRecordQuery query);
}
