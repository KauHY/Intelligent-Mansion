UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'ACCESS_DEVICE_TYPE',
    `desc`        = '门禁设备类型',
    `code`        = 'APARTMENT_ACCESS_CONTROL',
    `label`       = '公寓门禁',
    `value`       = 2,
    `sort`        = 2,
    `create_time` = '2021-09-07 14:22:03',
    `update_time` = '2022-04-07 15:09:02',
    `deleted`     = 0
WHERE `id` = 'ACCESS_DEVICE_TYPE_2';


UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'CONTRACT_STATE',
    `desc`        = '合同状态',
    `code`        = 'UNDER_REVIEW_WITHDRAW',
    `label`       = '退约审核中',
    `value`       = 9,
    `sort`        = 9,
    `create_time` = '2021-12-07 14:41:14',
    `update_time` = '2022-04-11 13:56:45',
    `deleted`     = 0
WHERE `id` = 'CONTRACT_STATE_9';

-- 优化20220928
ALTER TABLE `smart-park`.dev_card MODIFY COLUMN employee_id varchar (64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '关联员工ID';
UPDATE `smart-park`.sys_dictionary
SET deleted=1
WHERE id='CARD_STATUS_2';
