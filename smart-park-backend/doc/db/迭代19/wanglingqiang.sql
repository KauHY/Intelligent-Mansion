-- 新增定时任务2022-08-04
INSERT INTO `smart-park`.`com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('visitorRecordTask', '59 59 23 * * ?', '访客记录刷新', '每天23:59:59执行一次', 0, NULL, NULL, NULL, 'park-visitor', 'visitorRecordTask', 'refreshSignOutStatus');

-- 更新访客登记类型2022-08-18
DELETE FROM sys_dictionary WHERE type='REGISTER_TYPE';
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('REGISTER_TYPE_1', NULL, 'REGISTER_TYPE', '登记类型', 'ONSITE_REG', '现场登记', 1, 1, NOW(), NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('REGISTER_TYPE_3', NULL, 'REGISTER_TYPE', '登记类型', 'DOOR_ACCESS', '门禁核验', 3, 3, NOW(), NOW(), 0);

-- 更新车场表2022-08-19
DROP TABLE IF EXISTS `vis_parking_lot`;
CREATE TABLE `vis_parking_lot` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `parking_lot_code` varchar(30)  DEFAULT NULL COMMENT '停车场编码',
  `parking_lot` varchar(50)  DEFAULT NULL COMMENT '停车场名称',
  `total_lot` int DEFAULT NULL COMMENT '总车位数',
  `used_lot` int DEFAULT NULL COMMENT '使用了的车位数',
  `idle_lot` int DEFAULT NULL COMMENT '余位数',
  `parking_num` int DEFAULT NULL COMMENT '已停车数',
  `car_num` int DEFAULT NULL COMMENT '绑定车场的车辆数',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `park_org_id` varchar(64)  DEFAULT NULL COMMENT '园区ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64)  DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='停车场';

-- 更新车道表2022-08-19
DROP TABLE IF EXISTS `vis_car_lane`;
CREATE TABLE `vis_car_lane` (
	`id` VARCHAR ( 64 ) NOT NULL COMMENT '主键',
	`channel_name` VARCHAR ( 50 ) NOT NULL COMMENT '车道名',
	`car_direct` TINYINT ( 1 ) NOT NULL COMMENT '行驶方向',
	`car_direct_str` VARCHAR ( 10 ) NOT NULL COMMENT '行驶方向',
	`device_ip` VARCHAR ( 30 ) NOT NULL COMMENT '设备IP',
	`device_port` INT NOT NULL COMMENT '设备端口',
	`is_online` TINYINT ( 1 ) NOT NULL COMMENT '是否在线',
	`is_online_str` VARCHAR ( 10 ) NOT NULL COMMENT '是否在线',
	`tenant_id` VARCHAR ( 64 ) DEFAULT NULL COMMENT '租户ID',
	`park_org_id` VARCHAR ( 64 ) DEFAULT NULL COMMENT '园区ID',
	`parking_lot_id` VARCHAR ( 64 ) NOT NULL COMMENT '停车场ID',
	`deleted` TINYINT ( 1 ) NOT NULL DEFAULT '0' COMMENT '删除状态',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	`create_by` VARCHAR ( 64 ) DEFAULT NULL COMMENT '创建人',
	`update_by` VARCHAR ( 64 ) DEFAULT NULL COMMENT '修改人',
PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB COMMENT='车道';

-- 新增停车类型2022-08-22
DELETE FROM sys_dictionary WHERE type='PARKING_TYPE';
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`) VALUES ('PARKING_TYPE_1', NULL, 'PARKING_TYPE', '停车类型', 'EMPLOYEE_CAR', '员工车辆', 1, 1, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `deleted`) VALUES ('PARKING_TYPE_2', NULL, 'PARKING_TYPE', '停车类型', 'COMPANY_CAR', '公务车辆', 2, 3, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`) VALUES ('PARKING_TYPE_3', NULL, 'PARKING_TYPE', '停车类型', 'OUTSIDE_CAR', '外部车辆', 3, 3, NOW(), 0);



-- 更新车场表2022-08-23
DROP TABLE IF EXISTS `vis_parking_lot`;
CREATE TABLE `vis_parking_lot` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `parking_lot_code` varchar(30)  DEFAULT NULL COMMENT '停车场编码',
  `parking_lot` varchar(50)  DEFAULT NULL COMMENT '停车场名称',
  `total_lot` int DEFAULT NULL COMMENT '总车位数',
  `used_lot` int DEFAULT NULL COMMENT '使用了的车位数',
  `idle_lot` int DEFAULT NULL COMMENT '余位数',
  `parking_num` int DEFAULT NULL COMMENT '已停车数',
  `car_num` int DEFAULT NULL COMMENT '绑定车场的车辆数',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `park_org_id` varchar(64)  DEFAULT NULL COMMENT '园区ID',
	`allow_visitor` tinyint(1) DEFAULT NULL COMMENT '访客通行：0不适应，1适用',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64)  DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='停车场';

-- 更新车辆表结构2022-08-26
DROP TABLE IF EXISTS `vis_car`;
CREATE TABLE `vis_car` (
	`id` VARCHAR ( 64 ) NOT NULL COMMENT '主键',
	`car_code` VARCHAR ( 50 ) NOT NULL COMMENT '车牌编码',
	`car_num` VARCHAR ( 50 ) NOT NULL COMMENT '车牌号',
	`park_type` TINYINT NOT NULL COMMENT '停车类型：1员工车辆,2公务车辆,3外部车辆',
	`park_org_id` VARCHAR ( 64 ) DEFAULT NULL COMMENT '园区ID',
	`company_id` VARCHAR ( 64 ) DEFAULT NULL COMMENT '企业ID',
	`employee_id` VARCHAR ( 64 ) NOT NULL COMMENT '关联员工Id',
	`car_owner_name` VARCHAR(20)  NOT NULL COMMENT '员工姓名',
	`mobile` VARCHAR(11)  NOT NULL COMMENT '手机号',
	`parking_lot_ids` VARCHAR(200) NOT NULL COMMENT '停车场ID',
	`car_brand` TINYINT DEFAULT 0 COMMENT '车辆品牌:0未识别',
	`car_type` TINYINT DEFAULT 0 COMMENT '车辆类型:0未识别',
	`car_color` TINYINT DEFAULT 100 COMMENT '车辆颜色:100其他',
	`car_num_color` TINYINT DEFAULT 100 COMMENT '车牌颜色:100其他',
	`black_list` TINYINT DEFAULT 0 COMMENT '黑白名单:0普通,2黑名单',
	`status` TINYINT DEFAULT NULL COMMENT '状态：0-未生效；1-已生效；2-已失效',
	`auth_start_time` DATETIME DEFAULT NULL COMMENT '授权开始时间',
	`auth_end_time` DATETIME DEFAULT NULL COMMENT '授权结束时间',
	`deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '删除状态',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	`create_by` VARCHAR ( 64 ) DEFAULT NULL COMMENT '创建人',
	`update_by` VARCHAR ( 64 ) DEFAULT NULL COMMENT '修改人',
PRIMARY KEY ( `id` ) USING BTREE
) ENGINE = INNODB COMMENT='车辆信息';

