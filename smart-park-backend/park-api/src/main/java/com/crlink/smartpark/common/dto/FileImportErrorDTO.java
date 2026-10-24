package com.crlink.smartpark.common.dto;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
public class FileImportErrorDTO implements Serializable {
    private String sheetName;
    private File file;

    public FileImportErrorDTO() {
    }

    public FileImportErrorDTO(String sheetName, File file) {
        this.sheetName = sheetName;
        this.file = file;
    }
}
