package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.visitor.dto.ParkingLotSaveDTO;
import com.crlink.smartpark.visitor.dto.ParkingLotUpdateDTO;
import com.crlink.smartpark.visitor.entity.Car;
import com.crlink.smartpark.visitor.entity.CarAccessRecordRel;
import com.crlink.smartpark.visitor.entity.CarLane;
import com.crlink.smartpark.visitor.entity.ParkingLot;
import com.crlink.smartpark.visitor.mapper.CarAccessRecordMapper;
import com.crlink.smartpark.visitor.mapper.CarAccessRecordRelMapper;
import com.crlink.smartpark.visitor.mapper.ParkingLotMapper;
import com.crlink.smartpark.visitor.model.vo.CarAccessGroupVO;
import com.crlink.smartpark.visitor.model.vo.ParkingOccupationRatioVO;
import com.crlink.smartpark.visitor.query.ParkingLotListQuery;
import com.crlink.smartpark.visitor.service.CarLaneService;
import com.crlink.smartpark.visitor.service.CarService;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.vo.ParkingLotListVO;
import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 停车场服务类
 * */
@Slf4j
@Service
public class ParkingLotServiceImpl extends ServiceImpl<ParkingLotMapper, ParkingLot> implements ParkingLotService {
    @Resource
    private CarLaneService carLaneService;
    @Resource
    private CarAccessRecordMapper carAccessRecordMapper;
    @Resource
    private CarService carService;
    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    @Resource
    private CarAccessRecordRelMapper carAccessRecordRelMapper;

    @Override
    public void update(ParkingLotUpdateDTO dto) {
        ParkingLot entity = getById(dto.getId());
        AssertUtil.requireNotNullOrParamExp(entity);
        //entity.setTenantId(SystemUtil.getInstance().getCurrentHeadOrgId());
        int count = this.count(Wrappers.<ParkingLot>query().lambda()
                .eq(ParkingLot::getParkOrgId, entity.getParkOrgId())
                .eq(ParkingLot::getParkingLot, dto.getParkingLot().trim())
                .ne(ParkingLot::getId, dto.getId()));
        AssertUtil.requireTrue(count == 0, "80003");
        ParkingLot lot = new ParkingLot();
        lot.setId(dto.getId());
        lot.setAllowVisitor(dto.getAllowVisitor());
        lot.setUpdateTime(LocalDateTime.now());
        EntityUserUtil.setUpdateBy(lot);
        AssertUtil.requireTrueOrDataExp(updateById(lot));
    }

    @Override
    public void removeParkingLotById(String id) {
        //校验是否绑定了车道
        AssertUtil.requireTrue(carLaneService.count(Wrappers.<CarLane>lambdaQuery().eq(CarLane::getParkingLotId, id)) == 0, "80002");
        //校验车辆关联
        AssertUtil.requireTrue(carService.count(Wrappers.<Car>lambdaQuery().apply("FIND_IN_SET(" + id + ",parking_lot_ids)")) == 0, "80005");
        AssertUtil.requireTrueOrDataExp(removeById(id));
    }

    @Override
    public ParkingLotVO getParkingLotById(String id) {
        ParkingLot parkingLot = getById(id);
        if (parkingLot == null) {
            return null;
        }
        ParkingLotVO vo = MyBeanUtils.copy(parkingLot, ParkingLotVO::new);
        return vo;
    }

    @Override
    public ParkingLotVO getParkingLotByCode(String parkingLotCode) {
        if (StrUtil.isBlank(parkingLotCode)) {
            return null;
        }
        ParkingLot parkingLot = this.getOne(Wrappers.<ParkingLot>lambdaQuery().eq(ParkingLot::getParkingLotCode, parkingLotCode));
        if (Objects.isNull(parkingLot)) {
            return null;
        }
        return MyBeanUtils.copy(parkingLot, ParkingLotVO::new);
    }

