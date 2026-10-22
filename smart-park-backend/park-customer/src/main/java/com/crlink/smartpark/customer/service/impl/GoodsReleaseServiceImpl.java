package com.crlink.smartpark.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.BaseQuery;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.controller.request.GoodsReleaseCheckReqDTO;
import com.crlink.smartpark.customer.controller.request.GoodsReleaseReqDTO;
import com.crlink.smartpark.customer.controller.request.query.GoodsReleaseQuery;
import com.crlink.smartpark.customer.dto.Goods;
import com.crlink.smartpark.customer.dto.GoodsReleaseDTO;
import com.crlink.smartpark.customer.entity.GoodsRelease;
import com.crlink.smartpark.customer.mapper.GoodsReleaseMapper;
import com.crlink.smartpark.customer.model.excel.GoodsReleaseExcelExportDTO;
import com.crlink.smartpark.customer.service.GoodsReleaseService;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.*;
import static com.crlink.smartpark.common.constants.enums.JobCodeEnum.*;
import static com.crlink.smartpark.customer.common.Constant.GOODS_RELEASE_EXPORT_FILENAME;
import static com.crlink.smartpark.customer.common.Constant.GoodsReleaseStatusEnum.*;
import static com.crlink.smartpark.customer.common.Constant.WxGoodsReleaseTagStatusEnum.ALL_TAG;
import static com.crlink.smartpark.customer.common.Constant.WxGoodsReleaseTagStatusEnum.CHECKED;
import static java.util.stream.Collectors.joining;


/**
 * 服务实现类
 * */
