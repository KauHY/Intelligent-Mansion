ALTER TABLE `smart-park`.`cus_information`
    MODIFY COLUMN `views` int NULL DEFAULT 0 COMMENT '浏览量' AFTER `put_into_type`;
ALTER TABLE `smart-park`.`cus_information`
DROP
COLUMN `popuped`;


UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'INFOMATION_TYPE',
    `desc`        = '资讯具体类型',
    `code`        = 'NEWS',
    `label`       = '新闻资讯',
    `value`       = 1,
    `sort`        = 1,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:52:07',
    `deleted`     = 1
WHERE `id` = 'InfomationTypeItem-1';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'INFOMATION_TYPE',
    `desc`        = '资讯具体类型',
    `code`        = 'PREFERENTIAL_POLICIES',
    `label`       = '优惠政策',
    `value`       = 2,
    `sort`        = 2,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:52:12',
    `deleted`     = 1
WHERE `id` = 'InfomationTypeItem-2';


UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'PUT_INTO_TYPE',
    `desc`        = '投放的小程序类型',
    `code`        = 'PUT_INTO_WX_C',
    `label`       = 'C端',
    `value`       = 1,
    `sort`        = 1,
    `create_time` = '2022-01-04 10:33:46',
    `update_time` = '2022-03-10 13:59:07',
    `deleted`     = 1
WHERE `id` = 'INFORMATION_PUTINTOTYPE_1';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'PUT_INTO_TYPE',
    `desc`        = '投放的小程序类型',
    `code`        = 'PUT_INTO_WX_B',
    `label`       = '运营端',
    `value`       = 2,
    `sort`        = 2,
    `create_time` = '2022-01-04 10:33:46',
    `update_time` = '2022-03-10 13:59:12',
    `deleted`     = 1
WHERE `id` = 'INFORMATION_PUTINTOTYPE_2';


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INFOMATION_TYPE_1', NULL, 'INFOMATION_TYPE', '资讯具体类型', 'NEWS', '新闻资讯', 1, 1, '2021-09-30 10:22:03',
        '2022-03-10 13:52:07', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INFOMATION_TYPE_2', NULL, 'INFOMATION_TYPE', '资讯具体类型', 'PREFERENTIAL_POLICIES', '优惠政策', 2, 2,
        '2021-09-30 10:22:03', '2022-03-10 13:52:12', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('PUTINTOTYPE_1', NULL, 'PUT_INTO_TYPE', '投放的小程序类型', 'PUT_INTO_WX_C', 'C端', 1, 1, '2022-01-04 10:33:46',
        '2022-03-14 17:19:29', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('PUTINTOTYPE_2', NULL, 'PUT_INTO_TYPE', '投放的小程序类型', 'PUT_INTO_WX_B', '运营端', 2, 2, '2022-01-04 10:33:46',
        '2022-03-14 17:19:35', 0);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INFOMATION_STATUS_1', NULL, 'INFOMATION_STATUS', '资讯状态', 'UN_EFFECT', '待生效', 0, 1, '2021-09-30 10:22:03',
        '2022-03-14 16:28:00', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INFOMATION_STATUS_2', NULL, 'INFOMATION_STATUS', '资讯状态', 'EFFECTING', '已生效', 1, 2, '2021-09-30 10:22:03',
        '2022-03-14 16:28:04', 0);
