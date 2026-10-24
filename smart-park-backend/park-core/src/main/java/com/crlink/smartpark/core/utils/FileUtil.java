package com.crlink.smartpark.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;

@Slf4j
public class FileUtil {

    public static byte[] file2bytes(File file) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            FileUtils.copyFile(file, outputStream);
            byte[] bytes = outputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            log.error("IO流异常", e);
        }
        return new byte[0];
    }

    public static MultipartFile file2MultiPartFile(File file) {
        FileItem fileItem = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
        try (InputStream input = new FileInputStream(file); OutputStream os = fileItem.getOutputStream()) {
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }
        MultipartFile multiFile = new CommonsMultipartFile(fileItem);
        return multiFile;
    }
}
