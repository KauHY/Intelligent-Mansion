package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.constants.enums.VisitorRegisterTypeEnum;
import com.crlink.smartpark.visitor.dto.VisitorRecordSubscribeDTO;
import com.crlink.smartpark.visitor.dto.VisitorSignOutDTO;
import com.crlink.smartpark.visitor.entity.VisitorRecord;
import com.crlink.smartpark.visitor.query.VisitorRecordListQuery;
import com.crlink.smartpark.visitor.vo.FaceVerifyDevicePushOriginDataDTO;
import com.crlink.smartpark.visitor.vo.VisitorRecordSubScribeListVO;
import com.crlink.smartpark.visitor.vo.VisitorRecordVO;
import com.crlink.smartpark.visitor.vo.VisitorRegisterListVO;
import com.crlink.smartpark.visitor.vo.VisitorVerifyPushDataDTO;
import com.github.pagehelper.PageInfo;

/**
 * 访客记录服务接口
 * */
public interface VisitorRecordService extends IService<VisitorRecord> {

    /**
     * 访客登记
     */
    void subscribe(VisitorRecordSubscribeDTO dto);

    /**
     * 删除访客记录
     */
    void removeVisitorRecordById(String id);

    /**
     * 根据ID获取访客记录
     */
    VisitorRecordVO getVisitorRecordById(String id);

    /**
     * 分页查询访客登记记录
     */
    PageInfo<VisitorRegisterListVO> pageListVisitorRegister(VisitorRecordListQuery query, VisitorRegisterTypeEnum visitorRegisterType);

    /**
     * 分页查询访客预约记录
     */
    PageInfo<VisitorRecordSubScribeListVO> pageListVisitorSubscribe(VisitorRecordListQuery query);

    /**
     * 导出当前访客
     *
     * @param query
     */
    void exportVisitorRegister(VisitorRecordListQuery query, VisitorRegisterTypeEnum type);

    /**
     * 导出预约记录
     *
     * @param query
     */
    void exportVisitorSubscribe(VisitorRecordListQuery query);

    /**
     * 人证核验机器推送图片
     * dto：机器推送过来的原生数据（人脸，身份证，二维码信息（可能支持））
     *
     * @return
     */
    VisitorVerifyPushDataDTO faceVerifyDeviceDataPush(FaceVerifyDevicePushOriginDataDTO dto);

    VisitorRecordVO getByPersonId(Long devicePersonId);

    /**
     * 更新门禁核验状态
     *
     * @param devicePersonId
     */
    void updateDoorAccessStatus(Long devicePersonId);

    /**
     * 该访客信息失效，转为历史访客
     * @param signOutDTO
     * @return
     */
    void signOut(VisitorSignOutDTO signOutDTO);

}
