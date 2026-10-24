/**
  字典表——出入库方式
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_TYPE_1', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'MANUAL_WAREHOUSE', '手动入库', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_TYPE_2', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'RETURN_WAREHOUSE', '归还入库', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_TYPE_3', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'IMPORT_WAREHOUSE', '导入入库', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_TYPE_4', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'MANUAL_DELIVERY', '手动出库', 4, 4);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_TYPE_5', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'RECEIPT_DELIVERY', '领用出库', 5, 5);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_TYPE_6', NULL, 'PRO_STOCK_TYPE', '出入库方式', 'MODIFY_INVENTORY', '修改库存', 6, 6);

/**
  字典表--出入库操作类型
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_MODE_1', NULL, 'PRO_STOCK_MODE', '操作类型', 'STOCK_IN', '入库', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_MODE_2', NULL, 'PRO_STOCK_MODE', '操作类型', 'STOCK_OUT', '出库入库', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_STOCK_MODE_3', NULL, 'PRO_STOCK_MODE', '操作类型', 'MODIFY_INVENTORY', '修改库存', 3, 3);

/**
  字典表--领用审核状态
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_AUDIT_1', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'NOT_RECEIVE', '领用待审核', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_AUDIT_2', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RECEIVE_PASS', '领用审核通过', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_AUDIT_3', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RECEIVE_REJECT', '领用审核不通过', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_AUDIT_4', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'NOT_RETURN', '归还待审核', 4, 4);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_AUDIT_5', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RETURN_PASS', '归还审核通过', 5, 5);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_AUDIT_6', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'RETURN_REJECT', '归还审核通过', 6, 6);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_AUDIT_7', NULL, 'PRO_RECEIVE_AUDIT', '领用审核状态', 'CANCEL', '已取消', 7, 7);

/**
  字典表--领用出入库状态
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_STOCK_1', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'NOT_OUT', '未出库', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_STOCK_2', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'OUT_PASS', '已出库', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_STOCK_3', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'PART_IN', '部分已入库', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_RECEIVE_STOCK_4', NULL, 'PRO_RECEIVE_STOCK', '领用出入库状态', 'ALL_IN', '全部已入库', 4, 4);

/**
    字典表-物品类别
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_GOODS_TYPE_1', NULL, 'PRO_GOODS_TYPE', '物品类别', 'MAINTENANCE_CONSUMABLES', '维修耗材', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_GOODS_TYPE_2', NULL, 'PRO_GOODS_TYPE', '物品类别', 'OFFICE_SUPPLIES', '办公用品', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_GOODS_TYPE_3', NULL, 'PRO_GOODS_TYPE', '物品类别', 'CLEANING_SUPPLIES', '清洁用品', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_GOODS_TYPE_4', NULL, 'PRO_GOODS_TYPE', '物品类别', 'TOOLS_SUPPLIES', '工具用品', 4, 4);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_GOODS_TYPE_5', NULL, 'PRO_GOODS_TYPE', '物品类别', 'ELECTRICAL_EQUIPMENT', '电器设备', 5, 5);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('PRO_GOODS_TYPE_6', NULL, 'PRO_GOODS_TYPE', '物品类别', 'OTHERS', '其他', 6, 6);

/**
  自增序号表-增加序号
 */
INSERT INTO `smart-park`.`com_number_seq`(`number_name`, `value`)
VALUES ('WP', 1000);

INSERT INTO `smart-park`.`com_number_seq`(`number_name`, `value`)
VALUES ('LY', 1000);

INSERT INTO `smart-park`.`com_number_seq`(`number_name`, `value`)
VALUES ('RK', 1000);

INSERT INTO `smart-park`.`com_number_seq`(`number_name`, `value`)
VALUES ('CK', 1000);

INSERT INTO `smart-park`.`com_number_seq`(`number_name`, `value`)
VALUES ('XG', 1000);

/*
 定时任务表-增加月度盘点定时任务
 */
INSERT INTO `smart-park`.`com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`,
                                     `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`,
                                     `method_name`)
VALUES ('autoInventoryStock', '0 20 0 * * ?', '盘点管理-月度盘点', '每天凌晨0:20启动', 0, NULL, NULL, NULL, 'park-customer',
        'inventoryStockAutoTask', 'autoInventoryStock');

CREATE TABLE `cus_pro_goods_manager`
(
    `id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `goods_type`  tinyint                                                       DEFAULT NULL COMMENT '物品类别',
    `goods_no`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '物品编码',
    `goods_name`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '物品名称',
    `unit_price`  decimal(12, 2)                                                DEFAULT NULL COMMENT '入库单价(元)',
    `unit`        varchar(50)                                                   DEFAULT NULL COMMENT '单位',
    `brand`       varchar(200)                                                  DEFAULT NULL COMMENT '品牌',
    `model`       varchar(200)                                                  DEFAULT NULL COMMENT '型号',
    `min_stock`   int                                                           DEFAULT NULL COMMENT '库存下限预警值',
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
    `create_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                    DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) COMMENT ='物品管理';

