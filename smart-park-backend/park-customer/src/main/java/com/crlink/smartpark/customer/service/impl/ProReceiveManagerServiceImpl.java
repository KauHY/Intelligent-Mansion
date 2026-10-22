package com.crlink.smartpark.customer.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.constants.DictConstant;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.controller.request.*;
import com.crlink.smartpark.customer.controller.request.query.ProReceiveManagerQuery;
import com.crlink.smartpark.customer.dto.ProReceiveDetailDTO;
import com.crlink.smartpark.customer.dto.ProReceiveManagerDTO;
import com.crlink.smartpark.customer.entity.*;
import com.crlink.smartpark.customer.mapper.ProReceiveManagerMapper;
import com.crlink.smartpark.customer.model.vo.ReceiveDetailVo;
import com.crlink.smartpark.customer.model.vo.StockManagerSelectVo;
import com.crlink.smartpark.customer.service.*;
import com.crlink.smartpark.system.dto.employee.EmployeeQueryDTO;
import com.crlink.smartpark.system.feign.EmployeeProvider;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

import static com.crlink.smartpark.common.constants.Constants.SMALL_DATE_FORMAT;


/**
 * 物品领用管理 服务实现类
 * */
@Service
public class ProReceiveManagerServiceImpl extends ServiceImpl<ProReceiveManagerMapper, ProReceiveManager> implements ProReceiveManagerService {


    @Autowired
    private ProGoodsManagerService proGoodsManagerService;

    @Autowired
    private ProStockManagerService proStockManagerService;

    @Autowired
    private ProReceiveFlowService proReceiveFlowService;

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private EmployeeProvider employeeProvider;

    @Autowired
    private ProReceivePermissionService proReceivePermissionService;

