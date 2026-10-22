package com.crlink.smartpark.common.service;

import com.crlink.smartpark.common.config.OssConfig;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@DependsOn("ossConfig")
public class MinioOssService implements IOssService, InitializingBean {
    @Autowired
    private OssConfig ossConfig;

    private MinioClient client;

    private String bucketName;


    @Override
    public void uploadFile(String key, MultipartFile file) {
        Map<String, String> headerMap = new HashMap<>();
        //headerMap.put("Content-Type", contentType);
        try (InputStream inputStream = file.getInputStream()) {
            client.putObject(bucketName, key, inputStream, null);
        } catch (Exception e) {
            log.error("【文件上传失败】key:{},fileOriginalName:{}", key, file.getOriginalFilename(), e);
            throw new ResolinkRuntimeException("10008");
        }
    }

    @Override
    public void uploadFileBytes(String key, byte[] bytes) {
        Map<String, String> headerMap = new HashMap<>();
        //headerMap.put("Content-Type", contentType);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            client.putObject(bucketName, key, inputStream, null);
        } catch (Exception e) {
            log.error("【文件上传失败】key:{},fileOriginalName:{}", key, e);
            throw new ResolinkRuntimeException("10008");
        }
    }


    @Override
    public InputStream downloadFile(String key) {
        try {
            return this.client.getObject(bucketName, key);
        } catch (Exception e) {
            log.error("【文件下载失败】key:{}", key);
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() {
        bucketName = ossConfig.getMinioConfig().getBucketName();
        String accessKey = ossConfig.getMinioConfig().getAccessKey();
        String endpoint = ossConfig.getMinioConfig().getEndpoint();
        String secretKey = ossConfig.getMinioConfig().getSecretKey();
        try {
            client = new MinioClient(endpoint, accessKey, secretKey);
        } catch (Exception e) {
            log.error("【create minio client error】", e);
        }
    }

    @Override
    public Boolean deleteFile(String key) {
        try {
            this.client.removeObject(bucketName, key);
            return Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResolinkRuntimeException("10010");
        }

    }

}
