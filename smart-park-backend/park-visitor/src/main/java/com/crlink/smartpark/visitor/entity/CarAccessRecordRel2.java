package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 车辆通行抓拍记录关联关系
 * */
@Data
@TableName("vis_car_access_record_rel")
public class CarAccessRecordRel2 implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;

	/**
	 * 车牌号
	 */
	private String carNum;

	/**
	 * 驶入记录ID
	 */
	private String enterCarAccessRecordId;

	/**
	 * 驶出记录ID
	 */
	private String exitCarAccessRecordId;

	/**
	 * 组织园区ID
	 */
	private String parkOrgId;

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
