package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.CarLaneUpdateDTO;
import com.crlink.smartpark.visitor.entity.CarLane;
import com.crlink.smartpark.visitor.entity.ParkingLot;
import com.crlink.smartpark.visitor.mapper.CarLaneMapper;
import com.crlink.smartpark.visitor.query.CarLaneListQuery;
import com.crlink.smartpark.visitor.service.CarLaneService;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.vo.CarLaneListVO;
import com.crlink.smartpark.visitor.vo.CarLaneVO;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 车道服务类
 * */
@Service
public class CarLaneServiceImpl extends ServiceImpl<CarLaneMapper, CarLane> implements CarLaneService {

    @Resource
    private ParkingLotService parkingLotService;

    @Override
    public void update(CarLaneUpdateDTO dto) {
        CarLane entity = getById(dto.getId());
        AssertUtil.requireNotNullOrParamExp(entity);
        //String tenantId = SystemUtil.getInstance().getCurrentHeadOrgId();
        int count = this.count(Wrappers.<CarLane>query().lambda()
                .eq(CarLane::getParkOrgId, entity.getParkOrgId())
                .eq(CarLane::getChannelName, dto.getLaneName().trim())
                .ne(CarLane::getId, dto.getId()));
        AssertUtil.requireTrue(count == 0, "80004");
        entity.setChannelName(dto.getLaneName().trim());
        EntityUserUtil.setUpdateBy(entity);
        AssertUtil.requireTrueOrDataExp(updateById(entity));
    }

    @Override
    public void removeCarLaneById(String id) {
        AssertUtil.requireTrueOrDataExp(removeById(id));
    }

    @Override
    public CarLaneVO getCarLaneById(String id) {
        CarLane carLane = getById(id);
        return parseCarLaneVO(carLane);
    }

    @Override
    public CarLaneVO getCarLaneByIp(String ip) {
        CarLane carLane = this.getOne(Wrappers.<CarLane>lambdaQuery().eq(CarLane::getDeviceIp, ip));
        return parseCarLaneVO(carLane);
    }

    private CarLaneVO parseCarLaneVO(CarLane carLane) {
        if (carLane == null) {
            return null;
        }
        CarLaneVO vo = MyBeanUtils.copy(carLane, CarLaneVO::new, (s, t) -> {
            t.setLaneName(s.getChannelName());
        });
        return vo;
    }

    @Override
    public PageInfo<CarLaneListVO> pageList(CarLaneListQuery query) {
        LambdaQueryWrapper<CarLane> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CarLane::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrRetEmpty());
        wrapper.like(StringUtils.isNotBlank(query.getLaneName()), CarLane::getChannelName, query.getLaneName());
        wrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), CarLane::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StringUtils.isNotBlank(query.getParkingLotId()), CarLane::getParkingLotId, query.getParkingLotId());
        wrapper.orderByDesc(CarLane::getCreateTime);
        query.startPage();
        List<CarLane> list = list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<CarLaneListVO> voList = MyBeanUtils.copyList(list, CarLaneListVO::new, (s, t) -> {
            t.setLaneName(s.getChannelName());
        });
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

    @Override
    public List<DictVO> selectCarLane(String parkingLotId) {
        List<DictVO> dictVOS = this.lambdaQuery().select(CarLane::getId, CarLane::getChannelName)
                .in(StrUtil.isNotBlank(parkingLotId), CarLane::getParkingLotId, parkingLotId)
                .list().stream().map(carLane -> new DictVO(carLane.getChannelName(), carLane.getId()))
                .collect(Collectors.toList());
        return dictVOS;
    }

    @Override
    public int removeByParkingLotId(String parkingLotId) {
        return baseMapper.deleteByParkingLotId(parkingLotId);
    }

}
