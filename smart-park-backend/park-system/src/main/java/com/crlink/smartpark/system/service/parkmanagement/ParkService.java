package com.crlink.smartpark.system.service.parkmanagement;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.system.entity.Park;
import com.crlink.smartpark.system.query.ParkListQuery;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.MobileParkInfoVO;
import com.crlink.smartpark.system.vo.MobileParkVO;
import com.crlink.smartpark.system.vo.ParkDTO;

import java.util.List;

/**
 * <p>
 * 园区表 服务类
 * </p>
 * */
public interface ParkService extends IService<Park> {

    List<ParkDTO> listParkByOrgIds(ParkListQuery query);


    ///**
    // * 分页获取组织下的园区列表信息
    // *
    // * @param query
    // * @return
    // */
    //PageInfo<ParkDTO> listPageByOrgId(ParkListQuery query);

    /**
     * 查看可见园区名
     *
     * @return
     */
    List<DictVO> listParkName();

    /**
     * 小程序查看园区列表
     *
     * @param longitude
     * @param latitude
     * @param parkOrgName
     * @param appId
     * @return
     */


    MobileParkVO listParksForWx(Double longitude, Double latitude, String parkOrgName, String appId);

    /**
     * 根据园区ID查询 -园区介绍页面
     * @param id:
     * @return  com.crlink.smartpark.system.vo.MobileParkInfoVO
     * @MethodName getParkInfo
     */
    MobileParkInfoVO getParkInfo(String id);

    List<DictVO> listEnterpriseParkDict();

}
