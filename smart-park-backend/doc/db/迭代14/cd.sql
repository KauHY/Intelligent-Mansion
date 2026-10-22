/*
 建表
 */

CREATE TABLE `con_rent_contract_bill`
(
    `id`                       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `bill_no`                  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单编号',
    `contract_billing_plan_id` varchar(64)                                                   DEFAULT NULL COMMENT '费用方案id',
    `rent_contract_fee_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租赁合同费用id',
    `contract_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '合同id',
    `contract_no`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '合同编号',
    `park_org_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织id',
    `park_org_name`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
    `company_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司id',
    `company_name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
    `account_period`           varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '所在账期',
    `charge_name`              varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所含收费项',
    `house_name`               json                                                          DEFAULT NULL COMMENT '房源名称',
    `start_date`               date                                                          DEFAULT NULL COMMENT '计费周期 开始日期',
    `end_date`                 date                                                          DEFAULT NULL COMMENT '计费周期 结束日期',
    `due_date`                 date                                                          DEFAULT NULL COMMENT '应收日期',
    `data_source`              tinyint                                                       DEFAULT NULL COMMENT '数据来源 1合同生成',
    `receivable_amount`        decimal(12, 2)                                                DEFAULT '0.00' COMMENT '应收金额',
    `due_amount`               decimal(12, 2)                                                DEFAULT '0.00' COMMENT '待收金额',
    `received_amount`          decimal(12, 2)                                                DEFAULT '0.00' COMMENT '已收金额',
    `deduction_amount`         decimal(12, 2)                                                DEFAULT '0.00' COMMENT '减免金额（元）',
    `latest_pay_date`          date                                                          DEFAULT NULL COMMENT '最晚缴纳日期',
    `status`                   tinyint                                                       DEFAULT '0' COMMENT '收款状态：0未收，1已收',
    `checkout_date`            date                                                          DEFAULT NULL COMMENT '收款日期 ',
    `total_area`               double                                                        DEFAULT NULL COMMENT '面积（㎡）',
    `late_fee_status`          tinyint                                                       DEFAULT '0' COMMENT '滞纳金免除状态 1是 0否',
    `remark`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
    `create_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `create_time`              datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '修改人',
    `update_time`              datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`                  tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='租赁合同账单';

CREATE TABLE `con_rent_contract_bill_record`
(
    `id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='租赁合同账单减免记录';

CREATE TABLE `con_rent_contract_leave`
(
    `id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `park_org_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区组织ID',
    `park_org_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区名称',
    `company_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司ID',
    `company_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公司名称',
    `contract_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '合同id',
    `contract_no`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '合同编号',
    `deposit`       decimal(12, 2) unsigned DEFAULT '0.00' COMMENT '押金',
    `total_amount`  decimal(12, 1)                                                DEFAULT NULL COMMENT '实收总金额',
    `leave_date`    date                                                         NOT NULL COMMENT '结算日期',
    `checkout_date` date                                                          DEFAULT NULL COMMENT '收款日期',
    `remark`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '修改人',
    `update_time`   datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`       tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='租赁合同交易明细';

CREATE TABLE `con_rent_contract_trade`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
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
    `pay_time`          datetime                                                              DEFAULT NULL COMMENT '支付时间',
    `can_return`        tinyint(1) DEFAULT NULL COMMENT '是否可退款，0不可退款，1可退款',
    `return_reason`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '退款原因',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `remark`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `create_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '修改人',
    `update_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted`           tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='租赁合同交易明细';

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_BILL_STATUS_1', NULL, 'RENT_BILL_STATUS', '租赁账单收款状态 ', 'UNRECEIVED', '未收', 0, 1, '2022-04-24 11:21:29',
        '2022-04-24 11:23:37', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_BILL_STATUS_2', NULL, 'RENT_BILL_STATUS', '租赁账单收款状态 ', 'RECEIVED', '已收', 1, 2, '2022-04-24 11:21:29',
        '2022-04-24 11:23:28', 0);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_TRADE_TYPE_1', NULL, 'RENT_TRADE_TYPE', '租赁费用交易明细类型', 'PAY', '付款订单', 1, 1, '2022-04-24 16:25:44',
        '2022-04-24 16:27:21', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_TRADE_TYPE_2', NULL, 'RENT_TRADE_TYPE', '租赁费用交易明细类型', 'RETURN', '退款订单', 2, 2, '2022-04-24 16:25:44',
        '2022-04-24 16:27:31', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_FEE_PAY_TYPE_1', NULL, 'RENT_FEE_PAY_TYPE', '支付方式', 'OFFLINE_BANK', '线下银行转账', 1, 1, '2022-04-24 16:30:07',
        '2022-04-24 16:30:16', 0);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('RENT_FEE_LEAVE_TYPE_1', NULL, 'RENT_FEE_LEAVE_TYPE', '离场结算计费类型', 'LEAVE', '离场结算', 1, 1, '2022-04-25 10:14:37',
        NULL, 0);

ALTER TABLE `smart-park`.`com_number_seq`
    MODIFY COLUMN `date_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON
UPDATE CURRENT_TIMESTAMP AFTER `number_name`;

INSERT INTO `smart-park`.`com_number_seq` (`number_name`, `date_time`, `value`)
VALUES ('BN', '2022-04-27 10:26:48', 1000);

INSERT INTO `smart-park`.`com_number_seq` (`number_name`, `date_time`, `value`)
VALUES ('ON', '2022-04-27 10:26:44', 1000);




