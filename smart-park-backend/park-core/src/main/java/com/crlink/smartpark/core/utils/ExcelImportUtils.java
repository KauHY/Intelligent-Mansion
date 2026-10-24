package com.crlink.smartpark.core.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.AbstractReadHolder;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class ExcelImportUtils {

    public static <T, E> void importExcel(File file, Class<E> headClass, ReadListener excelListener) {
        EasyExcel.read(file, headClass, excelListener).doReadAll();
    }

    public static <T, E> void importExcel(InputStream inputStream, Class<E> headClass, ReadListener excelListener) {
        EasyExcel.read(inputStream, headClass, excelListener).doReadAll();
    }

    public static <T, E> void importExcel(MultipartFile file, Class<E> headClass, ReadListener excelListener) {
        String filename = file.getOriginalFilename();
        if (!(filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv"))) {
            throw new ResolinkRuntimeException("10014");
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, headClass, excelListener).doReadAll();
        } catch (IOException e) {
            log.error("文件导入失败！", e);
            throw new ResolinkRuntimeException("10013");
        }
    }


    public static <T, E> void importExcel(MultipartFile file, Class<E> headClass, ExcelImportFirstListener firstListener, ReadListener excelListener) {
        String filename = file.getOriginalFilename();
        if (!(filename.endsWith(".xls") || filename.endsWith(".xlsx") || filename.endsWith(".csv"))) {
            throw new ResolinkRuntimeException("10014");
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
            ExcelReader reader = EasyExcel.read(inputStream, headClass, excelListener).build();
            AnalysisContext analysisContext = reader.analysisContext();
            AbstractReadHolder holder = (AbstractReadHolder) analysisContext.currentReadHolder();
            List<ReadListener> readListeners = Lists.newArrayList(firstListener);
            readListeners.addAll(analysisContext.currentReadHolder().readListenerList());
            holder.setReadListenerList(readListeners);
            reader.readAll();
        } catch (IOException e) {
            log.error("文件导入失败！", e);
            throw new ResolinkRuntimeException("10013");
        }
    }

}
