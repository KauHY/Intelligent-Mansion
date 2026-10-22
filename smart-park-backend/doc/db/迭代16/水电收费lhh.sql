
/**
  字典表——租赁费用数据来源
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('RENT_DATA_SOURCE_2', NULL, 'RENT_DATA_SOURCE', '租赁费用数据来源', 'CON_METER', '抄表计费', 2, 2);

/**
  水电费定时任务
 */
INSERT INTO com_task(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('meterFeeCreate', '0 0 12 1 * ?', '水电费用自动生成', '每个月1号12点', 0, NULL, NULL, NULL, 'park_contract', 'meterConsumptionTask', 'meterFeeCreate');
INSERT INTO com_task(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('meterFeeUpdate', '0 10 0 * * ?', '自动未出费用转已出和账单', NULL, 0, NULL, NULL, NULL, 'park_contract', 'meterConsumptionTask', 'meterFeeUpdate');
INSERT INTO com_task(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('meterLateCreate', '0 20 0 * * ?', '自动生成水电滞纳金费用', NULL, 0, NULL, NULL, NULL, 'park_contract', 'meterConsumptionTask', 'meterLateCreate');


CREATE TABLE `con_meter_consumption_fee` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `contract_type` tinyint DEFAULT NULL COMMENT '费用类型 3-租赁合同；4-物业合同；5-公寓合同；',
  `contract_billing_plan_id` varchar(64) DEFAULT NULL COMMENT '费用方案id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区Id',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区名',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收费项名称',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业名称',
  `house_ids` json DEFAULT NULL COMMENT '房源ids集合',
  `due_date` date DEFAULT NULL COMMENT '应收日期',
  `latest_pay_date` date DEFAULT NULL COMMENT '最晚缴纳日期',
  `data_source` tinyint DEFAULT NULL COMMENT '数据来源 1合同生成 2抄表计费',
  `settled_month` varchar(64) DEFAULT NULL COMMENT '结算月份',
  `settled_time` datetime DEFAULT NULL COMMENT '结算时间',
  `receivable_amount` decimal(12,2) DEFAULT '0.00' COMMENT '应收金额',
  `due_amount` decimal(12,2) DEFAULT '0.00' COMMENT '待收金额',
  `received_amount` decimal(12,2) DEFAULT '0.00' COMMENT '已收金额',
  `deduction_amount` decimal(12,2) DEFAULT '0.00' COMMENT '减免金额',
  `billing_status` tinyint unsigned DEFAULT '1' COMMENT '出账状态 1待出账 2已出账 3已废除',
  `fee_status` tinyint DEFAULT '0' COMMENT '费用状态 0未收 1已收',
  `charge_detail` json DEFAULT NULL COMMENT '费用明细',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='水电费用';


CREATE TABLE `con_meter_consumption_late_fee` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rent_bill_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '租赁账单id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区Id',
  `contract_type` tinyint DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业id',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '入驻企业名称',
  `charge_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '收费项名称',
  `bill_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '账单编号',
  `settled_month` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '结算月份',
  `due_amount` decimal(12,2) DEFAULT NULL COMMENT '逾期金额',
  `due_days` int DEFAULT NULL COMMENT '逾期天数',
  `receive_fine` decimal(12,2) DEFAULT '0.00' COMMENT '待收滞纳金',
  `receivable_fine` decimal(12,2) DEFAULT '0.00' COMMENT '应收滞纳金',
  `overdue_fine` decimal(12,2) DEFAULT '0.00' COMMENT '已收滞纳金',
  `reduce_fine` decimal(12,2) DEFAULT '0.00' COMMENT '减免滞纳金',
  `status` tinyint DEFAULT NULL COMMENT '状态 1计算中 2已缴清 3已免除',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经办人',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='水电滞纳金费用';