package com.crlink.smartpark.visitor.model.vo;

import lombok.Data;

/**
 * 车辆通行进出分组VO
 *
 */
@Data
public class CarAccessGroupVO {

    private Integer gatewayType;
    private Long accessCount;
}