-- 新增停车类型2022-08-26
DELETE FROM sys_dictionary WHERE type='PARKING_TYPE';
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`) VALUES ('PARKING_TYPE_1', NULL, 'PARKING_TYPE', '停车类型', 'EMPLOYEE_CAR', '员工车辆', 1, 1, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `deleted`) VALUES ('PARKING_TYPE_2', NULL, 'PARKING_TYPE', '停车类型', 'OFFICIAL_CAR', '公务车辆', 2, 3, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`) VALUES ('PARKING_TYPE_3', NULL, 'PARKING_TYPE', '停车类型', 'EXTERNAL_CAR', '外部车辆', 3, 3, NOW(), 0);

-- 新增定时任务2022-08-29
INSERT INTO `smart-park`.`com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('deviceStatusTask', '0 0/5 * * * ?', '更新设备状态', '每5分钟执行一次', 0, NULL, NULL, NULL, 'park-device', 'deviceStatusTask', 'updateDeviceStatus');

-- 2022-09-06更新
DROP TABLE IF EXISTS `vis_parking_lot`;
CREATE TABLE `vis_parking_lot` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `parking_lot_code` VARCHAR(30)  DEFAULT NULL COMMENT '停车场编码',
  `parking_lot` VARCHAR(50)  DEFAULT NULL COMMENT '停车场名称',
  `total_lot` INT DEFAULT NULL COMMENT '总车位数',
  `used_lot` INT DEFAULT NULL COMMENT '使用了的车位数',
  `idle_lot` INT DEFAULT NULL COMMENT '余位数',
  `parking_num` INT DEFAULT NULL COMMENT '已停车数',
  `car_num` INT DEFAULT NULL COMMENT '绑定车场的车辆数',
  `tenant_id` VARCHAR(64) DEFAULT NULL COMMENT '租户ID',
  `park_org_id` VARCHAR(64)  DEFAULT NULL COMMENT '园区ID',
  `park_org_name` VARCHAR(50)  DEFAULT NULL COMMENT '园区名称',
  `allow_visitor` TINYINT DEFAULT NULL COMMENT '访客通行：0不适应，1适用',
  `deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(64)  DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='停车场';

