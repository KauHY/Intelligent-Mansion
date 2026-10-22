package com.crlink.smartpark.business.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.business.dto.HouseInfoReqDTO;
import com.crlink.smartpark.business.entity.HouseInfo;
import com.crlink.smartpark.business.query.HouseInfoQuery;
import com.crlink.smartpark.business.query.HouseQuery;
import com.crlink.smartpark.business.query.WxHouseQuery;
import com.crlink.smartpark.business.vo.*;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 * */
public interface HouseInfoService extends IService<HouseInfo> {

    /**
     * 获取意向房源名称 楼栋名+楼层房号+套内面积
     *
     * @param houseInfoIds
     * @return
     */
    List<BusinessHouseVO> getHouseInfoNames(Set<String> houseInfoIds);

    public <T> Map<String, T> houseInfoId2Value(Set<String> houseInfoIds, SFunction<HouseInfo, T> sValue);

//    Map<String, ?> houseId2Value(Set<String> houseId, SFunction<HouseInfo, ?> sValue);

    Map<String, HouseInfo> houseInfoId2HouseInfo(Collection<String> houseInfoIds);

    Boolean saveOrUpdateHouseInfo(HouseInfoReqDTO dto);

    Boolean deleteHouseInfoById(String id);

    HouseInfoVO getHouseInfoById(String id);

    PageInfo<HouseInfoVO> listHouseInfoPage(HouseInfoQuery houseInfoQuery);

    List<DictVO> listBusinessHouseInfoDict(String buildingId);

    /**
     * 查询招商房屋下楼栋
     *
     * @param parkOrgId
     * @return
     */
    List<DictVO> listBuildingDict(String parkOrgId);
    //Set<String> saveHouseInfos(HouseReqDTO dto);

    ///**
    // * 选择意向房源
    // */
    //PageInfo<HouseIntentionVO> listHouseIntentionPage(HouseIntentionQuery queryDTO);

    /**
     * 运营小程序 楼栋房屋
     *
     * @param buildingId
     * @return
     */
    WxHouseVO queryHouses(String buildingId);

    HouseInfoVO getHouseInfoByHouseId(String id);

//    List<WxBuildingVO> queryBuildings();

    List<WxBuildingVO> queryBuildings();

    PageInfo<BusinessHouseVO> queryHousesPage(WxHouseQuery query);

    /**
     * 用于创建招商信息，选择招商房屋
     *
     * @param houseQuery
     * @return
     */
    PageInfo<HouseInfoDictVO> selectListHouseInfoPage(HouseQuery houseQuery);

    /**
     * 、
     * 商机意向房源
     *
     * @param houseQuery
     * @return
     */
    PageInfo<HouseBaseVO> selectIntentionHousePage(HouseQuery houseQuery);


}
