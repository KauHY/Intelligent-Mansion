package com.crlink.smartpark.visitor.service.mobile;

import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.visitor.model.dto.AppointmentApplyDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorInfoCompleteDTO;
import com.crlink.smartpark.visitor.model.dto.VisitorRecordQuery;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordListVO;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordVO;
import com.github.pagehelper.PageInfo;

public interface VisitorUserService {

    /**
     * 用户预约
     *
     * @param dto
     */
    void makeAppointment(AppointmentApplyDTO dto);

    /**
     * 获取预约列表
     *
     * @param query
     * @return
     */
    PageInfo<MobileVisitorRecordListVO> listAppointments(VisitorRecordQuery query);

    MobileVisitorRecordListVO detail(String id);

    /**
     * 取消预约
     *
     * @param id
     */
    void cancelAppointment(String id);

    /**
     * 补充访客邀请信息
     *
     * @param dto
     */
    void completeVisitorInfo(String id, VisitorInfoCompleteDTO dto);

}
