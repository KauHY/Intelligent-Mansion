ALTER TABLE `smart-park`.`fee_billing_scheme`
ADD COLUMN `late_type` tinyint DEFAULT NULL COMMENT '滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积' AFTER `days_for_delay`,
ADD COLUMN `late_day_amount` decimal(12,2) DEFAULT NULL COMMENT '每日滞纳金' AFTER `late_rate`,
ADD COLUMN `month_by` int DEFAULT NULL COMMENT '第几月' AFTER `cumulative_cycle`;

ALTER TABLE `smart-park`.`con_contract_billing_plan`
ADD COLUMN `late_type` tinyint DEFAULT NULL COMMENT '滞纳金规则 1逾期金额*逾期天数*滞纳率 2每日滞纳金*逾期天数*建筑面积 3每日滞纳金*逾期天数*套内面积' AFTER `days_for_delay`,
ADD COLUMN `late_day_amount` decimal(12,2) DEFAULT NULL COMMENT '每日滞纳金' AFTER `late_rate`,
ADD COLUMN `month_by` int DEFAULT NULL COMMENT '第几月' AFTER `cumulative_cycle`;


/**
  字典表——租赁费用数据来源
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_DATA_SOURCE_1', NULL, 'RENT_DATA_SOURCE', '租赁费用数据来源', 'CON_CREATE', '合同生成', 1, 1);

INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_DATA_SOURCE_2', NULL, 'RENT_DATA_SOURCE', '租赁费用数据来源', 'CON_METER', '抄表计费', 2, 2);

/**
  字典表——租赁费用出账状态
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_FEE_OUT_1', NULL, 'RENT_FEE_OUT_STATUS', '租赁费用出账状态', 'BINDING', '待出账', 1, 1);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_FEE_OUT_2', NULL, 'RENT_FEE_OUT_STATUS', '租赁费用出账状态', 'LOST', '已出账', 2, 2);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_FEE_OUT_3', NULL, 'RENT_FEE_OUT_STATUS', '租赁费用出账状态', 'CANCELED', '已废除', 3, 3);

/**
  字典表——租赁费用状态
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_FEE_BILL_1', NULL, 'RENT_FEE_BILL_STATUS', '租赁费用状态', 'UNRECEIVED', '未收', 0, 0);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_FEE_BILL_2', NULL, 'RENT_FEE_BILL_STATUS', '租赁费用状态', 'RECEIVED', '已收', 1, 1);

/**
  字典表——租赁费用押金状态
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_CASH_STATUS_1', NULL, 'RENT_CASH_STATUS', '租赁费用押金状态', 'UNPAID', '未缴清', 1, 1);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_CASH_STATUS_2', NULL, 'RENT_CASH_STATUS', '租赁费用押金状态', 'PAID_UP', '已缴清', 2, 2);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_CASH_STATUS_3', NULL, 'RENT_CASH_STATUS', '租赁费用押金状态', 'CLEARED', '已结清', 3, 3);

/**
  字典表——租赁费用滞纳金状态
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_LATE_STATUS_1', NULL, 'RENT_LATE_STATUS', '租赁费用滞纳金状态', 'COMPUTING', '计算中', 1, 1);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_LATE_STATUS_2', NULL, 'RENT_LATE_STATUS', '租赁费用滞纳金状态', 'PAID_UP', '已缴清', 2, 2);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_LATE_STATUS_3', NULL, 'RENT_LATE_STATUS', '租赁费用滞纳金状态', 'CANCELED', '已免除', 3, 3);

/**
  租赁费用账单定时任务
 */
