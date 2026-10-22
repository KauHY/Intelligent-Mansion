package com.crlink.smartpark.visitor.dto;

import com.crlink.smartpark.visitor.vo.ParkingLotVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 车场车辆DTO（同步大华使用）
 *
 */
@Data
public class ParkCarDTO implements Serializable {
    private static final long serialVersionUID = 6103509007623081567L;

    private String id;

    private String carCode;

    /**
     * 车牌号
     */
    private String carNum;

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    private Integer parkType;

    /**
     * 车辆品牌:0未识别
     */
    private Integer carBrand;
    /**
     * 车辆类型:0未识别
     */
    private Integer carType;
    /**
     * 车辆颜色:100其他
     */
    private Integer carColor;
    /**
     * 车牌颜色:100其他
     */
    private Integer carNumColor;
    /**
     * 黑白名单:0普通,2黑名单
     */
    private Integer blackList;

    /**
     * 授权开始时间
     */
    private LocalDateTime authStartTime;

    /**
     * 授权结束时间
     */
    private LocalDateTime authEndTime;

    /**
     * 园区ID
     */
    private String parkOrgId;

    /**
     * 企业ID
     */
    private String companyId;

    private String employeeId;

    /**
     * 设备人员ID
     */
    private Long devicePersonId;

    /**
     * 状态：0-未生效；1-已生效；2-已失效
     */
    private Integer status;

    /**
     * 车场codes
     */
    private Set<String> parkingLotCodes;

    private List<ParkingLotVO> parkingLots;

}
