package com.crlink.smartpark.inspection.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.inspection.controller.request.CircuitQuery;
import com.crlink.smartpark.inspection.controller.request.CircuitRelatePointReqDTO;
import com.crlink.smartpark.inspection.controller.request.InspectionCircuitReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionCircuitDTO;
import com.crlink.smartpark.inspection.dto.InspectionPointDTO;
import com.crlink.smartpark.inspection.entity.InspectionCircuit;
import com.crlink.smartpark.system.vo.DictVO;
import com.github.pagehelper.PageInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 巡检路线 服务类
 * </p>
 * */
public interface InspectionCircuitService extends IService<InspectionCircuit> {


    Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<InspectionCircuit, ?> sValue);

    Map<String, List<InspectionPointDTO>> queryPointByCircuitIds(Collection<String> circuitIds);

    Boolean saveOrUpdateCircuit(InspectionCircuitReqDTO reqDTO);


    PageInfo<InspectionCircuitDTO> listCircuitPage(CircuitQuery circuitQuery);

    Boolean deleteCircuit(String id);

    InspectionCircuitDTO getInspectionCircuitById(String id);

    Boolean circuitRelatePoints(CircuitRelatePointReqDTO dto);

    List<InspectionCircuit> test(InspectionCircuitReqDTO reqDTO);

    List<DictVO> selectCircuits(String parkOrgId, Integer type);
}
