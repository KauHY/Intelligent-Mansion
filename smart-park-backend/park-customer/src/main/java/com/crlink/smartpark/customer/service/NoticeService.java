package com.crlink.smartpark.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.customer.dto.NoticeDTO;
import com.crlink.smartpark.customer.entity.Notice;
import com.crlink.smartpark.customer.model.NoticeReqDTO;
import com.crlink.smartpark.customer.model.query.NoticeQuery;
import com.crlink.smartpark.customer.vo.NoticeWxListVO;
import com.crlink.smartpark.customer.vo.WxNoticeVO;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 公告信息表 服务类
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
     * @param query 查询对象
     * @return 分页结果
     */
    PageInfo<NoticeDTO> listNoticePage(NoticeQuery query);

    PageInfo<NoticeWxListVO> listWxNoticePage(BaseQuery<String> query, String source, Boolean fromHomePage);

    Boolean removeNoticeById(String id);

    WxNoticeVO getWxNoticeDetailById(String id);
}
