-- auto-generated definition
CREATE TABLE `com_operate_log`
(
    `id`             varchar(32)  NOT NULL,
    `user_id`        varchar(32) COMMENT '账号',
    `request_uri`    varchar(255) NOT NULL,
    `ip`             varchar(50)  NOT NULL COMMENT 'IP',
    `operate_module` varchar(50)           DEFAULT NULL COMMENT '菜单',
    `operate_name`   varchar(50)           DEFAULT NULL COMMENT '操作名称',
    `time_consuming` int          NOT NULL COMMENT '耗时',
    `create_time`    datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志';

