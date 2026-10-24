package com.crlink.smartpark.visitor.service.mobile;

import com.crlink.smartpark.system.vo.CompanyClientInfoVO;
import com.crlink.smartpark.visitor.dto.ParkCarDTO;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.model.vo.MobileVisitorRecordVO;

public interface VisitorCommonService {

    /**
     * 预约详情
     *
     * @param id
     */
    MobileVisitorRecordVO appointmentDetail(String id);

    /**
     * 推送访客信息到设备
     *
     * @param visitorRecord
     */
    void pushVisitor2Device(VisitorRecord visitorRecord);

    /**
     * 生成人员id 20220808
     *
     * @return id
     */
    Long generatePersonId();

    /**
     * 生成卡片id 20220808
     *
     * @return id
     */
    Long generateCardId();

    /**
     * 添加员工和卡片权限20220808
     *
     * @param visitorRecord
     */
    void addPersonAndCard(VisitorRecord visitorRecord);

    /**
     * 更新访客卡片信息
     *
     * @param visitorRecord
     */
    void updateVisitorCard(VisitorRecord visitorRecord);

    /**
     * 删除访客设备权限
     *
     * @param visitorRecord
     */
    void removeVisitorDeviceAuth(VisitorRecord visitorRecord);

    /**
     * 获取当前公司宣传信息
     *
     * @return
     */
    CompanyClientInfoVO getCurCompanyPublicInfo(String companyId);

    /**
     * 添加车辆
     * @param carDTO
     * @return
     */
    String addReservedCar(ParkCarDTO carDTO);
}
