package com.crlink.smartpark.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.crlink.smartpark.common.constants.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @description: excel导出工具
 */
@Slf4j
public class ExcelExportUtils {

    /**
     * 默认文件名
     */
    private static final String DEFAULT_FILE_NAME = "数据导出";

    /**
     * 默认单页查询数据量
     */
    private static final Integer MAX_PAGE_SIZE = 10000;

    public static <T> void export(List<T> list) {
        export(list, DEFAULT_FILE_NAME);
    }

    public static <T> void export(List<T> list, String fileName) {
        export(list, fileName, fileName);
    }

    public static <T> void export(List<T> list, String fileName, String sheetName) {
        export(list, fileName, sheetName, getHorizontalCellStyleStrategy());
    }

    public static <T> void export(List<T> list, String fileName, HorizontalCellStyleStrategy horizontalCellStyleStrategy) {
        export(list, fileName, fileName, horizontalCellStyleStrategy);
    }

    public static <T> void export(List<T> list, String fileName, CustomCellWriteHandler customCellWriteHandler) {
        export(list, fileName, fileName, customCellWriteHandler);
    }

    public static <T> void export(List<T> list, String fileName, Class clazz, Collection<String> includeColumnFiledNames) {
        try {
            ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet().sheetName(fileName);
            if (CollectionUtil.isNotEmpty(includeColumnFiledNames)) {
                sheetBuilder.includeColumnFiledNames(includeColumnFiledNames);
            }
            HttpServletResponse response = buildResponse(fileName);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
                    .build();
            excelWriter.write(list, sheetBuilder.build()).finish();
        } catch (IOException e) {
            log.error("导出异常", e);
        }
    }

    public static <T> void export(List<T> list, String fileName, String sheetName, List<List<String>> head) {
        try {
            HttpServletResponse response = buildResponse(fileName);
            EasyExcel.write(response.getOutputStream()).sheet(sheetName)
                    .head(head)
                    .doWrite(list);
        } catch (IOException e) {
            log.error("导出异常", e);
        }
    }

    public static <T> void export(List<T> list, String fileName, String sheetName, HorizontalCellStyleStrategy horizontalCellStyleStrategy) {
        try {
            HttpServletResponse response = buildResponse(fileName);
            Class clazz = CollUtil.isEmpty(list) ? null : list.get(0).getClass();
            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .doWrite(list);
        } catch (IOException e) {
            log.error("导出异常", e);
        }
    }

    public static <T> void export(List<T> list, String fileName, String sheetName, CustomCellWriteHandler customCellWriteHandler) {
        try {
            HttpServletResponse response = buildResponse(fileName);
            Class clazz = CollUtil.isEmpty(list) ? null : list.get(0).getClass();

            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName)
                    .registerWriteHandler(customCellWriteHandler)
                    .doWrite(list);
        } catch (IOException e) {
            log.error("导出异常", e);
        }
    }

    public static <T> void exportToFileWithHead(List<T> list, File file, String sheetName, List<List<String>> head) {
        EasyExcel.write(file).sheet(sheetName)
                .head(head)
                .doWrite(list);
    }

    public static <T> void exportToFileWithClass(List<T> list, File file, String sheetName, Class clazz) {
        EasyExcel.write(file, clazz).sheet(sheetName)
                .doWrite(list);
    }

    public static <T> void exportToFile(List<T> list, File file, String sheetName, HorizontalCellStyleStrategy horizontalCellStyleStrategy) {
        Class clazz = CollUtil.isEmpty(list) ? null : list.get(0).getClass();
        EasyExcel.write(file, clazz).sheet(sheetName)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .doWrite(list);
    }

    public static <S, T> void export(Supplier<List<S>> dbGetOnePage, Class clazz) {
        export(dbGetOnePage, null, clazz, DEFAULT_FILE_NAME, null, getHorizontalCellStyleStrategy());
    }

    public static <S, T> void export(Supplier<List<S>> dbGetOnePage, Function<List<S>, List<T>> excelBeanConverter, Class clazz) {
        export(dbGetOnePage, excelBeanConverter, clazz, DEFAULT_FILE_NAME, null, getHorizontalCellStyleStrategy());
    }

    public static <S, T> void export(Supplier<List<S>> dbGetOnePage, String fileName, Class clazz) {
        export(dbGetOnePage, null, clazz, fileName, null, getHorizontalCellStyleStrategy());
    }

    public static <S, T> void export(Supplier<List<S>> dbGetOnePage, Function<List<S>, List<T>> excelBeanConverter, String fileName, Class clazz) {
        export(dbGetOnePage, excelBeanConverter, clazz, fileName, null, getHorizontalCellStyleStrategy());
    }

    public static <S, T> void export(Supplier<List<S>> dbGetOnePage, Function<List<S>, List<T>> excelBeanConverter, String fileName, Class clazz, Collection<String> excludeColumnFiledNames) {
        export(dbGetOnePage, excelBeanConverter, clazz, fileName, excludeColumnFiledNames, getHorizontalCellStyleStrategy());
    }

    /**
     * @param dbGetOnePage                必须是数据库查询list
     * @param excelBeanConverter          数据库bean转换为excel导出bean
     * @param clazz                       excel导出bean class
     * @param fileName                    文件名
     * @param horizontalCellStyleStrategy excel样式
     * @param <S>
     * @param <T>
     */
    public static <S, T> void export(Supplier<List<S>> dbGetOnePage, Function<List<S>, List<T>> excelBeanConverter, Class clazz, String fileName, Collection<String> excludeColumnFiledNames, HorizontalCellStyleStrategy horizontalCellStyleStrategy) {
        ExcelWriter excelWriter = null;
        try {
            HttpServletResponse response = buildResponse(fileName);
            excelWriter = EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .build();
            int pageNo = 1;
            while (true) {
                PageHelper.startPage(pageNo, MAX_PAGE_SIZE);
                List dbList = dbGetOnePage.get();
                PageInfo pageInfo = new PageInfo<>(dbList);
                ExcelWriterSheetBuilder builder = EasyExcel.writerSheet();
                if (CollectionUtil.isNotEmpty(excludeColumnFiledNames)) {
                    builder.excludeColumnFiledNames(excludeColumnFiledNames);
                }
                WriteSheet sheet = builder.sheetName(getPageText(pageNo, dbList.size())).build();
                excelWriter.write(excelBeanConverter != null ? excelBeanConverter.apply(dbList) : dbList, sheet);
                if (!pageInfo.isHasNextPage()) {
                    break;
                }
                pageNo++;
            }
        } catch (IOException e) {
            log.error("导出异常", e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private static String getPageText(Integer pageNo, int dataSize) {
        if (dataSize <= 0) {
            return "无数据";
        }
        int firstNo = (pageNo - 1) * MAX_PAGE_SIZE + 1;
        int lastNo = firstNo + Math.min(MAX_PAGE_SIZE, dataSize) - 1;
        return firstNo + "-" + lastNo;
    }

    @NotNull
    private static HttpServletResponse buildResponse(String fileName) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + buildFileName(fileName) + ".xlsx");
        return response;
    }

    @NotNull
    private static String buildFileName(String fileName) {
        try {
            fileName = URLEncoder.encode(fileName + LocalDateTime.now().format(Constants.DATE_FORMATTER), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("文件名编码异常", e);
        }
        return fileName;
    }

    /**
     * 配置excel导出内容格式
     *
     * @return
     */
    private static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }


}
