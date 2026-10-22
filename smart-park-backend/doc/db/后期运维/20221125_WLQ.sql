DROP TABLE IF EXISTS `vis_policy_employee`;
CREATE TABLE `vis_policy_employee`
(
    `id`               INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `policy_id`        VARCHAR(64)   NOT NULL COMMENT '通行策略ID',
    `company_id`       VARCHAR(64)   NOT NULL COMMENT '企业ID',
    `employee_id`      VARCHAR(64)   NOT NULL COMMENT '员工ID',
    `device_person_id` BIGINT        NOT NULL COMMENT '设备人员ID',
    `card_no`          VARCHAR(20)   NOT NULL COMMENT '人员卡号',
    `device_ids`       VARCHAR(1000) NOT NULL COMMENT '设备IDs(逗号隔开)',
    `create_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='策略员工表';