-- 2022-09-06更新
DROP TABLE IF EXISTS `vis_car_lane`;
CREATE TABLE `vis_car_lane` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `channel_name` VARCHAR(50) NOT NULL COMMENT '车道名',
  `car_direct` TINYINT NOT NULL COMMENT '行驶方向',
  `car_direct_str` VARCHAR(10) NOT NULL COMMENT '行驶方向',
  `device_ip` VARCHAR(30) NOT NULL COMMENT '设备IP',
  `device_port` INT NOT NULL COMMENT '设备端口',
  `is_online` TINYINT NOT NULL COMMENT '是否在线',
  `is_online_str` VARCHAR(10) NOT NULL COMMENT '是否在线',
  `tenant_id` VARCHAR(64) DEFAULT NULL COMMENT '租户ID',
  `park_org_id` VARCHAR(64) DEFAULT NULL COMMENT '园区ID',
  `park_org_name` VARCHAR(50)  DEFAULT NULL COMMENT '园区名称',
  `parking_lot_id` VARCHAR(64) NOT NULL COMMENT '停车场ID',
  `parking_lot` VARCHAR(50) NOT NULL COMMENT '停车场名',
  `deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='车道';


-- 更新车辆表结构2022-09-09
DROP TABLE IF EXISTS `vis_car`;
CREATE TABLE `vis_car` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `car_code` varchar(50) NOT NULL COMMENT '车牌编码',
  `car_num` varchar(50) NOT NULL COMMENT '车牌号',
  `park_type` tinyint NOT NULL COMMENT '停车类型：1员工车辆,2公务车辆,3外部车辆',
  `park_org_id` varchar(64)  NOT NULL COMMENT '园区ID',
  `company_id` varchar(64) DEFAULT NULL COMMENT '企业ID',
  `employee_id` varchar(64)  DEFAULT NULL COMMENT '关联员工Id',
  `car_owner_name` varchar(20)  DEFAULT NULL COMMENT '员工姓名',
  `mobile` varchar(11)  DEFAULT NULL COMMENT '手机号',
  `parking_lot_ids` varchar(200) NOT NULL COMMENT '停车场ID',
  `car_brand` tinyint DEFAULT '0' COMMENT '车辆品牌:0未识别',
  `car_type` tinyint DEFAULT '0' COMMENT '车辆类型:0未识别',
  `car_color` tinyint DEFAULT '100' COMMENT '车辆颜色:100其他',
  `car_num_color` tinyint DEFAULT '100' COMMENT '车牌颜色:100其他',
  `black_list` tinyint DEFAULT '0' COMMENT '黑白名单:0普通,2黑名单',
  `status` tinyint DEFAULT NULL COMMENT '状态：0-未生效；1-已生效；2-已失效',
  `auth_start_time` datetime DEFAULT NULL COMMENT '授权开始时间',
  `auth_end_time` datetime DEFAULT NULL COMMENT '授权结束时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='车辆信息';


-- 访客表2022-09-13
DROP TABLE IF EXISTS `vis_visitor_record`;
CREATE TABLE `vis_visitor_record` (
  `id` VARCHAR(64) NOT NULL COMMENT '主键',
  `visitor_name` VARCHAR(50)  DEFAULT NULL COMMENT '访客名',
  `visitor_tel` VARCHAR(20)  DEFAULT NULL COMMENT '访客电话号码',
  `visitor_id_card` VARCHAR(60)  DEFAULT NULL COMMENT '访客身份证号',
  `visitor_count` INT DEFAULT '1' COMMENT '访客人数',
  `visitor_face_img` VARCHAR(1000)  DEFAULT NULL COMMENT '访客人脸图',
  `car_id` VARCHAR(32)  DEFAULT NULL COMMENT '车辆id',
  `car_num` VARCHAR(32)  DEFAULT NULL COMMENT '访客车牌号',
  `parking_lot_codes` VARCHAR(200) DEFAULT NULL COMMENT '停车场编码',
  `visit_date` DATE NOT NULL COMMENT '来访日期',
  `visit_reason` TINYINT DEFAULT NULL COMMENT '来访事由 1:商务交流 2:面试 3:其他',
  `visitor_remark` VARCHAR(255)  DEFAULT NULL COMMENT '访客备注',
  `card_no` VARCHAR(100) DEFAULT NULL COMMENT '人员卡号',
  `device_person_id` BIGINT DEFAULT NULL COMMENT '设备人员ID',
  `visitor_type_id` VARCHAR(32)  DEFAULT NULL COMMENT '访客类型ID',
  `visitor_type_name` VARCHAR(32)  DEFAULT NULL COMMENT '访客类型名',
  `access_policy_id` VARCHAR(32)  DEFAULT NULL COMMENT '通行策略ID',
  `park_org_id` VARCHAR(64)  DEFAULT NULL COMMENT '园区ID',
  `company_id` VARCHAR(32)  DEFAULT NULL COMMENT '访问的企业ID',
  `invitor_employee_id` VARCHAR(32)  DEFAULT NULL COMMENT '邀请人或被访人',
  `invitor_name` VARCHAR(50)  DEFAULT NULL COMMENT '被访人姓名，邀请人姓名',
  `invitor_tel` VARCHAR(20)  DEFAULT NULL COMMENT '被访人电话号码，邀请人电话',
  `invitor_remark` VARCHAR(255)  DEFAULT NULL COMMENT '邀请人备注',
  `visitor_source` TINYINT DEFAULT NULL COMMENT '访客来源：1访客预约，2访客邀约',
  `process_status` TINYINT DEFAULT '0' COMMENT '流程状态：0审核中/邀请中，1审核通过/邀请成功，2审核不通过，3已取消，4登记/门禁核验，5签出',
  `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间，记录状态1、2、3的时间',
  `audit_employee_id` VARCHAR(32)  DEFAULT NULL COMMENT '审批人',
  `audit_remark` VARCHAR(155)  DEFAULT NULL COMMENT '审批备注',
  `sign_in_type` TINYINT DEFAULT NULL COMMENT '登记类型:1现场登记,3门禁核验',
  `sign_in_time` DATETIME DEFAULT NULL COMMENT '登记时间',
  `sign_out_time` DATETIME DEFAULT NULL COMMENT '签出时间',
  `deleted` TINYINT NOT NULL DEFAULT '0' COMMENT '删除状态',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(64)  DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='访客记录';

