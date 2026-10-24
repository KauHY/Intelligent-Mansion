package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.dto.MeetingReserveDTO;
import com.crlink.smartpark.customer.dto.MeetingReserveGoodsDTO;
import com.crlink.smartpark.customer.dto.MeetingReserveTimeDTO;
import com.crlink.smartpark.customer.entity.MeetingRent;
import com.crlink.smartpark.customer.entity.MeetingReserve;
import com.crlink.smartpark.customer.entity.MeetingReserveGoods;
import com.crlink.smartpark.customer.entity.MeetingReserveTime;
import com.crlink.smartpark.customer.mapper.MeetingReserveMapper;
import com.crlink.smartpark.customer.model.*;
import com.crlink.smartpark.customer.model.excel.MeetingReserveExportDTO;
import com.crlink.smartpark.customer.model.query.MeetingReserveQuery;
import com.crlink.smartpark.customer.service.MeetingRentService;
import com.crlink.smartpark.customer.service.MeetingReserveGoodsService;
import com.crlink.smartpark.customer.service.MeetingReserveService;
import com.crlink.smartpark.customer.service.MeetingReserveTimeService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.SMALL_DATE_FORMAT;

/**
 * 租赁会议室预订 服务实现类
 * */
@Service
public class MeetingReserveServiceImpl extends ServiceImpl<MeetingReserveMapper, MeetingReserve>
        implements MeetingReserveService {

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private MeetingReserveTimeService reserveTimeService;

    @Autowired
    private MeetingReserveGoodsService reserveGoodsService;

    @Autowired
    private MeetingRentService meetingRentService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<MeetingReserve, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<MeetingReserve> lambdaQuery = this.lambdaQuery().in(MeetingReserve::getId, ids);
        if (flag) {
            lambdaQuery.select(MeetingReserve::getId, sValue);
        }
        Stream<MeetingReserve> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(MeetingReserve::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(MeetingReserve::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateMeetingReserve(MeetingReserveReqDTO reqDTO) {
        MeetingReserve entity = MyBeanUtils.copy(reqDTO, MeetingReserve::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public MeetingReserveDTO getMeetingReserveById(String id) {
        MeetingReserve entity =
                Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        MeetingReserveDTO meetingReserveDTO = entitiesToDTOs(Lists.newArrayList(entity)).get(0);

        // List<MeetingReserveTimeDTO> reserveTimeList = MyBeanUtils.copyList(
        // reserveTimeService.lambdaQuery().eq(MeetingReserveTime::getMeetingReserveId, id)
        // .orderByAsc(MeetingReserveTime::getNowDate, MeetingReserveTime::getTimeType).list(),
        // MeetingReserveTimeDTO::new);
        // meetingReserveDTO.setReserveTimeList(reserveTimeList);
        // String reserveTimeStr;
        //
        // List<MeetingReserveGoodsDTO> reserveGoodsList = MyBeanUtils.copyList(
        // reserveGoodsService.lambdaQuery().eq(MeetingReserveGoods::getMeetingReserveId, id).list(),
        // MeetingReserveGoodsDTO::new);
        // meetingReserveDTO.setReserveGoodsList(reserveGoodsList);

        return meetingReserveDTO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<MeetingReserveDTO> listMeetingReservePage(MeetingReserveQuery query) {
        query.startPage();
        List<MeetingReserve> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingReserveDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public String saveMeetingReserve(MeetingReserveInsertDTO reqDTO) {
        if (CollUtil.isEmpty(reqDTO.getReserveTimeList())) {
            throw new ResolinkRuntimeException("10003");
        }
        MeetingReserve entity = MyBeanUtils.copy(reqDTO, MeetingReserve::new);
        String meetingId = entity.getMeetingId();
        // DO 第一步，检验时间合法性 -预留，后期实现
        List<MeetingReserveTimeInsertDTO> reserveTimeListCheck = reqDTO.getReserveTimeList();
        reserveTimeListCheck.forEach(s -> {
            int count = reserveTimeService.lambdaQuery().eq(MeetingReserveTime::getMeetingId, meetingId)
                    .eq(MeetingReserveTime::getTimeType, s.getTimeType()).eq(MeetingReserveTime::getNowDate, s.getNowDate())
                    .eq(MeetingReserveTime::getMeetingState, DictUtil.getValue(DictConstant.YesOrNo.NO)).count();
            if (count > Constants.ZERO) {
                throw new ResolinkRuntimeException("50016");
            }
        });

        // String curEmployeeId = "1503356849234120705";//
        String curEmployeeId = SystemUtil.getInstance().getEmployeeId();
        entity.setEmployeeId(curEmployeeId);
        entity.setReviewStatus(DictUtil.getValue(Constant.CusMeetingAuditStatus.AUDIT_RENDING));
        // entity.setReviewStatus(1);

        entity.setApplicantTime(LocalDateTime.now());
        entity.setParkOrgId(entity.getOldParkOrgId());
        // 生成订单编码
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(Constant.MEETING_RESERVE);
        String no = new StringBuilder(Constant.MEETING_RESERVE)
                .append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                .append(numberSeq.getValue()).toString();
        entity.setReserveNo(no);

        EntityUserUtil.setAllDynamic(entity);
        // 拼接备份数据 -已有 前端传参
        this.save(entity);

        String reserveId = entity.getId();
        // 处理时间数据
        List<MeetingReserveTimeInsertDTO> reserveTimeList = reqDTO.getReserveTimeList();
        List<MeetingReserveTime> meetingReserveTimes =
                MyBeanUtils.copyList(reserveTimeList, MeetingReserveTime::new, (s, t) -> {
                    t.setMeetingReserveId(reserveId);
                    t.setEmployeeId(curEmployeeId);
                    t.setMeetingId(meetingId);
                    t.setMeetingState(DictUtil.getValue(DictConstant.YesOrNo.NO));
                });
        reserveTimeService.saveBatch(meetingReserveTimes);
        if (CollUtil.isNotEmpty(reqDTO.getReserveGoodsList())) {
            // 处理物资数据
            List<MeetingReserveGoodsInsertDTO> reserveGoodsList = reqDTO.getReserveGoodsList();
            List<MeetingReserveGoods> meetingReserveGoods =
                    MyBeanUtils.copyList(reserveGoodsList, MeetingReserveGoods::new, (s, t) -> {
                        t.setMeetingReserveId(reserveId);
                        t.setMeetingId(meetingId);
                    });
            reserveGoodsService.saveBatch(meetingReserveGoods);
        }
        return reserveId;
    }

    @Override
    @Id2objHandle
    public PageInfo<MeetingReserveDTO> listWxMeetingReservePage(MeetingReserveQuery query) {
        query.startPage();
        String curEmployeeId = SystemUtil.getInstance().getEmployeeId();
        // String curEmployeeId = "1503356849234120705";
        LambdaQueryWrapper<MeetingReserve> queryWrapper = new LambdaQueryWrapper<MeetingReserve>();
        queryWrapper.eq(MeetingReserve::getEmployeeId, curEmployeeId);
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeetingReserve::getParkOrgId,
                query.getParkOrgId());
        if (query.getReviewStatus() != null) {
            if (query.getReviewStatus() == Constants.ONE) {
                queryWrapper.eq(MeetingReserve::getReviewStatus, query.getReviewStatus());
            } else if (query.getReviewStatus() == Constants.TWO) {
                Set<Integer> status = new HashSet<>();
                status.add(DictUtil.getValue(Constant.CusMeetingAuditStatus.AUDIT_PASS));
                status.add(DictUtil.getValue(Constant.CusMeetingAuditStatus.AUDIT_FAIL));
                // status.add (2);
                // status.add (3);
                queryWrapper.in(MeetingReserve::getReviewStatus, status);
            }

        }
        queryWrapper.orderByDesc(MeetingReserve::getApplicantTime);
        List<MeetingReserve> list = this.list(queryWrapper);

        // List<MeetingReserve> list = this.lambdaQuery().eq(MeetingReserve::getEmployeeId, curEmployeeId)
        //
        // .eq(query.getReviewStatus() != null, MeetingReserve::getReviewStatus, query.getReviewStatus()).list();
        MyPageInfoUtils.isEmptyPage(list);
        List<MeetingReserveDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean review(MeetingReserveReviewDTO reqDTO) {
        MeetingReserve entity = MyBeanUtils.copy(reqDTO, MeetingReserve::new);
        entity.setStateChangeTime(LocalDateTime.now());
        EntityUserUtil.setAllDynamic(entity);

        this.saveOrUpdate(entity);
        // DO 如果审核不通过，更新对应记录表数据释放
        if (reqDTO.getReviewStatus().equals(DictUtil.getValue(Constant.CusMeetingAuditStatus.AUDIT_FAIL))) {
            reserveTimeService.changeMeetingStates(reqDTO.getId());
        }
        return true;
    }

    @Override
    public Boolean cancel(String id) {
        // do 取消申请时当前状态校验 - 后期优化
        MeetingReserve entity =
                Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (entity.getReviewStatus().equals(DictUtil.getValue(Constant.CusMeetingAuditStatus.AUDIT_RENDING))) {
            entity.setStateChangeTime(LocalDateTime.now());
            entity.setReviewStatus(DictUtil.getValue(Constant.CusMeetingAuditStatus.CANCEL));
            this.updateById(entity);
            // 释放时间段
            reserveTimeService.changeMeetingStates(id);
            return true;
        } else {
            throw new ResolinkRuntimeException("50015");
        }
    }

    @Override
    public void export(MeetingReserveQuery query) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(query)), this::convertToExcel,
                Constant.MEETING_RESERVE_EXPORT_FILENAME, MeetingReserveExportDTO.class);
    }

    /**
     * 根据园区获取场地预定费用数据
     *
     * @param parkOrgId
     * @return
     */
    @Override
    public List<MeetingReserveDTO> getMeetingReserveBillList(String parkOrgId, Boolean nearTwelveMonths) {
        LambdaQueryChainWrapper<MeetingReserve> wrapper = this.lambdaQuery();
        wrapper.select(MeetingReserve::getId, MeetingReserve::getStateChangeTime, MeetingReserve::getReviewAmount)
                .eq(MeetingReserve::getParkOrgId, parkOrgId)
                .eq(MeetingReserve::getReviewStatus, DictUtil.getValue(Constant.CusMeetingAuditStatus.AUDIT_PASS));
        if (nearTwelveMonths) {
            LocalDateTime now = LocalDateTime.now();
            wrapper.between(MeetingReserve::getStateChangeTime, LocalDateTimeUtil.beginOfDay(now.minusMonths(12)), now);
        }
        List<MeetingReserve> result = wrapper.list();
        if (CollectionUtil.isEmpty(result)) {
            return Collections.emptyList();
        }
        return MyBeanUtils.copyList(result, MeetingReserveDTO::new);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<MeetingReserve> getConditionWrapper(MeetingReserveQuery query) {
        LambdaQueryWrapper<MeetingReserve> queryWrapper = new LambdaQueryWrapper<MeetingReserve>();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        // Set<String> parkOrgIds = new HashSet<>();// SystemUtil.getInstance().getParkOrgIds();
        // parkOrgIds.add("1480778130892263426");
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        if (StringUtils.isNotBlank(query.getMeetingName())) {
            Set<String> meetings =
                    meetingRentService.lambdaQuery().like(MeetingRent::getMeetingName, query.getMeetingName()).list()
                            .parallelStream().map(MeetingRent::getId).collect(Collectors.toSet());
            if (CollUtil.isEmpty(meetings)) {
                queryWrapper.eq(MeetingReserve::getMeetingId, "");
            } else {
                queryWrapper.in(MeetingReserve::getMeetingId, meetings);
            }
        }
        LocalDateTime end = query.getApplicantTimeEnd();
        LocalDateTime start = query.getApplicantTimeStart();
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            queryWrapper.between(MeetingReserve::getApplicantTime, start, end);
        }
        queryWrapper.like(StringUtils.isNotBlank(query.getMobile()), MeetingReserve::getMobile, query.getMobile());
        queryWrapper.eq(query.getReviewStatus() != null, MeetingReserve::getReviewStatus, query.getReviewStatus());
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), MeetingReserve::getParkOrgId, query.getParkOrgId());
        queryWrapper.in(MeetingReserve::getParkOrgId, parkOrgIds).orderByDesc(MeetingReserve::getApplicantTime);
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<MeetingReserveDTO> entitiesToDTOs(List<MeetingReserve> entities) {
        return MyBeanUtils.copyList(entities, MeetingReserveDTO::new, (s, t) -> {
            List<MeetingReserveTimeDTO> reserveTimeList = MyBeanUtils.copyList(
                    reserveTimeService.lambdaQuery().eq(MeetingReserveTime::getMeetingReserveId, s.getId())
                            .orderByAsc(MeetingReserveTime::getNowDate, MeetingReserveTime::getTimeType).list(),
                    MeetingReserveTimeDTO::new);
            t.setReserveTimeList(reserveTimeList);
            String reserveTimeStr = convertToString(reserveTimeList);
            t.setReserveTimeStr(reserveTimeStr);
            List<MeetingReserveGoodsDTO> reserveGoodsList = MyBeanUtils.copyList(
                    reserveGoodsService.lambdaQuery().eq(MeetingReserveGoods::getMeetingReserveId, s.getId()).list(),
                    MeetingReserveGoodsDTO::new);
            t.setReserveGoodsList(reserveGoodsList);
        });
    }

    private List<MeetingReserveExportDTO> convertToExcel(List<MeetingReserve> entities) {
        return MyBeanUtils.copyList(entities, MeetingReserveExportDTO::new, (s, t) -> {
            // DO 拼接时间字段
            List<MeetingReserveTimeDTO> reserveTimeList = MyBeanUtils.copyList(
                    reserveTimeService.lambdaQuery().eq(MeetingReserveTime::getMeetingReserveId, s.getId())
                            .orderByAsc(MeetingReserveTime::getNowDate, MeetingReserveTime::getTimeType).list(),
                    MeetingReserveTimeDTO::new);
            String reserveTimeStr = convertToString(reserveTimeList);
            t.setReserveTimeList(reserveTimeStr);
            // TODO 金额处理 ,加0 加-
            // if (!s.getReviewStatus().equals(DictUtil.getValue(Constant.CusMeetingAuditStatus.AUDIT_RENDING))) {
            // t.setReviewAmount("-");
            // } else {
            // t.setReviewAmount(s.getReviewAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            // }
            // t.setEstimatedAmount(s.getEstimatedAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        });
    }

    // 时间段拼接
    private String convertToString(List<MeetingReserveTimeDTO> reserveTimeDTOList) {
        Map<LocalDate, List<MeetingReserveTimeDTO>> map =
                reserveTimeDTOList.stream().collect(Collectors.groupingBy(MeetingReserveTimeDTO::getNowDate));
        // method1
        final StringBuilder[] result = {new StringBuilder("")};
        map.forEach((k, v) -> {
            System.out.println(k + " : " + v);
            if (StringUtils.isNotBlank(result[0].toString())) {
                result[0].append("、");
            }
            result[0].append(k.toString()).append(" ");
            for (int i = 0; i < v.size(); i++) {
                if (i != Constants.ZERO) {
                    result[0].append("，");
                }
                MeetingReserveTimeDTO dto = v.get(i);
                result[0].append(dto.getStartTime()).append("-").append(dto.getEndTime());
            }
        });
        return result[0].toString();
    }
}
