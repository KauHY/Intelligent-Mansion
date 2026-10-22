package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 车道
 * */
@Data
@ApiModel(value = "车道")
public class CarLaneSaveDTO implements Serializable {
	private static final long serialVersionUID = 9082704070799160866L;
	/**
	 * 道闸通道ID
	 */
	private String id;
	/**
	 * 道闸通道名称
	 */
	private String channelName;
	/**
	 * 行驶方向
	 */
	private Integer carDirect;
	private String carDirectStr;
	/**
	 * 是否在线
	 */
	private Integer isOnline;
	private String isOnlineStr;
	/**
	 * 设备IP
	 */
	private String deviceIp;
	/**
	 * 设备端口
	 */
	private Integer devicePort;

	/**
	 * 园区ID
	 */
	private String parkOrgId;
	private String parkOrgName;

	/**
	 * 停车场ID
	 */
	private String parkingLotId;

	/**
	 * 停车场名称
	 */
	private String parkingLot;

}
