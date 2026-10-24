package com.crlink.smartpark.common.config;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ConfigurationProperties(prefix = "file.upload")
@Component
public class OssConfig implements InitializingBean {

    public static List<String> IMAGE_SUFFIX = Lists.newArrayList("jpg", "jpeg", "png", "bmp", "gif");


    @Getter
    private static List<String> fileSuffixList;

    @NestedConfigurationProperty
    private minioConfig minioConfig;

    @NestedConfigurationProperty
    private fileConfig fileConfig;


    public static class minioConfig {
        private String accessKey;
        private String secretKey;
        private String bucketName;
        private String endpoint;

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    @Component
    public static class fileConfig {
        private String fileSuffix;
        private String fileHeader;

        public String getFileSuffix() {
            return fileSuffix;
        }

        public void setFileSuffix(String fileSuffix) {
            this.fileSuffix = fileSuffix;
        }

        public String getFileHeader() {
            return fileHeader;
        }

        public void setFileHeader(String fileHeader) {
            this.fileHeader = fileHeader;
        }

    }

    public OssConfig.minioConfig getMinioConfig() {
        return minioConfig;
    }

    public void setMinioConfig(OssConfig.minioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    public OssConfig.fileConfig getFileConfig() {
        return fileConfig;
    }

    public void setFileConfig(OssConfig.fileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        fileSuffixList = Stream.of(fileConfig.getFileSuffix().split(",")).collect(Collectors.toList());
    }
}
