CREATE TABLE `att_attendance_month_record` (
                                               `id` varchar(64) NOT NULL,
                                               `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工id',
                                               `employee_name` varchar(50) DEFAULT NULL COMMENT '员工名',
                                               `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区ID',
                                               `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区名',
                                               `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID',
                                               `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公司名',
                                               `year` int DEFAULT NULL COMMENT '年份',
                                               `month` int DEFAULT NULL COMMENT '月份',
                                               `exception_times` int DEFAULT '0' COMMENT '异常考勤次数',
                                               `absence_times` int DEFAULT '0' COMMENT '缺勤次数',
                                               `normal_times` int DEFAULT '0' COMMENT '正常考勤次数',
                                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                               `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                               `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
                                               `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考勤月度统计';

CREATE TABLE `att_attendance_record` (
                                         `id` varchar(64) NOT NULL,
                                         `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工ID',
                                         `employee_name` varchar(50) DEFAULT NULL COMMENT '员工名',
                                         `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区ID',
                                         `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区名',
                                         `company_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公司ID',
                                         `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公司名',
                                         `date` date DEFAULT NULL COMMENT '日期',
                                         `punch_in_time` datetime DEFAULT NULL COMMENT '上班打卡时间',
                                         `punch_out_time` datetime DEFAULT NULL COMMENT '下班打卡时间',
                                         `holiday` int DEFAULT NULL COMMENT '是否假期，0：否 1：是',
                                         `attendance_result` int DEFAULT NULL COMMENT '考勤结果，1：正常 2：异常',
                                         `punch_in_type` int DEFAULT NULL COMMENT '上班打卡方式，1：上下班打卡 2：外勤打卡',
                                         `punch_out_type` int DEFAULT NULL COMMENT '下班打卡方式，1：上下班打卡 2：外勤打卡',
                                         `punch_type` int DEFAULT NULL COMMENT '打卡方式，1：上下班打卡 2：外勤打卡',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                         `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                         `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
                                         `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工考勤记录';

CREATE TABLE `att_attendance_rule` (
                                       `id` varchar(32) NOT NULL,
                                       `rule_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '规则名称',
                                       `punch_in_time` time DEFAULT NULL COMMENT '上班打卡时间',
                                       `punch_out_time` time DEFAULT NULL COMMENT '下班打卡时间',
                                       `location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '考勤地点',
                                       `longitude` double DEFAULT NULL COMMENT '经度',
                                       `latitude` double DEFAULT NULL COMMENT '纬度',
                                       `days_of_work` json DEFAULT NULL COMMENT '关联工作日',
                                       `workday_type` int DEFAULT '0' COMMENT '关联工作日类型，0：法定工作日 1：自定义',
                                       `punch_range` int DEFAULT NULL COMMENT '考勤范围（米）',
                                       `status` tinyint DEFAULT '0' COMMENT '状态，0：禁用 1：启用',
                                       `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区ID',
                                       `company_id` varchar(32) DEFAULT NULL COMMENT '公司ID',
                                       `apply_type` tinyint DEFAULT '0' COMMENT '规则适用类型，0：全公司员工适用，1：指定员工',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
                                       `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考勤规则';

CREATE TABLE `att_attendance_rule_relation` (
                                                `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ID',
                                                `rule_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考勤规则ID',
                                                `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工ID',
                                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                                `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                                `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
                                                `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考勤规则关联员工';

CREATE TABLE `att_punch_record` (
                                    `id` varchar(64) NOT NULL,
                                    `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '员工ID',
                                    `employee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '员工名',
                                    `company_id` varchar(64) DEFAULT NULL COMMENT '公司ID',
                                    `company_name` varchar(50) DEFAULT NULL COMMENT '公司名',
                                    `park_org_id` varchar(64) DEFAULT NULL COMMENT '园区id',
                                    `park_org_name` varchar(50) DEFAULT NULL COMMENT '园区名',
                                    `punch_date` date NOT NULL COMMENT '打卡日期',
                                    `punch_time` time NOT NULL COMMENT '打卡时间',
                                    `punch_date_time` datetime NOT NULL COMMENT '打卡具体时间',
                                    `location` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '打卡地点',
                                    `longitude` double DEFAULT NULL COMMENT '经度',
                                    `latitude` double DEFAULT NULL COMMENT '纬度',
                                    `type` tinyint(1) DEFAULT NULL COMMENT '打卡类型:1-上下班打卡；2-外勤打卡',
                                    `extension` json DEFAULT NULL COMMENT '扩展字段:事由:reason;图片key-picKey',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                    `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
                                    `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='打卡记录';

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ATTENDANCE_RESULT_TYPE_1', NULL, 'ATTENDANCE_RESULT_TYPE', '考勤结果', 'NORMAL', '正常', 1, 1, '2022-01-24 14:44:48', NULL, 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ATTENDANCE_RESULT_TYPE_2', NULL, 'ATTENDANCE_RESULT_TYPE', '考勤结果', 'EXCEPTION', '异常', 2, 2, '2022-01-24 14:44:48', '2022-01-24 14:45:13', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ATTENDANCE_RULE_APPLY_TYPE_1', NULL, 'ATTENDANCE_RULE_APPLY_TYPE', '考勤规则关联类型', 'COMPANY', '适用全公司', 0, 0, '2022-01-20 14:54:05', '2022-02-09 14:24:35', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ATTENDANCE_RULE_APPLY_TYPE_2', NULL, 'ATTENDANCE_RULE_APPLY_TYPE', '考勤规则关联类型', 'EMPLOYEE', '适用指定人员', 1, 1, '2022-01-20 14:54:05', '2022-02-09 14:24:43', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ATTENDANCE_WORKDAY_TYPE_1', NULL, 'ATTENDANCE_WORKDAY_TYPE', '考勤规则关联工作日类型', 'LEGAL', '法定工作日', 0, 0, '2022-02-09 17:15:35', '2021-12-09 17:23:24', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ATTENDANCE_WORKDAY_TYPE_2', NULL, 'ATTENDANCE_WORKDAY_TYPE', '考勤规则关联工作日类型', 'CUSTOM', '自定义', 1, 1, '2022-02-09 17:15:35', '2022-02-09 17:16:05', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PUNCH_TYPE_1', NULL, 'PUNCH_TYPE', '打卡类型', 'NORMAL', '上下班打卡', 1, 0, '2022-01-24 15:32:49', '2022-01-24 14:16:43', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PUNCH_TYPE_2', NULL, 'PUNCH_TYPE', '打卡类型', 'FIELDWORK', '外勤打卡', 2, 0, '2022-01-24 15:32:49', '2022-01-24 10:57:25', 0);

INSERT INTO `smart-park`.`com_task`(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('1', '0 0 1 * * ?', '考勤记录数据生成', '', 0, NULL, NULL, NULL, 'park-attendance', 'attendanceTask', 'generateAttendanceRecord');

/*考勤位置默认状态为关闭*/
ALTER TABLE `smart-park`.`att_attendance_rule`
MODIFY COLUMN `status` tinyint ZEROFILL NULL DEFAULT 0 COMMENT '状态，0：禁用 1：启用' AFTER `punch_range`;