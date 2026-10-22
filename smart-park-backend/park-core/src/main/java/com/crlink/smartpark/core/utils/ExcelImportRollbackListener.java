package com.crlink.smartpark.core.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.dto.FileImportErrorInfoDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Data
public class ExcelImportRollbackListener<E> extends AnalysisEventListener<E> {

    //批量处理多条数据
    private Consumer<List<E>> afterReadDataOperation;
    //单行数据校验并处理
    private Function<E, String> dataCheckAndOpFunction;
    private List<E> successList = Lists.newArrayList();
    private List<List<String>> failList = Lists.newArrayList();
    private Collection<String> excludeHeads;
    private Class<E> dtoClass;
    private Integer totalColumn;
    private FileImportErrorInfoDTO fileImportResult;
    private static final Integer DEFAULT_DATA_START_ROW = 6;
    private Integer dataStartRow = DEFAULT_DATA_START_ROW;

    /**
     * @param dtoClass
     * @param dataCheckAndOpFunction 业务参数校验方法，null:校验通过,非null返回错误原因
     * @param afterReadDataOperation 获取完数据后的操作
     */
    public ExcelImportRollbackListener(Class<E> dtoClass, Function<E, String> dataCheckAndOpFunction, Consumer<List<E>> afterReadDataOperation) {
        this.afterReadDataOperation = afterReadDataOperation;
        this.dataCheckAndOpFunction = dataCheckAndOpFunction;
        this.dtoClass = dtoClass;
        this.totalColumn = MyReflectUtil.getTotalAnn(dtoClass, ExcelProperty.class);
    }


    public ExcelImportRollbackListener(Class<E> dtoClass, Function<E, String> dataCheckAndOpFunction) {
        this.dataCheckAndOpFunction = dataCheckAndOpFunction;
        this.dtoClass = dtoClass;
        this.totalColumn = MyReflectUtil.getTotalAnn(dtoClass, ExcelProperty.class);
    }

    public ExcelImportRollbackListener(Class<E> dtoClass) {
        this.dtoClass = dtoClass;
        this.totalColumn = MyReflectUtil.getTotalAnn(dtoClass, ExcelProperty.class);
    }

    public List<E> getResultData() {
        return successList;
    }

    public FileImportErrorInfoDTO getImportResult() {
        return this.fileImportResult;
    }

    public void setDataStartRow(Integer dataStartRow) {
        this.dataStartRow = dataStartRow;
    }

    @Override
    public void invoke(E e, AnalysisContext context) {
        if (!checkCurRowIsData(context)) {
            return;
        }
        //基本校验
        String validResult = ExcelImportValidUtil.validateValue(e);
        if (validResult != null) {
            failList.add(getCollValues(context.readRowHolder().getCellMap(), validResult));
            return;
        }
        //业务校验
        if (dataCheckAndOpFunction == null) {
            successList.add(e);
            return;
        }
        String failReason = dataCheckAndOpFunction.apply(e);
        if (failReason != null) {
            List<String> list = getCollValues(context.readRowHolder().getCellMap(), failReason);
            failList.add(list);
        } else {
            successList.add(e);
        }
    }

    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (afterReadDataOperation != null) {
            afterReadDataOperation.accept(this.successList);
        }
        fileImportResult = new FileImportErrorInfoDTO();
        fileImportResult.setSuccessTotal(successList.size());
        fileImportResult.setFailTotal(failList.size());
        if (fileImportResult.getFailTotal() > 0) {
            File tempFile = File.createTempFile("tmp", ".xlsx");
            String fileKey = "excel导入失败回显文件/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.EXCEL_FILE_TIME_PATTERN)) + "_" + RandomUtil.randomNumbers(3) + "_" + "报表导入失败数据" + ".xlsx";
            fileImportResult.setFileKey(fileKey);
            ThreadUtil.execAsync(() -> createFailData(fileKey, tempFile));
        }
    }

    public void createFailData(String fileKey, File file) {
        ExcelExportUtils.exportToFileWithHead(failList, file, "失败数据集合", MyReflectUtil.getExcelHeads(dtoClass, excludeHeads, "导入异常原因"));
        FileDTO dto = new FileDTO();
        dto.setBytes(FileUtil.file2bytes(file));
        dto.setRelativePath(fileKey);
        SpringContextUtils.getBean(FileProvider.class).fileUpload2oss(dto);
        failList.clear();
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) {
        if (!checkCurRowIsData(context)) {
            return;
        }
        log.error("导入数据异常", exception);
        Map<Integer, Cell> dataMap = context.readRowHolder().getCellMap();
        List<String> list = getCollValues(dataMap, getExMsg(exception));
        this.failList.add(list);
    }

    private String getExMsg(Throwable e) {
        List<String> errors = new ArrayList<>();
        while (e != null) {
            if (e instanceof ResolinkRuntimeException) {
                return ((ResolinkRuntimeException) e).getRealMessage().getDisplayText();
            }
            errors.add(e.getMessage());
            e = e.getCause();
        }
        return StrUtil.join("\n", errors);
    }

    private List<String> getCollValues(Map<Integer, Cell> dataMap, String failReason) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<Integer, Cell> entry : dataMap.entrySet()) {
            int index = entry.getKey();
            String value = entry.getValue().toString();
            while (list.size() < index) {
                list.add(StrUtil.EMPTY);
            }
            list.add(value);
        }
        while (list.size() < this.totalColumn) {
            list.add(StrUtil.EMPTY);
        }
        list.add(failReason);
        return list;
    }

    private boolean checkCurRowIsData(AnalysisContext context) {
        return context.readRowHolder().getRowIndex() + 1 >= dataStartRow;
    }
}
