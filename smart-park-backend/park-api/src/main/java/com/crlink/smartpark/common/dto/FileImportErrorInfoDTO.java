package com.crlink.smartpark.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileImportErrorInfoDTO implements Serializable {
    private Integer successTotal;
    private Integer failTotal;
    private String fileKey;
}
