/**
  活动建表
 */
CREATE TABLE `cus_activity`
(
    `id`                        varchar(32)   NOT NULL,
    `name`                      varchar(30)   NOT NULL COMMENT '活动名称',
    `place`                     varchar(30)   NOT NULL COMMENT '活动地点',
    `activity_time_start`       datetime      NOT NULL COMMENT '活动开始时间',
    `activity_time_end`         datetime      NOT NULL COMMENT '活动结束时间',
    `effect_time`               datetime      NOT NULL COMMENT '生效时间',
    `content`                   varchar(4096) NOT NULL COMMENT '活动详情',
    `conf_register`             tinyint                                                       DEFAULT '0' COMMENT '是否需要报名',
    `conf_register_deadline`    datetime                                                      DEFAULT NULL COMMENT '报名截止时间',
    `conf_register_limit`       tinyint       NOT NULL                                        DEFAULT '0' COMMENT '是否限制报名人数',
    `conf_register_limit_count` bigint                                                        DEFAULT NULL COMMENT '报名人数上限',
    `topping`                   tinyint(1) DEFAULT '0' COMMENT '置顶，1置顶',
    `topping_time`              datetime                                                      DEFAULT NULL COMMENT '置顶时间',
    `list_picture`              varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '列表图片',
    `detail_picture`            varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详情图片',
    `status`                    tinyint                                                       DEFAULT NULL,
    `deleted`                   tinyint(1) NOT NULL DEFAULT '0' COMMENT '0,未删除，1删除',
    `create_time`               datetime                                                      DEFAULT CURRENT_TIMESTAMP,
    `create_by`                 varchar(32)                                                   DEFAULT NULL,
    `update_time`               datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by`                 varchar(32)                                                   DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动';
/**
  字典表-活动状态
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ACTIVITY_STATUS_1', NULL, 'ACTIVITY_STATUS', '活动状态', 'NOT_EFFECT', '待生效', 1, 1, '2022-01-11 10:55:32',
        '2022-01-11 11:06:24', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ACTIVITY_STATUS_2', NULL, 'ACTIVITY_STATUS', '活动状态', 'NOTICING', '预告中', 2, 2, '2022-01-11 10:55:32',
        '2022-01-11 11:06:31', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ACTIVITY_STATUS_3', NULL, 'ACTIVITY_STATUS', '活动状态', 'ONGOING', '进行中', 3, 3, '2022-01-11 10:55:32',
        '2022-01-11 11:06:39', 0);
