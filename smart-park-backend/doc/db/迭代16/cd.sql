ALTER TABLE `smart-park`.`con_rent_contract_bill`
    ADD COLUMN `pay_type` tinyint NULL DEFAULT 1 COMMENT '支付方式:1线下银行转账' AFTER `checkout_date`,
ADD COLUMN `company_files` varchar(800) NULL COMMENT '企业端上传凭证' AFTER `remark`;


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_BILL_FEE_TYPE_COMPANY_1', NULL, 'RENT_BILL_FEE_TYPE_COMPANY', '企业端合同费用账单类型', 'RENT', '租赁费用', 3, 1,
        '2022-05-23 10:33:16', '2022-05-23 10:44:57', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_BILL_FEE_TYPE_COMPANY_2', NULL, 'RENT_BILL_FEE_TYPE_COMPANY', '企业端合同费用账单类型', 'PROPERTY', '物业费用', 4, 2,
        '2022-05-23 10:33:16', '2022-05-23 10:44:58', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_BILL_FEE_TYPE_COMPANY_3', NULL, 'RENT_BILL_FEE_TYPE_COMPANY', '企业端合同费用账单类型', 'APARTMENT', '公寓费用', 5, 3,
        '2022-05-23 10:33:16', '2022-05-23 10:45:00', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('METER_BILL_FEE_TYPE_COMPANY_1', NULL, 'METER_BILL_FEE_TYPE_COMPANY', '企业端水电费用账单类型', 'COMPANY', '企业水电费用', 3, 1,
        '2022-05-23 10:33:16', '2022-05-23 10:45:12', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('METER_BILL_FEE_TYPE_COMPANY_2', NULL, 'METER_BILL_FEE_TYPE_COMPANY', '企业端水电费用账单类型', 'APARTMENT', '公寓水电费用', 5,
        2, '2022-05-23 10:33:16', '2022-05-23 10:45:15', 0);

CREATE TABLE `con_meter_consumption_bill`
(
    `id`                       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `bill_no`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单编号',
    `contract_billing_plan_id` varchar(64)                                                   DEFAULT NULL COMMENT '费用方案id',
    `meter_consumption_fee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租赁合同费用id',
    `contract_type`            tinyint                                                       DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
    `park_org_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织id',
    `park_org_name`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
    `company_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司id',
    `company_name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
    `account_period`           varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '所在账期',
    `charge_name`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所含收费项',
    `house_ids`                json                                                          DEFAULT NULL COMMENT '房源ids集合',
    `employee_ids`             json                                                          DEFAULT NULL COMMENT '公寓人员中房屋所关联的员工',
    `due_date`                 date                                                          DEFAULT NULL COMMENT '应收日期',
    `data_source`              tinyint                                                       DEFAULT NULL COMMENT '数据来源 1合同生成',
    `settled_month`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '结算月份',
    `settled_time`             datetime                                                      DEFAULT NULL COMMENT '结算时间',
    `receivable_amount`        decimal(12, 2)                                                DEFAULT '0.00' COMMENT '应收金额',
    `due_amount`               decimal(12, 2)                                                DEFAULT '0.00' COMMENT '待收金额',
    `received_amount`          decimal(12, 2)                                                DEFAULT '0.00' COMMENT '已收金额',
    `deduction_amount`         decimal(12, 2)                                                DEFAULT '0.00' COMMENT '减免金额（元）',
    `latest_pay_date`          date                                                          DEFAULT NULL COMMENT '最晚缴纳日期',
    `status`                   tinyint                                                       DEFAULT '0' COMMENT '收款状态：0未收，1已收',
    `checkout_date`            date                                                          DEFAULT NULL COMMENT '收款日期 ',
    `pay_type`                 tinyint                                                       DEFAULT '1' COMMENT '支付方式:1线下银行转账',
    `late_fee_status`          tinyint                                                       DEFAULT '0' COMMENT '滞纳金免除状态 1是 0否',
    `remark`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
    `company_files`            varchar(800)                                                  DEFAULT NULL COMMENT '企业端上传凭证',
    `charge_detail`            json                                                          DEFAULT NULL COMMENT '费用明细',
    `create_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `create_time`              datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '修改人',
    `update_time`              datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`                  tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='水电费用账单';

CREATE TABLE `con_meter_consumption_bill_record`
(
    `id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `contract_type`    tinyint                                                               DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
    `bill_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单id',
    `deduction_amount` decimal(12, 2)                                               NOT NULL DEFAULT '0.00' COMMENT '减免金额',
    `file_list`        json                                                                  DEFAULT NULL COMMENT '附件',
    `remark`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `create_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '修改人',
    `update_time`      datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`          tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='水电费用账单减免记录';


CREATE TABLE `con_meter_consumption_leave`
(
    `id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `type`          tinyint                                                       DEFAULT '1' COMMENT '离场结算类型：RENT_FEE_LEAVE_TYPE',
    `park_org_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
    `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
    `company_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司ID',
    `company_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
    `contract_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '合同id',
    `contract_no`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '合同编号',
    `contract_type` tinyint                                                       DEFAULT NULL COMMENT '费用类型  1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；',
    `deposit`       decimal(12, 2) unsigned DEFAULT '0.00' COMMENT '押金',
    `total_amount`  decimal(12, 2)                                                DEFAULT NULL COMMENT '实收总金额',
    `leave_date`    date                                                         NOT NULL COMMENT '结算日期',
    `checkout_date` date                                                          DEFAULT NULL COMMENT '收款日期',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
    `charge_detail` json                                                          DEFAULT NULL COMMENT '费用明细',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '修改人',
    `update_time`   datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`       tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='租赁合同交易明细';

CREATE TABLE `con_meter_consumption_trade`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `contract_type`     tinyint                                                      NOT NULL COMMENT '合同费用类型 1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同',
    `trade_no`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
    `bill_no`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单编号',
    `bill_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单id',
    `park_org_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
    `park_org_name`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
    `company_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司ID',
    `company_name`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
    `type`              tinyint                                                               DEFAULT NULL COMMENT '类型：1付款订单，2退款订单',
    `pay_type`          tinyint                                                      NOT NULL DEFAULT '1' COMMENT '支付方式:1线下银行转账',
    `receivable_amount` decimal(12, 2)                                                        DEFAULT '0.00' COMMENT '应收金额(元)',
    `received_amount`   decimal(12, 2)                                                        DEFAULT NULL COMMENT '实付金额（元）',
    `checkout_date`     date                                                                  DEFAULT NULL COMMENT '收款日期 ',
    `return_amount`     decimal(12, 2)                                                        DEFAULT '0.00' COMMENT '退款金额',
    `can_return`        tinyint(1) DEFAULT NULL COMMENT '是否可退款，0不可退款，1可退款',
    `return_reason`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '退款原因',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `remark`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `create_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '修改人',
    `update_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`           tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='水电费用交易明细';


ALTER TABLE `smart-park`.`con_rent_contract_bill`
    ADD COLUMN `checkout_type` tinyint NULL COMMENT '收款类型：1正常收款，2离场结算' AFTER `checkout_date`;
ALTER TABLE `smart-park`.`con_rent_contract_leave`
    ADD COLUMN `bill_ids` json NULL COMMENT '结算的账单id集合' AFTER `checkout_date`;

ALTER TABLE `smart-park`.`con_rent_contract_leave`
    MODIFY COLUMN `total_amount` decimal (12, 2) NULL DEFAULT NULL COMMENT '实收总金额' AFTER `deposit`;

INSERT INTO `smart-park`.`sys_business_menu` (`id`, `origin_id`, `head_org_id`, `parent_id`, `name`, `icon`, `sort`,
                                              `status`, `route`, `route_name`, `api_url`, `type`, `create_time`,
                                              `update_time`, `create_by`, `update_by`, `deleted`)
VALUES ('1527485640836616193', '1527485640446545921', '1437679155114393601', '0', '费用账单', 'consumption-fill', 0.00, 1,
        '/wisdom-park-company/expenseil-bill-mange', 'expenseilBllMange', '/park-system', 0, '2022-05-23 10:17:30',
        '2022-05-23 10:19:05', NULL, NULL, 0);
INSERT INTO `smart-park`.`sys_business_menu` (`id`, `origin_id`, `head_org_id`, `parent_id`, `name`, `icon`, `sort`,
                                              `status`, `route`, `route_name`, `api_url`, `type`, `create_time`,
                                              `update_time`, `create_by`, `update_by`, `deleted`)
VALUES ('1527486091485220866', '1527486090948349954', '1437679155114393601', '1527485640836616193', '合同费用账单', NULL,
        0.00, 1, '/wisdom-park-company/expenseil-bill-mange/contract-expenseil-bill-list', 'contractExpenseilBillList',
        '/park-system', 0, '2022-05-23 10:18:27', '2022-05-23 10:20:23', NULL, NULL, 0);
INSERT INTO `smart-park`.`sys_business_menu` (`id`, `origin_id`, `head_org_id`, `parent_id`, `name`, `icon`, `sort`,
                                              `status`, `route`, `route_name`, `api_url`, `type`, `create_time`,
                                              `update_time`, `create_by`, `update_by`, `deleted`)
VALUES ('1527486185160806401', '1527486184577798145', '1437679155114393601', '1527485640836616193', '水电费用账单', NULL,
        0.00, 1, '/wisdom-park-company/expenseil-bill-mange/utilities-expenseil-bill-list', 'utilitiesExpenseilBllList',
        '/park-system', 0, '2022-05-23 10:20:21', '2022-05-23 10:20:27', NULL, NULL, 0);

ALTER TABLE `smart-park`.`con_meter_consumption_leave`
    ADD COLUMN `house_id` varchar(64) NULL COMMENT '结算房间id' AFTER `checkout_date`;
