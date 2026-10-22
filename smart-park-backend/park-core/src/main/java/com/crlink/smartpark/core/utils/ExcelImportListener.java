package com.crlink.smartpark.core.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.dto.FileImportErrorDTO;
import com.google.common.collect.Lists;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class ExcelImportListener<T, E> extends AnalysisEventListener<E> {
    private static final int BATCH_COUNT = 2000;

    private IService<T> iService;
    private Function<E, T> function;
    private Consumer<E> consumer;
    private FileImportErrorDTO fileImportErrorDTO;

    private List<T> list = Lists.newArrayList();

    private AtomicInteger successTotal = new AtomicInteger(0);
    private AtomicInteger failTotal = new AtomicInteger(0);
    private List<E> failList = Lists.newArrayList();

    public ExcelImportListener() {
    }

    public ExcelImportListener(IService<T> iService, Function<E, T> function) {
        this.iService = iService;
        this.function = function;
    }


    @Override
    public void invoke(E e, AnalysisContext context) {
        String errorMessage = ExcelImportValidUtil.validateValue(e);
        consumer.accept(e);
        T t = function.apply(e);
        if (Objects.nonNull(t)) {
            list.add(t);
        } else {
            failList.add(e);
            failTotal.getAndIncrement();
        }
        if (list.size() > BATCH_COUNT) {
            iService.saveOrUpdateBatch(list);
            successTotal.getAndAdd(list.size());
            list.clear();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        iService.saveOrUpdateBatch(list);
        successTotal.getAndAdd(list.size());
        downloadFailData(fileImportErrorDTO.getFile(), fileImportErrorDTO.getSheetName());
        //CompletableFuture.runAsync(() -> {
        //});

    }

    public void downloadFailData(File file, String sheetName) {
        ExcelExportUtils.exportToFile(failList, file, sheetName, null);
        failList.clear();
    }
}
