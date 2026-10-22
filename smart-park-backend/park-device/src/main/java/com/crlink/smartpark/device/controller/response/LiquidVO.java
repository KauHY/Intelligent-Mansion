package com.crlink.smartpark.device.controller.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 液位VO
 *
 */
@Data
public class LiquidVO implements Serializable {
    private static final long serialVersionUID = -2978011545526053660L;

    private String deviceId;
    private String value;
    private Date valueTime;
    private String position;

}
