package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 停车场
 * */
@Data
@TableName("vis_parking_lot")
public class ParkingLot implements Serializable{
	private static final long serialVersionUID = 1221111274573979470L;

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

	/**
	 * 租户ID
	 */
	private String tenantId;

	/**
	 * 访客通行：0-不适应，1-适用
	 */
	private Integer allowVisitor;

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
