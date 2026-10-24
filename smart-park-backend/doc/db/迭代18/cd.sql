CREATE TABLE `dev_device_info_house`
(
    `id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `device_info_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'device_info_id',
    `device_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备Id',
    `channel_no`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '通道编码',
    `device_type`    int                                                          DEFAULT NULL COMMENT '设备类型',
    `park_org_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区Id',
    `building_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '楼栋Id',
    `floor`          tinyint                                                      DEFAULT NULL COMMENT '楼层',
    `house_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '房屋Id',
    `deleted`        tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    `create_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `create_time`    datetime                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
    `update_time`    datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='智能锁_房间关联表';


CREATE TABLE `sys_employee_finger`
(
    `id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `employee_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工Id',
    `finger_data`  json                                                         NOT NULL COMMENT '指纹数据',
    `finger_index` tinyint                                                      DEFAULT NULL COMMENT '指纹数据索引（用作区分多个指纹）',
    `deleted`      tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    `create_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
    `update_time`  datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='人员指纹表';


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_10', NULL, 'DEVICE_TYPE', '设备类型', 'SMART_LOCK', '智能锁', 10, 9, '2021-09-07 14:22:03',
        '2022-07-29 11:16:09', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ACCESS_DEVICE_TYPE_3', NULL, 'ACCESS_DEVICE_TYPE', '门禁设备类型', 'SMART_LOCK', '智能锁', 10, 3, '2021-09-07 14:22:03',
        '2022-04-07 15:09:02', 0);


