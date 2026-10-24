/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : park

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 22/07/2022 13:51:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for att_attendance_month_record
-- ----------------------------
DROP TABLE IF EXISTS `att_attendance_month_record`;
CREATE TABLE `att_attendance_month_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工id',
  `employee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '员工名',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司名',
  `year` int NULL DEFAULT NULL COMMENT '年份',
  `month` int NULL DEFAULT NULL COMMENT '月份',
  `exception_times` int NULL DEFAULT 0 COMMENT '异常考勤次数',
  `absence_times` int NULL DEFAULT 0 COMMENT '缺勤次数',
  `normal_times` int NULL DEFAULT 0 COMMENT '正常考勤次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤月度统计' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_attendance_month_record
-- ----------------------------

-- ----------------------------
-- Table structure for att_attendance_record
-- ----------------------------
DROP TABLE IF EXISTS `att_attendance_record`;
CREATE TABLE `att_attendance_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工ID',
  `employee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '员工名',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司名',
  `date` date NULL DEFAULT NULL COMMENT '日期',
  `punch_in_time` datetime NULL DEFAULT NULL COMMENT '上班打卡时间',
  `punch_out_time` datetime NULL DEFAULT NULL COMMENT '下班打卡时间',
  `holiday` int NULL DEFAULT NULL COMMENT '是否假期，0：否 1：是',
  `attendance_result` int NULL DEFAULT NULL COMMENT '考勤结果，1：正常 2：异常',
  `punch_in_type` int NULL DEFAULT NULL COMMENT '上班打卡方式，1：上下班打卡 2：外勤打卡',
  `punch_out_type` int NULL DEFAULT NULL COMMENT '下班打卡方式，1：上下班打卡 2：外勤打卡',
  `punch_type` int NULL DEFAULT NULL COMMENT '打卡方式，1：上下班打卡 2：外勤打卡',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工考勤记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_attendance_record
-- ----------------------------

-- ----------------------------
-- Table structure for att_attendance_rule
-- ----------------------------
DROP TABLE IF EXISTS `att_attendance_rule`;
CREATE TABLE `att_attendance_rule`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rule_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规则名称',
  `punch_in_time` time NULL DEFAULT NULL COMMENT '上班打卡时间',
  `punch_out_time` time NULL DEFAULT NULL COMMENT '下班打卡时间',
  `location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '考勤地点',
  `longitude` double NULL DEFAULT NULL COMMENT '经度',
  `latitude` double NULL DEFAULT NULL COMMENT '纬度',
  `days_of_work` json NULL COMMENT '关联工作日',
  `workday_type` int NULL DEFAULT 0 COMMENT '关联工作日类型，0：法定工作日 1：自定义',
  `punch_range` int NULL DEFAULT NULL COMMENT '考勤范围（米）',
  `status` tinyint(3) UNSIGNED ZEROFILL NULL DEFAULT 000 COMMENT '状态，0：禁用 1：启用',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司ID',
  `apply_type` tinyint NULL DEFAULT 0 COMMENT '规则适用类型，0：全公司员工适用，1：指定员工',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤规则' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_attendance_rule
-- ----------------------------

-- ----------------------------
-- Table structure for att_attendance_rule_relation
-- ----------------------------
DROP TABLE IF EXISTS `att_attendance_rule_relation`;
CREATE TABLE `att_attendance_rule_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
  `rule_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考勤规则ID',
  `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤规则关联员工' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_attendance_rule_relation
-- ----------------------------

-- ----------------------------
-- Table structure for att_fieldwork_apply_record
-- ----------------------------
DROP TABLE IF EXISTS `att_fieldwork_apply_record`;
CREATE TABLE `att_fieldwork_apply_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司名',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人id',
  `employee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人姓名',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人号码',
  `fieldwork_start_time` datetime NULL DEFAULT NULL COMMENT '外勤开始时间',
  `fieldwork_end_time` datetime NULL DEFAULT NULL COMMENT '外勤结束时间',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事由',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '状态：1-待审批；2-审核通过；3-审批不通过；4-已取消',
  `situation` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地点',
  `audit_opinion` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新/审批时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人/审批人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '外勤申请记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_fieldwork_apply_record
-- ----------------------------

-- ----------------------------
-- Table structure for att_punch_record
-- ----------------------------
DROP TABLE IF EXISTS `att_punch_record`;
CREATE TABLE `att_punch_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工ID',
  `employee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '员工名',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司ID',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司名',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `punch_date` date NOT NULL COMMENT '打卡日期',
  `punch_time` time NOT NULL COMMENT '打卡时间',
  `punch_date_time` datetime NOT NULL COMMENT '打卡具体时间',
  `location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '打卡地点',
  `longitude` double NULL DEFAULT NULL COMMENT '经度',
  `latitude` double NULL DEFAULT NULL COMMENT '纬度',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '打卡类型:1-上下班打卡；2-外勤打卡',
  `extension` json NULL COMMENT '扩展字段:事由:reason;图片key-picKey',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '打卡记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of att_punch_record
-- ----------------------------

-- ----------------------------
-- Table structure for bus_apply_for_house
-- ----------------------------
DROP TABLE IF EXISTS `bus_apply_for_house`;
CREATE TABLE `bus_apply_for_house`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号码',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '咨询顾问（员工）id',
  `status` tinyint NULL DEFAULT NULL COMMENT '受理状态,1，待受理，2已完成，3已撤回',
  `house_info_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '意向房源:房源招商Id',
  `house_function` tinyint NULL DEFAULT NULL COMMENT '房屋功能：0办公、1商铺、2住宿、3仓库、4其他',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业/个人名称：：取值当前用户已关联的企业，如果关联多个公司，默认按拼音取值第一个关联企业',
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地点：默认意向房源所属楼栋',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '看房申请表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_apply_for_house
-- ----------------------------

-- ----------------------------
-- Table structure for bus_business_chance
-- ----------------------------
DROP TABLE IF EXISTS `bus_business_chance`;
CREATE TABLE `bus_business_chance`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `chance_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商机名称',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区组织Id',
  `business_source` tinyint(1) NULL DEFAULT NULL COMMENT '商机来源：1商机录入，2入驻申请',
  `contact_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `contact_mail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人邮箱',
  `quantity_number` int NULL DEFAULT NULL COMMENT '需求数量',
  `expected_price` double NULL DEFAULT NULL COMMENT '期望价格（元/㎡/月）',
  `chance_stage` tinyint(1) NULL DEFAULT 0 COMMENT '商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭',
  `thread_source` tinyint(1) NULL DEFAULT NULL COMMENT '线索来源：1老客户介绍，2政府资源，3公共资源，4第三方资源，5内部资源，6网络资源，7招商人员资源',
  `follow_employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '-1' COMMENT '跟进人Id',
  `house_ids` json NULL COMMENT '意向房源:房源招商Id集合',
  `latest_follow_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近实际跟进时间',
  `remark` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `remind_frequency` tinyint(1) NULL DEFAULT -1 COMMENT '提醒频次: 1 天、2 周',
  `remind_by_day` smallint NULL DEFAULT NULL COMMENT '按天提醒:天数',
  `remind_by_week` json NULL COMMENT '按周提醒:: 0 全选，1~~7 周一~~周日',
  `remind_time` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提醒时间: HH:MM',
  `remind_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提醒内容',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商机表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_business_chance
-- ----------------------------

-- ----------------------------
-- Table structure for bus_business_chance_record
-- ----------------------------
DROP TABLE IF EXISTS `bus_business_chance_record`;
CREATE TABLE `bus_business_chance_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `business_chance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商机id',
  `record_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '记录类型：1创建商机、2分配商机、3领取商机、4跟进、5提醒商机、6转成客户、7转移商机、8释放商机、9删除商机',
  `chance_stage` tinyint(1) NULL DEFAULT NULL COMMENT '商机阶段：1机会评估，2需求分析，3报价，4谈判，5达成意向，6签订合同，7赢单关闭',
  `follow_type` tinyint(1) NULL DEFAULT NULL COMMENT '跟进类型：1拜访，2接待，3参观',
  `follow_time` datetime NULL DEFAULT NULL COMMENT '实际跟进时间',
  `follow_content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跟进内容',
  `follow_employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '-1' COMMENT '跟进人Id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商机操作记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_business_chance_record
-- ----------------------------

-- ----------------------------
-- Table structure for bus_business_house_relation
-- ----------------------------
DROP TABLE IF EXISTS `bus_business_house_relation`;
CREATE TABLE `bus_business_house_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `business_info_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '招商信息Id',
  `house_info_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '招商房屋信息Id',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房源招商与招商房屋关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_business_house_relation
-- ----------------------------

-- ----------------------------
-- Table structure for bus_business_info
-- ----------------------------
DROP TABLE IF EXISTS `bus_business_info`;
CREATE TABLE `bus_business_info`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' ',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区组织Id',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `effect_type` tinyint NULL DEFAULT NULL COMMENT '招商时效类型,0，限时有效 1:长期有效',
  `deadline` datetime NULL DEFAULT NULL,
  `rent_or_sale` tinyint NULL DEFAULT NULL COMMENT '租售属性，1出租，2出售，3出租出售',
  `highlight` json NULL COMMENT '亮点描述 字典HIGHLIGHT 0智慧办公,1限时入住,2配套齐全,3交通便利,4临近商圈',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '咨询顾问（员工）id',
  `employee_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '咨询电话',
  `title_picture` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题图片',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容详情',
  `address` json NULL COMMENT '地址',
  `status` tinyint NULL DEFAULT 0 COMMENT '发布状态,0未上架，1招商中，2已下架',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房源招商表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_business_info
-- ----------------------------

-- ----------------------------
-- Table structure for bus_customer_info
-- ----------------------------
DROP TABLE IF EXISTS `bus_customer_info`;
CREATE TABLE `bus_customer_info`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `customer_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户编码',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户名称',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `type` tinyint NULL DEFAULT NULL COMMENT '客户类型（COMPANY_TYPE） 0物业公司、1其他、2科研院所、3事业单位、4机关单位、5科技型企业',
  `status` tinyint NULL DEFAULT NULL COMMENT '客户状态,1，已成交，2未成交，3历史成交',
  `contacts` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `contacts_tel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `contacts_mail` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人邮箱',
  `contacts_department` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人部门',
  `contacts_position` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人职位',
  `customer_tag_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户标签',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跟进人id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `business_contacts_list` json NULL COMMENT '业务联系人',
  `industry_field` tinyint NULL DEFAULT NULL COMMENT '行业领域',
  `company_scale` int NULL DEFAULT NULL COMMENT '企业规模(人数)\r\n\r\n',
  `legal_representative` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '法人代表',
  `company_mail` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业邮箱',
  `company_taxes_type` tinyint NULL DEFAULT NULL COMMENT '企业纳税类型',
  `company_management_status` tinyint NULL DEFAULT NULL COMMENT '企业经营状态',
  `address` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `registered_capital` int NULL DEFAULT NULL COMMENT '注册资本',
  `business_term` tinyint NULL DEFAULT NULL COMMENT '\r\n营业期限',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_customer_info
-- ----------------------------

-- ----------------------------
-- Table structure for bus_customer_tag
-- ----------------------------
DROP TABLE IF EXISTS `bus_customer_tag`;
CREATE TABLE `bus_customer_tag`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '客户标签名称',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '客户标签' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_customer_tag
-- ----------------------------

-- ----------------------------
-- Table structure for bus_house_info
-- ----------------------------
DROP TABLE IF EXISTS `bus_house_info`;
CREATE TABLE `bus_house_info`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房屋id',
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼栋id',
  `station_num` int NULL DEFAULT NULL COMMENT '工位数',
  `charge_type` tinyint NULL DEFAULT NULL COMMENT '押付方式',
  `usage_status` tinyint NULL DEFAULT NULL COMMENT '使用状态',
  `visit_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '看房时间',
  `can_divided` tinyint(1) NULL DEFAULT NULL COMMENT '能否分割',
  `unit_rent` double NULL DEFAULT NULL COMMENT '单位租金:元/㎡/月',
  `earliest_rent_time` date NULL DEFAULT NULL COMMENT '最早租期',
  `discuss_personally` tinyint(1) NULL DEFAULT 0 COMMENT '租金是否面议',
  `min_rent_month` int NULL DEFAULT NULL COMMENT '最短租期',
  `free_rent_month` int NULL DEFAULT NULL COMMENT '免租期',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '招商房屋表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bus_house_info
-- ----------------------------

-- ----------------------------
-- Table structure for com_disk_file
-- ----------------------------
DROP TABLE IF EXISTS `com_disk_file`;
CREATE TABLE `com_disk_file`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名_唯一',
  `file_original_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原文件名',
  `file_suffix` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后缀',
  `file_size` bigint NULL DEFAULT NULL COMMENT '大小',
  `relative_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '相对路径,oss的key',
  `deleted` tinyint(1) NULL DEFAULT 0,
  `status` tinyint NULL DEFAULT 2 COMMENT '状态,2正常,1未启用,3已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`file_name`) USING BTREE,
  UNIQUE INDEX `uk_uri`(`relative_path`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_original_name`(`file_original_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '上传文件' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of com_disk_file
-- ----------------------------

-- ----------------------------
-- Table structure for com_multistage_category
-- ----------------------------
DROP TABLE IF EXISTS `com_multistage_category`;
CREATE TABLE `com_multistage_category`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `parent_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `hierarchy` int UNSIGNED NULL DEFAULT 0 COMMENT '层级，顶级为0',
  `expand` tinyint(1) NULL DEFAULT 1 COMMENT '是否能扩展：0-否；1-是',
  `selectable` tinyint(1) NULL DEFAULT 1 COMMENT '是否可选：0-否；1-是',
  `disabled` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '禁用：0-否；1-是',
  `label` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0否1是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `cmc_idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '多级分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of com_multistage_category
-- ----------------------------

-- ----------------------------
-- Table structure for com_multistage_category_relation
-- ----------------------------
DROP TABLE IF EXISTS `com_multistage_category_relation`;
CREATE TABLE `com_multistage_category_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '多级分类表id',
  `business_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务id',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '多级分类关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of com_multistage_category_relation
-- ----------------------------

-- ----------------------------
-- Table structure for com_number_seq
-- ----------------------------
DROP TABLE IF EXISTS `com_number_seq`;
CREATE TABLE `com_number_seq`  (
  `number_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `value` int NULL DEFAULT NULL,
  PRIMARY KEY (`number_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '编号生成器' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of com_number_seq
-- ----------------------------
INSERT INTO `com_number_seq` VALUES ('BN', '2022-07-22 00:10:00', 1212);
INSERT INTO `com_number_seq` VALUES ('BS', '2022-07-18 16:16:04', 1008);
INSERT INTO `com_number_seq` VALUES ('BX', '2022-07-21 14:39:56', 1006);
INSERT INTO `com_number_seq` VALUES ('CK', '2022-06-09 15:44:29', 1004);
INSERT INTO `com_number_seq` VALUES ('DEVICE_ID', '2022-06-29 10:00:55', 1048);
INSERT INTO `com_number_seq` VALUES ('EM', '2022-03-04 19:55:24', 1007);
INSERT INTO `com_number_seq` VALUES ('FD', '2022-05-17 17:13:30', 1002);
INSERT INTO `com_number_seq` VALUES ('FX', '2022-07-05 09:49:14', 1100);
INSERT INTO `com_number_seq` VALUES ('LY', '2022-06-09 10:36:03', 1006);
INSERT INTO `com_number_seq` VALUES ('ON', '2022-07-20 10:37:17', 1107);
INSERT INTO `com_number_seq` VALUES ('RK', '2022-07-14 10:14:43', 1007);
INSERT INTO `com_number_seq` VALUES ('TZ', '2022-07-21 09:41:18', 2);
INSERT INTO `com_number_seq` VALUES ('WM', '2022-03-04 19:55:24', 1003);
INSERT INTO `com_number_seq` VALUES ('WP', '2022-07-14 10:14:10', 1007);
INSERT INTO `com_number_seq` VALUES ('XF', '2022-07-08 15:38:26', 1007);
INSERT INTO `com_number_seq` VALUES ('XG', '2022-03-04 19:54:03', 1000);
INSERT INTO `com_number_seq` VALUES ('YD', '2022-07-21 16:33:40', 1004);

-- ----------------------------
-- Table structure for com_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `com_operate_log`;
CREATE TABLE `com_operate_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP',
  `operate_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单',
  `operate_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作名称',
  `time_consuming` int NOT NULL COMMENT '耗时',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of com_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for com_task
-- ----------------------------
DROP TABLE IF EXISTS `com_task`;
CREATE TABLE `com_task`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cron` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表达式',
  `task_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `closed` tinyint(1) NULL DEFAULT 0 COMMENT '是否关闭，0开启，1关闭',
  `pool_size` tinyint NULL DEFAULT NULL COMMENT '任务线程数',
  `thread_name_prefix` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '线程名前缀',
  `remove_on_cancel_policy` tinyint(1) NULL DEFAULT NULL,
  `application_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属应用名',
  `bean_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '要执行定时任务的类',
  `method_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '要执行定时任务的方法',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '后台任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of com_task
-- ----------------------------
INSERT INTO `com_task` VALUES ('1', '0 0 1 * * ?', '考勤记录数据生成', '', 0, NULL, NULL, NULL, 'park-attendance', 'attendanceTask', 'generateAttendanceRecord');
INSERT INTO `com_task` VALUES ('aaa', '0 */10 * * * ?', '咨询公告浏览数同步', '每隔10分钟执行一次', 0, 2, '============', 1, 'park-customer', 'informationServiceImpl', 'updateViewsToDB');
INSERT INTO `com_task` VALUES ('autoCancelReserve', '0 1,31 * * * ?', '预约会议室超时自动取消', '每半小时触发一次', 0, NULL, NULL, NULL, 'park-meeting', 'meetingAuditTask', 'autoCancelReserve');
INSERT INTO `com_task` VALUES ('autoInventoryStock', '0 20 0 * * ?', '盘点管理-月度盘点', '每天凌晨0:20启动', 0, NULL, NULL, NULL, 'park-customer', 'inventoryStockAutoTask', 'autoInventoryStock');
INSERT INTO `com_task` VALUES ('autoUpdateNoticeViews', '0 0 0/1 * * ?', '公告浏览数同步', '每隔1小时执行一次', 0, NULL, '', 1, 'park-customer', 'noticeServiceImpl', 'updateViewsToDB');
INSERT INTO `com_task` VALUES ('bbb', '0 0 0 * * ?', '巡检计划生成巡检任务', '每天0点执行一次', 0, NULL, NULL, NULL, 'park-inspection', 'inspectionTaskServiceImpl', 'generateInspectionTask');
INSERT INTO `com_task` VALUES ('ccccc', '0 0 0 * * ?', '报修系统退单', '每天0点执行一次', 0, NULL, NULL, NULL, 'park-customer', 'orderServiceImpl', 'handleExceptionOrder');
INSERT INTO `com_task` VALUES ('controlScreenLight', '0 0/30 * * * ?', '会议信息屏灯光控制', '每半小时触发一次', 0, NULL, NULL, NULL, 'park-meeting', 'meetingAuditTask', 'controlScreenLight');
INSERT INTO `com_task` VALUES ('generateMeterConsumption', '0 10 0 1 * ?', '计算企业或房屋的水电用量', '生成水电表用量记录后再生成企业或房屋水电用量，每月1日计算上个月的的', 0, NULL, NULL, NULL, 'park-device', 'meterConsumptionTask', 'generateMeterConsumption');
INSERT INTO `com_task` VALUES ('generateMeterConsumptionRecord', '0 0 0 1 * ?', '生成水电表用量记录', '生成水电表用量记录,及生成水电表关联的合同房屋，每月1日计算上个月的的水表表的水电用量', 0, NULL, NULL, NULL, 'park-device', 'meterConsumptionRecordServiceTask', 'generateMeterConsumptionRecord');
INSERT INTO `com_task` VALUES ('generateMeterReadingRecord', '0 0 0 28 * ?', '生成抄表记录', '每月28日进行抄表', 0, NULL, NULL, NULL, 'park-device', 'meterReadingRecordServiceTask', 'generateMeterReadingRecord');
INSERT INTO `com_task` VALUES ('meterFeeCreate', '0 0 12 1 * ?', '水电费用自动生成', '每个月1号12点', 0, NULL, NULL, NULL, 'park_contract', 'meterConsumptionTask', 'meterFeeCreate');
INSERT INTO `com_task` VALUES ('meterFeeUpdate', '0 10 0 * * ?', '自动未出费用转已出和账单', NULL, 0, NULL, NULL, NULL, 'park_contract', 'meterConsumptionTask', 'meterFeeUpdate');
INSERT INTO `com_task` VALUES ('meterLateCreate', '0 20 0 * * ?', '自动生成水电滞纳金费用', NULL, 0, NULL, NULL, NULL, 'park_contract', 'meterConsumptionTask', 'meterLateCreate');
INSERT INTO `com_task` VALUES ('purchaseStateUpdate', '0 0/5 * * * ?', '采购服务状态值更新', NULL, 0, NULL, '', 1, 'park-industrial', 'purchaseStateTask', 'stateUpdate');
INSERT INTO `com_task` VALUES ('refreshAccessPolicy', '0 10 0 * * ?', '通行策略过期刷新', NULL, 0, NULL, NULL, NULL, 'park-visitor', 'accessPolicySchedule', 'refreshAccessPolicy');
INSERT INTO `com_task` VALUES ('refreshApartmentPerson', '0 20 0 * * ?', '公寓人员过期刷新', NULL, 0, NULL, NULL, NULL, 'park-visitor', 'apartmentPersonSchedule', 'refreshApartmentPerson');
INSERT INTO `com_task` VALUES ('rentFeeLateCreate', '0 10 0 * * ?', '滞纳金生成', '每天凌晨0:10启动', 0, NULL, '', 1, 'park-contract', 'rentContractLateTask', 'rentFeeLateCreate');
INSERT INTO `com_task` VALUES ('rentFeeUpdate', '0 10 0 * * ?', '已出租赁费用和账单生成', '每天凌晨0:10启动', 0, NULL, '', 1, 'park-contract', 'rentContractOutTask', 'rentFeeUpdate');
INSERT INTO `com_task` VALUES ('stateUpdate', '0 0 1 * * ?', '合同状态值更新', NULL, 0, NULL, '', 1, 'park-contract', 'contractStateTask', 'stateUpdate');
INSERT INTO `com_task` VALUES ('updateScreenDeviceStatus', '0 0/10 * * * ?', '会议信息屏状态更新', '每10分钟触发一次', 0, NULL, NULL, NULL, 'park-device', 'deviceInfoTask', 'updateScreenDeviceStatus');

-- ----------------------------
-- Table structure for con_contract
-- ----------------------------
DROP TABLE IF EXISTS `con_contract`;
CREATE TABLE `con_contract`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `no` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同名称',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `total_area` double NULL DEFAULT NULL COMMENT '总面积（㎡）',
  `employee_id` varchar(650) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经办人id，多个时,分隔',
  `attributes` tinyint(1) NULL DEFAULT NULL COMMENT '合同/协议属性：1-新签；2-续签；3-变更；4-退约；5-历史',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `start_date` date NULL DEFAULT NULL COMMENT '合同开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '合同结束日期',
  `contracted_date` date NULL DEFAULT NULL COMMENT '签约日期',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `company_id_a` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '甲方公司id',
  `credit_code_a` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '甲方统一社会信用代码',
  `name_a` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '甲方名称',
  `contact_a` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '甲方联系人',
  `contact_tel_a` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '甲方联系电话',
  `address_a` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '甲方地址',
  `customer_id_b` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乙方公司id',
  `name_b` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乙方企业名称',
  `credit_code_b` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乙方统一社会信用代码',
  `contact_b` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乙方联系人',
  `contact_tel_b` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乙方联系电话',
  `address_b` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乙方地址',
  `attachment_key` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件key，逗号隔开',
  `state` tinyint(1) NULL DEFAULT 1 COMMENT '合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中',
  `process_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当前流程节点',
  `extension` json NULL COMMENT '扩展字段',
  `house_messages` json NULL COMMENT '关联房屋信息',
  `withdraw_date` date NULL DEFAULT NULL COMMENT '退约日期',
  `withdraw_reason` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退约原因',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '合同' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_contract
-- ----------------------------

-- ----------------------------
-- Table structure for con_contract_billing_plan
-- ----------------------------
DROP TABLE IF EXISTS `con_contract_billing_plan`;
CREATE TABLE `con_contract_billing_plan`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同表主键',
  `fee_billing_scheme_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费方案Id',
  `fee_billing_scheme_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费方案名称',
  `billing_cycle` tinyint(1) NULL DEFAULT NULL COMMENT '计费周期：1-一次性；2-月；3-季；4-年',
  `reserved_digits` tinyint(1) NULL DEFAULT NULL COMMENT '保留位数：0-取整数；1-保留一位小数；2-保留两位小数',
  `carry_mode` tinyint(1) NULL DEFAULT NULL COMMENT '进位方式：4-四舍五入；0-向上取整；1-向下取整',
  `formula_type` tinyint(1) NULL DEFAULT NULL COMMENT '公式类型：1-固定金额；2-计算公式',
  `fixed_amount_without_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '固定金额元（不含税）',
  `fixed_amount_with_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '固定金额元（含税）',
  `amount_without_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '金额元（不含税）',
  `amount_with_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '金额元（含税）',
  `calculation_standard` tinyint(1) NULL DEFAULT NULL COMMENT '计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价',
  `unit_price_without_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '单价元（不含税）',
  `unit_price_with_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '单价元（含税）',
  `charge_type` tinyint(1) NULL DEFAULT NULL COMMENT '收费类型：1-预付费；2-后付费',
  `charging_cycle` tinyint(1) NULL DEFAULT NULL COMMENT '收费周期：1-与计费周期一致；2-多计费周期',
  `cumulative_cycle` int NULL DEFAULT NULL COMMENT '累计x个计费周期（当收费周期为多计费周期）',
  `month_by` int NULL DEFAULT NULL COMMENT '第几月',
  `day_of_month` int NULL DEFAULT NULL COMMENT '账单生成日期:每月第x天生成账单',
  `days_for_delay` int NULL DEFAULT NULL COMMENT '最晚缴纳日期，生成日期后第x天',
  `late_type` tinyint NULL DEFAULT NULL COMMENT '滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积',
  `late_rate` decimal(8, 2) NULL DEFAULT NULL COMMENT '滞纳率',
  `late_charge` int NULL DEFAULT NULL COMMENT '是否收取滞纳金 1是 0否',
  `late_day_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '每日滞纳金',
  `exempt_days` int NULL DEFAULT NULL COMMENT '豁免天数',
  `billing_start_date` date NULL DEFAULT NULL COMMENT '计费开始日期',
  `billing_end_date` date NULL DEFAULT NULL COMMENT '计费结束日期',
  `free_rent_day` int NULL DEFAULT NULL COMMENT '免租期（天）',
  `charge_item_message` json NULL COMMENT '收费项详细属性',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '合同计费信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_contract_billing_plan
-- ----------------------------

-- ----------------------------
-- Table structure for con_contract_process_record
-- ----------------------------
DROP TABLE IF EXISTS `con_contract_process_record`;
CREATE TABLE `con_contract_process_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同表主键',
  `operate` tinyint(1) NULL DEFAULT NULL COMMENT '操作：1-编辑；2-删除；3-作废；4-发起审核；5-变更；6-续约；7-退约；8-终止',
  `process_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程no',
  `pre_process_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上一节点流程no',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '合同状态',
  `pre_state` tinyint(1) NULL DEFAULT NULL COMMENT '上一节点合同状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `extension` json NULL COMMENT '拓展',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '合同操作记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_contract_process_record
-- ----------------------------

-- ----------------------------
-- Table structure for con_house_relation
-- ----------------------------
DROP TABLE IF EXISTS `con_house_relation`;
CREATE TABLE `con_house_relation`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `pk_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型为1时合同表主键；类型为2时合同计费信息表主键',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '类型：1-合同关联房屋；2-计费信息应用房源',
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '合同/计费房屋关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_house_relation
-- ----------------------------

-- ----------------------------
-- Table structure for con_meter_consumption_bill
-- ----------------------------
DROP TABLE IF EXISTS `con_meter_consumption_bill`;
CREATE TABLE `con_meter_consumption_bill`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `bill_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单编号',
  `contract_billing_plan_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '费用方案id',
  `meter_consumption_fee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租赁合同费用id',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织id',
  `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司id',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `account_period` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在账期',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所含收费项',
  `house_ids` json NULL COMMENT '房源ids集合',
  `employee_ids` json NULL COMMENT '公寓人员中房屋所关联的员工',
  `due_date` date NULL DEFAULT NULL COMMENT '应收日期',
  `data_source` tinyint NULL DEFAULT NULL COMMENT '数据来源 1合同生成',
  `settled_month` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结算月份',
  `settled_time` datetime NULL DEFAULT NULL COMMENT '结算时间',
  `receivable_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收金额',
  `due_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '待收金额',
  `received_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已收金额',
  `deduction_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '减免金额（元）',
  `latest_pay_date` date NULL DEFAULT NULL COMMENT '最晚缴纳日期',
  `status` tinyint NULL DEFAULT 0 COMMENT '收款状态：0未收，1已收',
  `checkout_date` date NULL DEFAULT NULL COMMENT '收款日期 ',
  `pay_type` tinyint NULL DEFAULT 1 COMMENT '支付方式:1线下银行转账',
  `late_fee_status` tinyint NULL DEFAULT 0 COMMENT '滞纳金免除状态 1是 0否',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `company_files` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业端上传凭证',
  `charge_detail` json NULL COMMENT '费用明细',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电费用账单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_meter_consumption_bill
-- ----------------------------

-- ----------------------------
-- Table structure for con_meter_consumption_bill_record
-- ----------------------------
DROP TABLE IF EXISTS `con_meter_consumption_bill_record`;
CREATE TABLE `con_meter_consumption_bill_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单id',
  `deduction_amount` decimal(12, 2) NOT NULL DEFAULT 0.00 COMMENT '减免金额',
  `file_list` json NULL COMMENT '附件',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电费用账单减免记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_meter_consumption_bill_record
-- ----------------------------

-- ----------------------------
-- Table structure for con_meter_consumption_fee
-- ----------------------------
DROP TABLE IF EXISTS `con_meter_consumption_fee`;
CREATE TABLE `con_meter_consumption_fee`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型 3-租赁合同；4-物业合同；5-公寓合同；',
  `contract_billing_plan_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '费用方案id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费项名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业名称',
  `house_ids` json NULL COMMENT '房源ids集合',
  `due_date` date NULL DEFAULT NULL COMMENT '应收日期',
  `latest_pay_date` date NULL DEFAULT NULL COMMENT '最晚缴纳日期',
  `data_source` tinyint NULL DEFAULT NULL COMMENT '数据来源 1合同生成 2抄表计费',
  `settled_month` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结算月份',
  `settled_time` datetime NULL DEFAULT NULL COMMENT '结算时间',
  `receivable_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收金额',
  `due_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '待收金额',
  `received_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已收金额',
  `deduction_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '减免金额',
  `billing_status` tinyint UNSIGNED NULL DEFAULT 1 COMMENT '出账状态 1待出账 2已出账 3已废除',
  `fee_status` tinyint NULL DEFAULT 0 COMMENT '费用状态 0未收 1已收',
  `charge_detail` json NULL COMMENT '费用明细',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电费用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_meter_consumption_fee
-- ----------------------------

-- ----------------------------
-- Table structure for con_meter_consumption_late_fee
-- ----------------------------
DROP TABLE IF EXISTS `con_meter_consumption_late_fee`;
CREATE TABLE `con_meter_consumption_late_fee`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rent_bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租赁账单id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业名称',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费项名称',
  `bill_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账单编号',
  `settled_month` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结算月份',
  `due_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '逾期金额',
  `due_days` int NULL DEFAULT NULL COMMENT '逾期天数',
  `receive_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '待收滞纳金',
  `receivable_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收滞纳金',
  `overdue_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已收滞纳金',
  `reduce_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '减免滞纳金',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态 1计算中 2已缴清 3已免除',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经办人',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电滞纳金费用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_meter_consumption_late_fee
-- ----------------------------

-- ----------------------------
-- Table structure for con_meter_consumption_leave
-- ----------------------------
DROP TABLE IF EXISTS `con_meter_consumption_leave`;
CREATE TABLE `con_meter_consumption_leave`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` tinyint NULL DEFAULT 1 COMMENT '离场结算类型：RENT_FEE_LEAVE_TYPE',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
  `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司ID',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `contract_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同id',
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同编号',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `deposit` decimal(12, 2) UNSIGNED NULL DEFAULT 0.00 COMMENT '押金',
  `total_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '实收总金额',
  `leave_date` date NOT NULL COMMENT '结算日期',
  `checkout_date` date NULL DEFAULT NULL COMMENT '收款日期',
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结算房间id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `charge_detail` json NULL COMMENT '费用明细',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁合同交易明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_meter_consumption_leave
-- ----------------------------

-- ----------------------------
-- Table structure for con_meter_consumption_trade
-- ----------------------------
DROP TABLE IF EXISTS `con_meter_consumption_trade`;
CREATE TABLE `con_meter_consumption_trade`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_type` tinyint NOT NULL COMMENT '合同费用类型 1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `bill_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单编号',
  `bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
  `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司ID',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '类型：1付款订单，2退款订单',
  `pay_type` tinyint NOT NULL DEFAULT 1 COMMENT '支付方式:1线下银行转账',
  `receivable_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收金额(元)',
  `received_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '实付金额（元）',
  `checkout_date` date NULL DEFAULT NULL COMMENT '收款日期 ',
  `return_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '退款金额',
  `can_return` tinyint(1) NULL DEFAULT NULL COMMENT '是否可退款，0不可退款，1可退款',
  `return_reason` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款原因',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电费用交易明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_meter_consumption_trade
-- ----------------------------

-- ----------------------------
-- Table structure for con_rent_contract_bill
-- ----------------------------
DROP TABLE IF EXISTS `con_rent_contract_bill`;
CREATE TABLE `con_rent_contract_bill`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `bill_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单编号',
  `contract_billing_plan_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '费用方案id',
  `rent_contract_fee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租赁合同费用id',
  `contract_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同id',
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同编号',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织id',
  `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司id',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `account_period` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在账期',
  `fee_type` tinyint NULL DEFAULT NULL COMMENT '费用类型 1房屋租金 7履约保证金',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所含收费项',
  `house_name` json NULL COMMENT '房源名称',
  `house_messages` json NULL COMMENT '房源对象',
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公寓费用使用',
  `employee_ids` json NULL COMMENT '公寓人员中房屋所关联的员工',
  `start_date` date NULL DEFAULT NULL COMMENT '计费周期 开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '计费周期 结束日期',
  `due_date` date NULL DEFAULT NULL COMMENT '应收日期',
  `data_source` tinyint NULL DEFAULT NULL COMMENT '数据来源 1合同生成',
  `receivable_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收金额',
  `due_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '待收金额',
  `received_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已收金额',
  `deduction_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '减免金额（元）',
  `latest_pay_date` date NULL DEFAULT NULL COMMENT '最晚缴纳日期',
  `status` tinyint NULL DEFAULT 0 COMMENT '收款状态：0未收，1已收',
  `checkout_date` date NULL DEFAULT NULL COMMENT '收款日期 ',
  `checkout_type` tinyint NULL DEFAULT NULL COMMENT '收款类型：1正常收款，2离场结算',
  `pay_type` tinyint NULL DEFAULT 1 COMMENT '支付方式:1线下银行转账',
  `total_area` double NULL DEFAULT NULL COMMENT '面积（㎡）',
  `late_fee_status` tinyint NULL DEFAULT 0 COMMENT '滞纳金免除状态 1是 0否',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `company_files` varchar(800) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业端上传凭证',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁合同账单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_rent_contract_bill
-- ----------------------------

-- ----------------------------
-- Table structure for con_rent_contract_bill_record
-- ----------------------------
DROP TABLE IF EXISTS `con_rent_contract_bill_record`;
CREATE TABLE `con_rent_contract_bill_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单id',
  `deduction_amount` decimal(12, 2) NOT NULL DEFAULT 0.00 COMMENT '减免金额',
  `file_list` json NULL COMMENT '附件',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁合同账单减免记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_rent_contract_bill_record
-- ----------------------------

-- ----------------------------
-- Table structure for con_rent_contract_fee
-- ----------------------------
DROP TABLE IF EXISTS `con_rent_contract_fee`;
CREATE TABLE `con_rent_contract_fee`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同id',
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同编号',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `contract_billing_plan_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '费用方案id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `fee_type` tinyint NULL DEFAULT NULL COMMENT '费用类型 1房屋租金 7履约保证金',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费项名称',
  `total_area` double NULL DEFAULT NULL COMMENT '建筑面积（㎡）',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业名称',
  `house_name` json NULL COMMENT '房源名称',
  `start_date` date NULL DEFAULT NULL COMMENT '计费周期 开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '计费周期 结束日期',
  `due_date` date NULL DEFAULT NULL COMMENT '应收日期',
  `account_period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在账期',
  `latest_pay_date` date NULL DEFAULT NULL COMMENT '最晚缴纳日期',
  `data_source` tinyint NULL DEFAULT NULL COMMENT '数据来源 1合同生成',
  `receivable_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收金额',
  `due_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '待收金额',
  `received_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已收金额',
  `deduction_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '减免金额',
  `billing_status` tinyint UNSIGNED NULL DEFAULT 1 COMMENT '出账状态 1待出账 2已出账 3已废除',
  `fee_status` tinyint NULL DEFAULT 0 COMMENT '费用状态 0未收 1已收',
  `cash_status` tinyint NULL DEFAULT 1 COMMENT '押金状态 1未缴清 2已缴清 3已结清',
  `deduction` tinyint NULL DEFAULT NULL COMMENT '是否抵扣 1是 0否',
  `deduction_msg` json NULL COMMENT '抵扣内容',
  `offset_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已抵扣金额',
  `refunded_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已退款金额',
  `lease_date` date NULL DEFAULT NULL COMMENT '退租日期',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经办人',
  `house_messages` json NULL COMMENT '房源对象',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '办理时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁合同费用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_rent_contract_fee
-- ----------------------------

-- ----------------------------
-- Table structure for con_rent_contract_leave
-- ----------------------------
DROP TABLE IF EXISTS `con_rent_contract_leave`;
CREATE TABLE `con_rent_contract_leave`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` tinyint NULL DEFAULT 1 COMMENT '离场结算类型：RENT_FEE_LEAVE_TYPE',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
  `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司ID',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `contract_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同id',
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同编号',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `deposit` decimal(12, 2) UNSIGNED NULL DEFAULT 0.00 COMMENT '押金',
  `total_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '实收总金额',
  `leave_date` date NOT NULL COMMENT '结算日期',
  `checkout_date` date NULL DEFAULT NULL COMMENT '收款日期',
  `bill_ids` json NULL COMMENT '结算的账单id集合',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁合同交易明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_rent_contract_leave
-- ----------------------------

-- ----------------------------
-- Table structure for con_rent_contract_trade
-- ----------------------------
DROP TABLE IF EXISTS `con_rent_contract_trade`;
CREATE TABLE `con_rent_contract_trade`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '合同费用类型 3-房屋租赁合同；4-物业合同；5-公寓合同',
  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `bill_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单编号',
  `bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
  `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司ID',
  `company_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '类型：1付款订单，2退款订单',
  `pay_type` tinyint NOT NULL DEFAULT 1 COMMENT '支付方式:1线下银行转账',
  `receivable_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收金额(元)',
  `received_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '实付金额（元）',
  `checkout_date` date NULL DEFAULT NULL COMMENT '收款日期 ',
  `return_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '退款金额',
  `can_return` tinyint(1) NULL DEFAULT NULL COMMENT '是否可退款，0不可退款，1可退款',
  `return_reason` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款原因',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁合同交易明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_rent_contract_trade
-- ----------------------------

-- ----------------------------
-- Table structure for con_rent_late_fee
-- ----------------------------
DROP TABLE IF EXISTS `con_rent_late_fee`;
CREATE TABLE `con_rent_late_fee`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rent_bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租赁账单id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业名称',
  `fee_type` tinyint NULL DEFAULT NULL COMMENT '费用类型 1房屋租金 7履约保证金',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费项名称',
  `total_area` double NULL DEFAULT NULL COMMENT '建筑面积（㎡）',
  `house_name` json NULL COMMENT '房源名称',
  `bill_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账单编号',
  `account_period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在账期',
  `due_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '逾期金额',
  `due_days` int NULL DEFAULT NULL COMMENT '逾期天数',
  `receive_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '待收滞纳金',
  `receivable_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '应收滞纳金',
  `overdue_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '已收滞纳金',
  `reduce_fine` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '减免滞纳金',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态 1计算中 2已缴清 3已免除',
  `deduction` tinyint(1) NULL DEFAULT NULL COMMENT '是否抵扣 1是 0否',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '经办人',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁滞纳金费用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_rent_late_fee
-- ----------------------------

-- ----------------------------
-- Table structure for con_template
-- ----------------------------
DROP TABLE IF EXISTS `con_template`;
CREATE TABLE `con_template`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `template_key` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'word模板key',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'word模板名字',
  `contract_key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成的合同key',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  `contract_key_time` datetime NULL DEFAULT NULL COMMENT '生成的合同时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '合同模板管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_template
-- ----------------------------

-- ----------------------------
-- Table structure for con_template_library
-- ----------------------------
DROP TABLE IF EXISTS `con_template_library`;
CREATE TABLE `con_template_library`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级',
  `group_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分组ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述说明',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value对应的编码',
  `label` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '-' COMMENT '名称',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '-' COMMENT '值（前端）',
  `field_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '匹配的字段名',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板字段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_template_library
-- ----------------------------
INSERT INTO `con_template_library` VALUES ('1', '0', '0', 'varchar', '-', '-', '合同信息', '-', NULL, 1, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('101', '1', '1', 'varchar', '-', '-', '编号', '${编号}', 'no', 24, '取值于“协议编号”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('102', '1', '1', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 10, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('103', '1', '1', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 6, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('104', '1', '1', 'date', '-', '-', '签约日期', '${签约日期}', 'contractedDate', 18, '取值于“办理日期”；', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('105', '1', '2', 'varchar', '-', '-', '面积', '${面积}', 'totalArea', 25, '取值于“总面积”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('106', '1', '2', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 11, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('107', '1', '2', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 7, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('108', '1', '2', 'date', '-', '-', '签定日期', '${签定日期}', 'contractedDate', 15, '取值于“办理日期”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('109', '1', '3', 'varchar', '-', '-', '合同编号', '${合同编号}', 'no', 19, '取值于“合同编号”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('110', '1', '3', 'number', '-', '-', '租赁期（年）', '${租赁期（年）}', 'leaseTermYear', 28, '取值于“租期”，转成大写', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('111', '1', '3', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 12, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('112', '1', '3', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 8, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('113', '1', '3', 'varchar', '-', '-', '免租期', '${免租期}', 'freeRentDay', 22, '取值于“免租期”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('114', '1', '3', 'date', '-', '-', '合同签订日', '${合同签订日}', 'contractedDate', 16, '取值于“办理日期”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('115', '1', '5', 'varchar', '-', '-', '编号', '${编号}', 'no', 20, '取值于“合同编号”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('116', '1', '5', 'varchar', '-', '-', '客房数', '${客房数}', 'numberOfRooms', 21, '取值于“客房数”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('117', '1', '5', 'money', '-', '-', '租金合计', '${租金合计}', 'rentAmount', 26, '取值于“租金”，（壹万叁仟肆佰元整）', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('118', '1', '5', 'betweendate', '-', '-', '有效期', '${有效期}', 'betweendate', 5, '2021年7月1日—2022年6月30日', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('119', '1', '5', 'date', '-', '-', '签约日期', '${签约日期}', 'contractedDate', 17, '取值于“办理日期”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('120', '1', '5', 'number', '-', '-', '租期', '${租期}', 'leaseTermYear', 27, '取值于“租期”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('121', '1', '4', 'betweendate', '-', '-', '起止日期', '${起止日期}', 'betweendate', 29, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('122', '1', '4', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 13, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('123', '1', '4', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 9, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('124', '1', '4', 'varchar', '-', '-', '物业费', '${物业费}', 'propertyCosts', 23, '取值于“物业费”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('125', '1', '4', 'number', '-', '-', '服务期限', '${服务期限}', 'leaseTermYear', 14, '取值“租期”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('2', '0', '0', 'varchar', '-', '-', '用户信息', '-', NULL, 2, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('201', '2', '1', 'varchar', '-', '-', '甲方', '${甲方}', 'nameA', 30, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('202', '2', '1', 'varchar', '-', '-', '甲方联系人', '${甲方联系人}', 'contactA', 39, '取值于“甲方联系人”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('203', '2', '1', 'varchar', '-', '-', '乙方', '${乙方}', 'nameB', 45, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('204', '2', '1', 'varchar', '-', '-', '乙方联系人', '${乙方联系人}', 'contactB', 55, '取值于“乙方联系人”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('205', '2', '1', 'varchar', '-', '-', '乙方联系电话', '${乙方联系电话}', 'contactTelB', 52, '取值于“乙方联系电话”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('206', '2', '1', 'varchar', '-', '-', '乙方联系人身份证号码', '${乙方联系人身份证号码}', 'contactIdCardNoB', 59, NULL, NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('207', '2', '2', 'varchar', '-', '-', '甲方', '${甲方}', 'nameA', 31, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('208', '2', '2', 'varchar', '-', '-', '乙方', '${乙方}', 'nameB', 46, '取值于“乙方”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('209', '2', '2', 'varchar', '-', '-', '乙方年纳税', '${乙方年纳税}', 'annualTaxTenThousand', 44, '取值于“年纳税额”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('210', '2', '2', 'varchar', '-', '-', '乙方经营范围', '${乙方经营范围}', 'businessScope', 43, '取值于“经营范围”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('211', '2', '3', 'varchar', '-', '-', '出租方', '${出租方}', 'nameA', 32, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('212', '2', '3', 'varchar', '-', '-', '出租方统一社会信用代码', '${出租方统一社会信用代码}', 'creditCodeA', 42, '取值于“甲方统一社会信用代码”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('213', '2', '3', 'varchar', '-', '-', '出租方指定联系人', '${出租方指定联系人}', 'contactA', 40, '取值于“甲方联系人”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('214', '2', '3', 'varchar', '-', '-', '出租方地址', '${出租方地址}', 'addressA', 35, '取值于“甲方地址”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('215', '2', '3', 'varchar', '-', '-', '出租方电话', '${出租方电话}', 'contactTelA', 37, '取值于“甲方联系电话”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('216', '2', '3', 'varchar', '-', '-', '承租方', '${承租方}', 'nameB', 47, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('217', '2', '3', 'varchar', '-', '-', '承租方统一社会信用代码', '${承租方统一社会信用代码}', 'creditCodeB', 58, '取值于“乙方统一社会信用代码”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('218', '2', '3', 'varchar', '-', '-', '承租方指定联系人', '${承租方指定联系人}', 'contactB', 56, '取值于“乙方联系人”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('219', '2', '3', 'varchar', '-', '-', '承租方地址', '${承租方地址}', 'addressB', 50, '取值于“乙方地址”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('220', '2', '3', 'varchar', '-', '-', '承租方电话', '${承租方电话}', 'contactTelB', 53, '取值于“乙方联系电话”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('221', '2', '5', 'varchar', '-', '-', '出租方', '${出租方}', 'nameA', 33, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('222', '2', '5', 'varchar', '-', '-', '承租方', '${承租方}', 'nameB', 48, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('223', '2', '4', 'varchar', '-', '-', '甲方', '${甲方}', 'nameA', 34, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('224', '2', '4', 'varchar', '-', '-', '甲方承办人', '${甲方承办人}', 'contactA', 41, '取值于“甲方联系人”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('225', '2', '4', 'varchar', '-', '-', '甲方地址', '${甲方地址}', 'addressA', 36, '取值于“甲方地址”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('226', '2', '4', 'varchar', '-', '-', '甲方电话', '${甲方电话}', 'contactTelA', 38, '取值于“甲方联系电话”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('227', '2', '4', 'varchar', '-', '-', '乙方', '${乙方}', 'nameB', 49, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('228', '2', '4', 'varchar', '-', '-', '乙方承办人', '${乙方承办人}', 'contactB', 57, '取值于“乙方联系人”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('229', '2', '4', 'varchar', '-', '-', '乙方地址', '${乙方地址}', 'addressB', 51, '取值于“乙方地址”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('230', '2', '4', 'varchar', '-', '-', '乙方电话', '${乙方电话}', 'contactTelB', 54, '取值于“乙方联系电话”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library` VALUES ('3', '0', '0', 'varchar', '-', '-', '关联房屋信息', '-', NULL, 3, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('301', '3', '1', 'varchar', '-', '-', '关联房屋', '${关联房屋}', 'houseMessagesStr', 69, '取值于“所属楼栋单元、所在楼层”', NULL, '2022-04-15 16:28:36', 0);
INSERT INTO `con_template_library` VALUES ('302', '3', '1', 'varchar', '-', '-', '所属园区', '${所属园区}', 'parkOrgName', 71, '取值于“所属园区”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('303', '3', '1', 'varchar', '-', '-', '建筑面积', '${建筑面积}', 'totalArea', 77, '取值于“总面积”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('304', '3', '1', 'varchar', '-', '-', '房屋用途', '${房屋用途}', 'houseFunction', 62, '取值于“房屋功能”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('305', '3', '2', 'varchar', '-', '-', '关联房屋', '${关联房屋}', 'houseMessagesStr', 68, '取值于“所属楼栋单元、楼层房号”', NULL, '2022-04-15 16:28:39', 0);
INSERT INTO `con_template_library` VALUES ('306', '3', '3', 'list', '-', '-', '房屋坐落', '${房屋坐落}', 'houseMessagesList', 65, '取值于“房源名称”', NULL, '2022-06-10 16:28:22', 0);
INSERT INTO `con_template_library` VALUES ('307', '3', '3', 'varchar', '-', '-', '所属园区', '${所属园区}', 'parkOrgName', 73, '取值于“所属园区”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('308', '3', '3', 'varchar', '-', '-', '租用面积', '${租用面积}', 'totalArea', 78, '取值于“总面积”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('309', '3', '3', 'list', '-', '-', '用途', '${用途}', 'houseFunctionList', 63, '取值于“房屋功能”', NULL, '2022-06-10 15:42:00', 0);
INSERT INTO `con_template_library` VALUES ('310', '3', '3', 'list', '-', '-', '房屋总层数', '${房屋总层数}', 'totalFloorList', 76, '取值于“总楼层数”', NULL, '2022-06-10 15:29:54', 0);
INSERT INTO `con_template_library` VALUES ('311', '3', '3', 'list', '-', '-', '所在层数', '${所在层数}', 'floorList', 74, '取值于“所在楼层”', NULL, '2022-06-10 16:28:04', 0);
INSERT INTO `con_template_library` VALUES ('312', '3', '3', 'varchar', '-', '-', '建筑面积', '${建筑面积}', 'totalArea', 79, '取值于“总面积”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('313', '3', '5', 'varchar', '-', '-', '房号', '${房号}', 'houseMessagesStr', 67, '取值于“楼层房号”', NULL, '2022-04-15 16:28:44', 0);
INSERT INTO `con_template_library` VALUES ('314', '3', '5', 'varchar', '-', '-', '单间', '${单间}', 'numberOfSingleRooms', 61, '取值于“单间”的总数', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('315', '3', '5', 'varchar', '-', '-', '标间', '${标间}', 'numberOfTwinRooms', 60, '取值于“标间”的总数', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('316', '3', '5', 'varchar', '-', '-', '套间', '${套间}', 'numberOfSuite', 75, '取值于“套间”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('317', '3', '4', 'varchar', '-', '-', '物业名称', '${物业名称}', 'parkOrgName', 72, '取值于“所属园区”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('318', '3', '4', 'varchar', '-', '-', '类型', '${类型}', 'houseFunction', 64, '取值于“房屋功能”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('319', '3', '4', 'varchar', '-', '-', '坐落位置', '${坐落位置}', 'addressA', 66, '取值于“甲方地址”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('320', '3', '4', 'varchar', '-', '-', '乙方承租房号', '${乙方承租房号}', 'houseMessagesStr', 70, '取值于“所属楼栋单元、所在楼层”', NULL, '2022-04-15 16:28:41', 0);
INSERT INTO `con_template_library` VALUES ('321', '3', '4', 'varchar', '-', '-', '乙方承租面积', '${乙方承租面积}', 'totalArea', 80, '取值于“总面积”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('4', '0', '0', 'varchar', '-', '-', '计费信息', '-', NULL, 4, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library` VALUES ('401', '4', '3', 'varchar', '-', '-', '租赁费用标准', '${租赁费用标准}', 'unitPriceWithTax', 87, '取值于合同详情—计费方案的“单价元”，计算公式为建筑面积*月单价', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('402', '4', '3', 'money', '-', '-', '房屋租赁费用', '${房屋租赁费用}', 'rentAmount', 86, '取值于“租金”（大写的租金）', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('403', '4', '3', 'money', '-', '-', '履约保证金', '${履约保证金}', 'performanceBond', 84, '取值于“履约保证金”（大写的履约保证金）', NULL, '2022-04-14 16:21:44', 0);
INSERT INTO `con_template_library` VALUES ('404', '4', '5', 'calculate', '-', '-', '增值税', '${增值税}', 'vat', 83, 'XXXX元，取值于收费项为“租金”的含税总金额-不含税总金额', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('405', '4', '5', 'varchar', '-', '-', '增值税税率', '${增值税税率}', 'taxRate', 82, '9%，取值于计费方案所关联收费项的“税率”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('406', '4', '5', 'money', '-', '-', '缴纳押金', '${缴纳押金}', 'deposit', 85, '取值于“押金”（肆仟元整）', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library` VALUES ('407', '4', '4', 'varchar', '-', '-', '物业费收费标准', '${物业费收费标准}', 'unitPriceWithTax', 81, '4元/平方米•月，取值于合同详情—计费方案的“单价元”，计算公式为建筑面积*月单价', NULL, '2022-04-11 16:34:52', 0);

-- ----------------------------
-- Table structure for cus_activity
-- ----------------------------
DROP TABLE IF EXISTS `cus_activity`;
CREATE TABLE `cus_activity`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `place` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动地点',
  `activity_time_start` datetime NOT NULL COMMENT '活动开始时间',
  `activity_time_end` datetime NOT NULL COMMENT '活动结束时间',
  `effect_time` datetime NOT NULL COMMENT '生效时间',
  `content` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动详情',
  `conf_register` tinyint NULL DEFAULT 0 COMMENT '是否需要报名',
  `conf_register_deadline` datetime NULL DEFAULT NULL COMMENT '报名截止时间',
  `conf_register_limit` tinyint NOT NULL DEFAULT 0 COMMENT '是否限制报名人数',
  `conf_register_limit_count` bigint NULL DEFAULT NULL COMMENT '报名人数上限',
  `topping` tinyint(1) NULL DEFAULT 0 COMMENT '置顶，1置顶',
  `topping_time` datetime NULL DEFAULT NULL COMMENT '置顶时间',
  `list_picture` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列表图片',
  `detail_picture` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情图片',
  `status` tinyint NULL DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0,未删除，1删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_activity
-- ----------------------------

-- ----------------------------
-- Table structure for cus_activity_register_record
-- ----------------------------
DROP TABLE IF EXISTS `cus_activity_register_record`;
CREATE TABLE `cus_activity_register_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `activity_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `mobile` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0,未删除，1删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动报名记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_activity_register_record
-- ----------------------------

-- ----------------------------
-- Table structure for cus_advertisement
-- ----------------------------
DROP TABLE IF EXISTS `cus_advertisement`;
CREATE TABLE `cus_advertisement`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '广告名称',
  `sort` tinyint NOT NULL COMMENT '排序：数字越小越靠前',
  `position` tinyint NULL DEFAULT 1 COMMENT ' 广告位置 ：1 首页轮播图',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `jump_type` tinyint NULL DEFAULT NULL COMMENT '跳转类型：0 无，1 URL，2 小程序AppID',
  `jump_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转地址/需跳转的小程序AppID',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态： 0 生效中，1 待生效，2 已过期',
  `effect_time_start` datetime NOT NULL COMMENT '生效开始时间',
  `effect_time_end` datetime NOT NULL COMMENT '生效开始时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '广告' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_advertisement
-- ----------------------------

-- ----------------------------
-- Table structure for cus_business_park_relation
-- ----------------------------
DROP TABLE IF EXISTS `cus_business_park_relation`;
CREATE TABLE `cus_business_park_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `business_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织园区id',
  `business_type` tinyint NULL DEFAULT NULL COMMENT '业务类型，1 公告资讯',
  `status` tinyint NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `business_id_index`(`business_type`, `business_id`) USING BTREE,
  INDEX `park_org_id`(`park_org_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '业务和园区多对多关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_business_park_relation
-- ----------------------------

-- ----------------------------
-- Table structure for cus_editor
-- ----------------------------
DROP TABLE IF EXISTS `cus_editor`;
CREATE TABLE `cus_editor`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `element_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项类型',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '题目',
  `value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件辅助字段',
  `constraint_condition` json NOT NULL COMMENT '组件约束条件',
  `element_content` json NULL COMMENT '选项内容',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文本编辑器' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_editor
-- ----------------------------

-- ----------------------------
-- Table structure for cus_equipment_ledger
-- ----------------------------
DROP TABLE IF EXISTS `cus_equipment_ledger`;
CREATE TABLE `cus_equipment_ledger`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名，同一园区下不重复',
  `device_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备编号',
  `category_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类id',
  `device_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备ID',
  `brand` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌',
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼栋id',
  `floor` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼层数',
  `installation_location` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细位置',
  `admin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `pic` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片,逗号隔开',
  `specifications` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格型号',
  `installation_date` date NULL DEFAULT NULL COMMENT '安装日期',
  `original_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原厂编号',
  `manufacturer` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生产厂家',
  `manufacturer_unit` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维保单位',
  `manufacturer_mobile` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '维保电话',
  `state` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-已报废；1-正常',
  `scrap_date` date NULL DEFAULT NULL COMMENT '报废日期',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否删除：0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备台账' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_equipment_ledger
-- ----------------------------

-- ----------------------------
-- Table structure for cus_form
-- ----------------------------
DROP TABLE IF EXISTS `cus_form`;
CREATE TABLE `cus_form`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '园区id',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
  `company_ids` json NULL COMMENT '收集对象,公司',
  `employee_ids` json NULL COMMENT '收集对象,员工',
  `cycle` tinyint NOT NULL COMMENT '周期，1每日、2每周、3每月、4自定义',
  `deadline_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '截止时间',
  `deadline_num` int NULL DEFAULT NULL COMMENT '截止时间的周几或第几天',
  `reminded` tinyint NOT NULL COMMENT '是否提醒，0不提醒，1提醒',
  `template_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板id',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态，1设计中，2收集中，3已结束',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '信息统计表单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_form
-- ----------------------------

-- ----------------------------
-- Table structure for cus_form_submission
-- ----------------------------
DROP TABLE IF EXISTS `cus_form_submission`;
CREATE TABLE `cus_form_submission`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `form_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '信息统计表单id',
  `submission` json NOT NULL COMMENT '信息统计表单提交结果',
  `received_employee_ids` json NOT NULL COMMENT '信息统计表单接收',
  `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提交人',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单提交内容' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_form_submission
-- ----------------------------

-- ----------------------------
-- Table structure for cus_form_template
-- ----------------------------
DROP TABLE IF EXISTS `cus_form_template`;
CREATE TABLE `cus_form_template`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '园区id',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板标题,模板名称',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `editor_ids` json NULL COMMENT '编辑器ids',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表单模板' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_form_template
-- ----------------------------

-- ----------------------------
-- Table structure for cus_goods_release
-- ----------------------------
DROP TABLE IF EXISTS `cus_goods_release`;
CREATE TABLE `cus_goods_release`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区',
  `goods_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '编号',
  `submitter_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人姓名',
  `submitter_mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人手机号',
  `release_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '放行码',
  `company` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所在公司',
  `release_datetime_start` datetime NULL DEFAULT NULL COMMENT '放行时间0',
  `release_datetime_end` datetime NULL DEFAULT NULL COMMENT '放行时间1',
  `goods_list` json NULL COMMENT '放行物品',
  `car_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人',
  `check_opinion` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态,1待审核2 审核通过3审核不通过4取消;',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物品放行' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_goods_release
-- ----------------------------

-- ----------------------------
-- Table structure for cus_information
-- ----------------------------
DROP TABLE IF EXISTS `cus_information`;
CREATE TABLE `cus_information`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` tinyint NOT NULL COMMENT '资讯类型',
  `put_into_type` tinyint NULL DEFAULT NULL COMMENT '投放类型：1 C端，2 运营端，3 C端 运营端',
  `views` int NULL DEFAULT 0 COMMENT '浏览量',
  `effect_time_start` datetime NOT NULL COMMENT '生效开始时间',
  `content` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `content_type` tinyint NOT NULL COMMENT '内容类型：0 内容, 1超链接',
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `status` tinyint NULL DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0,未删除，1删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资讯信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_information
-- ----------------------------

-- ----------------------------
-- Table structure for cus_meeting_goods
-- ----------------------------
DROP TABLE IF EXISTS `cus_meeting_goods`;
CREATE TABLE `cus_meeting_goods`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `goods_type` tinyint NULL DEFAULT NULL COMMENT '物资类型 1购买型、2租用型',
  `goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物资名称',
  `unit_price` decimal(12, 2) NULL DEFAULT NULL COMMENT '价格(元)',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计量单位',
  `goods_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物资图片',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '说明',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室物资管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_meeting_goods
-- ----------------------------

-- ----------------------------
-- Table structure for cus_meeting_permission
-- ----------------------------
DROP TABLE IF EXISTS `cus_meeting_permission`;
CREATE TABLE `cus_meeting_permission`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
  `relation_type` tinyint NOT NULL COMMENT '关联类型 1公司 2员工',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联公司id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联员工id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室权限关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_meeting_permission
-- ----------------------------

-- ----------------------------
-- Table structure for cus_meeting_rent
-- ----------------------------
DROP TABLE IF EXISTS `cus_meeting_rent`;
CREATE TABLE `cus_meeting_rent`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属楼栋单元',
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼层房号',
  `meeting_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室名称',
  `telephone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '咨询电话',
  `capacity` int NULL DEFAULT NULL COMMENT '容纳人数',
  `meeting_equipment` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室设备 1摄像机、2远程会议设备、3投影仪、4电子白板',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片（单张）',
  `detail_image` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情图片（最多五张）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1开启 0关闭',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室介绍',
  `reserve_instruct` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预定须知',
  `charge` tinyint NULL DEFAULT NULL COMMENT '是否收费 1是 0否',
  `rent_price` decimal(11, 2) NULL DEFAULT NULL COMMENT '租赁价格',
  `goods_id` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物资id 逗号分割',
  `use_permission` tinyint NULL DEFAULT NULL COMMENT '使用权限设置 0不限 1指定公司/人员',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室租赁管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_meeting_rent
-- ----------------------------

-- ----------------------------
-- Table structure for cus_meeting_reserve
-- ----------------------------
DROP TABLE IF EXISTS `cus_meeting_reserve`;
CREATE TABLE `cus_meeting_reserve`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人当前园区',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请人id',
  `reserve_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请单号',
  `meeting_subject` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议主题',
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室id',
  `applicant_unit` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请单位（当前员工所属企业名称）',
  `applicant_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人（当前员工姓名）',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '手机号码（当前员工手机号码）',
  `meeting_people_num` int NULL DEFAULT NULL COMMENT '预计人数',
  `estimated_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '预计总金额=所选时段租赁金额+已选物资金额；每一物资金额=单价*数量；',
  `applicant_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `state_change_time` datetime NULL DEFAULT NULL COMMENT '状态变更时间（审核或取消时间）',
  `review_status` tinyint NULL DEFAULT NULL COMMENT '审核状态 1未审核 2通过 3拒绝 4已取消',
  `review_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '审核（实际金额）',
  `review_msg` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `old_park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室所在园区（备份）',
  `old_meeting_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室名字（备份）',
  `old_capacity` int NULL DEFAULT NULL COMMENT '会议室容纳人数（备份）',
  `old_cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片（单张）（备份）',
  `old_reserve_instruct` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预定须知（备份）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁会议室预订' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_meeting_reserve
-- ----------------------------

-- ----------------------------
-- Table structure for cus_meeting_reserve_goods
-- ----------------------------
DROP TABLE IF EXISTS `cus_meeting_reserve_goods`;
CREATE TABLE `cus_meeting_reserve_goods`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室id',
  `meeting_reserve_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室预定id',
  `goods_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对应物资ID',
  `number` int NULL DEFAULT NULL COMMENT '物品数量',
  `old_goods_type` tinyint NULL DEFAULT NULL COMMENT '物资类型 1购买型、2租用型（备份）',
  `old_goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物资名称（备份）',
  `old_unit_price` decimal(12, 2) NULL DEFAULT NULL COMMENT '价格(元)（备份）',
  `old_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '计量单位（备份）',
  `old_goods_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物资图片（备份）',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '说明（备份）',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁会议室预定物资管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_meeting_reserve_goods
-- ----------------------------

-- ----------------------------
-- Table structure for cus_meeting_reserve_time
-- ----------------------------
DROP TABLE IF EXISTS `cus_meeting_reserve_time`;
CREATE TABLE `cus_meeting_reserve_time`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `meeting_reserve_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室预定id',
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人id',
  `now_date` date NULL DEFAULT NULL COMMENT '当前日期',
  `time_type` tinyint NULL DEFAULT NULL COMMENT '时间段类型 1上午 2下午 3晚上',
  `start_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结束时间',
  `meeting_state` tinyint NULL DEFAULT 3 COMMENT '会议可用状态：0不可预定 1可预订',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租赁会议室预订时间管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_meeting_reserve_time
-- ----------------------------

-- ----------------------------
-- Table structure for cus_meeting_schedule_time
-- ----------------------------
DROP TABLE IF EXISTS `cus_meeting_schedule_time`;
CREATE TABLE `cus_meeting_schedule_time`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
  `start_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结束时间',
  `time_type` tinyint NULL DEFAULT NULL COMMENT '时间段类型 1上午 2下午 3晚上',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '可预定时段管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_meeting_schedule_time
-- ----------------------------

-- ----------------------------
-- Table structure for cus_notice
-- ----------------------------
DROP TABLE IF EXISTS `cus_notice`;
CREATE TABLE `cus_notice`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` tinyint NOT NULL COMMENT '公告类型',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织Id',
  `without_limit` tinyint(1) NOT NULL COMMENT '推送范围：0 指定公司/人员, 1 不限',
  `put_into_type` tinyint NULL DEFAULT NULL COMMENT '投放类型：1 C端，2 运营端，3 C端 运营端',
  `popuped` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否弹窗提醒',
  `effect_time_start` datetime NOT NULL COMMENT '生效开始时间',
  `effect_time_end` datetime NULL DEFAULT NULL COMMENT '生效开始时间',
  `summary` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '摘要',
  `content` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `status` tinyint NULL DEFAULT NULL,
  `files` json NULL COMMENT '附件',
  `views` int NULL DEFAULT 0 COMMENT '浏览量',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0,未删除，1删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_notice
-- ----------------------------

-- ----------------------------
-- Table structure for cus_notice_permission
-- ----------------------------
DROP TABLE IF EXISTS `cus_notice_permission`;
CREATE TABLE `cus_notice_permission`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `notice_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告id',
  `relation_type` tinyint NOT NULL COMMENT '关联类型 1公司 2员工',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联公司id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联员工id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公告推送范围关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_notice_permission
-- ----------------------------

-- ----------------------------
-- Table structure for cus_order
-- ----------------------------
DROP TABLE IF EXISTS `cus_order`;
CREATE TABLE `cus_order`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主键',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报事报修工单编号',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区编码',
  `order_owner_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报事报修反馈人',
  `order_owner_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报事报修人电话',
  `order_type` tinyint NOT NULL COMMENT '报事报修类型：1-报事投诉、2-报修服务',
  `order_type_item` tinyint NOT NULL COMMENT '报事投诉类型 ORDERTYPE-1：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务、20-其他	\r\n报修服务类型 ORDERTYPE-2：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备、26-其他\r\n',
  `order_content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '报事报修内容',
  `order_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报事报修创建时间',
  `order_state` tinyint NULL DEFAULT 1 COMMENT '处理状态：1待派单,2待处理,3退单,8已完成,9已撤单',
  `order_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片uri，多个时逗号分隔',
  `order_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报事报修详细地址',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当前处理人Id(员工)',
  `transfer_man` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转办员对应employeeId',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除0，否，1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT ' 创建人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT ' 更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '报事投诉、报修服务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_order
-- ----------------------------

-- ----------------------------
-- Table structure for cus_order_process_record
-- ----------------------------
DROP TABLE IF EXISTS `cus_order_process_record`;
CREATE TABLE `cus_order_process_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工单id',
  `pre_pro_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '上一节点处理NO',
  `order_state` tinyint NOT NULL DEFAULT 1 COMMENT '处理状态：1待派单,2待处理,8已完成,9已撤销',
  `state` tinyint NULL DEFAULT NULL COMMENT '处理状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理备注',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转办处理结果',
  `employee_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前处理员工',
  `employee_mobile` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当前处理员工手机号',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属物业公司Id',
  `pre_employee_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一节点处理员工',
  `finish_time` timestamp NULL DEFAULT NULL COMMENT '完成时间',
  `cancel_reason` tinyint NULL DEFAULT NULL COMMENT '撤单理由:1 问题已自行解决，2 误会了，没有这个问题，3已有人现场处理，4 其他原因',
  `ask_finish_time` date NULL DEFAULT NULL COMMENT '要求完成时间',
  `return_order_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退单原因',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0-否(默认),1-是',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间\r\n',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '报事报修处理流程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_order_process_record
-- ----------------------------

-- ----------------------------
-- Table structure for cus_pro_goods_manager
-- ----------------------------
DROP TABLE IF EXISTS `cus_pro_goods_manager`;
CREATE TABLE `cus_pro_goods_manager`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `goods_type` tinyint NULL DEFAULT NULL COMMENT '物品类别',
  `goods_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物品编码',
  `goods_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物品名称',
  `unit_price` decimal(12, 2) NULL DEFAULT NULL COMMENT '入库单价(元)',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位',
  `brand` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '品牌',
  `model` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '型号',
  `min_stock` int NULL DEFAULT NULL COMMENT '库存下限预警值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物品管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_pro_goods_manager
-- ----------------------------

-- ----------------------------
-- Table structure for cus_pro_inventory_manager
-- ----------------------------
DROP TABLE IF EXISTS `cus_pro_inventory_manager`;
CREATE TABLE `cus_pro_inventory_manager`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `goods_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品id',
  `month` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '盘点月份',
  `last_balance` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上月结余数量',
  `last_balance_total` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上月结余总额（元）',
  `current_balance` int NULL DEFAULT NULL COMMENT '本月库存数',
  `current_balance_total` decimal(30, 2) NULL DEFAULT NULL COMMENT '本月库存总额（元）',
  `current_stock_in_number` int NULL DEFAULT 0 COMMENT '本月入库数量',
  `current_stock_out_number` int NULL DEFAULT 0 COMMENT '本月出库数量',
  `inventory_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '盘点时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '盘点管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_pro_inventory_manager
-- ----------------------------

-- ----------------------------
-- Table structure for cus_pro_receive_flow
-- ----------------------------
DROP TABLE IF EXISTS `cus_pro_receive_flow`;
CREATE TABLE `cus_pro_receive_flow`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `receive_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '领用id',
  `audit_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_state` tinyint NOT NULL COMMENT '审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消',
  `audit_msg` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `stock_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '出入库单号',
  `need_return` tinyint NULL DEFAULT NULL COMMENT '是否需要归还 1是 0否',
  `return_num_current` int NULL DEFAULT NULL COMMENT '本次归还数量',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物品领用流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_pro_receive_flow
-- ----------------------------

-- ----------------------------
-- Table structure for cus_pro_receive_manager
-- ----------------------------
DROP TABLE IF EXISTS `cus_pro_receive_manager`;
CREATE TABLE `cus_pro_receive_manager`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '领用人id',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司id',
  `stock_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品库存id',
  `apply_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请单号',
  `audit_state` tinyint NOT NULL COMMENT '审核状态',
  `stock_state` tinyint NULL DEFAULT NULL COMMENT '出入库状态',
  `receive_num` int NOT NULL COMMENT '领用数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '领用备注',
  `need_return` tinyint NULL DEFAULT NULL COMMENT '是否需要归还 1是 0否',
  `return_num_total` int NULL DEFAULT 0 COMMENT '已归还数量',
  `return_num_current` int NULL DEFAULT 0 COMMENT '本次归还数量',
  `apply_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `stock_in_time` datetime NULL DEFAULT NULL COMMENT '入库时间',
  `stock_out_time` datetime NULL DEFAULT NULL COMMENT '出库时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物品领用管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_pro_receive_manager
-- ----------------------------

-- ----------------------------
-- Table structure for cus_pro_receive_permission
-- ----------------------------
DROP TABLE IF EXISTS `cus_pro_receive_permission`;
CREATE TABLE `cus_pro_receive_permission`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `company_id` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司id',
  `employee_id` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '员工id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '领用权限设置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_pro_receive_permission
-- ----------------------------

-- ----------------------------
-- Table structure for cus_pro_stock_manager
-- ----------------------------
DROP TABLE IF EXISTS `cus_pro_stock_manager`;
CREATE TABLE `cus_pro_stock_manager`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `goods_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品ID',
  `stock_num` int NULL DEFAULT 0 COMMENT '库存数量',
  `average_price` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '入库均价（元）',
  `warehouse_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属仓库ID(暂不实现 统一展示‘公寓仓库’)',
  `total_stock_num` int NULL DEFAULT 0 COMMENT '累计入库数量（隐藏）',
  `total_price` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '累计入库总价（隐藏）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库存管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_pro_stock_manager
-- ----------------------------

-- ----------------------------
-- Table structure for cus_pro_stock_record
-- ----------------------------
DROP TABLE IF EXISTS `cus_pro_stock_record`;
CREATE TABLE `cus_pro_stock_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `goods_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品ID',
  `stock_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '库存id',
  `record_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出入库单号',
  `stock_in_num` int NULL DEFAULT NULL COMMENT '入库数量',
  `stock_in_unit_price` decimal(12, 2) NULL DEFAULT NULL COMMENT '入库单价（元）',
  `current_num` int NULL DEFAULT NULL COMMENT '入库操作后库存',
  `current_average_price` decimal(12, 2) NULL DEFAULT NULL COMMENT '入库操作后均价（元）',
  `stock_type` tinyint NULL DEFAULT NULL COMMENT '出入库方式（业务使用）：1.手动入库、2.归还入库、3.导入入库、4.手动出库、5.领用出库、6.修改库存',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `stock_mode` tinyint NOT NULL DEFAULT 0 COMMENT '出入库方式(统计使用)：1.入库、2.出库、3.修改库存',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '库存出入库记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_pro_stock_record
-- ----------------------------

-- ----------------------------
-- Table structure for cus_questionnaire
-- ----------------------------
DROP TABLE IF EXISTS `cus_questionnaire`;
CREATE TABLE `cus_questionnaire`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '园区id',
  `cover_photo` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面图片',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷标题',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '问卷描述',
  `company_ids` json NULL COMMENT '收集对象,公司',
  `employee_ids` json NULL COMMENT '收集对象,员工',
  `editor_ids` json NULL COMMENT '问卷题目编辑器ids',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `deadline_time` datetime NULL DEFAULT NULL COMMENT '截止时间',
  `anonymous` tinyint NOT NULL COMMENT '是否匿名，0否，1是',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态，1设计中，2收集中，3已结束',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '问卷' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_questionnaire
-- ----------------------------

-- ----------------------------
-- Table structure for cus_questionnaire_submission
-- ----------------------------
DROP TABLE IF EXISTS `cus_questionnaire_submission`;
CREATE TABLE `cus_questionnaire_submission`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `questionnaire_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问卷id',
  `submission` json NOT NULL COMMENT '问卷提交结果',
  `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '提交人',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '问卷提交内容' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cus_questionnaire_submission
-- ----------------------------

-- ----------------------------
-- Table structure for dev_card
-- ----------------------------
DROP TABLE IF EXISTS `dev_card`;
CREATE TABLE `dev_card`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `card_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '卡号',
  `card_medium` int NOT NULL COMMENT '卡片介质:CARD_MEDIUM',
  `card_type` int NULL DEFAULT NULL COMMENT '卡片类型:CARD_TYPE：1普通卡、2管理员卡、3其他',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联园区ID',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属企业ID',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工ID',
  `effect_start_date` date NOT NULL COMMENT '有效期开始日期',
  `effect_end_date` date NOT NULL COMMENT '有效期结束日期',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：CARD_STATUS：1已绑定、2已挂失、3已注销、4已解绑',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '卡片管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_card
-- ----------------------------

-- ----------------------------
-- Table structure for dev_day_reading
-- ----------------------------
DROP TABLE IF EXISTS `dev_day_reading`;
CREATE TABLE `dev_day_reading`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `device_id` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备id',
  `device_type` tinyint(1) NULL DEFAULT NULL COMMENT '5水表，6电表',
  `start_reading_num` decimal(8, 2) NOT NULL COMMENT '开始读数',
  `end_reading_num` decimal(8, 2) NOT NULL COMMENT '截止读数',
  `consumption` decimal(8, 2) NOT NULL COMMENT '用量',
  `reading_date` date NOT NULL COMMENT '抄表日期',
  `reading_time` time NOT NULL COMMENT '抄表时间',
  `reading_date_time` datetime NOT NULL COMMENT '抄表时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电表每天读数' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_day_reading
-- ----------------------------

-- ----------------------------
-- Table structure for dev_device_info
-- ----------------------------
DROP TABLE IF EXISTS `dev_device_info`;
CREATE TABLE `dev_device_info`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `device_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ID',
  `device_type` int NULL DEFAULT NULL COMMENT '设备类型',
  `ip_address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '园区组织ID',
  `extension` json NULL COMMENT '拓展字段',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态：0离线1在线',
  `using_status` tinyint NULL DEFAULT 1 COMMENT '使用状态：1使用中',
  `deleted` tinyint(1) NULL DEFAULT 0,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户ID',
  `location` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备位置',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业ID',
  `channel_nos` json NULL COMMENT '通道号集合（json数组）',
  `device_category` int NULL DEFAULT NULL COMMENT '设备大类，0：门禁',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_park_org_id`(`park_org_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_device_info
-- ----------------------------

-- ----------------------------
-- Table structure for dev_fire_warning_record
-- ----------------------------
DROP TABLE IF EXISTS `dev_fire_warning_record`;
CREATE TABLE `dev_fire_warning_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `product_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ip',
  `device_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `device_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_type` tinyint(1) NOT NULL COMMENT '5水表,6电表,8消防,9液位计',
  `param_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `param_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `param_value` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `warn_type` int NULL DEFAULT NULL COMMENT '报警类型',
  `warn_time` datetime NULL DEFAULT NULL COMMENT '报警时间',
  `warn_code` int NULL DEFAULT NULL COMMENT '告警事件',
  `warn_level` tinyint NULL DEFAULT NULL COMMENT '告警等级',
  `canceled` tinyint NULL DEFAULT NULL COMMENT '告警是否已取消：0未处理，1已处理',
  `real_reason_type` tinyint NULL DEFAULT NULL COMMENT '确认结果，1,真实火警，2误报，3消防演习',
  `opinion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理意见',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态,0待处理，1已处理',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消防报警记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_fire_warning_record
-- ----------------------------

-- ----------------------------
-- Table structure for dev_hour_reading
-- ----------------------------
DROP TABLE IF EXISTS `dev_hour_reading`;
CREATE TABLE `dev_hour_reading`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `device_id` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备id',
  `device_type` tinyint(1) NOT NULL COMMENT '5水表，6电表',
  `start_reading_num` decimal(8, 2) NOT NULL COMMENT '开始读数',
  `end_reading_num` decimal(8, 2) NOT NULL COMMENT '截止读数',
  `consumption` decimal(8, 2) NOT NULL COMMENT '用量',
  `reading_date` date NOT NULL COMMENT '抄表日期',
  `reading_hour` tinyint NOT NULL COMMENT '抄表小时，24H',
  `reading_date_time` datetime NOT NULL COMMENT '抄表时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电表每小时读数' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_hour_reading
-- ----------------------------

-- ----------------------------
-- Table structure for dev_meter
-- ----------------------------
DROP TABLE IF EXISTS `dev_meter`;
CREATE TABLE `dev_meter`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '园区id',
  `meter_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '仪表编号',
  `category` tinyint NOT NULL COMMENT '能耗类别（1电能耗，2水能耗）',
  `effect_property` tinyint NOT NULL COMMENT '表计属性',
  `effect_type` tinyint NULL DEFAULT NULL COMMENT '表计类别',
  `meter_type` tinyint NOT NULL COMMENT '表类型',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `serial_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表计号码',
  `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip地址',
  `port` int NOT NULL COMMENT '端口号',
  `communication_port` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '通信端口',
  `max_range` int NOT NULL COMMENT '最大量程',
  `Installation_location` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装位置',
  `building_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '楼栋',
  `floors` json NOT NULL COMMENT '所属楼层多选',
  `house_ids` json NULL COMMENT '房屋多选',
  `initial_reading` decimal(8, 2) NOT NULL COMMENT '初始读数',
  `initial_time` datetime NOT NULL COMMENT '初始时间',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '仪表（电表和水表）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_meter
-- ----------------------------

-- ----------------------------
-- Table structure for dev_meter_consumption
-- ----------------------------
DROP TABLE IF EXISTS `dev_meter_consumption`;
CREATE TABLE `dev_meter_consumption`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `contract_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同ID',
  `type` tinyint(1) NOT NULL COMMENT '0房屋用量记录，1企业用量记录',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '园区Id',
  `settled_month` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '结算月份',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业Id',
  `company_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业Id',
  `contract_type` tinyint(1) NOT NULL COMMENT '合同类型',
  `building_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '楼栋id',
  `house_ids` json NOT NULL COMMENT '房屋id集合',
  `consumption` json NULL COMMENT '用量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业或房屋水电用量' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_meter_consumption
-- ----------------------------

-- ----------------------------
-- Table structure for dev_meter_consumption_record
-- ----------------------------
DROP TABLE IF EXISTS `dev_meter_consumption_record`;
CREATE TABLE `dev_meter_consumption_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `meter_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应dev_device_info表主键',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '园区id',
  `device_type` int NULL DEFAULT NULL COMMENT '设备类型',
  `effect_property` tinyint NOT NULL COMMENT '表计属性',
  `effect_type` tinyint NULL DEFAULT NULL COMMENT '表计类别',
  `meter_type` tinyint NOT NULL COMMENT '表类型',
  `device_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `max_range` int NOT NULL COMMENT '最大量程',
  `location` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装位置',
  `building_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '楼栋',
  `floors` json NOT NULL COMMENT '所属楼层多选',
  `house_ids` json NULL COMMENT '房屋多选',
  `initial_reading` decimal(8, 2) NOT NULL COMMENT '初始读数',
  `initial_time` datetime NOT NULL COMMENT '初始时间',
  `settled_month` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '结算月份',
  `last_reading` decimal(8, 2) NOT NULL COMMENT '上次抄表读数',
  `last_time` datetime NOT NULL COMMENT '上次抄表时间',
  `this_reading` decimal(8, 2) NULL DEFAULT NULL COMMENT '本次抄表读数',
  `this_time` datetime NOT NULL COMMENT '本次抄表时间',
  `consumption` decimal(8, 2) NULL DEFAULT NULL COMMENT '用量',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '水电表用量记录，结算使用' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_meter_consumption_record
-- ----------------------------

-- ----------------------------
-- Table structure for dev_meter_contract_house_relation
-- ----------------------------
DROP TABLE IF EXISTS `dev_meter_contract_house_relation`;
CREATE TABLE `dev_meter_contract_house_relation`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `settled_month` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '结算月份',
  `meter_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应dev_device_info表主键',
  `contract_house_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同房屋id',
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '水电表合同房屋关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_meter_contract_house_relation
-- ----------------------------

-- ----------------------------
-- Table structure for dev_meter_reading_record
-- ----------------------------
DROP TABLE IF EXISTS `dev_meter_reading_record`;
CREATE TABLE `dev_meter_reading_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `meter_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '对应dev_device_info表主键',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '园区id',
  `device_type` int NULL DEFAULT NULL COMMENT '设备类型',
  `device_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `effect_property` tinyint NOT NULL COMMENT '表计属性',
  `effect_type` tinyint NULL DEFAULT NULL COMMENT '表计类别',
  `meter_type` tinyint NOT NULL COMMENT '表类型',
  `max_range` int NOT NULL COMMENT '最大量程',
  `location` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安装位置',
  `building_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '楼栋',
  `floors` json NOT NULL COMMENT '所属楼层多选',
  `house_ids` json NULL COMMENT '房屋多选',
  `initial_reading` decimal(8, 2) NOT NULL COMMENT '初始读数',
  `initial_time` datetime NOT NULL COMMENT '初始时间',
  `meter_reading` decimal(8, 2) NOT NULL COMMENT '抄表读数',
  `reading_time` datetime NOT NULL COMMENT '抄表时间',
  `corrected_reading` decimal(8, 2) NULL DEFAULT NULL COMMENT '修正后读数',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `import_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导入备注',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '抄表记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_meter_reading_record
-- ----------------------------

-- ----------------------------
-- Table structure for dev_meter_warning_record
-- ----------------------------
DROP TABLE IF EXISTS `dev_meter_warning_record`;
CREATE TABLE `dev_meter_warning_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `product_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备ip',
  `device_id` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `device_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_type` tinyint(1) NOT NULL COMMENT '5水表,6电表,8消防,9液位计',
  `param_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `param_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `param_value` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `warning_code` int NOT NULL COMMENT '告警事件',
  `warning_level` tinyint NULL DEFAULT NULL COMMENT '告警等级',
  `status` tinyint NULL DEFAULT NULL COMMENT '0未处理，1已处理',
  `warning_time` datetime NULL DEFAULT NULL COMMENT '告警时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '计通告警记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_meter_warning_record
-- ----------------------------

-- ----------------------------
-- Table structure for dev_month_reading
-- ----------------------------
DROP TABLE IF EXISTS `dev_month_reading`;
CREATE TABLE `dev_month_reading`  (
  `id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `device_id` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备id',
  `device_type` tinyint(1) NULL DEFAULT NULL COMMENT '5水表，6电表',
  `start_reading_num` decimal(8, 2) NOT NULL COMMENT '开始读数',
  `end_reading_num` decimal(8, 2) NOT NULL COMMENT '截止读数',
  `consumption` decimal(8, 2) NOT NULL COMMENT '用量',
  `year` smallint NOT NULL COMMENT '抄表年份',
  `month` tinyint NOT NULL COMMENT '抄表月份',
  `reading_date` date NOT NULL COMMENT '抄表日期',
  `reading_date_time` datetime NOT NULL COMMENT '抄表时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '水电表每月读数' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dev_month_reading
-- ----------------------------

-- ----------------------------
-- Table structure for fee_billing_scheme
-- ----------------------------
DROP TABLE IF EXISTS `fee_billing_scheme`;
CREATE TABLE `fee_billing_scheme`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `scheme_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '方案名称',
  `charge_item_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费项',
  `billing_cycle` tinyint NULL DEFAULT NULL COMMENT '计费周期,计费周期：1-一次性；2-月；3-季；4-年',
  `reserved_digits` tinyint NULL DEFAULT NULL COMMENT '保留位数,保留位数：0-取整数；1-保留一位小数；2-保留两位小数',
  `carry_mode` tinyint NULL DEFAULT NULL COMMENT '进位方式：4-四舍五入；0-向上取整；1-向下取整',
  `formula_type` tinyint NULL DEFAULT NULL COMMENT '公式类型',
  `amount_with_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '含税金额',
  `amount_without_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '不含税金额',
  `calculation_standard` tinyint(1) NULL DEFAULT NULL COMMENT '计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价',
  `unit_price_without_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '单价元（不含税）',
  `unit_price_with_tax` decimal(12, 2) NULL DEFAULT NULL COMMENT '单价元（含税）',
  `charge_type` tinyint NULL DEFAULT NULL COMMENT '收费方式',
  `charging_cycle` tinyint(1) NULL DEFAULT NULL COMMENT '收费周期：1-与计费周期一致；2-多计费周期',
  `cumulative_cycle` int NULL DEFAULT NULL COMMENT '累计周期',
  `month_by` int NULL DEFAULT NULL COMMENT '第几月',
  `day_of_month` int NULL DEFAULT NULL COMMENT '账单生成日期:每月第x天生成账单',
  `days_for_delay` int NULL DEFAULT NULL COMMENT '最晚缴纳日期\r\n\r\n最晚缴纳日期\r\n\r\n最晚缴纳日期，账单后的第几天',
  `late_type` tinyint NULL DEFAULT NULL COMMENT '滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积',
  `late_rate` decimal(8, 2) NULL DEFAULT NULL COMMENT '滞纳率',
  `late_charge` int NULL DEFAULT NULL COMMENT '是否收取滞纳金 1是 0否',
  `late_day_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '每日滞纳金',
  `exempt_days` int NULL DEFAULT NULL COMMENT '豁免天数',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态：0-关闭；1-开启',
  `fee_type` int NULL DEFAULT NULL COMMENT '费用类型',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '计费方案' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fee_billing_scheme
-- ----------------------------

-- ----------------------------
-- Table structure for fee_charge_item
-- ----------------------------
DROP TABLE IF EXISTS `fee_charge_item`;
CREATE TABLE `fee_charge_item`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区',
  `charge_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收费名称',
  `fee_type` tinyint NULL DEFAULT NULL COMMENT '费用类型',
  `payee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '收款方',
  `tax_rate` decimal(8, 2) NULL DEFAULT NULL COMMENT '税率',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态：0-关闭；1-开启',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收费项' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fee_charge_item
-- ----------------------------

-- ----------------------------
-- Table structure for fee_price
-- ----------------------------
DROP TABLE IF EXISTS `fee_price`;
CREATE TABLE `fee_price`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `price_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '价格名称',
  `price_type` tinyint NULL DEFAULT NULL COMMENT '价格类型',
  `unit_price` decimal(12, 2) NULL DEFAULT NULL COMMENT '单价',
  `has_tax` tinyint(1) NULL DEFAULT NULL COMMENT '是否含税',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `delete` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标准价' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of fee_price
-- ----------------------------

-- ----------------------------
-- Table structure for ind_enterprise_service
-- ----------------------------
DROP TABLE IF EXISTS `ind_enterprise_service`;
CREATE TABLE `ind_enterprise_service`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务名',
  `push_range` json NULL COMMENT '推送范围',
  `group_type` tinyint(1) NULL DEFAULT NULL COMMENT '所在分组：0-服务指南；1-政策申报；2-其他',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `cover` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `banner` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'banner',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '摘要',
  `detail` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接',
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '咨询电话',
  `att` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `state` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0-待发布；1-已发布；2-停用',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业服务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ind_enterprise_service
-- ----------------------------

-- ----------------------------
-- Table structure for ind_purchase_service
-- ----------------------------
DROP TABLE IF EXISTS `ind_purchase_service`;
CREATE TABLE `ind_purchase_service`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司Id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务名',
  `purchase_type` tinyint(1) NULL DEFAULT NULL COMMENT '采购类型：0-货物；1-服务；2-其他',
  `detail` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情',
  `effective_end_time` datetime NULL DEFAULT NULL COMMENT '有效截止时间',
  `att_key` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件',
  `jump_type` tinyint(1) NULL DEFAULT NULL COMMENT '跳转类型：0-无；1-URL；2-小程序',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `state` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0-待发布；1-进行中；2-已下架；3-已过期；',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '采购服务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ind_purchase_service
-- ----------------------------

-- ----------------------------
-- Table structure for ind_supply_service
-- ----------------------------
DROP TABLE IF EXISTS `ind_supply_service`;
CREATE TABLE `ind_supply_service`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区Id',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司Id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务名',
  `supply_type` tinyint(1) NULL DEFAULT NULL COMMENT '供应类型：0-产品供应；1-服务供应；2-其他供应',
  `detail` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详情',
  `jump_type` tinyint(1) NULL DEFAULT NULL COMMENT '跳转类型：0-无；1-URL；2-小程序',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转地址',
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `state` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0-待发布；1-已发布；2-下架',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '供应服务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ind_supply_service
-- ----------------------------

-- ----------------------------
-- Table structure for ind_tender_record
-- ----------------------------
DROP TABLE IF EXISTS `ind_tender_record`;
CREATE TABLE `ind_tender_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `purchase_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '招标id',
  `company_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业名称',
  `contact` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `tel` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `mail` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `bidding_documents_paid` tinyint(1) NULL DEFAULT 0 COMMENT '支付招标文件费：0-未支付；1-已支付',
  `margin_paid` tinyint(1) NULL DEFAULT 0 COMMENT '保证金缴纳：0未缴纳；1-已缴纳',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '投标记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ind_tender_record
-- ----------------------------

-- ----------------------------
-- Table structure for ins_circuit_point_relation
-- ----------------------------
DROP TABLE IF EXISTS `ins_circuit_point_relation`;
CREATE TABLE `ins_circuit_point_relation`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `circuit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检路线',
  `point_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检点',
  `sort` tinyint NULL DEFAULT NULL COMMENT '排序',
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '巡检路线巡检点关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ins_circuit_point_relation
-- ----------------------------

-- ----------------------------
-- Table structure for ins_inspection_circuit
-- ----------------------------
DROP TABLE IF EXISTS `ins_inspection_circuit`;
CREATE TABLE `ins_inspection_circuit`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区Id',
  `circuit_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检路线名',
  `type` tinyint NOT NULL COMMENT '巡检类型',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `orderly` tinyint(1) NOT NULL COMMENT '是否有序',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '巡检路线' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ins_inspection_circuit
-- ----------------------------

-- ----------------------------
-- Table structure for ins_inspection_employee_relation
-- ----------------------------
DROP TABLE IF EXISTS `ins_inspection_employee_relation`;
CREATE TABLE `ins_inspection_employee_relation`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '1,巡检计划，2巡检任务',
  `target_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检计划Id或巡检任务Id',
  `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检人Id',
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_target_id`(`target_id`) USING BTREE,
  INDEX `index_employee_id`(`employee_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '巡检计划巡检人关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ins_inspection_employee_relation
-- ----------------------------

-- ----------------------------
-- Table structure for ins_inspection_plan
-- ----------------------------
DROP TABLE IF EXISTS `ins_inspection_plan`;
CREATE TABLE `ins_inspection_plan`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织Id',
  `plan_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '计划名称',
  `type` tinyint NOT NULL COMMENT '巡检类型',
  `circuit_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检路线Id',
  `cycle_type` tinyint(1) NULL DEFAULT NULL COMMENT '执行周期类型,0天，1周',
  `execution_rule` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行规则',
  `can_delay` tinyint(1) NULL DEFAULT NULL COMMENT '节假日是否顺延',
  `execution_time_start` time NULL DEFAULT NULL COMMENT '执行时间0',
  `execution_time_end` time NULL DEFAULT NULL COMMENT '执行时间1',
  `effect_date_start` date NULL DEFAULT NULL COMMENT '生效日期0',
  `effect_date_end` date NULL DEFAULT NULL COMMENT '生效日期1',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '计划状态，0关闭，1开启，-1删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '巡检计划' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ins_inspection_plan
-- ----------------------------

-- ----------------------------
-- Table structure for ins_inspection_point
-- ----------------------------
DROP TABLE IF EXISTS `ins_inspection_point`;
CREATE TABLE `ins_inspection_point`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `point_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检点名称',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区id',
  `type` tinyint(1) NOT NULL COMMENT '巡检点类型',
  `location` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检位置',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '巡检点表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ins_inspection_point
-- ----------------------------

-- ----------------------------
-- Table structure for ins_inspection_point_detail
-- ----------------------------
DROP TABLE IF EXISTS `ins_inspection_point_detail`;
CREATE TABLE `ins_inspection_point_detail`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务Id',
  `point_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '巡检点名称',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '巡检情况',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '情况说明',
  `picture` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '巡检图片',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '巡检点巡检状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '巡检任务的巡检点详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ins_inspection_point_detail
-- ----------------------------

-- ----------------------------
-- Table structure for ins_inspection_task
-- ----------------------------
DROP TABLE IF EXISTS `ins_inspection_task`;
CREATE TABLE `ins_inspection_task`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '巡检类型',
  `task_content` json NOT NULL COMMENT '巡检内容包括巡检路线，巡检点等',
  `task_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `task_date` date NOT NULL COMMENT '任务日期',
  `execution_time_start` time NULL DEFAULT NULL,
  `execution_time_end` time NULL DEFAULT NULL,
  `completion_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `actual_employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '实际巡检人',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '巡检状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '巡检任务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ins_inspection_task
-- ----------------------------

-- ----------------------------
-- Table structure for mee_default_floor
-- ----------------------------
DROP TABLE IF EXISTS `mee_default_floor`;
CREATE TABLE `mee_default_floor`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '绑定的企业ID',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工id',
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设置默认楼栋单元',
  `floor_num` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设置默认楼层',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室默认楼层' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mee_default_floor
-- ----------------------------

-- ----------------------------
-- Table structure for mee_meeting_manage
-- ----------------------------
DROP TABLE IF EXISTS `mee_meeting_manage`;
CREATE TABLE `mee_meeting_manage`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属楼栋单元',
  `floor_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在楼层',
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼层房号',
  `meeting_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室名称',
  `meeting_type` tinyint NULL DEFAULT NULL COMMENT '会议室类型 1：普通会议 2：视频会议',
  `open_time_start` time NULL DEFAULT NULL COMMENT '开始开放时间',
  `open_time_end` time NULL DEFAULT NULL COMMENT '结束开放时间',
  `capacity` int NULL DEFAULT NULL COMMENT '容纳人数',
  `meeting_equipment` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室设备 1摄像机 2远程会议设备 3DVR 4电话 5投影仪 6电子白板',
  `need_review` tinyint(1) NULL DEFAULT 0 COMMENT '是否需要审核 1是 0否',
  `screen_device_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议屏设备，对应device_info表的id',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：1开启 0关闭',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室介绍',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mee_meeting_manage
-- ----------------------------

-- ----------------------------
-- Table structure for mee_meeting_permission
-- ----------------------------
DROP TABLE IF EXISTS `mee_meeting_permission`;
CREATE TABLE `mee_meeting_permission`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
  `relation_type` tinyint NOT NULL COMMENT '关联类型 1公司 2员工',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联公司id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联员工id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室权限关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mee_meeting_permission
-- ----------------------------

-- ----------------------------
-- Table structure for mee_meeting_plane_figure
-- ----------------------------
DROP TABLE IF EXISTS `mee_meeting_plane_figure`;
CREATE TABLE `mee_meeting_plane_figure`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属楼栋单元',
  `floor_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在楼层',
  `meeting_plane_figure` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室平面图',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室平面图' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mee_meeting_plane_figure
-- ----------------------------

-- ----------------------------
-- Table structure for mee_meeting_reserve
-- ----------------------------
DROP TABLE IF EXISTS `mee_meeting_reserve`;
CREATE TABLE `mee_meeting_reserve`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请人id',
  `meeting_subject` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议主题',
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室id',
  `meeting_type` tinyint NOT NULL COMMENT '会议类型 1：普通会议 2：视频会议',
  `type` tinyint NULL DEFAULT 1 COMMENT '预订类型 1预订 2候补',
  `meeting_people_num` int NULL DEFAULT NULL COMMENT '与会人数',
  `attendees` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '与会人list',
  `meeting_time_start` datetime NULL DEFAULT NULL COMMENT '会议开始时间',
  `meeting_time_end` datetime NULL DEFAULT NULL COMMENT '会议结束时间',
  `meeting_real_start` datetime NULL DEFAULT NULL COMMENT '会议实际开始时间',
  `meeting_real_end` datetime NULL DEFAULT NULL COMMENT '会议实际结束时间',
  `meeting_summary` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议纪要',
  `meeting_summary_file` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议纪要附件',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室预订' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mee_meeting_reserve
-- ----------------------------

-- ----------------------------
-- Table structure for mee_meeting_review
-- ----------------------------
DROP TABLE IF EXISTS `mee_meeting_review`;
CREATE TABLE `mee_meeting_review`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联审批员工id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室审核人关联' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mee_meeting_review
-- ----------------------------

-- ----------------------------
-- Table structure for mee_reserve_audit
-- ----------------------------
DROP TABLE IF EXISTS `mee_reserve_audit`;
CREATE TABLE `mee_reserve_audit`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `meeting_reserve_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室预定id',
  `meeting_subject` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议主题',
  `meeting_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '会议室id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人id',
  `now_date` date NULL DEFAULT NULL COMMENT '当前日期',
  `start_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结束时间',
  `meeting_state` tinyint NULL DEFAULT 1 COMMENT '会议预定状态：1不可预定 2可预订 3已预定 4可候补',
  `meeting_status` tinyint NULL DEFAULT NULL COMMENT '会议审核状态 1预定待审核、2预定成功、3候补待审核、4候补中、5手动取消 6超时取消',
  `review_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人',
  `review_status` tinyint NULL DEFAULT NULL COMMENT '审核状态 0未审核 1已审核',
  `review_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `meeting_cancel_time` datetime NULL DEFAULT NULL COMMENT '会议取消时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议室预订审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mee_reserve_audit
-- ----------------------------

-- ----------------------------
-- Table structure for sys_building
-- ----------------------------
DROP TABLE IF EXISTS `sys_building`;
CREATE TABLE `sys_building`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼栋名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '楼栋编码',
  `total_floor` int NULL DEFAULT NULL COMMENT '总层数',
  `build_time` date NULL DEFAULT NULL COMMENT '建成时间',
  `elevator` tinyint(1) NOT NULL COMMENT '电梯：0：无，1：有',
  `cover_area` double NULL DEFAULT NULL COMMENT '占地面积',
  `build_area` double NULL DEFAULT NULL COMMENT '建筑面积',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织Id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `property` tinyint NULL DEFAULT NULL COMMENT '属性: 字典 BUILDING_PROPERTY 1办公、2食堂、3公寓、4其他',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '楼栋表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_building
-- ----------------------------

-- ----------------------------
-- Table structure for sys_business_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_business_menu`;
CREATE TABLE `sys_business_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `origin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户菜单对应的原始菜单ID',
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织的顶级组织的id,即租户，0表示租户管理平台的模板菜单，-1表示租户管理平台的系统菜单',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父节点id，0表示菜单最上级节点',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` double(10, 2) NOT NULL COMMENT '排序',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0：不显示，1：显示',
  `route` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由',
  `route_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由名',
  `api_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口路径',
  `type` tinyint NULL DEFAULT NULL COMMENT '类型：0：菜单 1：按钮',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业端菜单/按钮表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_business_menu
-- ----------------------------
INSERT INTO `sys_business_menu` VALUES ('1506470175973257218', '1506470175272808449', '1437679155114393601', '0', '园区入驻', 'brand-fill', 2.00, 1, '/wisdom-park-company/entry-record', 'entryRecord', '/smart-park', 0, '2022-03-25 10:21:59', '2022-03-30 10:19:20', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506470443699875841', '1506470442739380226', '1437679155114393601', '1506470175973257218', '入驻申请', '', 0.00, 1, '/wisdom-park-company/entry-record/entry-supply', 'entrySupply', '/smart-park', 0, '2022-03-25 10:23:45', '2022-03-30 10:18:55', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506470804351299586', '1506470803843788802', '1437679155114393601', '1506470175973257218', '入驻记录', NULL, 0.00, 1, '/wisdom-park-company/entry-record/entry-log', 'entryLog', '/smart-park', 0, '2022-03-25 10:24:49', '2022-03-30 10:19:03', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506471067837476866', '1506471067535486977', '1437679155114393601', '0', '供应服务', 'good', 3.00, 1, '/wisdom-park-company/supply-service', 'supplyService', '/smart-park', 0, '2022-03-25 10:25:55', '2022-03-30 10:19:44', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506471231545356289', '1506471231226589186', '1437679155114393601', '0', '采购服务', 'brand', 4.00, 1, '/wisdom-park-company/procurement-services', 'procurementServices', '/smart-park', 0, '2022-03-25 10:27:15', '2022-03-30 10:19:52', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506509169448660994', '1506509169113116673', '1437679155114393601', '0', '企业信息', 'form', 1.00, 1, '/wisdom-park-company/enterprise-information', 'enterpriseInformation', '/smart-park', 0, '2022-03-25 10:20:19', '2022-03-30 10:18:44', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506587258329444354', '1506587257880653825', '1437679155114393601', '1506471231545356289', '投标信息详情', NULL, 0.00, 1, '/wisdom-park-company/procurement-services/procurement-detail', 'procurementDetail', '/smart-park', 2, '2022-03-25 10:28:23', '2022-07-08 17:13:24', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1527485640836616193', '1527485640446545921', '1437679155114393601', '0', '费用账单', 'consumption-fill', 0.00, 1, '/wisdom-park-company/expenseil-bill-mange', 'expenseilBllMange', '/park-system', 0, '2022-05-23 10:17:30', '2022-05-27 17:15:32', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1527486091485220866', '1527486090948349954', '1437679155114393601', '1527485640836616193', '合同费用账单', NULL, 0.00, 1, '/wisdom-park-company/expenseil-bill-mange/contract-expenseil-bill-list', 'contractExpenseilBillList', '/park-system', 0, '2022-05-23 10:18:27', '2022-05-23 10:20:23', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1527486185160806401', '1527486184577798145', '1437679155114393601', '1527485640836616193', '水电费用账单', NULL, 0.00, 1, '/wisdom-park-company/expenseil-bill-mange/utilities-expenseil-bill-list', 'utilitiesExpenseilBllList', '/park-system', 0, '2022-05-23 10:20:21', '2022-05-23 10:20:27', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_business_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_business_user`;
CREATE TABLE `sys_business_user`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '归属租户',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0否，1 是',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业端用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_business_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_company
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '绑定园区ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统一社会信用代码',
  `licenses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '营业执照图片路径',
  `company_type` tinyint NULL DEFAULT NULL COMMENT '0：物业公司，1：入驻企业',
  `industry_field` tinyint NULL DEFAULT NULL COMMENT '行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他',
  `company_taxes_type` tinyint NULL DEFAULT NULL COMMENT '企业纳税类型:1-一般纳税人；2-小规模纳税人',
  `enterprise_size` tinyint NULL DEFAULT NULL COMMENT '企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业',
  `atts` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '附件路径，多个逗号隔开',
  `corporate_tax` decimal(10, 2) NULL DEFAULT NULL COMMENT '企业税收',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `contacts` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `tel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `house_count` int NULL DEFAULT 0 COMMENT '绑定房屋个数',
  `address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `visitor_client_type` int NULL DEFAULT NULL COMMENT '企业绑定访客客户端类型，0：无 1：小程序 2：超链接',
  `visitor_client_appid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客客户端小程序appId',
  `visitor_client_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客客户端H5链接地址',
  `visitor_client_pic_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客客户端图片url',
  `source` tinyint NULL DEFAULT NULL COMMENT '数据来源：1-管理员录入 2-企业申请',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '企业状态:0-已禁用,1-正常',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `visitor_client_time` datetime NULL DEFAULT NULL COMMENT '访客企业信息的配置时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '入驻企业表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_company
-- ----------------------------

-- ----------------------------
-- Table structure for sys_company_audit
-- ----------------------------
DROP TABLE IF EXISTS `sys_company_audit`;
CREATE TABLE `sys_company_audit`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `business_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业端用户id',
  `company_information_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业信息id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请入驻园区ID',
  `contact_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `apply_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入驻提交时间',
  `audit_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  `audit_status` tinyint NULL DEFAULT NULL COMMENT '状态 1待审核 2通过 3拒绝 4已取消',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业入驻审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_company_audit
-- ----------------------------

-- ----------------------------
-- Table structure for sys_company_information
-- ----------------------------
DROP TABLE IF EXISTS `sys_company_information`;
CREATE TABLE `sys_company_information`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统一社会信用代码',
  `licenses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '营业执照图片路径',
  `company_type` tinyint NULL DEFAULT NULL COMMENT '企业类型:0-物业公司；1-普通公司；2-科研院所；3-事业单位；4-机关单位；5-科技型企业',
  `industry_field` tinyint NULL DEFAULT NULL COMMENT '行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他',
  `company_taxes_type` tinyint NULL DEFAULT NULL COMMENT '企业纳税类型:1-一般纳税人；2-小规模纳税人',
  `enterprise_size` tinyint NULL DEFAULT NULL COMMENT '企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业',
  `atts` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '附件路径，多个逗号隔开',
  `corporate_tax` decimal(10, 2) NULL DEFAULT NULL COMMENT '企业税收',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册地址',
  `business_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业端用户id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_company_information
-- ----------------------------

-- ----------------------------
-- Table structure for sys_company_information_audit
-- ----------------------------
DROP TABLE IF EXISTS `sys_company_information_audit`;
CREATE TABLE `sys_company_information_audit`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统一社会信用代码',
  `licenses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '营业执照图片路径',
  `company_type` tinyint NULL DEFAULT NULL COMMENT '企业类型:0-物业公司；1-普通公司；2-科研院所；3-事业单位；4-机关单位；5-科技型企业',
  `industry_field` tinyint NULL DEFAULT NULL COMMENT '行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他',
  `company_taxes_type` tinyint NULL DEFAULT NULL COMMENT '企业纳税类型:1-一般纳税人；2-小规模纳税人',
  `enterprise_size` tinyint NULL DEFAULT NULL COMMENT '企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业',
  `atts` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '附件路径，多个逗号隔开',
  `corporate_tax` decimal(10, 2) NULL DEFAULT NULL COMMENT '企业税收',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '注册地址',
  `business_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业端用户id',
  `audit_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核意见',
  `audit_status` tinyint NULL DEFAULT NULL COMMENT '状态 1待审核 2通过 3拒绝 4已取消',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '企业信息审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_company_information_audit
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
  `desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value对应的编码',
  `label` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `value` int NOT NULL COMMENT '值',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0否1是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dictionary
-- ----------------------------
INSERT INTO `sys_dictionary` VALUES ('ACCESS_DEVICE_TYPE_1', NULL, 'ACCESS_DEVICE_TYPE', '门禁设备类型', 'ACCESS_CONTROL', '公共门禁', 1, 1, '2021-09-07 14:22:03', '2022-04-07 15:03:46', 0);
INSERT INTO `sys_dictionary` VALUES ('ACCESS_DEVICE_TYPE_2', NULL, 'ACCESS_DEVICE_TYPE', '门禁设备类型', 'APARTMENT_ACCESS_CONTROL', '公寓门禁', 8, 2, '2021-09-07 14:22:03', '2022-04-07 15:09:02', 0);
INSERT INTO `sys_dictionary` VALUES ('ACCESS_RESULT_1', NULL, 'ACCESS_RESULT', '车行通行结果', 'FAIL', '识别不通过', 0, 0, '2021-09-09 15:32:49', '2021-12-09 17:13:21', 0);
INSERT INTO `sys_dictionary` VALUES ('ACCESS_RESULT_2', NULL, 'ACCESS_RESULT', '车行通行结果', 'SUCCESS', '成功放行', 1, 1, '2021-09-09 15:32:49', '2021-12-09 17:13:24', 0);
INSERT INTO `sys_dictionary` VALUES ('ACTIVITY_STATUS_1', NULL, 'ACTIVITY_STATUS', '活动状态', 'NOT_EFFECT', '待生效', 1, 1, '2022-01-11 10:55:32', '2022-01-11 11:06:24', 0);
INSERT INTO `sys_dictionary` VALUES ('ACTIVITY_STATUS_2', NULL, 'ACTIVITY_STATUS', '活动状态', 'NOTICING', '预告中', 2, 2, '2022-01-11 10:55:32', '2022-01-11 11:06:31', 0);
INSERT INTO `sys_dictionary` VALUES ('ACTIVITY_STATUS_3', NULL, 'ACTIVITY_STATUS', '活动状态', 'ONGOING', '进行中', 3, 3, '2022-01-11 10:55:32', '2022-01-11 11:06:39', 0);
INSERT INTO `sys_dictionary` VALUES ('ACTIVITY_STATUS_4', NULL, 'ACTIVITY_STATUS', '活动状态', 'END', '已结束', 4, 4, '2022-01-11 10:55:32', '2022-01-11 11:06:43', 0);
INSERT INTO `sys_dictionary` VALUES ('ADD_DATA_SOURCE_1', NULL, 'ADD_DATA_SOURCE', '新增数据来源', 'ENTERPRISE_ADDED', '企业申请', 2, 2, '2022-04-01 16:12:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ADD_DATA_SOURCE_2', NULL, 'ADD_DATA_SOURCE', '新增数据来源', 'ADMIN_ADDED', '管理员录入', 1, 1, '2022-04-01 16:12:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ADVERTISEMENT_JUMP_TYPE_1', NULL, 'ADVERTISEMENT_JUMP_TYPE', '广告跳转类型', 'NOTHING', '无', 0, 5, '2022-01-04 10:11:24', '2022-05-31 14:06:27', 0);
INSERT INTO `sys_dictionary` VALUES ('ADVERTISEMENT_JUMP_TYPE_2', NULL, 'ADVERTISEMENT_JUMP_TYPE', '广告跳转类型', 'URL', 'URL', 1, 2, '2022-01-04 10:11:24', '2022-01-04 11:01:03', 0);
INSERT INTO `sys_dictionary` VALUES ('ADVERTISEMENT_JUMP_TYPE_3', NULL, 'ADVERTISEMENT_JUMP_TYPE', '广告跳转类型', 'MINI_PROGRAM', '小程序', 2, 3, '2022-01-04 10:11:24', '2022-01-04 11:01:04', 0);
INSERT INTO `sys_dictionary` VALUES ('ADVERTISEMENT_POSITION_1', NULL, 'ADVERTISEMENT_POSITION', '广告位置', 'CIRCULAR_BANNER', '首页轮播图', 1, 1, '2022-01-04 10:54:05', '2022-01-04 11:01:05', 0);
INSERT INTO `sys_dictionary` VALUES ('ADVERTISEMENT_STATUS_1', NULL, 'ADVERTISEMENT_STATUS', '广告状态', 'EFFECTIVELY', '生效中', 0, 2, '2022-01-04 10:25:08', '2022-01-04 11:00:56', 0);
INSERT INTO `sys_dictionary` VALUES ('ADVERTISEMENT_STATUS_2', NULL, 'ADVERTISEMENT_STATUS', '广告状态', 'TO_BE_EFFECTIVE', '待生效', 1, 1, '2022-01-04 10:25:08', '2022-01-04 11:00:58', 0);
INSERT INTO `sys_dictionary` VALUES ('ADVERTISEMENT_STATUS_3', NULL, 'ADVERTISEMENT_STATUS', '广告状态', 'OUT_OF_DATE', '已过期', 2, 3, '2022-01-04 10:25:08', '2022-01-04 11:00:44', 0);
INSERT INTO `sys_dictionary` VALUES ('AHEAD', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'AHEAD', '即将开始', 2, 2, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('ALLOW_VISITOR_1', NULL, 'ALLOW_VISITOR', '不适用', 'NOT_ALLOW', '不适用', 0, 0, '2021-09-10 14:42:48', '2021-12-09 17:11:39', 0);
INSERT INTO `sys_dictionary` VALUES ('ALLOW_VISITOR_2', NULL, 'ALLOW_VISITOR', '适用', 'ALLOW', '适用', 1, 1, '2021-09-10 14:43:05', '2021-12-09 17:11:37', 0);
INSERT INTO `sys_dictionary` VALUES ('APPLY_FOR_HOUSE_STATUS_1', NULL, 'APPLY_FOR_HOUSE_STATUS', '看房申请状态', 'WAIT_FOR_ACCEPT', '待受理', 1, 1, '2021-11-22 16:53:23', '2022-01-04 11:00:35', 0);
INSERT INTO `sys_dictionary` VALUES ('APPLY_FOR_HOUSE_STATUS_2', NULL, 'APPLY_FOR_HOUSE_STATUS', '看房申请状态', 'FINISH', '已完成', 2, 2, '2021-11-22 16:53:23', '2022-01-04 11:00:36', 0);
INSERT INTO `sys_dictionary` VALUES ('APPLY_FOR_HOUSE_STATUS_3', NULL, 'APPLY_FOR_HOUSE_STATUS', '看房申请状态', 'RETURNED', '已撤回', 3, 3, '2021-11-22 16:53:23', '2022-01-04 11:00:37', 0);
INSERT INTO `sys_dictionary` VALUES ('APRIL', NULL, 'MONTH', '月份', 'APRIL', '4月', 4, 4, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ATTENDANCE_RESULT_TYPE_1', NULL, 'ATTENDANCE_RESULT_TYPE', '考勤结果', 'NORMAL', '正常', 1, 1, '2022-01-24 14:44:48', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ATTENDANCE_RESULT_TYPE_2', NULL, 'ATTENDANCE_RESULT_TYPE', '考勤结果', 'EXCEPTION', '异常', 2, 2, '2022-01-24 14:44:48', '2022-01-24 14:45:13', 0);
INSERT INTO `sys_dictionary` VALUES ('ATTENDANCE_RULE_APPLY_TYPE_1', NULL, 'ATTENDANCE_RULE_APPLY_TYPE', '考勤规则关联类型', 'COMPANY', '适用全公司', 0, 0, '2022-01-20 14:54:05', '2022-02-09 14:24:35', 0);
INSERT INTO `sys_dictionary` VALUES ('ATTENDANCE_RULE_APPLY_TYPE_2', NULL, 'ATTENDANCE_RULE_APPLY_TYPE', '考勤规则关联类型', 'EMPLOYEE', '适用指定人员', 1, 1, '2022-01-20 14:54:05', '2022-02-09 14:24:43', 0);
INSERT INTO `sys_dictionary` VALUES ('ATTENDANCE_WORKDAY_TYPE_1', NULL, 'ATTENDANCE_WORKDAY_TYPE', '考勤规则关联工作日类型', 'LEGAL', '法定工作日', 0, 0, '2022-02-09 17:15:35', '2021-12-09 17:23:24', 0);
INSERT INTO `sys_dictionary` VALUES ('ATTENDANCE_WORKDAY_TYPE_2', NULL, 'ATTENDANCE_WORKDAY_TYPE', '考勤规则关联工作日类型', 'CUSTOM', '自定义', 1, 1, '2022-02-09 17:15:35', '2022-02-09 17:16:05', 0);
INSERT INTO `sys_dictionary` VALUES ('AUDIT_PERSON_TYPE_1', NULL, 'AUDIT_PERSON_TYPE', '审核人类型', 'INTERVIEWEE', '被访人', 1, 1, '2021-09-13 14:35:08', '2021-12-09 17:07:49', 0);
INSERT INTO `sys_dictionary` VALUES ('AUDIT_PERSON_TYPE_2', NULL, 'AUDIT_PERSON_TYPE', '审核人类型', 'ADMINISTRATION_STAFF', '行政人员', 2, 2, '2021-09-13 14:35:08', '2021-12-09 17:07:51', 0);
INSERT INTO `sys_dictionary` VALUES ('AUDIT_RESULT_1', NULL, 'AUDIT_RESULT', '审核结果', 'PASS', '通过', 1, 1, '2021-12-07 14:35:34', '2022-04-08 17:05:29', 0);
INSERT INTO `sys_dictionary` VALUES ('AUDIT_RESULT_2', NULL, 'AUDIT_RESULT', '审核结果', 'NOT_PASS', '不通过', 2, 2, '2021-12-07 14:35:34', '2022-04-08 17:05:29', 0);
INSERT INTO `sys_dictionary` VALUES ('AUGUST', NULL, 'MONTH', '月份', 'AUGUST', '8月', 8, 8, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('BENEFICIARY_1', NULL, 'BENEFICIARY', '收款方', 'HUAI_AN', '淮安智慧谷对公账户', 0, 0, '2021-09-07 17:13:39', '2021-12-09 17:23:24', 0);
INSERT INTO `sys_dictionary` VALUES ('BUILDING_PROPERTY_1', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'OFFICE', '办公', 1, 1, '2022-03-22 11:23:16', '2022-03-22 11:28:29', 0);
INSERT INTO `sys_dictionary` VALUES ('BUILDING_PROPERTY_2', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'CANTEEN', '食堂', 2, 2, '2022-03-22 11:23:16', '2022-03-22 11:28:36', 0);
INSERT INTO `sys_dictionary` VALUES ('BUILDING_PROPERTY_3', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'APARTMENT', '公寓', 3, 3, '2022-03-22 11:23:16', '2022-03-22 11:28:43', 0);
INSERT INTO `sys_dictionary` VALUES ('BUILDING_PROPERTY_4', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'OTHER', '其他', 4, 4, '2022-03-22 11:23:16', '2022-03-22 11:28:52', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_CHANCE_STAGE_1', NULL, 'BUSINESS_CHANCE_STAGE', '商机阶段', 'OPPORTUNITY_ASSESSMENT', '机会评估', 1, 1, '2021-09-10 16:34:08', '2022-01-04 11:00:23', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_CHANCE_STAGE_2', NULL, 'BUSINESS_CHANCE_STAGE', '商机阶段', 'REQUIREMENT_ANALY', '需求分析', 2, 2, '2021-09-10 16:34:08', '2022-01-04 11:00:24', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_CHANCE_STAGE_3', NULL, 'BUSINESS_CHANCE_STAGE', '商机阶段', 'OFFER_PRICE', '报价', 3, 3, '2021-09-10 16:34:08', '2022-01-04 11:00:25', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_CHANCE_STAGE_4', NULL, 'BUSINESS_CHANCE_STAGE', '商机阶段', 'NEGOTIATION', '谈判', 4, 4, '2021-09-10 16:34:08', '2022-01-04 11:00:26', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_CHANCE_STAGE_5', NULL, 'BUSINESS_CHANCE_STAGE', '商机阶段', 'REACH_INTENTION', '达成意向', 5, 5, '2021-09-10 16:34:08', '2022-01-04 11:00:28', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_CHANCE_STAGE_6', NULL, 'BUSINESS_CHANCE_STAGE', '商机阶段', 'SIGN_CONTRACT', '签订合同', 6, 6, '2021-09-10 16:34:08', '2022-01-04 11:00:29', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_CHANCE_STAGE_7', NULL, 'BUSINESS_CHANCE_STAGE', '商机阶段', 'WIN_ORDER_CLOSING', '赢单关闭', 7, 7, '2021-09-10 16:34:08', '2022-01-04 11:00:30', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_EFFECT_TYPE_0', NULL, 'EFFECT_TYPE', '招商类型', 'TIME_LIMITED_VALIDITY', '限时有效', 0, 1, '2021-09-30 10:22:03', '2022-01-04 10:59:55', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_EFFECT_TYPE_1', NULL, 'EFFECT_TYPE', '招商类型', 'LONG_TERM_EFFECTIVENESS', '长期有效', 1, 2, '2021-09-30 10:22:03', '2022-01-04 10:59:57', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_FOLLOW_TYPE_1', NULL, 'BUSINESS_FOLLOW_TYPE', '跟进类型', 'VISIT', '拜访', 1, 1, '2021-11-01 14:39:13', '2022-01-04 11:00:17', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_FOLLOW_TYPE_2', NULL, 'BUSINESS_FOLLOW_TYPE', '跟进类型', 'RECEPYTION', '接待', 2, 2, '2021-11-01 14:39:13', '2022-01-04 11:00:18', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_FOLLOW_TYPE_3', NULL, 'BUSINESS_FOLLOW_TYPE', '跟进类型', 'SEE', '参观', 3, 3, '2021-11-01 14:39:13', '2022-01-04 11:00:19', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_HOUSE_AREA_0', NULL, 'BUSINESS_HOUSE_AREA', '房源招商的房屋面积', 'UNLIMITED', '不限', 0, 1, '2021-11-18 17:06:42', '2022-01-04 16:28:48', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_HOUSE_AREA_1', NULL, 'BUSINESS_HOUSE_AREA', '房源招商的房屋面积', 'LESS_THAN_100_SQUARE_METRES', '100㎡以下', 1, 1, '2021-11-18 17:06:42', '2022-01-04 11:00:12', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_HOUSE_AREA_2', NULL, 'BUSINESS_HOUSE_AREA', '房源招商的房屋面积', '100-500_SQUARE_METRES', '100-500㎡', 2, 2, '2021-11-18 17:06:42', '2022-01-04 11:00:08', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_HOUSE_AREA_3', NULL, 'BUSINESS_HOUSE_AREA', '房源招商的房屋面积', '500-1000_SQUARE_METRES', '500-1000㎡', 3, 3, '2021-11-18 17:06:42', '2022-01-04 11:00:09', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_HOUSE_AREA_4', NULL, 'BUSINESS_HOUSE_AREA', '房源招商的房屋面积', 'OVER_1000_SQUARE_METRES', '1000㎡以上', 4, 4, '2021-11-18 17:06:42', '2022-01-04 11:00:10', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_SOURCE_1', NULL, 'BUSINESS_SOURCE', '商机来源', 'BUSINESS_SOURCE-1', '商机录入', 1, 1, '2022-01-06 16:20:58', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_SOURCE_2', NULL, 'BUSINESS_SOURCE', '商机来源', 'BUSINESS_SOURCE-2', '看房申请', 2, 2, '2022-01-06 16:21:38', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_STATUS_0', NULL, 'BUSINESS_STATUS', '招商状态', 'NOT_ON_THE_SHELF', '未上架', 0, 1, '2021-09-30 10:22:03', '2022-01-04 10:59:52', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_STATUS_1', NULL, 'BUSINESS_STATUS', '招商状态', 'ATTRACTING_INVESTMENT', '招商中', 1, 2, '2021-09-30 10:22:03', '2022-01-04 10:59:52', 0);
INSERT INTO `sys_dictionary` VALUES ('BUSINESS_STATUS_2', NULL, 'BUSINESS_STATUS', '招商状态', 'OFF_THE_SHELF', '已下架', 2, 3, '2021-09-30 10:22:03', '2022-01-04 10:59:54', 0);
INSERT INTO `sys_dictionary` VALUES ('CANCEL_ORDER_REASON_1', NULL, 'CANCELORDERREASON', '撤单原因', 'RESOLVED', '问题已自行解决', 1, 1, '2021-09-30 10:22:03', '2022-01-04 10:59:46', 0);
INSERT INTO `sys_dictionary` VALUES ('CANCEL_ORDER_REASON_2', NULL, 'CANCELORDERREASON', '撤单原因', 'NOTHISPROBLEM', '误会了，没有这个问题', 2, 2, '2021-09-30 10:22:03', '2022-01-04 10:59:47', 0);
INSERT INTO `sys_dictionary` VALUES ('CANCEL_ORDER_REASON_3', NULL, 'CANCELORDERREASON', '撤单原因', 'SOMEONEPROCESSED', '已有人现场处理', 3, 3, '2021-09-30 10:22:03', '2022-01-04 10:59:48', 0);
INSERT INTO `sys_dictionary` VALUES ('CANCEL_ORDER_REASON_4', NULL, 'CANCELORDERREASON', '撤单原因', 'OTHERREASON', '其他原因', 4, 4, '2021-09-30 10:22:03', '2022-01-04 10:59:51', 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_MEDIUM_1', NULL, 'CARD_MEDIUM', '卡介质', 'IC_OR_ID', 'IC/ID卡', 1, 1, '2022-04-07 15:21:53', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_STATUS_1', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_1', '已绑定', 1, 1, '2022-04-07 16:15:19', '2022-04-07 16:16:57', 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_STATUS_2', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_2', '已挂失', 2, 2, '2022-04-07 16:15:27', '2022-04-07 16:17:01', 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_STATUS_3', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_3', '已注销', 3, 3, '2022-04-07 16:15:27', '2022-04-07 16:17:05', 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_STATUS_4', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_4', '已解绑', 4, 4, '2022-04-07 16:15:27', '2022-04-07 16:17:09', 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_TYPE_1', NULL, 'CARD_TYPE', '卡类型', 'COMMON_CARD', '普通卡', 1, 1, '2022-04-07 15:22:52', '2022-04-07 16:18:34', 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_TYPE_2', NULL, 'CARD_TYPE', '卡类型', 'ADMIN_CARD', '管理员卡', 2, 2, '2022-04-07 15:22:52', '2022-04-07 16:19:04', 0);
INSERT INTO `sys_dictionary` VALUES ('CARD_TYPE_3', NULL, 'CARD_TYPE', '卡类型', 'OTHER', '其他', 3, 3, '2022-04-07 15:22:52', '2022-04-07 16:19:14', 0);
INSERT INTO `sys_dictionary` VALUES ('CAR_STATUS_1', NULL, 'CAR_STATUS', '车辆状态', 'NOT_ACTIVE', '待生效', 0, 0, '2021-09-08 16:29:03', '2022-01-13 14:09:54', 0);
INSERT INTO `sys_dictionary` VALUES ('CAR_STATUS_2', NULL, 'CAR_STATUS', '车辆状态', 'ACTIVATING', '生效中', 1, 1, '2021-09-08 16:29:03', '2022-01-13 14:09:54', 0);
INSERT INTO `sys_dictionary` VALUES ('CAR_STATUS_3', NULL, 'CAR_STATUS', '车辆状态', 'INVALIDATION', '已失效', 2, 2, '2021-09-08 16:29:03', '2022-01-13 14:09:54', 0);
INSERT INTO `sys_dictionary` VALUES ('CAR_TYPE_1', NULL, 'CAR_TYPE', '车辆类型', 'EMPLOYEE_CAR', '员工车辆', 1, 1, '2021-09-09 11:34:05', '2021-12-09 17:14:17', 0);
INSERT INTO `sys_dictionary` VALUES ('CAR_TYPE_2', NULL, 'CAR_TYPE', '车辆类型', 'OFFICIAL_CAR', '公务车辆', 2, 2, '2021-09-09 11:34:05', '2021-12-09 17:14:20', 0);
INSERT INTO `sys_dictionary` VALUES ('CAR_TYPE_3', NULL, 'CAR_TYPE', '车辆类型', 'EXTERNAL_CAR', '外部车辆', 3, 3, '2021-09-09 11:34:05', '2021-12-09 17:14:23', 0);
INSERT INTO `sys_dictionary` VALUES ('CAR_TYPE_4', NULL, 'CAR_TYPE', '车辆类型', 'RED_LIST_CAR', '红名单', 4, 4, '2021-09-09 11:34:05', '2021-12-09 17:14:25', 0);
INSERT INTO `sys_dictionary` VALUES ('CHANCE_SOURCE_1', NULL, 'BUSINESS_CHANCE_SOURCE', '商机来源', 'BUSINESS_CHANCE_INPUT', '商机录入', 1, 1, '2021-09-30 10:22:03', '2022-01-04 10:59:40', 0);
INSERT INTO `sys_dictionary` VALUES ('CHANCE_SOURCE_2', NULL, 'BUSINESS_CHANCE_SOURCE', '商机来源', 'APPLY_FOR_SETTLED', '入驻申请', 2, 2, '2021-09-30 10:22:03', '2022-01-04 10:59:41', 0);
INSERT INTO `sys_dictionary` VALUES ('CHARGE_TYPE_0', NULL, 'CHARGE_TYPE', '押付方式', 'CHARGE_TYPE_0', '押一付一', 0, 1, '2021-09-30 10:22:03', '2022-01-04 10:59:32', 0);
INSERT INTO `sys_dictionary` VALUES ('CHARGE_TYPE_1', NULL, 'CHARGE_TYPE', '押付方式', 'CHARGE_TYPE_1', '押二付一', 1, 2, '2021-09-30 10:22:03', '2022-01-04 10:59:36', 0);
INSERT INTO `sys_dictionary` VALUES ('CHARGE_TYPE_2', NULL, 'CHARGE_TYPE', '押付方式', 'CHARGE_TYPE_2', '押一付三', 2, 3, '2021-09-30 10:22:03', '2022-01-04 10:59:37', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_BIND_CLIENT_TYPE_1', NULL, 'COMPANY_BIND_CLIENT_TYPE', '入驻企业绑定客户端类型', 'UNBIND', '无链接', 0, 0, '2021-09-10 11:05:11', '2021-12-09 17:12:57', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_BIND_CLIENT_TYPE_2', NULL, 'COMPANY_BIND_CLIENT_TYPE', '入驻企业绑定客户端类型', 'APPID', '小程序', 1, 1, '2021-09-10 11:05:11', '2022-06-17 10:54:30', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_BIND_CLIENT_TYPE_3', NULL, 'COMPANY_BIND_CLIENT_TYPE', '入驻企业绑定客户端类型', 'H5', 'h5链接', 2, 2, '2021-09-10 11:05:11', '2022-06-17 10:54:13', 1);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_MANAGEMENT_STATUS_1', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'IN_INDUSTRY', '在业', 1, 1, '2021-12-08 15:53:42', '2022-01-04 10:59:02', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_MANAGEMENT_STATUS_2', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'SUBSIST', '存续', 2, 2, '2021-12-08 15:53:42', '2022-01-04 10:59:03', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_MANAGEMENT_STATUS_3', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'CANCELLATION', '注销', 3, 3, '2021-12-08 15:53:42', '2022-01-04 10:59:04', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_MANAGEMENT_STATUS_4', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'OTHER', '其他', 4, 4, '2021-12-08 15:53:42', '2022-01-04 10:59:05', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TAXES_TYPE_1', NULL, 'COMPANY_TAXES_TYPE', '企业纳税类型', 'FIRST_CLASS_TAXPAYERS', '一般纳税人', 1, 1, '2021-12-08 15:52:22', '2022-03-02 10:14:31', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TAXES_TYPE_2', NULL, 'COMPANY_TAXES_TYPE', '企业纳税类型', 'SMALL_SCALE_TAXPAYER', '小规模纳税人', 2, 2, '2021-12-08 15:52:22', '2022-01-04 10:59:08', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TYPE_1', NULL, 'COMPANY_TYPE', '企业类型', 'PROPERTY_COMPANY', '物业公司', 0, 4, '2022-04-01 16:12:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TYPE_2', NULL, 'COMPANY_TYPE', '企业类型', 'ORDINARY_COMPANY', '其他', 1, 5, '2022-04-01 16:12:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TYPE_3', NULL, 'COMPANY_TYPE', '企业类型', 'RESEARCH_INSTITUTES', '科研院所', 2, 0, '2022-04-01 16:12:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TYPE_4', NULL, 'COMPANY_TYPE', '企业类型', 'BUSINESS_UNIT', '事业单位', 3, 1, '2022-04-01 16:12:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TYPE_5', NULL, 'COMPANY_TYPE', '企业类型', 'ORGANS', '机关单位', 4, 2, '2022-04-01 16:12:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('COMPANY_TYPE_6', NULL, 'COMPANY_TYPE', '企业类型', 'TECHNOLOGY_ENTERPRISE', '科技型企业', 5, 3, '2022-04-01 16:12:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_1', NULL, 'ORDERTYPE-1', '投诉具体类型', 'ENVIRONMENT', '环境', 11, 1, '2021-09-30 10:22:03', '2022-01-04 10:59:11', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_10', NULL, 'ORDERTYPE-1', '投诉具体类型', 'OTHER', '其他', 20, 10, '2021-09-30 10:22:03', '2022-01-04 10:59:24', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_2', NULL, 'ORDERTYPE-1', '投诉具体类型', 'SAFETY', '安全', 12, 2, '2021-09-30 10:22:03', '2022-01-04 10:59:12', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_3', NULL, 'ORDERTYPE-1', '投诉具体类型', 'ORDER', '秩序', 13, 3, '2021-09-30 10:22:03', '2022-01-04 10:59:13', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_4', NULL, 'ORDERTYPE-1', '投诉具体类型', 'STAFF', '工作人员', 14, 4, '2021-09-30 10:22:03', '2022-01-04 10:59:14', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_5', NULL, 'ORDERTYPE-1', '投诉具体类型', 'DECVICE', '设备设施', 15, 5, '2021-09-30 10:22:03', '2022-01-04 10:59:16', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_6', NULL, 'ORDERTYPE-1', '投诉具体类型', 'WATER', '供水', 16, 6, '2021-09-30 10:22:03', '2022-01-04 10:59:17', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_7', NULL, 'ORDERTYPE-1', '投诉具体类型', 'ELECTRICITY', '供电', 17, 7, '2021-09-30 10:22:03', '2022-01-04 10:59:18', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_8', NULL, 'ORDERTYPE-1', '投诉具体类型', 'FIRE_FIGHTING', '消防', 18, 8, '2021-09-30 10:22:03', '2022-01-04 10:59:19', 0);
INSERT INTO `sys_dictionary` VALUES ('COMPLAINT_TYPE_9', NULL, 'ORDERTYPE-1', '投诉具体类型', 'CONVENIENCE_FOR_PEOPLE', '便民服务', 19, 9, '2021-09-30 10:22:03', '2022-01-04 10:59:21', 0);
INSERT INTO `sys_dictionary` VALUES ('config_1', NULL, '3D_MENU_URL_TYPE', '3d大屏的菜单链接ip+端口', 'MENU_URL', 'http://39.101.150.104:3000', 1, 1, '2022-06-24 17:20:15', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('config_2', NULL, '3D_MENU_URL_TYPE', '3d大屏的菜单获取token的ip+端口', 'TOKEN_URL', 'http://39.101.150.104:8080', 2, 2, '2022-06-24 17:20:15', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('config_3', NULL, '3D_MENU_URL_TYPE', '3d大屏获取token用户名', 'UserName', 'TKUser', 3, 3, '2022-06-24 17:20:15', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('config_4', NULL, '3D_MENU_URL_TYPE', '3d大屏获取token密码', 'PassWord', '123456', 4, 4, '2022-06-24 17:20:15', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('config_5', NULL, '3D_MENU_URL_TYPE', '淮安智慧谷', 'parkOrgId', '1541335941202771970', 5, 5, '2022-06-30 16:19:08', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_ATTRIBUTES_1', NULL, 'CONTRACT_ATTRIBUTES', '合同属性', 'NEW_CONTRACT', '新签合同', 1, 1, '2021-12-07 14:29:04', '2021-12-09 16:56:52', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_ATTRIBUTES_2', NULL, 'CONTRACT_ATTRIBUTES', '合同属性', 'RENEW_CONTRACT', '续签合同', 2, 2, '2021-12-07 14:29:04', '2021-12-09 16:56:54', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_ATTRIBUTES_3', NULL, 'CONTRACT_ATTRIBUTES', '合同属性', 'CHANGE_CONTRACT', '变更合同', 3, 3, '2021-12-07 14:29:04', '2021-12-09 16:56:56', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_ATTRIBUTES_4', NULL, 'CONTRACT_ATTRIBUTES', '合同属性', 'WITHDRAWAL_CONTRACT', '退约合同', 4, 4, '2021-12-07 14:29:04', '2021-12-09 16:56:58', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_ATTRIBUTES_5', NULL, 'CONTRACT_ATTRIBUTES', '合同属性', 'HISTORICAL_CONTRACT', '历史合同', 5, 5, '2021-12-07 14:29:04', '2021-12-09 16:57:00', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_CHANGE_TYPE_1', NULL, 'CONTRACT_CHANGE_TYPE', '合同变更类型', 'SUBJECT_CHANGE', '主体变更', 1, 1, '2021-12-09 15:52:39', '2021-12-09 16:51:53', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_CHANGE_TYPE_2', NULL, 'CONTRACT_CHANGE_TYPE', '合同变更类型', 'LEASE_ADJUSTMENT', '租期调整', 2, 2, '2021-12-09 15:52:39', '2021-12-09 16:51:57', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_CHANGE_TYPE_3', NULL, 'CONTRACT_CHANGE_TYPE', '合同变更类型', 'ASSET_CHANGES', '资产变更', 3, 3, '2021-12-09 15:52:39', '2021-12-09 16:52:00', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_CHANGE_TYPE_4', NULL, 'CONTRACT_CHANGE_TYPE', '合同变更类型', 'BILLING_CHANGES', '计费变更', 4, 4, '2021-12-09 15:52:39', '2021-12-09 16:52:15', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_1', NULL, 'CONTRACT_OPERATE', '合同操作', 'EDIT', '编辑', 1, 1, '2021-12-07 14:36:59', '2021-12-09 16:55:41', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_2', NULL, 'CONTRACT_OPERATE', '合同操作', 'DELETE', '删除', 2, 2, '2021-12-07 14:36:59', '2021-12-09 16:55:45', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_3', NULL, 'CONTRACT_OPERATE', '合同操作', 'NULLIFY', '作废', 3, 3, '2021-12-07 14:36:59', '2021-12-09 16:55:49', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_4', NULL, 'CONTRACT_OPERATE', '合同操作', 'INITIATE_REVIEW', '发起审核', 4, 4, '2021-12-07 14:36:59', '2021-12-09 16:55:52', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_5', NULL, 'CONTRACT_OPERATE', '合同操作', 'CHANGE', '变更', 5, 5, '2021-12-07 14:36:59', '2021-12-09 16:55:59', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_6', NULL, 'CONTRACT_OPERATE', '合同操作', 'EXTEND', '续约', 6, 6, '2021-12-07 14:36:59', '2021-12-09 16:56:02', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_7', NULL, 'CONTRACT_OPERATE', '合同操作', 'WITHDRAW', '退约', 7, 7, '2021-12-07 14:36:59', '2021-12-09 16:56:05', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_8', NULL, 'CONTRACT_OPERATE', '合同操作', 'TERMINATION', '终止', 8, 8, '2021-12-07 14:36:59', '2021-12-09 16:56:08', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_OPERATE_9', NULL, 'CONTRACT_OPERATE', '合同操作', 'AUDIT', '审核', 8, 8, '2021-12-07 14:36:59', '2021-12-09 16:56:08', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_1', NULL, 'CONTRACT_STATE', '合同状态', 'TO_BE_SUBMITTED', '待提交', 1, 1, '2021-12-07 14:41:14', '2021-12-09 16:53:16', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_2', NULL, 'CONTRACT_STATE', '合同状态', 'UNDER_REVIEW', '审核中', 2, 2, '2021-12-07 14:41:14', '2021-12-09 16:53:19', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_3', NULL, 'CONTRACT_STATE', '合同状态', 'TO_BE_EFFECTIVE', '待生效', 3, 3, '2021-12-07 14:41:14', '2021-12-09 16:53:22', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_4', NULL, 'CONTRACT_STATE', '合同状态', 'IN_EFFECT', '生效中', 4, 4, '2021-12-07 14:41:14', '2021-12-09 16:53:35', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_5', NULL, 'CONTRACT_STATE', '合同状态', 'WITHDRAWN', '已退约', 5, 5, '2021-12-07 14:41:14', '2021-12-09 16:53:41', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_6', NULL, 'CONTRACT_STATE', '合同状态', 'INVALIDATION', '已失效', 6, 6, '2021-12-07 14:41:14', '2021-12-09 16:53:45', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_7', NULL, 'CONTRACT_STATE', '合同状态', 'ABOLISHED', '已作废', 7, 7, '2021-12-07 14:41:14', '2021-12-09 16:53:51', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_8', NULL, 'CONTRACT_STATE', '合同状态', 'EXPIRED', '已过期', 8, 8, '2021-12-07 14:41:14', '2021-12-09 16:53:55', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_STATE_9', NULL, 'CONTRACT_STATE', '合同状态', 'UNDER_REVIEW_WITHDRAW', '审核中', 9, 9, '2021-12-07 14:41:14', '2022-04-11 13:56:45', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_TYPE_1', NULL, 'CONTRACT_TYPE', '合同类型', 'ADMISSION_AGREEMENT', '入园协议', 1, 1, '2021-12-07 14:35:34', '2022-04-08 17:05:19', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_TYPE_2', NULL, 'CONTRACT_TYPE', '合同类型', 'INVITATION_BID', '招引合同', 2, 2, '2021-12-07 14:35:34', '2022-04-08 17:05:15', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_TYPE_3', NULL, 'CONTRACT_TYPE', '合同类型', 'RENTAL_CONTRACT', '租赁合同', 3, 3, '2021-12-07 14:35:34', '2022-05-13 16:40:02', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_TYPE_4', NULL, 'CONTRACT_TYPE', '合同类型', 'PROPERTY_CONTRACT', '物业合同', 4, 4, '2021-12-07 14:35:34', '2022-04-08 17:05:24', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_TYPE_5', NULL, 'CONTRACT_TYPE', '合同类型', 'APARTMENT_CONTRACT', '公寓合同', 5, 5, '2021-12-07 14:35:34', '2022-04-08 17:05:29', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_TYPE_FOR_HOUSE_3', NULL, 'CONTRACT_TYPE_FOR_HOUSE', '房源状态查询合同类型', 'RENTAL_CONTRACT', '租赁合同', 3, 3, '2021-12-07 14:35:34', '2022-05-06 16:19:07', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRACT_TYPE_FOR_HOUSE_5', NULL, 'CONTRACT_TYPE_FOR_HOUSE', '房源状态查询合同类型', 'APARTMENT_CONTRACT', '公寓合同', 5, 5, '2021-12-07 14:35:34', '2022-04-08 17:05:29', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRIBUTE_1', NULL, 'CONTRIBUTE', '缴纳情况', 'UNPAID', '未缴纳', 0, 0, '2021-09-09 15:32:49', '2022-03-17 15:54:35', 0);
INSERT INTO `sys_dictionary` VALUES ('CONTRIBUTE_2', NULL, 'CONTRIBUTE', '缴纳情况', 'PAID', '已缴纳', 1, 1, '2021-09-09 15:32:49', '2022-03-17 15:54:39', 0);
INSERT INTO `sys_dictionary` VALUES ('CON_LIBRARY_TYPE_1', NULL, 'CON_LIBRARY_TYPE', '模板字段类别', 'CONTRACT_INFO', '合同信息', 1, 1, '2021-12-07 14:29:04', '2021-12-09 16:56:52', 0);
INSERT INTO `sys_dictionary` VALUES ('CON_LIBRARY_TYPE_2', NULL, 'CON_LIBRARY_TYPE', '模板字段类别', 'USER_INFO', '用户信息', 2, 2, '2021-12-07 14:29:04', '2021-12-09 16:56:54', 0);
INSERT INTO `sys_dictionary` VALUES ('CON_LIBRARY_TYPE_3', NULL, 'CON_LIBRARY_TYPE', '模板字段类别', 'HOUSE_INFO', '关联房屋信息', 3, 3, '2021-12-07 14:29:04', '2021-12-09 16:56:56', 0);
INSERT INTO `sys_dictionary` VALUES ('CON_LIBRARY_TYPE_4', NULL, 'CON_LIBRARY_TYPE', '模板字段类别', 'BILLING_INFO', '计费信息', 4, 4, '2021-12-07 14:29:04', '2021-12-09 16:56:58', 0);
INSERT INTO `sys_dictionary` VALUES ('CUSTOMERINFO_STATUS_1', NULL, 'CUSTOMERINFO_STATUS', '客户状态', 'DEALED', '已成交', 1, 1, '2021-12-08 15:40:40', '2022-01-04 10:58:39', 0);
INSERT INTO `sys_dictionary` VALUES ('CUSTOMERINFO_STATUS_2', NULL, 'CUSTOMERINFO_STATUS', '客户状态', 'NO_DEAL', '未成交', 2, 2, '2021-12-08 15:40:40', '2022-01-04 10:58:40', 0);
INSERT INTO `sys_dictionary` VALUES ('CUSTOMERINFO_STATUS_3', NULL, 'CUSTOMERINFO_STATUS', '客户状态', 'HISTORICAL_TRANSACTION', '历史成交', 3, 3, '2021-12-08 15:40:40', '2022-01-04 10:58:41', 0);
INSERT INTO `sys_dictionary` VALUES ('CUSTOMERINFO_TYPE_1', NULL, 'CUSTOMERINFO_TYPE', '客户类型', 'PROPERTY_COMPANY', '物业公司', 0, 1, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `sys_dictionary` VALUES ('CUSTOMERINFO_TYPE_2', NULL, 'CUSTOMERINFO_TYPE', '客户类型', 'ORDINARY_COMPANY', '普通企业', 1, 2, '2021-12-08 15:40:40', '2022-01-04 10:58:51', 0);
INSERT INTO `sys_dictionary` VALUES ('CUS_MEETING_AUDIT_1', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'AUDIT_RENDING', '待审核', 1, 1, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('CUS_MEETING_AUDIT_2', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'AUDIT_PASS', '审核通过', 2, 2, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('CUS_MEETING_AUDIT_3', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'AUDIT_FAIL', '审核不通过', 3, 3, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('CUS_MEETING_AUDIT_4', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'CANCEL', '已取消', 4, 4, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('CYCLE_TYPE_DAY', NULL, 'CYCLE_TYPE', '执行周期类型', 'DAY', '天', 0, 1, '2021-09-30 10:22:03', '2022-01-04 11:03:07', 0);
INSERT INTO `sys_dictionary` VALUES ('CYCLE_TYPE_WEEK', NULL, 'CYCLE_TYPE', '执行周期类型', 'WEEK', '周', 1, 2, '2021-09-30 10:22:03', '2022-01-04 11:03:08', 0);
INSERT INTO `sys_dictionary` VALUES ('DECEMBER', NULL, 'MONTH', '月份', 'DECEMBER', '12月', 12, 12, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_STATUS_1', NULL, 'DEVICE_STATUS', '离线', 'OFFLINE', '离线', 0, 0, '2021-09-10 14:38:29', '2021-12-09 17:12:03', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_STATUS_2', NULL, 'DEVICE_STATUS', '在线', 'ONLINE', '在线', 1, 1, '2021-09-10 14:38:55', '2021-12-09 17:12:00', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_1', NULL, 'DEVICE_TYPE', '设备类型', 'ACCESS_CONTROL', '公共门禁', 1, 1, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_2', NULL, 'DEVICE_TYPE', '设备类型', 'APARTMENT_ACCESS_CONTROL', '公寓门禁', 2, 2, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_3', NULL, 'DEVICE_TYPE', '设备类型', 'BARRIER_ALL_IN_ONE', '道闸一体机', 3, 3, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_4', NULL, 'DEVICE_TYPE', '设备类型', 'VIDEO_MONITORING', '视频监控', 4, 4, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_5', NULL, 'DEVICE_TYPE', '设备类型', 'WATER_METER', '水表', 5, 5, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_6', NULL, 'DEVICE_TYPE', '设备类型', 'ELECTRICITY_METER', '电表', 6, 6, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_7', NULL, 'DEVICE_TYPE', '设备类型', 'MEETING_SCREEN', '会议室信息屏', 7, 7, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_8', NULL, 'DEVICE_TYPE', '设备类型', 'SMOKE_SENSING', '消防设备-烟感设备', 8, 8, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('DEVICE_TYPE_9', NULL, 'DEVICE_TYPE', '设备类型', 'LIQUID_WATER_LEVEL', '消防设备-液位设备', 9, 9, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('ELECTRICITY_CATEGORY', NULL, 'ENERGY_CONSUMPTION_CATEGORY', '能耗类别', 'ELECTRICITY_CATEGORY', '电能耗', 6, 2, '2022-06-15 17:23:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ELECTRICITY_METER_EFFECT_TYPE_1', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别', 'ELECTRICITY_METER_EFFECT_TYPE_1', '室内用电', 1, 1, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ELECTRICITY_METER_EFFECT_TYPE_2', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别', 'ELECTRICITY_METER_EFFECT_TYPE_2', '空调', 2, 2, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ELECTRICITY_METER_EFFECT_TYPE_3', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别', 'ELECTRICITY_METER_EFFECT_TYPE_3', '公共照明', 3, 3, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ELECTRICITY_METER_EFFECT_TYPE_4', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别', 'ELECTRICITY_METER_EFFECT_TYPE_4', '消防照明', 4, 4, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ELECTRICITY_METER_EFFECT_TYPE_5', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别', 'ELECTRICITY_METER_EFFECT_TYPE_5', '新风机', 5, 5, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ELECTRICITY_METER_EFFECT_TYPE_6', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别', 'ELECTRICITY_METER_EFFECT_TYPE_6', '其他', 6, 6, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('ENTERPRISE_PUBLISH_STATUS_1', NULL, 'ENTERPRISE_PUBLISH_STATUS', '企业服务状态', 'TO_BE_PUBLISHED', '待发布', 0, 0, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `sys_dictionary` VALUES ('ENTERPRISE_PUBLISH_STATUS_2', NULL, 'ENTERPRISE_PUBLISH_STATUS', '企业服务状态', 'PUBLISHED', '已发布', 1, 1, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `sys_dictionary` VALUES ('ENTERPRISE_PUBLISH_STATUS_3', NULL, 'ENTERPRISE_PUBLISH_STATUS', '企业服务状态', 'DISABLE', '禁用', 2, 2, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `sys_dictionary` VALUES ('ENTERPRISE_SIZE_1', NULL, 'ENTERPRISE_SIZE', '企业规模', 'MEDIUM_ENTERPRISE', '中型企业', 0, 0, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `sys_dictionary` VALUES ('ENTERPRISE_SIZE_2', NULL, 'ENTERPRISE_SIZE', '企业规模', 'SMALL_COMPANIES', '小型企业', 1, 1, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `sys_dictionary` VALUES ('ENTERPRISE_SIZE_3', NULL, 'ENTERPRISE_SIZE', '企业规模', 'MICRO_ENTERPRISES', '微型企业', 2, 2, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `sys_dictionary` VALUES ('ENTERPRISE_SIZE_4', NULL, 'ENTERPRISE_SIZE', '企业规模', 'MEDIUM_AND_SMALL_SIZED_ENTERPRISES', '中小微型企业', 3, 3, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `sys_dictionary` VALUES ('EQUIPMENT_LEDGER_RESULT_1', NULL, 'LEDGER_STATE', '设备台账状态', 'NORMAL', '正常', 1, 1, '2021-09-09 15:32:49', '2022-03-22 17:43:27', 0);
INSERT INTO `sys_dictionary` VALUES ('EQUIPMENT_LEDGER_RESULT_2', NULL, 'LEDGER_STATE', '设备台账状态', 'SCRAPED', '已报废', 0, 0, '2021-09-09 15:32:49', '2022-03-22 17:43:31', 0);
INSERT INTO `sys_dictionary` VALUES ('FEBRUARY', NULL, 'MONTH', '月份', 'FEBRUARY', '2月', 2, 2, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILLING_CYCLE_1', NULL, 'FEE_BILLING_CYCLE', '计费周期', 'ONE_TIME', '一次性', 1, 1, '2021-12-07 14:44:38', '2021-12-09 16:53:02', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILLING_CYCLE_2', NULL, 'FEE_BILLING_CYCLE', '计费周期', 'MONTHLY', '月', 2, 2, '2021-12-07 14:44:38', '2021-12-09 16:53:07', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILLING_CYCLE_3', NULL, 'FEE_BILLING_CYCLE', '计费周期', 'QUARTERLY', '季', 3, 3, '2021-12-07 14:44:38', '2021-12-09 16:53:10', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILLING_CYCLE_4', NULL, 'FEE_BILLING_CYCLE', '计费周期', 'YEARLY', '年', 4, 4, '2021-12-07 14:44:38', '2021-12-09 16:53:13', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILL_TYPE_0', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'ALL_BILL', '全部', 0, 0, '2022-06-16 09:41:28', '2022-06-16 09:44:54', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILL_TYPE_1', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'COMPANY_METER_BILL', '企业水电费用', 1, 4, '2022-06-16 09:41:04', '2022-06-16 09:45:58', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILL_TYPE_2', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'APARTMENT_METER_BILL', '公寓水电费用', 2, 5, '2022-06-16 09:41:28', '2022-06-16 09:46:01', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILL_TYPE_3', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'RENTAL_BILL', '租赁费用', 3, 1, '2022-06-16 09:41:28', '2022-06-16 09:45:50', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILL_TYPE_4', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'PROPERTY_BILL', '物业费用', 4, 2, '2022-06-16 09:41:28', '2022-06-16 09:45:52', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_BILL_TYPE_5', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'APARTMENT_BILL', '公寓租金', 5, 3, '2022-06-16 09:41:28', '2022-06-16 09:45:55', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CALCULATION_STANDARD-1', NULL, 'FEE_CALCULATION_STANDARD', '计算标准', 'BUILDING_AREA_MULTIPLY_MONTHLY_UNIT_PRICE', '建筑面积*月单价', 1, 1, '2021-12-14 10:57:30', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CALCULATION_STANDARD-2', NULL, 'FEE_CALCULATION_STANDARD', '计算标准', 'COVER_AREA_MULTIPLY_MONTHLY_UNIT_PRICE', '套内面积*月单价', 2, 2, '2021-12-14 10:57:30', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CALCULATION_STANDARD-3', NULL, 'FEE_CALCULATION_STANDARD', '计算标准', 'DOSAGE_MULTIPLY_UNIT_PRICE', '用量*单价', 3, 4, '2021-12-14 10:57:30', '2022-04-15 16:04:00', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CALCULATION_STANDARD-4', NULL, 'FEE_CALCULATION_STANDARD', '计算标准', 'PRICE_PER_DAY', '天单价', 4, 5, '2021-12-14 10:57:30', '2022-04-15 16:04:00', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CALCULATION_STANDARD-5', NULL, 'FEE_CALCULATION_STANDARD', '计算标准', 'NUMBER_OF_ROOMS_MULTIPLY_UNIT_PRICE', '房间数*单价', 5, 3, '2021-12-14 10:57:30', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CARRY_MODE_1', NULL, 'FEE_CARRY_MODE', '进位方式', 'ROUND_HALF_UP', '四舍五入', 4, 1, '2021-12-07 14:47:39', '2021-12-14 14:45:10', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CARRY_MODE_2', NULL, 'FEE_CARRY_MODE', '进位方式', 'ROUND_UP', '向上取整', 0, 2, '2021-12-07 14:47:39', '2021-12-14 14:53:51', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CARRY_MODE_3', NULL, 'FEE_CARRY_MODE', '进位方式', 'ROUND_DOWN', '向下取整', 1, 3, '2021-12-07 14:47:39', '2021-12-14 14:45:13', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CHARGE_TYPE_1', NULL, 'FEE_CHARGE_TYPE', '收费类型', 'PREPAID', '预付费', 1, 1, '2021-12-07 14:49:30', '2021-12-09 16:52:31', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CHARGE_TYPE_2', NULL, 'FEE_CHARGE_TYPE', '收费类型', 'POSTPAID', '后付费', 2, 2, '2021-12-07 14:49:30', '2021-12-09 16:52:34', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CHARGING_CYCLE_1', NULL, 'FEE_CHARGING_CYCLE', '收费周期', 'CONSISTENT_WITH_THE_BILLING_CYCLE', '与计费周期一致', 1, 1, '2021-12-07 14:50:11', '2021-12-09 16:52:24', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_CHARGING_CYCLE_2', NULL, 'FEE_CHARGING_CYCLE', '收费周期', 'MULTIPLE_BILLING_CYCLES', '多计费周期', 2, 2, '2021-12-07 14:50:11', '2021-12-09 16:52:28', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_FORMULA_TYPE_1', NULL, 'FEE_FORMULA_TYPE', '公式类型', 'FIXED_AMOUNT', '固定金额', 1, 1, '2021-12-07 14:48:36', '2021-12-09 16:52:38', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_FORMULA_TYPE_2', NULL, 'FEE_FORMULA_TYPE', '公式类型', 'CALCULATION_FORMULA', '计算公式', 2, 2, '2021-12-07 14:48:36', '2021-12-09 16:52:41', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_RESERVED_DIGITS_1', NULL, 'FEE_RESERVED_DIGITS', '保留位数', 'ROUNDING', '取整数', 0, 1, '2021-12-07 14:46:32', '2022-05-05 14:24:47', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_RESERVED_DIGITS_2', NULL, 'FEE_RESERVED_DIGITS', '保留位数', 'KEEP_ONE_DECIMAL_PLACE', '保留一位小数', 1, 2, '2021-12-07 14:46:32', '2022-05-05 14:24:47', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_RESERVED_DIGITS_3', NULL, 'FEE_RESERVED_DIGITS', '保留位数', 'KEEP_TWO_DECIMAL_PLACES', '保留两位小数', 2, 3, '2021-12-07 14:46:32', '2022-05-05 14:24:47', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-0', NULL, 'FEE_TYPE', '费用类型', 'RENT', '租金', 0, 0, '2021-09-10 16:34:08', '2021-12-07 15:37:08', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-1', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'HOUSE_RENT', '房屋租金', 1, 1, '2021-09-10 16:34:08', '2021-12-07 15:37:15', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-10', 'FEE_TYPE-8', 'FEE_TYPE', '费用类型', 'WATER_METER', '水表', 10, 10, '2021-09-10 16:34:08', '2021-12-07 15:37:19', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-13', NULL, 'FEE_TYPE', '费用类型', 'OTHERS', '其他', 13, 13, '2021-09-10 16:34:08', '2021-12-07 15:37:28', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-14', 'FEE_TYPE-13', 'FEE_TYPE', '费用类型', 'ADVERTISING_EXPENSES', '广告费', 14, 14, '2021-09-10 16:34:08', '2021-12-07 15:37:33', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-15', 'FEE_TYPE-13', 'FEE_TYPE', '费用类型', 'PARKING_EXPENCES', '停车费', 15, 15, '2021-09-10 16:34:08', '2021-12-07 15:37:38', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-16', 'FEE_TYPE-13', 'FEE_TYPE', '费用类型', 'DECORATION_EXPENCES', '装修费', 16, 16, '2021-09-10 16:34:08', '2021-12-07 15:37:43', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-17', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'APARTMENT_RENT_SINGLE', '公寓租金-单间', 17, 17, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-18', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'APARTMENT_RENT_TWIN', '公寓租金-标间', 18, 18, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-19', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'APARTMENT_RENT_SUITE', '公寓租金-套间', 19, 19, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-2', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'APARTMENT_RENT', '公寓租金', 2, 2, '2021-09-10 16:34:08', '2022-05-05 14:24:46', 1);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-20', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'DEPOSIT_SINGLE', '公寓押金-单间', 20, 20, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-21', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'DEPOSIT_TWIN', '公寓押金-标间', 21, 21, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-22', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'DEPOSIT_SUITE', '公寓押金-套间', 22, 22, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-3', NULL, 'FEE_TYPE', '费用类型', 'PROPERTY', '物业', 3, 3, '2021-09-10 16:34:08', '2021-12-07 15:37:45', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-4', 'FEE_TYPE-3', 'FEE_TYPE', '费用类型', 'PROPERTY_FEE', '物业费', 4, 4, '2021-09-10 16:34:08', '2021-12-07 15:37:47', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-5', NULL, 'FEE_TYPE', '费用类型', 'DEPOSIT', '押金', 5, 5, '2021-09-10 16:34:08', '2021-12-07 15:37:49', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-6', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'FRONT_MONEY', '定金', 6, 6, '2021-09-10 16:34:08', '2022-05-05 14:24:47', 1);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-7', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'PROMISED_MONEY', '履约保证金', 7, 7, '2021-09-10 16:34:08', '2022-03-04 19:55:52', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-8', NULL, 'FEE_TYPE', '费用类型', 'METER_READING', '抄表', 8, 8, '2021-09-10 16:34:08', '2021-12-07 15:37:59', 0);
INSERT INTO `sys_dictionary` VALUES ('FEE_TYPE-9', 'FEE_TYPE-8', 'FEE_TYPE', '费用类型', 'ELECTRICITY_METER', '电表', 9, 9, '2021-09-10 16:34:08', '2021-12-07 15:38:03', 0);
INSERT INTO `sys_dictionary` VALUES ('FIELDWORK_APPLY_STATE_1', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'TO_BE_AUDITED', '待审批', 1, 1, '2022-02-21 14:41:14', '2022-02-21 16:53:55', 0);
INSERT INTO `sys_dictionary` VALUES ('FIELDWORK_APPLY_STATE_2', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'APPROVE', '审核通过', 2, 2, '2022-02-21 14:41:14', '2022-02-21 14:54:18', 0);
INSERT INTO `sys_dictionary` VALUES ('FIELDWORK_APPLY_STATE_3', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'REJECTED', '审核不通过', 3, 3, '2022-02-21 14:41:14', '2022-02-21 14:58:43', 0);
INSERT INTO `sys_dictionary` VALUES ('FIELDWORK_APPLY_STATE_4', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'CANCEL', '已取消', 4, 4, '2022-02-21 14:41:14', '2022-02-21 14:59:15', 0);
INSERT INTO `sys_dictionary` VALUES ('FILE_DEVICE_WARN_16', NULL, 'FILE_DEVICE_WARN_TYPE', '消防报警', 'FILE_DEVICE_WARN_16', '火警报警', 16, 2, '2022-06-17 14:16:54', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FILE_DEVICE_WARN_20', NULL, 'FILE_DEVICE_WARN_TYPE', '消防报警', 'FILE_DEVICE_WARN_20', '可燃气体监管报警', 20, 3, '2022-06-17 14:16:54', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FILE_DEVICE_WARN_32', NULL, 'FILE_DEVICE_WARN_TYPE', '消防报警', 'FILE_DEVICE_WARN_32', '设备故障', 32, 4, '2022-06-17 14:16:54', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FLOOR_TYPE_0', NULL, 'FLOOR_TYPE', '楼层属性', 'FLOOR_TYPE_0', '平层', 0, 1, '2021-09-30 10:22:03', '2022-01-04 10:58:26', 0);
INSERT INTO `sys_dictionary` VALUES ('FLOOR_TYPE_1', NULL, 'FLOOR_TYPE', '楼层属性', 'FLOOR_TYPE_1', '低层', 1, 2, '2021-09-30 10:22:03', '2022-01-04 10:58:27', 0);
INSERT INTO `sys_dictionary` VALUES ('FLOOR_TYPE_2', NULL, 'FLOOR_TYPE', '楼层属性', 'FLOOR_TYPE_2', '中层', 2, 3, '2021-09-30 10:22:03', '2022-01-04 10:58:28', 0);
INSERT INTO `sys_dictionary` VALUES ('FLOOR_TYPE_3', NULL, 'FLOOR_TYPE', '楼层属性', 'FLOOR_TYPE_3', '高层', 3, 4, '2021-09-30 10:22:03', '2022-01-04 10:58:29', 0);
INSERT INTO `sys_dictionary` VALUES ('FORM_CYCLE_1', NULL, 'FORM_CYCLE', '统计周期', 'FORM_CYCLE_1', '每日', 1, 1, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FORM_CYCLE_2', NULL, 'FORM_CYCLE', '统计周期', 'FORM_CYCLE_2', '每周', 2, 2, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FORM_CYCLE_3', NULL, 'FORM_CYCLE', '统计周期', 'FORM_CYCLE_3', '每月', 3, 3, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FORM_CYCLE_4', NULL, 'FORM_CYCLE', '统计周期', 'FORM_CYCLE_4', '自定义', 4, 4, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FORM_STATUS_1', NULL, 'FORM_STATUS', '表单状态', 'FORM_STATUS_1', '待发布', 1, 1, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FORM_STATUS_2', NULL, 'FORM_STATUS', '表单状态', 'FORM_STATUS_2', '收集中', 2, 2, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FORM_STATUS_3', NULL, 'FORM_STATUS', '表单状态', 'FORM_STATUS_3', '已结束', 3, 3, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('FREE', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'FREE', '空闲中', 1, 1, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('GATEWAY_TYPE_1', NULL, 'GATEWAY_TYPE', '入口出口类型', 'ENTRANCE', '入口', 0, 0, '2021-09-09 15:29:31', '2021-12-09 17:13:35', 0);
INSERT INTO `sys_dictionary` VALUES ('GATEWAY_TYPE_2', NULL, 'GATEWAY_TYPE', '入口出口类型', 'EXIT', '出口', 1, 1, '2021-09-09 15:29:31', '2021-12-09 17:13:39', 0);
INSERT INTO `sys_dictionary` VALUES ('GOODS_RELEASE_STATUS_1', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'NOT_CHECK', '待审核', 1, 1, '2022-01-24 15:08:28', '2022-01-24 15:12:00', 0);
INSERT INTO `sys_dictionary` VALUES ('GOODS_RELEASE_STATUS_2', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'CHECK_PASS', '审核通过', 2, 2, '2022-01-24 15:08:28', '2022-01-24 15:12:15', 0);
INSERT INTO `sys_dictionary` VALUES ('GOODS_RELEASE_STATUS_3', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'CHECK_REJECT', '审核不通过', 3, 3, '2022-01-24 15:08:28', '2022-01-24 15:12:16', 0);
INSERT INTO `sys_dictionary` VALUES ('GOODS_RELEASE_STATUS_4', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'CANCEL', '已取消', 4, 4, '2022-01-24 15:08:28', '2022-01-24 15:12:22', 0);
INSERT INTO `sys_dictionary` VALUES ('HAVE', NULL, 'HAVE_OR_NOT_HAVE', '有或无', 'HAVE', '有', 1, 1, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HEALTH_CODE_1', NULL, 'HEALTH_CODE', '健康码', 'GREEN', '绿色', 0, 0, '2022-04-06 13:59:24', '2022-04-06 13:59:39', 0);
INSERT INTO `sys_dictionary` VALUES ('HEALTH_CODE_2', NULL, 'HEALTH_CODE', '健康码', 'YELLOW', '黄色', 1, 1, '2022-04-06 13:59:24', '2022-04-06 14:00:21', 0);
INSERT INTO `sys_dictionary` VALUES ('HEALTH_CODE_3', NULL, 'HEALTH_CODE', '健康码', 'RED', '红色', 2, 2, '2022-04-06 13:59:24', '2022-04-06 14:00:23', 0);
INSERT INTO `sys_dictionary` VALUES ('HIGHLIGHT_0', NULL, 'HIGHLIGHT', '亮点描述', 'HIGHLIGHT_0', '智慧办公', 0, 1, '2021-09-30 10:22:03', '2022-01-04 10:58:17', 0);
INSERT INTO `sys_dictionary` VALUES ('HIGHLIGHT_1', NULL, 'HIGHLIGHT', '亮点描述', 'HIGHLIGHT_1', '限时入住', 1, 2, '2021-09-30 10:22:03', '2022-01-04 10:58:18', 0);
INSERT INTO `sys_dictionary` VALUES ('HIGHLIGHT_2', NULL, 'HIGHLIGHT', '亮点描述', 'HIGHLIGHT_2', '配套齐全', 2, 3, '2021-09-30 10:22:03', '2022-01-04 10:58:19', 0);
INSERT INTO `sys_dictionary` VALUES ('HIGHLIGHT_3', NULL, 'HIGHLIGHT', '亮点描述', 'HIGHLIGHT_3', '交通便利', 3, 4, '2021-09-30 10:22:03', '2022-01-04 10:58:20', 0);
INSERT INTO `sys_dictionary` VALUES ('HIGHLIGHT_4', NULL, 'HIGHLIGHT', '亮点描述', 'HIGHLIGHT_4', '临近商圈', 4, 5, '2021-09-30 10:22:03', '2022-01-04 10:58:21', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_FUNCTION_0', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_0', '办公', 0, 1, '2022-04-01 16:11:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_FUNCTION_1', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_1', '商铺', 1, 2, '2022-04-01 16:11:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_FUNCTION_2', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_2', '公寓-单间', 2, 3, '2022-04-01 16:11:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_FUNCTION_3', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_3', '公寓-标间', 3, 4, '2022-04-01 16:11:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_FUNCTION_4', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_4', '公寓-套间', 4, 5, '2022-04-01 16:11:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_FUNCTION_5', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_5', '仓库', 5, 6, '2022-04-01 16:11:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_FUNCTION_6', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_6', '其他', 6, 7, '2022-04-01 16:11:28', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-1', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'EAST', '东', 1, 1, '2021-11-25 15:58:39', '2022-01-04 10:57:34', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-2', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'SOUTH', '南', 2, 2, '2021-11-25 15:58:39', '2022-01-04 10:57:34', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-3', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'WEST', '西', 3, 3, '2021-11-25 15:58:39', '2022-01-04 10:57:35', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-4', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'NORTH', '北', 4, 4, '2021-11-25 15:58:39', '2022-01-04 10:57:36', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-5', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'SOUTHEAST', '东南', 5, 5, '2021-11-25 15:58:39', '2022-01-04 10:57:37', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-6', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'NORTHEAST', '东北', 6, 6, '2021-11-25 15:58:39', '2022-01-04 10:57:38', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-7', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'SOUTHWEST', '西南', 7, 7, '2021-11-25 15:58:39', '2022-01-04 10:57:39', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_ORIENTATION-8', NULL, 'HOUSE_ORIENTATION', '房屋朝向', 'NORTHWEST', '西北', 8, 8, '2021-11-25 15:58:39', '2022-01-04 10:57:41', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_PROPERTY_0', NULL, 'HOUSE_PROPERTY', '房屋属性', 'HOUSE_PROPERTY_0', '自用', 0, 1, '2021-09-30 10:22:03', '2022-01-04 10:57:43', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_PROPERTY_1', NULL, 'HOUSE_PROPERTY', '房屋属性', 'HOUSE_PROPERTY_1', '租赁', 1, 2, '2021-09-30 10:22:03', '2022-01-04 10:57:44', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_PROPERTY_2', NULL, 'HOUSE_PROPERTY', '房屋属性', 'HOUSE_PROPERTY_2', '销售', 2, 3, '2021-09-30 10:22:03', '2022-01-04 10:57:45', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_RELATION_TYPE_1', NULL, 'HOUSE_RELATION_TYPE', '房屋关联类型', 'CONTRACT', '合同关联房屋', 1, 1, '2021-12-07 14:40:15', '2021-12-09 16:54:10', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_RELATION_TYPE_2', NULL, 'HOUSE_RELATION_TYPE', '房屋关联类型', 'FEE_BILLING', '计费信息应用房源', 2, 2, '2021-12-07 14:40:15', '2021-12-09 16:54:22', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_STATUS_0', NULL, 'HOUSE_STATUS', '房屋状态', 'FOR_SELF', '自用', 0, 1, '2021-09-30 10:22:03', '2022-01-04 10:57:22', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_STATUS_1', NULL, 'HOUSE_STATUS', '房屋状态', 'RENTING', '待租', 1, 2, '2021-09-30 10:22:03', '2022-01-04 10:57:23', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_STATUS_2', NULL, 'HOUSE_STATUS', '房屋状态', 'RENTED', '已租', 2, 3, '2021-09-30 10:22:03', '2022-01-04 10:57:23', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_STATUS_3', NULL, 'HOUSE_STATUS', '房屋状态', 'SELLING', '待售', 3, 4, '2021-09-30 10:22:03', '2022-01-05 15:41:44', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_STATUS_4', NULL, 'HOUSE_STATUS', '房屋状态', 'SOLD', '已售', 4, 5, '2021-09-30 10:22:03', '2022-01-04 10:57:26', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_UNIT_RENT_1', NULL, 'HOUSE_UNIT_RENT', '房源招商的房屋单位租金', 'HouseUnitRent-1', '不限', 0, 1, '2021-11-18 17:19:51', '2022-02-16 16:24:55', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_UNIT_RENT_2', NULL, 'HOUSE_UNIT_RENT', '房源招商的房屋单位租金', 'HouseUnitRent-2', '50元/㎡/月以下', 1, 2, '2021-11-18 17:19:51', '2022-02-16 16:24:58', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_UNIT_RENT_3', NULL, 'HOUSE_UNIT_RENT', '房源招商的房屋单位租金', 'HouseUnitRent-3', '50-100元/㎡/月', 2, 3, '2021-11-18 17:19:51', '2022-02-16 16:24:59', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_UNIT_RENT_4', NULL, 'HOUSE_UNIT_RENT', '房源招商的房屋单位租金', 'HouseUnitRent-4', '100-200元/㎡/月', 3, 4, '2021-11-18 17:19:51', '2022-02-16 16:25:00', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_UNIT_RENT_5', NULL, 'HOUSE_UNIT_RENT', '房源招商的房屋单位租金', 'HouseUnitRent-5', '200-500元/㎡/月', 4, 5, '2021-11-18 17:19:51', '2022-02-16 16:25:05', 0);
INSERT INTO `sys_dictionary` VALUES ('HOUSE_UNIT_RENT_6', NULL, 'HOUSE_UNIT_RENT', '房源招商的房屋单位租金', 'HouseUnitRent-6', '500元/㎡/月以上', 5, 6, '2021-11-18 17:19:51', '2022-02-16 16:25:07', 0);
INSERT INTO `sys_dictionary` VALUES ('INDUSTRY_FIELD_1', NULL, 'INDUSTRY_FIELD', '行业领域', 'INFORMATION_SOFTWARE', '信息软件', 1, 1, '2021-12-08 15:50:27', '2022-01-04 10:57:10', 0);
INSERT INTO `sys_dictionary` VALUES ('INDUSTRY_FIELD_2', NULL, 'INDUSTRY_FIELD', '行业领域', 'COMMUNICATIONS_TECHNOLOGY', '通信技术', 2, 2, '2021-12-08 15:50:27', '2022-01-04 10:57:10', 0);
INSERT INTO `sys_dictionary` VALUES ('INDUSTRY_FIELD_3', NULL, 'INDUSTRY_FIELD', '行业领域', 'BIOMEDICINE', '生物医药', 3, 3, '2021-12-08 15:50:27', '2022-01-04 10:57:11', 0);
INSERT INTO `sys_dictionary` VALUES ('INDUSTRY_FIELD_4', NULL, 'INDUSTRY_FIELD', '行业领域', 'MEDICAL_APPARATUS_AND_INSTRUMENTS', '医疗器械', 4, 4, '2021-12-08 15:50:27', '2022-01-04 10:57:12', 0);
INSERT INTO `sys_dictionary` VALUES ('INDUSTRY_FIELD_5', NULL, 'INDUSTRY_FIELD', '行业领域', 'FINANCIAL_SERVICE', '金融服务', 5, 5, '2021-12-08 15:50:27', '2022-01-04 10:57:14', 0);
INSERT INTO `sys_dictionary` VALUES ('INDUSTRY_FIELD_6', NULL, 'INDUSTRY_FIELD', '行业领域', 'NEW_ENERGY_AND_ENVIRONMENTAL_PROTECTION', '新能源环保', 6, 6, '2021-12-08 15:50:27', '2022-01-04 10:57:15', 0);
INSERT INTO `sys_dictionary` VALUES ('INDUSTRY_FIELD_7', NULL, 'INDUSTRY_FIELD', '行业领域', 'OTHER', '其他', 7, 6, '2021-12-08 15:50:27', '2022-01-04 10:57:16', 0);
INSERT INTO `sys_dictionary` VALUES ('INFOMATION_STATUS_1', NULL, 'INFOMATION_STATUS', '资讯状态', 'UN_EFFECT', '待生效', 0, 1, '2021-09-30 10:22:03', '2022-03-14 16:28:00', 0);
INSERT INTO `sys_dictionary` VALUES ('INFOMATION_STATUS_2', NULL, 'INFOMATION_STATUS', '资讯状态', 'EFFECTING', '已生效', 1, 2, '2021-09-30 10:22:03', '2022-03-14 16:28:04', 0);
INSERT INTO `sys_dictionary` VALUES ('INFOMATION_TYPE_1', NULL, 'INFOMATION_TYPE', '资讯具体类型', 'NEWS', '新闻资讯', 1, 1, '2021-09-30 10:22:03', '2022-03-10 13:52:07', 0);
INSERT INTO `sys_dictionary` VALUES ('INFOMATION_TYPE_2', NULL, 'INFOMATION_TYPE', '资讯具体类型', 'PREFERENTIAL_POLICIES', '优惠政策', 2, 2, '2021-09-30 10:22:03', '2022-03-10 13:52:12', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_POINT_STATUS_1', NULL, 'INSPECTION_POINT_STATUS', '巡检点状态', 'INSPECTION_POINT_STATUS-1', '待巡检', 1, 1, '2021-09-10 16:34:08', '2022-01-04 10:56:41', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_POINT_STATUS_2', NULL, 'INSPECTION_POINT_STATUS', '巡检点状态', 'INSPECTION_POINT_STATUS-2', '巡检中', 2, 2, '2021-09-10 16:34:08', '2022-01-04 10:56:42', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_POINT_STATUS_3', NULL, 'INSPECTION_POINT_STATUS', '巡检点状态', 'INSPECTION_POINT_STATUS-3', '已完成', 3, 3, '2021-09-10 16:34:08', '2022-01-04 10:56:43', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_POINT_STATUS_4', NULL, 'INSPECTION_POINT_STATUS', '巡检点状态', 'INSPECTION_POINT_STATUS-4', '未完成', 4, 4, '2021-09-10 16:34:08', '2022-01-04 10:56:47', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_POINT_STATUS_5', NULL, 'INSPECTION_POINT_STATUS', '巡检点状态', 'INSPECTION_POINT_STATUS-5', '超时完成', 5, 5, '2021-09-10 16:34:08', '2022-01-04 10:56:50', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_TASK_STATUS_1', NULL, 'INSPECTION_TASK_STATUS', '巡检状态', 'INSPECTION_TASK_STATUS-0', '待执行', 1, 1, '2021-09-10 16:34:08', '2022-01-04 10:56:53', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_TASK_STATUS_2', NULL, 'INSPECTION_TASK_STATUS', '巡检状态', 'INSPECTION_TASK_STATUS-1', '巡检中', 2, 2, '2021-09-10 16:34:08', '2022-01-04 10:56:54', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_TASK_STATUS_3', NULL, 'INSPECTION_TASK_STATUS', '巡检状态', 'INSPECTION_TASK_STATUS-2', '已结束', 3, 3, '2021-09-10 16:34:08', '2022-01-04 10:56:55', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_TYPE_0', NULL, 'INSPECTION_TYPE', '巡检类型', 'EQUIPMENT', '设备', 1, 1, '2021-09-30 10:22:03', '2022-01-04 10:56:28', 0);
INSERT INTO `sys_dictionary` VALUES ('INSPECTION_TYPE_1', NULL, 'INSPECTION_TYPE', '巡检类型', 'ENVIRONMENT', '环境', 2, 2, '2021-09-30 10:22:03', '2022-01-04 10:56:31', 0);
INSERT INTO `sys_dictionary` VALUES ('INVITE_PROCESS_STATUS_1', NULL, 'INVITE_PROCESS_STATUS', '邀请访客流程状态', 'INVITING', '邀请中', 0, 0, '2021-09-13 14:32:49', '2021-12-15 17:09:49', 0);
INSERT INTO `sys_dictionary` VALUES ('INVITE_PROCESS_STATUS_2', NULL, 'INVITE_PROCESS_STATUS', '邀请访客流程状态', 'INVITING_SUCCESS', '邀请成功', 1, 1, '2021-09-13 14:32:49', '2021-12-15 17:09:52', 0);
INSERT INTO `sys_dictionary` VALUES ('JANUARY', NULL, 'MONTH', '月份', 'JANUARY', '1月', 1, 1, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_1', NULL, 'JOBCODE', '岗位', 'ADMINISTRATOR', '管理员', 1, NULL, '2021-09-30 10:22:03', '2021-09-30 10:22:22', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_10', NULL, 'JOBCODE', '岗位', 'BUSINESSMAN', '招商人员', 10, NULL, '2021-09-30 10:22:03', '2021-09-30 11:14:07', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_11', NULL, 'JOBCODE', '岗位', 'INSPECTIONMAN', '巡检人员', 11, NULL, '2021-09-30 10:22:03', '2022-01-04 10:19:41', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_12', NULL, 'JOBCODE', '岗位', 'GOODS_RELEASE_CHECK', '物品放行审核员', 12, 12, '2021-09-30 10:22:03', '2022-01-26 18:15:47', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_13', NULL, 'JOBCODE', '岗位', 'MEETING_CHECK', '会议室管理员', 13, 13, '2022-02-21 15:26:27', '2022-02-21 16:17:55', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_15', NULL, 'JOBCODE', '岗位', 'TRANSFER_MAN', '转办员', 15, 15, '2022-02-21 15:26:27', '2022-02-21 16:17:55', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_2', NULL, 'JOBCODE', '岗位', 'REPAIR', '维修', 2, NULL, '2021-09-30 10:22:03', '2021-09-30 11:13:43', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_3', NULL, 'JOBCODE', '岗位', 'CUSTOMER_SERVICE', '客服', 3, NULL, '2021-09-30 10:22:03', '2021-09-30 11:13:48', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_4', NULL, 'JOBCODE', '岗位', 'CLEANER', '保洁', 4, NULL, '2021-09-30 10:22:03', '2021-09-30 11:13:51', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_5', NULL, 'JOBCODE', '岗位', 'SECURITY', '保安', 5, NULL, '2021-09-30 10:22:03', '2021-09-30 11:13:56', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_6', NULL, 'JOBCODE', '岗位', 'GREEN', '绿化', 6, NULL, '2021-09-30 10:22:03', '2021-09-30 11:13:59', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_7', NULL, 'JOBCODE', '岗位', 'FINANCE', '财务', 7, NULL, '2021-09-30 10:22:03', '2021-09-30 11:14:01', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_8', NULL, 'JOBCODE', '岗位', 'ADMINISTRATION', '行政', 8, NULL, '2021-09-30 10:22:03', '2021-09-30 11:14:04', 0);
INSERT INTO `sys_dictionary` VALUES ('JOB_CODE_9', NULL, 'JOBCODE', '岗位', 'MANAGER', '经理', 9, NULL, '2021-09-30 10:22:03', '2021-09-30 11:14:07', 0);
INSERT INTO `sys_dictionary` VALUES ('JULY', NULL, 'MONTH', '月份', 'JULY', '7月', 7, 7, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('JUMP_TYPE_1', NULL, 'JUMP_TYPE', '跳转类型', 'NOTHING', '无', 0, 3, '2022-01-04 10:11:24', '2022-05-31 14:06:27', 0);
INSERT INTO `sys_dictionary` VALUES ('JUMP_TYPE_2', NULL, 'JUMP_TYPE', '跳转类型', 'URL', 'URL', 1, 1, '2022-01-04 10:11:24', '2022-06-22 11:20:22', 0);
INSERT INTO `sys_dictionary` VALUES ('JUMP_TYPE_3', NULL, 'JUMP_TYPE', '跳转类型', 'MINI_PROGRAM', '小程序', 2, 2, '2022-01-04 10:11:24', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('JUNE', NULL, 'MONTH', '月份', 'JUNE', '6月', 6, 6, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MARCH', NULL, 'MONTH', '月份', 'MARCH', '3月', 3, 3, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MAY', NULL, 'MONTH', '月份', 'MAY', '5月', 5, 5, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_AUDIT_1', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_1', '预定待审核', 1, 1, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_AUDIT_2', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_2', '预定成功', 2, 2, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_AUDIT_3', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_3', '候补待审核', 3, 3, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_AUDIT_4', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_4', '候补中', 4, 4, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_AUDIT_5', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_5', '手动取消', 5, 5, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_AUDIT_6', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_6', '超时取消', 6, 6, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_AUDIT_7', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_7', '审核失败', 7, 7, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_DEVICE_TYPE_1', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'VIDEO_CAMERA', '摄像机', 1, 1, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_DEVICE_TYPE_2', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'TELECONFERENCE_EQUIPMENT', '远程会议设备', 2, 2, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_DEVICE_TYPE_3', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'PROJECTOR', '投影仪', 3, 3, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_DEVICE_TYPE_4', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'ELECTRONIC_WHITEBOARD', '电子白板', 4, 4, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_GOODS_TYPE_1', NULL, 'MEETING_GOODS_TYPE', '会议室物资类型', 'PURCHASE', '购买型', 1, 1, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_GOODS_TYPE_2', NULL, 'MEETING_GOODS_TYPE', '会议室物资类型', 'RENTAL', '租用型', 2, 2, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_RESERVE_1', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'RESERVE_NO', '不可预定', 1, 1, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_RESERVE_2', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'RESERVE_YES', '可预订', 2, 2, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_RESERVE_3', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'RESERVE_OVER', '已预定', 3, 3, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_RESERVE_4', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'CANDIDATE_YES', '可候补', 4, 4, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_RESERVE_TIME_1', NULL, 'MEETING_RESERVE_TIME', '会议室可预定时段', 'NOON', '上午', 1, 1, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_RESERVE_TIME_2', NULL, 'MEETING_RESERVE_TIME', '会议室可预定时段', 'AFTERNOON', '下午', 2, 2, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEETING_RESERVE_TIME_3', NULL, 'MEETING_RESERVE_TIME', '会议室可预定时段', 'EVENING', '晚上', 3, 3, '2022-03-18 16:00:32', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_EQUIPMENT_1', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'VIDEO_CAMERA', '摄像机', 1, 1, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_EQUIPMENT_2', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'WEB_CONFERENCE', '远程会议设备', 2, 2, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_EQUIPMENT_3', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'DVR', 'DVR', 3, 3, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_EQUIPMENT_4', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'TELPHONE', '电话', 4, 4, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_EQUIPMENT_5', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'PROJECTOR', '投影仪', 5, 5, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_EQUIPMENT_6', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'ELECTRONIC_WHITEBOARD', '电子白板', 6, 6, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_RELATION_TYPE_1', NULL, 'MEE_RELATION_TYPE', '会议室权限类型', 'COMPANY', '公司', 1, 1, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_RELATION_TYPE_2', NULL, 'MEE_RELATION_TYPE', '会议室权限类型', 'EMPLOYEE', '员工', 2, 2, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_ROOM_TYPE_1', NULL, 'MEETING_ROOM_TYPE', '会议类型', 'ORDINARY_MEETING', '普通会议', 1, 1, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('MEE_ROOM_TYPE_2', NULL, 'MEETING_ROOM_TYPE', '会议类型', 'VIDEO_MEETING', '视频会议', 2, 2, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_BILL_FEE_TYPE_COMPANY_1', NULL, 'METER_BILL_FEE_TYPE_COMPANY', '企业端水电费用账单类型', 'COMPANY', '企业水电费用', 3, 1, '2022-05-23 10:33:16', '2022-05-23 10:45:12', 0);
INSERT INTO `sys_dictionary` VALUES ('METER_BILL_FEE_TYPE_COMPANY_2', NULL, 'METER_BILL_FEE_TYPE_COMPANY', '企业端水电费用账单类型', 'APARTMENT', '公寓水电费用', 5, 2, '2022-05-23 10:33:16', '2022-05-23 10:45:15', 0);
INSERT INTO `sys_dictionary` VALUES ('METER_EFFECT_PROPERTY_1', NULL, 'METER_EFFECT_PROPERTY', '表计属性', 'METER_EFFECT_PROPERTY_1', '自用表计', 1, 1, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_EFFECT_PROPERTY_2', NULL, 'METER_EFFECT_PROPERTY', '表计属性', 'METER_EFFECT_PROPERTY_2', '公摊表计', 2, 3, '2022-03-04 21:04:11', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_EFFECT_PROPERTY_3', NULL, 'METER_EFFECT_PROPERTY', '表计属性', 'METER_EFFECT_PROPERTY_3', '总分表计', 3, 3, '2022-03-04 21:04:11', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_EFFECT_PROPERTY_4', NULL, 'METER_EFFECT_PROPERTY', '表计属性', 'METER_EFFECT_PROPERTY_4', '总表计', 4, 4, '2022-03-04 21:04:11', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_STATUS_1', NULL, 'METER_STATUS', '仪表状态', 'USING', '已开启', 1, 1, '2022-03-08 11:26:15', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('METER_STATUS_2', NULL, 'METER_STATUS', '仪表状态', 'STOP_USING', '已停用', 2, 2, '2022-03-08 11:28:24', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('METER_STATUS_3', NULL, 'METER_STATUS', '仪表状态', 'DISCARD', '已报废', 3, 3, '2022-03-08 11:28:24', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('METER_TYPE_1', NULL, 'METER_TYPE', '仪表类型', 'GENERAL_METER', '普通表', 1, 1, '2022-03-04 19:59:23', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_TYPE_2', NULL, 'METER_TYPE', '仪表类型', 'INTELLIGENT_METER', '智能表', 2, 2, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_TYPE_READING_METHOD_1', NULL, 'METER_TYPE_READING_METHOD', '抄表方式', 'MANUAL_READING', '手动抄表', 1, 1, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('METER_TYPE_READING_METHOD_2', NULL, 'METER_TYPE_READING_METHOD', '抄表方式', 'AUTO_READING', '自动抄表', 2, 2, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_STATUS_1', NULL, 'NOTICE_STATUS', '公告状态', 'UN_EFFECT', '待生效', 0, 1, '2021-09-30 10:22:03', '2022-03-14 17:01:54', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_STATUS_2', NULL, 'NOTICE_STATUS', '公告状态', 'EFFECTING', '生效中', 1, 2, '2021-09-30 10:22:03', '2022-03-14 17:02:24', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_STATUS_3', NULL, 'NOTICE_STATUS', '公告状态', 'OUT_OF_DATE', '已过期', 2, 3, '2021-09-30 10:22:03', '2022-03-14 17:02:29', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-1', NULL, 'NOTICE_TYPE', '公告具体类型', 'MAINTENANCE_NOTCIFICATION', '维修通知', 1, 1, '2021-09-30 10:22:03', '2022-03-10 13:53:59', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-2', NULL, 'NOTICE_TYPE', '公告具体类型', 'NO_WATER_POWER', '停水停电', 2, 2, '2021-09-30 10:22:03', '2022-03-10 13:54:06', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-3', NULL, 'NOTICE_TYPE', '公告具体类型', 'SECURITY_REMINDER', '安全提醒', 3, 3, '2021-09-30 10:22:03', '2022-03-10 13:54:09', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-4', NULL, 'NOTICE_TYPE', '公告具体类型', 'PUBLIC_SERVICE', '公益服务', 4, 4, '2021-09-30 10:22:03', '2022-03-10 13:54:11', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-5', NULL, 'NOTICE_TYPE', '公告具体类型', 'WARM_PROMPT', '温馨提示', 5, 5, '2021-09-30 10:22:03', '2022-03-10 13:54:13', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-6', NULL, 'NOTICE_TYPE', '公告具体类型', 'EMERGENCY_BROADCAST', '应急广播', 6, 6, '2021-09-30 10:22:03', '2022-03-10 13:54:15', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-7', NULL, 'NOTICE_TYPE', '公告具体类型', 'CHARAGE_NOTICE', '收费通知', 7, 7, '2021-09-30 10:22:03', '2022-03-10 13:54:17', 0);
INSERT INTO `sys_dictionary` VALUES ('NOTICE_TYPE-8', NULL, 'NOTICE_TYPE', '公告具体类型', 'OTHER', '其他', 8, 8, '2021-09-30 10:22:03', '2022-03-10 13:54:28', 0);
INSERT INTO `sys_dictionary` VALUES ('NOT_HAVE', NULL, 'HAVE_OR_NOT_HAVE', '有或无', 'NOT_HAVE', '无', 0, 0, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('NOVEMBER', NULL, 'MONTH', '月份', 'NOVEMBER', '11月', 11, 11, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('OCTOBER', NULL, 'MONTH', '月份', 'OCTOBER', '10月', 10, 10, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('OFFLINE', NULL, 'SCREEN_DEVICE_STATUS', '会议室信息屏状态', 'OFFLINE', '离线', 0, 0, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('ORDER_STATE_1', NULL, 'ORDER_STATE', '报事报修状态', 'WAIT_ASSIGN', '待派单', 1, 1, '2022-03-22 16:51:53', '2022-03-22 17:41:05', 0);
INSERT INTO `sys_dictionary` VALUES ('ORDER_STATE_2', NULL, 'ORDER_STATE', '报事报修状态', 'WAIT_HANDLE', '待处理', 2, 2, '2022-03-22 16:51:53', '2022-03-22 17:41:13', 0);
INSERT INTO `sys_dictionary` VALUES ('ORDER_STATE_3', NULL, 'ORDER_STATE', '报事报修状态', 'FINISH', '已完成', 8, 4, '2022-03-22 16:51:53', '2022-03-22 17:41:23', 0);
INSERT INTO `sys_dictionary` VALUES ('ORDER_STATE_4', NULL, 'ORDER_STATE', '报事报修状态', 'CANCEL', '已撤单', 9, 5, '2022-03-22 16:51:53', '2022-03-22 17:41:27', 0);
INSERT INTO `sys_dictionary` VALUES ('ORDER_STATE_5', NULL, 'ORDER_STATE', '报事报修状态', '10', '转办中', 10, 3, '2022-03-22 16:51:53', '2022-03-22 17:21:50', 0);
INSERT INTO `sys_dictionary` VALUES ('PAYMENT_1', NULL, 'PAYMENT', '支付情况', 'UNPAID', '未支付', 0, 0, '2021-09-09 15:32:49', '2022-03-17 15:54:53', 0);
INSERT INTO `sys_dictionary` VALUES ('PAYMENT_2', NULL, 'PAYMENT', '支付情况', 'PAID', '已支付', 1, 1, '2021-09-09 15:32:49', '2022-03-17 15:54:56', 0);
INSERT INTO `sys_dictionary` VALUES ('POLICY_STATUS_1', NULL, 'POLICY_STATUS', '禁用', 'disabled', '禁用', 0, 0, '2021-09-10 14:49:04', '2021-12-09 17:11:24', 0);
INSERT INTO `sys_dictionary` VALUES ('POLICY_STATUS_2', NULL, 'POLICY_STATUS', '启用', 'enabled', '启用', 1, 1, '2021-09-10 14:49:18', '2021-12-09 17:11:22', 0);
INSERT INTO `sys_dictionary` VALUES ('PRICE_TYPE-1', NULL, 'PRICE_TYPE', '价格类型', 'UNIT_PRICE', '单价', 1, 1, '2021-12-20 17:11:43', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRICE_TYPE-2', NULL, 'PRICE_TYPE', '价格类型', 'PRICE_PER_DAY', '天单价', 1, 1, '2021-12-20 17:11:43', '2021-12-20 17:12:27', 0);
INSERT INTO `sys_dictionary` VALUES ('PRICE_TYPE-3', NULL, 'PRICE_TYPE', '价格类型', 'PRICE_PER_MONTH', '单价', 1, 1, '2021-12-20 17:11:43', '2021-12-20 17:28:29', 0);
INSERT INTO `sys_dictionary` VALUES ('PROTOCOL_ATTRIBUTES_1', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'NEW_PROTOCOL', '新签协议', 1, 1, '2021-12-07 14:29:04', '2021-12-09 16:56:52', 0);
INSERT INTO `sys_dictionary` VALUES ('PROTOCOL_ATTRIBUTES_2', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'RENEW_PROTOCOL', '续签协议', 2, 2, '2021-12-07 14:29:04', '2021-12-09 16:56:54', 0);
INSERT INTO `sys_dictionary` VALUES ('PROTOCOL_ATTRIBUTES_3', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'CHANGE_PROTOCOL', '变更协议', 3, 3, '2021-12-07 14:29:04', '2021-12-09 16:56:56', 0);
INSERT INTO `sys_dictionary` VALUES ('PROTOCOL_ATTRIBUTES_4', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'WITHDRAWAL_PROTOCOL', '退约协议', 4, 4, '2021-12-07 14:29:04', '2021-12-09 16:56:58', 0);
INSERT INTO `sys_dictionary` VALUES ('PROTOCOL_ATTRIBUTES_5', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'HISTORICAL_PROTOCOL', '历史协议', 5, 5, '2021-12-07 14:29:04', '2021-12-09 16:57:00', 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_GOODS_TYPE_1', NULL, 'PRO_GOODS_TYPE', '物品类别', 'MAINTENANCE_CONSUMABLES', '维修耗材', 1, 1, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_GOODS_TYPE_2', NULL, 'PRO_GOODS_TYPE', '物品类别', 'OFFICE_SUPPLIES', '办公用品', 2, 2, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_GOODS_TYPE_3', NULL, 'PRO_GOODS_TYPE', '物品类别', 'CLEANING_SUPPLIES', '清洁用品', 3, 3, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_GOODS_TYPE_4', NULL, 'PRO_GOODS_TYPE', '物品类别', 'TOOLS_SUPPLIES', '工具用品', 4, 4, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_GOODS_TYPE_5', NULL, 'PRO_GOODS_TYPE', '物品类别', 'ELECTRICAL_EQUIPMENT', '电器设备', 5, 5, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_GOODS_TYPE_6', NULL, 'PRO_GOODS_TYPE', '物品类别', 'OTHERS', '其他', 6, 6, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_AUDIT_1', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'NOT_RECEIVE', '领用待审核', 1, 1, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_AUDIT_2', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RECEIVE_PASS', '领用审核通过', 2, 2, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_AUDIT_3', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RECEIVE_REJECT', '领用审核不通过', 3, 3, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_AUDIT_4', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'NOT_RETURN', '归还待审核', 4, 4, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_AUDIT_5', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RETURN_PASS', '归还审核通过', 5, 5, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_AUDIT_6', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RETURN_REJECT', '归还审核通过', 6, 6, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_AUDIT_7', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'CANCEL', '已取消', 7, 7, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_STOCK_1', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'NOT_OUT', '未出库', 1, 1, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_STOCK_2', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'OUT_PASS', '已出库', 2, 2, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_STOCK_3', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'PART_IN', '部分已入库', 3, 3, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_RECEIVE_STOCK_4', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'ALL_IN', '全部已入库', 4, 4, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_MODE_1', NULL, 'PRO_STOCK_MODE', '操作类型', 'STOCK_IN', '入库', 1, 1, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_MODE_2', NULL, 'PRO_STOCK_MODE', '操作类型', 'STOCK_OUT', '出库入库', 2, 2, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_MODE_3', NULL, 'PRO_STOCK_MODE', '操作类型', 'MODIFY_INVENTORY', '修改库存', 3, 3, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_TYPE_1', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'MANUAL_WAREHOUSE', '手动入库', 1, 1, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_TYPE_2', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'RETURN_WAREHOUSE', '归还入库', 2, 2, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_TYPE_3', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'IMPORT_WAREHOUSE', '导入入库', 3, 3, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_TYPE_4', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'MANUAL_DELIVERY', '手动出库', 4, 4, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_TYPE_5', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'RECEIPT_DELIVERY', '领用出库', 5, 5, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PRO_STOCK_TYPE_6', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'MODIFY_INVENTORY', '修改库存', 6, 6, '2022-03-04 19:54:03', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('PUNCH_TYPE_1', NULL, 'PUNCH_TYPE', '打卡类型', 'NORMAL', '上下班打卡', 1, 0, '2022-01-24 15:32:49', '2022-01-24 14:16:43', 0);
INSERT INTO `sys_dictionary` VALUES ('PUNCH_TYPE_2', NULL, 'PUNCH_TYPE', '打卡类型', 'FIELDWORK', '外勤打卡', 2, 0, '2022-01-24 15:32:49', '2022-01-24 10:57:25', 0);
INSERT INTO `sys_dictionary` VALUES ('PURCHASE_STATE_1', NULL, 'PURCHASE_STATE', '采购服务状态', 'TO_BE_PUBLISHED', '待发布', 0, 0, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `sys_dictionary` VALUES ('PURCHASE_STATE_2', NULL, 'PURCHASE_STATE', '采购服务状态', 'IN_PROGRESS', '进行中', 1, 1, '2022-01-04 10:11:24', '2022-03-17 17:19:47', 0);
INSERT INTO `sys_dictionary` VALUES ('PURCHASE_STATE_3', NULL, 'PURCHASE_STATE', '采购服务状态', 'HAS_BEEN_REMOVED', '已下架', 2, 2, '2022-01-04 10:11:24', '2022-03-17 17:04:09', 0);
INSERT INTO `sys_dictionary` VALUES ('PURCHASE_STATE_5', NULL, 'PURCHASE_STATE', '采购服务状态', 'EXPIRED', '已过期', 3, 3, '2022-01-04 10:11:24', '2022-03-17 17:19:57', 0);
INSERT INTO `sys_dictionary` VALUES ('PURCHASE_TYPE_1', NULL, 'PURCHASE_TYPE', '采购类型', 'GOODS', '货物', 0, 0, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `sys_dictionary` VALUES ('PURCHASE_TYPE_2', NULL, 'PURCHASE_TYPE', '采购类型', 'SERVICES', '服务', 1, 1, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `sys_dictionary` VALUES ('PURCHASE_TYPE_3', NULL, 'PURCHASE_TYPE', '采购类型', 'OTHERS', '其他', 2, 2, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `sys_dictionary` VALUES ('PUTINTOTYPE_1', NULL, 'PUT_INTO_TYPE', '投放的小程序类型', 'PUT_INTO_WX_C', 'C端', 1, 1, '2022-01-04 10:33:46', '2022-03-14 17:19:29', 0);
INSERT INTO `sys_dictionary` VALUES ('PUTINTOTYPE_2', NULL, 'PUT_INTO_TYPE', '投放的小程序类型', 'PUT_INTO_WX_B', '运营端', 2, 2, '2022-01-04 10:33:46', '2022-03-14 17:19:35', 0);
INSERT INTO `sys_dictionary` VALUES ('QUESTIONNAIRE_STATUS_1', NULL, 'QUESTIONNAIRE_STATUS', '问卷状态', 'QUESTIONNAIRE_STATUS_1', '设计中', 1, 1, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('QUESTIONNAIRE_STATUS_2', NULL, 'QUESTIONNAIRE_STATUS', '问卷状态', 'QUESTIONNAIRE_STATUS_2', '收集中', 2, 2, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('QUESTIONNAIRE_STATUS_3', NULL, 'QUESTIONNAIRE_STATUS', '问卷状态', 'QUESTIONNAIRE_STATUS_3', '已结束', 3, 3, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RECOGNIZE_RESULT_1', NULL, 'RECOGNIZE_RESULT', '识别结果', 'SUCCESS', '识别通过', 1, 1, '2021-09-10 14:53:29', '2021-12-09 17:11:00', 0);
INSERT INTO `sys_dictionary` VALUES ('RECOGNIZE_RESULT_2', NULL, 'RECOGNIZE_RESULT', '识别结果', 'FAIL', '识别不通过', 0, 0, '2021-09-10 14:53:41', '2021-12-09 17:10:58', 0);
INSERT INTO `sys_dictionary` VALUES ('REGISTER_TYPE_1', NULL, 'REGISTER_TYPE', '登记类型', 'ID_CARD', '人证核验', 1, 1, '2021-12-16 14:38:05', '2021-12-16 14:38:47', 0);
INSERT INTO `sys_dictionary` VALUES ('REGISTER_TYPE_2', NULL, 'REGISTER_TYPE', '登记类型', 'ARTIFICIAL', '无证核验', 2, 2, '2021-12-16 14:39:22', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('REGISTER_TYPE_3', NULL, 'REGISTER_TYPE', '登记类型', 'DOOR_ACCESS', '门禁核验', 3, 3, '2021-12-16 14:39:22', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('REGISTER_VERIFY_TYPE_1', NULL, 'REGISTER_VERIFY_TYPE', '访客核验类型', 'NO_CERTIFICATE', '无凭证', 1, 1, '2021-09-13 14:25:43', '2021-12-09 17:08:47', 0);
INSERT INTO `sys_dictionary` VALUES ('REGISTER_VERIFY_TYPE_2', NULL, 'REGISTER_VERIFY_TYPE', '访客核验类型', 'ID_CARD', '身份证', 2, 2, '2021-09-13 14:25:43', '2021-12-09 17:08:49', 0);
INSERT INTO `sys_dictionary` VALUES ('REGISTER_VERIFY_TYPE_3', NULL, 'REGISTER_VERIFY_TYPE', '访客核验类型', 'SUBSCRIBE_CERTIFICATE', '预约凭证', 3, 3, '2021-09-13 14:25:43', '2021-12-09 17:08:53', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_BILL_FEE_TYPE_COMPANY_1', NULL, 'RENT_BILL_FEE_TYPE_COMPANY', '企业端合同费用账单类型', 'RENT', '租赁费用', 3, 1, '2022-05-23 10:33:16', '2022-05-23 10:44:57', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_BILL_FEE_TYPE_COMPANY_2', NULL, 'RENT_BILL_FEE_TYPE_COMPANY', '企业端合同费用账单类型', 'PROPERTY', '物业费用', 4, 2, '2022-05-23 10:33:16', '2022-05-23 10:44:58', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_BILL_FEE_TYPE_COMPANY_3', NULL, 'RENT_BILL_FEE_TYPE_COMPANY', '企业端合同费用账单类型', 'APARTMENT', '公寓费用', 5, 3, '2022-05-23 10:33:16', '2022-05-23 10:45:00', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_BILL_STATUS_1', NULL, 'RENT_BILL_STATUS', '租赁账单收款状态 ', 'UNRECEIVED', '未收', 0, 1, '2022-04-24 11:21:29', '2022-04-24 11:23:37', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_BILL_STATUS_2', NULL, 'RENT_BILL_STATUS', '租赁账单收款状态 ', 'RECEIVED', '已收', 1, 2, '2022-04-24 11:21:29', '2022-04-24 11:23:28', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_CASH_STATUS_1', NULL, 'RENT_CASH_STATUS', '租赁费用押金状态', 'UNPAID', '未缴清', 1, 1, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_CASH_STATUS_2', NULL, 'RENT_CASH_STATUS', '租赁费用押金状态', 'PAID_UP', '已缴清', 2, 2, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_CASH_STATUS_3', NULL, 'RENT_CASH_STATUS', '租赁费用押金状态', 'CLEARED', '已结清', 3, 3, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_DATA_SOURCE_1', NULL, 'RENT_DATA_SOURCE', '租赁费用数据来源', 'CON_CREATE', '合同生成', 1, 1, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_DATA_SOURCE_2', NULL, 'RENT_DATA_SOURCE', '租赁费用数据来源', 'CON_METER', '抄表计费', 2, 2, '2022-05-27 13:45:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_FEE_BILL_1', NULL, 'RENT_FEE_BILL_STATUS', '租赁费用状态', 'UNRECEIVED', '未收', 0, 0, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_FEE_BILL_2', NULL, 'RENT_FEE_BILL_STATUS', '租赁费用状态', 'RECEIVED', '已收', 1, 1, '2022-05-05 14:27:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_FEE_LEAVE_TYPE_1', NULL, 'RENT_FEE_LEAVE_TYPE', '离场结算计费类型', 'LEAVE', '离场结算', 1, 1, '2022-04-25 10:14:37', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_FEE_OUT_1', NULL, 'RENT_FEE_OUT_STATUS', '租赁费用出账状态', 'BINDING', '待出账', 1, 1, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_FEE_OUT_2', NULL, 'RENT_FEE_OUT_STATUS', '租赁费用出账状态', 'LOST', '已出账', 2, 2, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_FEE_OUT_3', NULL, 'RENT_FEE_OUT_STATUS', '租赁费用出账状态', 'CANCELED', '已废除', 3, 3, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_FEE_PAY_TYPE_1', NULL, 'RENT_FEE_PAY_TYPE', '支付方式', 'OFFLINE_BANK', '线下银行转账', 1, 1, '2022-04-24 16:30:07', '2022-04-24 16:30:16', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_LATE_RULE_1', NULL, 'RENT_LATE_RULE', '滞纳金规则', 'ONE', '逾期金额*逾期天数*滞纳率', 1, 1, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_LATE_RULE_2', NULL, 'RENT_LATE_RULE', '滞纳金规则', 'TWO', '每日滞纳金*逾期天数*建筑面积', 2, 2, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_LATE_RULE_3', NULL, 'RENT_LATE_RULE', '滞纳金规则', 'THREE', '每日滞纳金*逾期天数*套内面积', 3, 3, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_LATE_STATUS_1', NULL, 'RENT_LATE_STATUS', '租赁费用滞纳金状态', 'COMPUTING', '计算中', 1, 1, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_LATE_STATUS_2', NULL, 'RENT_LATE_STATUS', '租赁费用滞纳金状态', 'PAID_UP', '已缴清', 2, 2, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_LATE_STATUS_3', NULL, 'RENT_LATE_STATUS', '租赁费用滞纳金状态', 'CANCELED', '已免除', 3, 3, '2022-05-05 14:25:05', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_TRADE_TYPE_1', NULL, 'RENT_TRADE_TYPE', '租赁费用交易明细类型', 'PAY', '付款订单', 1, 1, '2022-04-24 16:25:44', '2022-04-24 16:27:21', 0);
INSERT INTO `sys_dictionary` VALUES ('RENT_TRADE_TYPE_2', NULL, 'RENT_TRADE_TYPE', '租赁费用交易明细类型', 'RETURN', '退款订单', 2, 2, '2022-04-24 16:25:44', '2022-04-24 16:27:31', 0);
INSERT INTO `sys_dictionary` VALUES ('REPAIR_TYPE_1', NULL, 'ORDERTYPE-2', '报修具体类型', 'WATER_ELECTRICITY', '水电', 21, 1, '2021-09-30 10:22:03', '2022-01-04 10:55:55', 0);
INSERT INTO `sys_dictionary` VALUES ('REPAIR_TYPE_2', NULL, 'ORDERTYPE-2', '报修具体类型', 'ACCESS', '门禁', 22, 2, '2021-09-30 10:22:03', '2022-01-04 10:55:56', 0);
INSERT INTO `sys_dictionary` VALUES ('REPAIR_TYPE_3', NULL, 'ORDERTYPE-2', '报修具体类型', 'ELEVATOR', '电梯', 23, 3, '2021-09-30 10:22:03', '2022-01-04 10:55:56', 0);
INSERT INTO `sys_dictionary` VALUES ('REPAIR_TYPE_4', NULL, 'ORDERTYPE-2', '报修具体类型', 'COMMUNAL_FACILITIES', '公共设施', 24, 4, '2021-09-30 10:22:03', '2022-01-04 10:55:57', 0);
INSERT INTO `sys_dictionary` VALUES ('REPAIR_TYPE_5', NULL, 'ORDERTYPE-2', '报修具体类型', 'TENEMENT_DEVICE', '物业设备', 25, 5, '2021-09-30 10:22:03', '2022-01-04 10:55:59', 0);
INSERT INTO `sys_dictionary` VALUES ('REPAIR_TYPE_6', NULL, 'ORDERTYPE-2', '报修具体类型', 'OTHER', '其他', 26, 6, '2021-09-30 10:22:03', '2022-01-04 10:56:00', 0);
INSERT INTO `sys_dictionary` VALUES ('REVIEW_STATUS_1', NULL, 'REVIEW_STATUS', '预定审核状态', 'REVIEW_STATUS_NON', '未审核', 1, 1, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('REVIEW_STATUS_2', NULL, 'REVIEW_STATUS', '预定审核状态', 'REVIEW_STATUS_YES', '通过', 2, 2, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('REVIEW_STATUS_3', NULL, 'REVIEW_STATUS', '预定审核状态', 'REVIEW_STATUS_NO', '拒绝', 3, 3, '2022-02-11 13:53:45', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('RUNNING', NULL, 'SCREEN_DEVICE_STATUS', '会议室信息屏状态', 'RUNNING', '运行', 2, 2, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('SEPTEMBER', NULL, 'MONTH', '月份', 'SEPTEMBER', '9月', 9, 9, '2022-04-01 16:13:19', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SERVICE_GROUPING_1', NULL, 'SERVICE_GROUPING', '企业服务分组', 'SERVICE_GUIDE', '服务指南', 0, 0, '2021-12-08 15:40:40', '2022-03-19 17:56:50', 0);
INSERT INTO `sys_dictionary` VALUES ('SERVICE_GROUPING_2', NULL, 'SERVICE_GROUPING', '企业服务分组', 'POLICY_DECLARATION', '政策申报', 1, 1, '2021-12-08 15:40:40', '2022-03-19 17:56:52', 0);
INSERT INTO `sys_dictionary` VALUES ('SERVICE_GROUPING_3', NULL, 'SERVICE_GROUPING', '企业服务分组', 'OTHER', '其他', 2, 2, '2021-12-08 15:40:40', '2022-03-19 17:56:55', 0);
INSERT INTO `sys_dictionary` VALUES ('SEX_1', NULL, 'SEX', '性别', 'MAN', '男', 1, 1, '2021-08-31 11:43:53', '2021-12-09 17:25:06', 0);
INSERT INTO `sys_dictionary` VALUES ('SEX_2', NULL, 'SEX', '性别', 'WOMAN', '女', 2, 2, '2021-08-31 11:44:21', '2021-12-09 17:25:03', 0);
INSERT INTO `sys_dictionary` VALUES ('SIGN_IN_STATUS_1', NULL, 'SIGN_IN_STATUS', '访客登入状态', 'NOT_LOGIN_IN', '未登入', 1, 1, '2021-09-13 14:28:25', '2021-12-09 17:08:32', 0);
INSERT INTO `sys_dictionary` VALUES ('SIGN_IN_STATUS_2', NULL, 'SIGN_IN_STATUS', '访客登入状态', 'LOGIN_IN', '已登入', 2, 2, '2021-09-13 14:28:25', '2021-12-09 17:08:34', 0);
INSERT INTO `sys_dictionary` VALUES ('SIGN_IN_STATUS_3', NULL, 'SIGN_IN_STATUS', '访客登入状态', 'LOGIN_OUT', '已登出', 3, 3, '2021-09-13 14:28:25', '2021-12-09 17:08:36', 0);
INSERT INTO `sys_dictionary` VALUES ('SLEEP', NULL, 'SCREEN_DEVICE_STATUS', '会议室信息屏状态', 'SLEEP', '休眠', 1, 1, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('SUBSCRIBE_PROCESS_STATUS_1', NULL, 'SUBSCRIBE_PROCESS_STATUS', '访客流程状态', 'AUDITING', '审核中', 0, 0, '2021-09-13 14:32:49', '2021-12-15 17:10:07', 0);
INSERT INTO `sys_dictionary` VALUES ('SUBSCRIBE_PROCESS_STATUS_2', NULL, 'SUBSCRIBE_PROCESS_STATUS', '访客流程状态', 'AUDIT_PASS', '审核通过', 1, 1, '2021-09-13 14:32:49', '2021-12-15 17:10:08', 0);
INSERT INTO `sys_dictionary` VALUES ('SUBSCRIBE_PROCESS_STATUS_3', NULL, 'SUBSCRIBE_PROCESS_STATUS', '访客流程状态', 'AUDIT_NOT_PASS', '审核不通过', 2, 2, '2021-09-13 14:32:49', '2021-12-15 17:10:10', 0);
INSERT INTO `sys_dictionary` VALUES ('SUBSCRIBE_PROCESS_STATUS_4', NULL, 'SUBSCRIBE_PROCESS_STATUS', '访客流程状态', 'CANCEL', '已取消', 3, 3, '2021-09-13 14:32:49', '2022-01-12 15:50:29', 0);
INSERT INTO `sys_dictionary` VALUES ('SUPPLY_PUBLISH_STATUS_1', NULL, 'SUPPLY_PUBLISH_STATUS', '供应服务状态', 'TO_BE_PUBLISHED', '待发布', 0, 0, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `sys_dictionary` VALUES ('SUPPLY_PUBLISH_STATUS_2', NULL, 'SUPPLY_PUBLISH_STATUS', '供应服务状态', 'PUBLISHED', '已发布', 1, 1, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `sys_dictionary` VALUES ('SUPPLY_PUBLISH_STATUS_3', NULL, 'SUPPLY_PUBLISH_STATUS', '供应服务状态', 'HAS_BEEN_REMOVED', '已下架', 2, 2, '2021-09-10 14:49:04', '2022-03-16 19:50:19', 0);
INSERT INTO `sys_dictionary` VALUES ('SUPPLY_TYPE_1', NULL, 'SUPPLY_TYPE', '供应类型', 'PRODUCT_SUPPLY', '产品供应', 0, 0, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `sys_dictionary` VALUES ('SUPPLY_TYPE_2', NULL, 'SUPPLY_TYPE', '供应类型', 'SERVICE_SUPPLY', '服务供应', 1, 1, '2021-12-08 15:40:40', '2022-03-13 23:48:22', 0);
INSERT INTO `sys_dictionary` VALUES ('SUPPLY_TYPE_3', NULL, 'SUPPLY_TYPE', '供应类型', 'OTHER_SUPPLY', '其他供应', 2, 2, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `sys_dictionary` VALUES ('SUSPENDED', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'SUSPENDED', '暂停使用', 0, 0, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_1', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_1', '消防主机', 1, 1, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_2', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_2', '无线烟感', 2, 2, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_3', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_3', '电气火灾', 3, 3, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_4', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_4', '可燃气体', 4, 4, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_5', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_5', '消防用水', 5, 5, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_6', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_6', '防火门', 6, 6, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_7', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_7', '消防电源', 7, 7, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_8', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_8', '充电桩', 8, 8, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYSTEM_TYPE_9', NULL, 'SYSTEM_TYPE', '消防设备所属系统', 'SYSTEM_TYPE_9', '物联设备', 9, 9, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYS_COMPANY_AUDIT_1', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'AUDIT_RENDING', '待审核', 1, 1, '2022-04-01 21:55:35', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYS_COMPANY_AUDIT_2', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'AUDIT_PASS', '审核通过', 2, 2, '2022-04-01 16:12:37', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYS_COMPANY_AUDIT_3', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'AUDIT_FAIL', '审核不通过', 3, 3, '2022-04-01 16:12:37', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('SYS_COMPANY_AUDIT_4', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'CANCEL', '已取消', 4, 4, '2022-04-01 16:12:38', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('TAX_20', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_20', '10-20w', 2, 2, '2022-06-15 17:23:09', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('TAX_30', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_30', '20-30w', 3, 3, '2022-06-15 17:23:09', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('TAX_50', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_50', '30-50w', 4, 4, '2022-06-15 17:23:09', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('TAX_G50', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_G50', '50w以上', 5, 5, '2022-06-15 17:23:10', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('TAX_L10', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_L10', '10w以下', 1, 1, '2022-06-15 17:23:09', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('THREAD_SOURCE_1', NULL, 'THREAD_SOURCE', '线索来源', 'FROM_OLD_CUSTOMERS', '老客户介绍', 1, 1, '2021-09-30 10:22:03', '2022-01-04 10:55:39', 0);
INSERT INTO `sys_dictionary` VALUES ('THREAD_SOURCE_2', NULL, 'THREAD_SOURCE', '线索来源', 'GOVERNMENT_RESOURCES', '政府资源', 2, 2, '2021-09-30 10:22:03', '2022-01-04 10:55:39', 0);
INSERT INTO `sys_dictionary` VALUES ('THREAD_SOURCE_3', NULL, 'THREAD_SOURCE', '线索来源', 'PUBLIC_RESOURCE', '公共资源', 3, 3, '2021-09-30 10:22:03', '2022-01-04 10:55:41', 0);
INSERT INTO `sys_dictionary` VALUES ('THREAD_SOURCE_4', NULL, 'THREAD_SOURCE', '线索来源', 'THIRD_PARTY_RESOURCES', '第三方资源', 4, 4, '2021-09-30 10:22:03', '2022-01-04 10:55:42', 0);
INSERT INTO `sys_dictionary` VALUES ('THREAD_SOURCE_5', NULL, 'THREAD_SOURCE', '线索来源', 'INTERNAL_RESOURCES', '内部资源', 5, 5, '2021-09-30 10:22:03', '2022-01-04 10:55:43', 0);
INSERT INTO `sys_dictionary` VALUES ('THREAD_SOURCE_6', NULL, 'THREAD_SOURCE', '线索来源', 'NETWORK_RESOURCE', '网络资源', 6, 6, '2021-09-30 10:22:03', '2022-01-04 10:55:45', 0);
INSERT INTO `sys_dictionary` VALUES ('THREAD_SOURCE_7', NULL, 'THREAD_SOURCE', '线索来源', 'INVESTMENT_PROMOTION_PERSONNEL_RESOURCES', '招商人员资源', 7, 7, '2021-09-30 10:22:03', '2022-01-04 10:55:46', 0);
INSERT INTO `sys_dictionary` VALUES ('USING', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'USING', '使用中', 3, 3, '2021-09-07 14:22:03', '2022-04-20 16:37:02', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_PROCESS_NODE_1', NULL, 'VISIT_PROCESS_NODE', '访客流程节点', 'VISIT_SUBSCRIBE', '访客预约', 1, 1, '2021-09-10 16:35:34', '2021-12-17 17:13:10', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_PROCESS_NODE_2', NULL, 'VISIT_PROCESS_NODE', '访客流程节点', 'AUDIT', '审核', 2, 2, '2021-09-10 16:35:34', '2021-12-17 17:13:03', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_PROCESS_NODE_3', NULL, 'VISIT_PROCESS_NODE', '访客流程节点', 'INVITE', '员工邀请', 3, 3, '2021-09-10 16:36:31', '2021-12-17 17:13:03', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_PROCESS_NODE_4', NULL, 'VISIT_PROCESS_NODE', '访客流程节点', 'COMPLETE_INFO', '访客补充', 4, 4, '2021-09-10 16:36:31', '2021-12-17 17:13:04', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_PROCESS_NODE_5', NULL, 'VISIT_PROCESS_NODE', '访客流程节点', 'ID_CARD_VERIFY', '人证核验', 5, 5, '2022-01-11 14:27:53', '2022-01-12 14:18:02', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_REASON_1', NULL, 'VISIT_REASON', '来访事项', 'BUSINESS', '商务交流', 1, 1, '2021-09-13 14:24:15', '2021-12-15 17:08:36', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_REASON_2', NULL, 'VISIT_REASON', '来访事项', 'INTERVIEW', '面试', 2, 2, '2021-09-13 14:24:15', '2021-12-15 17:08:37', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_REASON_3', NULL, 'VISIT_REASON', '来访事项', 'OTHER', '其他', 3, 3, '2021-09-13 14:24:15', '2021-12-15 17:08:38', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_SOURCE_TYPE_1', NULL, 'VISIT_SOURCE_TYPE', '访客来源类型', 'SUBSCRIBE', '访客预约', 1, 1, '2021-09-10 16:34:08', '2021-12-15 17:14:49', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_SOURCE_TYPE_2', NULL, 'VISIT_SOURCE_TYPE', '访客来源类型', 'INVITE', '访客邀约', 2, 2, '2021-09-10 16:34:08', '2021-12-15 17:14:49', 0);
INSERT INTO `sys_dictionary` VALUES ('VISIT_SOURCE_TYPE_3', NULL, 'VISIT_SOURCE_TYPE', '访客来源类型', 'REGISTER', '现场登记', 3, 3, '2021-09-10 16:34:08', '2021-12-15 17:14:52', 0);
INSERT INTO `sys_dictionary` VALUES ('WARNING_CODE_1', NULL, 'WARNING_EVENT_CODE', '告警事件', 'WARNING_CODE_1', '上限告警', 1, 1, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WARNING_CODE_2', NULL, 'WARNING_EVENT_CODE', '告警事件', 'WARNING_CODE_2', '下限告警', 2, 2, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WARNING_CODE_3', NULL, 'WARNING_EVENT_CODE', '告警事件', 'WARNING_CODE_3', '高电平告警', 3, 3, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WARNING_CODE_4', NULL, 'WARNING_EVENT_CODE', '告警事件', 'WARNING_CODE_4', '低电平告警', 4, 4, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WARNING_CODE_5', NULL, 'WARNING_EVENT_CODE', '告警事件', 'WARNING_CODE_5', '通讯失败', 5, 5, '2022-03-04 19:55:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WARN_TYPE_1', NULL, 'WARN_TYPE', '消防设备报警类型', 'WARN_TYPE_1', '故障', 1, 1, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WARN_TYPE_2', NULL, 'WARN_TYPE', '消防设备报警类型', 'WARN_TYPE_2', '报警', 2, 2, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WARN_TYPE_3', NULL, 'WARN_TYPE', '消防设备报警类型', 'WARN_TYPE_3', '离线', 3, 3, '2022-03-18 16:00:24', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WATER_CATEGORY', NULL, 'ENERGY_CONSUMPTION_CATEGORY', '能耗类别', 'WATER_CATEGORY', '水能耗', 5, 1, '2022-06-15 17:23:27', NULL, 0);
INSERT INTO `sys_dictionary` VALUES ('WEEK_DAY_FRIDAY', NULL, 'Week_Day', '星期', 'FRIDAY', '星期五', 5, 5, '2021-12-03 17:07:52', '2022-01-04 10:55:21', 0);
INSERT INTO `sys_dictionary` VALUES ('WEEK_DAY_MONDAY', NULL, 'Week_Day', '星期', 'MONDAY', '星期一', 1, 1, '2021-12-03 17:07:52', '2022-01-04 10:55:04', 0);
INSERT INTO `sys_dictionary` VALUES ('WEEK_DAY_SATURDAY', NULL, 'Week_Day', '星期', 'SATURDAY', '星期六', 6, 6, '2021-12-03 17:07:52', '2022-01-04 10:55:23', 0);
INSERT INTO `sys_dictionary` VALUES ('WEEK_DAY_SUNDAY', NULL, 'Week_Day', '星期', 'SUNDAY', '星期日', 7, 7, '2021-12-03 17:07:52', '2022-01-04 10:55:28', 0);
INSERT INTO `sys_dictionary` VALUES ('WEEK_DAY_THURSDAY', NULL, 'Week_Day', '星期', 'THURSDAY', '星期四', 4, 4, '2021-12-03 17:07:52', '2022-01-04 10:55:19', 0);
INSERT INTO `sys_dictionary` VALUES ('WEEK_DAY_TUESDAY', NULL, 'Week_Day', '星期', 'TUESDAY', '星期二', 2, 2, '2021-12-03 17:07:52', '2022-01-04 10:55:13', 0);
INSERT INTO `sys_dictionary` VALUES ('WEEK_DAY_WEDNESDAY', NULL, 'Week_Day', '星期', 'WEDNESDAY', '星期三', 3, 3, '2021-12-03 17:07:52', '2022-01-04 10:55:15', 0);
INSERT INTO `sys_dictionary` VALUES ('YES_OR_NO_1', NULL, 'YES_OR_NO', '肯定或否定', 'YES', '是', 1, 1, '2021-09-07 17:12:43', '2021-12-09 17:23:27', 0);
INSERT INTO `sys_dictionary` VALUES ('YES_OR_NO_2', NULL, 'YES_OR_NO', '肯定或否定', 'NO', '否', 0, 0, '2021-09-07 17:13:39', '2021-12-09 17:23:24', 0);

-- ----------------------------
-- Table structure for sys_employee
-- ----------------------------
DROP TABLE IF EXISTS `sys_employee`;
CREATE TABLE `sys_employee`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区组织ID',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属入驻企业ID',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工姓名',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `pics` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '照片（英文逗号分隔）',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `tag_ids` json NULL COMMENT '员工标签id集合',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态，0：禁用 1：启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `card_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '人员卡号',
  `device_person_id` bigint NULL DEFAULT NULL COMMENT '设备人员ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '入住企业员工信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_employee
-- ----------------------------

-- ----------------------------
-- Table structure for sys_house
-- ----------------------------
DROP TABLE IF EXISTS `sys_house`;
CREATE TABLE `sys_house`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼栋ID',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织Id',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼层房号',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋编码',
  `floor` int NULL DEFAULT NULL COMMENT '所在楼层',
  `floor_type` tinyint(1) NULL DEFAULT NULL COMMENT '楼层属性：平层，底层，中层，高层',
  `cover_area` double NULL DEFAULT NULL COMMENT '套内面积',
  `build_area` double NULL DEFAULT NULL COMMENT '建筑面积',
  `house_status` tinyint NULL DEFAULT 0 COMMENT '房屋状态：0自用，1待租， 2已租，3待售，4已售',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `house_property` tinyint NULL DEFAULT NULL COMMENT '房屋属性：0自用，1租赁，2销售',
  `house_function` tinyint NULL DEFAULT NULL COMMENT '房屋功能；办公 商铺 公寓 仓库 其他',
  `orientation` tinyint NULL DEFAULT NULL COMMENT '朝向',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房屋表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_house
-- ----------------------------

-- ----------------------------
-- Table structure for sys_house_rent_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_house_rent_record`;
CREATE TABLE `sys_house_rent_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房屋ID',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业ID',
  `rent_start_time` date NOT NULL COMMENT '起租时间',
  `rent_end_time` date NOT NULL COMMENT '到期时间（包含续租）',
  `bind` tinyint NULL DEFAULT 1 COMMENT '绑定状态：1：已绑定 0：已解绑',
  `unbind_time` date NULL DEFAULT NULL COMMENT '解绑时间',
  `rent_first_end_time` date NULL DEFAULT NULL COMMENT '初始到期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房屋入驻记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_house_rent_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_house_rent_renew_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_house_rent_renew_record`;
CREATE TABLE `sys_house_rent_renew_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rent_record_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '房屋租赁ID',
  `rent_end_time` date NULL DEFAULT NULL COMMENT '当前到期时间',
  `renew_end_time` date NULL DEFAULT NULL COMMENT '续租后到期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '房屋续租记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_house_rent_renew_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_code` tinyint NOT NULL COMMENT '岗位代码',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织Id',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业Id',
  `status` tinyint NOT NULL DEFAULT 1,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '工作岗位' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_employee_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_employee_relation`;
CREATE TABLE `sys_job_employee_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `job_id_index`(`job_id`) USING BTREE,
  INDEX `user_id_index`(`employee_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位用户关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_employee_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_job_user_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_user_relation`;
CREATE TABLE `sys_job_user_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `job_id_index`(`job_id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位用户关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_user_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `origin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户菜单对应的原始菜单ID',
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织的顶级组织的id,即租户，0表示租户管理平台的模板菜单，-1表示租户管理平台的系统菜单',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父节点id，0表示菜单最上级节点',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` double(10, 2) NOT NULL COMMENT '排序',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0：不显示，1：显示',
  `enabled` tinyint(1) NULL DEFAULT 1 COMMENT '租户菜单是否启用，0不启用，1启用',
  `route` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由',
  `route_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由名',
  `api_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口路径',
  `type` tinyint NULL DEFAULT NULL COMMENT '类型：0：菜单 1：按钮',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单/按钮表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1413107113673318401', NULL, '0', '0', '园区管理', 'company-fill', 1.00, 1, NULL, 'park-management', 'parkManagement', 'park-management', 0, NULL, '2022-03-09 13:42:58', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1413107829863309314', NULL, '0', '1413107113673318401', '房屋管理', '', 1.00, 1, NULL, 'house', 'parkManagementHouse', 'park-management', 0, NULL, NULL, '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1413108475773542402', NULL, '0', '0', '系统管理', 'set', 14.00, 1, NULL, 'system-management', 'systemManagement', 'system-management', 0, NULL, '2022-07-07 10:32:26', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1413411999791157250', NULL, '0', '1413108475773542402', '用户管理', '', 1.00, 1, NULL, 'user', 'systemManagementUser', 'system-management', 0, NULL, '2022-01-05 17:50:21', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1414490267730554882', NULL, '0', '1413108475773542402', '角色管理', '', 2.00, 0, NULL, '/system-management/role', 'systemManagementRole', 'role', 0, NULL, '2021-09-13 16:43:04', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1415222344495075329', NULL, '0', '1413108475773542402', '组织架构', '', 0.00, 0, NULL, '/organizational-structure', 'systemManagementOrganizationalStructure', '/organization', 0, NULL, '2022-01-05 17:50:13', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1419940479215292418', NULL, '0', '1413107113673318401', '园区列表', '', 0.00, 1, NULL, '/park-list', 'parkManagementParkList', '/park-system', 0, '2021-07-27 16:39:26', '2022-01-05 17:45:06', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1419944004242591745', NULL, '0', '1419940479215292418', '园区详情', '', 0.00, 1, NULL, '/park-details', 'parkManagementParkDetails', '/park-system', 2, '2021-07-27 16:53:26', '2021-07-30 10:46:45', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1436205736883646465', NULL, '-1', '5', '用户管理', '', 2.00, 1, NULL, '/system-management/user', 'systemManagementUser', '/system-management', 0, '2021-09-10 13:51:45', '2021-09-10 13:51:45', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1436206217857069058', NULL, '-1', '5', '角色管理', '', 3.00, 1, NULL, '/system-management/role', 'systemManagementRole', '/system-management', 0, '2021-09-10 13:53:40', '2021-09-10 13:53:40', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1436215157328056322', NULL, '-1', '0', '租户管理', 'usercenter1', 0.00, 1, NULL, '/tenant-management', 'tenantManagement', '/tenant-management', 0, '2021-09-10 14:29:11', '2022-01-07 10:53:53', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1436215805771649026', NULL, '-1', '1436215157328056322', '租户列表', '', 1.00, 1, NULL, '', 'tenantManagementList', '/tenant-management/list', 0, '2021-09-10 14:31:46', '2021-12-10 11:13:52', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1437296076726185986', NULL, '-1', '1436215157328056322', '租户菜单管理', '', 3.00, 1, 1, '/tenant-management/menu/list', 'tenantManagementMenuList', '/tenant-management', 0, '2021-09-13 14:04:23', '2022-01-04 14:07:12', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1437679155743539202', '1413107113673318401', '1437679155114393601', '0', '园区管理', 'company-fill', 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2022-05-24 17:53:08', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1437679155743539203', '1413107829863309314', '1437679155114393601', '1437679155743539202', '房屋管理', '', 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2021-12-29 16:51:28', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1437679155743539204', '1413108475773542402', '1437679155114393601', '0', '系统管理', 'set', 14.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2022-07-18 16:40:17', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1437679155751927809', '1413411999791157250', '1437679155114393601', '1437679155743539204', '用户管理', '', 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2021-09-14 16:00:36', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1437679155751927810', '1414490267730554882', '1437679155114393601', '1437679155743539204', '角色管理', '', 2.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2021-12-29 16:59:54', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1437679155751927811', '1415222344495075329', '1437679155114393601', '1437679155743539204', '组织架构', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2022-01-05 17:50:13', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1437679155760316417', '1419940479215292418', '1437679155114393601', '1437679155743539202', '园区列表', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2021-09-14 16:00:36', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1437679155760316418', '1419944004242591745', '1437679155114393601', '1437679155760316417', '园区详情', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-14 15:26:36', '2021-09-14 16:00:36', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1438382628499853314', NULL, '0', '1413108475773542402', '菜单管理', '', 3.00, 1, 1, '/system-management/menu-list', 'menuManagementMenuList', '/system-management', 0, '2021-09-16 14:01:57', '2022-01-05 17:50:34', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1438382628843786242', '1438382628499853314', '1437679155114393601', '1437679155743539204', '菜单管理', '', 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-16 14:01:57', '2022-01-05 17:50:28', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1441967553601794050', NULL, '0', '0', '园区企业管理', 'Customermanagement-fill', 3.00, 1, 1, '/company-mangement', 'companyMangement', '/company-mangement', 0, '2021-09-26 11:27:09', '2022-06-14 17:44:05', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1441967553996058626', '1441967553601794050', '1437679155114393601', '0', '园区企业管理', 'Customermanagement-fill', 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-26 11:27:10', '2022-06-14 17:44:05', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1441967669746266113', NULL, '0', '1441967553601794050', '员工管理', '', 2.00, 1, 1, '/company-mangement/employees-mangement', 'companyMangementEmployeesMangement', '/company-mangement/employees-mangement', 0, '2021-09-26 11:27:37', '2022-01-05 17:47:29', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1441967670220222465', '1441967669746266113', '1437679155114393601', '1441967553996058626', '员工管理', '', 2.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-26 11:27:37', '2021-09-26 11:27:37', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1441967752080453634', NULL, '0', '1441967553601794050', '入驻记录', NULL, 1.00, 1, 1, '/company-mangement/occupancy-record', 'companyMangementOccupancyRecord', '/company-mangement/occupancy-record', 0, '2021-09-26 11:27:57', '2022-04-02 17:37:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1441967752541827074', '1441967752080453634', '1437679155114393601', '1441967553996058626', '入驻记录', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-26 11:27:57', '2022-04-02 17:37:59', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1441967821190000642', NULL, '0', '1441967553601794050', '园区企业', NULL, 0.00, 1, 1, '/company-mangement/company-list', 'companyMangementPompanyList', '/company-mangement/company-list', 0, '2021-09-26 11:28:13', '2022-06-14 17:44:12', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1441967821672345602', '1441967821190000642', '1437679155114393601', '1441967553996058626', '园区企业', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-26 11:28:13', '2022-06-14 17:44:12', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1441967931118514178', NULL, '0', '1441967553601794050', '员工标签管理', '', 4.00, 1, 1, '/company-mangement/employees-labels', 'companyMangementEmployeesLabels', '/company-mangement/employees-labels', 0, '2021-09-26 11:28:39', '2022-01-05 17:47:42', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1441967931680550914', '1441967931118514178', '1437679155114393601', '1441967553996058626', '员工标签管理', '', 4.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-26 11:28:40', '2022-01-05 17:47:42', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1442041009123024897', NULL, '0', '1441967821190000642', '解绑房屋', '', 0.00, 1, 1, '/company-mangement/company-list', 'companyMangementUnbindHouse', '/company-mangement/company-list', 2, '2021-09-26 16:19:03', '2021-09-28 15:39:02', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1442041009651507201', '1442041009123024897', '1437679155114393601', '1441967821672345602', '解绑房屋', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-26 16:19:03', '2021-09-28 15:39:02', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1442047027764715521', NULL, '0', '1441967821190000642', '绑定房屋', '', 0.00, 1, 1, '/bind-house', 'companyMangementBindHouse', '/bind-house', 2, '2021-09-26 16:42:58', '2021-09-28 15:27:02', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1442047028188340226', '1442047027764715521', '1437679155114393601', '1441967821672345602', '绑定房屋', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-26 16:42:58', '2021-09-28 15:27:02', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1442752894109691905', NULL, '0', '1441967752080453634', '续租记录', '', 0.00, 1, 1, '/occupancy-record/renewal-record', 'companyMangementRenewalRecord', '/occupancy-record/renewal-record', 2, '2021-09-28 15:27:49', '2021-09-28 15:27:49', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1442752894571065345', '1442752894109691905', '1437679155114393601', '1441967752541827074', '续租记录', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-28 15:27:49', '2021-09-28 15:27:49', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1443470588224192514', NULL, '0', '1441967553601794050', '岗位管理', '', 5.00, 1, 1, '/jobs-mangement`', 'companyMangementJobsMangement', '/jobs-mangement`', 0, '2021-09-30 14:59:41', '2022-01-05 17:47:51', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1443470588823977985', '1443470588224192514', '1437679155114393601', '1441967553996058626', '岗位管理', '', 5.00, 1, 1, NULL, NULL, NULL, NULL, '2021-09-30 14:59:41', '2022-01-05 17:47:51', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446677983683354626', NULL, '0', '0', '园区服务', 'service', 12.00, 1, 1, '/customer-service', 'customerService', '/customer-service', 0, '2021-10-09 11:24:43', '2022-07-07 10:32:15', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446677984035676162', '1446677983683354626', '1437679155114393601', '0', '园区服务', 'service', 12.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:24:44', '2022-07-07 10:32:15', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446678253830086657', NULL, '0', '1446677983683354626', '报事投诉', '', 1.00, 1, 1, '/report-complaint', 'customerServiceReportComplaint', '/report-complaint', 0, '2021-10-09 11:25:48', '2021-10-09 11:36:15', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446678254299848706', '1446678253830086657', '1437679155114393601', '1446677984035676162', '报事投诉', '', 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:25:48', '2021-10-09 11:25:48', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446678390379847682', NULL, '0', '1446677983683354626', '报修服务', '', 2.00, 1, 1, '/customer-service/repair-service', 'customerServiceRepairService', '/customer-service/repair-service', 0, '2021-10-09 11:26:20', '2021-10-09 11:26:20', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446678390870581249', '1446678390379847682', '1437679155114393601', '1446677984035676162', '报修服务', '', 2.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:26:21', '2021-10-09 11:26:21', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446678929398243329', NULL, '0', '1544870299570208769', '访客管理', NULL, 25.00, 1, 1, '/visitors-management', 'visitorsManagement', '/visitors-management', 0, '2021-10-09 11:28:29', '2022-07-07 11:07:11', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446678929746370562', '1446678929398243329', '1437679155114393601', '1544870299645706241', '访客管理', NULL, 25.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:28:29', '2022-07-07 11:07:11', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446679117877682177', NULL, '0', '1446678929398243329', '访客信息', NULL, 1.00, 1, 1, '/visitors-management/visitors', 'visitorsManagementAppointmentList', '/visitors-management/visitors', 0, '2021-10-09 11:29:14', '2022-03-09 15:17:25', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446679118401970178', '1446679117877682177', '1437679155114393601', '1446678929746370562', '访客信息', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:29:14', '2022-03-09 15:17:25', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446679255807369218', NULL, '0', '1446679117877682177', '已预约访客', '', 0.00, 1, 1, '/visitors-management/appointment-list', 'visitorsManagementAppointmentList', '/appointment-list', 0, '2021-10-09 11:29:47', '2021-10-09 11:29:47', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446679256306491394', '1446679255807369218', '1437679155114393601', '1446679118401970178', '已预约访客', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:29:47', '2021-12-29 16:52:21', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446679443238232066', NULL, '0', '1446679117877682177', '当前访客', '', 1.00, 1, 1, '/visitors-management/current-visitors', 'visitorsManagementCurrentVisitors', '/visitors-management', 0, '2021-10-09 11:30:31', '2022-01-05 17:55:09', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446679443707994114', '1446679443238232066', '1437679155114393601', '1446679118401970178', '当前访客', '', 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:30:32', '2022-01-05 17:55:09', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1446679595206254593', NULL, '0', '1446679117877682177', '历史访客', '', 3.00, 1, 1, '/visitors-management/history-visitors', 'visitorsManagementHistoryVisitors', '/visitors-management', 0, '2021-10-09 11:31:08', '2022-01-05 17:55:04', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1446679595814428674', '1446679595206254593', '1437679155114393601', '1446679118401970178', '历史访客', '', 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-09 11:31:08', '2022-01-05 17:55:04', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1448182963817590786', NULL, '0', '1446677983683354626', '公告管理', '', 3.00, 1, 1, '/customer-service/announcement', 'customerServiceAnnouncement', '/customer-service/announcement', 0, '2021-10-13 15:04:59', '2021-10-13 15:04:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1448182964169912322', '1448182963817590786', '1437679155114393601', '1446677984035676162', '公告管理', '', 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-13 15:04:59', '2021-12-29 16:48:00', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1448252937575776258', NULL, '0', '1446677983683354626', '资讯管理', '', 4.00, 1, 1, '/customer-service/notice', 'customerServiceNotice', '/customer-service/notice', 0, '2021-10-13 19:43:02', '2021-10-20 17:39:33', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1448252937928097794', '1448252937575776258', '1437679155114393601', '1446677984035676162', '资讯管理', '', 4.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-13 19:43:02', '2021-12-31 16:09:21', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1450758892792205314', NULL, '0', '0', '招商管理', 'tuishuirongzi', 4.00, 1, 1, '/wisdom-canvass-business', 'wisdomCanvassBusiness', '/park-business', 0, '2021-10-20 17:40:48', '2022-07-07 10:31:06', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1450758893001920513', '1450758892792205314', '1437679155114393601', '0', '招商管理', 'tuishuirongzi', 4.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-20 17:40:48', '2022-07-07 10:31:06', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1450759145389969410', NULL, '0', '1450758892792205314', '房源招商', '', 1.00, 1, 1, '/wisdom-canvass-business/housing-business', 'wisdomCanvassBusinessHousingBusiness', '/park-business', 0, '2021-10-20 17:41:48', '2022-01-13 09:54:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1450759145691959297', '1450759145389969410', '1437679155114393601', '1450758893001920513', '房源招商', '', 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-20 17:41:48', '2022-01-13 09:54:54', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1451018943524200450', NULL, '0', '1450759145389969410', '招商详情', '', 0.00, 1, 1, '/wisdom-canvass-business/business-info-Add', 'wisdomCanvassBusinessBusinessInfoAdd', '/park-business', 2, '2021-10-21 10:54:09', '2022-01-18 17:49:41', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1451018943834578946', '1451018943524200450', '1437679155114393601', '1450759145691959297', '招商详情', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-21 10:54:09', '2022-01-18 17:49:41', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1452873337793626113', NULL, '0', '1450758892792205314', '招商房屋', '', 0.00, 1, 1, '/wisdom-canvass-business/house-list', 'wisdomCanvassBusinessHouseList', '/park-business', 0, '2021-10-26 13:42:50', '2022-01-13 09:54:44', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1452873338322108417', '1452873337793626113', '1437679155114393601', '1450758893001920513', '招商房屋', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-26 13:42:50', '2022-01-13 09:54:44', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1452893057758478338', NULL, '0', '1452873337793626113', '编辑房屋信息', '', 0.00, 1, 1, '/wisdom-canvass-business/housing-info-edit', 'wisdomCanvassBusinessHousingInfoEdit', '/park-business', 2, '2021-10-26 15:01:13', '2021-10-27 14:32:46', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1452893058261794817', '1452893057758478338', '1437679155114393601', '1452873338322108417', '编辑房屋信息', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-26 15:01:13', '2021-10-26 15:01:13', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1453271340635357185', NULL, '0', '1453271697046339585', '全部商机', '', 0.00, 1, 1, '/wisdom-canvass-business/all-business-opportunities', 'wisdomCanvassBusinessAllBusinessOpportunities', '/park-business', 0, '2021-10-27 16:04:22', '2021-10-27 16:06:11', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1453271341121896449', '1453271340635357185', '1437679155114393601', '1453271697503518721', '全部商机', '', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-27 16:04:22', '2021-12-29 16:59:24', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1453271697046339585', NULL, '0', '1450758892792205314', '商机管理', '', 3.00, 1, 1, '/wisdom-canvass-business/business-opportunity-management', 'wisdomCanvassBusinessBusinessOpportunityManagement', '/park-business', 0, '2021-10-27 16:05:47', '2022-01-05 17:52:52', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1453271697503518721', '1453271697046339585', '1437679155114393601', '1450758893001920513', '商机管理', '', 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-27 16:05:47', '2022-01-05 17:52:52', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1453278535431192578', NULL, '0', '1453271697046339585', '我的商机', '', 2.00, 1, 1, '/wisdom-canvass-business/my-business-opportunities', 'wisdomCanvassBusinessMyBusinessOpportunities', '/park-business', 0, '2021-10-27 16:32:58', '2022-01-05 17:54:20', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1453278535909343234', '1453278535431192578', '1437679155114393601', '1453271697503518721', '我的商机', '', 2.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-27 16:32:58', '2022-01-05 17:54:20', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1453278874494533633', NULL, '0', '1453271697046339585', '公共商机', '', 1.00, 1, 1, '/wisdom-canvass-business', 'wisdomCanvassBusinessPublicBusinessOpportunities', '/park-business', 0, '2021-10-27 16:34:18', '2022-01-05 17:54:07', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1453278874955907074', '1453278874494533633', '1437679155114393601', '1453271697503518721', '公共商机', '', 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-10-27 16:34:19', '2022-01-05 17:54:07', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1455110558244380674', NULL, '0', '1453271697046339585', '商机详情', NULL, 0.00, 1, 1, '/wisdom-canvass-business/business-opportunities-details', 'wisdomCanvassBusinessOpportunitiesDetails', '/park-business', 2, '2021-11-01 17:52:46', '2021-11-01 17:52:46', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1455110558835777538', '1455110558244380674', '1437679155114393601', '1453271697503518721', '商机详情', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-11-01 17:52:46', '2021-11-01 17:52:46', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1462701076894400513', NULL, '0', '1450758892792205314', '看房申请', NULL, 2.00, 1, 1, '/wisdom-canvass-business/view-house-apply', 'wisdomCanvassBusinessViewHouseApply', '/park-business', 0, '2021-11-22 16:34:47', '2022-01-05 17:52:46', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1462701077301248001', '1462701076894400513', '1437679155114393601', '1450758893001920513', '看房申请', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2021-11-22 16:34:47', '2022-01-05 17:52:46', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1465163166870798338', NULL, '0', '1544870728823668737', '巡检管理', NULL, 27.00, 1, 1, '/patrol-inspection', 'patrolInspectionPatrolPlace', '/park-inspection', 0, '2021-11-29 11:38:15', '2022-07-07 11:08:49', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1465163167441223681', '1465163166870798338', '1437679155114393601', '1544870728857223170', '巡检管理', NULL, 27.00, 1, 1, NULL, NULL, NULL, NULL, '2021-11-29 11:38:15', '2022-07-07 11:08:49', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1465163346575753218', NULL, '0', '1465163166870798338', '巡检点管理', NULL, 0.00, 1, 1, '/patrol-inspection/patrol-place', 'patrolInspectionPatrolPlace', '/park-inspection', 0, '2021-11-29 11:38:57', '2021-11-29 11:38:57', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1465163347175538689', '1465163346575753218', '1437679155114393601', '1465163167441223681', '巡检点管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-11-29 11:38:58', '2021-12-29 16:54:58', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1465569649144057858', NULL, '0', '1465163166870798338', '巡检路线', NULL, 1.00, 1, 1, '/patrol-inspection/patrol-line', 'patrolInspectionPatrolLine', '/park-inspection', 0, '2021-11-30 14:33:28', '2022-01-17 11:22:10', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1465569649701900290', '1465569649144057858', '1437679155114393601', '1465163167441223681', '巡检路线', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-11-30 14:33:28', '2022-01-17 11:22:10', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1466365916094865410', NULL, '0', '1465163166870798338', '巡检计划', NULL, 2.00, 1, 1, '/patrol-inspection/patrol-plan', 'patrolInspectionPatrolPlan', '/park-inspection', 0, '2021-12-02 19:17:32', '2022-01-05 17:56:42', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1466365916686262274', '1466365916094865410', '1437679155114393601', '1465163167441223681', '巡检计划', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-02 19:17:33', '2022-01-05 17:56:42', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1468054231990898689', NULL, '0', '1465163166870798338', '巡检任务', NULL, 3.00, 1, 1, '/patrol-inspection/patrol-task', 'patrolInspectionPatrolTask', '/park-inspection', 0, '2021-12-07 11:06:18', '2022-01-05 17:56:48', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1468054232573906945', '1468054231990898689', '1437679155114393601', '1465163167441223681', '巡检任务', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-07 11:06:18', '2022-01-05 17:56:48', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1475742604231491585', NULL, '0', '1446678929398243329', '配置管理', NULL, 0.00, 1, 1, '/configuration-management', 'visitorsConfigurationManagement', '/park-visitor', 0, '2021-12-28 16:17:09', '2021-12-28 16:17:09', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1475742604818694146', '1475742604231491585', '1437679155114393601', '1446678929746370562', '配置管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-28 16:17:09', '2022-03-21 13:47:20', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1475742781197565954', NULL, '0', '1475742604231491585', '审批人配置', NULL, 3.00, 1, 1, '/visitors-management/audit-person', 'visitorsConfigurationManagementAuditPerson', '/park-visitor', 0, '2021-12-28 16:17:51', '2022-06-07 14:54:35', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1475742781654745089', '1475742781197565954', '1437679155114393601', '1475742604818694146', '审批人配置', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-28 16:17:51', '2022-06-07 14:54:35', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476031286778679297', NULL, '0', '1475742604231491585', '访客类型', NULL, 0.00, 1, 1, '/visitors-management/visitor-type', 'visitorsConfigurationManagementVisitorType', '/park-visitor', 0, '2021-12-29 11:24:16', '2021-12-29 11:24:16', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476031287353298946', '1476031286778679297', '1437679155114393601', '1475742604818694146', '访客类型', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-29 11:24:16', '2021-12-29 11:24:16', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476074921603780609', NULL, '0', '1475742604231491585', '访客流程', NULL, 1.00, 1, 1, '/visitors-management/visitor-process', 'visitorsConfigurationManagementVisitorProcess', '/park-visitor', 0, '2021-12-29 14:17:40', '2022-06-07 14:54:27', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476074922123874305', '1476074921603780609', '1437679155114393601', '1475742604818694146', '访客流程', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-29 14:17:40', '2022-06-07 14:54:28', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476393149202894849', NULL, '0', '1475742604231491585', '企业信息', NULL, 4.00, 1, 1, '/visitors-management/company-info-bind', 'visitorsConfigurationManagementCompanyInfoBind', '/park-system/', 0, '2021-12-30 11:22:11', '2022-06-07 14:53:51', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476393149790097409', '1476393149202894849', '1437679155114393601', '1475742604818694146', '企业信息', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-30 11:22:11', '2022-06-07 14:53:51', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476452792528777218', NULL, '0', '1544870299570208769', '车行管理', NULL, 23.00, 1, 1, '/car-pass', 'carPassManagement', '/park-visitor/', 0, '2021-12-30 15:19:11', '2022-07-07 11:07:05', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476452792855932929', '1476452792528777218', '1437679155114393601', '1544870299645706241', '车行管理', NULL, 23.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-30 15:19:11', '2022-07-07 11:07:05', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476453144984420353', NULL, '0', '1476452792528777218', '车辆管理', NULL, 2.00, 1, 1, '/car-pass/car', 'carPassManagementCar', '/park-visitor', 0, '2021-12-30 15:20:35', '2022-03-09 15:16:23', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476453145584205826', '1476453144984420353', '1437679155114393601', '1476452792855932929', '车辆管理', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-30 15:20:35', '2022-03-09 15:16:23', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476453287326515201', NULL, '0', '1476452792528777218', '车道管理', NULL, 4.00, 1, 1, '/car-pass/lane', 'carPassManagementLane', '/park-visitor', 0, '2021-12-30 15:21:09', '2022-03-09 15:16:33', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476453287972438018', '1476453287326515201', '1437679155114393601', '1476452792855932929', '车道管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-30 15:21:09', '2022-03-09 15:16:33', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476453430260006914', NULL, '0', '1476452792528777218', '停车场管理', NULL, 3.00, 1, 1, '/car-pass/parking-lot', 'carPassManagementParkingLot', '/park-visitor', 0, '2021-12-30 15:21:43', '2022-03-09 15:16:28', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476453430855598081', '1476453430260006914', '1437679155114393601', '1476452792855932929', '停车场管理', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-30 15:21:43', '2022-03-09 15:16:28', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1476829541590581250', NULL, '0', '1476452792528777218', '通行记录', NULL, 0.00, 1, 1, '/car-pass/car-pass-record', 'carPassManagementCarPassRecord', '/park-visitor', 0, '2021-12-31 16:16:15', '2021-12-31 16:16:15', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1476829542114869249', '1476829541590581250', '1437679155114393601', '1476452792855932929', '通行记录', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2021-12-31 16:16:15', '2021-12-31 16:16:15', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1478633443804131329', NULL, '0', '1476452792528777218', '抓拍记录', NULL, 1.00, 1, 1, '/car-pass/car-pass-snap-record', 'carPassManagementCarPassSnapRecord', '/park-visitor', 0, '2022-01-05 15:44:19', '2022-03-09 15:16:18', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1478633444508774402', '1478633443804131329', '1437679155114393601', '1476452792855932929', '抓拍记录', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-05 15:44:19', '2022-03-09 15:16:19', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1478695869169872897', NULL, '0', '1544864968110702593', '设备管理', NULL, 11.00, 1, 1, '/equipment-management', 'equipmentManagement', '/park-device', 0, '2022-01-05 19:52:22', '2022-07-07 11:06:25', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1478695869564137474', '1478695869169872897', '1437679155114393601', '1544864968194588674', '设备管理', NULL, 11.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-05 19:52:22', '2022-07-07 11:06:25', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1479367665950994434', NULL, '0', '1544870299570208769', '人行管理', NULL, 21.00, 1, 1, '/person-pass/pass-record', 'personPass', '/park-visitor', 0, '2022-01-07 16:21:51', '2022-07-07 11:07:00', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1479367666378813441', '1479367665950994434', '1437679155114393601', '1544870299645706241', '人行管理', NULL, 21.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-07 16:21:51', '2022-07-07 11:07:00', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1479367970520379393', NULL, '0', '1479367665950994434', '通行记录', NULL, 0.00, 1, 1, '/person-pass/pass-record', 'personPassPassRecordList', '/park-visitor', 0, '2022-01-07 16:23:04', '2022-01-07 16:50:38', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1479367971090804738', '1479367970520379393', '1437679155114393601', '1479367666378813441', '通行记录', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-07 16:23:04', '2022-01-07 16:23:04', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1479422945827663873', NULL, '0', '1479367665950994434', '抓拍记录', NULL, 1.00, 1, 1, '/person-pass/pass-snap-record', 'personPassPassSnapRecordList', '/park-visitor', 0, '2022-01-07 20:01:31', '2022-03-09 15:16:01', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1479422946272260098', '1479422945827663873', '1437679155114393601', '1479367666378813441', '抓拍记录', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-07 20:01:31', '2022-03-09 15:16:01', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1480813318691098626', NULL, '0', '0', '园区合同', 'hot', 5.00, 1, 1, '/contract-manage', 'contractManage', '/contract-manage', 0, '2022-01-11 16:06:22', '2022-07-07 10:31:12', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1480813318863065089', '1480813318691098626', '1437679155114393601', '0', '园区合同', 'hot', 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-11 16:06:22', '2022-07-07 10:31:12', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1480821373977759746', NULL, '0', '1480814708003966978', '起草合同', NULL, 2.00, 1, 1, '/contract-manage/contract-draft', 'contractDraft', '/contract-manage', 0, '2022-01-11 16:38:22', '2022-01-11 17:11:36', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1480821374174892034', '1480821373977759746', '1437679155114393601', '1480814708326928386', '起草合同', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-11 16:38:22', '2022-01-11 17:11:36', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1480822523934281730', NULL, '0', '1480814708003966978', '合同变更', NULL, 3.00, 1, 1, '/contract-manage/contract-change', 'contractChange', '/contract-manage', 0, '2022-01-11 16:42:56', '2022-01-11 17:11:51', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1480822524152385538', '1480822523934281730', '1437679155114393601', '1480814708326928386', '合同变更', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-11 16:42:56', '2022-01-11 17:11:51', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1480822675952635905', NULL, '0', '1480814708003966978', '合同续约', NULL, 4.00, 1, 1, '/contract-manage/contract-renewal', 'contractRenewal', '/contract-manage', 0, '2022-01-11 16:43:32', '2022-01-11 17:12:01', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1480822676208488450', '1480822675952635905', '1437679155114393601', '1480814708326928386', '合同续约', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-11 16:43:33', '2022-01-11 17:12:01', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1480829667303755778', NULL, '0', '1480814708003966978', '合同列表', NULL, 1.00, 1, 1, '/contract-manage/contract-list', 'contractList', '/contract-manage', 0, '2022-01-11 17:11:19', '2022-01-11 17:11:19', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1480829667555414017', '1480829667303755778', '1437679155114393601', '1480814708326928386', '合同列表', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-11 17:11:19', '2022-01-11 17:11:19', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1482951933643972610', NULL, '0', '0', '收费管理', 'consumption', 6.00, 1, 1, '/collect-fee', 'collectFee', '/park-fee/', 0, '2022-01-17 13:44:27', '2022-07-07 10:31:17', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1482951934168260610', '1482951933643972610', '1437679155114393601', '0', '收费管理', 'consumption', 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-17 13:44:27', '2022-07-07 10:31:17', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1482952153329033218', NULL, '0', '1525037609539010561', '收费项', NULL, 0.00, 1, 1, '/collect-fee/charging-items', 'collectFeeChargingItems', '/collect-fee', 0, '2022-01-17 13:45:20', '2022-05-13 16:58:18', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1482952154075619330', '1482952153329033218', '1437679155114393601', '1525037610885382146', '收费项', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-17 13:45:20', '2022-05-13 16:58:18', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1482998044043763714', NULL, '0', '1525037609539010561', '计费方案', NULL, 1.00, 1, 1, '/collect-fee/billing-scheme', 'collectFeeBillingScheme', '/park-fee', 0, '2022-01-17 16:47:41', '2022-05-13 16:59:09', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1482998044794544129', '1482998044043763714', '1437679155114393601', '1525037610885382146', '计费方案', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-17 16:47:41', '2022-05-13 16:59:09', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1483693246415196162', NULL, '0', '1479367665950994434', '通行策略', NULL, 3.00, 1, 1, '/person-pass/access-policy', 'personPassAccessPolicy', '/park-visitor', 0, '2022-01-19 14:50:10', '2022-03-09 15:16:06', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1483693246973038594', '1483693246415196162', '1437679155114393601', '1479367666378813441', '通行策略', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-19 14:50:10', '2022-03-09 15:16:06', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1484083621441572865', NULL, '0', '1446677983683354626', '活动管理', NULL, 6.00, 1, 1, '/customer-service/activity-list', 'customerServiceActivityManagement', '/park-customer', 0, '2022-01-20 16:41:22', '2022-03-09 15:19:02', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1484083621789700098', '1484083621441572865', '1437679155114393601', '1446677984035676162', '活动管理', NULL, 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-20 16:41:23', '2022-03-09 15:19:02', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1484084169221869570', NULL, '0', '1484083621441572865', '报名记录', NULL, 0.00, 1, 1, '/customer-service/activity-management/sign-up-record', 'customerServiceActivitySignUpRecord', '/park-customer', 2, '2022-01-20 16:43:33', '2022-01-20 16:43:33', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1484084169469333506', '1484084169221869570', '1437679155114393601', '1484083621789700098', '报名记录', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-01-20 16:43:33', '2022-01-20 16:43:33', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1492086157699121153', NULL, '0', '1544865898860314626', '考勤管理', NULL, 19.00, 1, 1, '/attendance', 'attendance', '/park-attendance', 0, '2022-02-11 18:40:36', '2022-07-07 11:00:40', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1492086158202437634', '1492086157699121153', '1437679155114393601', '1544865898923229186', '考勤管理', NULL, 19.00, 1, 1, NULL, NULL, NULL, NULL, '2022-02-11 18:40:36', '2022-07-07 11:00:40', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1492086255975858177', NULL, '0', '1492086157699121153', '考勤规则', NULL, 0.00, 1, 1, '/attendance/attendance-rules', 'attendanceAttendanceRules', '/park-attendance', 0, '2022-02-11 18:40:59', '2022-02-11 18:40:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1492086256474980354', '1492086255975858177', '1437679155114393601', '1492086158202437634', '考勤规则', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-02-11 18:40:59', '2022-02-11 18:40:59', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499724221573431298', NULL, '0', '1492086157699121153', '月度考勤', NULL, 1.00, 1, 1, '/attendance/month-attendance', 'monthAttendance', '/park-attendance', 0, '2022-03-04 20:31:32', '2022-03-04 20:31:32', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499724222366154753', '1499724221573431298', '1437679155114393601', '1492086158202437634', '月度考勤', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:31:32', '2022-03-04 20:31:32', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499724322031206402', NULL, '0', '1492086157699121153', '每日考勤', NULL, 2.00, 1, 1, '/attendance/day-attendance', 'dayAttendance', '/park-attendance', 0, '2022-03-04 20:31:56', '2022-03-09 15:15:34', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499724322236727298', '1499724322031206402', '1437679155114393601', '1492086158202437634', '每日考勤', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:31:56', '2022-03-09 15:15:34', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499724399487418369', NULL, '0', '1492086157699121153', '打卡记录', NULL, 3.00, 1, 1, '/attendance/clock-record', 'clockRecord', '/park-attendance', 0, '2022-03-04 20:32:15', '2022-03-09 15:15:41', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499724399755853826', '1499724399487418369', '1437679155114393601', '1492086158202437634', '打卡记录', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:32:15', '2022-03-09 15:15:41', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499724493859258370', NULL, '0', '1492086157699121153', '外勤申请记录', NULL, 4.00, 1, 1, '/attendance/field-application', 'fieldApplication', '/park-attendance', 0, '2022-03-04 20:32:37', '2022-03-09 15:15:46', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499724494131888129', '1499724493859258370', '1437679155114393601', '1492086158202437634', '外勤申请记录', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:32:37', '2022-03-09 15:15:46', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499724699644395521', NULL, '0', '1446677983683354626', '广告管理 ', NULL, 5.00, 1, 1, '/advertisement', 'customerServiceAdvertisement', '/customer-service/park-customer', 0, '2022-03-04 20:33:26', '2022-03-09 15:18:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499724700080603138', '1499724699644395521', '1437679155114393601', '1446677984035676162', '广告管理 ', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:33:26', '2022-03-09 15:18:54', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499724838312280066', NULL, '0', '1450758892792205314', '客户管理', NULL, 4.00, 1, 1, '/customer-manage', 'customerManage', '/wisdom-canvass-business', 0, '2022-03-04 20:33:59', '2022-03-09 15:09:23', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499724838559744001', '1499724838312280066', '1437679155114393601', '1450758893001920513', '客户管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:33:59', '2022-03-09 15:09:23', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499724986945830914', NULL, '0', '1499724838312280066', '客户列表', NULL, 0.00, 1, 1, '/customer-management', 'customerManagement', '/wisdom-canvass-business', 0, '2022-03-04 20:34:35', '2022-03-04 20:34:35', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499724987138768898', '1499724986945830914', '1437679155114393601', '1499724838559744001', '客户列表', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:34:35', '2022-03-04 20:34:35', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499725086715740161', NULL, '0', '1499724838312280066', '客户标签管理', NULL, 1.00, 1, 1, '/customer-tag', 'customerTag', '/wisdom-canvass-business', 0, '2022-03-04 20:34:58', '2022-03-09 15:09:45', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499725086879318018', '1499725086715740161', '1437679155114393601', '1499724838559744001', '客户标签管理', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:34:58', '2022-03-09 15:09:45', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499725740859392001', NULL, '0', '1478695869169872897', '设备台账', NULL, 10.00, 1, 1, '/equipment-management/equipment-account', 'equipmentManagementEquipmentAccount', '/park-device', 0, '2022-03-04 20:37:34', '2022-03-09 15:15:04', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499725741039747073', '1499725740859392001', '1437679155114393601', '1478695869564137474', '设备台账', NULL, 10.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:37:34', '2022-03-09 15:15:04', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499726316649250818', NULL, '0', '1544870728823668737', '资产管理', NULL, 29.00, 1, 1, '/asset-mangement', 'assetMangement', '/park-asset', 0, '2022-03-04 20:39:52', '2022-07-07 11:08:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499726316758302721', '1499726316649250818', '1437679155114393601', '1544870728857223170', '资产管理', NULL, 29.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:39:52', '2022-07-07 11:08:54', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499726451097665538', NULL, '0', '1499726316649250818', '物品管理', NULL, 0.00, 1, 1, '/asset-mangement/good-mangement', 'assetMangementGoodsMangement', '/park-asset', 0, '2022-03-04 20:40:24', '2022-03-04 20:40:24', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499726451340935169', '1499726451097665538', '1437679155114393601', '1499726316758302721', '物品管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:40:24', '2022-03-04 20:40:24', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499726996621426689', NULL, '0', '1499726316649250818', '库存管理', NULL, 1.00, 1, 1, '/asset-mangement/inventory-management', 'assetMangementInventoryManagement', '/park-asset', 0, '2022-03-04 20:42:34', '2022-03-09 15:17:51', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499726996793393153', '1499726996621426689', '1437679155114393601', '1499726316758302721', '库存管理', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:42:34', '2022-03-09 15:17:51', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499727122823839746', NULL, '0', '1499726316649250818', '领用管理', NULL, 2.00, 1, 1, '/asset-mangement/receive-mangement', 'assetMangementReceiveMangement', '/park-asset', 0, '2022-03-04 20:43:04', '2022-03-09 15:18:01', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499727122983223298', '1499727122823839746', '1437679155114393601', '1499726316758302721', '领用管理', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:43:04', '2022-03-09 15:18:01', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499727220295270401', NULL, '0', '1499726316649250818', '领用权限设置', NULL, 3.00, 1, 1, '/asset-mangement/receive-audit', 'assetMangementReceiveAudit', '/park-asset', 0, '2022-03-04 20:43:27', '2022-03-09 15:18:19', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499727220484014081', '1499727220295270401', '1437679155114393601', '1499726316758302721', '领用权限设置', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:43:27', '2022-03-09 15:18:19', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499727340424331265', NULL, '0', '1499726316649250818', '出入库记录', NULL, 4.00, 1, 1, '/asset-mangement/out-in-stock-record', 'assetMangementOutInStockRecord', '/park-asset', 0, '2022-03-04 20:43:56', '2022-03-09 15:18:24', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499727340579520514', '1499727340424331265', '1437679155114393601', '1499726316758302721', '出入库记录', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:43:56', '2022-03-09 15:18:24', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499727442475941889', NULL, '0', '1499726316649250818', '盘点记录', NULL, 5.00, 1, 1, '/asset-mangement/calculatio-management', 'assetMangementCalculationManagement', '/park-asset', 0, '2022-03-04 20:44:20', '2022-03-09 15:18:31', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499727442639519746', '1499727442475941889', '1437679155114393601', '1499726316758302721', '盘点记录', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:44:20', '2022-03-09 15:18:31', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499727592023851009', NULL, '0', '1544864968110702593', '智慧能源', NULL, 15.00, 1, 1, '/smarter-energy', 'smarterEnergy', '/smart-park', 0, '2022-03-04 20:44:56', '2022-07-07 11:06:33', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499727592158068737', '1499727592023851009', '1437679155114393601', '1544864968194588674', '智慧能源', NULL, 15.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:44:56', '2022-07-07 11:06:33', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499728097391345666', NULL, '0', '1478695869169872897', '水表管理', NULL, 4.00, 1, 1, '/smarter-energy/water-meter-management', 'smarterEnergyWaterMeterManagement', '/smart-park', 0, '2022-03-04 20:46:56', '2022-04-15 18:43:05', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499728097559117826', '1499728097391345666', '1437679155114393601', '1478695869564137474', '水表管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:46:56', '2022-04-15 18:43:06', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499728208284549121', NULL, '0', '1478695869169872897', '电表管理', NULL, 5.00, 1, 1, '/ammeter-management/smarter-energy', 'smarterEnergyAmmeterManagement', '/smart-park', 0, '2022-03-04 20:47:23', '2022-04-15 18:43:17', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499728208464904193', '1499728208284549121', '1437679155114393601', '1478695869564137474', '电表管理', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:47:23', '2022-04-15 18:43:17', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499728309052702722', NULL, '0', '1499727592023851009', '表计用量记录', NULL, 1.00, 1, 1, '/smarter-energy/operating-record', 'smarterEnergyOperatingRecord', '/smart-park', 0, '2022-03-04 20:47:47', '2022-06-07 14:51:02', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499728309270806530', '1499728309052702722', '1437679155114393601', '1499727592158068737', '表计用量记录', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:47:47', '2022-06-07 14:51:02', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499728414300372994', NULL, '0', '1499727592023851009', '抄表记录', NULL, 0.00, 1, 1, '/smarter-energy/check-meter', 'smarterEnergyCheckMeter', '/smart-park', 0, '2022-03-04 20:48:12', '2022-03-04 20:48:12', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499728414463950849', '1499728414300372994', '1437679155114393601', '1499727592158068737', '抄表记录', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:48:12', '2022-03-04 20:48:12', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499728546819407874', NULL, '0', '1499727592023851009', '能耗分析', NULL, 5.00, 1, 1, '/smarter-energy/energy-analysis', 'smarterEnergyEnergyAnalysis', '/smart-park', 0, '2022-03-04 20:48:43', '2022-06-07 14:51:41', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499728546999762945', '1499728546819407874', '1437679155114393601', '1499727592158068737', '能耗分析', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:48:43', '2022-06-07 14:51:41', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1499728635759624194', NULL, '0', '1499727592023851009', '异常告警', NULL, 4.00, 1, 1, '/smarter-energy/unusual-warn', 'smarterEnergyUnusualWarn', '/smart-park', 0, '2022-03-04 20:49:05', '2022-06-07 14:51:36', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1499728635935784961', '1499728635759624194', '1437679155114393601', '1499727592158068737', '异常告警', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-04 20:49:05', '2022-06-07 14:51:36', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1501458280628551681', NULL, '0', '1544865898860314626', '智慧会议室', NULL, 17.00, 1, 1, 'meeting-manage', 'meetingManage', 'meeting-manage', 0, '2022-03-09 15:22:04', '2022-07-07 11:00:27', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1501458280725020673', '1501458280628551681', '1437679155114393601', '1544865898923229186', '智慧会议室', NULL, 17.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-09 15:22:04', '2022-07-07 11:00:27', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1501458588989587457', NULL, '0', '1501458280628551681', '会议室管理', NULL, 1.00, 1, 1, '/meeting-list', 'meetingList', '/meeting-manage', 0, '2022-03-09 15:23:18', '2022-05-03 18:07:40', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1501458589127999489', '1501458588989587457', '1437679155114393601', '1501458280725020673', '会议室管理', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-09 15:23:18', '2022-05-03 18:07:40', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1501458722326511617', NULL, '0', '1501458280628551681', '会议室分布图', NULL, 2.00, 1, 1, '/meeting-map', 'meetingMap', '/meeting-map', 0, '2022-03-09 15:23:49', '2022-03-09 15:23:49', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1501458722485895170', '1501458722326511617', '1437679155114393601', '1501458280725020673', '会议室分布图', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-09 15:23:49', '2022-03-09 15:23:49', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1501459021225197569', NULL, '0', '1446677983683354626', '物品放行', NULL, 7.00, 1, 1, '/customer-service/park-customer', 'customerServiceGoodsRelease', '/goods-release', 0, '2022-03-09 15:25:01', '2022-03-09 15:25:01', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1501459021384581121', '1501459021225197569', '1437679155114393601', '1446677984035676162', '物品放行', NULL, 7.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-09 15:25:01', '2022-03-09 15:25:01', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1504804677293637634', NULL, '0', '1446677983683354626', '会议室租赁', NULL, 8.00, 1, 1, '/meeting-room-rental', 'meetingRoomManage', '/customer-service', 0, '2022-03-18 20:59:27', '2022-03-18 21:30:58', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1504804677796954114', '1504804677293637634', '1437679155114393601', '1446677984035676162', '会议室租赁', NULL, 8.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-18 20:59:27', '2022-03-18 21:30:58', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1504812727106732033', NULL, '0', '1504804677293637634', '物资管理', NULL, 2.00, 1, 1, '/material-manage', 'materialManage', '/customer-service', 0, '2022-03-18 21:31:26', '2022-03-18 21:41:12', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1504812727236755457', '1504812727106732033', '1437679155114393601', '1504804677796954114', '物资管理', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-18 21:31:26', '2022-03-18 21:41:13', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1504812818605473793', NULL, '0', '1504804677293637634', '订单管理', NULL, 3.00, 1, 1, '/order-manage', 'orderManage', '/customer-service', 0, '2022-03-18 21:31:48', '2022-03-18 21:41:17', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1504812818769051650', '1504812818605473793', '1437679155114393601', '1504804677796954114', '订单管理', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-18 21:31:48', '2022-03-18 21:41:17', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1504812898389524481', NULL, '0', '1504804677293637634', '会议室管理', NULL, 1.00, 1, 1, '/meeting-room-manage', 'meetingRoomManage', '/customer-service', 0, '2022-03-18 21:32:07', '2022-03-18 21:40:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1504812898523742210', '1504812898389524481', '1437679155114393601', '1504804677796954114', '会议室管理', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-18 21:32:07', '2022-03-18 21:40:54', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1504819936930758657', NULL, '0', '1544864968110702593', '智慧消防', NULL, 13.00, 1, 1, '/smarter-fire', 'smarterFire', '/smart-park', 0, '2022-03-18 22:00:05', '2022-07-07 11:06:29', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1504819937023033345', '1504819936930758657', '1437679155114393601', '1544864968194588674', '智慧消防', NULL, 13.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-18 22:00:05', '2022-07-07 11:06:29', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1504820046381121538', NULL, '0', '1504819936930758657', '报警记录', NULL, 0.00, 1, 1, '/smarter-fire/alarm-log', 'smarterAlarmLogManagement', '/smart-park', 0, '2022-03-18 22:00:31', '2022-03-18 22:00:31', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1504820046515339265', '1504820046381121538', '1437679155114393601', '1504819937023033345', '报警记录', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-18 22:00:31', '2022-03-18 22:00:31', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1504834246595772417', NULL, '0', '1478695869169872897', '消防设备管理', NULL, 6.00, 1, 1, '/equipment-management/equipment-fire', 'fireFightingEquipment', '/park-device', 0, '2022-03-18 22:56:57', '2022-04-15 18:43:32', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1504834246721601537', '1504834246595772417', '1437679155114393601', '1478695869564137474', '消防设备管理', NULL, 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-18 22:56:57', '2022-04-15 18:43:33', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1505783546658095106', NULL, '0', '1475742604231491585', '来访须知', NULL, 5.00, 1, 1, '/visitors-management/visiting-instructions', 'visitorsConfigurationManagementVisitingInstructions', '/park-visitor', 0, '2022-03-21 13:49:08', '2022-06-07 14:54:44', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1505783546796507138', '1505783546658095106', '1437679155114393601', '1475742604818694146', '来访须知', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-03-21 13:49:08', '2022-06-07 14:54:44', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509768601483018242', NULL, '0', '1480813318691098626', '房屋状态查询', NULL, 6.00, 1, 1, '/contract-manage/house-status', 'houseStatus', '/contract-manage', 0, '2022-04-01 13:44:19', '2022-04-15 18:36:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509768601613041666', '1509768601483018242', '1437679155114393601', '1480813318863065089', '房屋状态查询', NULL, 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 13:44:19', '2022-04-15 18:36:59', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509768926839373826', NULL, '0', '1441967553601794050', '企业入驻审核', NULL, 0.00, 1, 1, '/company-check', 'companyCheck', '/company-mangement', 0, '2022-04-01 13:45:36', '2022-04-01 13:45:36', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509768926994563073', '1509768926839373826', '1437679155114393601', '1441967553996058626', '企业入驻审核', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 13:45:37', '2022-04-01 13:45:37', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509769055684198402', NULL, '0', '1441967553601794050', '企业信息审核', NULL, 0.00, 1, 1, '/company-info-check', 'companyInfoCheck', '/company-mangement', 0, '2022-04-01 13:46:07', '2022-04-01 13:46:07', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509769055814221826', '1509769055684198402', '1437679155114393601', '1441967553996058626', '企业信息审核', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 13:46:07', '2022-04-01 13:46:07', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509769246269177857', NULL, '0', '1413108475773542402', '企业端管理', NULL, 4.00, 1, 1, '/system-management/company', 'companyManagement', '/system-management', 0, '2022-04-01 13:46:53', '2022-04-01 20:52:24', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509769246420172802', '1509769246269177857', '1437679155114393601', '1437679155743539204', '企业端管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 13:46:53', '2022-04-01 20:52:24', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509772997566595073', NULL, '0', '0', '产业服务', 'service', 13.00, 1, 1, '/enterprise-service', 'enterpriseService', '/park-system', 0, '2022-04-01 14:01:47', '2022-07-07 10:32:20', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509772997650481154', '1509772997566595073', '1437679155114393601', '0', '产业服务', 'service', 13.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:01:47', '2022-07-07 10:32:20', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773098590601218', NULL, '0', '1509772997566595073', '企业产业链服务', NULL, 1.00, 1, 1, '/enterprise-service/enterpriseServiceList', 'enterpriseServiceMsg', '/park-system', 0, '2022-04-01 14:02:11', '2022-04-01 20:33:15', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773098724818945', '1509773098590601218', '1437679155114393601', '1509772997650481154', '企业产业链服务', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:02:11', '2022-04-01 20:33:15', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773196666011650', NULL, '0', '1509772997566595073', '供应服务', NULL, 4.00, 1, 1, '/enterprise-service/supplyServices', 'supplyServices', '/park-industrial', 0, '2022-04-01 14:02:34', '2022-04-01 20:33:36', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773196821200898', '1509773196666011650', '1437679155114393601', '1509772997650481154', '供应服务', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:02:35', '2022-04-01 20:33:36', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773290744250369', NULL, '0', '1509772997566595073', '企业服务', NULL, 3.00, 1, 1, '/enterprise-service/companyService', 'companyService', '/park-industrial', 0, '2022-04-01 14:02:57', '2022-04-01 20:33:29', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773290874273793', '1509773290744250369', '1437679155114393601', '1509772997650481154', '企业服务', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:02:57', '2022-04-01 20:33:29', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773377142718466', NULL, '0', '1509772997566595073', '采购服务', NULL, 5.00, 1, 1, '/enterprise-service/purchaseService', 'purchaseService', '/purchase-service', 0, '2022-04-01 14:03:18', '2022-04-01 20:34:09', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773377268547585', '1509773377142718466', '1437679155114393601', '1509772997650481154', '采购服务', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:03:18', '2022-04-01 20:34:09', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773505232568322', NULL, '0', '1446677983683354626', '满意度调查', NULL, 34.00, 1, 1, '/satisfaction-survey', 'satisfactionSurvey', '/park-customer', 0, '2022-04-01 14:03:48', '2022-07-07 10:43:18', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773505320648706', '1509773505232568322', '1437679155114393601', '1446677984035676162', '满意度调查', NULL, 34.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:03:48', '2022-07-07 10:43:18', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773597318512642', NULL, '0', '1509773505232568322', '问卷管理', NULL, 0.00, 1, 1, '/satisfaction-survey/questionnaire-list', 'satisfactionSurveyQuestionnaireList', '/park-customer', 0, '2022-04-01 14:04:10', '2022-04-01 14:04:10', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773597440147457', '1509773597318512642', '1437679155114393601', '1509773505320648706', '问卷管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:04:10', '2022-04-01 14:04:10', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773700049600514', NULL, '0', '1544865898860314626', '信息统计', NULL, 35.00, 1, 1, '/information-statistical', 'informationStatistical', '/park-customer', 0, '2022-04-01 14:04:34', '2022-07-07 11:00:45', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773700154458114', '1509773700049600514', '1437679155114393601', '1544865898923229186', '信息统计', NULL, 35.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:04:35', '2022-07-07 11:00:45', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773825455095810', NULL, '0', '1509773700049600514', '表单模板', NULL, 0.00, 1, 1, '/information-statistical//form-template', 'informationStatisticalFormTemplate', '/park-customer', 0, '2022-04-01 14:05:04', '2022-04-01 14:05:04', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773825618673666', '1509773825455095810', '1437679155114393601', '1509773700154458114', '表单模板', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:05:04', '2022-04-01 14:05:04', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509773920850345985', NULL, '0', '1509773700049600514', '表单管理', NULL, 0.00, 1, 1, '/information-statistical/form-manage', 'informationStatisticalFormManage', '/park-customer', 0, '2022-04-01 14:05:27', '2022-04-01 14:05:27', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509773920980369409', '1509773920850345985', '1437679155114393601', '1509773700154458114', '表单管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:05:27', '2022-04-01 14:05:27', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509782850477424641', NULL, '0', '1509773597318512642', '问卷设计', NULL, 0.00, 1, 1, '/satisfaction-survey/design-questionnaire', 'satisfactionSurveyDesignQuestionnaire', '/park-customer', 2, '2022-04-01 14:40:56', '2022-04-01 14:40:56', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509782850607448065', '1509782850477424641', '1437679155114393601', '1509773597440147457', '问卷设计', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:40:56', '2022-04-01 14:40:56', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509782961819418625', NULL, '0', '1509773597318512642', '收集统计', NULL, 0.00, 1, 1, '/satisfaction-survey/data-statistics', 'satisfactionSurveyDataStatistics', '/park-customer', 2, '2022-04-01 14:41:23', '2022-04-01 14:41:23', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509782961949442050', '1509782961819418625', '1437679155114393601', '1509773597440147457', '收集统计', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:41:23', '2022-04-01 14:41:23', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509783044002611201', NULL, '0', '1509773597318512642', '数据分析', NULL, 0.00, 1, 1, '/satisfaction-survey/analysis-report', 'satisfactionSurveyAnalysisReport', '/park-customer', 2, '2022-04-01 14:41:42', '2022-04-01 14:41:52', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509783044174577665', '1509783044002611201', '1437679155114393601', '1509773597440147457', '数据分析', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:41:42', '2022-04-01 14:41:42', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509783210076078082', NULL, '0', '1509773597318512642', '答案详情', NULL, 0.00, 1, 1, '/satisfaction-survey/answer-details', 'satisfactionSurveyAnswerDetails', '/park-customer', 2, '2022-04-01 14:42:22', '2022-04-01 14:42:22', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509783210269016066', '1509783210076078082', '1437679155114393601', '1509773597440147457', '答案详情', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:42:22', '2022-04-01 14:42:22', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509783391601360897', NULL, '0', '1509773825455095810', '表单模板设计', NULL, 0.00, 1, 1, '/information-statistical/design-questionnaire', 'informationDesignQuestionnaire', '/park-customer', 2, '2022-04-01 14:43:05', '2022-04-06 15:05:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509783391731384321', '1509783391601360897', '1437679155114393601', '1509773825618673666', '表单模板设计', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:43:05', '2022-04-06 15:05:55', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1509783698003656705', NULL, '0', '1509773377142718466', '投标信息详情', NULL, 0.00, 1, 1, '/enterprise-service/enterprise-service', 'bidService', '/purchase-service', 2, '2022-04-01 14:44:18', '2022-04-01 20:10:25', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1509783698146263042', '1509783698003656705', '1437679155114393601', '1509773377268547585', '投标信息详情', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-01 14:44:18', '2022-04-01 14:44:18', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514886655544066049', NULL, '0', '0', '智慧公寓', 'hot', 10.00, 1, 1, '/smarter-apartment', 'smarterApartment', '/park-visitor', 0, '2022-04-15 16:41:38', '2022-07-07 10:31:50', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514886655636340737', '1514886655544066049', '1437679155114393601', '0', '智慧公寓', 'hot', 10.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:41:38', '2022-07-07 10:31:50', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514886829880311809', NULL, '0', '1514886655544066049', '公寓人员管理', NULL, 0.00, 1, 1, '/smarter-apartment/personnel-management', 'ApartmentPersonnelManagement', '/park-visitor', 0, '2022-04-15 16:42:20', '2022-04-15 16:42:20', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514886830094221313', '1514886829880311809', '1437679155114393601', '1514886655636340737', '公寓人员管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:42:20', '2022-04-15 16:42:20', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514886973157736449', NULL, '0', '1478695869169872897', '卡片管理', NULL, 3.00, 1, 1, '/equipment-management/card-management', 'equipmentManagementCard', '/park-device', 0, '2022-04-15 16:42:54', '2022-04-15 18:42:30', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514886973308731394', '1514886973157736449', '1437679155114393601', '1478695869564137474', '卡片管理', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:42:54', '2022-04-15 18:42:30', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514887089327374337', NULL, '0', '1478695869169872897', '车辆设备管理', NULL, 1.00, 1, 1, '/equipment-management/equipment-vehicle', 'equipmentManagementEquipmentVehicle', '/park-device', 0, '2022-04-15 16:43:21', '2022-04-15 18:42:04', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514887089507729409', '1514887089327374337', '1437679155114393601', '1478695869564137474', '车辆设备管理', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:43:22', '2022-04-15 18:42:04', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514887175482572801', NULL, '0', '1478695869169872897', '门禁管理', NULL, 0.00, 1, 1, '/equipment-management/equipment-entranceGuard', 'equipmentManagementEquipmentEntranceGuard', '/park-device', 0, '2022-04-15 16:43:42', '2022-04-15 16:43:42', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514887175616790529', '1514887175482572801', '1437679155114393601', '1478695869564137474', '门禁管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:43:42', '2022-04-15 16:43:42', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514887257904840706', NULL, '0', '1478695869169872897', '视频管理', NULL, 2.00, 1, 1, '/equipment-management/equipmentManagementEquipmentVideo', 'equipmentManagementEquipmentVideo', '/park-device', 0, '2022-04-15 16:44:02', '2022-04-15 18:42:14', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514887258034864130', '1514887257904840706', '1437679155114393601', '1478695869564137474', '视频管理', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:44:02', '2022-04-15 18:42:14', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514888363137499138', NULL, '0', '1480813318691098626', '入园协议', NULL, 1.00, 1, 1, '/contract-manage/enter-park-contract', 'enterParkContract', '/contract-manage', 0, '2022-04-15 16:48:25', '2022-04-15 18:36:08', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514888363271716865', '1514888363137499138', '1437679155114393601', '1480813318863065089', '入园协议', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:48:25', '2022-04-15 18:36:08', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514888454233587714', NULL, '0', '1480813318691098626', '招引合同', NULL, 2.00, 1, 1, '/contract-manage/attract-contract', 'attractContract', '/contract-manage', 0, '2022-04-15 16:48:47', '2022-04-15 18:36:14', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514888454443302913', '1514888454233587714', '1437679155114393601', '1480813318863065089', '招引合同', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:48:47', '2022-04-15 18:36:14', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514888540195848194', NULL, '0', '1480813318691098626', '租赁合同', NULL, 3.00, 1, 1, '/contract-manage/lease-contract', 'leaseContract', '/contract-manage', 0, '2022-04-15 16:49:07', '2022-04-15 18:38:31', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514888540338454529', '1514888540195848194', '1437679155114393601', '1480813318863065089', '租赁合同', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:49:07', '2022-04-15 18:38:31', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514888615777206273', NULL, '0', '1480813318691098626', '物业合同', NULL, 4.00, 1, 1, '/contract-manage/property-contract', 'propertyContract', '/contract-manage', 0, '2022-04-15 16:49:25', '2022-04-15 18:38:35', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514888615974338561', '1514888615777206273', '1437679155114393601', '1480813318863065089', '物业合同', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:49:25', '2022-04-15 18:38:35', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514888689240440833', NULL, '0', '1480813318691098626', '公寓合同', NULL, 5.00, 1, 1, '/contract-manage/apartment-contract', 'apartmentContract', '/contract-manage', 0, '2022-04-15 16:49:43', '2022-04-15 18:38:39', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514888689370464257', '1514888689240440833', '1437679155114393601', '1480813318863065089', '公寓合同', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:49:43', '2022-04-15 18:38:39', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514888801861697537', NULL, '0', '1480813318691098626', '合同模板', NULL, 0.00, 1, 1, '/contract-manage/contract-template', 'contractTemplate', '/contract-manage', 0, '2022-04-15 16:50:10', '2022-04-15 18:38:21', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514888802000109569', '1514888801861697537', '1437679155114393601', '1480813318863065089', '合同模板', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:50:10', '2022-04-15 18:38:21', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514888930840739841', NULL, '0', '1514888801861697537', '合同模板', NULL, 0.00, 1, 1, '/contract-manage/contract-template/template-list', 'contractContractTemplateList', '/contract-manage', 0, '2022-04-15 16:50:41', '2022-04-15 16:50:41', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514888930987540482', '1514888930840739841', '1437679155114393601', '1514888802000109569', '合同模板', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:50:41', '2022-04-15 16:50:41', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514889028073095170', NULL, '0', '1514888801861697537', '模板字段库', NULL, 0.00, 1, 1, '/contract-manage/contract-template/template-field-lib', 'contractContractTemplateFieldLib', '/contract-manage', 0, '2022-04-15 16:51:04', '2022-04-15 16:51:04', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514889028224090114', '1514889028073095170', '1437679155114393601', '1514888802000109569', '模板字段库', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 16:51:04', '2022-04-15 16:51:04', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514938787550920706', NULL, '0', '1509772997566595073', '企业群组管理', NULL, 0.00, 1, 1, '/enterprise-service/enterpriseServiceCompanyGroup', 'enterpriseServicecompanyGroup', '/park-common', 0, '2022-04-15 20:08:47', '2022-04-15 20:08:47', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514938787685138433', '1514938787550920706', '1437679155114393601', '1509772997650481154', '企业群组管理', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 20:08:47', '2022-04-15 20:08:47', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514947147763482625', NULL, '0', '1480813318691098626', '起草合同', NULL, 0.00, 1, 1, '/contract-manage/contract-draft', 'contractDraft', '/contract-manage/contract-draft', 2, '2022-04-15 20:42:01', '2022-04-15 20:42:01', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514947148245827585', '1514947147763482625', '1437679155114393601', '1480813318863065089', '起草合同', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 20:42:01', '2022-04-15 20:42:01', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1514947248049291265', NULL, '0', '1480813318691098626', '编辑合同', NULL, 0.00, 1, 1, '/contract-manage/contract-draft', 'contractDraftEdit', '/contract-manage/contract-draft', 2, '2022-04-15 20:42:24', '2022-04-15 20:42:24', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1514947248204480513', '1514947248049291265', '1437679155114393601', '1480813318863065089', '编辑合同', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-04-15 20:42:24', '2022-04-15 20:42:24', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522104951515316226', NULL, '0', '1501458280628551681', '预定概览', NULL, 0.00, 1, 1, '/meeting-manage/scheduled-overview', 'scheduledOverview', '/park-device', 2, '2022-05-05 14:44:34', '2022-05-05 14:44:34', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522104951746002945', '1522104951515316226', '1437679155114393601', '1501458280725020673', '预定概览', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:44:34', '2022-05-05 14:44:34', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522105141018165250', NULL, '0', '1478695869169872897', '会议室信息屏', NULL, 7.00, 1, 1, '/equipment-management//conference-screen', 'conferenceRoomInformationScreen', '/park-device', 0, '2022-05-05 14:45:19', '2022-05-05 18:43:01', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522105141181743105', '1522105141018165250', '1437679155114393601', '1478695869564137474', '会议室信息屏', NULL, 7.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:45:19', '2022-05-05 18:43:01', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522105340406988802', NULL, '0', '1482951933643972610', '租赁费用管理', NULL, 1.00, 1, 1, '/collect-fee/rent-feeMange', 'rentFeeMangeUnknownFee', '/park-contract', 0, '2022-05-05 14:46:07', '2022-05-27 15:41:52', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522105340620898305', '1522105340406988802', '1437679155114393601', '1482951934168260610', '租赁费用管理', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:46:07', '2022-05-27 15:41:52', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522105448389345282', NULL, '0', '1522105340406988802', '离场结算记录', NULL, 7.00, 1, 1, '/collect-fee/departure-settlement-record', 'departureSettlementRecord', '/park-contract', 0, '2022-05-05 14:46:32', '2022-05-05 18:34:20', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522105448540340226', '1522105448389345282', '1437679155114393601', '1522105340620898305', '离场结算记录', NULL, 7.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:46:32', '2022-05-05 18:34:20', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522105803617533954', NULL, '0', '1522105448389345282', '离场结算', NULL, 0.00, 1, 1, '/collect-fee/departure-settlement-record/departure-settlement', 'departureSettlement', '/park-contract', 2, '2022-05-05 14:47:57', '2022-05-05 14:47:57', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522105803823054850', '1522105803617533954', '1437679155114393601', '1522105448540340226', '离场结算', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:47:57', '2022-05-05 14:47:57', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522105938074337282', NULL, '0', '1522105340406988802', '未出费用', NULL, 0.00, 1, 1, '/collect-fee/rent-feeMange/unknown-fee', 'rentFeeMangeUnknownFee', '/park-contract', 0, '2022-05-05 14:48:29', '2022-05-05 14:48:29', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522105938237915138', '1522105938074337282', '1437679155114393601', '1522105340620898305', '未出费用', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:48:29', '2022-05-05 14:48:29', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522106116902682625', NULL, '0', '1522105340406988802', '已出费用', NULL, 1.00, 1, 1, '/collect-fee/rent-feeMange/final-fee', 'rentFeeMangeFinalFee', '/park-contract', 0, '2022-05-05 14:49:12', '2022-05-05 18:34:27', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522106117099814914', '1522106116902682625', '1437679155114393601', '1522105340620898305', '已出费用', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:49:12', '2022-05-05 18:34:27', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522106218329341954', NULL, '0', '1522105340406988802', '账单管理', NULL, 4.00, 1, 1, '/collect-fee/rent-feeMange/bill', 'rentFeeMangebill', '/park-contract', 0, '2022-05-05 14:49:36', '2022-05-05 18:35:40', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522106218476142593', '1522106218329341954', '1437679155114393601', '1522105340620898305', '账单管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:49:36', '2022-05-05 18:35:40', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522106341574770690', NULL, '0', '1522105340406988802', '交易明细', NULL, 5.00, 1, 1, '/collect-fee/rent-feeMange/transaction-detail', 'rentFeeMangeTransactionDetail', '/park-contract', 0, '2022-05-05 14:50:05', '2022-05-05 18:35:08', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522106341721571330', '1522106341574770690', '1437679155114393601', '1522105340620898305', '交易明细', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:50:05', '2022-05-05 18:35:08', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522106484806057986', NULL, '0', '1522106341574770690', '交易明细详情', NULL, 0.00, 1, 1, '/rent-feeMange/transaction-detail/details', 'rentFeeMangeTransactionDetailDetails', '/park-contract', 2, '2022-05-05 14:50:40', '2022-05-05 14:50:40', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522106484969635842', '1522106484806057986', '1437679155114393601', '1522106341721571330', '交易明细详情', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:50:40', '2022-05-05 14:50:40', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522106647758962689', NULL, '0', '1522105340406988802', '滞纳金管理', NULL, 3.00, 1, 1, '/collect-fee/rent-feeMange/late-fee', 'rentFeeMangeLateFee', '/park-contract', 0, '2022-05-05 14:51:18', '2022-05-05 18:34:44', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522106647901569026', '1522106647758962689', '1437679155114393601', '1522105340620898305', '滞纳金管理', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:51:18', '2022-05-05 18:34:44', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522106772052967426', NULL, '0', '1522105340406988802', '押金管理', NULL, 2.00, 1, 1, '/collect-fee/rent-feeMange/deposit-fee', 'rentFeeMangeDepositFee', '/park-contract', 0, '2022-05-05 14:51:48', '2022-05-05 18:34:39', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522106772187185154', '1522106772052967426', '1437679155114393601', '1522105340620898305', '押金管理', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:51:48', '2022-05-05 18:34:39', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1522106893213827074', NULL, '0', '1522106772052967426', '押金详情', NULL, 0.00, 1, 1, '/collect-fee/rent-feeMange/deposit-fee/details', 'rentFeeMangeDepositFeeDetails', '/park-contract', 2, '2022-05-05 14:52:17', '2022-05-05 14:52:17', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1522106893364822018', '1522106893213827074', '1437679155114393601', '1522106772187185154', '押金详情', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-05 14:52:17', '2022-05-05 14:52:17', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525032539675168769', NULL, '0', '1482951933643972610', '物业费用管理', NULL, 2.00, 1, 1, '/collect-fee/property-feeMange', 'propertyFeeMange', '/park-contract', 0, '2022-05-13 16:37:45', '2022-05-13 16:37:45', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525032539868106754', '1525032539675168769', '1437679155114393601', '1482951934168260610', '物业费用管理', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:37:45', '2022-05-13 16:37:45', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525032674891141121', NULL, '0', '1525032539675168769', '离场结算记录', NULL, 7.00, 1, 1, '/collect-fee/property/departure-settlement-record', 'propertyDepartureSettlementRecord', '/park-contract', 0, '2022-05-13 16:38:18', '2022-05-13 16:38:18', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525032675029553153', '1525032674891141121', '1437679155114393601', '1525032539868106754', '离场结算记录', NULL, 7.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:38:18', '2022-05-13 16:38:18', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525032832819269634', NULL, '0', '1525032674891141121', '离场结算', NULL, 0.00, 1, 1, '/collect-fee/property/departure-settlement-record/departure-settlement', 'propertyDepartureSettlement', '/park-contract', 2, '2022-05-13 16:38:55', '2022-05-13 16:38:55', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525032833045762049', '1525032832819269634', '1437679155114393601', '1525032675029553153', '离场结算', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:38:55', '2022-05-13 16:38:55', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525032969490665473', NULL, '0', '1525032539675168769', '未出费用', NULL, 1.00, 1, 1, '/collect-fee/property/rent-feeMange/unknown-fee', 'propertyFeeMangeUnknownFee', '/park-contract', 0, '2022-05-13 16:39:28', '2022-05-13 16:39:28', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525032969633271809', '1525032969490665473', '1437679155114393601', '1525032539868106754', '未出费用', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:39:28', '2022-05-13 16:39:28', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525033081252089857', NULL, '0', '1525032539675168769', '已出费用', NULL, 2.00, 1, 1, '/collect-fee/property/rent-feeMange/final-fee', 'propertyFeeMangeFinalFee', '/park-contract', 0, '2022-05-13 16:39:54', '2022-05-13 16:39:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525033081474387970', '1525033081252089857', '1437679155114393601', '1525032539868106754', '已出费用', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:39:55', '2022-05-13 16:39:55', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525033179361054722', NULL, '0', '1525032539675168769', '账单', NULL, 5.00, 1, 1, '/collect-fee/property/rent-feeMange/bill', 'propertyFeeMangebill', '/park-contract', 0, '2022-05-13 16:40:18', '2022-05-13 16:40:18', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525033179499466754', '1525033179361054722', '1437679155114393601', '1525032539868106754', '账单', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:40:18', '2022-05-13 16:40:18', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525033344083955713', NULL, '0', '1525032539675168769', '交易明细', NULL, 6.00, 1, 1, '/collect-fee/property/rent-feeMange/transaction-detail', 'propertyFeeMangeTransactionDetail', '/park-contract', 0, '2022-05-13 16:40:57', '2022-05-13 16:40:57', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525033344226562049', '1525033344083955713', '1437679155114393601', '1525032539868106754', '交易明细', NULL, 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:40:57', '2022-05-13 16:40:57', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525033484123377666', NULL, '0', '1525033344083955713', '交易明细详情', NULL, 0.00, 1, 1, '/collect-fee/property/rent-feeMange/transaction-detail', 'propertyFeeMangeTransactionDetailDetails', '/park-contract', 2, '2022-05-13 16:41:31', '2022-05-13 16:41:31', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525033484274372609', '1525033484123377666', '1437679155114393601', '1525033344226562049', '交易明细详情', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:41:31', '2022-05-13 16:41:31', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525033603287748609', NULL, '0', '1525032539675168769', '滞纳金管理', NULL, 4.00, 1, 1, '/collect-fee/property/rent-feeMange/late-fee', 'propertyFeeMangeLateFee', '/park-contract', 0, '2022-05-13 16:41:59', '2022-05-13 16:41:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525033603463909377', '1525033603287748609', '1437679155114393601', '1525032539868106754', '滞纳金管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:41:59', '2022-05-13 16:41:59', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525033728760352769', NULL, '0', '1482951933643972610', '公寓费用管理', NULL, 3.00, 1, 1, '/collect-fee/apartment-feeMange', 'apartmentFeeMange', '/park-contract', 0, '2022-05-13 16:42:29', '2022-05-13 16:42:29', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525033728907153409', '1525033728760352769', '1437679155114393601', '1482951934168260610', '公寓费用管理', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:42:29', '2022-05-13 16:42:29', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034106360958977', NULL, '0', '1525033728760352769', '离场结算记录', NULL, 7.00, 1, 1, '/collect-fee/apartment/departure-settlement-record', 'apartmentDepartureSettlementRecord', '/park-contract', 0, '2022-05-13 16:43:59', '2022-05-13 16:43:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034106524536834', '1525034106360958977', '1437679155114393601', '1525033728907153409', '离场结算记录', NULL, 7.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:43:59', '2022-05-13 16:43:59', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034252796694529', NULL, '0', '1525034106360958977', '离场结算', NULL, 0.00, 1, 1, '/collect-fee/apartment/departure-settlement-record/departure-settlement', 'apartmentDepartureSettlement', '/park-contract', 2, '2022-05-13 16:44:34', '2022-05-13 16:44:34', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034252939300866', '1525034252796694529', '1437679155114393601', '1525034106524536834', '离场结算', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:44:34', '2022-05-13 16:44:34', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034375320702978', NULL, '0', '1525033728760352769', '未出费用', NULL, 1.00, 1, 1, '/collect-fee/apartment/rent-feeMange/unknown-fee', 'apartmentFeeMangeUnknownFee', '/park-contract', 0, '2022-05-13 16:45:03', '2022-05-13 16:45:03', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034375492669441', '1525034375320702978', '1437679155114393601', '1525033728907153409', '未出费用', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:45:03', '2022-05-13 16:45:03', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034459504578561', NULL, '0', '1525033728760352769', '已出费用', NULL, 2.00, 1, 1, '/collect-fee/apartment/rent-feeMange/final-fee', 'apartmentFeeMangeFinalFee', '/park-contract', 0, '2022-05-13 16:45:23', '2022-05-13 16:45:23', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034459701710849', '1525034459504578561', '1437679155114393601', '1525033728907153409', '已出费用', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:45:23', '2022-05-13 16:45:23', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034546788044802', NULL, '0', '1525033728760352769', '账单', NULL, 5.00, 1, 1, '/collect-fee/apartment/rent-feeMange/bill', 'apartmentFeeMangebill', '/park-contract', 0, '2022-05-13 16:45:44', '2022-05-13 16:45:44', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034546943234050', '1525034546788044802', '1437679155114393601', '1525033728907153409', '账单', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:45:44', '2022-05-13 16:45:44', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034654489382914', NULL, '0', '1525033728760352769', '交易明细', NULL, 6.00, 1, 1, '/collect-fee/apartment/rent-feeMange/transaction-detail', 'apartmentFeeMangeTransactionDetail', '/park-contract', 0, '2022-05-13 16:46:10', '2022-05-13 16:46:10', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034654644572162', '1525034654489382914', '1437679155114393601', '1525033728907153409', '交易明细', NULL, 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:46:10', '2022-05-13 16:46:10', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034769111322626', NULL, '0', '1525033728760352769', '滞纳金管理', NULL, 4.00, 1, 1, '/collect-fee/apartment/rent-feeMange/late-fee', 'apartmentFeeMangeLateFee', '/park-contract', 0, '2022-05-13 16:46:37', '2022-05-13 16:46:37', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034769279094785', '1525034769111322626', '1437679155114393601', '1525033728907153409', '滞纳金管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:46:37', '2022-05-13 16:46:37', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525034877622161410', NULL, '0', '1525033728760352769', '押金管理', NULL, 5.00, 1, 1, '/collect-fee/apartment/rent-feeMange/deposit-fee', 'apartmentFeeMangeDepositFee', '/park-contract', 0, '2022-05-13 16:47:03', '2022-05-13 16:47:03', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525034877760573442', '1525034877622161410', '1437679155114393601', '1525033728907153409', '押金管理', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:47:03', '2022-05-13 16:47:03', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525035067271811074', NULL, '0', '1525034877622161410', '押金明细', NULL, 0.00, 1, 1, '/collect-fee/apartment/rent-feeMange/deposit-fee/details', 'apartmentFeeMangeDepositFeeDetails', '/park-contract', 2, '2022-05-13 16:47:48', '2022-05-17 15:34:01', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525035067418611714', '1525035067271811074', '1437679155114393601', '1525034877760573442', '押金明细', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:47:48', '2022-05-13 16:47:48', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525035201929940994', NULL, '0', '1525032539675168769', '押金管理', NULL, 5.00, 1, 1, '/collect-fee/apartment/rent-feeMange/deposit-fee', 'apartmentFeeMangeDepositFee', '/park-contract', 0, '2022-05-13 16:48:20', '2022-05-13 16:48:20', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525035202080935938', '1525035201929940994', '1437679155114393601', '1525032539868106754', '押金管理', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:48:20', '2022-05-13 16:48:20', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525035302052171778', NULL, '0', '1525035201929940994', '押金明细', NULL, 0.00, 1, 1, '/collect-fee//apartment/rent-feeMange/deposit-fee/details', 'apartmentFeeMangeDepositFeeDetails', '/park-contract', 2, '2022-05-13 16:48:44', '2022-05-13 16:48:44', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525035302203166721', '1525035302052171778', '1437679155114393601', '1525035202080935938', '押金明细', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:48:44', '2022-05-13 16:48:44', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525035554951925762', NULL, '0', '1525034654489382914', '交易明细详情', NULL, 0.00, 1, 1, '/collect-fee/apartment/rent-feeMange/transaction-detail/details', 'apartmentFeeMangeTransactionDetailDetails', '/park-contract', 2, '2022-05-13 16:49:44', '2022-05-13 16:49:44', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525035555153252353', '1525035554951925762', '1437679155114393601', '1525034654644572162', '交易明细详情', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:49:44', '2022-05-13 16:49:44', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525035994124914689', NULL, '0', '1499727592023851009', '企业用量记录', NULL, 3.00, 1, 1, '/smarter-energy/enterprise-consumption', 'EnterpriseConsumption', '/park-device', 0, '2022-05-13 16:51:29', '2022-06-07 14:51:30', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525035994389155842', '1525035994124914689', '1437679155114393601', '1499727592158068737', '企业用量记录', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:51:29', '2022-06-07 14:51:30', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525036114262364161', NULL, '0', '1499727592023851009', '合同房屋用量记录', NULL, 2.00, 1, 1, '/smarter-energy/house-record', 'ContractHouseConsumptionRecord', '/park-device', 0, '2022-05-13 16:51:58', '2022-06-07 14:51:25', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525036114417553410', '1525036114262364161', '1437679155114393601', '1499727592158068737', '合同房屋用量记录', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:51:58', '2022-06-07 14:51:25', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1525037609539010561', NULL, '0', '1482951933643972610', '计费设置', NULL, 0.00, 1, 1, '/collect-fee/billing-settings', 'collectFeeBillingSettings', '/park-fee', 0, '2022-05-13 16:57:54', '2022-05-13 16:57:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1525037610885382146', '1525037609539010561', '1437679155114393601', '1482951934168260610', '计费设置', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-13 16:57:54', '2022-05-13 16:57:54', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1528924875286052865', NULL, '-1', '1528924835247226882', '222', NULL, 0.00, 1, 1, '22', '22', '22', 0, '2022-05-24 10:24:30', '2022-05-24 10:24:30', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1528925032664727554', NULL, '0', '1528925000846737409', '22', NULL, 0.00, 1, 1, '22', '2', '22', 0, '2022-05-24 10:25:08', '2022-05-24 10:25:08', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1528925032832499714', '1528925032664727554', '1437679155114393601', '1528925000959983618', '22', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-24 10:25:08', '2022-05-24 10:25:08', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1528925066504372226', NULL, '0', '1528925032664727554', '33', NULL, 0.00, 1, 1, NULL, '33', '33', 0, '2022-05-24 10:25:16', '2022-05-24 10:25:16', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1528925066642784257', '1528925066504372226', '1437679155114393601', '1528925032832499714', '33', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-24 10:25:16', '2022-05-24 10:25:16', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530069802036428802', NULL, '0', '1482951933643972610', '企业水电费管理', NULL, 4.00, 1, 1, '/collect-fee/water-ectricity-mange', 'WaterAndEectricityMange', '/park-contract', 0, '2022-05-27 14:14:02', '2022-05-27 15:42:08', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530069802262921218', '1530069802036428802', '1437679155114393601', '1482951934168260610', '企业水电费管理', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:14:02', '2022-05-27 15:42:08', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530069979677786114', NULL, '0', '1530069802036428802', '离场结算记录', NULL, 6.00, 1, 1, '/collect-fee/water-ectricity-mange/departure-settlement-record', 'WaterAndEectricitySettlementRecord', '/park-contract', 0, '2022-05-27 14:14:45', '2022-05-27 15:44:03', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530069979879112705', '1530069979677786114', '1437679155114393601', '1530069802262921218', '离场结算记录', NULL, 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:14:45', '2022-05-27 15:44:03', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530070085529436162', NULL, '0', '1530069802036428802', '未出水电费用', NULL, 0.00, 1, 1, '/collect-fee/water-ectricity-mange/unknown-fee', 'waterAndEectricityUnknownFee', '/park-contract', 0, '2022-05-27 14:15:10', '2022-05-27 14:15:10', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530070085730762754', '1530070085529436162', '1437679155114393601', '1530069802262921218', '未出水电费用', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:15:10', '2022-05-27 14:15:10', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530070195197902850', NULL, '0', '1530069802036428802', '已出水电费用', NULL, 1.00, 1, 1, '/collect-fee/water-ectricity-mange/final-fee', 'waterAndEectricityFinalFee', '/park-contract', 0, '2022-05-27 14:15:36', '2022-05-27 15:42:53', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530070195428589569', '1530070195197902850', '1437679155114393601', '1530069802262921218', '已出水电费用', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:15:36', '2022-05-27 15:42:53', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530070296385486850', NULL, '0', '1530069802036428802', '账单', NULL, 4.00, 1, 1, '/collect-fee/water-ectricity-mange/bill', 'waterAndEectricityBill', '/park-contract', 0, '2022-05-27 14:16:00', '2022-05-27 15:43:10', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530070296603590657', '1530070296385486850', '1437679155114393601', '1530069802262921218', '账单', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:16:00', '2022-05-27 15:43:11', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530070399758303234', NULL, '0', '1530069802036428802', '交易明细', NULL, 5.00, 1, 1, '/collect-fee/water-ectricity-mange/transaction-detail', 'waterAndEectricityTransactionDetail', '/park-contract', 0, '2022-05-27 14:16:25', '2022-05-27 15:43:15', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530070399972212737', '1530070399758303234', '1437679155114393601', '1530069802262921218', '交易明细', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:16:25', '2022-05-27 15:43:15', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530070523213447169', NULL, '0', '1530069802036428802', '滞纳金管理', NULL, 3.00, 1, 1, '/collect-fee/water-ectricity-mange/late-fee', 'waterAndEectricityLateFee', '/park-contract', 0, '2022-05-27 14:16:54', '2022-05-27 15:43:04', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530070523414773761', '1530070523213447169', '1437679155114393601', '1530069802262921218', '滞纳金管理', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:16:54', '2022-05-27 15:43:04', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530071009182285826', NULL, '0', '1530069979677786114', '离场结算', NULL, 0.00, 1, 1, '/collect-fee/water-ectricity-mange/departure-settlement-record/departure-settlement', 'WaterAndEectricitySettlement', '/park-contract', 2, '2022-05-27 14:18:50', '2022-05-27 14:18:50', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530071009429749762', '1530071009182285826', '1437679155114393601', '1530069979879112705', '离场结算', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:18:50', '2022-05-27 14:18:50', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530071295271567361', NULL, '0', '1530070399758303234', '交易明细', NULL, 0.00, 1, 1, '/rent-feeMange/water-ectricity-mange/transaction-detail/details', 'waterAndEectricityTransactionDetailDetails', '/park-contract', 2, '2022-05-27 14:19:58', '2022-05-27 14:19:58', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530071295544197122', '1530071295271567361', '1437679155114393601', '1530070399972212737', '交易明细', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:19:58', '2022-05-27 14:19:58', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530071477405024258', NULL, '0', '1482951933643972610', '公寓水电费管理', NULL, 5.00, 1, 1, '/collect-fee/water-ectricity-mange', 'WaterAndEectricityMange', '/park-contract', 0, '2022-05-27 14:20:42', '2022-05-27 15:42:16', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530071477631516674', '1530071477405024258', '1437679155114393601', '1482951934168260610', '公寓水电费管理', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:20:42', '2022-05-27 15:42:17', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530071669160214530', NULL, '0', '1530071477405024258', '离场结算记录', NULL, 5.00, 1, 1, '/collect-fee/apartment-ectricity-mange/departure-settlement-record', 'WaterAndEectricityApartmentSettlementRecord', '/park-contract', 0, '2022-05-27 14:21:27', '2022-05-27 15:43:46', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530071669445427202', '1530071669160214530', '1437679155114393601', '1530071477631516674', '离场结算记录', NULL, 5.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:21:28', '2022-05-27 15:43:46', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530071835011383298', NULL, '0', '1530071477405024258', '未出水电费用', NULL, 0.00, 1, 1, '/collect-fee/apartment-ectricity-mange/unknown-fee', 'waterAndEectricityApartmentUnknownFee', '/park-contract', 0, '2022-05-27 14:22:07', '2022-05-27 14:22:07', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530071835216904194', '1530071835011383298', '1437679155114393601', '1530071477631516674', '未出水电费用', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:22:07', '2022-05-27 14:22:07', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530071943396392961', NULL, '0', '1530071477405024258', '已出水电费用', NULL, 1.00, 1, 1, '/collect-fee/apartment-ectricity-mange/final-fee', 'waterAndEectricityApartmentFinalFee', '/park-contract', 0, '2022-05-27 14:22:33', '2022-05-27 15:43:24', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530071943606108161', '1530071943396392961', '1437679155114393601', '1530071477631516674', '已出水电费用', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:22:33', '2022-05-27 15:43:24', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530072033624260610', NULL, '0', '1530071477405024258', '账单', NULL, 3.00, 1, 1, '/collect-fee/apartment-ectricity-mange/bill', 'waterAndEectricityApartmentBill', '/park-contract', 0, '2022-05-27 14:22:54', '2022-05-27 15:43:36', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530072033854947330', '1530072033624260610', '1437679155114393601', '1530071477631516674', '账单', NULL, 3.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:22:54', '2022-05-27 15:43:36', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530072137986932738', NULL, '0', '1530071477405024258', '交易明细', NULL, 4.00, 1, 1, '/collect-fee/apartment-ectricity-mange/transaction-detail', 'waterAndEectricityApartmentTransactionDetail', '/park-contract', 0, '2022-05-27 14:23:19', '2022-05-27 15:43:43', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530072138230202370', '1530072137986932738', '1437679155114393601', '1530071477631516674', '交易明细', NULL, 4.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:23:19', '2022-05-27 15:43:43', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530072228764254209', NULL, '0', '1530071477405024258', '滞纳金管理', NULL, 2.00, 1, 1, '/collect-fee/apartment-ectricity-mange/late-fee', 'waterAndEectricityApartmentLateFee', '/park-contract', 0, '2022-05-27 14:23:41', '2022-05-27 15:43:31', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530072228978163714', '1530072228764254209', '1437679155114393601', '1530071477631516674', '滞纳金管理', NULL, 2.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:23:41', '2022-05-27 15:43:31', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530072357831376897', NULL, '0', '1530071669160214530', '离场结算', NULL, 0.00, 1, 1, '/collect-fee/apartment-ectricity-mange/departure-settlement-record/departure-settlement', 'WaterAndEectricityApartmentSettlement', '/park-contract', 2, '2022-05-27 14:24:12', '2022-05-27 14:24:12', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530072358087229442', '1530072357831376897', '1437679155114393601', '1530071669445427202', '离场结算', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:24:12', '2022-05-27 14:24:12', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1530072505110167554', NULL, '0', '1530072137986932738', '交易明细', NULL, 0.00, 1, 1, '/rent-feeMange/apartment-ectricity-mange/transaction-detail/details', 'waterAndEectricityApartmentTransactionDetailDetails', '/park-contract', 2, '2022-05-27 14:24:47', '2022-05-27 14:24:47', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1530072505332465666', '1530072505110167554', '1437679155114393601', '1530072138230202370', '交易明细', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-05-27 14:24:47', '2022-05-27 14:24:47', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1539862964875493378', NULL, '-1', '1539862940280094722', '2', NULL, 0.00, 1, 1, '2', '2', '2', 0, '2022-06-23 14:48:34', '2022-06-23 14:48:34', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1541371001029197826', NULL, '0', '0', '战情指挥大屏', 'scenes-fill', 0.00, 1, 1, '/command-big-sscreen', 'commandBigScreen', '/command-big-sscreen', 0, '2022-06-27 18:40:58', '2022-06-27 18:40:58', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1541371001129861122', '1541371001029197826', '1437679155114393601', '0', '战情指挥大屏', 'scenes-fill', 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-06-27 18:40:58', '2022-07-18 16:40:13', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1541371204700405761', NULL, '0', '1541371001029197826', '运营大屏', NULL, 0.00, 1, 1, '/command-big-sscreen/operation-screen?new_blank=1', 'commandBigScreenOperationScreen', '/command-big-sscreen', 0, '2022-06-27 18:41:47', '2022-06-27 18:41:47', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1541371204780097537', '1541371204700405761', '1437679155114393601', '1541371001129861122', '运营大屏', NULL, 0.00, 1, 1, NULL, NULL, NULL, NULL, '2022-06-27 18:41:47', '2022-06-27 18:41:47', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1541371328063275009', NULL, '0', '1541371001029197826', 'BIM大屏', NULL, 1.00, 1, 1, '/park-device/3d/device/3dMenuUrl/redirect?external_link=1', 'BIM', 'BIM', 0, '2022-06-27 18:42:16', '2022-06-27 18:42:16', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1541371328126189570', '1541371328063275009', '1437679155114393601', '1541371001129861122', 'BIM大屏', NULL, 1.00, 1, 1, NULL, NULL, NULL, NULL, '2022-06-27 18:42:16', '2022-06-27 18:42:16', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1541689923288891393', NULL, '0', '1450758892792205314', '招商分析', NULL, 6.00, 1, 1, '/statement-analysis/attract-investment-analysis', 'statementAnalysisAttractInvestmentAnalysis', '/statement-analysis', 0, '2022-06-28 15:48:15', '2022-06-28 20:29:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1541689923360194561', '1541689923288891393', '1437679155114393601', '1450758893001920513', '招商分析', NULL, 6.00, 1, 1, NULL, NULL, NULL, NULL, '2022-06-28 15:48:15', '2022-06-28 20:29:59', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1541690084824121345', NULL, '0', '1482951933643972610', '收费统计', NULL, 8.00, 1, 1, '/statement-analysis/fee-statistical', 'statementAnalysisfeeStatistical', '/statement-analysis', 0, '2022-06-28 15:48:54', '2022-06-28 20:30:15', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1541690084941561857', '1541690084824121345', '1437679155114393601', '1482951934168260610', '收费统计', NULL, 8.00, 1, 1, NULL, NULL, NULL, NULL, '2022-06-28 15:48:54', '2022-06-28 20:30:15', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1544864968110702593', NULL, '0', '0', '园区物联', 'conditions', 8.00, 1, 1, 'equipmentManagement', 'equipmentManagement', 'equipmentManagement', 0, '2022-07-07 10:04:45', '2022-07-07 13:46:59', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1544864968194588674', '1544864968110702593', '1437679155114393601', '0', '园区物联', 'conditions', 8.00, 1, 1, NULL, NULL, NULL, NULL, '2022-07-07 10:04:45', '2022-07-07 10:53:51', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1544865898860314626', NULL, '0', '0', '内部办公', 'docjpge-fill', 7.00, 1, 1, 'meetingManage', 'meetingManage', 'meetingManage', 0, '2022-07-07 10:08:27', '2022-07-07 13:45:57', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1544865898923229186', '1544865898860314626', '1437679155114393601', '0', '内部办公', 'docjpge-fill', 7.00, 1, 1, NULL, NULL, NULL, NULL, '2022-07-07 10:08:27', '2022-07-07 10:53:09', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1544870299570208769', NULL, '0', '0', '通行管理', 'image-text', 9.00, 1, 1, 'personPass', 'personPass', 'personPass', 0, '2022-07-07 10:25:56', '2022-07-07 13:47:25', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1544870299645706241', '1544870299570208769', '1437679155114393601', '0', '通行管理', 'image-text', 9.00, 1, 1, NULL, NULL, NULL, NULL, '2022-07-07 10:25:56', '2022-07-07 10:57:46', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('1544870728823668737', NULL, '0', '0', '物业管理', 'scenes', 11.00, 1, 1, 'patrolInspectionPatrolPlace', 'patrolInspectionPatrolPlace', 'patrolInspectionPatrolPlace', 0, '2022-07-07 10:27:38', '2022-07-07 13:47:47', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('1544870728857223170', '1544870728823668737', '1437679155114393601', '0', '物业管理', 'scenes', 11.00, 1, 1, NULL, NULL, NULL, NULL, '2022-07-07 10:27:38', '2022-07-07 11:08:07', '1', '1541336399027830786', 0);
INSERT INTO `sys_menu` VALUES ('2', NULL, '-1', '5', '菜单管理', '', 3.00, 1, NULL, 'menu-list', 'menuManagementMenuList', 'sys_dict_listPage', 0, NULL, '2022-01-04 14:06:54', '1', '1', 0);
INSERT INTO `sys_menu` VALUES ('5', NULL, '-1', '0', '系统管理', 'set1', 1.00, 1, NULL, 'system-management', 'systemManagement', 'system-management', 0, NULL, '2022-01-10 12:07:38', '1', '1', 0);

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '当前组织的顶级组织的id,即租户',
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级公司ID ，0表示是租户，是组织的顶级机构',
  `org_type` tinyint NULL DEFAULT NULL COMMENT '0，机构，1园区',
  `org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司名称',
  `level` tinyint NULL DEFAULT NULL COMMENT '当前组织在树中的第几层',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司地址',
  `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `credit_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `license_file_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '营业执照电子版',
  `extra_info` json NULL COMMENT '额外信息',
  `c_app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户对应的C端小程序appId',
  `c_app_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户对应的C端小程序sercret',
  `b_app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户对应的运营端小程序appId',
  `b_app_secret` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户对应的运营端小程序sercret',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:0-已禁用,1-正常',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0否1是',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `deleted_parentId`(`deleted`, `parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '组织机构' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_organization
-- ----------------------------
INSERT INTO `sys_organization` VALUES ('0', '0', '-1', 0, '全部', 0, NULL, NULL, NULL, NULL, '{}', NULL, NULL, NULL, NULL, 0, 0, NULL, '2021-06-18 17:15:28', '2021-07-15 17:37:25', '2021-09-16 10:49:27');
INSERT INTO `sys_organization` VALUES ('1437679155114393601', '1437679155114393601', '0', 0, '淮安科教产业投资控股有限公司', 1, '淮安经济技术开发区枚皋路19号淮安智慧谷B1号楼', NULL, '91320891078215116D', '暂无接口', '{\"contactUserName\": \"刘宇恒\", \"contactUserMobile\": \"17665354037\"}', 'wx5766e6a4e09a4958', 'f2b22be36b7e240ea27101c11486937f', 'wx9ac3e3b15a24d658', '12517eac9c8ec5f2f57d39daca6dafc5', 1, 0, '1', '1', '2021-09-14 15:26:36', '2022-01-11 13:54:19');
INSERT INTO `sys_organization` VALUES ('1541335941202771970', '1437679155114393601', '1437679155114393601', 1, '淮安智慧谷', 2, NULL, '智慧谷位于淮安科教产业园南部，宁连路东侧，枚皋路南侧，紧邻市生态新城、文体中心，规划用地面积2600亩，预计总投资80亿元，其中1500亩科研平台区域规划总建筑面积118万平方米。', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, 0, '1437679155261194242', '1437679155261194242', '2022-06-27 16:21:39', '2022-06-27 16:38:52');

-- ----------------------------
-- Table structure for sys_park
-- ----------------------------
DROP TABLE IF EXISTS `sys_park`;
CREATE TABLE `sys_park`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `province` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `longitude` double NULL DEFAULT NULL COMMENT '经度',
  `latitude` double NULL DEFAULT NULL COMMENT '纬度',
  `build_time` date NULL DEFAULT NULL COMMENT '建成时间',
  `park_type` int NULL DEFAULT NULL COMMENT '园区属性 1：社会大型园区 2：政府园区 3：社区集体园区 4：区属国企物业 5：社会零散物业',
  `park_function` int NULL DEFAULT NULL COMMENT '功能用途 1：研发办公 2：工业生产 3：物流仓储 4：商业配套 5：其他',
  `property` int NULL DEFAULT NULL COMMENT '产权属性 1：无 2：红本 3：绿本 4：其他 ',
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权属单位',
  `company_contacts` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权属单位联系人',
  `company_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权属单位联系电话',
  `management_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理机构',
  `management_contacts` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理机构联系人',
  `management_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理机构联系电话',
  `cover_area` double NULL DEFAULT NULL COMMENT '占地面积',
  `building_area` double NULL DEFAULT NULL COMMENT '建筑面积',
  `fire_examine_status` int NULL DEFAULT NULL COMMENT '消防审批状态 0：已通过 1：未通过 2：暂未审批',
  `fire_person` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消防责任管理人',
  `fire_person_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消防责任管理人联系电话',
  `pics` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图片',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '园区信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_park
-- ----------------------------
INSERT INTO `sys_park` VALUES ('1541335942159073281', '1541335941202771970', '江苏省淮安市清江浦区皋路与宁连路交叉处', '江苏省', '淮安市', 119.059844, 33.536304, NULL, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, 0, 1180000, 0, NULL, NULL, 'dmMtdXBsb2FkLTE2NTYzMTg3OTk3NzktOF8xNjU2MzE5MDk5MTQw', '2022-06-27 16:21:39', '2022-06-27 16:39:40', NULL, '1437679155261194242', 0);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色ID',
  `permission_type` tinyint NOT NULL COMMENT '权限类型：0，菜单（包括菜单、按钮、导航），2数据',
  `dest_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标，菜单（菜单、按钮、导航）',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT ' \'状态:0-已禁用,1-正常\'',
  `mark` tinyint NULL DEFAULT 1 COMMENT '用于区分组织树的父节点勾选情况，1正常数据,2，数据权限，用于编辑时显示',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除0，否，1是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT ' 创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ' 更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT ' 更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1438708580123660290', '1437245748215533569', 0, '0', 1, 1, 0, '2021-09-17 11:37:10', '1', '2021-09-17 11:37:10', '1');
INSERT INTO `sys_permission` VALUES ('1438708580123660291', '1437245748215533569', 0, '2', 1, 1, 0, '2021-09-17 11:37:10', '1', '2021-09-17 11:37:10', '1');
INSERT INTO `sys_permission` VALUES ('1438708580123660292', '1437245748215533569', 0, '1436205736883646465', 1, 1, 0, '2021-09-17 11:37:10', '1', '2021-09-17 11:37:10', '1');
INSERT INTO `sys_permission` VALUES ('1438708580123660293', '1437245748215533569', 0, '1436206217857069058', 1, 1, 0, '2021-09-17 11:37:10', '1', '2021-09-17 11:37:10', '1');
INSERT INTO `sys_permission` VALUES ('1438708580123660294', '1437245748215533569', 0, '5', 1, 1, 0, '2021-09-17 11:37:10', '1', '2021-09-17 11:37:10', '1');
INSERT INTO `sys_permission` VALUES ('1438709005350588417', '1438330676093067265', 0, '1437679155751927809', 1, 1, 0, '2021-09-17 11:38:51', '1437679155261194242', '2021-09-17 11:38:51', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1438709005350588418', '1438330676093067265', 0, '1437679155751927810', 1, 1, 0, '2021-09-17 11:38:51', '1437679155261194242', '2021-09-17 11:38:51', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1438709005350588419', '1438330676093067265', 0, '1438382628843786242', 1, 1, 0, '2021-09-17 11:38:51', '1437679155261194242', '2021-09-17 11:38:51', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1438709005350588420', '1438330676093067265', 0, '1437679155743539204', 1, 1, 0, '2021-09-17 11:38:51', '1437679155261194242', '2021-09-17 11:38:51', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1528932819755008002', '1', 0, '1476074921603780609', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819755008003', '1', 0, '1509773920850345985', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819792756738', '1', 0, '1514889028073095170', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819796951042', '1', 0, '1479367970520379393', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819805339649', '1', 0, '1525033603287748609', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819809533954', '1', 0, '1448252937575776258', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819817922561', '1', 0, '1436215157328056322', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819822116866', '1', 0, '1499724493859258370', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819826311170', '1', 0, '1509773196666011650', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819834699777', '1', 0, '1415222344495075329', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819838894081', '1', 0, '1465163166870798338', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819847282690', '1', 0, '1446678390379847682', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819851476993', '1', 0, '1450759145389969410', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819855671298', '1', 0, '1436205736883646465', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819864059905', '1', 0, '1441967553601794050', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819868254209', '1', 0, '1509773377142718466', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819876642817', '1', 0, '1509782961819418625', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819880837121', '1', 0, '1448182963817590786', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819885031425', '1', 0, '1522106772052967426', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819893420034', '1', 0, '1509769246269177857', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819897614337', '1', 0, '1413411999791157250', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819901808642', '1', 0, '1522106893213827074', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819910197250', '1', 0, '1504812818605473793', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819914391554', '1', 0, '1509773597318512642', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819918585858', '1', 0, '1514887175482572801', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819926974465', '1', 0, '1438382628499853314', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819931168770', '1', 0, '1514888689240440833', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819935363073', '1', 0, '1441967752080453634', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819943751682', '1', 0, '1501459021225197569', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819947945986', '1', 0, '1509773290744250369', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819956334594', '1', 0, '1525033728760352769', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819960528897', '1', 0, '1484084169221869570', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819968917505', '1', 0, '1499728546819407874', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932819977306114', '1', 0, '1509772997566595073', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820019249154', '1', 0, '0', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820023443458', '1', 0, '1514888363137499138', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820040220674', '1', 0, '2', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820048609282', '1', 0, '1476453287326515201', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820052803586', '1', 0, '1476031286778679297', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820061192194', '1', 0, '1525035994124914689', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820065386498', '1', 0, '5', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820073775106', '1', 0, '1525033179361054722', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820077969409', '1', 0, '1514887089327374337', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820082163713', '1', 0, '1522104951515316226', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820090552321', '1', 0, '1492086255975858177', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820094746626', '1', 0, '1514886655544066049', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820103135233', '1', 0, '1525035554951925762', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820107329538', '1', 0, '1476453430260006914', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820111523841', '1', 0, '1528927634244698114', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820119912449', '1', 0, '1446679255807369218', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820124106754', '1', 0, '1479367665950994434', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820128301058', '1', 0, '1450758892792205314', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820136689665', '1', 0, '1499728309052702722', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820140883970', '1', 0, '1514938787550920706', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820145078273', '1', 0, '1528925066504372226', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820153466882', '1', 0, '1499728635759624194', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820157661185', '1', 0, '1419940479215292418', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820166049793', '1', 0, '1446679443238232066', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820170244097', '1', 0, '1509783044002611201', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820174438402', '1', 0, '1525035201929940994', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820182827009', '1', 0, '1514888930840739841', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820187021314', '1', 0, '1499727592023851009', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820195409921', '1', 0, '1509773825455095810', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820199604226', '1', 0, '1514888540195848194', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820207992833', '1', 0, '1514888615777206273', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820212187137', '1', 0, '1446678929398243329', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820216381441', '1', 0, '1525033081252089857', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820224770050', '1', 0, '1499724322031206402', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820228964354', '1', 0, '1525035302052171778', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820233158657', '1', 0, '1441967931118514178', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820241547266', '1', 0, '1509783698003656705', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820245741569', '1', 0, '1441967821190000642', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820254130178', '1', 0, '1446679117877682177', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820258324482', '1', 0, '1479422945827663873', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820266713089', '1', 0, '1514886829880311809', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820270907393', '1', 0, '1446678253830086657', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820275101698', '1', 0, '1446677983683354626', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820283490305', '1', 0, '1451018943524200450', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820287684610', '1', 0, '1462701076894400513', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820325433345', '1', 0, '1514888454233587714', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820329627650', '1', 0, '1525034654489382914', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820338016257', '1', 0, '1499728097391345666', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820342210562', '1', 0, '1499727122823839746', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820354793473', '1', 0, '1499724986945830914', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820354793474', '1', 0, '1525032832819269634', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820367376386', '1', 0, '1514888801861697537', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820409319426', '1', 0, '1484083621441572865', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820413513730', '1', 0, '1514887257904840706', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820421902338', '1', 0, '1522106484806057986', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820430290946', '1', 0, '1522105938074337282', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820434485250', '1', 0, '1525034375320702978', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820438679554', '1', 0, '1509782850477424641', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820447068161', '1', 0, '1442047027764715521', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820451262466', '1', 0, '1437296076726185986', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820455456770', '1', 0, '1480822523934281730', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820463845377', '1', 0, '1499724399487418369', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820468039681', '1', 0, '1528925032664727554', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820476428289', '1', 0, '1442752894109691905', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820480622594', '1', 0, '1499724221573431298', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820484816897', '1', 0, '1499728414300372994', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820493205506', '1', 0, '1453271340635357185', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820497399809', '1', 0, '1465569649144057858', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820501594113', '1', 0, '1436206217857069058', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820509982722', '1', 0, '1528924875286052865', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820514177025', '1', 0, '1501458722326511617', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820514177026', '1', 0, '1413107829863309314', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820526759938', '1', 0, '1504804677293637634', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820530954241', '1', 0, '1480813318691098626', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820535148546', '1', 0, '1453271697046339585', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820543537153', '1', 0, '1525034769111322626', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820547731457', '1', 0, '1522106341574770690', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820551925761', '1', 0, '1514886973157736449', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820560314370', '1', 0, '1509768926839373826', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820564508674', '1', 0, '1499727340424331265', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820568702977', '1', 0, '1509773700049600514', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820577091585', '1', 0, '1504819936930758657', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820581285890', '1', 0, '1514947147763482625', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820585480194', '1', 0, '1522106647758962689', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820593868801', '1', 0, '1525035067271811074', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820598063105', '1', 0, '1453278874494533633', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820602257410', '1', 0, '1509769055684198402', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820610646018', '1', 0, '1525033344083955713', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820614840321', '1', 0, '1478633443804131329', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820623228929', '1', 0, '1499728208284549121', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820627423234', '1', 0, '1499726316649250818', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820631617538', '1', 0, '1509773505232568322', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820640006145', '1', 0, '1452893057758478338', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820644200449', '1', 0, '1509768601483018242', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820648394753', '1', 0, '1522106218329341954', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820656783361', '1', 0, '1480821373977759746', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820660977666', '1', 0, '1528924835247226882', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820665171969', '1', 0, '1499725086715740161', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820669366273', '1', 0, '1514947248049291265', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820677754882', '1', 0, '1442041009123024897', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820681949185', '1', 0, '1476829541590581250', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820690337793', '1', 0, '1414490267730554882', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820694532098', '1', 0, '1504820046381121538', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820728086529', '1', 0, '1499724699644395521', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820744863745', '1', 0, '1413108475773542402', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820749058049', '1', 0, '1522105340406988802', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820753252354', '1', 0, '1525033484123377666', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820761640962', '1', 0, '1504812727106732033', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820765835266', '1', 0, '1443470588224192514', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820770029569', '1', 0, '1522105141018165250', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820778418178', '1', 0, '1499724838312280066', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820782612482', '1', 0, '1525032674891141121', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820786806786', '1', 0, '1525034877622161410', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820795195394', '1', 0, '1505783546658095106', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820799389697', '1', 0, '1465163346575753218', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820807778305', '1', 0, '1525032969490665473', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820811972609', '1', 0, '1453278535431192578', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820816166914', '1', 0, '1525036114262364161', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820824555521', '1', 0, '1509783210076078082', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820828749825', '1', 0, '1525034106360958977', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820832944129', '1', 0, '1509773098590601218', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820841332738', '1', 0, '1499727442475941889', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820845527041', '1', 0, '1419944004242591745', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820853915649', '1', 0, '1476393149202894849', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820858109954', '1', 0, '1525037609539010561', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820862304258', '1', 0, '1501458280628551681', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820870692865', '1', 0, '1522105803617533954', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820874887170', '1', 0, '1452873337793626113', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820883275778', '1', 0, '1480829667303755778', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820887470082', '1', 0, '1468054231990898689', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820891664385', '1', 0, '1499726996621426689', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820900052993', '1', 0, '1504812898389524481', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820904247298', '1', 0, '1499726451097665538', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820912635905', '1', 0, '1476453144984420353', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820916830210', '1', 0, '1476452792528777218', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820921024514', '1', 0, '1501458588989587457', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820929413121', '1', 0, '1436215805771649026', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820933607425', '1', 0, '1478695869169872897', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820941996033', '1', 0, '1499725740859392001', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820946190337', '1', 0, '1499727220295270401', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820950384642', '1', 0, '1525034459504578561', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820958773250', '1', 0, '1437757799699050497', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820962967554', '1', 0, '1522105448389345282', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820971356161', '1', 0, '1455110558244380674', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820975550465', '1', 0, '1475742604231491585', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820979744770', '1', 0, '1509783391601360897', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820988133377', '1', 0, '1441967669746266113', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820992327682', '1', 0, '1492086157699121153', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932820996521985', '1', 0, '1522106116902682625', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821004910593', '1', 0, '1525034252796694529', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821009104898', '1', 0, '1475742781197565954', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821013299201', '1', 0, '1413107113673318401', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821021687809', '1', 0, '1504834246595772417', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821025882114', '1', 0, '1446679595206254593', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821059436546', '1', 0, '1480822675952635905', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821067825154', '1', 0, '1466365916094865410', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821072019457', '1', 0, '1525032539675168769', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1528932821076213761', '1', 0, '1525034546788044802', 1, 1, 0, '2022-05-24 10:56:05', '1', '2022-05-24 10:56:05', '1');
INSERT INTO `sys_permission` VALUES ('1530069802296475649', '1', 0, '1530069802036428802', 1, 1, 0, '2022-05-27 14:14:02', NULL, '2022-05-27 14:14:02', NULL);
INSERT INTO `sys_permission` VALUES ('1530069979879112710', '1', 0, '1530069979677786114', 1, 1, 0, '2022-05-27 14:14:45', NULL, '2022-05-27 14:14:45', NULL);
INSERT INTO `sys_permission` VALUES ('1530070085768511490', '1', 0, '1530070085529436162', 1, 1, 0, '2022-05-27 14:15:10', NULL, '2022-05-27 14:15:10', NULL);
INSERT INTO `sys_permission` VALUES ('1530070195478921217', '1', 0, '1530070195197902850', 1, 1, 0, '2022-05-27 14:15:36', NULL, '2022-05-27 14:15:36', NULL);
INSERT INTO `sys_permission` VALUES ('1530070296658116610', '1', 0, '1530070296385486850', 1, 1, 0, '2022-05-27 14:16:00', NULL, '2022-05-27 14:16:00', NULL);
INSERT INTO `sys_permission` VALUES ('1530070400014155778', '1', 0, '1530070399758303234', 1, 1, 0, '2022-05-27 14:16:25', NULL, '2022-05-27 14:16:25', NULL);
INSERT INTO `sys_permission` VALUES ('1530070523448328193', '1', 0, '1530070523213447169', 1, 1, 0, '2022-05-27 14:16:54', NULL, '2022-05-27 14:16:54', NULL);
INSERT INTO `sys_permission` VALUES ('1530071009501052930', '1', 0, '1530071009182285826', 1, 1, 0, '2022-05-27 14:18:50', NULL, '2022-05-27 14:18:50', NULL);
INSERT INTO `sys_permission` VALUES ('1530071295581945857', '1', 0, '1530071295271567361', 1, 1, 0, '2022-05-27 14:19:58', NULL, '2022-05-27 14:19:58', NULL);
INSERT INTO `sys_permission` VALUES ('1530071477660876803', '1', 0, '1530071477405024258', 1, 1, 0, '2022-05-27 14:20:42', NULL, '2022-05-27 14:20:42', NULL);
INSERT INTO `sys_permission` VALUES ('1530071669483175938', '1', 0, '1530071669160214530', 1, 1, 0, '2022-05-27 14:21:28', NULL, '2022-05-27 14:21:28', NULL);
INSERT INTO `sys_permission` VALUES ('1530071835250458626', '1', 0, '1530071835011383298', 1, 1, 0, '2022-05-27 14:22:07', NULL, '2022-05-27 14:22:07', NULL);
INSERT INTO `sys_permission` VALUES ('1530071943639662593', '1', 0, '1530071943396392961', 1, 1, 0, '2022-05-27 14:22:33', NULL, '2022-05-27 14:22:33', NULL);
INSERT INTO `sys_permission` VALUES ('1530072033888501761', '1', 0, '1530072033624260610', 1, 1, 0, '2022-05-27 14:22:54', NULL, '2022-05-27 14:22:54', NULL);
INSERT INTO `sys_permission` VALUES ('1530072138263756801', '1', 0, '1530072137986932738', 1, 1, 0, '2022-05-27 14:23:19', NULL, '2022-05-27 14:23:19', NULL);
INSERT INTO `sys_permission` VALUES ('1530072229011718146', '1', 0, '1530072228764254209', 1, 1, 0, '2022-05-27 14:23:41', NULL, '2022-05-27 14:23:41', NULL);
INSERT INTO `sys_permission` VALUES ('1530072358124978177', '1', 0, '1530072357831376897', 1, 1, 0, '2022-05-27 14:24:12', NULL, '2022-05-27 14:24:12', NULL);
INSERT INTO `sys_permission` VALUES ('1530072505366020097', '1', 0, '1530072505110167554', 1, 1, 0, '2022-05-27 14:24:47', NULL, '2022-05-27 14:24:47', NULL);
INSERT INTO `sys_permission` VALUES ('1539862940351397889', '1', 0, '1539862940280094722', 1, 1, 0, '2022-06-23 14:48:28', NULL, '2022-06-23 14:48:28', NULL);
INSERT INTO `sys_permission` VALUES ('1539862964913242113', '1', 0, '1539862964875493378', 1, 1, 0, '2022-06-23 14:48:34', NULL, '2022-06-23 14:48:34', NULL);
INSERT INTO `sys_permission` VALUES ('1541323587503501314', '1', 0, '1541323587331534850', 1, 1, 0, '2022-06-27 15:32:34', NULL, '2022-06-27 15:32:34', NULL);
INSERT INTO `sys_permission` VALUES ('1541336541252485122', '1437679155466715137', 2, '1437679155114393601', 1, 2, 0, '2022-06-27 16:24:02', '1437679155261194242', '2022-06-27 16:24:02', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1541336541252485123', '1437679155466715137', 2, '1541335941202771970', 1, 2, 0, '2022-06-27 16:24:02', '1437679155261194242', '2022-06-27 16:24:02', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1541336570373537794', '1437679155466715137', 2, '1437679155114393601', 1, 2, 0, '2022-06-27 16:24:09', '1437679155261194242', '2022-06-27 16:24:09', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1541336570377732097', '1437679155466715137', 2, '1541335941202771970', 1, 2, 0, '2022-06-27 16:24:09', '1437679155261194242', '2022-06-27 16:24:09', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1541371001142444033', '1', 0, '1541371001029197826', 1, 1, 0, '2022-06-27 18:40:58', NULL, '2022-06-27 18:40:58', NULL);
INSERT INTO `sys_permission` VALUES ('1541371204792680450', '1', 0, '1541371204700405761', 1, 1, 0, '2022-06-27 18:41:47', NULL, '2022-06-27 18:41:47', NULL);
INSERT INTO `sys_permission` VALUES ('1541371328134578177', '1', 0, '1541371328063275009', 1, 1, 0, '2022-06-27 18:42:16', NULL, '2022-06-27 18:42:16', NULL);
INSERT INTO `sys_permission` VALUES ('1541689923372777473', '1', 0, '1541689923288891393', 1, 1, 0, '2022-06-28 15:48:15', NULL, '2022-06-28 15:48:15', NULL);
INSERT INTO `sys_permission` VALUES ('1541690084945756162', '1', 0, '1541690084824121345', 1, 1, 0, '2022-06-28 15:48:54', NULL, '2022-06-28 15:48:54', NULL);
INSERT INTO `sys_permission` VALUES ('1541951673104007169', '1438330676093067265', 2, '1437679155114393601', 1, 2, 0, '2022-06-29 09:08:21', '1541368271011250177', '2022-06-29 09:08:21', '1541368271011250177');
INSERT INTO `sys_permission` VALUES ('1541951673112395777', '1438330676093067265', 2, '1541335941202771970', 1, 2, 0, '2022-06-29 09:08:21', '1541368271011250177', '2022-06-29 09:08:21', '1541368271011250177');
INSERT INTO `sys_permission` VALUES ('1544864968211365890', '1', 0, '1544864968110702593', 1, 1, 0, '2022-07-07 10:04:45', NULL, '2022-07-07 10:04:45', NULL);
INSERT INTO `sys_permission` VALUES ('1544865898931617793', '1', 0, '1544865898860314626', 1, 1, 0, '2022-07-07 10:08:27', NULL, '2022-07-07 10:08:27', NULL);
INSERT INTO `sys_permission` VALUES ('1544870299658289153', '1', 0, '1544870299570208769', 1, 1, 0, '2022-07-07 10:25:56', NULL, '2022-07-07 10:25:56', NULL);
INSERT INTO `sys_permission` VALUES ('1544870728857223171', '1', 0, '1544870728823668737', 1, 1, 0, '2022-07-07 10:27:38', NULL, '2022-07-07 10:27:38', NULL);
INSERT INTO `sys_permission` VALUES ('1548949158582222850', '1541336676195827714', 2, '1437679155114393601', 1, 2, 0, '2022-07-18 16:33:52', '1541336399027830786', '2022-07-18 16:33:52', '1541336399027830786');
INSERT INTO `sys_permission` VALUES ('1548949158586417153', '1541336676195827714', 2, '1541335941202771970', 1, 2, 0, '2022-07-18 16:33:52', '1541336399027830786', '2022-07-18 16:33:52', '1541336399027830786');
INSERT INTO `sys_permission` VALUES ('1548955580174893058', '1541336676195827714', 0, '1522106647901569026', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580174893059', '1541336676195827714', 0, '1480813318863065089', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580174893060', '1541336676195827714', 0, '1482951934168260610', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580174893061', '1541336676195827714', 0, '1541690084941561857', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580204253186', '1541336676195827714', 0, '1499724987138768898', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580204253187', '1541336676195827714', 0, '1522105340620898305', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580216836098', '1541336676195827714', 0, '1514888802000109569', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580221030401', '1541336676195827714', 0, '1522106218476142593', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580225224706', '1541336676195827714', 0, '1514888930987540482', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580237807617', '1541336676195827714', 0, '1482952154075619330', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580242001922', '1541336676195827714', 0, '1453271697503518721', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580250390529', '1541336676195827714', 0, '1514889028224090114', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580254584833', '1541336676195827714', 0, '1522106341721571330', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580258779137', '1541336676195827714', 0, '1522105803823054850', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580267167745', '1541336676195827714', 0, '1482998044794544129', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580271362050', '1541336676195827714', 0, '1522106117099814914', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580279750657', '1541336676195827714', 0, '1514888540338454529', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580283944961', '1541336676195827714', 0, '1499725086879318018', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580288139265', '1541336676195827714', 0, '1450758893001920513', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580296527873', '1541336676195827714', 0, '1522106772187185154', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580300722178', '1541336676195827714', 0, '1522105448540340226', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580304916482', '1541336676195827714', 0, '1509768601613041666', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580313305089', '1541336676195827714', 0, '1525037610885382146', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580317499394', '1541336676195827714', 0, '1522106484969635842', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580321693698', '1541336676195827714', 0, '1499724838559744001', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580321693699', '1541336676195827714', 0, '1522106893364822018', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580330082305', '1541336676195827714', 0, '1453278535909343234', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548955580338470913', '1541336676195827714', 0, '1522105938237915138', 1, 1, 0, '2022-07-18 16:59:23', '1437679155261194242', '2022-07-18 16:59:23', '1437679155261194242');
INSERT INTO `sys_permission` VALUES ('1548959909149605890', '1437679155466715137', 0, '1437679155751927809', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909166383106', '1437679155466715137', 0, '1509773597440147457', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909170577410', '1437679155466715137', 0, '1514888802000109569', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909178966017', '1437679155466715137', 0, '1530070399972212737', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909183160321', '1437679155466715137', 0, '1499727122983223298', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909191548929', '1437679155466715137', 0, '1499728635935784961', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909195743234', '1437679155466715137', 0, '1514887175616790529', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909199937537', '1437679155466715137', 0, '1509773290874273793', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909208326146', '1437679155466715137', 0, '1530071835216904194', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909212520450', '1437679155466715137', 0, '1530071009429749762', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909216714754', '1437679155466715137', 0, '1499726996793393153', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909225103362', '1437679155466715137', 0, '1492086158202437634', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909229297665', '1437679155466715137', 0, '1525034106524536834', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909237686274', '1437679155466715137', 0, '0', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909241880577', '1437679155466715137', 0, '1525036114417553410', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909292212226', '1437679155466715137', 0, '1509769246420172802', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909292212227', '1437679155466715137', 0, '1499724399755853826', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909292212228', '1437679155466715137', 0, '1499728208464904193', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909304795137', '1437679155466715137', 0, '1446679118401970178', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909313183746', '1437679155466715137', 0, '1437679155751927811', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909313183747', '1437679155466715137', 0, '1437679155751927810', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909325766657', '1437679155466715137', 0, '1541689923360194561', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909325766658', '1437679155466715137', 0, '1453278535909343234', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909338349570', '1437679155466715137', 0, '1476453430855598081', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909338349571', '1437679155466715137', 0, '1514888615974338561', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909338349572', '1437679155466715137', 0, '1482951934168260610', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909355126786', '1437679155466715137', 0, '1530069802262921218', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909355126787', '1437679155466715137', 0, '1522106218476142593', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909367709698', '1437679155466715137', 0, '1525033484274372609', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909380292609', '1437679155466715137', 0, '1514888454443302913', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909384486914', '1437679155466715137', 0, '1525034459701710849', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909388681217', '1437679155466715137', 0, '1451018943834578946', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909397069825', '1437679155466715137', 0, '1525035994389155842', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909401264130', '1437679155466715137', 0, '1525034546943234050', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909405458434', '1437679155466715137', 0, '1468054232573906945', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909413847042', '1437679155466715137', 0, '1525033728907153409', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909418041346', '1437679155466715137', 0, '1522105448540340226', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909422235649', '1437679155466715137', 0, '1448252937928097794', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909430624258', '1437679155466715137', 0, '1499724322236727298', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909434818561', '1437679155466715137', 0, '1541371001129861122', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909439012866', '1437679155466715137', 0, '1530071669445427202', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909447401473', '1437679155466715137', 0, '1522106484969635842', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909451595777', '1437679155466715137', 0, '1475742604818694146', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909455790082', '1437679155466715137', 0, '1446677984035676162', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909459984386', '1437679155466715137', 0, '1499728309270806530', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909468372994', '1437679155466715137', 0, '1514947148245827585', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909472567297', '1437679155466715137', 0, '1480813318863065089', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909476761601', '1437679155466715137', 0, '1522105340620898305', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909485150209', '1437679155466715137', 0, '1499728097559117826', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909489344514', '1437679155466715137', 0, '1499724987138768898', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909497733122', '1437679155466715137', 0, '1499727592158068737', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909501927426', '1437679155466715137', 0, '1466365916686262274', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909506121730', '1437679155466715137', 0, '1525032833045762049', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909514510338', '1437679155466715137', 0, '1525032969633271809', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909518704641', '1437679155466715137', 0, '1441967553996058626', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909522898946', '1437679155466715137', 0, '1482952154075619330', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909531287553', '1437679155466715137', 0, '1453271697503518721', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909535481857', '1437679155466715137', 0, '1514888363271716865', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909539676161', '1437679155466715137', 0, '1522104951746002945', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909548064769', '1437679155466715137', 0, '1504812818769051650', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909552259074', '1437679155466715137', 0, '1525034769279094785', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909556453378', '1437679155466715137', 0, '1446678929746370562', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909564841986', '1437679155466715137', 0, '1530070523414773761', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909569036290', '1437679155466715137', 0, '1522106772187185154', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909573230594', '1437679155466715137', 0, '1530072505332465666', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909581619202', '1437679155466715137', 0, '1514886655636340737', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909585813505', '1437679155466715137', 0, '1525034375492669441', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909623562242', '1437679155466715137', 0, '1525034877760573442', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909631950850', '1437679155466715137', 0, '1525037610885382146', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909636145153', '1437679155466715137', 0, '1525035302203166721', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909640339457', '1437679155466715137', 0, '1476393149790097409', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909648728065', '1437679155466715137', 0, '1509769055814221826', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909652922369', '1437679155466715137', 0, '1522106893364822018', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909661310977', '1437679155466715137', 0, '1541371328126189570', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909665505281', '1437679155466715137', 0, '1525034252939300866', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909669699586', '1437679155466715137', 0, '1530072033854947330', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909678088193', '1437679155466715137', 0, '1499727340579520514', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909682282497', '1437679155466715137', 0, '1530070085730762754', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909686476801', '1437679155466715137', 0, '1504804677796954114', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909694865409', '1437679155466715137', 0, '1522106647901569026', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909699059714', '1437679155466715137', 0, '1499726316758302721', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909703254017', '1437679155466715137', 0, '1514886973308731394', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909711642625', '1437679155466715137', 0, '1476031287353298946', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909715836930', '1437679155466715137', 0, '1479367666378813441', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909724225538', '1437679155466715137', 0, '1437679155743539203', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909728419841', '1437679155466715137', 0, '1437679155743539204', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909732614146', '1437679155466715137', 0, '1465569649701900290', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909741002753', '1437679155466715137', 0, '1492086256474980354', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909745197057', '1437679155466715137', 0, '1509773196821200898', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909753585666', '1437679155466715137', 0, '1437679155743539202', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909757779969', '1437679155466715137', 0, '1514889028224090114', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909761974274', '1437679155466715137', 0, '1462701077301248001', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909770362881', '1437679155466715137', 0, '1504819937023033345', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909774557185', '1437679155466715137', 0, '1478633444508774402', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909782945793', '1437679155466715137', 0, '1509783210269016066', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909787140097', '1437679155466715137', 0, '1525035067418611714', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909795528705', '1437679155466715137', 0, '1522105141181743105', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909803917314', '1437679155466715137', 0, '1499725086879318018', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909808111618', '1437679155466715137', 0, '1525035555153252353', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909816500225', '1437679155466715137', 0, '1514887089507729409', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909820694530', '1437679155466715137', 0, '1475742781654745089', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909824888834', '1437679155466715137', 0, '1499727442639519746', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909833277442', '1437679155466715137', 0, '1452873338322108417', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909837471745', '1437679155466715137', 0, '1509782961949442050', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909841666050', '1437679155466715137', 0, '1509768601613041666', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909850054657', '1437679155466715137', 0, '1441967931680550914', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909854248962', '1437679155466715137', 0, '1437679155760316418', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909858443266', '1437679155466715137', 0, '1544870299645706241', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909866831874', '1437679155466715137', 0, '1437679155760316417', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909871026178', '1437679155466715137', 0, '1525032539868106754', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909875220482', '1437679155466715137', 0, '1530072228978163714', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909883609090', '1437679155466715137', 0, '1442047028188340226', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909887803394', '1437679155466715137', 0, '1452893058261794817', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909891997698', '1437679155466715137', 0, '1479367971090804738', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909900386306', '1437679155466715137', 0, '1453271341121896449', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909904580610', '1437679155466715137', 0, '1446679256306491394', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909912969218', '1437679155466715137', 0, '1525033603463909377', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909942329345', '1437679155466715137', 0, '1530070296603590657', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909946523649', '1437679155466715137', 0, '1544865898923229186', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909954912258', '1437679155466715137', 0, '1438382628843786242', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909959106561', '1437679155466715137', 0, '1514886830094221313', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909963300866', '1437679155466715137', 0, '1450758893001920513', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909971689474', '1437679155466715137', 0, '1530070195428589569', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909975883777', '1437679155466715137', 0, '1530069979879112705', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909984272385', '1437679155466715137', 0, '1522105938237915138', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909988466689', '1437679155466715137', 0, '1483693246973038594', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959909992660994', '1437679155466715137', 0, '1509773825618673666', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910001049601', '1437679155466715137', 0, '1541690084941561857', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910005243905', '1437679155466715137', 0, '1442041009651507201', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910009438209', '1437679155466715137', 0, '1499728414463950849', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910013632514', '1437679155466715137', 0, '1530072138230202370', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910022021122', '1437679155466715137', 0, '1482998044794544129', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910026215425', '1437679155466715137', 0, '1522106117099814914', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910034604033', '1437679155466715137', 0, '1530071477631516674', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910038798337', '1437679155466715137', 0, '1525033344226562049', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910042992641', '1437679155466715137', 0, '1478695869564137474', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910047186945', '1437679155466715137', 0, '1441967670220222465', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910055575554', '1437679155466715137', 0, '1514888689370464257', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910059769858', '1437679155466715137', 0, '1504820046515339265', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910068158465', '1437679155466715137', 0, '1476453287972438018', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910072352769', '1437679155466715137', 0, '1499726451340935169', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910076547073', '1437679155466715137', 0, '1509773098724818945', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910084935681', '1437679155466715137', 0, '1509783391731384321', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910089129986', '1437679155466715137', 0, '1509768926994563073', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910093324290', '1437679155466715137', 0, '1446678390870581249', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910097518593', '1437679155466715137', 0, '1499728546999762945', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910105907202', '1437679155466715137', 0, '1525032675029553153', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910110101505', '1437679155466715137', 0, '1499724700080603138', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910156238850', '1437679155466715137', 0, '1530072358087229442', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910173016065', '1437679155466715137', 0, '1453278874955907074', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910189793282', '1437679155466715137', 0, '1501458589127999489', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910193987586', '1437679155466715137', 0, '1465163347175538689', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910202376194', '1437679155466715137', 0, '1509773505320648706', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910206570498', '1437679155466715137', 0, '1476452792855932929', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910210764802', '1437679155466715137', 0, '1522106341721571330', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910219153409', '1437679155466715137', 0, '1446679443707994114', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910223347714', '1437679155466715137', 0, '1514887258034864130', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910227542017', '1437679155466715137', 0, '1501458280725020673', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910235930625', '1437679155466715137', 0, '1441967821672345602', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910240124929', '1437679155466715137', 0, '1450759145691959297', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910248513537', '1437679155466715137', 0, '1455110558835777538', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910252707841', '1437679155466715137', 0, '1514888540338454529', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910256902145', '1437679155466715137', 0, '1525035202080935938', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910265290754', '1437679155466715137', 0, '1509783698146263042', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910269485058', '1437679155466715137', 0, '1501458722485895170', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910273679362', '1437679155466715137', 0, '1509783044174577665', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910282067969', '1437679155466715137', 0, '1446678254299848706', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910286262274', '1437679155466715137', 0, '1465163167441223681', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910290456578', '1437679155466715137', 0, '1504812898523742210', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910328205313', '1437679155466715137', 0, '1504812727236755457', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910344982530', '1437679155466715137', 0, '1476453145584205826', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910361759745', '1437679155466715137', 0, '1448182964169912322', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910365954049', '1437679155466715137', 0, '1499724494131888129', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910370148353', '1437679155466715137', 0, '1499724222366154753', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910378536962', '1437679155466715137', 0, '1501459021384581121', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910382731266', '1437679155466715137', 0, '1484084169469333506', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910391119874', '1437679155466715137', 0, '1509772997650481154', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910395314177', '1437679155466715137', 0, '1509782850607448065', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910399508482', '1437679155466715137', 0, '1509773700154458114', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910407897090', '1437679155466715137', 0, '1484083621789700098', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910420480002', '1437679155466715137', 0, '1514888930987540482', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910424674306', '1437679155466715137', 0, '1544864968194588674', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910433062914', '1437679155466715137', 0, '1530071943606108161', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910437257218', '1437679155466715137', 0, '1522105803823054850', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910441451521', '1437679155466715137', 0, '1499725741039747073', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910449840129', '1437679155466715137', 0, '1443470588823977985', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910454034433', '1437679155466715137', 0, '1541371204780097537', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910458228737', '1437679155466715137', 0, '1446679595814428674', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910466617346', '1437679155466715137', 0, '1544870728857223170', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910470811650', '1437679155466715137', 0, '1499727220484014081', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910475005954', '1437679155466715137', 0, '1504834246721601537', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910483394561', '1437679155466715137', 0, '1530071295544197122', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910487588866', '1437679155466715137', 0, '1509773920980369409', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910491783169', '1437679155466715137', 0, '1479422946272260098', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910500171777', '1437679155466715137', 0, '1505783546796507138', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910504366082', '1437679155466715137', 0, '1514938787685138433', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910508560386', '1437679155466715137', 0, '1525033081474387970', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910516948994', '1437679155466715137', 0, '1525034654644572162', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910521143298', '1437679155466715137', 0, '1476829542114869249', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910575669249', '1437679155466715137', 0, '1509773377268547585', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910592446466', '1437679155466715137', 0, '1525033179499466754', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910605029378', '1437679155466715137', 0, '1499724838559744001', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910613417986', '1437679155466715137', 0, '1476074922123874305', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');
INSERT INTO `sys_permission` VALUES ('1548959910621806594', '1437679155466715137', 0, '1514947248204480513', 1, 1, 0, '2022-07-18 17:16:35', '1', '2022-07-18 17:16:35', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户ID',
  `org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属当前组织的ID',
  `admin_flag` tinyint(1) NULL DEFAULT 0 COMMENT '是否内置的admin',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色描述',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:0-已禁用,1-正常',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除：0否1是',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '0', '0', 1, '租户管理平台超级管理员', '345', 1, 0, NULL, NULL, NULL, '2021-09-10 17:29:49');
INSERT INTO `sys_role` VALUES ('1437245748215533569', '0', '0', 0, '系统管理员', '具备系统管理权限', 1, 0, '2021-09-13 10:44:23', '1', '1', '2022-01-05 16:07:44');
INSERT INTO `sys_role` VALUES ('1437679155466715137', '1437679155114393601', '1437679155114393601', 1, '超级管理员', NULL, 1, 0, '2021-09-14 15:26:36', '1', '1', '2021-09-14 15:26:36');
INSERT INTO `sys_role` VALUES ('1438330676093067265', '1437679155114393601', '1437679155114393601', 0, '园区管理人员', NULL, 1, 0, '2021-09-16 10:35:30', '1437679155261194242', '1437679155261194242', '2022-01-10 11:09:23');
INSERT INTO `sys_role` VALUES ('1541336676195827714', '1437679155114393601', '1437679155114393601', 0, '园区运营人员', '一期培训', 1, 0, '2022-06-27 16:24:34', '1437679155261194242', '1437679155261194242', '2022-06-27 16:25:52');
INSERT INTO `sys_role` VALUES ('1547758701311361025', '1437679155114393601', '1437679155114393601', 0, '物业人员', NULL, 1, 0, '2022-07-15 09:43:25', '1437679155261194242', '1541336399027830786', '2022-07-18 16:32:40');

-- ----------------------------
-- Table structure for sys_rs_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_rs_user`;
CREATE TABLE `sys_rs_user`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码,当user_type=2时，表示openid',
  `user_type` tinyint NULL DEFAULT NULL COMMENT '0：平台的用户，1租户的用户',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态:0-已禁用,1-正常',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除 0否，1 是',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_rs_user
-- ----------------------------
INSERT INTO `sys_rs_user` VALUES ('1', 'admin', '5333a83a36d73d67456a24ba9ec57f885ba8d943e7d8036d1e21647c86516886', 0, 1, 0, NULL, '1', '2021-06-17 15:17:08', '2022-07-14 11:07:09');
INSERT INTO `sys_rs_user` VALUES ('1437679155261194242', 'runlian', '5333a83a36d73d67456a24ba9ec57f885ba8d943e7d8036d1e21647c86516886', 1, 1, 0, '1', '1437679155261194242', '2021-09-14 15:26:36', '2022-06-14 12:09:19');
INSERT INTO `sys_rs_user` VALUES ('1541336399027830786', '智慧谷', '29215e8275cfadea52c030921c07c85bc42fa860151e27cfa2007c1c396bdec8', 1, 1, 0, '1437679155261194242', '1437679155261194242', '2022-06-27 16:23:28', '2022-07-18 16:31:25');
INSERT INTO `sys_rs_user` VALUES ('1541368271011250177', 'rongyi', '5333a83a36d73d67456a24ba9ec57f885ba8d943e7d8036d1e21647c86516886', 1, 1, 0, '1437679155261194242', '1437679155261194242', '2022-06-27 18:30:07', '2022-07-15 09:32:41');
INSERT INTO `sys_rs_user` VALUES ('1545315770314981378', 'wx_15625977402', 'ab0c854119d548aaa57d1b4da5827475', 2, 1, 0, '1545315770314981378', '1545315770314981378', '2022-07-08 15:56:04', '2022-07-08 15:56:04');
INSERT INTO `sys_rs_user` VALUES ('1546768920943198210', 'wx_15184303750', '158b8e65d8eb4d76a09714e90f6bb10e', 2, 1, 0, '1546768920943198210', '1546768920943198210', '2022-07-12 16:10:22', '2022-07-12 16:10:22');
INSERT INTO `sys_rs_user` VALUES ('1547048111798419457', 'wx_18566267794', '1f554ce3d61b41309c398767cad0b813', 2, 1, 0, '1547048111798419457', '1547048111798419457', '2022-07-13 10:39:47', '2022-07-13 10:39:47');
INSERT INTO `sys_rs_user` VALUES ('1547460916817100801', 'zhangyi', '16a16772347c011cc6ae92292534307c7e2124e7bffffed752d64937537d5640', 0, 1, 0, '1', '1', '2022-07-14 14:00:07', '2022-07-14 14:00:07');
INSERT INTO `sys_rs_user` VALUES ('1548843305896181762', 'wx_13726243654', '8f4b0daf35cf4011aad594eea011660f', 2, 1, 0, '1548843305896181762', '1548843305896181762', '2022-07-18 09:33:14', '2022-07-18 09:33:14');

-- ----------------------------
-- Table structure for sys_rs_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_rs_user_info`;
CREATE TABLE `sys_rs_user_info`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对应tb_user表',
  `org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属组织',
  `admin_flag` tinyint NOT NULL DEFAULT 0 COMMENT '是否内置管理员.0否，1是',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '昵称',
  `real_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '姓名',
  `sex` tinyint NULL DEFAULT 1 COMMENT '性别：1男；2女',
  `id_card` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '身份证',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机',
  `telephone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '电话',
  `wechat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '微信号',
  `email` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '邮箱',
  `avatar_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像URL',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '地址',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除：0-否；1-是',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '修改者',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`real_name`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户扩展信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_rs_user_info
-- ----------------------------
INSERT INTO `sys_rs_user_info` VALUES ('1', '1', '0', 1, '', '超级管理员', 1, '', '13355556666', '', '', '', '', '', 0, '', '1', '2021-06-17 15:17:30', '2022-06-23 10:42:14');
INSERT INTO `sys_rs_user_info` VALUES ('1437679155378634754', '1437679155261194242', '1437679155114393601', 1, '', '郭超', 1, '', '17665354037', '', '', '', '', '', 0, '1', '1437679155261194242', '2021-09-14 15:26:36', '2021-10-29 17:05:22');
INSERT INTO `sys_rs_user_info` VALUES ('1541336399044608002', '1541336399027830786', '1437679155114393601', 0, '', '张怡', 1, '', '18710470624', '', '', '', '', '', 0, '1437679155261194242', '1437679155261194242', '2022-06-27 16:23:28', '2022-06-27 16:23:28');
INSERT INTO `sys_rs_user_info` VALUES ('1541368271028027394', '1541368271011250177', '1437679155114393601', 0, '', '容易', 1, '', '15818699785', '', '', '', '', '', 0, '1437679155261194242', '1437679155261194242', '2022-06-27 18:30:07', '2022-06-27 18:30:07');
INSERT INTO `sys_rs_user_info` VALUES ('1545315770394673153', '1545315770314981378', NULL, 0, 'wx_15625977402', '', 1, '', '15625977402', '', '', '', '', '', 0, '1545315770314981378', '1545315770314981378', '2022-07-08 15:56:04', '2022-07-08 15:56:04');
INSERT INTO `sys_rs_user_info` VALUES ('1546768920955781121', '1546768920943198210', NULL, 0, 'wx_15184303750', '', 1, '', '15184303750', '', '', '', '', '', 0, '1546768920943198210', '1546768920943198210', '2022-07-12 16:10:22', '2022-07-12 16:10:22');
INSERT INTO `sys_rs_user_info` VALUES ('1547048111811002369', '1547048111798419457', NULL, 0, 'wx_18566267794', '', 1, '', '18566267794', '', '', '', '', '', 0, '1547048111798419457', '1547048111798419457', '2022-07-13 10:39:47', '2022-07-13 10:39:47');
INSERT INTO `sys_rs_user_info` VALUES ('1547460916842266625', '1547460916817100801', '0', 0, '', '张怡', 1, '', '18710470627', '', '', '', '', '', 0, '1', '1', '2022-07-14 14:00:07', '2022-07-21 14:01:30');
INSERT INTO `sys_rs_user_info` VALUES ('1548843305917153282', '1548843305896181762', NULL, 0, 'wx_13726243654', '', 1, '', '13726243654', '', '', '', '', '', 0, '1548843305896181762', '1548843305896181762', '2022-07-18 09:33:14', '2022-07-18 09:33:14');

-- ----------------------------
-- Table structure for sys_rs_user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_rs_user_role_relation`;
CREATE TABLE `sys_rs_user_role_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户Id',
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色Id',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态:0-已禁用,1-正常',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态；0未删除，1已删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_rs_user_role_relation
-- ----------------------------
INSERT INTO `sys_rs_user_role_relation` VALUES ('1438765176488976386', '1437679155261194242', '1437679155466715137', 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_rs_user_role_relation` VALUES ('1528928218226032642', '1', '1437245748215533569', 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_rs_user_role_relation` VALUES ('1528928218234421250', '1', '1', 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_rs_user_role_relation` VALUES ('1547756001161052162', '1541368271011250177', '1437679155466715137', 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_rs_user_role_relation` VALUES ('1548948859956166657', '1437679155261194242', '1547758701311361025', 1, 0, NULL, '1541336399027830786', NULL, '1541336399027830786');
INSERT INTO `sys_rs_user_role_relation` VALUES ('1548948859964555266', '1541368271011250177', '1547758701311361025', 1, 0, NULL, '1541336399027830786', NULL, '1541336399027830786');
INSERT INTO `sys_rs_user_role_relation` VALUES ('1549656453658116097', '1541368271011250177', '1438330676093067265', 1, 0, NULL, '1437679155261194242', NULL, '1437679155261194242');
INSERT INTO `sys_rs_user_role_relation` VALUES ('1549940111820390402', '1541368271011250177', '1541336676195827714', 1, 0, NULL, '1437679155261194242', NULL, '1437679155261194242');
INSERT INTO `sys_rs_user_role_relation` VALUES ('1549940111828779010', '1541336399027830786', '1541336676195827714', 1, 0, NULL, '1437679155261194242', NULL, '1437679155261194242');
INSERT INTO `sys_rs_user_role_relation` VALUES ('1549997979856732162', '1547460916817100801', '1437245748215533569', 1, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_tag
-- ----------------------------
DROP TABLE IF EXISTS `sys_tag`;
CREATE TABLE `sys_tag`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签Id',
  `tag_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织Id',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司Id',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '0,禁用，1启用',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tag
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wx_oa_mobile_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_wx_oa_mobile_relation`;
CREATE TABLE `sys_wx_oa_mobile_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `open_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号用户openId',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公众号appId',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公众号用户openId与手机号关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_wx_oa_mobile_relation
-- ----------------------------

-- ----------------------------
-- Table structure for sys_wx_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_wx_user`;
CREATE TABLE `sys_wx_user`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `openid` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '小程序用户openid',
  `mobile_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话号',
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信昵称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '微信小程序用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_wx_user
-- ----------------------------

-- ----------------------------
-- Table structure for vis_access_policy
-- ----------------------------
DROP TABLE IF EXISTS `vis_access_policy`;
CREATE TABLE `vis_access_policy`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `policy_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '策略名称',
  `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户ID',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区ID',
  `company_id` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业ID集合',
  `tag_ids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '允许员工标签ID(逗号隔开)',
  `allow_visitor` tinyint NULL DEFAULT NULL COMMENT '访客通行：0-不适应，1-适用',
  `device_ids` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备id(逗号隔开)',
  `policy_start_time` datetime NOT NULL COMMENT '策略生效时间',
  `policy_expire_time` datetime NOT NULL COMMENT '策略过期时间',
  `all_time` tinyint NULL DEFAULT 0 COMMENT '0-全时段，1-指定时段',
  `specified_time_period` json NULL COMMENT '指定时段具体信息(JSON)',
  `status` tinyint NULL DEFAULT 0 COMMENT '0-禁用，1-启用',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改者',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `expire` tinyint NOT NULL DEFAULT 0 COMMENT '已过期，0：否 1：是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_access_policy_park_org_id`(`park_org_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通行策略' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_access_policy
-- ----------------------------

-- ----------------------------
-- Table structure for vis_access_record
-- ----------------------------
DROP TABLE IF EXISTS `vis_access_record`;
CREATE TABLE `vis_access_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `face_image_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登记照片url',
  `scene_image_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '抓拍照片url',
  `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '员工ID',
  `label_ids` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户标签ID(逗号隔开)',
  `labels` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户标签(逗号隔开)',
  `recognize_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '识别时间',
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备ID',
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名称',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `device_location` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备位置',
  `is_pass` tinyint NULL DEFAULT NULL COMMENT '是否通过：0-识别不通过，1-识别通过',
  `is_over_temp` tinyint NULL DEFAULT NULL COMMENT '体温是否超过37.3：0正常、1超标',
  `health_code` int NULL DEFAULT NULL COMMENT '健康码，0：绿色 1：黄色 2：红色',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_access_record_moblie`(`mobile`) USING BTREE,
  INDEX `idx_access_record_device_id`(`device_id`) USING BTREE,
  INDEX `idx_access_record_park_org_id`(`park_org_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '抓拍记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_access_record
-- ----------------------------

-- ----------------------------
-- Table structure for vis_apartment_person
-- ----------------------------
DROP TABLE IF EXISTS `vis_apartment_person`;
CREATE TABLE `vis_apartment_person`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区组织Id',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属企业Id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工Id',
  `id_card_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `house_ids` json NULL COMMENT '关联房屋id集合',
  `device_ids` json NULL COMMENT '公寓门禁设备id集合',
  `effect_start_date` date NOT NULL COMMENT '生效开始日期',
  `effect_end_date` date NOT NULL COMMENT '生效结束日期',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态，0：禁用 1：启用',
  `expire` tinyint NOT NULL DEFAULT 0 COMMENT '已过期，0：否 1：是',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公寓人员管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_apartment_person
-- ----------------------------

-- ----------------------------
-- Table structure for vis_apartment_person_house_relation
-- ----------------------------
DROP TABLE IF EXISTS `vis_apartment_person_house_relation`;
CREATE TABLE `vis_apartment_person_house_relation`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `apartment_person_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公寓人员Id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工Id',
  `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联房屋id',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '公寓人员管理房屋关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_apartment_person_house_relation
-- ----------------------------

-- ----------------------------
-- Table structure for vis_audit_person_config
-- ----------------------------
DROP TABLE IF EXISTS `vis_audit_person_config`;
CREATE TABLE `vis_audit_person_config`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区组织ID',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业ID',
  `audit_employee_ids` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批员工ID集合（英文逗号隔开）',
  `invitor_employee_audit` tinyint NULL DEFAULT NULL COMMENT '被访人审批，0：否 1：是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '访客审批人配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_audit_person_config
-- ----------------------------

-- ----------------------------
-- Table structure for vis_car
-- ----------------------------
DROP TABLE IF EXISTS `vis_car`;
CREATE TABLE `vis_car`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `car_plate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `car_owner_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车主姓名',
  `tel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话号码',
  `car_type` int NULL DEFAULT NULL COMMENT '车辆类型，1:员工车辆、2:公务车辆、3:外部车辆、4:红名单',
  `auth_start_time` datetime NULL DEFAULT NULL COMMENT '授权开始时间',
  `auth_end_time` datetime NULL DEFAULT NULL COMMENT '授权结束时间',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '企业ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态：0-未生效；1-已生效；2-已失效',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_car_tel`(`tel`) USING BTREE,
  INDEX `idx_car_park_org_id`(`park_org_id`) USING BTREE,
  INDEX `idx_car_company_id`(`company_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车辆信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_car
-- ----------------------------

-- ----------------------------
-- Table structure for vis_car_access_record
-- ----------------------------
DROP TABLE IF EXISTS `vis_car_access_record`;
CREATE TABLE `vis_car_access_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `img_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '抓拍照片',
  `car_plate` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `car_type` int NULL DEFAULT NULL COMMENT '车辆类型，1:员工车辆、2:公务车辆、3:外部车辆、4:红名单',
  `car_registered` int NULL DEFAULT NULL COMMENT '是否在册,0：不在册，1：在册',
  `recognize_time` datetime NULL DEFAULT NULL COMMENT '识别时间',
  `device_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备ID',
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `parking_lot_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '停车场ID',
  `parking_lot_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '停车场名',
  `car_lane_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车道ID',
  `car_lane_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车道名',
  `gateway_type` int NULL DEFAULT NULL COMMENT '出入口，0：入口 1：出口',
  `access_result` int NULL DEFAULT NULL COMMENT '通行结果，0：识别不通过 1：成功放行',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车牌抓拍记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_car_access_record
-- ----------------------------

-- ----------------------------
-- Table structure for vis_car_access_record_rel
-- ----------------------------
DROP TABLE IF EXISTS `vis_car_access_record_rel`;
CREATE TABLE `vis_car_access_record_rel`  (
  `id` int NOT NULL,
  `car_plate` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车牌号',
  `enter_car_access_record_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驶入记录ID',
  `exit_car_access_record_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驶出记录ID',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车辆通行抓拍记录关联关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_car_access_record_rel
-- ----------------------------

-- ----------------------------
-- Table structure for vis_car_lane
-- ----------------------------
DROP TABLE IF EXISTS `vis_car_lane`;
CREATE TABLE `vis_car_lane`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lane_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '车道名',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `park_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `parking_lot_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '停车场ID',
  `parking_lot_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '停车场名称',
  `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parking_log_id`(`parking_lot_id`) USING BTREE COMMENT '停车场ID索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车道' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_car_lane
-- ----------------------------

-- ----------------------------
-- Table structure for vis_notice
-- ----------------------------
DROP TABLE IF EXISTS `vis_notice`;
CREATE TABLE `vis_notice`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区id',
  `notice_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '须知名称',
  `notice_content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '须知内容',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '来访须知' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_notice
-- ----------------------------

-- ----------------------------
-- Table structure for vis_parking_lot
-- ----------------------------
DROP TABLE IF EXISTS `vis_parking_lot`;
CREATE TABLE `vis_parking_lot`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `parking_lot_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '停车场名称',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `number_of_space` int NULL DEFAULT NULL COMMENT '车位数量',
  `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '停车场' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_parking_lot
-- ----------------------------

-- ----------------------------
-- Table structure for vis_process_config
-- ----------------------------
DROP TABLE IF EXISTS `vis_process_config`;
CREATE TABLE `vis_process_config`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
  `process_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程名',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区组织ID',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业ID',
  `process_type` tinyint NULL DEFAULT NULL COMMENT '流程类型，1：访客预约 2：访客邀约',
  `visitor_type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客类型ID',
  `process_nodes` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客流程节点集合（英文逗号分隔），1：访客预约 2：员工审核 3：员工邀请 4：访客补充信息',
  `enable` tinyint NULL DEFAULT 1 COMMENT '启用：0：否 1：是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '访客流程配置' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_process_config
-- ----------------------------

-- ----------------------------
-- Table structure for vis_visitor_record
-- ----------------------------
DROP TABLE IF EXISTS `vis_visitor_record`;
CREATE TABLE `vis_visitor_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区ID',
  `visitor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客名',
  `visitor_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客电话号码',
  `visitor_id_card` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客身份证号',
  `visitor_type_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客类型ID',
  `visitor_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客类型名',
  `visitor_count` int NULL DEFAULT 1 COMMENT '访客人数',
  `visitor_face_img` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客人脸图',
  `visitor_car_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客车牌号',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问的企业ID',
  `visit_date` date NULL DEFAULT NULL COMMENT '来访日期',
  `sign_out_time` datetime NULL DEFAULT NULL COMMENT '签出时间',
  `invitor_employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请人或被访人',
  `invitor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请人备注',
  `access_policy_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通行策略ID',
  `is_register` tinyint(1) NULL DEFAULT 0 COMMENT '已现场核验，0：未现场登记核验 1：已现场登记核验',
  `register_type` tinyint NULL DEFAULT NULL COMMENT '登记类型，1:人证核验 2:人工登记',
  `register_time` datetime NULL DEFAULT NULL COMMENT '登记核验时间',
  `sign_in_status` tinyint NULL DEFAULT 1 COMMENT '登入状态：1:未登入 2:已登入 3:已登出',
  `visitor_source` tinyint NULL DEFAULT NULL COMMENT '访客来源：1:访客预约 2:访客邀约 3:现场登记',
  `audit_employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批人',
  `audit_remark` varchar(155) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批备注',
  `subscribe_process_status` tinyint NULL DEFAULT NULL COMMENT '预约流程状态：0:待审核 1:审核通过 2:审核不通过 3:已取消',
  `invite_process_status` tinyint NULL DEFAULT NULL COMMENT '邀请流程状态：0:邀请中 1:邀请成功',
  `success_flag` tinyint NULL DEFAULT 0 COMMENT '预约成功标识,0:否 1:是',
  `visit_reason` tinyint NULL DEFAULT NULL COMMENT '来访事由 1:商务交流 2:面试 3:其他',
  `visitor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访客备注',
  `invitor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '被访人姓名，邀请人姓名',
  `invitor_tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '被访人电话号码，邀请人电话',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  `card_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '人员卡号',
  `device_person_id` bigint NULL DEFAULT NULL COMMENT '设备人员ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '访客记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_visitor_record
-- ----------------------------

-- ----------------------------
-- Table structure for vis_visitor_type
-- ----------------------------
DROP TABLE IF EXISTS `vis_visitor_type`;
CREATE TABLE `vis_visitor_type`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `visitor_type_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '访客类型名称',
  `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '入驻企业ID',
  `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区组织ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '访客类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of vis_visitor_type
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
