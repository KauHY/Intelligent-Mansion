package com.crlink.smartpark.visitor.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 停车场
 * */
@Data
@ApiModel(value = "停车场保存模型")
public class ParkingLotSaveDTO implements Serializable {

	private static final long serialVersionUID = 8733530137729815359L;

	private String id;

	/**
	 * 停车场编码
	 */
	private String parkingLotCode;
	/**
	 * 停车场名称
	 */
	private String parkingLot;

	/**
	 * 总车位数
	 */
	private Integer totalLot;
	/**
	 * 使用了的车位数
	 */
	private Integer usedLot;
	/**
	 * 	余位数
	 */
	private Integer idleLot;
	/**
	 * 已停车数
	 */
	private Integer parkingNum;
	/**
	 * 绑定车场的车辆数
	 */
	private Integer carNum;

	/**
	 * 园区ID
	 */
	private String parkOrgId;
	private String parkOrgName;

	private List<CarLaneSaveDTO> carLaneList;

}
