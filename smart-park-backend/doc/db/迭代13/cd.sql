ALTER TABLE `smart-park`.`bus_business_chance`
    MODIFY COLUMN `expected_price` double NULL DEFAULT NULL COMMENT '期望价格（元/㎡/月）' AFTER `quantity_number`;


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_MEDIUM_1', NULL, 'CARD_MEDIUM', '卡介质', 'IC_OR_ID', 'IC/ID卡', 1, 1, '2022-04-07 15:21:53', NULL, 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_STATUS_1', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_1', '已绑定', 1, 1, '2022-04-07 16:15:19',
        '2022-04-07 16:16:57', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_STATUS_2', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_2', '已挂失', 2, 2, '2022-04-07 16:15:27',
        '2022-04-07 16:17:01', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_STATUS_3', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_3', '已注销', 3, 3, '2022-04-07 16:15:27',
        '2022-04-07 16:17:05', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_STATUS_4', NULL, 'CARD_STATUS', '卡片状态', 'CARD_STATUS_4', '已解绑', 4, 4, '2022-04-07 16:15:27',
        '2022-04-07 16:17:09', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_TYPE_1', NULL, 'CARD_TYPE', '卡类型', 'COMMON_CARD', '普通卡', 1, 1, '2022-04-07 15:22:52',
        '2022-04-07 16:18:34', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_TYPE_2', NULL, 'CARD_TYPE', '卡类型', 'ADMIN_CARD', '管理员卡', 2, 2, '2022-04-07 15:22:52',
        '2022-04-07 16:19:04', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CARD_TYPE_3', NULL, 'CARD_TYPE', '卡类型', 'OTHER', '其他', 3, 3, '2022-04-07 15:22:52', '2022-04-07 16:19:14', 0);

/*
 楼层属性字典值修改
 */
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'FLOOR_TYPE',
    `desc`        = '楼层属性',
    `code`        = 'FLOOR_TYPE_1',
    `label`       = '低层',
    `value`       = 1,
    `sort`        = 2,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-01-04 10:58:27',
    `deleted`     = 0
WHERE `id` = 'FLOOR_TYPE_1';

/**
  卡片建表
 */
CREATE TABLE `dev_card`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
    `card_no`           varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '卡号',
    `card_medium`       int                                                          NOT NULL COMMENT '卡片介质:CARD_MEDIUM',
    `card_type`         int                                                                   DEFAULT NULL COMMENT '卡片类型:CARD_TYPE：1普通卡、2管理员卡、3其他',
    `park_org_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联园区ID',
    `company_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属企业ID',
    `employee_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工ID',
    `effect_start_date` date                                                         NOT NULL COMMENT '有效期开始日期',
    `effect_end_date`   date                                                         NOT NULL COMMENT '有效期结束日期',
    `remark`            varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `status`            tinyint                                                      NOT NULL DEFAULT '1' COMMENT '状态：CARD_STATUS：1已绑定、2已挂失、3已注销、4已解绑',
    `deleted`           tinyint(1) DEFAULT '0' COMMENT '删除状态',
    `create_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建者',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '修改者',
    `update_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='卡片管理';

/*
 公寓人员管理建表
 */

CREATE TABLE `vis_apartment_person`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `park_org_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '园区组织Id',
    `company_id`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属企业Id',
    `employee_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工Id',
    `id_card_number`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '身份证号',
    `house_ids`         json                                                                  DEFAULT NULL COMMENT '关联房屋id集合',
    `device_ids`        json                                                                  DEFAULT NULL COMMENT '公寓门禁设备id集合',
    `effect_start_date` date                                                         NOT NULL COMMENT '生效开始日期',
    `effect_end_date`   date                                                         NOT NULL COMMENT '生效结束日期',
    `remark`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `status`            tinyint                                                      NOT NULL DEFAULT '1' COMMENT '状态，0：禁用 1：启用',
    `expire`            tinyint                                                      NOT NULL DEFAULT '0' COMMENT '已过期，0：否 1：是',
    `deleted`           tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `create_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '修改人',
    `update_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公寓人员管理';
