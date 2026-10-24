package com.crlink.smartpark.inspection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.inspection.controller.request.TaskPointDetailReqDTO;
import com.crlink.smartpark.inspection.entity.InspectionPointDetail;
import com.crlink.smartpark.inspection.entity.InspectionTask;
import com.crlink.smartpark.inspection.mapper.InspectionPointDetailMapper;
import com.crlink.smartpark.inspection.service.InspectionPointDetailService;
import com.crlink.smartpark.inspection.service.InspectionTaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crlink.smartpark.inspection.common.Constant.InspectionTaskStatus.ENDED;
import static com.crlink.smartpark.inspection.common.Constant.InspectionTaskStatus.EXECUTING;
import static com.crlink.smartpark.inspection.common.Constant.TaskPointDetailStatus.*;

/**
 * <p>
 * 服务实现类
 * </p>
 * */
@Service
public class InspectionPointDetailServiceImpl extends ServiceImpl<InspectionPointDetailMapper, InspectionPointDetail> implements InspectionPointDetailService {

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean closePointDetail(String id) {
        InspectionPointDetail pointDetail = this.getById(id);
        InspectionTask task = inspectionTaskService.getById(pointDetail.getTaskId());
        pointDetail.setStatus(INCOMPLETE.status);
        EntityUserUtil.setAllDynamic(pointDetail);
        this.updateById(pointDetail);
        //根据巡检点判断巡检任务状态
        task.setStatus(getTaskStatusByPointsStatus(task.getId()));
        setTaskCompletionTime(task);
        EntityUserUtil.setUpdateBy(task);
        return inspectionTaskService.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean reportPointDetail(TaskPointDetailReqDTO detailDTO) {
        LocalTime now = LocalTime.now();
        InspectionPointDetail pointDetail = this.getById(detailDTO.getId());
        InspectionTask task = inspectionTaskService.getById(pointDetail.getTaskId());
        boolean flag = now.isAfter(task.getExecutionTimeStart()) && now.isBefore(task.getExecutionTimeEnd());
        int status = flag ? POINT_ENDED.status : TIMEOUT_END.status;
        BeanUtils.copyProperties(detailDTO, pointDetail);
        pointDetail.setStatus(status);
        EntityUserUtil.setAllDynamic(pointDetail);
        this.updateById(pointDetail);
        task.setStatus(getTaskStatusByPointsStatus(task.getId()));
        setTaskCompletionTime(task);
        EntityUserUtil.setUpdateBy(task);
        return inspectionTaskService.updateById(task);
    }

    //根据巡检点状态查询巡检任务状态
    private int getTaskStatusByPointsStatus(String taskId) {
        Set<Integer> statusSet = this.lambdaQuery().select(InspectionPointDetail::getStatus)
                .eq(InspectionPointDetail::getTaskId, taskId).list()
                .parallelStream().map(InspectionPointDetail::getStatus).collect(Collectors.toSet());
        if (statusSet.contains(POINT_NOT_EXECUTE.status)) {
            return EXECUTING.status;
        }
        return ENDED.status;
    }

    private void setTaskCompletionTime(InspectionTask task) {
        if (task.getStatus() == ENDED.status) {
            task.setCompletionTime(LocalDateTime.now());
        }
    }
}
