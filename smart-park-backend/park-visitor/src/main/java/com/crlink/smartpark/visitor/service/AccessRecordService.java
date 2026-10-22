package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.dto.AccessRecordDTO;
import com.crlink.smartpark.visitor.entity.AccessRecord;
import com.crlink.smartpark.visitor.model.vo.AccessRecordListVO;
import com.crlink.smartpark.visitor.query.AccessRecordQuery;
import com.crlink.smartpark.visitor.vo.AccessRecordVO;
import com.github.pagehelper.PageInfo;

import java.io.IOException;

/**
 * <p>
 * 人行抓拍记录表 服务类
 * </p>
 * */
public interface AccessRecordService extends IService<AccessRecord> {

    /**
     *  根据id查询实体对象
     *
     * @param id 查询条件
     * @return   实体对象
     */
    AccessRecordVO getAccessRecordById(String id);

    /**
     * 分页查询
     *
     * @param condition 查询对象
     * @return 分页结果
     */
    PageInfo<AccessRecordListVO> listPage(AccessRecordQuery condition);

    /**
     * 导出
     * @param query
     * @throws IOException
     */
	void export(AccessRecordQuery query);

    /**
     * 考勤
     * @param query
     * @throws IOException
     */
    void exportAttendance(AccessRecordQuery query);

    Boolean saveAccessRecord(AccessRecordDTO accessRecordDTO);

    void buildHistorySceneImageUrl(String startTime, String endTime);
}
