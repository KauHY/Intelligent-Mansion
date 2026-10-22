DROP TABLE IF EXISTS `dev_meter_warning_record`;
CREATE TABLE `dev_meter_warning_record`
(
    `id`            varchar(32) NOT NULL,
    `device_id`     varchar(32) NOT NULL,
    `device_type`   tinyint(1) NOT NULL COMMENT '5水表,6电表,9液位计',
    `param_id`      varchar(32) DEFAULT NULL,
    `param_name`    varchar(32) DEFAULT NULL,
    `param_value`   varchar(10) DEFAULT NULL,
    `warning_code`  int         NOT NULL COMMENT '告警事件',
    `warning_level` tinyint     DEFAULT NULL COMMENT '告警等级',
    `status`        tinyint     DEFAULT NULL COMMENT '0,未处理，1已处理',
    `warning_time`  datetime    DEFAULT NULL COMMENT '告警时间',
    `create_time`   datetime    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  COMMENT='仪表异常告警记录';


DROP TABLE IF EXISTS `dev_hour_reading`;
CREATE TABLE `dev_hour_reading`
(
    `id`                varchar(24)   NOT NULL,
    `device_id`         varchar(10)   NOT NULL COMMENT '设备id，设备唯一标识，非设备的主键id',
    `device_type`       tinyint(1) NOT NULL COMMENT '5水表，6电表',
    `start_reading_num` decimal(8, 2) NOT NULL COMMENT '开始读数',
    `end_reading_num`   decimal(8, 2) NOT NULL COMMENT '截止读数',
    `consumption`       decimal(8, 2) NOT NULL COMMENT '用量',
    `reading_date`      date          NOT NULL COMMENT '抄表日期',
    `reading_hour`      tinyint       NOT NULL COMMENT '抄表小时，24H',
    `reading_date_time` datetime      NOT NULL COMMENT '抄表时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='水电表每小时读数';

DROP TABLE IF EXISTS `dev_day_reading`;
CREATE TABLE `dev_day_reading`
(
    `id`                varchar(24)   NOT NULL,
    `device_id`         varchar(10)   NOT NULL COMMENT '设备id，设备唯一标识，非设备的主键id',
    `device_type`       tinyint(1) DEFAULT NULL COMMENT '5水表，6电表',
    `start_reading_num` decimal(8, 2) NOT NULL COMMENT '开始读数',
    `end_reading_num`   decimal(8, 2) NOT NULL COMMENT '截止读数',
    `consumption`       decimal(8, 2) NOT NULL COMMENT '用量',
    `reading_date`      date          NOT NULL COMMENT '抄表日期',
    `reading_time`      time          NOT NULL COMMENT '抄表时间',
    `reading_date_time` datetime      NOT NULL COMMENT '抄表时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='水电表每天读数';

DROP TABLE IF EXISTS `dev_month_reading`;
CREATE TABLE `dev_month_reading`
(
    `id`                varchar(24)   NOT NULL,
    `device_id`         varchar(10)   NOT NULL COMMENT '设备id，设备唯一标识，非设备的主键id',
    `device_type`       tinyint(1) DEFAULT NULL COMMENT '5水表，6电表',
    `start_reading_num` decimal(8, 2) NOT NULL COMMENT '开始读数',
    `end_reading_num`   decimal(8, 2) NOT NULL COMMENT '截止读数',
    `consumption`       decimal(8, 2) NOT NULL COMMENT '用量',
    `year`              smallint      NOT NULL COMMENT '抄表年份',
    `month`             tinyint       NOT NULL COMMENT '抄表月份',
    `reading_date`      date          NOT NULL COMMENT '抄表日期',
    `reading_date_time` datetime      NOT NULL COMMENT '抄表时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='水电表每月读数';