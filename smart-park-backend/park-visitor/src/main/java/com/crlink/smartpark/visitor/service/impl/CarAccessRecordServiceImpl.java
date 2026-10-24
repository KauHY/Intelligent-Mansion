package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.ExcelExportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.RocketmqUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.device.feign.DeviceInfoProvider;
import com.crlink.smartpark.device.vo.DeviceInfoVO;
import com.crlink.smartpark.visitor.dto.CarAccessRecordDTO;
import com.crlink.smartpark.visitor.entity.CarAccessRecord;
import com.crlink.smartpark.visitor.entity.CarAccessRecordRel;
import com.crlink.smartpark.visitor.mapper.CarAccessRecordMapper;
import com.crlink.smartpark.visitor.model.vo.CarAccessRecordMqMsgVO;
import com.crlink.smartpark.visitor.query.CarAccessRecordListQuery;
import com.crlink.smartpark.visitor.service.CarAccessRecordRelService;
import com.crlink.smartpark.visitor.service.CarAccessRecordService;
import com.crlink.smartpark.visitor.service.CarService;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.vo.CarAccessRecordListVO;
import com.crlink.smartpark.visitor.vo.CarAccessRecordVO;
import com.crlink.smartpark.visitor.vo.CarVO;
import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.crlink.smartpark.common.constants.enums.RocketmqConstant.RocketmqTopicEnum.CAR_ACCESS_TOPIC;

/**
 * 车牌抓拍记录服务类
 * */
@Slf4j
@Service
public class CarAccessRecordServiceImpl extends ServiceImpl<CarAccessRecordMapper, CarAccessRecord> implements CarAccessRecordService {
    @Resource
    private DeviceInfoProvider deviceInfoProvider;
    @Resource
    private CarService carService;
    @Resource
    private ParkingLotService parkingLotService;
    @Resource
    private CarAccessRecordRelService carAccessRecordRelService;
    @Resource
    private FileProvider fileProvider;

    @Value("${scene_image_url.host:https://park.hakjkg.com}")
    private String host;
    private String GET_PIC_PREFIX = "/smartPark/park-common/file/get?key={}&name={}";

    @Value("#{${car_lane_mapping}}")
    private Map<String, String> carLaneMapping;

    @Override
    public PageInfo<CarAccessRecordListVO> pageListRecord(CarAccessRecordListQuery query) {
        LambdaQueryWrapper<CarAccessRecord> wrapper = buildPageListRecordWrapper(query);
        query.startPage();
        List<CarAccessRecord> list = list(wrapper);
        List<CarAccessRecordListVO> voList = buildCarAccessRecordListVOS(list);
        PageInfo pageInfo = new PageInfo(list);
        pageInfo.setList(voList);
        return pageInfo;
    }

    @Override
    public void exportListRecord(CarAccessRecordListQuery query) {
        LambdaQueryWrapper<CarAccessRecord> wrapper = buildPageListRecordWrapper(query);
        ExcelExportUtils.export(() -> list(wrapper), sourceList -> buildCarAccessRecordListVOS(sourceList), CarAccessRecordListVO.class);
    }

    @Override
    public List<CarAccessRecordVO> getCarAccessRecords(String parkLotId, LocalDateTime startTime) {
        List<CarAccessRecord> list = this.lambdaQuery().eq(CarAccessRecord::getParkingLotId, parkLotId)
                //.ge(CarAccessRecord::getRecognizeTime, startTime)
                .orderByDesc(CarAccessRecord::getRecognizeTime)
                .last("limit 3")
                .list();
        List<CarAccessRecordVO> result = (List<CarAccessRecordVO>) MyBeanUtils.copyCollection(list, CarAccessRecordVO::new, (s, t) -> {
            // 填充车辆类型，1 员工车辆，2 公务车辆，3 外部车辆
            t.setCarType(s.getParkType());
            // 出口
            if (s.getGatewayType() != null && s.getGatewayType() == 1) {
                t.setDepartureTime(s.getRecognizeTime());
                t.setRecognizeTime(null);
                // 通过抓拍记录查询通行记录并获取入场时间
                List<CarAccessRecordRel> temp = carAccessRecordRelService.lambdaQuery()
                        // 车牌号
                        .eq(CarAccessRecordRel::getCarNum, s.getCarNum())
                        // 出场时间
                        .eq(CarAccessRecordRel::getExitRecognizeTime, s.getRecognizeTime())
                        .orderByDesc(CarAccessRecordRel::getId)
                        .last("limit 1")
                        .list();
                if (CollectionUtil.isNotEmpty(temp)) {
                    t.setRecognizeTime(temp.get(0).getEnterRecognizeTime());
                }
            }
            // 地址转换
            if (StringUtils.isNotBlank(s.getImgUrl())) {
                String imgUrl = s.getImgUrl();
                String fileName = StrUtil.subAfter(imgUrl, '/', true);
                String fullUrl = host + StrUtil.format(GET_PIC_PREFIX, imgUrl, fileName);
                t.setImgUrl(fullUrl);
            }
        });
        return result;
    }

