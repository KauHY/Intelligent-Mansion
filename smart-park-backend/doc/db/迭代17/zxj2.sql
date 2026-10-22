delete
from sys_dictionary
where type = 'ENERGY_CONSUMPTION_CATEGORY';

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WATER_CATEGORY', NULL, 'ENERGY_CONSUMPTION_CATEGORY', '能耗类别',
        'WATER_CATEGORY', '水能耗', 5, 1);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_CATEGORY', NULL, 'ENERGY_CONSUMPTION_CATEGORY', '能耗类别',
        'ELECTRICITY_CATEGORY', '电能耗', 6, 2);


DROP TABLE IF EXISTS `dev_fire_control_device`;
DROP TABLE IF EXISTS `dev_fire_warning_record`;
CREATE TABLE `dev_fire_warning_record`
(
    `id`               varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `device_id`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消防类型设备，设备id',
    `warn_type`        tinyint                                                      NOT NULL COMMENT '报警类型',
    `warn_time`        datetime                                                      DEFAULT NULL COMMENT '报警时间',
    `real_reason_type` tinyint                                                       DEFAULT NULL COMMENT '确认结果，1,真实火警，2误报，3消防演习',
    `opinion`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理意见',
    `status`           tinyint                                                       DEFAULT '0' COMMENT '状态,0待处理，1已处理',
    `create_by`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `create_time`      datetime                                                      DEFAULT CURRENT_TIMESTAMP,
    `update_by`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `update_time`      datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`          tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='消防报警记录';



INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FILE_DEVICE_WARN_0', NULL, 'FILE_DEVICE_WARN_TYPE', '消防报警',
        'FILE_DEVICE_WARN_0', '正常', 0, 1);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FILE_DEVICE_WARN_16', NULL, 'FILE_DEVICE_WARN_TYPE', '消防报警',
        'FILE_DEVICE_WARN_16', '火警报警', 16, 2);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FILE_DEVICE_WARN_20', NULL, 'FILE_DEVICE_WARN_TYPE', '消防报警',
        'FILE_DEVICE_WARN_20', '可燃气体监管报警', 20, 3);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FILE_DEVICE_WARN_32', NULL, 'FILE_DEVICE_WARN_TYPE', '消防报警',
        'FILE_DEVICE_WARN_32', '设备故障', 32, 4);


INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('config_1', NULL, '3D_MENU_URL_TYPE', '3d大屏的菜单链接ip+端口',
        'MENU_URL', 'http://39.101.150.104:3000', 1, 1);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('config_2', NULL, '3D_MENU_URL_TYPE', '3d大屏的菜单获取token的ip+端口',
        'TOKEN_URL', 'http://39.101.150.104:8080', 2, 2);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('config_3', NULL, '3D_MENU_URL_TYPE', '3d大屏获取token用户名',
        'UserName', 'TKUser', 3, 3);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('config_4', NULL, '3D_MENU_URL_TYPE', '3d大屏获取token密码',
        'PassWord', '123456', 4, 4);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('config_5', NULL, '3D_MENU_URL_TYPE', '淮安智慧谷',
        'parkOrgId', '1541335941202771970', 5, 5);