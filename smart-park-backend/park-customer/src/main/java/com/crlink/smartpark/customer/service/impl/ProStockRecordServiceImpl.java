package com.crlink.smartpark.customer.service.impl;

import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.dto.ProStockManagerDTO;
import com.crlink.smartpark.customer.entity.ProGoodsManager;
import com.crlink.smartpark.customer.entity.ProStockRecord;
import com.crlink.smartpark.customer.controller.request.query.ProStockRecordQuery;
import com.crlink.smartpark.customer.model.dto.ProStockRecordReqDTO;
import com.crlink.smartpark.customer.dto.ProStockRecordDTO;
import com.crlink.smartpark.customer.model.excel.ProGoodsManagerExportDTO;
import com.crlink.smartpark.customer.model.excel.ProStockRecordExportDTO;
import com.crlink.smartpark.customer.service.ProStockRecordService;
import com.crlink.smartpark.customer.mapper.ProStockRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.time.format.DateTimeFormatter;
import java.util.Collections;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.SMALL_DATE_FORMAT;


/**
 * 库存出入库记录 服务实现类
 * */
@Service
public class ProStockRecordServiceImpl extends ServiceImpl<ProStockRecordMapper, ProStockRecord> implements ProStockRecordService {

    @Autowired
    private NumberProvider numberProvider;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProStockRecord, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ProStockRecord> lambdaQuery = this.lambdaQuery().in(ProStockRecord::getId, ids);
        if (flag) {
            lambdaQuery.select(ProStockRecord::getId, sValue);
        }
        Stream<ProStockRecord> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ProStockRecord::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ProStockRecord::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateProStockRecord(ProStockRecordReqDTO reqDTO) {
        ProStockRecord entity = MyBeanUtils.copy(reqDTO, ProStockRecord::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    @Override
    public Boolean saveOrUpdateStockRecord(ProStockRecord stockRecord) {
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(Constant.GOODS_STOCK_IN);
        String no = new StringBuilder(Constant.GOODS_STOCK_IN)
                .append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                .append(numberSeq.getValue()).toString();
        stockRecord.setRecordNo(no);
        EntityUserUtil.setAllDynamic(stockRecord);
        return this.save(stockRecord);
    }

    @Override
    public void saveRecordBatch(List<ProStockRecord> stockRecordList) {
        stockRecordList.forEach (e->{
            this.saveOrUpdateStockRecord (e);
        });
    }


    /**
     * 通过id查询实体
     */
    @Override
    public ProStockRecordDTO getProStockRecordById(String id) {
        ProStockRecord entity = Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<ProStockRecordDTO> listProStockRecordPage(ProStockRecordQuery query) {
        query.startPage();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        List<ProStockRecordDTO> result = baseMapper.listPage(query, parkOrgIds);
        MyPageInfoUtils.isEmptyPage(result);
        result.stream().forEach(e -> {
            setStockIn(e);
        });
        return query.createPageInfo(result);
    }

    @Override
    public void export(ProStockRecordQuery reqDTO) {
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        List<ProStockRecordDTO> result = baseMapper.listPage(reqDTO, parkOrgIds);
        ExcelExportUtils.export(() -> result, this::convertToExcel, Constant.STOCK_RECORD_FILE_NAME, ProStockRecordExportDTO.class);

    }


    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ProStockRecord> getConditionWrapper(ProStockRecordQuery query) {
        LambdaQueryWrapper<ProStockRecord> queryWrapper = new LambdaQueryWrapper<ProStockRecord>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     * @return DTO集合
     */
    private List<ProStockRecordDTO> entitiesToDTOs(List<ProStockRecord> entities) {
        return MyBeanUtils.copyList(entities, ProStockRecordDTO::new, (s, t) -> {

        });
    }

    private List<ProStockRecordExportDTO> convertToExcel(List<ProStockRecordDTO> entities) {
        return MyBeanUtils.copyList(entities, ProStockRecordExportDTO::new, (s, t) -> {
            setStockIn(s);
            t.setGoodsHouse(Constant.GOODS_HOUSE_NAME);
            t.setStockInNum(s.getStockInNum());
            t.setStockInUnitPrice(s.getStockInUnitPrice());
        });
    }

    private void setStockIn(ProStockRecordDTO stockRecordDTO) {
        if (DictUtil.getValue(Constant.ProStockMode.MODIFY_INVENTORY).equals(stockRecordDTO.getStockMode())) {
            stockRecordDTO.setStockInNum("-");
            stockRecordDTO.setStockInUnitPrice("-");
        } else if (DictUtil.getValue(Constant.ProStockMode.STOCK_OUT).equals(stockRecordDTO.getStockMode())) {
            stockRecordDTO.setStockInNum("-" + stockRecordDTO.getStockInNum());
            stockRecordDTO.setStockInUnitPrice("-");
        }

    }
}
