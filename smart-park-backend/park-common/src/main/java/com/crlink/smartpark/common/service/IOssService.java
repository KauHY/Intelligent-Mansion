package com.crlink.smartpark.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface IOssService {
    void uploadFile(String key, MultipartFile file);


    void uploadFileBytes(String key, byte[] bytes);

    InputStream downloadFile(String key);

    Boolean deleteFile(String key) throws Exception;
}
