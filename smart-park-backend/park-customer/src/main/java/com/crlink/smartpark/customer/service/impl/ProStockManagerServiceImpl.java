package com.crlink.smartpark.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.annotations.Id2objHandle;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.core.utils.*;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.controller.request.query.ProStockManagerQuery;
import com.crlink.smartpark.customer.dto.ProStockManagerDTO;
import com.crlink.smartpark.customer.entity.EquipmentLedger;
import com.crlink.smartpark.customer.entity.ProGoodsManager;
import com.crlink.smartpark.customer.entity.ProStockManager;
import com.crlink.smartpark.customer.entity.ProStockRecord;
import com.crlink.smartpark.customer.mapper.ProStockManagerMapper;
import com.crlink.smartpark.customer.model.dto.ProStockManagerReqDTO;
import com.crlink.smartpark.customer.model.excel.ProGoodsManagerImportDTO;
import com.crlink.smartpark.customer.model.excel.ProStockInImportDTO;
import com.crlink.smartpark.customer.model.vo.StockManagerSelectVo;
import com.crlink.smartpark.customer.service.ProGoodsManagerService;
import com.crlink.smartpark.customer.service.ProStockManagerService;
import com.crlink.smartpark.customer.service.ProStockRecordService;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 库存管理 服务实现类
 * */
