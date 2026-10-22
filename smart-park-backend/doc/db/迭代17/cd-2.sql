ALTER TABLE `smart-park`.`sys_company`
    ADD COLUMN `status` tinyint NOT NULL DEFAULT 1 COMMENT '企业状态:0-已禁用,1-正常' AFTER `source`;


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('FEE_BILL_TYPE_0', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'ALL_BILL', '全部', 0, 0, '2022-06-16 09:41:28',
        '2022-06-16 09:44:54', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('FEE_BILL_TYPE_1', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'COMPANY_METER_BILL', '企业水电费用', 1, 4,
        '2022-06-16 09:41:04', '2022-06-16 09:45:58', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('FEE_BILL_TYPE_2', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'APARTMENT_METER_BILL', '公寓水电费用', 2, 5,
        '2022-06-16 09:41:28', '2022-06-16 09:46:01', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('FEE_BILL_TYPE_3', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'RENTAL_BILL', '租赁费用', 3, 1, '2022-06-16 09:41:28',
        '2022-06-16 09:45:50', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('FEE_BILL_TYPE_4', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'PROPERTY_BILL', '物业费用', 4, 2, '2022-06-16 09:41:28',
        '2022-06-16 09:45:52', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('FEE_BILL_TYPE_5', NULL, 'FEE_BILL_TYPE', '费用统计账单类型', 'APARTMENT_BILL', '公寓租金', 5, 3, '2022-06-16 09:41:28',
        '2022-06-16 09:45:55', 0);

UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'COMPANY_BIND_CLIENT_TYPE',
    `desc`        = '入驻企业绑定客户端类型',
    `code`        = 'APPID',
    `label`       = '小程序',
    `value`       = 1,
    `sort`        = 1,
    `create_time` = '2021-09-10 11:05:11',
    `update_time` = '2022-06-17 10:54:30',
    `deleted`     = 0
WHERE `id` = 'COMPANY_BIND_CLIENT_TYPE_2';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'COMPANY_BIND_CLIENT_TYPE',
    `desc`        = '入驻企业绑定客户端类型',
    `code`        = 'H5',
    `label`       = 'h5链接',
    `value`       = 2,
    `sort`        = 2,
    `create_time` = '2021-09-10 11:05:11',
    `update_time` = '2022-06-17 10:54:13',
    `deleted`     = 1
WHERE `id` = 'COMPANY_BIND_CLIENT_TYPE_3';

ALTER TABLE `smart-park`.`sys_menu` MODIFY COLUMN `route` VARCHAR ( 1000 ) CHARACTER
    SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由' AFTER `enabled`;

ALTER TABLE `smart-park`.`sys_organization`
    MODIFY COLUMN `description` varchar (256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述' AFTER `address`;

UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'ADVERTISEMENT_JUMP_TYPE',
    `desc`        = '广告跳转类型',
    `code`        = 'NOTHING',
    `label`       = '无',
    `value`       = 0,
    `sort`        = 5,
    `create_time` = '2022-01-04 10:11:24',
    `update_time` = '2022-05-31 14:06:27',
    `deleted`     = 0
WHERE `id` = 'ADVERTISEMENT_JUMP_TYPE_1';

UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'ADVERTISEMENT_JUMP_TYPE',
    `desc`        = '广告跳转类型',
    `code`        = 'NOTICE',
    `label`       = '资讯',
    `value`       = 3,
    `sort`        = 2,
    `create_time` = '2022-01-04 10:11:24',
    `update_time` = '2022-06-21 14:00:22',
    `deleted`     = 1
WHERE `id` = 'ADVERTISEMENT_JUMP_TYPE_4';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'ADVERTISEMENT_JUMP_TYPE',
    `desc`        = '广告跳转类型',
    `code`        = 'INFORMATION',
    `label`       = '公告',
    `value`       = 4,
    `sort`        = 1,
    `create_time` = '2022-01-04 10:11:24',
    `update_time` = '2022-06-21 14:01:55',
    `deleted`     = 1
WHERE `id` = 'ADVERTISEMENT_JUMP_TYPE_5';