    @Autowired
    private ProStockRecordService proStockRecordService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProReceiveManager, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ProReceiveManager> lambdaQuery = this.lambdaQuery().in(ProReceiveManager::getId, ids);
        if (flag) {
            lambdaQuery.select(ProReceiveManager::getId, sValue);
        }
        Stream<ProReceiveManager> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ProReceiveManager::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ProReceiveManager::getId, Function.identity()));
        }
        return result;
    }

    @Override
    public Boolean auth(String parkOrgId) {
        if (!proReceivePermissionService.auth(parkOrgId)) {
            throw new ResolinkRuntimeException("600006");
        }
        return true;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateProReceiveManager(ProReceiveApplyDTO reqDTO) {
        ProStockManager proStockManager = Optional.ofNullable(proStockManagerService.getById(reqDTO.getStockId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (reqDTO.getReceiveNum() > proStockManager.getStockNum()) {
            throw new ResolinkRuntimeException("600001");
        }
        ProReceiveManager entity = MyBeanUtils.copy(reqDTO, ProReceiveManager::new);
        entity.setApplyTime(LocalDateTime.now());
        EntityUserUtil.setAllDynamic(entity);
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(Constant.GOODS_RECEIVE_NO);
        String no = new StringBuilder(Constant.GOODS_RECEIVE_NO)
                .append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                .append(numberSeq.getValue()).toString();
        entity.setApplyNo(no);
        entity.setStockState(Constant.ProReceiveStockState.NOT_OUT.state);
        entity.setAuditState(Constant.ProReceiveAuditState.NOT_RECEIVE.state);
        this.saveOrUpdate(entity);
        ProReceiveFlow proReceiveFlow = new ProReceiveFlow();
        proReceiveFlow.setReceiveId(entity.getId());
        proReceiveFlow.setAuditState(Constant.ProReceiveAuditState.NOT_RECEIVE.state);
        return proReceiveFlowService.saveOrUpdate(proReceiveFlow);
    }

    @Override
    public Boolean returnGoods(ProReceiveReturnDTO reqDTO) {

        ProReceiveManager proReceiveManager = Optional.ofNullable(this.getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));

        if (reqDTO.getReturnNum() > (proReceiveManager.getReceiveNum() - proReceiveManager.getReturnNumTotal())) {
            throw new ResolinkRuntimeException("600003");
        }
        ProReceiveFlow proReceiveFlow = new ProReceiveFlow();
        if (StringUtils.isNotBlank(reqDTO.getRemark())) {
            proReceiveFlow.setRemark(reqDTO.getRemark());
        }
        proReceiveManager.setAuditState(Constant.ProReceiveAuditState.NOT_RETURN.state);
        proReceiveManager.setReturnNumCurrent(reqDTO.getReturnNum());
        this.updateById(proReceiveManager);

        proReceiveFlow.setReceiveId(proReceiveManager.getId());
        proReceiveFlow.setReturnNumCurrent(reqDTO.getReturnNum());
        proReceiveFlow.setAuditState(Constant.ProReceiveAuditState.NOT_RETURN.state);
        return proReceiveFlowService.saveOrUpdate(proReceiveFlow);
    }

    @Override
    public PageInfo<StockManagerSelectVo> selectGoods(ProReceiveGoodsDTO reqDTO) {
        reqDTO.startPage();
        Set<String> goodsIdSet = proGoodsManagerService.lambdaQuery().eq(reqDTO.getGoodsType() != null, ProGoodsManager::getGoodsType, reqDTO.getGoodsType())
                .eq(StringUtils.isNotBlank(reqDTO.getGoodsName()), ProGoodsManager::getGoodsName, reqDTO.getGoodsName()).list()
                .parallelStream().map(ProGoodsManager::getId).collect(Collectors.toSet());
        if (CollectionUtil.isEmpty(goodsIdSet)) {
            return new PageInfo<>();
        }
        List<StockManagerSelectVo> selectVoList = proStockManagerService.getStockByGoodsIds(goodsIdSet);
        MyPageInfoUtils.isEmptyPage(selectVoList);
        return reqDTO.createPageInfo(selectVoList);
    }

    @Override
    public Boolean cancel(ProReceiveCancelDTO reqDTO) {
        ProReceiveManager proReceiveManager = Optional.ofNullable(this.getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (!proReceiveManager.getAuditState().equals(Constant.ProReceiveAuditState.NOT_RECEIVE.state)) {
            throw new ResolinkRuntimeException("600005");
        }
        proReceiveManager.setAuditState(Constant.ProReceiveAuditState.CANCEL.state);
        this.updateById(proReceiveManager);
        ProReceiveFlow proReceiveFlow = new ProReceiveFlow();
        proReceiveFlow.setReceiveId(proReceiveManager.getId());
        proReceiveFlow.setAuditState(Constant.ProReceiveAuditState.CANCEL.state);
        proReceiveFlow.setCancelTime(LocalDateTime.now());
        return proReceiveFlowService.saveOrUpdate(proReceiveFlow);
    }

    @Override
    public void auditReceive(ProReceiveInAuditDTO reqDTO) {
        ProReceiveManager proReceiveManager = Optional.ofNullable(this.getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (!proReceiveManager.getAuditState().equals(Constant.ProReceiveAuditState.NOT_RECEIVE.state)) {
            throw new ResolinkRuntimeException("600005");
        }
        ProStockManager proStockManager = Optional.ofNullable(proStockManagerService.getById(proReceiveManager.getStockId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        proReceiveManager.setNeedReturn(reqDTO.getNeedReturn());
        proReceiveManager.setAuditState(reqDTO.getAuditState());

        ProReceiveFlow proReceiveFlow = new ProReceiveFlow();
        proReceiveFlow.setAuditUserId(SystemUtil.getInstance().getCurrentUserId());
        proReceiveFlow.setReceiveId(proReceiveManager.getId());
        proReceiveFlow.setAuditState(reqDTO.getAuditState());
        proReceiveFlow.setAuditTime(LocalDateTime.now());
        proReceiveFlow.setAuditMsg(reqDTO.getAuditMsg());
        proReceiveFlow.setNeedReturn(reqDTO.getNeedReturn());
        //领用审核通过则更新库存 存记录
        if (reqDTO.getAuditState().equals(Constant.ProReceiveAuditState.RECEIVE_PASS.state)) {
            proReceiveManager.setStockState(Constant.ProReceiveStockState.OUT_PASS.state);
            proReceiveManager.setStockOutTime(LocalDateTime.now());
            //更新库存
            if (proStockManager.getStockNum() < proReceiveManager.getReceiveNum()) {
                throw new ResolinkRuntimeException("600004");
            }
            proStockManager.setStockNum(proStockManager.getStockNum() - proReceiveManager.getReceiveNum());
            proStockManagerService.updateById(proStockManager);
            //存出库记录
            ProStockRecord proStockRecord = new ProStockRecord();
            proStockRecord.setParkOrgId(proReceiveManager.getParkOrgId());
            proStockRecord.setStockId(proReceiveManager.getStockId());
            proStockRecord.setGoodsId(proStockManager.getGoodsId());
            NumberSeqDTO numberSeq = numberProvider.getNumberSeq(Constant.GOODS_STOCK_OUT);
            String no = new StringBuilder(Constant.GOODS_STOCK_OUT)
                    .append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                    .append(numberSeq.getValue()).toString();
            proStockRecord.setRecordNo(no);
            proStockRecord.setStockInNum(proReceiveManager.getReceiveNum());
            proStockRecord.setCurrentNum(proStockManager.getStockNum());
            proStockRecord.setCurrentAveragePrice(proStockManager.getAveragePrice());
            proStockRecord.setStockType(DictUtil.getValue(Constant.ProStockType.RECEIPT_DELIVERY));
            proStockRecord.setStockMode(DictUtil.getValue(Constant.ProStockMode.STOCK_IN));
            //2022.03.22 v12 新增需求 增加备注字段
            proStockRecord.setRemark(reqDTO.getAuditMsg());
            EntityUserUtil.setAllDynamic(proStockRecord);
            proStockRecordService.save(proStockRecord);
            proReceiveFlow.setStockNo(no);
        }
        EntityUserUtil.setAllDynamic(proReceiveManager);
        this.updateById(proReceiveManager);
        proReceiveFlowService.saveOrUpdate(proReceiveFlow);
    }

    @Override
    public void auditReturn(ProReceiveOutAuditDTO reqDTO) {
        ProReceiveManager proReceiveManager = Optional.ofNullable(this.getById(reqDTO.getId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        if (!proReceiveManager.getAuditState().equals(Constant.ProReceiveAuditState.NOT_RETURN.state)) {
            throw new ResolinkRuntimeException("600005");
        }
        proReceiveManager.setAuditState(reqDTO.getAuditState());

        ProReceiveFlow proReceiveFlow = new ProReceiveFlow();
        proReceiveFlow.setAuditState(reqDTO.getAuditState());
        proReceiveFlow.setAuditUserId(SystemUtil.getInstance().getCurrentUserId());
        proReceiveFlow.setReceiveId(proReceiveManager.getId());
        proReceiveFlow.setAuditTime(LocalDateTime.now());
        proReceiveFlow.setAuditMsg(reqDTO.getAuditMsg());
        if (reqDTO.getAuditState().equals(Constant.ProReceiveAuditState.RETURN_PASS.state)) {
            Integer returnNum = proReceiveManager.getReturnNumCurrent() + proReceiveManager.getReturnNumTotal();
            if (returnNum.equals(proReceiveManager.getReceiveNum())) {
                proReceiveManager.setStockState(Constant.ProReceiveStockState.ALL_IN.state);
            } else {
                proReceiveManager.setStockState(Constant.ProReceiveStockState.PART_IN.state);
            }
            proReceiveManager.setReturnNumTotal(returnNum);
            proReceiveManager.setStockInTime(LocalDateTime.now());

            //更新库存
            ProStockManager proStockManager = Optional.ofNullable(proStockManagerService.getById(proReceiveManager.getStockId())).orElseThrow(() -> new ResolinkRuntimeException("100071"));
            proStockManager.setStockNum(proStockManager.getStockNum() + proReceiveManager.getReturnNumCurrent());
            proStockManagerService.updateById(proStockManager);
            //存入库记录
            ProStockRecord proStockRecord = new ProStockRecord();
            proStockRecord.setParkOrgId(proReceiveManager.getParkOrgId());
            proStockRecord.setStockId(proReceiveManager.getStockId());
            proStockRecord.setGoodsId(proStockManager.getGoodsId());
            NumberSeqDTO numberSeq = numberProvider.getNumberSeq(Constant.GOODS_STOCK_IN);
            String no = new StringBuilder(Constant.GOODS_STOCK_IN)
                    .append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                    .append(numberSeq.getValue()).toString();
            proStockRecord.setRecordNo(no);
            proStockRecord.setStockInNum(proReceiveManager.getReturnNumCurrent());
            proStockRecord.setCurrentNum(proStockManager.getStockNum());
            proStockRecord.setCurrentAveragePrice(proStockManager.getAveragePrice());
            proStockRecord.setStockType(DictUtil.getValue(Constant.ProStockType.RETURN_WAREHOUSE));
            proStockRecord.setStockMode(DictUtil.getValue(Constant.ProStockMode.STOCK_OUT));
            //2022.03.22 v12 新增需求 增加备注字段
            proStockRecord.setRemark(reqDTO.getAuditMsg());
            EntityUserUtil.setAllDynamic(proStockRecord);
            proStockRecordService.save(proStockRecord);
            proReceiveFlow.setStockNo(no);
        }
        EntityUserUtil.setAllDynamic(proReceiveManager);
        this.updateById(proReceiveManager);
        proReceiveFlowService.saveOrUpdate(proReceiveFlow);

    }

    /**
     * 通过id查询实体
     */
    @Override
    @Id2objHandle
    public ReceiveDetailVo getProReceiveManagerById(String id) {
        ProReceiveManager entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        List<ProReceiveFlow> proReceiveFlowList = proReceiveFlowService.lambdaQuery().eq(ProReceiveFlow::getReceiveId, entity.getId()).orderByAsc(ProReceiveFlow::getCreateTime).list();
        List<ProReceiveDetailDTO> receiveDetailDTOS = entitiesToDTOs(proReceiveFlowList);
        String mobile = getEmployeeMobile(entity.getEmployeeId());
        ProReceiveDetailDTO proReceiveFirst = receiveDetailDTOS.get(Constant.ZERO);
        proReceiveFirst.setApplyTime(entity.getApplyTime());
        proReceiveFirst.setRemark(entity.getRemark());
        ReceiveDetailVo receiveDetailVo = new ReceiveDetailVo();
        receiveDetailVo.setMobile(mobile);
        receiveDetailVo.setReceiveDetailList(receiveDetailDTOS);
        return receiveDetailVo;
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<ProReceiveManagerDTO> listProReceiveManagerPage(ProReceiveManagerQuery query) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        Set<String> employeeIds = null;
        if (StringUtils.isNotBlank(query.getEmployeeName())) {
            Map<String, String> employeeId2NameMap = employeeProvider.listEmployeeByName(query.getEmployeeName());
            employeeIds = employeeId2NameMap.keySet();
            MyPageInfoUtils.isEmptyPage(employeeIds);
        }
        query.startPage();
        List<ProReceiveManagerDTO> list = baseMapper.listPage(query, parkOrgIds, employeeIds);
        return query.createPageInfo(list);
    }

    @Override
    @Id2objHandle
    public PageInfo<ProReceiveManagerDTO> listProReceivePage(ProReceiveManagerQuery query) {
        query.startPage();
        query.setEmployeeId(SystemUtil.getInstance().getEmployeeId());
        List<ProReceiveManagerDTO> list = baseMapper.listWxPage(query);
        return query.createPageInfo(list);
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ProReceiveManager> getConditionWrapper(ProReceiveManagerQuery query) {
        LambdaQueryWrapper<ProReceiveManager> queryWrapper = new LambdaQueryWrapper<ProReceiveManager>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ProReceiveDetailDTO> entitiesToDTOs(List<ProReceiveFlow> entities) {
        return MyBeanUtils.copyList(entities, ProReceiveDetailDTO::new, (s, t) -> {

        });
    }

    private String getEmployeeMobile(String employeeId) {
        Map<String, Map<String, Object>> employeeMap = employeeProvider.queryEmployeeColumns(
                EmployeeQueryDTO.employeeIdsBuildDTO(Sets.newHashSet(employeeId), "mobile"));
        return (String) employeeMap.get(employeeId).get("mobile");
    }
}
