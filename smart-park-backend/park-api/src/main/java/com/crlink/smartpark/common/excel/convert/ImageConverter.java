package com.crlink.smartpark.common.excel.convert;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.feign.FileProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ImageConverter implements Converter<String> {

    public static final String hint = "照片缺失";
    private static final Pattern pattern = Pattern.compile(Constants.REGEX_URL);

    private static FileProvider fileProvider;

    @Autowired
    public void setFileProvider(FileProvider fileProvider) {
        ImageConverter.fileProvider = fileProvider;
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
        if (StringUtils.isNotBlank(stringValue)) {
            String localFileParentFolder = ResourceUtils.getURL("classpath:").getPath();
            String pictPath = localFileParentFolder + "staffTemplate/" + stringValue;
            File file = new File(pictPath);
            String fileKey = "员工图片/" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.EXCEL_FILE_TIME_PATTERN)) + "_" + stringValue;
//                ThreadUtil.execAsync(() -> createFailData(fileKey, tempFile));
        }


        return null;
    }

    public void createFailData(String fileKey, File file) throws IOException {
        FileDTO dto = new FileDTO();
        dto.setBytes(FileUtils.readFileToByteArray(file));
        dto.setRelativePath(fileKey);
//        SpringContextUtils.getBean(FileProvider.class).fileUpload2oss(dto);
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return new CellData("-");
        if (StringUtils.isBlank(value)) {
            return new CellData("-");
        }
        if (pattern.matcher(value).matches()) {
            try {
                return new CellData(getFile(value));
            } catch (IOException e) {
                log.error("获取完整路径照片字节流异常", e);
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
        HttpRequest request = HttpUtil.createGet(urlStr);
        HttpResponse response = request.execute();
        if (response.getStatus() == 200) {
            return response.bodyBytes();
        }
        if (response.getStatus() == 302) {
            String newUrl = response.header("location");
            if (StrUtil.isBlank(newUrl)) {
                return null;
            }
            if (!newUrl.toLowerCase().startsWith("http")) {
                URL url = request.getConnection().getUrl();
                newUrl = url.getProtocol() + "://" + url.getHost() + newUrl;
            }
            return HttpUtil.createGet(newUrl).execute().bodyBytes();
        }
        return null;
        /*URL url = new URL(urlStr);
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
        }*/
    }

}
