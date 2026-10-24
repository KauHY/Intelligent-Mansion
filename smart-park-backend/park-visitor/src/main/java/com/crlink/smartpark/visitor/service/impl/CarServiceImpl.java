package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.ExcelImportRollbackListener;
import com.crlink.smartpark.core.utils.ExcelImportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.NoAndIdUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.customer.vo.EmployeeVO;
import com.crlink.smartpark.device.feign.DeviceOperateProvider;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.visitor.dto.CarSaveDTO;
import com.crlink.smartpark.visitor.dto.ParkCarDTO;
import com.crlink.smartpark.visitor.entity.Car;
import com.crlink.smartpark.visitor.entity.ParkingLot;
import com.crlink.smartpark.visitor.mapper.CarMapper;
import com.crlink.smartpark.visitor.model.excel.CarImportDTO;
import com.crlink.smartpark.visitor.query.CarQuery;
import com.crlink.smartpark.visitor.service.CarService;
import com.crlink.smartpark.visitor.service.ParkingLotService;
import com.crlink.smartpark.visitor.vo.CarVO;
import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.DictConstant.PARK_TYPE;

/**
 * <p>
 * 车辆信息 服务实现类
 * </p>
 * */
@Slf4j
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    @Resource
    private ParkingLotService parkingLotService;
    @Resource
    private CommonProvider commonProvider;
    @Resource
    private EmployeeProvider employeeProvider;
    @Resource
    private DeviceOperateProvider deviceOperateProvider;

    /**
     * @return 0-小于start,1-大于等于start&小于等于end，2-大于end
     */
    public static int betweenDateStartAndEnd(LocalDateTime start, LocalDateTime end) {
        LocalDateTime nowTime = LocalDateTime.now();
        if (nowTime.isBefore(start)) {
            return 0;
        }
        if (nowTime.isAfter(end)) {
            return 2;
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateCar(CarSaveDTO dto) {
        log.info("车辆新增or编辑开始: {}", dto);
        //停车类型值校验
        Map<Integer, String> parkType = DictUtil.getLabels(PARK_TYPE);
        AssertUtil.requireTrue(parkType.containsKey(dto.getParkType()), "80009");
        //验证id的有效性
        if (StrUtil.isNotBlank(dto.getId())) {
            AssertUtil.requireTrue(this.count(Wrappers.<Car>lambdaQuery().eq(Car::getId, dto.getId())) == 1, "80007");
        }
        //验证车牌唯一性
        AssertUtil.requireTrue(this.count(Wrappers.<Car>lambdaQuery().eq(Car::getParkOrgId, dto.getParkOrgId())
                .eq(Car::getCarNum, dto.getCarNum()).ne(StrUtil.isNotBlank(dto.getId()), Car::getId, dto.getId())) == 0, "80001");
        //内部车辆
        boolean internalCar = !DictConstant.ParkType.EXTERNAL_CAR.getCode().equals(dto.getParkType());
        EmployeeVO employeeVO = null;
        if (internalCar) {
            //必填项校验
            AssertUtil.requireTrue(StrUtil.isNotBlank(dto.getCompanyId()) && Objects.nonNull(dto.getEmployeeId()), "80010");
            //验证员工的有效性
            employeeVO = employeeProvider.getEmployeeById(dto.getEmployeeId());
            AssertUtil.requireTrue(Objects.nonNull(employeeVO) && Objects.nonNull(employeeVO.getDevicePersonId()), "10017");
        }
        //验证车场的有效性
        List<ParkingLotVO> parkingLots = parkingLotService.getByIds(dto.getParkingLotIds());
        AssertUtil.requireTrue(CollUtil.isNotEmpty(parkingLots) && dto.getParkingLotIds().size() == parkingLots.size(), "80006");
        //查询旧停车场信息
        List<Car> oldCars = this.list(Wrappers.<Car>lambdaQuery().select(Car::getParkingLotIds).eq(Car::getEmployeeId, dto.getEmployeeId()));
        if (CollUtil.isNotEmpty(oldCars)) {
            Set<String> oldParkingLotIds = Sets.newHashSet();
            oldCars.forEach(e -> oldParkingLotIds.addAll(e.getParkingLotIds()));
            oldParkingLotIds.removeAll(dto.getParkingLotIds());
            List<ParkingLotVO> oldLots = parkingLotService.getByIds(oldParkingLotIds);
            if (CollUtil.isNotEmpty(oldLots)) {
                parkingLots.addAll(oldLots);
            }
        }
        Car car = MyBeanUtils.copy(dto, Car::new);
        car.setCarCode(NoAndIdUtil.generateCode());
        car.setCarBrand(0);
        car.setCarType(0);
        car.setCarColor(100);
        car.setCarNumColor(100);
        car.setBlackList(0);
        car.setStatus(betweenDateStartAndEnd(car.getAuthStartTime(), car.getAuthEndTime()));
        if (internalCar) {
            car.setCarOwnerName(employeeVO.getEmployeeName());
            car.setMobile(employeeVO.getMobile());
        }
        EntityUserUtil.setAll(car);

        ParkCarDTO carDTO = MyBeanUtils.copy(car, ParkCarDTO::new);
        if (internalCar) {
            carDTO.setDevicePersonId(employeeVO.getDevicePersonId());
        }
        carDTO.setParkingLotCodes(parkingLots.stream().map(ParkingLotVO::getParkingLotCode).collect(Collectors.toSet()));
        carDTO.setParkingLots(parkingLots);

        if (internalCar) {
            log.info("调用ICC更新车场人员: {}", carDTO);
            String errMsg = deviceOperateProvider.updateParkPerson(carDTO);
            if (Objects.isNull(errMsg)) {
                if (StrUtil.isBlank(dto.getId())) {
                    log.info("调用ICC新增车辆: {}", carDTO);
                    String id = deviceOperateProvider.addCar(carDTO);
                    try {
                        Long.parseLong(id);
                    } catch (NumberFormatException e) {
                        log.error("", e);
                        throw new ResolinkRuntimeException(Message.code("60010", id));
                    }
                    car.setId(id);
                    try {
                        log.info("新增车辆: {}", car);
                        this.save(car);
                    } catch (Exception e) {
                        log.error("", e);
                        this.deleteCar(id);
                        throw new ResolinkRuntimeException("80008");
                    }
                } else {
                    log.info("编辑车辆: {}", car);
                    this.updateById(car);
                }
            } else {
                throw new ResolinkRuntimeException(Message.code("60010", errMsg));
            }
        } else {
            if (StrUtil.isBlank(dto.getId())) {
                log.info("调用ICC新增预约车辆: {}", carDTO);
                String id = deviceOperateProvider.addReservedCar(carDTO);
                try {
                    Long.parseLong(id);
                } catch (NumberFormatException e) {
                    log.error("", e);
                    throw new ResolinkRuntimeException(Message.code("60010", id));
                }
                car.setId(id);
                try {
                    log.info("新增预约车辆: {}", car);
                    this.save(car);
                } catch (Exception e) {
                    log.error("", e);
                    this.deleteCar(id);
                    throw new ResolinkRuntimeException("80008");
                }
            } else {
                log.info("编辑预约车辆: {}", car);
                this.updateById(car);
                log.info("调用ICC修改预约车辆: {}", carDTO);
                String errMsg = deviceOperateProvider.updateReservedCar(carDTO);
                if (Objects.nonNull(errMsg)) {
                    throw new ResolinkRuntimeException(Message.code("60010", errMsg));
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCar(String id) {
        Car car = this.getById(id);
        AssertUtil.requireTrue(Objects.nonNull(car) && Objects.nonNull(car.getParkType()), "10003");
        log.info("车辆管理-删除: {}", car);
        if (this.removeById(id)) {
            String errMsg = null;
            if (DictConstant.ParkType.EXTERNAL_CAR.getCode().equals(car.getParkType())) {
                errMsg = deviceOperateProvider.deleteBatchReservedCar(Sets.newHashSet(id));
            } else {
                errMsg = deviceOperateProvider.deleteBatchCar(Sets.newHashSet(id));
            }
            if (Objects.nonNull(errMsg)) {
                throw new ResolinkRuntimeException(Message.code("60010", errMsg));
            }
        }
    }

    /**
     * 导入Excel
     *
     * @param file
     * @param parkOrgId
     * @param companyId
     * @param parkingLotIds
     * @return
     */
    @Override
    public FileImportErrorInfoDTO importExcel(MultipartFile file, String parkOrgId, String companyId, Set<String> parkingLotIds) {
        String filename = file.getOriginalFilename();
        if (!(filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv"))) {
            throw new ResolinkRuntimeException("10014");
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        ExcelImportRollbackListener<CarImportDTO> importListener = new ExcelImportRollbackListener<>(CarImportDTO.class,
                //业务参数校验方法，并单行保存数据
                t -> {
                    //验证车牌唯一性
                    if (this.count(Wrappers.<Car>lambdaQuery().eq(Car::getParkOrgId, parkOrgId).eq(Car::getCarNum, t.getCarNum().trim())) > 0) {
                        return Message.code("80001").getDisplayText();
                    }
                    //内部车辆
                    boolean internalCar = !DictConstant.ParkType.EXTERNAL_CAR.getCode().equals(t.getParkType());
                    EmployeeVO employeeVO = null;
                    if (internalCar) {
                        //手机号校验
                        employeeVO = employeeProvider.findEmployeeByCompanyAndTel(companyId, t.getMobile());
                        if (Objects.isNull(employeeVO) || !t.getCarOwnerName().trim().equals(employeeVO.getEmployeeName())) {
                            return "姓名或手机号与所选园区企业员工不符";
                        }
                        //验证员工的有效性
                        if (Objects.isNull(employeeVO.getDevicePersonId())) {
                            return Message.code("10017").getDisplayText();
                        }
                    }
                    //停车类型校验
                    if (t.getParkType() == null) {
                        return "停车类型不匹配";
                    }
                    LocalDate authStartDate = null, authEndDate = null;
                    try {
                        authStartDate = LocalDate.parse(t.getAuthStartDateStr(), dtf);
                        authEndDate = LocalDate.parse(t.getAuthEndDateStr(), dtf);
                    } catch (Exception e) {
                        log.error("", e);
                        return "日期格式不正确";
                    }
                    //验证车场的有效性
                    List<ParkingLotVO> parkingLots = parkingLotService.getByIds(parkingLotIds);
                    if (CollUtil.isEmpty(parkingLots) || parkingLotIds.size() != parkingLots.size()) {
                        return Message.code("80006").getDisplayText();
                    }
                    //查询旧停车场信息
                    List<Car> oldCars = this.list(Wrappers.<Car>lambdaQuery().select(Car::getParkingLotIds).eq(Car::getEmployeeId, employeeVO.getId()));
                    if (CollUtil.isNotEmpty(oldCars)) {
                        Set<String> oldParkingLotIds = Sets.newHashSet();
                        oldCars.forEach(e -> oldParkingLotIds.addAll(e.getParkingLotIds()));
                        oldParkingLotIds.removeAll(parkingLotIds);
                        List<ParkingLotVO> oldLots = parkingLotService.getByIds(oldParkingLotIds);
                        if (CollUtil.isNotEmpty(oldLots)) {
                            parkingLots.addAll(oldLots);
                        }
                    }
                    Car car = MyBeanUtils.copy(t, Car::new);
                    car.setCarCode(NoAndIdUtil.generateCode());
                    car.setCarBrand(0);
                    car.setCarType(0);
                    car.setCarColor(100);
                    car.setCarNumColor(100);
                    car.setBlackList(0);
                    car.setParkOrgId(parkOrgId);
                    car.setCompanyId(companyId);
                    car.setEmployeeId(employeeVO.getId());
                    car.setParkingLotIds(parkingLotIds);
                    car.setAuthStartTime(authStartDate.atTime(0, 0, 0));
                    car.setAuthEndTime(authEndDate.atTime(23, 59, 59));
                    car.setStatus(betweenDateStartAndEnd(car.getAuthStartTime(), car.getAuthEndTime()));
                    if (internalCar) {
                        car.setCarOwnerName(employeeVO.getEmployeeName());
                        car.setMobile(employeeVO.getMobile());
                    }
                    EntityUserUtil.setAll(car);

                    ParkCarDTO carDTO = MyBeanUtils.copy(car, ParkCarDTO::new);
                    if (internalCar) {
                        carDTO.setDevicePersonId(employeeVO.getDevicePersonId());
                    }
                    carDTO.setParkingLotCodes(parkingLots.stream().map(ParkingLotVO::getParkingLotCode).collect(Collectors.toSet()));
                    carDTO.setParkingLots(parkingLots);
                    if (internalCar) {
                        String errMsg = deviceOperateProvider.updateParkPerson(carDTO);
                        if (Objects.isNull(errMsg)) {
                            String id = deviceOperateProvider.addCar(carDTO);
                            try {
                                Long.parseLong(id);
                            } catch (NumberFormatException e) {
                                log.error("", e);
                                return Message.code("60010", id).getDisplayText();
                            }
                            car.setId(id);
                            try {
                                this.save(car);
                            } catch (Exception e) {
                                log.error("", e);
                                this.deleteCar(id);
                                return Message.code("80008").getDisplayText();
                            }
                        } else {
                            return Message.code("60010", errMsg).getDisplayText();
                        }
                    } else {
                        String id = deviceOperateProvider.addReservedCar(carDTO);
                        try {
                            Long.parseLong(id);
                        } catch (NumberFormatException e) {
                            log.error("", e);
                            return Message.code("60010", id).getDisplayText();
                        }
                        car.setId(id);
                        try {
                            this.save(car);
                        } catch (Exception e) {
                            log.error("", e);
                            this.deleteCar(id);
                            return Message.code("80008").getDisplayText();
                        }
                    }
                    return null;
                },
                null
        );
        ExcelImportUtils.importExcel(file, CarImportDTO.class, importListener);
        return importListener.getImportResult();
    }

    /**
     * 通过id查询实体
     *
     * @param id 查询条件
     * @return 实体对象
     */
    @Override
    public CarVO getCarById(String id) {
        if (StrUtil.isNotBlank(id)) {
            Car entity = getById(id);
            if (entity != null) {
                CarVO dto = new CarVO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    /**
     * 根据carNum查询
     *
     * @param carNum
     * @return
     */
    @Override
    public CarVO getCarByCarNum(String carNum) {
        if (StrUtil.isNotBlank(carNum)) {
            Car entity = this.getOne(Wrappers.<Car>lambdaQuery().eq(Car::getCarNum, carNum));
            if (entity != null) {
                CarVO dto = new CarVO();
                BeanUtils.copyProperties(entity, dto);
                return dto;
            }
        }
        return null;
    }

    /**
     * 分页查询
     *
     * @param query 查询对象
     * @return 分页结果
     */
    @Override
    public PageInfo<CarVO> listPage(CarQuery query) {
        query.startPage();
        LambdaQueryWrapper<Car> wrapper = buildPageListRecordWrapper(query);
        List<Car> list = list(wrapper);
        return query.createPageInfo(listEntityToListVO(list));
    }

    @NotNull
    private List<CarVO> listEntityToListVO(List<Car> list) {
        Set<String> parkOrgQueryIds = new HashSet<>();
        Set<String> companyQueryIds = new HashSet<>();
        Set<String> parkingLotIds = new HashSet<>();
        list.stream().forEach(e -> {
            parkOrgQueryIds.add(e.getParkOrgId());
            companyQueryIds.add(e.getCompanyId());
            if (CollUtil.isNotEmpty(e.getParkingLotIds())) {
                parkingLotIds.addAll(e.getParkingLotIds());
            }
        });
        Map<String, String> parkNameMap = commonProvider.getNamesByIdsAndType(parkOrgQueryIds, SimpleNode.TypeEnum.PARK.getCode());
        Map<String, String> companyNameMap = commonProvider.getNamesByIdsAndType(companyQueryIds, SimpleNode.TypeEnum.COMPANY.getCode());
        Map<String, String> parkLotNamesMap = null;
        if (CollUtil.isNotEmpty(parkingLotIds)) {
            parkLotNamesMap = parkingLotService.listByIds(parkingLotIds).stream().collect(Collectors.toMap(ParkingLot::getId, ParkingLot::getParkingLot));
        } else {
            parkLotNamesMap = Maps.newHashMap();
        }
        List<CarVO> voList = new ArrayList<>();
        for (Car e : list) {
            CarVO copy = MyBeanUtils.copy(e, CarVO::new);
            copy.setParkOrgName(parkNameMap.get(e.getParkOrgId()));
            copy.setCompanyName(companyNameMap.get(e.getCompanyId()));
            Set<String> parkingNames = Sets.newHashSet();
            if (CollUtil.isNotEmpty(e.getParkingLotIds())) {
                for (String p : e.getParkingLotIds()) {
                    parkingNames.add(parkLotNamesMap.get(p));
                }
            }
            copy.setParkingLots(StrUtil.join(",", parkingNames));
            voList.add(copy);
        }
        return voList;
    }

    /**
     * 封装查询条件
     *
     * @param query 前端查询条件对象
     * @return 查询条件
     */
    private LambdaQueryWrapper<Car> buildPageListRecordWrapper(CarQuery query) {
        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Car::getParkOrgId, SystemUtil.getInstance().getParkOrgIds())
                .eq(StrUtil.isNotBlank(query.getParkOrgId()), Car::getParkOrgId, query.getParkOrgId())
                .eq(StrUtil.isNotBlank(query.getCompanyId()), Car::getCompanyId, query.getCompanyId())
                .eq(Objects.nonNull(query.getParkType()), Car::getParkType, query.getParkType())
                .eq(Objects.nonNull(query.getStatus()), Car::getStatus, query.getStatus())
                .like(StrUtil.isNotBlank(query.getCarNum()), Car::getCarNum, query.getCarNum())
                .like(StrUtil.isNotBlank(query.getCarOwnerName()), Car::getCarOwnerName, query.getCarOwnerName())
                .apply(StrUtil.isNotBlank(query.getParkingLotId()), "FIND_IN_SET(" + query.getParkingLotId() + ",parking_lot_ids)")
                .orderByDesc(Car::getCreateTime);
        return queryWrapper;
    }

}
