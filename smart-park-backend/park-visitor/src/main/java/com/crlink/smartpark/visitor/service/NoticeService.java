package com.crlink.smartpark.visitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.visitor.dto.NoticeDTO;
import com.crlink.smartpark.visitor.entity.Notice;
import com.crlink.smartpark.visitor.model.dto.NoticeReqDTO;
import com.crlink.smartpark.visitor.model.query.NoticeQuery;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 来访须知 服务类
 * </p>
 * */
public interface NoticeService extends IService<Notice> {

    /**
     * 保存或更新数据
     *
     * @return 实体对象
     */
    Boolean saveOrUpdateNotice(NoticeReqDTO reqDTO);

    /**
     * 根据id查询实体对象
     *
     * @param id 查询条件
     * @return 实体对象
     */
    NoticeDTO getNoticeById(String id);

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    PageInfo<NoticeDTO> listNoticePage(NoticeQuery query);

    /**
     * 根据id查询实体对象
     *
     * @param parkOrgId 查询条件
     * @return 实体对象
     */
    NoticeDTO getNoticeByParkOrgId(String parkOrgId);
}
