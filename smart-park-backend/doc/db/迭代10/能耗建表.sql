CREATE TABLE `dev_meter`
(
    `id`                    varchar(32)   NOT NULL,
    `park_org_id`           varchar(32)   NOT NULL COMMENT '园区id',
    `meter_no`              varchar(32)   NOT NULL COMMENT '仪表编号',
    `category`              tinyint       NOT NULL COMMENT '能耗类别（1电能耗，2水能耗）',
    `effect_property`       tinyint       NOT NULL COMMENT '表计属性',
    `effect_type`           tinyint       NOT NULL COMMENT '表计类别',
    `meter_type`            tinyint       NOT NULL COMMENT '表类型',
    `name`                  varchar(32)   NOT NULL COMMENT '名称',
    `serial_number`         varchar(32)   NOT NULL COMMENT '表计号码',
    `device_id`             varchar(32) DEFAULT NULL COMMENT '设备id',
    `ip`                    varchar(32) DEFAULT NULL COMMENT 'ip地址',
    `port`                  int         DEFAULT NULL COMMENT '端口号',
    `communication_port`    varchar(32) DEFAULT NULL COMMENT '通信端口',
    `max_range`             int           NOT NULL COMMENT '最大量程',
    `Installation_location` varchar(32) DEFAULT NULL COMMENT '安装位置',
    `building_id`           varchar(32)   NOT NULL COMMENT '楼栋',
    `floors`                json          NOT NULL COMMENT '所属楼层多选',
    `house_ids`             json        DEFAULT NULL COMMENT '房屋多选',
    `initial_reading`       decimal(8, 2) NOT NULL COMMENT '初始读数',
    `initial_time`          datetime      NOT NULL COMMENT '初始时间',
    `status`                tinyint     DEFAULT '1' COMMENT '状态',
    `create_by`             varchar(32) DEFAULT NULL,
    `create_time`           datetime    DEFAULT CURRENT_TIMESTAMP,
    `update_by`             varchar(32) DEFAULT NULL,
    `update_time`           datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`               tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仪表（电表和水表）';

CREATE TABLE `dev_meter_reading_record`
(
    `id`                varchar(32)   NOT NULL,
    `meter_id`          varchar(32)   NOT NULL,
    `meter_reading`     decimal(8, 2) NOT NULL COMMENT '抄表读数',
    `reading_time`      datetime      NOT NULL COMMENT '抄表时间',
    `corrected_reading` decimal(8, 2) NOT NULL COMMENT '修正后读数',
    `year`              smallint      NOT NULL COMMENT '抄表年份',
    `month`             tinyint       NOT NULL COMMENT '抄表月份',
    `day`               tinyint       NOT NULL COMMENT '抄表天',
    `remark`            varchar(255) DEFAULT NULL COMMENT '备注',
    `create_by`         varchar(255) DEFAULT NULL,
    `create_time`       datetime     DEFAULT CURRENT_TIMESTAMP,
    `update_by`         varchar(32)  DEFAULT NULL,
    `update_time`       datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`           tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE,
    KEY                 `index_reading_time` (`reading_time`) USING BTREE COMMENT '抄表时间索引',
    KEY                 `index_meterId_year_month_day` (`meter_id`,`year`,`month`,`day`) USING BTREE COMMENT '联合索引',
    KEY                 `index_meterId` (`meter_id`) USING BTREE COMMENT '仪表字段索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='抄表记录';

CREATE TABLE `dev_meter_warning_record`
(
    `id`            varchar(32) NOT NULL,
    `meter_id`      varchar(32) NOT NULL,
    `warning_code`  int         NOT NULL COMMENT '告警事件',
    `warning_level` tinyint  DEFAULT NULL COMMENT '告警等级',
    `warning_time`  datetime DEFAULT NULL COMMENT '告警时间',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='仪表异常告警记录';

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_METER_EFFECT_TYPE_1', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别',
        'ELECTRICITY_METER_EFFECT_TYPE_1', '室内用电', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_METER_EFFECT_TYPE_2', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别',
        'ELECTRICITY_METER_EFFECT_TYPE_2', '空调', 2, 2);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_METER_EFFECT_TYPE_3', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别',
        'ELECTRICITY_METER_EFFECT_TYPE_3', '公共照明', 3, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_METER_EFFECT_TYPE_4', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别',
        'ELECTRICITY_METER_EFFECT_TYPE_4', '消防照明', 4, 4);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_METER_EFFECT_TYPE_5', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别',
        'ELECTRICITY_METER_EFFECT_TYPE_5', '新风机', 5, 5);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_METER_EFFECT_TYPE_6', NULL, 'ELECTRICITY_METER_EFFECT_TYPE', '表计类别',
        'ELECTRICITY_METER_EFFECT_TYPE_6', '其他', 6, 6);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_EFFECT_PROPERTY_1', NULL, 'METER_EFFECT_PROPERTY', '表计属性',
        'METER_EFFECT_PROPERTY_1', '自用表计', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_EFFECT_PROPERTY_2', NULL, 'METER_EFFECT_PROPERTY', '表计属性',
        'METER_EFFECT_PROPERTY_2', '公摊表计', 2, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_EFFECT_PROPERTY_3', NULL, 'METER_EFFECT_PROPERTY', '表计属性',
        'METER_EFFECT_PROPERTY_3', '总分表计', 3, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_EFFECT_PROPERTY_4', NULL, 'METER_EFFECT_PROPERTY', '表计属性',
        'METER_EFFECT_PROPERTY_4', '总表计', 4, 4);