@Service
public class GoodsReleaseServiceImpl extends ServiceImpl<GoodsReleaseMapper, GoodsRelease> implements GoodsReleaseService {
    public static final String GOODS_RELEASE_NUMBER = "FX";

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private EmployeeProvider employeeProvider;


    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<GoodsRelease, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<GoodsRelease> lambdaQuery = this.lambdaQuery().in(GoodsRelease::getId, ids);
        if (flag) {
            lambdaQuery.select(GoodsRelease::getId, sValue);
        }
        Stream<GoodsRelease> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(GoodsRelease::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(GoodsRelease::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Boolean saveOrUpdateGoodsRelease(GoodsReleaseReqDTO reqDTO) {
        GoodsRelease entity = MyBeanUtils.copy(reqDTO, GoodsRelease::new);
        EntityUserUtil.setAll(entity);
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(GOODS_RELEASE_NUMBER);
        String no = new StringBuilder(GOODS_RELEASE_NUMBER).append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                .append(numberSeq.getValue()).toString();
        entity.setGoodsNo(no);
        return this.save(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public GoodsReleaseDTO getGoodsReleaseById(String id) {
        GoodsRelease entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        GoodsReleaseDTO goodsReleaseDTO = MyBeanUtils.copy(entity, GoodsReleaseDTO::new, (s, t) -> {
            t.setReleaseDatetimeStart(s.getReleaseDatetimeStart().format(TIME_WITHOUT_SECONDS_FORMATTER));
            t.setReleaseDatetimeEnd(s.getReleaseDatetimeEnd().format(TIME_WITHOUT_SECONDS_FORMATTER));
            completeGoodReleaseStatus(entity, AUTO_CANCEL.state);
            t.setStatus(s.getStatus());
        });
        goodsReleaseDTO.setStatus(entity.getStatus());
        String userId = SystemUtil.getInstance().getCurrentUserId();
        goodsReleaseDTO.setSelfCreate(Objects.equals(userId, goodsReleaseDTO.getCreateBy()));
        return goodsReleaseDTO;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<GoodsReleaseDTO> listGoodsReleasePage(GoodsReleaseQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        query.startPage();
        List<GoodsRelease> list = this.list(getConditionWrapper(query, parkOrgIds));
        MyPageInfoUtils.isEmptyPage(list);
        List<GoodsReleaseDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }


    @Override
    public void exportGoodsRelease(GoodsReleaseQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIdsOrFakeIds();
        ExcelExportUtils.export(() -> this.list(getConditionWrapper(query, parkOrgIds)),
                sourceList -> MyBeanUtils.copyList(sourceList, GoodsReleaseExcelExportDTO::new, (s, t) -> completeGoodsReleaseInfo(s, t)),
                GOODS_RELEASE_EXPORT_FILENAME, GoodsReleaseExcelExportDTO.class);
    }


    /**
     * c端小程序分页查询
     *
     * @param query
     * @param status
     * @return
     */
    @Override
    public PageInfo<GoodsReleaseDTO> cWxGoodsReleasePage(BaseQuery<String> query, Integer status) {
        String parkOrgId = query.getParam();
        LambdaQueryChainWrapper<GoodsRelease> lambdaQuery = wxStatusQuery(status);
        lambdaQuery.eq(StringUtils.isNotBlank(parkOrgId), GoodsRelease::getParkOrgId, parkOrgId);
        lambdaQuery.eq(GoodsRelease::getCreateBy, SystemUtil.getInstance().getCurrentUserId());
        lambdaQuery.orderByDesc(GoodsRelease::getCreateTime);
        query.startPage();
        List<GoodsRelease> list = lambdaQuery.list();
        List<GoodsReleaseDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }


    /**
     * 运营端小程序分页查询
     *
     * @param query
     * @param status
     * @return
     */
    @Override
    public PageInfo<GoodsReleaseDTO> bWxGoodsReleasePage(BaseQuery query, Integer status) {
        String parkOrgId = SystemUtil.getInstance().getEmployeeParkOrgId();
        MyPageInfoUtils.isEmptyPage(parkOrgId);
        LambdaQueryChainWrapper<GoodsRelease> lambdaQuery = bWxStatusTagQuery(status);
        lambdaQuery.eq(GoodsRelease::getParkOrgId, parkOrgId);
        lambdaQuery.orderByDesc(GoodsRelease::getCreateTime);
        query.startPage();
        List<GoodsRelease> list = lambdaQuery.list();
        List<GoodsReleaseDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }


    /**
     * 放行审核
     *
     * @param reqDTO
     * @return
     */
    @Override
    public Boolean checkGoodsRelease(GoodsReleaseCheckReqDTO reqDTO) {
        GoodsRelease goodsRelease = this.lambdaQuery()
                .select(GoodsRelease::getId, GoodsRelease::getStatus, GoodsRelease::getReleaseDatetimeEnd)
                .eq(GoodsRelease::getId, reqDTO.getGoodsReleaseId()).one();
        goodsRelease = Optional.ofNullable(goodsRelease).orElseThrow(() -> new ResolinkRuntimeException("100072"));
        Integer checkStatus = reqDTO.getCheckStatus();
        //为待审核状态才可审核
        completeGoodReleaseStatus(goodsRelease, CANCEL.state);
        if (NOT_CHECK.state == goodsRelease.getStatus()) {
            goodsRelease.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
            goodsRelease.setStatus(checkStatus);
            goodsRelease.setCheckOpinion(reqDTO.getCheckOpinion());
            return this.updateById(goodsRelease);
        } else {
            throw new ResolinkRuntimeException("50008");
        }
    }


    /**
     * 取消放行申请
     *
     * @param goodsReleaseId
     * @return
     */
    @Override
    public Boolean cancelGoodsRelease(String goodsReleaseId) {
        GoodsRelease goodsRelease = this.lambdaQuery()
                .select(GoodsRelease::getId, GoodsRelease::getStatus, GoodsRelease::getReleaseDatetimeEnd)
                .eq(GoodsRelease::getId, goodsReleaseId).one();
        goodsRelease = Optional.ofNullable(goodsRelease).orElseThrow(() -> new ResolinkRuntimeException("100072"));
        //为待审核状态才可取消
        completeGoodReleaseStatus(goodsRelease, CANCEL.state);
        if (NOT_CHECK.state == goodsRelease.getStatus()) {
            goodsRelease.setUpdateBy(SystemUtil.getInstance().getCurrentUserId());
            goodsRelease.setStatus(CANCEL.state);
            return this.updateById(goodsRelease);
        } else {
            throw new ResolinkRuntimeException("50008");
        }
    }


    private LambdaQueryChainWrapper<GoodsRelease> bWxStatusTagQuery(Integer status) {
        Set<Integer> employeeJobCodes = Optional.ofNullable(SystemUtil.getInstance().getEmployeeJobCodes())
                .orElseThrow(() -> new ResolinkRuntimeException("40008"));
        LambdaQueryChainWrapper<GoodsRelease> lambdaQuery = this.lambdaQuery();
        if (employeeJobCodes.contains(ADMINISTRATOR_JOB_CODE.code)
                || employeeJobCodes.contains(GOODS_RELEASE_CHECK_JOB_CODE.code)) {
            //管理员、物品放行审核员可看到园区内所有相关数据
            lambdaQuery = wxStatusQuery(status);
            return lambdaQuery;
        } else if (employeeJobCodes.contains(SECURITY_JOB_CODE.code)
                || employeeJobCodes.contains(ADMINISTRATION_JOB_CODE.code)) {
            //保安、行政只能看到他人提交的放行申请，仅显示“审核通过“的数据
            return lambdaQuery.eq(GoodsRelease::getStatus, CHECK_PASS.state);
        }
        throw new ResolinkRuntimeException("40008");

    }

    /**
     * 小程序根据状态封装查询条件
     *
     * @param tagStatus
     * @return
     */
    private LambdaQueryChainWrapper<GoodsRelease> wxStatusQuery(Integer tagStatus) {
        LambdaQueryChainWrapper<GoodsRelease> lambdaQuery = this.lambdaQuery();
        if (tagStatus != ALL_TAG.state) {
            if (tagStatus == NOT_CHECK.state) {
                lambdaQuery.eq(GoodsRelease::getStatus, NOT_CHECK.state)
                        .gt(GoodsRelease::getReleaseDatetimeEnd, LocalDateTime.now());
            } else if (tagStatus == CHECKED.state) {
                lambdaQuery.in(GoodsRelease::getStatus, CHECK_PASS.state, CHECK_REJECT.state);
            }
        }
        return lambdaQuery;
    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<GoodsRelease> getConditionWrapper(GoodsReleaseQuery query, Set<String> parkOrgIds) {
        LambdaQueryWrapper<GoodsRelease> queryWrapper = new LambdaQueryWrapper<GoodsRelease>();
        queryWrapper.like(StringUtils.isNotBlank(query.getSubmitterName()), GoodsRelease::getSubmitterName, query.getSubmitterName());
        queryWrapper.like(StringUtils.isNotBlank(query.getSubmitterMobile()), GoodsRelease::getSubmitterMobile, query.getSubmitterMobile());
        queryWrapper.eq(StringUtils.isNotBlank(query.getParkOrgId()), GoodsRelease::getParkOrgId, query.getParkOrgId())
                .in(GoodsRelease::getParkOrgId, parkOrgIds).orderByDesc(GoodsRelease::getCreateTime);
        Integer status = query.getStatus();
        if (Objects.nonNull(status)) {
            if (NOT_CHECK.state == status) {
                queryWrapper.eq(GoodsRelease::getStatus, status)
                        .ge(GoodsRelease::getReleaseDatetimeEnd, LocalDateTime.now());
            } else if (CANCEL.state == status) {
                queryWrapper.and(con1 -> con1.eq(GoodsRelease::getStatus, status)
                        .or(con2 -> con2.eq(GoodsRelease::getStatus, NOT_CHECK.state).le(GoodsRelease::getReleaseDatetimeEnd, LocalDateTime.now())));
            } else {
                queryWrapper.eq(GoodsRelease::getStatus, status);
            }
        }
        LocalDateTime end = query.getCreateTimeEnd();
        LocalDateTime start = query.getCreateTimeStart();
        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            queryWrapper.le(GoodsRelease::getCreateTime, end).ge(GoodsRelease::getCreateTime, start);
        }
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private void completeGoodReleaseStatus(GoodsRelease goodsRelease, int status) {
        if (goodsRelease.getStatus() == NOT_CHECK.state) {
            if (LocalDateTime.now().isAfter(goodsRelease.getReleaseDatetimeEnd())) {
                goodsRelease.setStatus(status);
            }
        }
    }

    private List<GoodsReleaseDTO> entitiesToDTOs(List<GoodsRelease> entities) {
        return MyBeanUtils.copyList(entities, GoodsReleaseDTO::new, (s, t) -> {
            t.setReleaseDatetimeStart(s.getReleaseDatetimeStart().format(TIME_WITHOUT_SECONDS_FORMATTER));
            t.setReleaseDatetimeEnd(s.getReleaseDatetimeEnd().format(TIME_WITHOUT_SECONDS_FORMATTER));
            completeGoodReleaseStatus(s, CANCEL.state);
            t.setStatus(s.getStatus());
        });
    }

    private void completeGoodsReleaseInfo(GoodsRelease goods, GoodsReleaseExcelExportDTO dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT_WITHOUT_SECONDS);
        String start = formatter.format(goods.getReleaseDatetimeStart());
        String end = formatter.format(goods.getReleaseDatetimeEnd());
        dto.setReleaseDatetime(new StringBuilder(start).append("-").append(end).toString());
        String goodsList = goods.getGoodsList().parallelStream().map(Goods::toString).collect(joining(","));
        dto.setGoodsList(goodsList);
    }
}
