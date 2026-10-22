package com.crlink.smartpark.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.inspection.controller.request.InspectionPointReqDTO;
import com.crlink.smartpark.inspection.controller.request.PointQuery;
import com.crlink.smartpark.inspection.dto.InspectionPointDTO;
import com.crlink.smartpark.inspection.entity.InspectionPoint;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 巡检点表 服务类
 * </p>
 * */
public interface InspectionPointService extends IService<InspectionPoint> {


    Boolean saveOrUpdatePoint(InspectionPointReqDTO reqDTO);

    Boolean deletedPointById(String id);

    PageInfo<InspectionPointDTO> listPointPage(PointQuery pointQuery);

    void export(PointQuery query);

    FileImportErrorInfoDTO importPoints(MultipartFile file, String parkOrgId);
}
