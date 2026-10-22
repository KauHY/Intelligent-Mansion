package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.dto.house.HouseRentRecordSaveDTO;
import com.crlink.smartpark.system.dto.house.HouseRentRenewDTO;
import com.crlink.smartpark.system.entity.HouseRentRecord;
import com.crlink.smartpark.system.query.CompanyHouseQuery;
import com.crlink.smartpark.system.query.HouseBindRecordQuery;
import com.crlink.smartpark.system.query.HouseRentRecordQuery;
import com.crlink.smartpark.system.query.HouseRentRenewRecordQuery;
import com.crlink.smartpark.system.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 * 房屋入驻记录表 服务类
 * </p>
 * */
public interface HouseRentRecordService extends IService<HouseRentRecord> {

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    Page<HouseRentRecord> listPage(HouseRentRecordQuery condition);

    /**
     * 绑定房屋
     *
     * @param companyId
     * @param recordList
     */
    void bindHouse(String companyId, List<HouseRentRecordSaveDTO> recordList);

    /**
     * 解绑房屋
     *
     * @param id
     */
    void unbindHouse(String id);

    /**
     * 企业已绑定房屋分页查询
     *
     * @param companyHouseQuery
     * @return
     */
    PageInfo<HouseBindListVO> pageCompanyHouseBind(CompanyHouseQuery companyHouseQuery);

    /**
     * 房屋入驻记录分页查询
     *
     * @param query
     * @return
     */
    PageInfo<HouseBindRecordListVO> pageHouseBindRecord(HouseBindRecordQuery query);

    /**
     * 续租房屋
     *
     * @param dto
     */
    void renewHouse(HouseRentRenewDTO dto);

    /**
     * 获取房屋租赁详情
     *
     * @param id
     * @return
     */
    HouseBindVO getRecordById(String id);

    /**
     * 房屋续租记录-分页查询
     *
     * @param query
     * @return
     */
    PageInfo<HouseBindRenewListVO> pageHouseRenewBindRecord(HouseRentRenewRecordQuery query);

    /**
     * 绑定房屋，关联房号的下拉列表，过滤掉已经绑定的房屋
     *
     * @param companyId
     * @param buildingId
     * @return
     */
    List<DictVO> getHouseNotBindDict(String companyId, String buildingId);
}