INSERT INTO `smart-park`.`com_task`(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('rentFeeUpdate', '0 10 0 * * ?', '已出租赁费用和账单生成', '每天凌晨0:10启动', 0, NULL, '', 1, 'park-contract', 'RentContractOutTask', 'rentFeeUpdate');

/**
  字典表——滞纳金规则
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_LATE_RULE_1', NULL, 'RENT_LATE_RULE', '滞纳金规则', 'ONE', '逾期金额*逾期天数*滞纳率', 1, 1);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_LATE_RULE_2', NULL, 'RENT_LATE_RULE', '滞纳金规则', 'TWO', '每日滞纳金*逾期天数*建筑面积', 2, 2);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_LATE_RULE_3', NULL, 'RENT_LATE_RULE', '滞纳金规则', 'THREE', '每日滞纳金*逾期天数*套内面积', 3, 3);


CREATE TABLE `con_rent_contract_fee` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contract_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '合同id',
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '合同编号',
  `contract_type` tinyint DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `contract_billing_plan_id` varchar(64) DEFAULT NULL COMMENT '费用方案id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区Id',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区名',
  `fee_type` tinyint DEFAULT NULL COMMENT '费用类型 1房屋租金 7履约保证金',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收费项名称',
  `total_area` double DEFAULT NULL COMMENT '建筑面积（㎡）',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业名称',
  `house_name` json DEFAULT NULL COMMENT '房源名称',
  `start_date` date DEFAULT NULL COMMENT '计费周期 开始日期',
  `end_date` date DEFAULT NULL COMMENT '计费周期 结束日期',
  `due_date` date DEFAULT NULL COMMENT '应收日期',
  `account_period` varchar(50) DEFAULT NULL COMMENT '所在账期',
  `latest_pay_date` date DEFAULT NULL COMMENT '最晚缴纳日期',
  `data_source` tinyint DEFAULT NULL COMMENT '数据来源 1合同生成',
  `receivable_amount` decimal(12,2) DEFAULT '0.00' COMMENT '应收金额',
  `due_amount` decimal(12,2) DEFAULT '0.00' COMMENT '待收金额',
  `received_amount` decimal(12,2) DEFAULT '0.00' COMMENT '已收金额',
  `deduction_amount` decimal(12,2) DEFAULT '0.00' COMMENT '减免金额',
  `billing_status` tinyint unsigned DEFAULT '1' COMMENT '出账状态 1待出账 2已出账 3已废除',
  `fee_status` tinyint DEFAULT '0' COMMENT '费用状态 0未收 1已收',
  `cash_status` tinyint DEFAULT '1' COMMENT '押金状态 1未缴清 2已缴清 3已结清',
  `deduction` tinyint DEFAULT NULL COMMENT '是否抵扣 1是 0否',
  `deduction_msg` json DEFAULT NULL COMMENT '抵扣内容',
  `offset_amount` decimal(12,2) DEFAULT '0.00' COMMENT '已抵扣金额',
  `refunded_amount` decimal(12,2) DEFAULT '0.00' COMMENT '已退款金额',
  `lease_date` date DEFAULT NULL COMMENT '退租日期',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经办人',
  `handle_time` datetime DEFAULT NULL COMMENT '办理时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='租赁合同费用';

CREATE TABLE `con_rent_late_fee` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rent_bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租赁账单id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区Id',
  `contract_type` tinyint DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业名称',
  `fee_type` tinyint DEFAULT NULL COMMENT '费用类型 1房屋租金 7履约保证金',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收费项名称',
  `total_area` double DEFAULT NULL COMMENT '建筑面积（㎡）',
  `house_name` json DEFAULT NULL COMMENT '房源名称',
  `bill_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '账单编号',
  `account_period` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所在账期',
  `due_amount` decimal(12,2) DEFAULT NULL COMMENT '逾期金额',
  `due_days` int DEFAULT NULL COMMENT '逾期天数',
  `receive_fine` decimal(12,2) DEFAULT '0.00' COMMENT '待收滞纳金',
  `receivable_fine` decimal(12,2) DEFAULT '0.00' COMMENT '应收滞纳金',
  `overdue_fine` decimal(12,2) DEFAULT '0.00' COMMENT '已收滞纳金',
  `reduce_fine` decimal(12,2) DEFAULT '0.00' COMMENT '减免滞纳金',
  `status` tinyint DEFAULT NULL COMMENT '状态 1计算中 2已缴清 3已免除',
  `deduction` tinyint(1) DEFAULT NULL COMMENT '是否抵扣 1是 0否',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经办人',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='租赁滞纳金费用';