-- 访客流程类型 2022-09-14
DELETE FROM `sys_dictionary` a WHERE a.type='PROCESS_STATUS';
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`)
VALUES ('PROCESS_STATUS_1', NULL, 'PROCESS_STATUS', '访客流程类型', 'ZERO', '审核中/邀请中', 0, 0, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`)
VALUES ('PROCESS_STATUS_2', NULL, 'PROCESS_STATUS', '访客流程类型', 'ONE', '审核通过/邀请成功', 1, 1, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`)
VALUES ('PROCESS_STATUS_3', NULL, 'PROCESS_STATUS', '访客流程类型', 'TWO', '审核不通过', 2, 2, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`)
VALUES ('PROCESS_STATUS_4', NULL, 'PROCESS_STATUS', '访客流程类型', 'THREE', '已取消', 3, 3, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`)
VALUES ('PROCESS_STATUS_5', NULL, 'PROCESS_STATUS', '访客流程类型', 'FOUR', '登记/门禁核验', 4, 4, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`)
VALUES ('PROCESS_STATUS_6', NULL, 'PROCESS_STATUS', '访客流程类型', 'FIVE', '签出', 5, 5, NOW(), 0);

-- 删除访客记录状态维护的任务 2022-09-14
DELETE FROM `com_task` WHERE id='visitorRecordTask';
INSERT INTO `smart-park`.`com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('visitorRecordTask', '59 59 23 * * ?', '访客记录刷新', '每天23:59:59执行一次', 0, NULL, NULL, NULL, 'park-visitor', 'visitorRecordTask', 'refreshSignOutStatus');

-- 修改表列名 2022-09-22
ALTER TABLE `vis_car_access_record_rel` CHANGE COLUMN `car_plate` `car_num` VARCHAR(50) DEFAULT NULL COMMENT '车牌号';

-- 新增停车类型2022-09-22
DELETE FROM sys_dictionary WHERE type='PARKING_TYPE' OR type='PARK_TYPE' ;
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`) VALUES ('PARK_TYPE_1', NULL, 'PARK_TYPE', '停车类型', 'EMPLOYEE_CAR', '员工车辆', 1, 1, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `deleted`) VALUES ('PARK_TYPE_2', NULL, 'PARK_TYPE', '停车类型', 'OFFICIAL_CAR', '公务车辆', 2, 3, NOW(), 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`,`deleted`) VALUES ('PARK_TYPE_3', NULL, 'PARK_TYPE', '停车类型', 'EXTERNAL_CAR', '外部车辆', 3, 3, NOW(), 0);
