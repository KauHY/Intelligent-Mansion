package com.crlink.smartpark.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.inspection.controller.request.TaskQuery;
import com.crlink.smartpark.inspection.controller.request.TaskRotateReqDTO;
import com.crlink.smartpark.inspection.dto.InspectionTaskDTO;
import com.crlink.smartpark.inspection.dto.InspectionTaskDetailVO;
import com.crlink.smartpark.inspection.entity.InspectionTask;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 * */
public interface InspectionTaskService extends IService<InspectionTask> {


    PageInfo<InspectionTaskDTO> listTaskPage(TaskQuery query);

    @Transactional
    void generateInspectionTask();

    /**
     * 巡检人接单处理巡检 任务
     *
     * @param id
     * @param employeeId
     * @return
     */
    Boolean acceptTask(String id, String employeeId);

    InspectionTaskDTO findTaskById(String id);

//    PageInfo<InspectionTaskDTO> pendingTaskList(BaseQuery query);

//    PageInfo<InspectionTaskDTO> historyTaskList(BaseQuery query);
//
//    PageInfo<InspectionTaskDTO> ongoingTaskList(BaseQuery query);

    PageInfo<InspectionTaskDTO> wxTaskList(BaseQuery query, Integer listType);


    Boolean rotateTask(TaskRotateReqDTO dto);


    Map<Integer, Long> getTodayTaskCount(String parkOrgId);

    List<InspectionTaskDetailVO> get3DayExceptionalTaskDetail(String parkOrgId);
}
