package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车道
 * */
@Data
@TableName("vis_car_lane")
public class CarLane implements Serializable{
	private static final long serialVersionUID = -8828883980404506619L;

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


	private String tenantId;

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

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 修改人
	 */
	private String updateBy;

	/**
	 * 删除状态
	 */
	private Boolean deleted;

}
