package com.crlink.smartpark.system.service.parkmanagement.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.ExcelImportRollbackListener;
import com.crlink.smartpark.core.utils.ExcelImportUtils;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.system.dto.house.BuildingSaveDTO;
import com.crlink.smartpark.system.entity.Building;
import com.crlink.smartpark.system.entity.House;
import com.crlink.smartpark.system.entity.Organization;
import com.crlink.smartpark.system.mapper.BuildingMapper;
import com.crlink.smartpark.system.model.BuildingExcelImportDTO;
import com.crlink.smartpark.system.service.common.OrganizationService;
import com.crlink.smartpark.system.service.parkmanagement.BuildingService;
import com.crlink.smartpark.system.service.parkmanagement.HouseService;
import com.crlink.smartpark.system.service.parkmanagement.ParkService;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.crlink.smartpark.system.vo.builiding.BuildingTreeVO;
import com.crlink.smartpark.system.vo.builiding.BuildingVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 楼栋表 服务实现类
 * </p>
 * */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Autowired
    private HouseService houseService;

    @Autowired
    private ParkService parkService;

    @Autowired
    private OrganizationService organizationService;

    @Override
    public Map<String, ?> id2ValueMap(Set<String> ids, SFunction<Building, ?> sValue) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<Building> lambdaQuery = this.lambdaQuery().in(Building::getId, ids);
        if (flag) {
            lambdaQuery.select(Building::getId, sValue);
        }
        Stream<Building> stream = lambdaQuery.list().stream();
        Map<String, ?> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(Building::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(Building::getId, Function.identity()));
        }
        return result;
    }

    @Override
    public Map<String, ?> name2IdMap(Set<String> names, @NotNull String parkOrgId) {
        Map<String, String> name2IdMap = this.lambdaQuery()
                .select(Building::getId, Building::getName)
                .eq(StringUtils.isNotBlank(parkOrgId), Building::getParkOrgId, parkOrgId)
                .in(ObjectUtils.isNotEmpty(names), Building::getName, names)
                .list().parallelStream().collect(Collectors.toMap(Building::getName, Building::getId, (k1, k2) -> k2));
        return name2IdMap;
    }

    @Override
    public void saveOrUpdateBuilding(BuildingSaveDTO buildingSaveDTO) {
        checkSaveParam(buildingSaveDTO);
        Building building = MyBeanUtils.copy(buildingSaveDTO, Building::new);
        EntityUserUtil.setAll(building, StringUtils.isBlank(buildingSaveDTO.getId()));
        AssertUtil.requireTrueOrDataExp(saveOrUpdate(building));
    }

    @Override
    public FileImportErrorInfoDTO importBuildings(MultipartFile file, String parkOrgId) {
        String filename = file.getOriginalFilename();
        if (filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv")) {
            ExcelImportRollbackListener<BuildingExcelImportDTO> importListener = new ExcelImportRollbackListener<>(BuildingExcelImportDTO.class,
                    //业务参数校验方法,并单行保存数据
                    t -> validAndOperateExcelDTO(t, parkOrgId),
                    null
            );
            ExcelImportUtils.importExcel(file, BuildingExcelImportDTO.class, importListener);
            return importListener.getImportResult();
        }
        throw new ResolinkRuntimeException("10014");
    }

    /**
     * v12导入只作新增
     *
     * @param dto
     * @param parkOrgId
     * @return
     */
    private String validAndOperateExcelDTO(BuildingExcelImportDTO dto, String parkOrgId) {
        dto.setParkOrgId(parkOrgId);
//        //房屋编码：此为国土局法定编码，若编码已存在，则对应行将导入失败。
//        if (StringUtils.isNotBlank(dto.getCode()) && checkBuildingCode(dto.getCode())) {
//            return "编号已经存在！";
//        }
//        //同一园区、同一楼栋下，若楼层房号名称相同，则以待导入的数据为准更新系统已有数据。
//        checkBuilding(dto);
        Building afterBuilding = MyBeanUtils.copy(dto, Building::new);
        BuildingSaveDTO buildingSaveDTO = MyBeanUtils.copy(dto, BuildingSaveDTO::new);
        checkSaveParam(buildingSaveDTO);
        EntityUserUtil.setAllDynamic(afterBuilding);
        return this.saveOrUpdate(afterBuilding) ? null : "插入数据失败！";
    }

    private boolean checkBuildingCode(String code) {
        Building one = this.lambdaQuery().select(Building::getId).eq(Building::getCode, code).last("limit 1").one();
        return Objects.nonNull(one);
    }

    private void checkBuilding(BuildingExcelImportDTO dto) {
        Building one = this.lambdaQuery().select(Building::getId).eq(Building::getName, dto.getName())
                .eq(Building::getParkOrgId, dto.getParkOrgId()).one();
        if (Objects.nonNull(one)) {
            dto.setId(one.getId());
        }
    }

    @Override
    public Boolean removeBuildingById(String id) {
        if (houseService.getOne(new LambdaQueryWrapper<House>().select(House::getId).eq(House::getBuildingId, id), false) != null) {
            throw new ResolinkRuntimeException("20002");
        }
        return removeById(id);
    }

    @Override
    public List<DictVO> listBuildingDictByParkOrgId(String parkOrgId) {
        List<Building> buildings = this.list(new LambdaQueryWrapper<Building>().select(Building::getId, Building::getName, Building::getTotalFloor).eq(Building::getParkOrgId, parkOrgId).orderByAsc(Building::getName));
        List<DictVO> buildingDict = buildings.parallelStream().map(t -> {
            SimpleNode node = new SimpleNode(t.getId(), t.getName(), SimpleNode.TypeEnum.BUILDING.getCode());
            setBuildingExtraInfo(node, t);
            return node;
        }).collect(Collectors.toList());
        return buildingDict;
    }

    private void setBuildingExtraInfo(SimpleNode node, Building building) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalFloor", building.getTotalFloor());
        node.setExtraInfo(jsonObject);
    }

    @Override
    public BuildingVO getBuildingById(String id) {
        Building building = this.getById(id);
        if (building == null) {
            return null;
        }
        BuildingVO vo = new BuildingVO();
        BeanUtils.copyProperties(building, vo);
        return vo;
    }

    @Override
    public BuildingTreeVO listBuildingTreeByParkOrgId(String parkOrgId) {
        Organization parkOrg = organizationService.getOne(Wrappers.<Organization>lambdaQuery().select(Organization::getId, Organization::getOrgName).eq(Organization::getId, parkOrgId), false);
        BuildingTreeVO parkTreeVO = new BuildingTreeVO(parkOrg.getId(), parkOrg.getOrgName(), SimpleNode.TypeEnum.PARK.getCode());
        List<Building> buildings = this.list(new LambdaQueryWrapper<Building>().select(Building::getId, Building::getName).eq(Building::getParkOrgId, parkOrgId).orderByAsc(Building::getName));
        List<BuildingTreeVO> buildingTreeVOS = buildings.parallelStream().map(t -> new BuildingTreeVO(t.getId(), t.getName(), SimpleNode.TypeEnum.BUILDING.getCode())).collect(Collectors.toList());
        buildingTreeVOS.forEach(buildingTree -> buildHouseTree(buildingTree));
        parkTreeVO.setChildren(buildingTreeVOS);
        return parkTreeVO;
    }

    @Override
    public Integer getBuildingTotalFloor(String buildingId) {
        Building building = this.lambdaQuery().select(Building::getId, Building::getTotalFloor).eq(Building::getId, buildingId).one();
        return building.getTotalFloor();
    }

    /**
     * 22.7.13 产品要求楼层房号为纯数字，按照从小到大排序
     *
     * @param buildingTreeVO
     */
    private void buildHouseTree(BuildingTreeVO buildingTreeVO) {
        List<House> houses = houseService.list(Wrappers.<House>lambdaQuery().select(House::getId, House::getName).eq(House::getBuildingId, buildingTreeVO.getValue())
//                .orderByAsc(House::getName));
                .last("order by name+0 ASC"));
        List<BuildingTreeVO> houseTree = houses.parallelStream().map(t -> new BuildingTreeVO(t.getId(), t.getName(), SimpleNode.TypeEnum.HOUSE.getCode())).collect(Collectors.toList());
        buildingTreeVO.setChildren(houseTree);
    }

    private void checkSaveParam(BuildingSaveDTO building) {
        LambdaQueryWrapper<Building> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Building::getParkOrgId, building.getParkOrgId());
        wrapper.eq(Building::getName, building.getName());
        wrapper.ne(StringUtils.isNotBlank(building.getId()), Building::getId, building.getId());
        //针对当前园区，楼栋单元名称不可重复
        if (this.count(wrapper) > 0) {
            throw new ResolinkRuntimeException("20003");
        }
        wrapper.clear();
        wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Building::getParkOrgId, building.getParkOrgId());
        wrapper.eq(Building::getCode, building.getCode());
        wrapper.ne(StringUtils.isNotBlank(building.getId()), Building::getId, building.getId());
        //此编码为国土局法定编码，在整个平台内不允许重复
        if (this.count(wrapper) > 0) {
            throw new ResolinkRuntimeException("20004");
        }
    }

}
