-- 20221012重新设计车辆抓拍表
DROP TABLE IF EXISTS `vis_car_access_record_rel`;
CREATE TABLE `vis_car_access_record_rel` (
 `id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
 `car_num` VARCHAR(50) DEFAULT NULL COMMENT '车牌号',
 `park_type` INT NOT NULL COMMENT '停车类型：1员工车辆,2公务车辆,3外部车辆',
 `park_org_id` VARCHAR(64) NOT NULL COMMENT '园区ID',
 `park_org_name` VARCHAR(50) DEFAULT NULL COMMENT '园区名',
 `parking_lot_id` VARCHAR(64) NOT NULL COMMENT '停车场ID',
 `parking_lot` VARCHAR(50) DEFAULT NULL COMMENT '停车场名',
 `enter_img_url` VARCHAR(1000) DEFAULT NULL COMMENT '驶入抓拍照片',
 `enter_car_lane_id` VARCHAR(64) NOT NULL COMMENT '驶入车道ID',
 `enter_car_lane_name` VARCHAR(50) DEFAULT NULL COMMENT '驶入车道名',
 `enter_recognize_time` DATETIME NOT NULL COMMENT '驶入时间',
 `exit_img_url` VARCHAR(1000) DEFAULT NULL COMMENT '驶出抓拍照片',
 `exit_car_lane_id` VARCHAR(64) NOT NULL COMMENT '驶出车道ID',
 `exit_car_lane_name` VARCHAR(50) DEFAULT NULL COMMENT '驶出车道名',
 `exit_recognize_time` DATETIME NOT NULL COMMENT '驶出时间',
 `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='车辆通行记录';