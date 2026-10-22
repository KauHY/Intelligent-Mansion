package com.crlink.smartpark.visitor.service;

import com.crlink.smartpark.visitor.entity.VisitorRecord;

public interface DoorAccessDeviceService {

    /**
     * 推送访客到设备
     *
     * @param visitorRecord
     * @return
     */
    String pushVisitor(VisitorRecord visitorRecord);

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
     * 新增人员和卡片 20220808
     * @param visitorRecord
     * @return
     */
    String addPersonAndCard(VisitorRecord visitorRecord);

    /**
     * 更新访客卡片信息
     *
     * @param visitorRecord
     * @return
     */
    String updateVisitorCard(VisitorRecord visitorRecord);

    /**
     * 删除访客设备权限
     * @param visitorRecord
     * @return
     */
    String removeVisitorDeviceAuth(VisitorRecord visitorRecord);

}
