package com.crlink.smartpark.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.inspection.controller.request.TaskPointDetailReqDTO;
import com.crlink.smartpark.inspection.entity.InspectionPointDetail;

/**
 * <p>
 * 服务类
 * </p>
 * */
public interface InspectionPointDetailService extends IService<InspectionPointDetail> {


    Boolean closePointDetail(String id);

    /**
     * 巡检点巡检
     *
     * @param detailDTO
     * @return
     */
    Boolean reportPointDetail(TaskPointDetailReqDTO detailDTO);
}
