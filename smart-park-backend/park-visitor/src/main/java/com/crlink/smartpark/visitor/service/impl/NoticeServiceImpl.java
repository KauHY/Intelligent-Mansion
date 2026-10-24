package com.crlink.smartpark.visitor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.core.utils.EntityUserUtil;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyPageInfoUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import com.crlink.smartpark.visitor.model.dto.NoticeReqDTO;
import com.crlink.smartpark.visitor.model.query.NoticeQuery;
import com.crlink.smartpark.visitor.dto.NoticeDTO;
import com.crlink.smartpark.visitor.entity.Notice;
import com.crlink.smartpark.visitor.mapper.NoticeMapper;
import com.crlink.smartpark.visitor.service.NoticeService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * 来访须知 服务实现类
 * */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateNotice(NoticeReqDTO reqDTO) {
        checkParams(reqDTO);
        Notice entity = MyBeanUtils.copy(reqDTO, Notice::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 检查参数
     *
     * @param reqDTO
     */
    private void checkParams(NoticeReqDTO reqDTO) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getNoticeName, reqDTO.getNoticeName().trim());
        Notice notice = getOne(wrapper, false);
        AssertUtil.requireTrue(notice == null || notice.getId().equals(reqDTO.getId()), "70008");
        wrapper.clear();
        wrapper.eq(Notice::getParkOrgId, reqDTO.getParkOrgId());
        notice = getOne(wrapper, false);
        AssertUtil.requireTrue(notice == null || notice.getId().equals(reqDTO.getId()), "70021");
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public NoticeDTO getNoticeById(String id) {
        Notice entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<NoticeDTO> listNoticePage(NoticeQuery query) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Notice::getCreateTime);//原型要求根据创建时间倒序
        wrapper.in(Notice::getParkOrgId, SystemUtil.getInstance().getParkOrgIdsOrRetEmpty());
        wrapper.eq(StrUtil.isNotBlank(query.getParkOrgId()), Notice::getParkOrgId, query.getParkOrgId());
        query.startPage();
        List<Notice> list = list(wrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<NoticeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    @Id2objHandle
    public NoticeDTO getNoticeByParkOrgId(String parkOrgId) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notice::getParkOrgId, parkOrgId);
        Notice notice = getOne(wrapper, false);
        return MyBeanUtils.copy(notice, NoticeDTO::new);
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<NoticeDTO> entitiesToDTOs(List<Notice> entities) {
        return MyBeanUtils.copyList(entities, NoticeDTO::new, (s, t) -> {
        });
    }
}
