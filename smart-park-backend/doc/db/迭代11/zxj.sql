INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('NOT_HAVE', NULL, 'HAVE_OR_NOT_HAVE', '有或无',
        'NOT_HAVE', '无', 0, 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HAVE', NULL, 'HAVE_OR_NOT_HAVE', '有或无',
        'HAVE', '有', 1, 1);


CREATE TABLE `dev_fire_control_device`
(
    `id`                    varchar(32) NOT NULL,
    `park_org_id`           varchar(32) NOT NULL COMMENT '园区id',
    `system_type`           tinyint     NOT NULL COMMENT '所属系统',
    `name`                  varchar(32) NOT NULL COMMENT '设备名称',
    `device_no`             varchar(32) NOT NULL COMMENT '设备编号',
    `serial_number`         varchar(32) NOT NULL COMMENT 'SN号码',
    `device_id`             varchar(32) NOT NULL COMMENT '设备id',
    `ip`                    varchar(32) NOT NULL COMMENT 'ip地址',
    `port`                  int         NOT NULL COMMENT '端口号',
    `communication_port`    varchar(32) NOT NULL COMMENT '通信端口',
    `building_id`           varchar(32) NOT NULL COMMENT '安装楼栋',
    `floor`                 int(4) NOT NULL COMMENT '所属楼层',
    `house_id`              varchar(32) DEFAULT NULL COMMENT '房屋',
    `installation_location` varchar(32) DEFAULT NULL COMMENT '安装位置',
    `status`                tinyint     DEFAULT NULL COMMENT '状态',
    `create_by`             varchar(32) DEFAULT NULL,
    `create_time`           datetime    DEFAULT CURRENT_TIMESTAMP,
    `update_by`             varchar(32) DEFAULT NULL,
    `update_time`           datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`               tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消防设备';


CREATE TABLE `dev_fire_warning_record`
(
    `id`                     varchar(32) NOT NULL,
    `fire_control_device_id` varchar(32) NOT NULL COMMENT '消防设备id',
    `warn_type`              tinyint     NOT NULL COMMENT '报警类型',
    `warn_reason_code`       tinyint      DEFAULT NULL COMMENT '报警原因',
    `warn_time`              datetime     DEFAULT NULL COMMENT '报警时间',
    `real_reason_type`       tinyint      DEFAULT NULL COMMENT '确认结果，1,真实火警，2误报，3消防演习',
    `opinion`                varchar(100) DEFAULT NULL COMMENT '处理意见',
    `status`                 tinyint      DEFAULT '0' COMMENT '状态,0待处理，1已处理',
    `create_by`              varchar(255) DEFAULT NULL,
    `create_time`            datetime     DEFAULT CURRENT_TIMESTAMP,
    `update_by`              varchar(32)  DEFAULT NULL,
    `update_time`            datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`                tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报警记录';


INSERT INTO `smart-park`.`com_number_seq` (`number_name`, `value`)
VALUES ('FD', 1000);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_1', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_1', '消防主机', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_2', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_2', '无线烟感', 2, 2);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_3', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_3', '电气火灾', 3, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_4', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_4', '可燃气体', 4, 4);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_5', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_5', '消防用水', 5, 5);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_6', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_6', '防火门', 6, 6);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_7', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_7', '消防电源', 7, 7);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_8', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_8', '充电桩', 8, 8);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYSTEM_TYPE_9', NULL, 'SYSTEM_TYPE', '消防设备所属系统',
        'SYSTEM_TYPE_9', '物联设备', 9, 9);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARN_TYPE_1', NULL, 'WARN_TYPE', '消防设备报警类型',
        'WARN_TYPE_1', '故障', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARN_TYPE_2', NULL, 'WARN_TYPE', '消防设备报警类型',
        'WARN_TYPE_2', '报警', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARN_TYPE_3', NULL, 'WARN_TYPE', '消防设备报警类型',
        'WARN_TYPE_3', '离线', 3, 3);


alter table ins_inspection_plan
    alter column status set default '1';