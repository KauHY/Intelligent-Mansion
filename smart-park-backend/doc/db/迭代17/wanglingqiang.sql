ALTER TABLE dev_hour_reading MODIFY COLUMN `device_id` varchar(15) NOT NULL COMMENT '设备id';
ALTER TABLE dev_day_reading MODIFY COLUMN `device_id` varchar(15) NOT NULL COMMENT '设备id';
ALTER TABLE dev_month_reading MODIFY COLUMN `device_id` varchar(15) NOT NULL COMMENT '设备id';
ALTER TABLE dev_meter_warning_record MODIFY COLUMN `device_id` varchar(15) NOT NULL COMMENT '设备id';


DROP TABLE IF EXISTS `dev_meter_warning_record`;
CREATE TABLE `dev_meter_warning_record` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
	`product_ip` varchar(15) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ip',
  `device_id` varchar(15) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `device_name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_type` tinyint(1) NOT NULL COMMENT '5水表,6电表,8消防,9液位计',
  `param_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `param_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `param_value` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `warning_code` int NOT NULL COMMENT '告警事件',
  `warning_level` tinyint DEFAULT NULL COMMENT '告警等级',
  `status` tinyint DEFAULT NULL COMMENT '0未处理，1已处理',
  `warning_time` datetime DEFAULT NULL COMMENT '告警时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='计通告警记录';

DROP TABLE IF EXISTS `dev_fire_warning_record`;
CREATE TABLE `dev_fire_warning_record` (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `product_ip` varchar(15) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ip',
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `device_name` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_type` tinyint(1) NOT NULL COMMENT '5水表,6电表,8消防,9液位计',
  `param_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `param_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `param_value` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `warn_type` int DEFAULT NULL COMMENT '报警类型',
  `warn_time` datetime DEFAULT NULL COMMENT '报警时间',
  `warn_code` int DEFAULT NULL COMMENT '告警事件',
  `warn_level` tinyint DEFAULT NULL COMMENT '告警等级',
  `canceled` tinyint DEFAULT NULL COMMENT '告警是否已取消：0未处理，1已处理',
  `real_reason_type` tinyint DEFAULT NULL COMMENT '确认结果，1,真实火警，2误报，3消防演习',
  `opinion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理意见',
  `status` tinyint DEFAULT '0' COMMENT '状态,0待处理，1已处理',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='消防报警记录';


ALTER TABLE vis_access_record ADD COLUMN is_over_temp tinyint DEFAULT NULL COMMENT '体温是否超过37.3：0正常、1超标' AFTER is_pass;
ALTER TABLE vis_access_record DROP COLUMN body_temperature;

ALTER TABLE sys_company ADD COLUMN `visitor_client_time` datetime DEFAULT NULL COMMENT '访客企业信息的配置时间';

-- 设备台账表20220714
ALTER TABLE cus_equipment_ledger MODIFY COLUMN `pic` varchar(300) DEFAULT NULL COMMENT '图片,逗号隔开';

-- 设备台账表20220715
ALTER TABLE vis_apartment_person DROP COLUMN `house_ids`;
ALTER TABLE vis_apartment_person MODIFY COLUMN `device_ids` varchar(1000)  DEFAULT NULL COMMENT '设备id集合,逗号隔开';