@Service
@Slf4j
public class ProStockManagerServiceImpl extends ServiceImpl<ProStockManagerMapper, ProStockManager>
        implements ProStockManagerService {

    @Autowired
    private ProGoodsManagerService goodsManagerService;

    @Autowired
    private ProStockRecordService stockRecordService;

    @Override
    public Map<String, ?> id2ValueMap(Collection<String> ids, SFunction<ProStockManager, ?> sValue) {
        if (ObjectUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        boolean flag = sValue != null;
        LambdaQueryChainWrapper<ProStockManager> lambdaQuery = this.lambdaQuery().in(ProStockManager::getId, ids);
        if (flag) {
            lambdaQuery.select(ProStockManager::getId, sValue);
        }
        Stream<ProStockManager> stream = lambdaQuery.list().parallelStream();
        Map<String, Object> result = null;
        if (flag) {
            result = stream.collect(Collectors.toMap(ProStockManager::getId, sValue));
        } else {
            result = stream.collect(Collectors.toMap(ProStockManager::getId, Function.identity()));
        }
        return result;
    }

    /**
     * 保存或更新数据
     */
    @Override
    public Boolean saveOrUpdateProStockManager(ProStockManagerReqDTO reqDTO) {

        // 新增入库时校验是否已有入库数据
        if (StringUtils.isEmpty(reqDTO.getId())) {
            ProStockManager entityCode = this.lambdaQuery().eq(ProStockManager::getGoodsId, reqDTO.getGoodsId()).one();
            // 如果没有，先新增一条数据
            if (entityCode == null) {
                entityCode = MyBeanUtils.copy(reqDTO, ProStockManager::new);
                EntityUserUtil.setAllDynamic(entityCode);
                this.saveOrUpdate(entityCode);
            }
            reqDTO.setId(entityCode.getId());
        }
        ProStockManager entity = this.getById(reqDTO.getId());

        ProStockRecord stockRecord = MyBeanUtils.copy(reqDTO, ProStockRecord::new);
        // id不使用前端回传值
        stockRecord.setId(null);
        int stockMode = reqDTO.getStockMode();
        // 入库场景
        if (DictUtil.getValue(Constant.ProStockMode.STOCK_IN).equals(stockMode)) {
            // 当前库存数量 = 库存数量+入库数量
            entity.setStockNum(entity.getStockNum() + reqDTO.getStockInNum());
            // 当入库单价不为 0；进行 入库均价 字段更新
            if (reqDTO.getStockInUnitPrice() != null && !reqDTO.getStockInUnitPrice().equals(BigDecimal.ZERO)) {
                // 累计入库数量 = 累计入库数量+新入库数量
                entity.setTotalStockNum(entity.getTotalStockNum() + reqDTO.getStockInNum());
                // 累计入库总价 = 新入库数量*单价+累计入库总价
                entity.setTotalPrice(reqDTO.getStockInUnitPrice().multiply(new BigDecimal(reqDTO.getStockInNum()))
                        .add(entity.getTotalPrice()));
                // 入库均价 = 累计入库总价 / 累计入库数量
                entity.setAveragePrice(divideBigDecimal(entity));
            } else {
                stockRecord.setStockInUnitPrice(BigDecimal.ZERO);
            }
            // 处理出入库记录表
            stockRecord.setStockType(DictUtil.getValue(Constant.ProStockType.MANUAL_WAREHOUSE));
        } else if (DictUtil.getValue(Constant.ProStockMode.STOCK_OUT).equals(stockMode)) {
            // 出库场景
            // 当前库存数量 = 库存数量-出库数量
            entity.setStockNum(entity.getStockNum() - reqDTO.getStockInNum());

            // 处理出入库记录表
            stockRecord.setStockType(DictUtil.getValue(Constant.ProStockType.MANUAL_DELIVERY));
        } else if (DictUtil.getValue(Constant.ProStockMode.MODIFY_INVENTORY).equals(stockMode)) {
            // 修改库存场景
            // 数据覆盖
            entity.setStockNum(reqDTO.getStockInNum());
            // 累计入库数量 = 更新的入库数量
            entity.setTotalStockNum(reqDTO.getStockInNum());

            // 入库单价
            BigDecimal averagePrice =
                    reqDTO.getStockInUnitPrice() != null ? reqDTO.getStockInUnitPrice() : entity.getAveragePrice();

            // 累计入库总价 = 入库数量*入库单价
            entity.setTotalPrice(averagePrice.multiply(new BigDecimal(reqDTO.getStockInNum())));
            // 入库均价 = 更新的入库单价
            entity.setAveragePrice(averagePrice);
            // 处理出入库记录表
            stockRecord.setStockType(DictUtil.getValue(Constant.ProStockType.MODIFY_INVENTORY));
        }
        // 入库记录表统一填充字段
        stockRecord.setStockId(entity.getId());
        stockRecord.setCurrentNum(entity.getStockNum());
        stockRecord.setCurrentAveragePrice(entity.getAveragePrice());
        //2022.03.22 v12 新增需求 增加备注字段
        if (!StringUtils.isEmpty(reqDTO.getRemark())) {
            stockRecord.setRemark(reqDTO.getRemark());
        } else {
            stockRecord.setRemark("-");
        }
        stockRecordService.saveOrUpdateStockRecord(stockRecord);
        EntityUserUtil.setAllDynamic(entity);
        entity.setUpdateTime(LocalDateTime.now());
        return this.saveOrUpdate(entity);
    }

    /**
     * 求入库均价 累计入库总价/累计入库数量 结果取2位小数
     *
     * @param entity:
     * @return java.math.BigDecimal
     * @MethodName divideBigDecimal
     */
    public BigDecimal divideBigDecimal(ProStockManager entity) {
        BigDecimal totalPrice = entity.getTotalPrice();
        BigDecimal totalStockNum = new BigDecimal(entity.getTotalStockNum());
        BigDecimal averagePrice = totalPrice.divide(totalStockNum, 2, BigDecimal.ROUND_HALF_UP);
        return averagePrice;
    }

    /**
     * 通过id查询实体
     */
    @Override
    public ProStockManagerDTO getProStockManagerById(String id) {
        ProStockManager entity =
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
    public PageInfo<ProStockManagerDTO> listProStockManagerPage(ProStockManagerQuery query) {
        query.startPage();
        Set<String> parkOrgIds = SystemUtil.getInstance().getParkOrgIds();
        MyPageInfoUtils.isEmptyPage(parkOrgIds);
        List<ProStockManagerDTO> result = baseMapper.listPage(query, parkOrgIds);
        MyPageInfoUtils.isEmptyPage(result);
        return query.createPageInfo(result);
    }

    @Override
    public List<StockManagerSelectVo> getStockByGoodsIds(Set<String> ids) {
        return baseMapper.getStockByGoodsIds(ids);
    }

    @Override
    @Id2objHandle
    public ProStockManagerDTO getProStockManagerByGoodsId(String goodsId) {
        ProStockManager entity = Optional.ofNullable(this.lambdaQuery().eq(ProStockManager::getGoodsId, goodsId).one())
                .orElseThrow(() -> new ResolinkRuntimeException("100071"));
        ProStockManagerDTO stockManager = entitiesToDTOs(Lists.newArrayList(entity)).get(0);
        // 土拼接
        ProGoodsManager goodsManager = goodsManagerService.getById(goodsId);
        stockManager.setGoodsName(goodsManager.getGoodsName());
        stockManager.setGoodsNo(goodsManager.getGoodsNo());
        stockManager.setGoodsType(goodsManager.getGoodsType());
        stockManager.setUnit(goodsManager.getUnit());
        return stockManager;
    }

    @Override
    public FileImportErrorInfoDTO importStockIn(MultipartFile file, String parkOrgId) {
        // 拉取当前园区的物品清单
        List<ProGoodsManager> goodsManagerList =
                goodsManagerService.lambdaQuery().eq(ProGoodsManager::getParkOrgId, parkOrgId).list();
        Map<String, ProGoodsManager> goodsMap =
                goodsManagerList.stream().collect(Collectors.toMap(ProGoodsManager::getGoodsNo, Function.identity()));
        Set<String> goodsNoSet = goodsManagerList.stream().map(ProGoodsManager::getGoodsNo).collect(Collectors.toSet());

        ExcelImportRollbackListener<ProStockInImportDTO> importListener =
                new ExcelImportRollbackListener<>(ProStockInImportDTO.class,
                        // 业务参数校验方法
                        dataCheck -> {
                            log.info("dataCheckAndOpFunction{}", dataCheck);
                            if (!goodsNoSet.contains(dataCheck.getGoodsNo())) {
                                return Message.code("600007").getDisplayText();
                            }
                            // 预留 - 仓库校验
                            return null;
                        },
                        // 数据读取完成后操作
                        result -> {
                            // 现有存量数据拉取
                            List<ProStockManager> oldStockList =
                                    this.lambdaQuery().eq(ProStockManager::getParkOrgId, parkOrgId).list();
                            Map<String, ProStockManager> oldStockMap = oldStockList.stream()
                                    .collect(Collectors.toMap(ProStockManager::getGoodsId, Function.identity()));

                            List<ProStockManager> newStockList = new ArrayList<>();
                            Map<String, ProStockManager> newStockMap = new HashMap<>();
                            List<ProStockRecord> stockRecordList = new ArrayList<>();
                            result.stream().forEach(stockInImportDTO -> {
                                // 1、获取当前导入物品编码对应ID
                                String goodsId = goodsMap.get(stockInImportDTO.getGoodsNo()).getId();
                                ProStockManager newStockManager = new ProStockManager();
                                ProStockRecord newStockRecord = MyBeanUtils.copy(stockInImportDTO, ProStockRecord::new);
                                newStockRecord.setStockType(DictUtil.getValue(Constant.ProStockType.IMPORT_WAREHOUSE));
                                newStockRecord.setStockMode(DictUtil.getValue(Constant.ProStockMode.STOCK_IN));
                                newStockRecord.setGoodsId(goodsId);
                                newStockRecord.setParkOrgId(parkOrgId);
                                // 2、判断之前已读取数据里是否存在
                                // 3、判断原有系统缓存数据里是否存在
                                // 4、都无新增
                                if (newStockMap.containsKey(goodsId)) {
                                    newStockManager = newStockMap.get(goodsId);
                                } else if (oldStockMap.containsKey(goodsId)) {
                                    newStockManager = oldStockMap.get(goodsId);
                                } else {
                                    newStockManager = this.createNewStockManager(parkOrgId, goodsId);
                                }
                                // 5、开始计算
                                // 当前库存数量 = 库存数量+入库数量
                                newStockManager.setStockNum(newStockManager.getStockNum() + stockInImportDTO.getStockInNum());
                                // 当入库单价不为 0；进行 入库均价 字段更新
                                if (stockInImportDTO.getStockInUnitPrice() != null
                                        && !stockInImportDTO.getStockInUnitPrice().equals(BigDecimal.ZERO)) {
                                    // 累计入库数量 = 累计入库数量+新入库数量
                                    newStockManager.setTotalStockNum(
                                            newStockManager.getTotalStockNum() + stockInImportDTO.getStockInNum());
                                    // 累计入库总价 = 新入库数量*单价+累计入库总价
                                    newStockManager.setTotalPrice(stockInImportDTO.getStockInUnitPrice()
                                            .multiply(new BigDecimal(stockInImportDTO.getStockInNum()))
                                            .add(newStockManager.getTotalPrice()));
                                    // 入库均价 = 累计入库总价 / 累计入库数量
                                    newStockManager.setAveragePrice(divideBigDecimal(newStockManager));
                                } else {
                                    newStockRecord.setStockInUnitPrice(BigDecimal.ZERO);
                                }
                                newStockRecord.setStockId(newStockManager.getId());
                                newStockRecord.setCurrentNum(newStockManager.getStockNum());
                                newStockRecord.setCurrentAveragePrice(newStockManager.getAveragePrice());

                                //2022.03.22 v12 新增需求 增加备注字段
                                newStockRecord.setRemark(
                                        StringUtils.isEmpty(stockInImportDTO.getRemark()) ? "-" : stockInImportDTO.getRemark());
                                // 6、记录表数据处理完成
                                stockRecordList.add(newStockRecord);
                                // 7、新增库存表更新
                                EntityUserUtil.setAllDynamic(newStockManager);
                                newStockManager.setUpdateTime(LocalDateTime.now());
                                newStockMap.put(goodsId, newStockManager);
                            });
                            this.saveOrUpdateBatch(newStockMap.values().stream().collect(Collectors.toList()));
                            stockRecordService.saveRecordBatch(stockRecordList);

                        });
        ExcelImportUtils.importExcel(file, ProStockInImportDTO.class, importListener);
        return importListener.getImportResult();
    }

    private ProStockManager createNewStockManager(String parkOrgId, String goodsId) {
        ProStockManager proStockManager = new ProStockManager();
        proStockManager.setParkOrgId(parkOrgId);
        proStockManager.setGoodsId(goodsId);
        proStockManager.setTotalPrice(BigDecimal.ZERO);
        proStockManager.setTotalStockNum(Constant.ZERO);
        EntityUserUtil.setAllDynamic(proStockManager);
        return proStockManager;
    }

    /**
     * 封装查询条件
     *
     * @return 查询条件
     */
    private LambdaQueryWrapper<ProStockManager> getConditionWrapper(ProStockManagerQuery query) {
        LambdaQueryWrapper<ProStockManager> queryWrapper = new LambdaQueryWrapper<ProStockManager>();
        return queryWrapper;
    }

    /***
     * entity转DTO
     *
     * @return DTO集合
     */
    private List<ProStockManagerDTO> entitiesToDTOs(List<ProStockManager> entities) {
        return MyBeanUtils.copyList(entities, ProStockManagerDTO::new, (s, t) -> {

        });
    }
}
