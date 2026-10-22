-- Initialize smart-park database
CREATE DATABASE IF NOT EXISTS `smart-park` CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `smart-park`;

-- Face check-in tables
CREATE TABLE IF NOT EXISTS `mee_face_checkin_person` (
    `id`            varchar(64) NOT NULL COMMENT '主键id',
    `person_name`   varchar(100) DEFAULT NULL COMMENT '人员姓名',
    `employee_no`   varchar(64) DEFAULT NULL COMMENT '工号',
    `phone`         varchar(32) DEFAULT NULL COMMENT '电话号码',
    `person_type`   tinyint DEFAULT NULL COMMENT '人员类型 1内部员工 2访客',
    `photo_path`    mediumtext COMMENT '人脸照片',
    `face_vector`   mediumtext COMMENT '人脸特征向量',
    `model_version` varchar(64) DEFAULT NULL COMMENT '模型版本',
    `status`        tinyint NOT NULL DEFAULT '1',
    `audit_status`  tinyint NOT NULL DEFAULT '0',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP,
    `create_by`     varchar(64) DEFAULT NULL,
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `update_by`     varchar(64) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `mee_face_checkin_member` (
    `id`                 varchar(64) NOT NULL,
    `meeting_reserve_id` varchar(64) NOT NULL,
    `person_id`          varchar(64) DEFAULT NULL,
    `status`             tinyint NOT NULL DEFAULT '1',
    `leave_status`       tinyint NOT NULL DEFAULT '0',
    `create_time`        datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `mee_face_checkin_request` (
    `id`                 varchar(64) NOT NULL,
    `request_id`         varchar(64) NOT NULL,
    `device_id`          varchar(64) DEFAULT NULL,
    `meeting_reserve_id` varchar(64) DEFAULT NULL,
    `accept_time`        datetime DEFAULT NULL,
    `status`             tinyint DEFAULT '0',
    `result`             varchar(64) DEFAULT NULL,
    `person_id`          varchar(64) DEFAULT NULL,
    `photo_path`         mediumtext,
    `create_time`        datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device_request` (`device_id`, `request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `mee_face_checkin_attendance` (
    `id`                 varchar(64) NOT NULL,
    `meeting_reserve_id` varchar(64) NOT NULL,
    `person_id`          varchar(64) NOT NULL,
    `person_name`        varchar(100) DEFAULT NULL,
    `checkin_time`       datetime DEFAULT NULL,
    `status`             tinyint DEFAULT NULL,
    `request_id`         varchar(64) DEFAULT NULL,
    `device_id`          varchar(64) DEFAULT NULL,
    `photo_path`         mediumtext,
    `remark`             varchar(255) DEFAULT NULL,
    `participant_category` tinyint DEFAULT NULL COMMENT '人员识别分类 1与会人员 2非与会人员 3非记录人员',
    `create_time`        datetime DEFAULT CURRENT_TIMESTAMP,
    `create_by`          varchar(64) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

