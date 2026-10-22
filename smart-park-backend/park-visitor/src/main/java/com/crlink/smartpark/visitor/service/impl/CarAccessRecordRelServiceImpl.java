package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.feign.DahuaIccProvider;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.visitor.entity.CarAccessRecordRel;
import com.crlink.smartpark.visitor.mapper.CarAccessRecordMapper;
import com.crlink.smartpark.visitor.mapper.CarAccessRecordRelMapper;
import com.crlink.smartpark.visitor.query.CarAccessFullRecordListQuery;
import com.crlink.smartpark.visitor.service.CarAccessRecordRelService;
import com.crlink.smartpark.visitor.vo.CarAccessFullRecordListVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 车辆通行记录
 * * @date 2022-10-12
 */
@Service
public class CarAccessRecordRelServiceImpl extends ServiceImpl<CarAccessRecordRelMapper, CarAccessRecordRel> implements CarAccessRecordRelService {

    @Resource
    private CarAccessRecordMapper carAccessRecordMapper;
    @Resource
    private CommonProvider commonProvider;
    //@Resource
    //private DahuaIccProvider dahuaIccProvider;

    @Override
    public PageInfo<CarAccessFullRecordListVO> pageList(CarAccessFullRecordListQuery query) {
        LambdaQueryWrapper wrapper = buildWrapper(query);
        query.startPage();
        List<CarAccessRecordRel> recordList = this.list(wrapper);
        List<CarAccessFullRecordListVO> fullRecords = (List<CarAccessFullRecordListVO>) MyBeanUtils.copyCollection(recordList, CarAccessFullRecordListVO::new, (s, t) -> {
            t.setParkTypeName(DictUtil.getLabel(DictConstant.ParkType.class, s.getParkType()));
        });
        PageInfo pageInfo = new PageInfo(recordList);
        pageInfo.setList(fullRecords);
        return pageInfo;
    }

    private List<CarAccessFullRecordListVO> getRecordVOList(CarAccessFullRecordListQuery query) {
        LambdaQueryWrapper wrapper = buildWrapper(query);
        query.startPage();
        List<CarAccessRecordRel> recordList = this.list(wrapper);
        //Set<String> urls = Sets.newHashSet();
        //recordList.forEach(e -> {
        //    if (StrUtil.isNotBlank(e.getEnterImgUrl())) {
        //        urls.add(e.getEnterImgUrl());
        //    }
        //    if (StrUtil.isNotBlank(e.getExitImgUrl())) {
        //        urls.add(e.getExitImgUrl());
        //    }
        //});
        //Map<String, String> urlsMap = dahuaIccProvider.getFullParkUrls(urls);
        List<CarAccessFullRecordListVO> fullRecords = (List<CarAccessFullRecordListVO>) MyBeanUtils.copyCollection(recordList, CarAccessFullRecordListVO::new, (s, t) -> {
            t.setParkTypeName(DictUtil.getLabel(DictConstant.ParkType.class, s.getParkType()));
            //t.setEnterImgUrl(urlsMap.get(t.getEnterImgUrl()));
            //t.setExitImgUrl(urlsMap.get(t.getExitImgUrl()));
        });
        return fullRecords;
    }

    @Override
    public CarAccessRecordRel getLatestEnterRecord(String carNum, String parkingLotId) {
        return carAccessRecordMapper.getLatestEnterRecord(carNum, parkingLotId);
    }

    @Override
    public void exportList(CarAccessFullRecordListQuery query) {
        query.setParkOrgIds(commonProvider.getCurrentUseParkOrgIds());
        ExcelExportUtils.export(() -> getRecordVOList(query), (Function<List<CarAccessFullRecordListVO>, List<Object>>) null, CarAccessFullRecordListVO.class);
    }

    private LambdaQueryWrapper buildWrapper(CarAccessFullRecordListQuery query) {
        LambdaQueryWrapper<CarAccessRecordRel> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CarAccessRecordRel::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), CarAccessRecordRel::getParkOrgId, query.getParkOrgId());
        wrapper.like(StrUtil.isNotBlank(query.getCarNum()), CarAccessRecordRel::getCarNum, query.getCarNum());
        wrapper.eq(query.getParkType() != null, CarAccessRecordRel::getParkType, query.getParkType());
        wrapper.between(query.getEnterRecognizeTimeStartTime() != null && query.getEnterRecognizeTimeEndTime() != null,
                CarAccessRecordRel::getEnterRecognizeTime, query.getEnterRecognizeTimeStartTime(), query.getEnterRecognizeTimeEndTime());
        wrapper.between(query.getExitRecognizeTimeStartTime() != null && query.getExitRecognizeTimeEndTime() != null,
                CarAccessRecordRel::getExitRecognizeTime, query.getExitRecognizeTimeStartTime(), query.getExitRecognizeTimeEndTime());
        wrapper.orderByDesc(CarAccessRecordRel::getCreateTime);
        return wrapper;
    }

}
