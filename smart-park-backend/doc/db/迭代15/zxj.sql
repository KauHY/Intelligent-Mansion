INSERT INTO `com_number_seq` (`number_name`, `value`)
VALUES ('DEVICE_ID', 1000);

drop table dev_device_info;
CREATE TABLE `dev_device_info`
(
    `id`              varchar(64) NOT NULL COMMENT '主键',
    `device_name`     varchar(30) NOT NULL COMMENT '设备名称',
    `device_id`       varchar(30) NOT NULL COMMENT '设备ID',
    `device_type`     int          DEFAULT NULL COMMENT '设备类型',
    `ip_address`      varchar(30)  DEFAULT NULL COMMENT 'IP地址',
    `park_org_id`     varchar(64)  DEFAULT NULL COMMENT '园区组织ID',
    `extension`       json         DEFAULT NULL COMMENT '拓展字段',
    `remark`          varchar(200) DEFAULT NULL COMMENT '备注',
    `status`          tinyint      DEFAULT '0' COMMENT '状态：0离线1在线',
    `using_status`    tinyint      DEFAULT '1' COMMENT '使用状态：1使用中',
    `deleted`         tinyint(1) DEFAULT '0',
    `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`       varchar(64)  DEFAULT NULL COMMENT '创建者',
    `update_by`       varchar(64)  DEFAULT NULL COMMENT '修改者',
    `update_time`     datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `tenant_id`       varchar(64)  DEFAULT NULL COMMENT '租户ID',
    `location`        varchar(300) DEFAULT NULL COMMENT '设备位置',
    `company_id`      varchar(32)  DEFAULT NULL COMMENT '企业ID',
    `channel_nos`     json         DEFAULT NULL COMMENT '通道号集合（json数组）',
    `device_category` int          DEFAULT NULL COMMENT '设备大类，0：门禁',
    PRIMARY KEY (`id`) USING BTREE,
    KEY               `idx_park_org_id` (`park_org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  COMMENT='设备信息';


drop table dev_meter_reading_record;
CREATE TABLE `dev_meter_reading_record`
(
    `id`                varchar(32)   NOT NULL,
    `meter_id`          varchar(32)   NOT NULL COMMENT '对应dev_device_info表主键',

    `park_org_id`       varchar(32)   NOT NULL COMMENT '园区id',
    `device_type`       int           DEFAULT NULL COMMENT '设备类型',
    `device_name`       varchar(30)   NOT NULL COMMENT '设备名称',
    `device_id`         varchar(32)   DEFAULT NULL COMMENT '设备id',
    `effect_property`   tinyint       NOT NULL COMMENT '表计属性',
    `effect_type`       tinyint       NOT NULL COMMENT '表计类别',
    `meter_type`        tinyint       NOT NULL COMMENT '表类型',
    `max_range`         int           NOT NULL COMMENT '最大量程',
    `location`          varchar(32)   DEFAULT NULL COMMENT '安装位置',
    `building_id`       varchar(32)   NOT NULL COMMENT '楼栋',
    `floors`            json          NOT NULL COMMENT '所属楼层多选',
    `house_ids`         json          DEFAULT NULL COMMENT '房屋多选',
    `initial_reading`   decimal(8, 2) NOT NULL COMMENT '初始读数',
    `initial_time`      datetime      NOT NULL COMMENT '初始时间',


    `meter_reading`     decimal(8, 2) NOT NULL COMMENT '抄表读数',
    `reading_time`      datetime      NOT NULL COMMENT '抄表时间',
    `corrected_reading` decimal(8, 2) DEFAULT NULL COMMENT '修正后读数',
    `remark`            varchar(255)  DEFAULT NULL COMMENT '备注',
    `import_remark`     varchar(255)  DEFAULT NULL COMMENT '导入备注',
    `create_by`         varchar(32)   DEFAULT NULL,
    `create_time`       datetime      DEFAULT CURRENT_TIMESTAMP,
    `update_time`       datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by`         varchar(32)   DEFAULT NULL,
    `deleted`           tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='抄表记录';


drop table dev_meter_consumption_record;
CREATE TABLE `dev_meter_consumption_record`
(
    `id`              varchar(32)   NOT NULL,
    `meter_id`        varchar(32)   NOT NULL COMMENT '对应dev_device_info表主键',

    `park_org_id`     varchar(32)   NOT NULL COMMENT '园区id',
    `device_type`     int           DEFAULT NULL COMMENT '设备类型',
    `effect_property` tinyint       NOT NULL COMMENT '表计属性',
    `effect_type`     tinyint       NOT NULL COMMENT '表计类别',
    `meter_type`      tinyint       NOT NULL COMMENT '表类型',
    `device_name`     varchar(30)   NOT NULL COMMENT '设备名称',
    `device_id`       varchar(32)   DEFAULT NULL COMMENT '设备id',
    `max_range`       int           NOT NULL COMMENT '最大量程',
    `location`        varchar(32)   DEFAULT NULL COMMENT '安装位置',
    `building_id`     varchar(32)   NOT NULL COMMENT '楼栋',
    `floors`          json          NOT NULL COMMENT '所属楼层多选',
    `house_ids`       json          DEFAULT NULL COMMENT '房屋多选',
    `initial_reading` decimal(8, 2) NOT NULL COMMENT '初始读数',
    `initial_time`    datetime      NOT NULL COMMENT '初始时间',

    `settled_month`   varchar(32)   NOT NULL COMMENT '结算月份',
    `last_reading`    decimal(8, 2) NOT NULL COMMENT '上次抄表读数',
    `last_time`       datetime      NOT NULL COMMENT '上次抄表时间',
    `this_reading`    decimal(8, 2) DEFAULT NULL COMMENT '本次抄表读数',
    `this_time`       datetime      NOT NULL COMMENT '本次抄表时间',
    `consumption`     decimal(8, 2) DEFAULT NULL COMMENT '用量',
    `create_by`       varchar(32)   DEFAULT NULL,
    `create_time`     datetime      DEFAULT CURRENT_TIMESTAMP,
    `update_time`     datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by`       varchar(32)   DEFAULT NULL,
    `deleted`         tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='水电表用量记录，结算使用';



CREATE TABLE `dev_meter_contract_house_relation`
(
    `id`                varchar(32) NOT NULL,
    `settled_month`     varchar(32) NOT NULL COMMENT '结算月份',
    `meter_id`          varchar(32) NOT NULL COMMENT '对应dev_device_info表主键',
    `contract_house_id` varchar(32) NOT NULL COMMENT '合同房屋id',
    `deleted`           tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='水电表合同房屋关系';

CREATE TABLE `dev_meter_consumption`
(
    `id`            varchar(32) NOT NULL,
    `type`          tinyint(1) NOT NULL COMMENT '0房屋用量记录，1企业用量记录',
    `park_org_id`   varchar(32) NOT NULL COMMENT '园区Id',
    `settled_month` varchar(32) NOT NULL COMMENT '结算月份',
    `company_id`    varchar(32) NOT NULL COMMENT '企业Id',
    `company_name`  varchar(32) NOT NULL COMMENT '企业Id',
    `contract_type` tinyint(1) NOT NULL COMMENT '合同类型',
    `building_id`   varchar(32) DEFAULT NULL COMMENT '楼栋id',
    `house_ids`     json        NOT NULL COMMENT '房屋id集合',
    `consumption`   json        DEFAULT NULL COMMENT '用量',
    `create_time`   datetime    DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by`     varchar(32) DEFAULT NULL,
    `deleted`       tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='企业或房屋水电用量';



CREATE TABLE `dev_month_reading`
(
    `id`           varchar(32)   NOT NULL,
    `device_id`    varchar(32)   NOT NULL COMMENT '设备id，设备唯一标识',
    `device_type`  tinyint(1) DEFAULT NULL COMMENT '5水表，6电表',
    `reading`      decimal(8, 2) NOT NULL COMMENT '本次读数',
    `consumption`  decimal(8, 2) NOT NULL COMMENT '用量',
    `year`         smallint      NOT NULL COMMENT '抄表年份',
    `month`        tinyint       NOT NULL COMMENT '抄表月份',
    `reading_time` datetime      NOT NULL COMMENT '抄表时间',
    `create_time`  datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='水电表每月读数';

CREATE TABLE `dev_day_reading`
(
    `id`           varchar(32)                                                  NOT NULL,
    `device_id`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备id，设备唯一标识，非设备的主键id',
    `device_type`  tinyint(1) DEFAULT NULL COMMENT '5水表，6电表',
    `reading`      decimal(8, 2)                                                NOT NULL COMMENT '本次读数',
    `consumption`  decimal(8, 2)                                                NOT NULL COMMENT '用量',
    `reading_date` date                                                         NOT NULL COMMENT '抄表日期',
    `reading_time` time                                                         NOT NULL COMMENT '抄表时间',
    `create_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='水电表每天读数';




















