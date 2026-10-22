package com.crlink.smartpark.visitor.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 停车场占用率
 *
 */
@Data
public class ParkingOccupationRatioVO implements Serializable {
    private static final long serialVersionUID = -1651247657560691450L;

    private String parkingName;
    private long stopCount;
    private Integer parkCount;
}
