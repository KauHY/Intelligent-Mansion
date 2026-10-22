package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.system.dto.house.HouseRentRecordSaveDTO;
import com.crlink.smartpark.system.dto.house.HouseRentRenewDTO;
import com.crlink.smartpark.system.entity.Company;
import com.crlink.smartpark.system.entity.House;
import com.crlink.smartpark.system.entity.HouseRentRecord;
import com.crlink.smartpark.system.entity.HouseRentRenewRecord;
import com.crlink.smartpark.system.mapper.HouseRentRecordMapper;
import com.crlink.smartpark.system.query.CompanyHouseQuery;
import com.crlink.smartpark.system.query.HouseBindRecordQuery;
import com.crlink.smartpark.system.query.HouseRentRecordQuery;
import com.crlink.smartpark.system.query.HouseRentRenewRecordQuery;
import com.crlink.smartpark.system.service.parkmanagement.CompanyService;
import com.crlink.smartpark.system.service.parkmanagement.HouseRentRecordService;
import com.crlink.smartpark.system.service.parkmanagement.HouseRentRenewRecordService;
import com.crlink.smartpark.system.service.parkmanagement.HouseService;
import com.crlink.smartpark.system.vo.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 房屋入驻记录表 服务实现类
 * </p>
 * */
@Service
public class HouseRentRecordServiceImpl extends ServiceImpl<HouseRentRecordMapper, HouseRentRecord> implements HouseRentRecordService {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private HouseRentRenewRecordService renewRecordService;

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    @Override
    public Page<HouseRentRecord> listPage(HouseRentRecordQuery condition) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindHouse(String companyId, List<HouseRentRecordSaveDTO> recordList) {
        if (CollectionUtil.isEmpty(recordList)) {
            return;
        }
        checkBindParam(recordList);
        //校验入驻企业ID
        Company company = companyService.getById(companyId);
        AssertUtil.requireNotNullOrParamExp(company);
        List<HouseRentRecord> rentRecordList = new ArrayList<>(recordList.size());
        for (HouseRentRecordSaveDTO dto : recordList) {
            HouseRentRecord rentRecord = new HouseRentRecord();
            BeanUtils.copyProperties(dto, rentRecord);
            rentRecord.setCompanyId(companyId);
            rentRecord.setRentFirstEndTime(dto.getRentEndTime());
            EntityUserUtil.setAll(rentRecord);
            rentRecordList.add(rentRecord);
        }
        AssertUtil.requireTrueOrDataExp(saveBatch(rentRecordList));
        //更新公司房屋绑定数
        companyService.updateHouseCount(companyId, recordList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindHouse(String id) {
        HouseRentRecord rentRecord = getById(id);
        AssertUtil.requireNotNullOrParamExp(rentRecord);
        LambdaUpdateWrapper<HouseRentRecord> wrapper = new LambdaUpdateWrapper();
        wrapper.eq(HouseRentRecord::getId, id);
        wrapper.set(HouseRentRecord::getBind, false);
        wrapper.set(HouseRentRecord::getUnbindTime, LocalDate.now());
        wrapper.set(HouseRentRecord::getUpdateBy, SystemUtil.getInstance().getCurrentUserId());
        AssertUtil.requireTrueOrDataExp(update(wrapper));
        //更新公司房屋绑定数
        companyService.updateHouseCount(rentRecord.getCompanyId(), -1);
    }

    @Override
    public PageInfo<HouseBindListVO> pageCompanyHouseBind(CompanyHouseQuery companyHouseQuery) {
        companyHouseQuery.startPage();
        List<HouseBindListVO> list = baseMapper.pageCompanyHouseBind(companyHouseQuery);
        return new PageInfo(list);
    }

    @Override
    public PageInfo<HouseBindRecordListVO> pageHouseBindRecord(HouseBindRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            return new PageInfo<>();
        }
        query.setParkOrgIds(parkOrgIds);
        query.startPage();
        List<HouseBindRecordListVO> list = baseMapper.pageHouseBindRecord(query);
        return new PageInfo(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renewHouse(HouseRentRenewDTO dto) {
        HouseRentRecord houseRentRecord = getById(dto.getId());
        AssertUtil.requireNotNullOrParamExp(houseRentRecord);
        AssertUtil.requireTrue(houseRentRecord.getBind(), "20009");
        AssertUtil.requireTrue(houseRentRecord.getRentEndTime().isBefore(dto.getRentRenewEndTime()), "20017");
        //添加续租记录
        HouseRentRenewRecord renewRecord = new HouseRentRenewRecord();
        renewRecord.setRentRecordId(dto.getId());
        renewRecord.setRentEndTime(houseRentRecord.getRentEndTime());
        renewRecord.setRenewEndTime(dto.getRentRenewEndTime());
        EntityUserUtil.setAll(renewRecord);
        AssertUtil.requireTrueOrDataExp(renewRecordService.save(renewRecord));
        //更新房屋的入驻公司租赁记录
        EntityUserUtil.setUpdateBy(houseRentRecord);
        houseRentRecord.setRentEndTime(dto.getRentRenewEndTime());
        AssertUtil.requireTrueOrDataExp(updateById(houseRentRecord));
    }

    @Override
    public HouseBindVO getRecordById(String id) {
        return baseMapper.getCompanyHouseBindById(id);
    }

    @Override
    public PageInfo<HouseBindRenewListVO> pageHouseRenewBindRecord(HouseRentRenewRecordQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        if (CollectionUtil.isEmpty(parkOrgIds)) {
            return new PageInfo<>();
        }
        query.setParkOrgIds(parkOrgIds);
        query.startPage();
        List<HouseBindRenewListVO> list = baseMapper.pageHouseRenewBindRecord(query);
        return new PageInfo<>(list);
    }

    @Override
    public List<DictVO> getHouseNotBindDict(String companyId, String buildingId) {
        //过滤掉已经绑定的非当前公司的houseId
        Set<String> houseIds = this.lambdaQuery()
                .select(HouseRentRecord::getHouseId)
                .eq(HouseRentRecord::getBind, true)
                .ne(HouseRentRecord::getCompanyId, companyId).list()
                .parallelStream().map(HouseRentRecord::getHouseId).collect(Collectors.toSet());
        List<DictVO> houseDictList = houseService.lambdaQuery().select(House::getId, House::getName)
                .eq(House::getBuildingId, buildingId)
                .notIn(CollectionUtil.isNotEmpty(houseIds), House::getId, houseIds)
                .list().parallelStream().map(t -> new DictVO(t.getName(), t.getId())).collect(Collectors.toList());
        return houseDictList;
    }

    /**
     * 绑定房屋校验参数
     *
     * @param recordList
     */
    private void checkBindParam(List<HouseRentRecordSaveDTO> recordList) {
        //检查时间区间参数
//        for (HouseRentRecordSaveDTO dto : recordList) {
//            AssertUtil.requireTrueOrParamExp(dto.getRentStartTime().isBefore(dto.getRentEndTime()));
//        }
        //校验房屋ID列表
        List<String> houseIds = recordList.stream().map(HouseRentRecordSaveDTO::getHouseId).collect(Collectors.toList());
        LambdaQueryWrapper<House> houseWrapper = new LambdaQueryWrapper();
        houseWrapper.in(House::getId, houseIds);
        AssertUtil.requireTrueOrParamExp(houseService.list(houseWrapper).size() == houseIds.size());
        //校验是否有房屋已被绑定
        LambdaQueryWrapper<HouseRentRecord> rentWrapper = new LambdaQueryWrapper<>();
        rentWrapper.in(HouseRentRecord::getHouseId, houseIds).eq(HouseRentRecord::getBind, true);
        AssertUtil.requireTrue(CollectionUtil.isEmpty(list(rentWrapper)), "20007");
    }

}
