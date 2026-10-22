package com.crlink.smartpark.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.device.dto.FireControlDeviceDTO;
import com.crlink.smartpark.device.entity.FireControlDevice;
import com.crlink.smartpark.device.mapper.FireControlDeviceMapper;
import com.crlink.smartpark.device.model.FireControlDeviceReqDTO;
import com.crlink.smartpark.device.model.excel.FireControlDeviceExcelExportDTO;
import com.crlink.smartpark.device.model.query.FireControlDeviceQuery;
import com.crlink.smartpark.device.service.FireControlDeviceService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.SMALL_DATE_FORMAT;
import static com.crlink.smartpark.device.common.FireControlConstant.FIRE_CONTROL_DEVICE_EXPORT_FILENAME;


/**
 * 消防设备 服务实现类
 * */
@Service
public class FireControlDeviceServiceImpl extends ServiceImpl<FireControlDeviceMapper, FireControlDevice> implements FireControlDeviceService {

    //编号前缀
    public static final String FIRE_CONTROL_DEVICE_NUMBER = "FD";

    @Autowired
    private NumberProvider numberProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<FireControlDevice, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<FireControlDevice> lambdaQuery = this.lambdaQuery().in(FireControlDevice::getId, ids);
        if (flag) {
            lambdaQuery.select(FireControlDevice::getId, sValue);
        }
        Stream<FireControlDevice> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(FireControlDevice::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(FireControlDevice::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateFireControlDevice(FireControlDeviceReqDTO reqDTO) {
        checkDeviceId(reqDTO);
        checkSerialNum(reqDTO);
        checkName(reqDTO);
        FireControlDevice entity = MyBeanUtils.copy(reqDTO, FireControlDevice::new);
        EntityUserUtil.setAllDynamic(entity);
        if (StringUtils.isBlank(reqDTO.getId())) {
            String no = getNum();
            entity.setDeviceNo(no);
        }
        return this.saveOrUpdate(entity);
    }


    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public FireControlDeviceDTO getFireControlDeviceById(String id) {
        FireControlDevice entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<FireControlDeviceDTO> listFireControlDevicePage(FireControlDeviceQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrRetEmpty();
        query.startPage();
        List<FireControlDevice> list = this.list(getConditionWrapper(query, parkOrgIds));
        MyPageInfoUtils.isEmptyPage(list);
        List<FireControlDeviceDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public void exportFireControlDevice(FireControlDeviceQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        ExcelExportUtils.export(() -> this.list(getConditionWrapper(query, parkOrgIds)),
                entities -> MyBeanUtils.copyList(entities, FireControlDeviceExcelExportDTO::new),
                FIRE_CONTROL_DEVICE_EXPORT_FILENAME, FireControlDeviceExcelExportDTO.class);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<FireControlDevice> getConditionWrapper(FireControlDeviceQuery query, Set<String> parkOrgIds) {
        LambdaQueryWrapper<FireControlDevice> queryWrapper = new LambdaQueryWrapper<FireControlDevice>();
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), FireControlDevice::getParkOrgId, query.getParkOrgId());
        queryWrapper.eq(StringUtils.isNotBlank(query.getBuildingId()), FireControlDevice::getBuildingId, query.getBuildingId());
        queryWrapper.eq(query.getFloor() != null, FireControlDevice::getFloor, query.getFloor());
        queryWrapper.eq(StringUtils.isNotBlank(query.getHouseId()), FireControlDevice::getHouseId, query.getHouseId());
        queryWrapper.eq(query.getSystemType() != null, FireControlDevice::getSystemType, query.getSystemType());
        queryWrapper.like(StringUtils.isNotBlank(query.getName()), FireControlDevice::getName, query.getName());
        queryWrapper.in(FireControlDevice::getParkOrgId, parkOrgIds);
        queryWrapper.orderByDesc(FireControlDevice::getCreateTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<FireControlDeviceDTO> entitiesToDTOs(List<FireControlDevice> entities) {
        return MyBeanUtils.copyList(entities, FireControlDeviceDTO::new, (s, t) -> {

        });
    }

    private void checkSerialNum(FireControlDeviceReqDTO reqDTO) {
        FireControlDevice meter = this.lambdaQuery().select(FireControlDevice::getId).eq(FireControlDevice::getSerialNumber, reqDTO.getSerialNumber())
                .ne(StringUtils.isNotBlank(reqDTO.getId()), FireControlDevice::getId, reqDTO.getId()).last("limit 1").one();
        if (Objects.nonNull(meter)) {
            throw new ResolinkRuntimeException("81002");
        }
    }

    private void checkDeviceId(FireControlDeviceReqDTO reqDTO) {
        FireControlDevice meter = this.lambdaQuery().select(FireControlDevice::getId).eq(FireControlDevice::getDeviceId, reqDTO.getDeviceId())
                .ne(StringUtils.isNotBlank(reqDTO.getId()), FireControlDevice::getId, reqDTO.getId()).last("limit 1").one();
        if (Objects.nonNull(meter)) {
            throw new ResolinkRuntimeException("81003");
        }
    }

    private void checkName(FireControlDeviceReqDTO reqDTO) {
        FireControlDevice meter = this.lambdaQuery().select(FireControlDevice::getId).eq(FireControlDevice::getName, reqDTO.getName()).eq(FireControlDevice::getParkOrgId, reqDTO.getParkOrgId())
                .ne(StringUtils.isNotBlank(reqDTO.getId()), FireControlDevice::getId, reqDTO.getId()).last("limit 1").one();
        if (Objects.nonNull(meter)) {
            throw new ResolinkRuntimeException("81004");
        }
    }

    private String getNum() {
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(FIRE_CONTROL_DEVICE_NUMBER);
        String no = new StringBuilder(FIRE_CONTROL_DEVICE_NUMBER).append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                .append(numberSeq.getValue()).toString();
        return no;
    }
}