INSERT INTO `smart-park`.`com_number_seq` (`number_name`, `value`)
VALUES ('EM', 1000);
INSERT INTO `smart-park`.`com_number_seq` (`number_name`, `value`)
VALUES ('WM', 1000);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_STATUS_1', NULL, 'METER_STATUS', '仪表状态',
        'USING', '已开启', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_STATUS_2', NULL, 'METER_STATUS', '仪表状态',
        'STOP_USING', '已停用', 2, 2);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_STATUS_3', NULL, 'METER_STATUS', '仪表状态',
        'DISCARD', '已报废', 3, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_STATUS_4', NULL, 'METER_STATUS', '仪表状态',
        'REPLACE', '已换表', 4, 4);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('ELECTRICITY_CATEGORY', NULL, 'ENERGY_CONSUMPTION_CATEGORY', '能耗类别',
        'ELECTRICITY_CATEGORY', '电能耗', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WATER_CATEGORY', NULL, 'ENERGY_CONSUMPTION_CATEGORY', '能耗类别',
        'WATER_CATEGORY', '水能耗', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_TYPE_1', NULL, 'METER_TYPE', '仪表类型',
        'GENERAL_METER', '普通表', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_TYPE_2', NULL, 'METER_TYPE', '仪表类型',
        'INTELLIGENT_METER', '智能表', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_TYPE_READING_METHOD_1', NULL, 'METER_TYPE_READING_METHOD', '抄表方式',
        'MANUAL_READING', '手动抄表', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('METER_TYPE_READING_METHOD_2', NULL, 'METER_TYPE_READING_METHOD', '抄表方式',
        'AUTO_READING', '自动抄表', 2, 2);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARNING_CODE_1', NULL, 'WARNING_EVENT_CODE', '告警事件',
        'WARNING_CODE_1', '上限告警', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARNING_CODE_2', NULL, 'WARNING_EVENT_CODE', '告警事件',
        'WARNING_CODE_2', '下限告警', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARNING_CODE_3', NULL, 'WARNING_EVENT_CODE', '告警事件',
        'WARNING_CODE_3', '高电平告警', 3, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARNING_CODE_4', NULL, 'WARNING_EVENT_CODE', '告警事件',
        'WARNING_CODE_4', '低电平告警', 4, 4);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('WARNING_CODE_5', NULL, 'WARNING_EVENT_CODE', '告警事件',
        'WARNING_CODE_5', '通讯失败', 5, 5);

CREATE TABLE `dev_meter_reading_everyday_record`
(
    `id`                varchar(32)   NOT NULL,
    `meter_id`          varchar(32)   NOT NULL COMMENT '仪表id',
    `meter_reading`     decimal(8, 2) NOT NULL COMMENT '抄表读数',
    `reading_time`      datetime      NOT NULL COMMENT '抄表时间',
    `corrected_reading` decimal(8, 2) NOT NULL COMMENT '修正后读数',
    `year`              smallint      NOT NULL COMMENT '抄表年份',
    `month`             tinyint       NOT NULL COMMENT '抄表月份',
    `day`               tinyint       NOT NULL COMMENT '抄表天',
    `remark`            varchar(255) DEFAULT NULL COMMENT '备注',
    `create_time`       datetime     DEFAULT CURRENT_TIMESTAMP,
    `update_time`       datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by`         varchar(32)  DEFAULT NULL,
    `deleted`           tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='每天抄表记录';