CREATE TABLE `cus_pro_inventory_manager`
(
    `id`                       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `goods_id`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品id',
    `month`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '盘点月份',
    `last_balance`             varchar(30)                                                           DEFAULT NULL COMMENT '上月结余数量',
    `last_balance_total`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '上月结余总额（元）',
    `current_balance`          int                                                                   DEFAULT NULL COMMENT '本月库存数',
    `current_balance_total`    decimal(30, 2)                                                        DEFAULT NULL COMMENT '本月库存总额（元）',
    `current_stock_in_number`  int                                                                   DEFAULT '0' COMMENT '本月入库数量',
    `current_stock_out_number` int                                                                   DEFAULT '0' COMMENT '本月出库数量',
    `inventory_time`           datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '盘点时间',
    `remark`                   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `create_time`              datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `update_time`              datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`                  tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='盘点管理';

CREATE TABLE `cus_pro_receive_flow` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `receive_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '领用id',
  `audit_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核人',
  `audit_state` tinyint NOT NULL COMMENT '审核状态 1领用待审核 2领用审核通过 3领用审核不通过 4归还待审核 5归还审核通过 6归还审核不通过 7已取消',
  `audit_msg` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核意见',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `stock_no` varchar(32) DEFAULT NULL COMMENT '出入库单号',
  `need_return` tinyint DEFAULT NULL COMMENT '是否需要归还 1是 0否',
  `return_num_current` int DEFAULT NULL COMMENT '本次归还数量',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`)
) COMMENT ='物品领用流程';

CREATE TABLE `cus_pro_receive_manager` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区id',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '领用人id',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公司id',
  `stock_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品库存id',
  `apply_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '申请单号',
  `audit_state` tinyint NOT NULL COMMENT '审核状态',
  `stock_state` tinyint DEFAULT NULL COMMENT '出入库状态',
  `receive_num` int NOT NULL COMMENT '领用数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '领用备注',
  `need_return` tinyint DEFAULT NULL COMMENT '是否需要归还 1是 0否',
  `return_num_total` int DEFAULT '0' COMMENT '已归还数量',
  `return_num_current` int DEFAULT '0' COMMENT '本次归还数量',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `stock_in_time` datetime DEFAULT NULL COMMENT '入库时间',
  `stock_out_time` datetime DEFAULT NULL COMMENT '出库时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`)
) COMMENT ='物品领用管理';

CREATE TABLE `cus_pro_receive_permission` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
  `company_id` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公司id',
  `employee_id` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '员工id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
  PRIMARY KEY (`id`)
) COMMENT ='领用权限设置';

CREATE TABLE `cus_pro_stock_manager`
(
    `id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `goods_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品ID',
    `stock_num`       int                                                                   DEFAULT '0' COMMENT '库存数量',
    `average_price`   decimal(12, 2)                                                        DEFAULT '0.00' COMMENT '入库均价（元）',
    `warehouse_id`    varchar(64)                                                           DEFAULT NULL COMMENT '所属仓库ID(暂不实现 统一展示‘公寓仓库’)',
    `total_stock_num` int                                                                   DEFAULT '0' COMMENT '累计入库数量（隐藏）',
    `total_price`     decimal(30, 2)                                                        DEFAULT '0.00' COMMENT '累计入库总价（隐藏）',
    `remark`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `create_time`     datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `update_time`     datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`         tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) COMMENT ='库存管理';

CREATE TABLE `cus_pro_stock_record`
(
    `id`                    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `goods_id`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物品ID',
    `stock_id`              varchar(64)                                                           DEFAULT NULL COMMENT '库存id',
    `record_no`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出入库单号',
    `stock_in_num`          int                                                                   DEFAULT NULL COMMENT '入库数量',
    `stock_in_unit_price`   decimal(12, 2)                                                        DEFAULT NULL COMMENT '入库单价（元）',
    `current_num`           int                                                                   DEFAULT NULL COMMENT '入库操作后库存',
    `current_average_price` decimal(12, 2)                                                        DEFAULT NULL COMMENT '入库操作后均价（元）',
    `stock_type`            tinyint                                                               DEFAULT NULL COMMENT '出入库方式（业务使用）：1.手动入库、2.归还入库、3.导入入库、4.手动出库、5.领用出库、6.修改库存',
    `remark`                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `stock_mode`            tinyint                                                      NOT NULL DEFAULT '0' COMMENT '出入库方式(统计使用)：1.入库、2.出库、3.修改库存',
    `create_time`           datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `update_time`           datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`               tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) COMMENT ='库存出入库记录';