    @Override
    public List<ParkingLotVO> getByIds(Set<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return null;
        }
        List<ParkingLot> parkingLots = this.list(Wrappers.<ParkingLot>lambdaQuery().in(ParkingLot::getId, ids));
        return MyBeanUtils.copyList(parkingLots, ParkingLotVO::new);
    }

    @Override
    public PageInfo<ParkingLotListVO> pageList(ParkingLotListQuery query) {
        LambdaQueryWrapper<ParkingLot> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ParkingLot::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), ParkingLot::getParkOrgId, query.getParkOrgId());
        wrapper.like(StrUtil.isNotBlank(query.getParkingLot()), ParkingLot::getParkingLot, query.getParkingLot());
        wrapper.orderByDesc(ParkingLot::getCreateTime);
        query.startPage();
        List<ParkingLot> list = list(wrapper);
        List<ParkingLotListVO> voList = MyBeanUtils.copyList(list, ParkingLotListVO::new, null);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

    @Override
    public List<DictVO> getAllByParkOrgId(String parkOrgId) {
        LambdaQueryWrapper<ParkingLot> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ParkingLot::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(ParkingLot::getParkOrgId, parkOrgId);
        wrapper.select(ParkingLot::getParkingLot, ParkingLot::getId);
        List<DictVO> list = list(wrapper).stream().map(e -> new DictVO(e.getParkingLot(), e.getId())).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<DictVO> getParkLotDictList(String parkOrgId) {
        List<DictVO> result = this.lambdaQuery()
                .select(ParkingLot::getId, ParkingLot::getParkingLot)
                .eq(ParkingLot::getParkOrgId, parkOrgId)
                .list().parallelStream().map(e -> new DictVO(e.getParkingLot(), e.getId())).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取停车场占用率（运营大屏）
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public List<ParkingOccupationRatioVO> getOccupationRatio(String parkOrgId) {
        List<ParkingOccupationRatioVO> result = new ArrayList<>();
        LambdaQueryWrapper<ParkingLot> qw = Wrappers.<ParkingLot>lambdaQuery().eq(ParkingLot::getParkOrgId, parkOrgId).orderByAsc(ParkingLot::getId).last("LIMIT 3");
        List<ParkingLot> parkList = this.list(qw);
        if (CollUtil.isNotEmpty(parkList)) {
            for (ParkingLot parkingLot : parkList) {
                ParkingOccupationRatioVO ratioVO = new ParkingOccupationRatioVO();
                ratioVO.setParkingName(parkingLot.getParkingLot());
                ratioVO.setParkCount(parkingLot.getTotalLot() == null ? 0 : parkingLot.getTotalLot());
                // 从通行记录中取停车数量--只要没有出场时间的都归为入场未出场
                Integer stopCount = carAccessRecordRelMapper.selectCount(Wrappers.<CarAccessRecordRel>lambdaQuery()
                        // 根据停车场ID
                        .eq(CarAccessRecordRel::getParkingLotId, parkingLot.getId())
                        // 车牌号不为空
                        .isNotNull(CarAccessRecordRel::getCarNum)
                        // 出场时间是空的
                        .isNull(CarAccessRecordRel::getExitRecognizeTime)
                        // 从2022-12-04 00:00:00开始
                        .gt(CarAccessRecordRel::getCreateTime, LocalDateTime.of(2022, 12, 4, 00, 00, 00))
                );
                ratioVO.setStopCount(stopCount);
                /*
                List<CarAccessGroupVO> gatewayGroupVOList = carAccessRecordMapper.selectAccessGatewayGroup(parkingLot.getId());
                if (CollUtil.isNotEmpty(gatewayGroupVOList)) {
                    CarAccessGroupVO entry = gatewayGroupVOList.get(0);
                    CarAccessGroupVO out = gatewayGroupVOList.get(1);
                    long entryCount = entry == null ? 0L : entry.getAccessCount();
                    long outCount = out == null ? 0L : out.getAccessCount();
                    ratioVO.setStopCount(entryCount - outCount);
                } else {
                    ratioVO.setStopCount(0L);
                }
                */
                result.add(ratioVO);
            }
        }
        return result;
    }

    /**
     * 同步车场车道信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync() {
        List<ParkingLotSaveDTO> lotSaveDTOList = deviceOperateProvider.getParkingLotList();
        log.info("同步车场车道信息: {}", lotSaveDTOList);
        if (CollUtil.isNotEmpty(lotSaveDTOList)) {
            Set<String> parkOrgIds = new HashSet<>();
            Set<String> lotIds = new HashSet<>();
            for (ParkingLotSaveDTO lotDTO : lotSaveDTOList) {
                if (StrUtil.isNotBlank(lotDTO.getParkOrgId())) {
                    parkOrgIds.add(lotDTO.getParkOrgId());
                }
                lotIds.add(lotDTO.getId());
            }
            this.removeByParkOrgId(parkOrgIds, lotIds);
            String userId = SystemUtil.getInstance().getCurrentUserId();
            for (ParkingLotSaveDTO lotDTO : lotSaveDTOList) {
                ParkingLot lot = new ParkingLot();
                BeanUtils.copyProperties(lotDTO, lot);
                lot.setAllowVisitor(DictUtil.getValue(DictConstant.YesOrNo.NO));
                lot.setCreateBy(userId);
                this.save(lot);
                if (CollUtil.isEmpty(lotDTO.getCarLaneList())) {
                    continue;
                }
                List<CarLane> carLaneList = MyBeanUtils.copyList(lotDTO.getCarLaneList(), CarLane::new, (s, t) -> {
                    t.setCreateBy(userId);
                });
                carLaneService.removeByParkingLotId(lotDTO.getId());
                carLaneService.saveBatch(carLaneList);
            }
        }
    }

    /**
     * 根据园区id删除
     *
     * @param parkOrgIds
     * @param ids
     * @return
     */
    @Override
    public int removeByParkOrgId(Set<String> parkOrgIds, Set<String> ids) {
        return baseMapper.deleteByParkOrgId(parkOrgIds, ids);
    }

    /**
     * 获取允许访客停车的车场集合
     *
     * @return
     */
    @Override
    public List<ParkingLotVO> getAllowVisitorList() {
        LambdaQueryWrapper<ParkingLot> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ParkingLot::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.eq(ParkingLot::getAllowVisitor, DictUtil.getValue(DictConstant.YesOrNo.YES));
        List<ParkingLot> parkingLots = list(wrapper);
        return MyBeanUtils.copyList(parkingLots, ParkingLotVO::new);
    }

}
