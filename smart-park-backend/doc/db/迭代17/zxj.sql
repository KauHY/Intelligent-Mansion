INSERT INTO `com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`,
                        `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`)
VALUES ('generateMeterReadingRecord', '0 0 0 28 * ?', '生成抄表记录', '每月28日进行抄表', 0, NULL, NULL, NULL, 'park-device',
        'meterReadingRecordServiceTask', 'generateMeterReadingRecord');


INSERT INTO `com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`,
                        `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`)
VALUES ('generateMeterConsumptionRecord', '0 0 0 1 * ?', '生成水电表用量记录', '生成水电表用量记录,及生成水电表关联的合同房屋，每月1日计算上个月的的水表表的水电用量', 0,
        NULL, NULL, NULL, 'park-device',
        'meterConsumptionRecordServiceTask', 'generateMeterConsumptionRecord');


INSERT INTO `com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`,
                        `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`)
VALUES ('generateMeterConsumption', '0 10 0 1 * ?', '计算企业或房屋的水电用量', '生成水电表用量记录后再生成企业或房屋水电用量，每月1日计算上个月的的', 0,
        NULL, NULL, NULL, 'park-device',
        'meterConsumptionTask', 'generateMeterConsumption');


ALTER TABLE `ins_inspection_point_detail`
    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON
UPDATE CURRENT_TIMESTAMP;


INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('TAX_L10', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_L10', '10w以下', 1, 1);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('TAX_20', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_20', '10-20w', 2, 2);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('TAX_30', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_30', '20-30w', 3, 3);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('TAX_50', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_50', '30-50w', 4, 4);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('TAX_G50', NULL, 'COMPANY_TAX_LEVEL', '税收分布', 'TAX_G50', '50w以上', 5, 5);

update `sys_dictionary`
set value= 6
where id = 'ELECTRICITY_CATEGORY';

update `sys_dictionary`
set value= 5
where id = 'WATER_CATEGORY';

ALTER TABLE `con_template`
    ADD COLUMN `contract_key` varchar(512) NULL COMMENT '生成的合同key' AFTER `template_name`;

ALTER TABLE `con_template`
    ADD COLUMN `contract_key_time` datetime NULL COMMENT '生成的合同时间';

