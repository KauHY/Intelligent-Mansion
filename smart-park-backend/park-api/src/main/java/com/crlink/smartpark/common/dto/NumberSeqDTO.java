package com.crlink.smartpark.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NumberSeqDTO implements Serializable {
    private String numberName;

    private LocalDateTime dateTime;

    private Integer value;
}
