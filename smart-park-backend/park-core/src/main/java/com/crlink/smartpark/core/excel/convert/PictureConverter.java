package com.crlink.smartpark.core.excel.convert;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.core.utils.SpringContextUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.crlink.smartpark.common.constants.Constants.EMPLOYEE_FACE_PIC_DIR;

@Slf4j
@Component
public class PictureConverter implements Converter<String> {

    public static final String hint = "照片缺失";
    private static final Pattern pattern = Pattern.compile(Constants.REGEX_URL);

    private static FileProvider fileProvider;

    @Autowired
    public void setFileProvider(FileProvider fileProvider) {
        PictureConverter.fileProvider = fileProvider;
    }

    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        String fileKey = null;
        if (StringUtils.isNotBlank(stringValue)) {
            String localFileParentFolder = FileUtils.getTempDirectory().getAbsolutePath();
            String pictPath = localFileParentFolder + File.separator + EMPLOYEE_FACE_PIC_DIR + File.separator + stringValue;
            log.info("人脸图片:{}", pictPath);
            File file = new File(pictPath);
            fileKey = "employee_face/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.EXCEL_FILE_TIME_PATTERN)) + "_" + stringValue;
            String finalFileKey = fileKey;
            ThreadUtil.execAsync(() -> {
                createFileData(finalFileKey, file);
            });
        }
        return fileKey;
    }

    @SneakyThrows
    public void createFileData(String fileKey, File file) {
        FileDTO dto = new FileDTO();
        dto.setBytes(FileUtils.readFileToByteArray(file));
        dto.setRelativePath(fileKey);
        dto.setFileOriginalName(file.getName());
        SpringContextUtils.getBean(FileProvider.class).feignUploadBytes(dto);
        log.info("文件上传成功,fileKey:{},file:{}", file.getName());
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (StringUtils.isBlank(value)) {
            return new CellData("-");
        }
        if (pattern.matcher(value).matches()) {
            try {
                return new CellData(getFile(value));
            } catch (IOException e) {
                return new CellData(hint);
            }
        }

        byte[] bytes = fileProvider.downloadByte(value);
        if (Objects.isNull(bytes) || bytes.length <= 0) {
            return new CellData(hint);
        }
        return new CellData(bytes);
    }

    /**
     * 获取 文件 流
     *
     * @param urlStr
     * @return
     * @throws IOException
     */
    private byte[] getFile(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        InputStream inStream = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            connection.setConnectTimeout(15000);
            // 设置读取超时时间
            connection.setReadTimeout(15000);
            // 设置请求参数，即具体的 HTTP 方法
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Connection", "Keep-Alive");

            inStream = connection.getInputStream();    //通过输入流获取图片数据
            byte[] data = IOUtils.toByteArray(inStream);
            return data;
        } finally {
            if (inStream != null) {
                try {
                    IOUtils.closeQuietly(inStream);
                } catch (Exception e) {
                    log.error("关闭流失败", e);
                }

            }
        }
    }
}
