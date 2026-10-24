/**
  公告建表
 */
CREATE TABLE `cus_notice`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL,
    `title`             varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `type`              tinyint                                                        NOT NULL COMMENT '公告类型',
    `park_org_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '园区组织Id',
    `without_limit`     tinyint(1) NOT NULL COMMENT '推送范围：0 指定公司/人员, 1 不限',
    `put_into_type`     tinyint                                                      DEFAULT NULL COMMENT '投放类型：1 C端，2 运营端，3 C端 运营端',
    `popuped`           tinyint unsigned NOT NULL DEFAULT '0' COMMENT '是否弹窗提醒',
    `effect_time_start` datetime                                                       NOT NULL COMMENT '生效开始时间',
    `effect_time_end`   datetime                                                     DEFAULT NULL COMMENT '生效开始时间',
    `summary`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '摘要',
    `content`           varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
    `status`            tinyint                                                      DEFAULT NULL,
    `files`             json                                                         DEFAULT NULL COMMENT '附件',
    `views`             int                                                          DEFAULT '0' COMMENT '浏览量',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
    `deleted`           tinyint(1) NOT NULL DEFAULT '0' COMMENT '0,未删除，1删除',
    `create_time`       datetime                                                     DEFAULT CURRENT_TIMESTAMP,
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
    `update_time`       datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='公告信息表';


/**
  公告权限表（推送范围）
 */

CREATE TABLE `cus_notice_permission`
(
    `id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `notice_id`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告id',
    `relation_type` tinyint                                                      NOT NULL COMMENT '关联类型 1公司 2员工',
    `company_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联公司id',
    `employee_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联员工id',
    `create_time`   datetime                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `update_time`   datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
    `deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公告推送范围关联';



UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'MAINTENANCE_NOTCIFICATION',
    `label`       = '维修通知',
    `value`       = 1,
    `sort`        = 1,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:53:59',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-1';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'NO_WATER_POWER',
    `label`       = '停水停电',
    `value`       = 2,
    `sort`        = 2,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:54:06',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-2';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'SECURITY_REMINDER',
    `label`       = '安全提醒',
    `value`       = 3,
    `sort`        = 3,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:54:09',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-3';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'PUBLIC_SERVICE',
    `label`       = '公益服务',
    `value`       = 4,
    `sort`        = 4,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:54:11',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-4';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'WARM_PROMPT',
    `label`       = '温馨提示',
    `value`       = 5,
    `sort`        = 5,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:54:13',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-5';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'EMERGENCY_BROADCAST',
    `label`       = '应急广播',
    `value`       = 6,
    `sort`        = 6,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:54:15',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-6';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'CHARAGE_NOTICE',
    `label`       = '收费通知',
    `value`       = 7,
    `sort`        = 7,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:54:17',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-7';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'NOTICE_TYPE',
    `desc`        = '公告具体类型',
    `code`        = 'OTHER',
    `label`       = '其他',
    `value`       = 8,
    `sort`        = 8,
    `create_time` = '2021-09-30 10:22:03',
    `update_time` = '2022-03-10 13:54:28',
    `deleted`     = 1
WHERE `id` = 'NoticeTypeItem-8';

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_STATUS_1', NULL, 'NOTICE_STATUS', '公告状态', 'UN_EFFECT', '待生效', 0, 1, '2021-09-30 10:22:03',
        '2022-03-14 17:01:54', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_STATUS_2', NULL, 'NOTICE_STATUS', '公告状态', 'EFFECTING', '生效中', 1, 2, '2021-09-30 10:22:03',
        '2022-03-14 17:02:24', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_STATUS_3', NULL, 'NOTICE_STATUS', '公告状态', 'OUT_OF_DATE', '已过期', 2, 3, '2021-09-30 10:22:03',
        '2022-03-14 17:02:29', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-1', NULL, 'NOTICE_TYPE', '公告具体类型', 'MAINTENANCE_NOTCIFICATION', '维修通知', 1, 1,
        '2021-09-30 10:22:03', '2022-03-10 13:53:59', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-2', NULL, 'NOTICE_TYPE', '公告具体类型', 'NO_WATER_POWER', '停水停电', 2, 2, '2021-09-30 10:22:03',
        '2022-03-10 13:54:06', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-3', NULL, 'NOTICE_TYPE', '公告具体类型', 'SECURITY_REMINDER', '安全提醒', 3, 3, '2021-09-30 10:22:03',
        '2022-03-10 13:54:09', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-4', NULL, 'NOTICE_TYPE', '公告具体类型', 'PUBLIC_SERVICE', '公益服务', 4, 4, '2021-09-30 10:22:03',
        '2022-03-10 13:54:11', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-5', NULL, 'NOTICE_TYPE', '公告具体类型', 'WARM_PROMPT', '温馨提示', 5, 5, '2021-09-30 10:22:03',
        '2022-03-10 13:54:13', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-6', NULL, 'NOTICE_TYPE', '公告具体类型', 'EMERGENCY_BROADCAST', '应急广播', 6, 6, '2021-09-30 10:22:03',
        '2022-03-10 13:54:15', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-7', NULL, 'NOTICE_TYPE', '公告具体类型', 'CHARAGE_NOTICE', '收费通知', 7, 7, '2021-09-30 10:22:03',
        '2022-03-10 13:54:17', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('NOTICE_TYPE-8', NULL, 'NOTICE_TYPE', '公告具体类型', 'OTHER', '其他', 8, 8, '2021-09-30 10:22:03',
        '2022-03-10 13:54:28', 0);

INSERT INTO `smart-park`.`com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`,
                                     `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`,
                                     `method_name`)
VALUES ('autoUpdateNoticeViews', '0 0 0/1 * * ?', '公告浏览数同步', '每隔1小时执行一次', 0, NULL, '', 1, 'park-customer',
        'noticeServiceImpl', 'updateViewsToDB');
