package com.crlink.smartpark.customer.task;

import cn.hutool.core.collection.CollUtil;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.core.utils.DictUtil;
import com.crlink.smartpark.customer.common.Constant;
import com.crlink.smartpark.customer.entity.ProInventoryManager;
import com.crlink.smartpark.customer.entity.ProStockManager;
import com.crlink.smartpark.customer.entity.ProStockRecord;
import com.crlink.smartpark.customer.service.ProInventoryManagerService;
import com.crlink.smartpark.customer.service.ProStockManagerService;
import com.crlink.smartpark.customer.service.ProStockRecordService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName: InventoryStockAutoTask
 * @Package com.crlink.smartpark.customer.task
 * @Description: 资产管理-月度盘点-跑批
 * @Version 1.0
 */
@Service
@Slf4j
public class InventoryStockAutoTask {

    SimpleDateFormat formatData = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
    SimpleDateFormat formatMonth = new SimpleDateFormat(Constants.YEAR_MONTH_FORMAT);

    @Resource
    private ProStockRecordService stockRecordService;

    @Resource
    private ProStockManagerService stockManagerService;

    @Resource
    private ProInventoryManagerService inventoryManagerService;

    /**
     * 盘点管理 - 月度盘点 - 跑批更新物品表格 1、拉取昨天出入库记录 2、提取GoodsId 3、根据GoodsId 查询盘点表对应月份是否有对应数据 4、Set
     * 列表去重，有对应数据进行更新操作（每个物品对应最后一条数据为结果数据） 5、无对应数据的查询上月是否有对应数据，有依据上月数据进行新增字段填充，无对应上月字段为 '-' 6、生成新的月度统计数据，归入待处理统计
     * 7、待处理统计表依据出入库记录表，进行数据更新 8、执行批量写入操作
     *
     * @return void
     * @MethodName autoCancelReserve
     */
    public void autoInventoryStock() {
        StopWatch stopWatch = new StopWatch();
        log.info("月度盘点定时任务启动！");
        stopWatch.start();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        String today = formatData.format(calendar.getTime()); // 当前日期
        calendar.add(calendar.DATE, -1);
        String yesterday = formatData.format(calendar.getTime()); // 前一日
        String lastDayMonth = formatMonth.format(calendar.getTime()); // 上一日对应月份
        calendar.add(calendar.MONTH, -1); // 当前日期前一日 的上一个月
        String lastDayLastMonth = formatData.format(calendar.getTime()); // 上一日的上一个月

        List<ProStockManager> stockManagerList = stockManagerService.list();
        if (CollUtil.isEmpty(stockManagerList)) {
            log.info("月度盘点定时任务结束！库存无数据。");
            return;
        }
        // 库存数据列表 提取现有ID
        Set<String> stockGoodsIds =
                stockManagerList.stream().map(ProStockManager::getGoodsId).collect(Collectors.toSet());
        // 数据抽取，做新增初始数据使用
        Map<String, ProStockManager> stockMap =
                stockManagerList.stream().collect(Collectors.toMap(ProStockManager::getGoodsId, Function.identity()));

        // 拉取前一日的出入库数据
        List<ProStockRecord> recordList = stockRecordService.lambdaQuery()
                .like(ProStockRecord::getCreateTime, lastDayMonth).orderByDesc(ProStockRecord::getCreateTime).list();
        if (CollUtil.isEmpty(recordList)) {
            log.info("月度盘点定时任务结束！{}日无新增数据。", yesterday);
            return;
        }
        // 依据GoodsId 分类
        Map<String, List<ProStockRecord>> recordMap =
                recordList.stream().collect(Collectors.groupingBy(ProStockRecord::getGoodsId));
        // 抽取出入库记录 对应物品ID
        Set<String> recordGoodsIds = recordList.stream().map(ProStockRecord::getGoodsId).collect(Collectors.toSet());

        // 查询当月是否有对应数据
        List<ProInventoryManager> inventoryManagerList = inventoryManagerService.lambdaQuery()
                .eq(ProInventoryManager::getMonth, lastDayMonth).in(ProInventoryManager::getGoodsId, recordGoodsIds).list();
        // Map<String, ProInventoryManager> inventoryManagerMap = inventoryManagerList.stream()
        // .collect(Collectors.toMap(ProInventoryManager::getGoodsId, Function.identity()));

        // 拉取盘点表已有物品id
        Set<String> inventoryGoodsIds =
                inventoryManagerList.stream().map(ProInventoryManager::getGoodsId).collect(Collectors.toSet());

        // 剔除已有物品ID的数据，即为需要新增记录的数据(20200304修改包含无出入库记录数据)
        Set<String> needAddInventoryIds = Sets.newHashSet(stockGoodsIds);
        needAddInventoryIds.removeAll(inventoryGoodsIds);
        if (CollUtil.isNotEmpty(needAddInventoryIds)) {
            // 拉取待新增记录上一月份数据备用
            List<ProInventoryManager> inventoryManagerLastMonthList =
                    inventoryManagerService.lambdaQuery().eq(ProInventoryManager::getMonth, lastDayLastMonth)
                            .in(ProInventoryManager::getGoodsId, needAddInventoryIds).list();
            Map<String, ProInventoryManager> invLastMonthMap = inventoryManagerLastMonthList.stream()
                    .collect(Collectors.toMap(ProInventoryManager::getGoodsId, Function.identity()));

            // 新增数据处理
            needAddInventoryIds.forEach(goodsId -> {
                ProInventoryManager newInventoryManager = new ProInventoryManager();
                newInventoryManager.setGoodsId(goodsId);
                newInventoryManager.setMonth(lastDayMonth);
                ProInventoryManager lastMonthRecord = invLastMonthMap.get(goodsId);
                newInventoryManager
                        .setLastBalance(lastMonthRecord != null ? lastMonthRecord.getCurrentBalance().toString() : "-");
                newInventoryManager.setLastBalanceTotal(
                        lastMonthRecord != null ? lastMonthRecord.getLastBalanceTotal().toString() : "-");
                newInventoryManager.setCurrentStockInNumber(Constants.ZERO);
                newInventoryManager.setCurrentStockOutNumber(Constants.ZERO);

                // 新增数据写入库存默认值
                ProStockManager proStockManager = stockMap.get(goodsId);
                newInventoryManager.setCurrentBalance(proStockManager.getStockNum());
                newInventoryManager.setCurrentBalanceTotal(proStockManager.getAveragePrice()
                        .multiply(new BigDecimal(proStockManager.getStockNum())).setScale(2, BigDecimal.ROUND_HALF_UP));
                newInventoryManager.setParkOrgId(proStockManager.getParkOrgId());
                // 新造数据，归入统一处理
                inventoryManagerList.add(newInventoryManager);
            });
        }

        // 新增、修改数据处理
        List<ProInventoryManager> savaOrUpdateList = new ArrayList<>();

        // 所有数据处理
        inventoryManagerList.stream().forEach(e -> {
            List<ProStockRecord> records = recordMap.get(e.getGoodsId());
            //区分是否有出入库数据
            if (CollUtil.isNotEmpty(records)) {

                ProStockRecord lastRecord = records.get(0);
                // 当前最新库存数等于上一天最后一条库存数
                e.setCurrentBalance(lastRecord.getCurrentNum());
                // 当前最新库存总额 = 上一天最后一条库存数*上一天最后一条平均值
                e.setCurrentBalanceTotal(lastRecord.getCurrentAveragePrice()
                        .multiply(new BigDecimal(lastRecord.getCurrentNum())).setScale(2, BigDecimal.ROUND_HALF_UP));

                if (StringUtils.isEmpty(e.getId())) {
                    e.setParkOrgId(lastRecord.getParkOrgId());
                }

                AtomicReference<Integer> stockIn = new AtomicReference<>(0);
                records.forEach(record -> {
                    Integer stockMode = record.getStockMode();
                    stockIn.set(record.getStockInNum() + stockIn.get());
                    // 区分对应类型 累加
                    if (DictUtil.getValue(Constant.ProStockMode.STOCK_OUT).equals(stockMode)) {
                        e.setCurrentStockOutNumber(e.getCurrentStockOutNumber() + record.getStockInNum());
                    } else if (DictUtil.getValue(Constant.ProStockMode.STOCK_IN).equals(stockMode)) {
                        e.setCurrentStockInNumber(stockIn.get());
                    }

                });
                e.setInventoryTime(LocalDateTime.now());
            }
            savaOrUpdateList.add(e);
        });

        inventoryManagerService.saveOrUpdateBatch(savaOrUpdateList);
        stopWatch.stop();
        log.info("月度盘点定时任务结束！共调整 {} 条数据，耗时：{}。", savaOrUpdateList.size(), stopWatch.getTotalTimeSeconds());
    }

}
