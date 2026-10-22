package com.crlink.smartpark.customer.service.impl;

import com.crlink.smartpark.attendance.dto.FieldworkApplyRecordExportDTO;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.dto.NumberSeqDTO;
import com.crlink.smartpark.common.feign.NumberProvider;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.entity.EquipmentLedger;
import com.crlink.smartpark.customer.entity.ProGoodsManager;
import com.crlink.smartpark.customer.controller.request.query.ProGoodsManagerQuery;
import com.crlink.smartpark.customer.model.dto.EquipmentLedgerImportDTO;
import com.crlink.smartpark.customer.model.dto.ProGoodsManagerReqDTO;
import com.crlink.smartpark.customer.dto.ProGoodsManagerDTO;
import com.crlink.smartpark.customer.entity.ProStockManager;
import com.crlink.smartpark.customer.model.dto.ProGoodsSettingsReqDTO;
import com.crlink.smartpark.customer.model.excel.ProGoodsManagerExportDTO;
import com.crlink.smartpark.customer.model.excel.ProGoodsManagerImportDTO;
import com.crlink.smartpark.customer.service.ProGoodsManagerService;
import com.crlink.smartpark.customer.mapper.ProGoodsManagerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.customer.service.ProStockManagerService;
import com.github.pagehelper.PageInfo;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.Meter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crlink.smartpark.common.constants.Constants.SMALL_DATE_FORMAT;

/**
 * 物品管理 服务实现类
 * */
@Service
@Slf4j
public class ProGoodsManagerServiceImpl extends ServiceImpl<ProGoodsManagerMapper, ProGoodsManager>
    implements ProGoodsManagerService {

    @Autowired
    private NumberProvider numberProvider;

    @Autowired
    private ProStockManagerService stockManagerService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProGoodsManager, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ProGoodsManager> lambdaQuery = this.lambdaQuery().in(ProGoodsManager::getId, ids);
        if (flag) {
            lambdaQuery.select(ProGoodsManager::getId, sValue);
        }
        Stream<ProGoodsManager> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ProGoodsManager::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ProGoodsManager::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateProGoodsManager(ProGoodsManagerReqDTO reqDTO) {
        ProGoodsManager entity = MyBeanUtils.copy(reqDTO, ProGoodsManager::new);
        // 新增时生成物品编码
        if (StringUtils.isEmpty(reqDTO.getId())) {
            entity.setGoodsNo(getGoodsNo());
        }
        EntityUserUtil.setAllDynamic(entity);
        return this.saveOrUpdate(entity);
    }

    /**
     * 通过id查询实体
     */
    @Override
    public ProGoodsManagerDTO getProGoodsManagerById(String id) {
        ProGoodsManager entity =
            Optional.ofNullable(getById(id)).orElseThrow(() -> new ResolinkRuntimeException("100071"));
        return entitiesToDTOs(Lists.newArrayList(entity)).get(0);
    }

    /**
     * 分页查询
     *
     * @return 分页结果
     */
    @Override
    @Id2objHandle
    public PageInfo<ProGoodsManagerDTO> listProGoodsManagerPage(ProGoodsManagerQuery query) {
        query.startPage();
        List<ProGoodsManager> list = this.list(getConditionWrapper(query));
        MyPageInfoUtils.isEmptyPage(list);
        List<ProGoodsManagerDTO> result = entitiesToDTOs(list);
        return query.createPageInfo(result);
    }

    @Override
    public Boolean removeGoodsById(String id) {
        // do 当物品已被“库存管理”模块关联时，不允许删除。
        int count = stockManagerService.lambdaQuery().eq(ProStockManager::getGoodsId, id).count();
        if (count > 0) {
            throw new ResolinkRuntimeException("600002");
        } else {
            return this.removeById(id);
        }
    }

    @Override
    public Boolean settingMinStock(ProGoodsSettingsReqDTO reqDTO) {
        ProGoodsManager entity = MyBeanUtils.copy(reqDTO, ProGoodsManager::new);
        EntityUserUtil.setAllDynamic(entity);
        return this.updateById(entity);
    }

    @Override
    public void export(ProGoodsManagerQuery reqDTO) {
        ExcelExportUtils.export(() -> list(getConditionWrapper(reqDTO)), this::convertToExcel, Constant.GOODS_FILE_NAME,
            ProGoodsManagerExportDTO.class);
    }

    @Override
    public FileImportErrorInfoDTO importGoods(MultipartFile file, String parkOrgId) {
        ExcelImportRollbackListener<ProGoodsManagerImportDTO> importListener =
            new ExcelImportRollbackListener<>(ProGoodsManagerImportDTO.class,
                // 业务参数校验方法
                s -> {
                    return null;
                },
                // 数据读取完成后操作
                result -> {
                    List<ProGoodsManager> list = MyBeanUtils.copyList(result, ProGoodsManager::new, (s, t) -> {
                        t.setParkOrgId(parkOrgId);
                        t.setGoodsNo (getGoodsNo ());
                        EntityUserUtil.setAllDynamic(t);
                    });
                     this.saveBatch (list);
                    log.info("list{}", list);
                });
        ExcelImportUtils.importExcel(file, ProGoodsManagerImportDTO.class, importListener);
        return importListener.getImportResult();
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ProGoodsManager> getConditionWrapper(ProGoodsManagerQuery query) {
        LambdaQueryWrapper<ProGoodsManager> queryWrapper = new LambdaQueryWrapper<ProGoodsManager>();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        queryWrapper.like(StringUtils.hasText(query.getGoodsName()), ProGoodsManager::getGoodsName,
            query.getGoodsName());
        queryWrapper.eq(StringUtils.hasText(query.getParkOrgId()), ProGoodsManager::getParkOrgId, query.getParkOrgId())
            .in(ProGoodsManager::getParkOrgId, parkOrgIds).orderByDesc(ProGoodsManager::getCreateTime);

        return queryWrapper;
    }

    private String getGoodsNo(){
        NumberSeqDTO numberSeq = numberProvider.getNumberSeq(Constant.GOODS_MANAGER_NUMBER);
        String no = new StringBuilder(Constant.GOODS_MANAGER_NUMBER)
                .append(numberSeq.getDateTime().format(DateTimeFormatter.ofPattern(SMALL_DATE_FORMAT)))
                .append(numberSeq.getValue()).toString();
        return no;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<ProGoodsManagerDTO> entitiesToDTOs(List<ProGoodsManager> entities) {
        return MyBeanUtils.copyList(entities, ProGoodsManagerDTO::new, (s, t) -> {

        });
    }

    private List<ProGoodsManagerExportDTO> convertToExcel(List<ProGoodsManager> entities) {
        return MyBeanUtils.copyList(entities, ProGoodsManagerExportDTO::new, (s, t) -> {

        });
    }
}
