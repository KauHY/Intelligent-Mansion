/**
  活动报名记录
 */
CREATE TABLE `cus_activity_register_record`
(
    `id`          varchar(32)                                                  NOT NULL,
    `activity_id` varchar(32)                                                  NOT NULL COMMENT '活动ID',
    `name`        varchar(30)                                                  NOT NULL COMMENT '姓名',
    `mobile`      varchar(30)                                                  NOT NULL COMMENT '手机号',
    `deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '0,未删除，1删除',
    `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='活动报名记录';
