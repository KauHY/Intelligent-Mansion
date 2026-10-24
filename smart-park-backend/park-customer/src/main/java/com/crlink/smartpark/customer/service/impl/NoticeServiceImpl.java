package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.dto.NoticeDTO;
import com.crlink.smartpark.customer.entity.Notice;
import com.crlink.smartpark.customer.entity.NoticePermission;
import com.crlink.smartpark.customer.mapper.NoticeMapper;
import com.crlink.smartpark.customer.model.NoticeReqDTO;
import com.crlink.smartpark.customer.model.query.NoticeQuery;
import com.crlink.smartpark.customer.service.InformationService;
import com.crlink.smartpark.customer.service.NoticePermissionService;
import com.crlink.smartpark.customer.service.NoticeService;
import com.crlink.smartpark.customer.vo.NoticeWxListVO;
import com.crlink.smartpark.customer.vo.WxNoticeVO;
import com.crlink.smartpark.system.feign.CommonProvider;
import com.crlink.smartpark.system.vo.DictVO;
import com.crlink.smartpark.system.vo.SimpleNode;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.Constants.Redis.NOTICE_PAGE_VIEW_PREFIX;
import static com.crlink.smartpark.customer.common.Constant.NoticePermissionTypeEnum.COMPANY;
import static com.crlink.smartpark.customer.common.Constant.NoticePermissionTypeEnum.EMPLOYEE;
import static com.crlink.smartpark.customer.common.Constant.NoticeStatusEnum.*;
import static com.crlink.smartpark.customer.common.Constant.putIntoTypeEnum.*;


/**
 * 公告信息表 服务实现类
 * */