/*
 Navicat Premium Data Transfer

 Source Server         : 10.200.3.154_测试环境
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 10.200.3.154:8006
 Source Schema         : smart-park

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 10/06/2022 17:56:52
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for con_template_library
-- ----------------------------
DROP TABLE IF EXISTS `con_template_library`;
CREATE TABLE `con_template_library`
(
    `id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '主键',
    `parent_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级',
    `group_ids`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分组ID',
    `type`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '类型',
    `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述说明',
    `code`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'value对应的编码',
    `label`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '-' COMMENT '名称',
    `value`       varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '-' COMMENT '值（前端）',
    `field_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '匹配的字段名',
    `sort`        int NULL DEFAULT NULL COMMENT '排序',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`     tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0否1是',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板字段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of con_template_library
-- ----------------------------
INSERT INTO `con_template_library`
VALUES ('1', '0', '0', 'varchar', '-', '-', '合同信息', '-', NULL, 1, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('101', '1', '1', 'varchar', '-', '-', '编号', '${编号}', 'no', 24, '取值于“协议编号”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('102', '1', '1', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 10, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('103', '1', '1', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 6, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('104', '1', '1', 'date', '-', '-', '签约日期', '${签约日期}', 'contractedDate', 18, '取值于“办理日期”；', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('105', '1', '2', 'varchar', '-', '-', '面积', '${面积}', 'totalArea', 25, '取值于“总面积”', NULL, '2022-04-11 16:34:51',
        0);
INSERT INTO `con_template_library`
VALUES ('106', '1', '2', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 11, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('107', '1', '2', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 7, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('108', '1', '2', 'date', '-', '-', '签定日期', '${签定日期}', 'contractedDate', 15, '取值于“办理日期”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('109', '1', '3', 'varchar', '-', '-', '合同编号', '${合同编号}', 'no', 19, '取值于“合同编号”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('110', '1', '3', 'number', '-', '-', '租赁期（年）', '${租赁期（年）}', 'leaseTermYear', 28, '取值于“租期”，转成大写', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('111', '1', '3', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 12, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('112', '1', '3', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 8, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('113', '1', '3', 'varchar', '-', '-', '免租期', '${免租期}', 'freeRentDay', 22, '取值于“免租期”', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('114', '1', '3', 'date', '-', '-', '合同签订日', '${合同签订日}', 'contractedDate', 16, '取值于“办理日期”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('115', '1', '5', 'varchar', '-', '-', '编号', '${编号}', 'no', 20, '取值于“合同编号”', NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('116', '1', '5', 'varchar', '-', '-', '客房数', '${客房数}', 'numberOfRooms', 21, '取值于“客房数”', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('117', '1', '5', 'money', '-', '-', '租金合计', '${租金合计}', 'rentAmount', 26, '取值于“租金”，（壹万叁仟肆佰元整）', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('118', '1', '5', 'betweendate', '-', '-', '有效期', '${有效期}', 'betweendate', 5, '2021年7月1日—2022年6月30日', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('119', '1', '5', 'date', '-', '-', '签约日期', '${签约日期}', 'contractedDate', 17, '取值于“办理日期”', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('120', '1', '5', 'number', '-', '-', '租期', '${租期}', 'leaseTermYear', 27, '取值于“租期”', NULL, '2022-04-11 16:34:50',
        0);
INSERT INTO `con_template_library`
VALUES ('121', '1', '4', 'betweendate', '-', '-', '起止日期', '${起止日期}', 'betweendate', 29, NULL, NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('122', '1', '4', 'date', '-', '-', '合同开始日期', '${合同开始日期}', 'startDate', 13, '取值“有效期的开始日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('123', '1', '4', 'date', '-', '-', '合同结束日期', '${合同结束日期}', 'endDate', 9, '取值“有效期的结束日期”，格式为XXX年XXX月XXX日', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('124', '1', '4', 'varchar', '-', '-', '物业费', '${物业费}', 'propertyCosts', 23, '取值于“物业费”', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('125', '1', '4', 'number', '-', '-', '服务期限', '${服务期限}', 'leaseTermYear', 14, '取值“租期”', NULL,
        '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('2', '0', '0', 'varchar', '-', '-', '用户信息', '-', NULL, 2, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('201', '2', '1', 'varchar', '-', '-', '甲方', '${甲方}', 'nameA', 30, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('202', '2', '1', 'varchar', '-', '-', '甲方联系人', '${甲方联系人}', 'contactA', 39, '取值于“甲方联系人”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('203', '2', '1', 'varchar', '-', '-', '乙方', '${乙方}', 'nameB', 45, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('204', '2', '1', 'varchar', '-', '-', '乙方联系人', '${乙方联系人}', 'contactB', 55, '取值于“乙方联系人”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('205', '2', '1', 'varchar', '-', '-', '乙方联系电话', '${乙方联系电话}', 'contactTelB', 52, '取值于“乙方联系电话”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('206', '2', '1', 'varchar', '-', '-', '乙方联系人身份证号码', '${乙方联系人身份证号码}', 'contactIdCardNoB', 59, NULL, NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('207', '2', '2', 'varchar', '-', '-', '甲方', '${甲方}', 'nameA', 31, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('208', '2', '2', 'varchar', '-', '-', '乙方', '${乙方}', 'nameB', 46, '取值于“乙方”', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('209', '2', '2', 'varchar', '-', '-', '乙方年纳税', '${乙方年纳税}', 'annualTaxTenThousand', 44, '取值于“年纳税额”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('210', '2', '2', 'varchar', '-', '-', '乙方经营范围', '${乙方经营范围}', 'businessScope', 43, '取值于“经营范围”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('211', '2', '3', 'varchar', '-', '-', '出租方', '${出租方}', 'nameA', 32, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('212', '2', '3', 'varchar', '-', '-', '出租方统一社会信用代码', '${出租方统一社会信用代码}', 'creditCodeA', 42, '取值于“甲方统一社会信用代码”',
        NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('213', '2', '3', 'varchar', '-', '-', '出租方指定联系人', '${出租方指定联系人}', 'contactA', 40, '取值于“甲方联系人”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('214', '2', '3', 'varchar', '-', '-', '出租方地址', '${出租方地址}', 'addressA', 35, '取值于“甲方地址”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('215', '2', '3', 'varchar', '-', '-', '出租方电话', '${出租方电话}', 'contactTelA', 37, '取值于“甲方联系电话”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('216', '2', '3', 'varchar', '-', '-', '承租方', '${承租方}', 'nameB', 47, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('217', '2', '3', 'varchar', '-', '-', '承租方统一社会信用代码', '${承租方统一社会信用代码}', 'creditCodeB', 58, '取值于“乙方统一社会信用代码”',
        NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('218', '2', '3', 'varchar', '-', '-', '承租方指定联系人', '${承租方指定联系人}', 'contactB', 56, '取值于“乙方联系人”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('219', '2', '3', 'varchar', '-', '-', '承租方地址', '${承租方地址}', 'addressB', 50, '取值于“乙方地址”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('220', '2', '3', 'varchar', '-', '-', '承租方电话', '${承租方电话}', 'contactTelB', 53, '取值于“乙方联系电话”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('221', '2', '5', 'varchar', '-', '-', '出租方', '${出租方}', 'nameA', 33, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('222', '2', '5', 'varchar', '-', '-', '承租方', '${承租方}', 'nameB', 48, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('223', '2', '4', 'varchar', '-', '-', '甲方', '${甲方}', 'nameA', 34, '取值于“甲方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('224', '2', '4', 'varchar', '-', '-', '甲方承办人', '${甲方承办人}', 'contactA', 41, '取值于“甲方联系人”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('225', '2', '4', 'varchar', '-', '-', '甲方地址', '${甲方地址}', 'addressA', 36, '取值于“甲方地址”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('226', '2', '4', 'varchar', '-', '-', '甲方电话', '${甲方电话}', 'contactTelA', 38, '取值于“甲方联系电话”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('227', '2', '4', 'varchar', '-', '-', '乙方', '${乙方}', 'nameB', 49, '取值于“乙方”', NULL, '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('228', '2', '4', 'varchar', '-', '-', '乙方承办人', '${乙方承办人}', 'contactB', 57, '取值于“乙方联系人”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('229', '2', '4', 'varchar', '-', '-', '乙方地址', '${乙方地址}', 'addressB', 51, '取值于“乙方地址”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('230', '2', '4', 'varchar', '-', '-', '乙方电话', '${乙方电话}', 'contactTelB', 54, '取值于“乙方联系电话”', NULL,
        '2022-04-11 16:34:51', 0);
INSERT INTO `con_template_library`
VALUES ('3', '0', '0', 'varchar', '-', '-', '关联房屋信息', '-', NULL, 3, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('301', '3', '1', 'varchar', '-', '-', '关联房屋', '${关联房屋}', 'houseMessagesStr', 69, '取值于“所属楼栋单元、所在楼层”', NULL,
        '2022-04-15 16:28:36', 0);
INSERT INTO `con_template_library`
VALUES ('302', '3', '1', 'varchar', '-', '-', '所属园区', '${所属园区}', 'parkOrgName', 71, '取值于“所属园区”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('303', '3', '1', 'varchar', '-', '-', '建筑面积', '${建筑面积}', 'totalArea', 77, '取值于“总面积”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('304', '3', '1', 'varchar', '-', '-', '房屋用途', '${房屋用途}', 'houseFunction', 62, '取值于“房屋功能”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('305', '3', '2', 'varchar', '-', '-', '关联房屋', '${关联房屋}', 'houseMessagesStr', 68, '取值于“所属楼栋单元、楼层房号”', NULL,
        '2022-04-15 16:28:39', 0);
INSERT INTO `con_template_library`
VALUES ('306', '3', '3', 'list', '-', '-', '房屋坐落', '${房屋坐落}', 'houseMessagesList', 65, '取值于“房源名称”', NULL,
        '2022-06-10 16:28:22', 0);
INSERT INTO `con_template_library`
VALUES ('307', '3', '3', 'varchar', '-', '-', '所属园区', '${所属园区}', 'parkOrgName', 73, '取值于“所属园区”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('308', '3', '3', 'varchar', '-', '-', '租用面积', '${租用面积}', 'totalArea', 78, '取值于“总面积”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('309', '3', '3', 'list', '-', '-', '用途', '${用途}', 'houseFunctionList', 63, '取值于“房屋功能”', NULL,
        '2022-06-10 15:42:00', 0);
INSERT INTO `con_template_library`
VALUES ('310', '3', '3', 'list', '-', '-', '房屋总层数', '${房屋总层数}', 'totalFloorList', 76, '取值于“总楼层数”', NULL,
        '2022-06-10 15:29:54', 0);
INSERT INTO `con_template_library`
VALUES ('311', '3', '3', 'list', '-', '-', '所在层数', '${所在层数}', 'floorList', 74, '取值于“所在楼层”', NULL, '2022-06-10 16:28:04',
        0);
INSERT INTO `con_template_library`
VALUES ('312', '3', '3', 'varchar', '-', '-', '建筑面积', '${建筑面积}', 'totalArea', 79, '取值于“总面积”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('313', '3', '5', 'varchar', '-', '-', '房号', '${房号}', 'houseMessagesStr', 67, '取值于“楼层房号”', NULL,
        '2022-04-15 16:28:44', 0);
INSERT INTO `con_template_library`
VALUES ('314', '3', '5', 'varchar', '-', '-', '单间', '${单间}', 'numberOfSingleRooms', 61, '取值于“单间”的总数', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('315', '3', '5', 'varchar', '-', '-', '标间', '${标间}', 'numberOfTwinRooms', 60, '取值于“标间”的总数', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('316', '3', '5', 'varchar', '-', '-', '套间', '${套间}', 'numberOfSuite', 75, '取值于“套间”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('317', '3', '4', 'varchar', '-', '-', '物业名称', '${物业名称}', 'parkOrgName', 72, '取值于“所属园区”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('318', '3', '4', 'varchar', '-', '-', '类型', '${类型}', 'houseFunction', 64, '取值于“房屋功能”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('319', '3', '4', 'varchar', '-', '-', '坐落位置', '${坐落位置}', 'addressA', 66, '取值于“甲方地址”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('320', '3', '4', 'varchar', '-', '-', '乙方承租房号', '${乙方承租房号}', 'houseMessagesStr', 70, '取值于“所属楼栋单元、所在楼层”', NULL,
        '2022-04-15 16:28:41', 0);
INSERT INTO `con_template_library`
VALUES ('321', '3', '4', 'varchar', '-', '-', '乙方承租面积', '${乙方承租面积}', 'totalArea', 80, '取值于“总面积”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('4', '0', '0', 'varchar', '-', '-', '计费信息', '-', NULL, 4, NULL, NULL, '2022-04-11 16:34:50', 0);
INSERT INTO `con_template_library`
VALUES ('401', '4', '3', 'varchar', '-', '-', '租赁费用标准', '${租赁费用标准}', 'unitPriceWithTax', 87,
        '取值于合同详情—计费方案的“单价元”，计算公式为建筑面积*月单价', NULL, '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('402', '4', '3', 'money', '-', '-', '房屋租赁费用', '${房屋租赁费用}', 'rentAmount', 86, '取值于“租金”（大写的租金）', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('403', '4', '3', 'money', '-', '-', '履约保证金', '${履约保证金}', 'performanceBond', 84, '取值于“履约保证金”（大写的履约保证金）', NULL,
        '2022-04-14 16:21:44', 0);
INSERT INTO `con_template_library`
VALUES ('404', '4', '5', 'calculate', '-', '-', '增值税', '${增值税}', 'vat', 83, 'XXXX元，取值于收费项为“租金”的含税总金额-不含税总金额', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('405', '4', '5', 'varchar', '-', '-', '增值税税率', '${增值税税率}', 'taxRate', 82, '9%，取值于计费方案所关联收费项的“税率”', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('406', '4', '5', 'money', '-', '-', '缴纳押金', '${缴纳押金}', 'deposit', 85, '取值于“押金”（肆仟元整）', NULL,
        '2022-04-11 16:34:52', 0);
INSERT INTO `con_template_library`
VALUES ('407', '4', '4', 'varchar', '-', '-', '物业费收费标准', '${物业费收费标准}', 'unitPriceWithTax', 81,
        '4元/平方米•月，取值于合同详情—计费方案的“单价元”，计算公式为建筑面积*月单价', NULL, '2022-04-11 16:34:52', 0);

SET
FOREIGN_KEY_CHECKS = 1;
