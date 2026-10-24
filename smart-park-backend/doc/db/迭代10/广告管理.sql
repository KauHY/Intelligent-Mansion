/**
  广告建表
 */
CREATE TABLE `cus_advertisement`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `name`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '广告名称',
    `sort`              tinyint                                                      NOT NULL COMMENT '排序：数字越小越靠前',
    `position`          tinyint                                                       DEFAULT '1' COMMENT ' 广告位置 ：1 首页轮播图',
    `picture`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片',
    `jump_type`         tinyint                                                       DEFAULT NULL COMMENT '跳转类型：0 无，1 URL，2 小程序AppID',
    `jump_content`      varchar(255)                                                  DEFAULT NULL COMMENT '跳转地址/需跳转的小程序AppID',
    `status`            tinyint                                                       DEFAULT '0' COMMENT '状态： 0 生效中，1 待生效，2 已过期',
    `effect_time_start` datetime                                                     NOT NULL COMMENT '生效开始时间',
    `effect_time_end`   datetime                                                     NOT NULL COMMENT '生效开始时间',
    `create_time`       datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '修改人',
    `deleted`           tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='广告';

/**
  字典表-广告位置
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ADVERTISEMENT_POSITION_1', NULL, 'ADVERTISEMENT_POSITION', '广告位置', 'CIRCULAR_BANNER', '首页轮播图', 1, 1,
        '2022-01-04 10:54:05', '2022-01-04 11:01:05', 0);


/**
  字典表-广告跳转类型
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ADVERTISEMENT_JUMP_TYPE_1', NULL, 'ADVERTISEMENT_JUMP_TYPE', '广告跳转类型', 'NOTHING', '无', 0, 1,
        '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ADVERTISEMENT_JUMP_TYPE_2', NULL, 'ADVERTISEMENT_JUMP_TYPE', '广告跳转类型', 'URL', 'URL', 1, 2,
        '2022-01-04 10:11:24', '2022-01-04 11:01:03', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ADVERTISEMENT_JUMP_TYPE_3', NULL, 'ADVERTISEMENT_JUMP_TYPE', '广告跳转类型', 'MINI_PROGRAM', '小程序', 2, 3,
        '2022-01-04 10:11:24', '2022-01-04 11:01:04', 0);

/**
  字典表-广告状态
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ADVERTISEMENT_STATUS_1', NULL, 'ADVERTISEMENT_STATUS', '广告状态', 'EFFECTIVELY', '生效中', 0, 2,
        '2022-01-04 10:25:08', '2022-01-04 11:00:56', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ADVERTISEMENT_STATUS_2', NULL, 'ADVERTISEMENT_STATUS', '广告状态', 'TO_BE_EFFECTIVE', '待生效', 1, 1,
        '2022-01-04 10:25:08', '2022-01-04 11:00:58', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ADVERTISEMENT_STATUS_3', NULL, 'ADVERTISEMENT_STATUS', '广告状态', 'OUT_OF_DATE', '已过期', 2, 3,
        '2022-01-04 10:25:08', '2022-01-04 11:00:44', 0);