@Slf4j
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    NoticePermissionService noticePermissionService;

    @Autowired
    CommonProvider commonProvider;

    @Autowired
    private InformationService informationService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 保存或更新数据
     * v18 生效时间：时间字段不做限制，可选择当前时间点之前或之后的时间段
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateNotice(NoticeReqDTO reqDTO) {
//        LocalDateTime now = LocalDateTime.now();
//        if (!reqDTO.getEffectTimeStart().isAfter(now) || !reqDTO.getEffectTimeEnd().isAfter(now)) {
//            throw new ResolinkRuntimeException("50020");
//        }
        if (!reqDTO.getEffectTimeEnd().isAfter(reqDTO.getEffectTimeStart())) {
            throw new ResolinkRuntimeException("50021");
        }
        Notice entity = MyBeanUtils.copy(reqDTO, Notice::new);
        EntityUserUtil.setAllDynamic(entity);
        List<Integer> putIntoType = reqDTO.getPutIntoType();
        if (putIntoType.size() > 1) {
            entity.setPutIntoType(PUT_INTO_WX_C_AND_B.type);
        } else {
            entity.setPutIntoType(putIntoType.get(0));
        }
        String id = reqDTO.getId();
        if (StringUtils.isNotBlank(id)) {
            //删除关联表中的数据
            noticePermissionService.remove(Wrappers.<NoticePermission>lambdaQuery().eq(NoticePermission::getNoticeId, id));
        }
        this.saveOrUpdate(entity);
        if (!reqDTO.getWithoutLimit()) {
            List<NoticePermission> noticePermissions = Lists.newArrayList();
            noticePermissions.addAll(insertNoticePermissionData(
                    entity.getId(), reqDTO.getCompanyIds(), COMPANY.type));
            noticePermissions.addAll(insertNoticePermissionData(
                    entity.getId(), reqDTO.getEmployeeIds(), EMPLOYEE.type));
            if (CollectionUtil.isEmpty(noticePermissions)) {
                //推送范围不是不限，则公司、员工不能都为空
                throw new ResolinkRuntimeException("10003");
            }
            noticePermissionService.saveBatch(noticePermissions);
        }
        return true;
    }


    /**
     * 后台通过id查询实体
     */
    @Override
    @Id2objHandle
    public NoticeDTO getNoticeById(String id) {
        Notice entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        NoticeDTO result = MyBeanUtils.copy(entity, NoticeDTO::new);
        Integer status = getNoticeStatus(entity.getEffectTimeStart(), entity.getEffectTimeEnd(), LocalDateTime.now());
        LinkedList<Integer> putIntoType = informationService.getPutIntoTypeList(entity.getPutIntoType());
        result.setPutIntoType(putIntoType);
        result.setStatus(status);
        if (!entity.getWithoutLimit()) {
            Set<String> employeeIds = Sets.newHashSet();
            List<String> companyIds = Lists.newArrayList();
            noticePermissionService.lambdaQuery().eq(NoticePermission::getNoticeId, id).list()
                    .forEach(t -> {
                        if (t.getRelationType().equals(EMPLOYEE.type)) {
                            employeeIds.add(t.getEmployeeId());
                        } else {
                            companyIds.add(t.getCompanyId());
                        }
                    });
//            Map<String, String> companyId2NameMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.COMPANY.getCode());
//            List<DictVO> companyDict = companyIds.parallelStream()
//                    .map(t -> new DictVO(companyId2NameMap.get(t), t)).collect(Collectors.toList());
            //员工名称
            Map<String, String> employeeId2NameMap = commonProvider.getNamesByIdsAndType(employeeIds, SimpleNode.TypeEnum.EMPLOYEE.getCode());
            List<DictVO> employeeDict = employeeIds.parallelStream()
                    .map(t -> new DictVO(employeeId2NameMap.get(t), t)).collect(Collectors.toList());
            result.setCompanyIds(companyIds);
            result.setEmployeeIds(employeeDict);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeNoticeById(String id) {
        Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        noticePermissionService.remove(Wrappers.<NoticePermission>lambdaQuery()
                .eq(NoticePermission::getNoticeId, id));
        return removeById(id);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<NoticeDTO> listNoticePage(NoticeQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds(query.getParkOrgId());
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        LambdaQueryWrapper<Notice> conditionWrapper = getConditionWrapper(query, parkOrgIds);
        query.startPage();
        List<Notice> list = list(conditionWrapper);
        MyPageInfoUtils.isEmptyPage(list);
        List<NoticeDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    /**
     * 小程序列表
     *
     * @param query
     * @param source
     * @return
     */
    @Override
    public PageInfo<NoticeWxListVO> listWxNoticePage(BaseQuery<String> query, String source, Boolean fromHomePage) {
        String parkOrgId = query.getParam();
        MyPageInfoUtils.isEmptyPage(parkOrgId);
        LambdaQueryWrapper<Notice> wxConditionWrapper = getWxConditionWrapper(parkOrgId, source, fromHomePage);
        query.startPage();
        List<Notice> list = this.list(wxConditionWrapper);
        int currentYear = LocalDate.now().getYear();
        List<NoticeWxListVO> result = MyBeanUtils.copyList(list, NoticeWxListVO::new, (s, t) -> {
            LocalDateTime effectTimeStart = s.getEffectTimeStart();
            if (currentYear == effectTimeStart.getYear()) {
                //当年的公告则显示MM-dd HH:mm
                t.setEffectTimeStart(effectTimeStart.format(TIME_WITHOUT_YEAR_FORMATTER));
            } else {
                //非当年的公告则显示yyyy-MM-dd HH:mm
                t.setEffectTimeStart(effectTimeStart.format(TIME_WITHOUT_SECONDS_FORMATTER));
            }
        });
        return query.createPageInfo(result);
    }

    /**
     * 小程序详情
     *
     * @param id
     * @return
     */
    @Override
    public WxNoticeVO getWxNoticeDetailById(String id) {
        Notice notice = this.getById(id);
        WxNoticeVO result = MyBeanUtils.copy(notice, WxNoticeVO::new);
        Integer redisPageView = (Integer) Optional.ofNullable(redisUtil.getObject(NOTICE_PAGE_VIEW_PREFIX.getBuildCode(id))).orElse(0);
        result.setViews(notice.getViews() + redisPageView);
        result.setEffectTimeStart(notice.getEffectTimeStart().format(DATE_FORMATTER));
        return result;
    }

    /**
     * 获取生效中的公告
     * 待生效”状态是公告在小程序端都不显示
     *
     * @return
     */
    private LambdaQueryWrapper<Notice> getWxConditionWrapper(String parkOrgId, String source, Boolean fromHomePage) {
        LocalDateTime now = LocalDateTime.now();
        //判断是否登录，已登录则能取到对应值，未登录则为空
        String currentSource = SystemUtil.getInstance().getSource();
        LambdaQueryWrapper<Notice> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.select(Notice::getId, Notice::getTitle, Notice::getSummary, Notice::getEffectTimeStart)
                .eq(Notice::getParkOrgId, parkOrgId)
                .le(Notice::getEffectTimeStart, now)
                .orderByDesc(Notice::getEffectTimeStart);
        if (fromHomePage) {
            lambdaQuery.ge(Notice::getEffectTimeEnd, now);
        }
        if (StringUtils.isBlank(currentSource)) {
            //未登录时，推送范围不限的公告信息
            lambdaQuery.eq(Notice::getWithoutLimit, true);
        } else {
            //已登录时，取值“不限”+推送权限范围内的公告信息。
            Set<String> noticeIds = getNoticeIds();
            lambdaQuery.eq(Notice::getWithoutLimit, true)
                    .or(CollectionUtil.isNotEmpty(noticeIds))
                    .in(CollectionUtil.isNotEmpty(noticeIds), Notice::getId, noticeIds);
        }
        if (USER_WX_SOURCE_B.equals(source) || USER_WX_SOURCE_C.equals(source)) {
            //根据投放小程序类型进行筛选
            lambdaQuery.and(wrapper -> wrapper
                    .eq(USER_WX_SOURCE_B.equals(source), Notice::getPutIntoType, PUT_INTO_WX_B.type)
                    .eq(USER_WX_SOURCE_C.equals(source), Notice::getPutIntoType, PUT_INTO_WX_C.type)
                    .or().eq(Notice::getPutIntoType, PUT_INTO_WX_C_AND_B.type));
        }
        return lambdaQuery;
    }

    /**
     * 根据公司id、员工id筛选公告id
     *
     * @return
     */
    private Set<String> getNoticeIds() {
        String employeeId = SystemUtil.getInstance().getEmployeeId();
        String curCompanyId = SystemUtil.getInstance().getEmployeeCompanyId();
        if (StringUtils.isNotBlank(employeeId) && StringUtils.isNotBlank(curCompanyId)) {
            List<NoticePermission> list = noticePermissionService.lambdaQuery()
                    .eq(NoticePermission::getEmployeeId, employeeId)
                    .or().eq(NoticePermission::getCompanyId, curCompanyId).list();
            Set<String> noticeIds = list.parallelStream().map(NoticePermission::getNoticeId).collect(Collectors.toSet());
            return noticeIds;
        } else {
            return Collections.emptySet();
        }
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<Notice> getConditionWrapper(NoticeQuery query, Set<String> parkOrgIds) {
        Integer status = query.getEffectStatus();
        LocalDateTime timeStart = query.getEffectTimeStart();
        LocalDateTime timeEnd = query.getEffectTimeEnd();
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>();
        queryWrapper.like(StringUtils.isNotBlank(query.getTitle()), Notice::getTitle, query.getTitle())
                .eq(Objects.nonNull(query.getType()), Notice::getType, query.getType())
                //对应的投放小程序类型及两个段都投放的
                .and(Objects.nonNull(query.getPutIntoType()), wrapper -> wrapper
                        .eq(Notice::getPutIntoType, query.getPutIntoType())
                        .or().eq(Notice::getPutIntoType, PUT_INTO_WX_C_AND_B.type))
                .eq(StringUtils.isNotBlank(query.getParkOrgId()), Notice::getParkOrgId, query.getParkOrgId())
                .in(Notice::getParkOrgId, parkOrgIds)
                .orderByDesc(Notice::getCreateTime);
        //按时间端筛选
        if (Objects.nonNull(timeStart) && Objects.nonNull(timeEnd)) {
            //查询生效时间段与查询时间段存在交集的
            queryWrapper.not(wrapper -> wrapper.gt(Notice::getEffectTimeStart, timeEnd)
                    .or().lt(Notice::getEffectTimeEnd, timeStart));
        }
        if (Objects.nonNull(status)) {
            LocalDateTime now = LocalDateTime.now();
            if (status == UN_EFFECT.status) {
                queryWrapper.gt(Notice::getEffectTimeStart, now);
            } else if (status == EFFECTING.status) {
                queryWrapper.lt(Notice::getEffectTimeStart, now)
                        .gt(Notice::getEffectTimeEnd, now);
            } else if (status == OUT_OF_DATE.status) {
                queryWrapper.lt(Notice::getEffectTimeEnd, now);
            }
        }

        return queryWrapper;
    }

    /**
     * 要插入关联表的数据处理
     *
     * @param noticeId
     * @param list
     * @return
     */
    private List<NoticePermission> insertNoticePermissionData(String noticeId, List<String> list, Integer relationType) {
        if (CollectionUtil.isNotEmpty(list)) {
            List<NoticePermission> result = list.stream().map(t -> {
                if (StringUtils.isNotBlank(t)) {
                    NoticePermission noticePermission = new NoticePermission();
                    if (relationType.equals(COMPANY.type)) {
                        noticePermission.setCompanyId(t);
                    } else {
                        noticePermission.setEmployeeId(t);
                    }
                    noticePermission.setNoticeId(noticeId);
                    noticePermission.setRelationType(relationType);
                    EntityUserUtil.setAll(noticePermission);
                    return noticePermission;
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return result;
        }
        return Collections.emptyList();
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<NoticeDTO> entitiesToDTOs(List<Notice> list) {
        LocalDateTime now = LocalDateTime.now();
        return MyBeanUtils.copyList(list, NoticeDTO::new, (s, t) -> {
            Integer noticeStatus = getNoticeStatus(s.getEffectTimeStart(), s.getEffectTimeEnd(), now);
            t.setStatus(noticeStatus);
            LinkedList<Integer> putIntoType = informationService.getPutIntoTypeList(s.getPutIntoType());
            t.setPutIntoType(putIntoType);
        });
    }

    /**
     * 获取公告的状态
     *
     * @param startTime
     * @param
     * @return
     */
    private Integer getNoticeStatus(LocalDateTime startTime, LocalDateTime endTime, LocalDateTime now) {
        if (startTime.isAfter(now)) {
            return UN_EFFECT.status;
        } else if (endTime.isAfter(now)) {
            return EFFECTING.status;
        } else {
            return OUT_OF_DATE.status;
        }
    }


    /**
     * 定时任务：浏览量入库
     */
    //一个小时执行一次
    //@Scheduled(cron = "0 0 */1 * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void updateViewsToDB() {
        log.info("公告浏览量入库开始");
        //默认单页查询数据量
        final int MAX_PAGE_SIZE = 10000;
        LocalDateTime now = LocalDateTime.now();
        int pageNo = 1;
        while (true) {
            PageHelper.startPage(pageNo, MAX_PAGE_SIZE);
            List<Notice> notices = this.lambdaQuery().select(Notice::getId, Notice::getViews)
                    .le(Notice::getEffectTimeStart, now)
                    .gt(Notice::getEffectTimeEnd, now)
//                            .or().isNull(Notice::getEffectTimeEnd))
                    .list();
            notices = notices.parallelStream().map(t -> {
                Integer redisPageView = (Integer) Optional.ofNullable(redisUtil.getObject(NOTICE_PAGE_VIEW_PREFIX.getBuildCode(t.getId()))).orElse(0);
                t.setViews(Optional.ofNullable(t.getViews()).orElse(0L) + redisPageView);
                redisUtil.setObject(NOTICE_PAGE_VIEW_PREFIX.getBuildCode(t.getId()), 0, 24L, TimeUnit.HOURS);
                return t;
            }).filter(t -> !Objects.equals(0, t.getViews())).collect(Collectors.toList());
            this.updateBatchById(notices);
            PageInfo pageInfo = new PageInfo<>(notices);
            if (!pageInfo.isHasNextPage()) {
                break;
            }
            pageNo++;
        }
    }
}
