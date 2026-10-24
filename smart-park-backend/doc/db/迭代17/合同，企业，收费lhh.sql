ALTER TABLE `smart-park`.`ind_enterprise_service`
modify `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注';


INSERT INTO `sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`)
 VALUES ('JUMP_TYPE_1', NULL, 'JUMP_TYPE', '跳转类型', 'NOTHING', '无', 0, 3, '2022-01-04 10:11:24', '2022-05-31 14:06:27', 0);
INSERT INTO `sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`)
 VALUES ('JUMP_TYPE_2', NULL, 'JUMP_TYPE', '跳转类型', 'URL', 'URL', 1, 1, '2022-01-04 10:11:24', '2022-06-22 11:20:22', 0);
INSERT INTO `sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`)
 VALUES ('JUMP_TYPE_3', NULL, 'JUMP_TYPE', '跳转类型', 'MINI_PROGRAM', '小程序', 2, 2, '2022-01-04 10:11:24', '2022-04-20 16:37:02', 0);


/*
 Navicat Premium Data Transfer

 Source Server         : park-sit
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 10.200.3.154:8006
 Source Schema         : smart-park

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 26/07/2022 14:45:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dev_client_device
-- ----------------------------
DROP TABLE IF EXISTS `dev_client_device`;
CREATE TABLE `dev_client_device`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `device_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备名称',
  `device_sn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备序列号',
  `gateway_sn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网关主键id',
  `device_type` int(0) NOT NULL COMMENT '设备类型',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备位置',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态；1在线 0离线',
  `area_id` bigint(0) NULL DEFAULT NULL COMMENT '区域id',
  `factory_code` int(0) NOT NULL DEFAULT 21337 COMMENT '设备厂家编码	',
  `factory_subtype` int(0) NOT NULL COMMENT '设备子类型',
  `room_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '位置（设备属性）',
  `auth_state` tinyint(0) NULL DEFAULT 0 COMMENT '认证状态 0未认证 1已认证 2失效',
  `tenant_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户id',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '0 否，1 是',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1801 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '设备管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dev_client_device
-- ----------------------------
INSERT INTO `dev_client_device` VALUES ('1811', '开合帘（后1）', '265359020148C6', '2418C69FCBAA', 4, NULL, NULL, 1, 69, 257, 1, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1812', '电视右', '2953590202538D', '2418C69FCBAA', 41, NULL, NULL, 1, 69, 21337, 10017, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1813', '电视（中）', '2953590202538C', '2418C69FCBAA', 41, NULL, NULL, 1, 69, 21337, 10017, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1814', '筒灯（右3）', '0D535901076FA2', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1815', '中央射灯5', '0D535901076C4E', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1816', '筒灯（右1）', '0D535901076995', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1817', '灯带', 'F52022107E3385', '2418C69FCBAA', 193, NULL, NULL, 0, 69, 8226, 2, '', 2, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1818', '中央射灯3', '0D535901077125', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1819', '中央射灯1', '0D535901076D6A', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1820', '中央射灯4', '0D535901077121', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1821', '筒灯（前2）', '0D535901076B06', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1822', '空调1', '2953590202538E', '2418C69FCBAA', 41, NULL, NULL, 1, 69, 21337, 10017, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1823', '中央射灯2', '0D5359010769C0', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1824', '筒灯（左1）', '0D535901076FFE', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1825', '筒灯（后1）', '0D535901076A46', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1826', '人体雷达前', '1A5359070120FB', '2418C69FCBAA', 26, NULL, NULL, 1, 69, 21337, 10008, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1827', '语音管家', '295359020227E4', '2418C69FCBAA', 41, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1828', '外围灯带1', '0D53590107DF1D', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1829', '筒灯（前1）', '0D535901076F80', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1830', '内灯带2', '0D53590107DB89', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1831', '开合帘（左）', '2653590201091F', '2418C69FCBAA', 4, NULL, NULL, 1, 69, 257, 1, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1832', '电视（左）', '29535902025366', '2418C69FCBAA', 41, NULL, NULL, 1, 69, 21337, 10017, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1833', '开合帘（后2）', '2653590201093A', '2418C69FCBAA', 4, NULL, NULL, 1, 69, 257, 1, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1834', '门磁传感器（左）', '1A53590101017F', '2418C69FCBAA', 26, NULL, NULL, 1, 69, 21337, 10001, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1835', '门磁传感器（右）', '1A535901010116', '2418C69FCBAA', 26, NULL, NULL, 1, 69, 21337, 10001, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1836', '中央射灯6', '0D535901077016', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1837', '筒灯（左2）', '0D535901076C3E', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1838', '外围灯带2', '0D53590107DF25', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1839', '亮度传感器', '33535901014105', '2418C69FCBAA', 51, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 1, NULL, '2022-07-18 11:06:39', NULL, '2022-07-26 10:23:16');
INSERT INTO `dev_client_device` VALUES ('1840', '筒灯（右2）', '0D535901076B9E', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1841', '筒灯（后2）', '0D535901076D9D', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1842', '环境传感器', '12535901010088', '2418C69FCBAA', 18, NULL, NULL, 1, 69, 21337, 10001, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1843', '筒灯（左3）', '0D535901076C39', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1844', '中心大灯', '0D5359010416EE', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1845', '筒灯（后3）', '0D535901076E10', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1846', '内灯带1', '0D53590107DB94', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1847', '筒灯（前3）', '0D535901076C30', '2418C69FCBAA', 13, NULL, NULL, 1, 69, 21337, 0, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1848', '人体雷达后', '1A5359070120EF', '2418C69FCBAA', 26, NULL, NULL, 1, 69, 21337, 10008, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1849', '开合帘（右）', '2653590201092D', '2418C69FCBAA', 4, NULL, NULL, 1, 69, 257, 1, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');
INSERT INTO `dev_client_device` VALUES ('1850', '空调2', '29535902025379', '2418C69FCBAA', 41, NULL, NULL, 1, 69, 21337, 10017, '', 1, 'C22', 0, NULL, '2022-07-18 11:06:39', NULL, '2022-07-18 11:06:39');

SET FOREIGN_KEY_CHECKS = 1;