    /**
     * 保存车辆通行（用于通行事件）
     * @param recordDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveEventRecord(CarAccessRecordDTO recordDTO) {
        log.info("车辆通行事件:{}", JSONUtil.toJsonStr(recordDTO));
        CarAccessRecord entity = MyBeanUtils.copy(recordDTO, CarAccessRecord::new);
        String carNum = entity.getCarNum();
        //填充停车类型
        CarVO car = null;
        if (StrUtil.isNotBlank(carNum)) {
            car = carService.getCarByCarNum(carNum);
        }
        entity.setParkType(Optional.ofNullable(car).map(CarVO::getParkType).orElse(3));
        if (Objects.isNull(entity.getParkType())) {
            entity.setParkType(DictUtil.getValue(DictConstant.ParkType.EXTERNAL_CAR));
        }
        //填充停车场、园区信息
        ParkingLotVO parkingLotVO = parkingLotService.getParkingLotByCode(recordDTO.getParkingLotCode());
        if (Objects.nonNull(parkingLotVO)) {
            entity.setParkingLotId(parkingLotVO.getId());
            entity.setParkOrgId(parkingLotVO.getParkOrgId());
            entity.setParkOrgName(parkingLotVO.getParkOrgName());
        }
        //填充设备名
        DeviceInfoVO deviceInfoVO = deviceInfoProvider.getByDeviceId(recordDTO.getDeviceId());
        if (Objects.nonNull(deviceInfoVO)) {
            entity.setDeviceName(deviceInfoVO.getDeviceName());
        }
        //图片存储
        log.info("车辆通行事件-图片存储: {}", entity.getImgUrl());
        String imgUrl = fileProvider.uploadByUrl(entity.getImgUrl());
        log.info("originImgUrl={}, imgUrl={}", entity.getImgUrl(), imgUrl);
        /*if (StrUtil.isBlank(imgUrl)) {
            Map<String, String> urlsMap = dahuaIccProvider.getFullParkUrls(Sets.newHashSet(entity.getImgUrl()));
            imgUrl = urlsMap.get(entity.getImgUrl());
            log.info("originImgUrl={}, imgUrl2={}", entity.getImgUrl(), imgUrl);
        }*/
        entity.setImgUrl(imgUrl);
        //保存到DB
        boolean carRecord = save(entity);
        //发送MQ
        sendCarAccessMQMsg(entity);
        if (carRecord) {
            Integer gatewayType = entity.getGatewayType();
            //0入口
            if (DictUtil.getValue(DictConstant.GatewayType.ENTRANCE).equals(gatewayType)) {
                CarAccessRecordRel recordRel = MyBeanUtils.copy(entity, CarAccessRecordRel::new);
                recordRel.setEnterImgUrl(imgUrl);
                recordRel.setEnterCarLaneId(entity.getCarLaneId());
                recordRel.setEnterCarLaneName(entity.getCarLaneName());
                recordRel.setEnterRecognizeTime(entity.getRecognizeTime());
                carAccessRecordRelService.save(recordRel);
            }
            //1出口
            else {
                CarAccessRecordRel recordRel = null;
                if (StrUtil.isNotBlank(carNum)) {
                    recordRel = carAccessRecordRelService.getLatestEnterRecord(carNum, entity.getParkingLotId());
                }
                if (Objects.nonNull(recordRel)) {
                    recordRel.setExitImgUrl(imgUrl);
                    recordRel.setExitCarLaneId(entity.getCarLaneId());
                    recordRel.setExitCarLaneName(entity.getCarLaneName());
                    recordRel.setExitRecognizeTime(entity.getRecognizeTime());
                    carAccessRecordRelService.updateById(recordRel);
                } else {
                    recordRel = MyBeanUtils.copy(entity, CarAccessRecordRel::new);
                    recordRel.setExitImgUrl(imgUrl);
                    recordRel.setExitCarLaneId(entity.getCarLaneId());
                    recordRel.setExitCarLaneName(entity.getCarLaneName());
                    recordRel.setExitRecognizeTime(entity.getRecognizeTime());
                    carAccessRecordRelService.save(recordRel);
                }
            }
        }
        return carRecord;
    }

    private void sendCarAccessMQMsg(CarAccessRecord entity) {
        String imgUrl = entity.getImgUrl();
        String fileName = StrUtil.subAfter(imgUrl, '/', true);
        String fullUrl = host + StrUtil.format(GET_PIC_PREFIX, imgUrl, fileName);
        CarAccessRecordMqMsgVO msgVO = new CarAccessRecordMqMsgVO();
        BeanUtils.copyProperties(entity, msgVO);
        msgVO.setPicUrl(fullUrl);
        msgVO.setTime(entity.getRecognizeTime().format(DateTimeFormatter.ofPattern(Constants.DEFAULT_DATE_TIME_FORMAT)));
        // 车道映射配置
        msgVO.setDevChnid(carLaneMapping.get(entity.getCarLaneId()));
        msgVO.setDevChnname(entity.getCarLaneName());
        log.info("发送车辆通行MQ消息: {}, {}", CAR_ACCESS_TOPIC.name(), msgVO);
        RocketmqUtil.sendSyncMessageWithoutTag(CAR_ACCESS_TOPIC, msgVO);
    }

    private List<CarAccessRecordListVO> buildCarAccessRecordListVOS(List<CarAccessRecord> list) {
        //Map<String, String> urlsMap = dahuaIccProvider.getFullParkUrls(list.stream().filter(e -> StrUtil.isNotBlank(e.getImgUrl())).map(CarAccessRecord::getImgUrl).collect(Collectors.toSet()));
        return MyBeanUtils.copyList(list, CarAccessRecordListVO::new, (s, t) -> {
            t.setParkTypeName(DictUtil.getLabel(DictConstant.ParkType.class, s.getParkType()));
            t.setGatewayTypeName(DictUtil.getLabel(DictConstant.GatewayType.class, s.getGatewayType()));
            t.setAccessResultName(DictUtil.getLabel(DictConstant.AccessResult.class, s.getAccessResult()));
            //t.setImgUrl(urlsMap.get(t.getImgUrl()));
        });
    }

    /**
     * 封装查询条件
     *
     * @param query
     * @return
     */
    private LambdaQueryWrapper<CarAccessRecord> buildPageListRecordWrapper(CarAccessRecordListQuery query) {
        LambdaQueryWrapper<CarAccessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CarAccessRecord::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrFakeIds());
        wrapper.like(StringUtils.isNotBlank(query.getCarNum()), CarAccessRecord::getCarNum, query.getCarNum());
        wrapper.eq(query.getParkType() != null, CarAccessRecord::getParkType, query.getParkType());
        wrapper.ge(query.getRecognizeTimeStartTime() != null, CarAccessRecord::getRecognizeTime, query.getRecognizeTimeStartTime());
        wrapper.le(query.getRecognizeTimeEndTime() != null, CarAccessRecord::getRecognizeTime, query.getRecognizeTimeEndTime());
        wrapper.eq(StringUtils.isNotBlank(query.getDeviceId()), CarAccessRecord::getDeviceId, query.getDeviceId());
        wrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), CarAccessRecord::getParkOrgId, query.getParkOrgId());
        wrapper.eq(StringUtils.isNotBlank(query.getParkingLotId()), CarAccessRecord::getParkingLotId, query.getParkingLotId());
        wrapper.eq(StringUtils.isNotBlank(query.getCarLaneId()), CarAccessRecord::getCarLaneId, query.getCarLaneId());
        wrapper.eq(query.getGatewayType() != null, CarAccessRecord::getGatewayType, query.getGatewayType());
        wrapper.eq(query.getAccessResult() != null, CarAccessRecord::getAccessResult, query.getAccessResult());
        wrapper.orderByDesc(CarAccessRecord::getRecognizeTime);
        return wrapper;
    }

}
