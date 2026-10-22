package com.crlink.smartpark.meeting.mapper;

import com.crlink.smartpark.meeting.dto.ReserveAuditDTO;
import com.crlink.smartpark.meeting.entity.ReserveAudit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会议室预订审核 Mapper 接口
 * </p>
 * */
public interface ReserveAuditMapper extends BaseMapper<ReserveAudit> {

    List<ReserveAuditDTO> listPage(@Param("param") com.crlink.smartpark.meeting.controller.request.query.ReserveAuditQuery param);

    List<ReserveAuditDTO> listAuditPage(@Param("param") com.crlink.smartpark.meeting.controller.request.query.ReserveAuditQuery param);

    int autoCancelReserve();
}
