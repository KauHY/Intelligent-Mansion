-- Face check-in tables for smart meeting attendance
CREATE TABLE IF NOT EXISTS `mee_face_checkin_person` (
    `id`            varchar(64) NOT NULL COMMENT '主键id',
    `person_name`   varchar(100) DEFAULT NULL COMMENT '人员姓名',
    `employee_no`   varchar(64) DEFAULT NULL COMMENT '工号（内部员工）',
    `phone`         varchar(32) DEFAULT NULL COMMENT '电话号码',
    `person_type`   tinyint DEFAULT NULL COMMENT '人员类型 1内部员工 2访客',
    `photo_path`    mediumtext COMMENT '人脸照片文件路径或base64',
    `face_vector`   mediumtext COMMENT '人脸特征向量（base64）',
    `model_version` varchar(64) DEFAULT NULL COMMENT '模型版本',
    `status`        tinyint NOT NULL DEFAULT '1' COMMENT '状态 1启用 0停用',
    `audit_status`  tinyint NOT NULL DEFAULT '0' COMMENT '审核状态 0待审核 1通过 2驳回',
    `create_time`   datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`     varchar(64) DEFAULT NULL COMMENT '创建人',
    `update_time`   datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`     varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '人脸识别签到人员';

CREATE TABLE IF NOT EXISTS `mee_face_checkin_member` (
    `id`                 varchar(64) NOT NULL COMMENT '主键id',
    `meeting_reserve_id` varchar(64) NOT NULL COMMENT '会议预约id',
    `person_id`          varchar(64) NOT NULL COMMENT '人员id',
    `status`             tinyint NOT NULL DEFAULT '1' COMMENT '资格状态 1有效 0取消',
    `leave_status`       tinyint NOT NULL DEFAULT '0' COMMENT '请假状态 0未请假 1已请假',
    `create_time`        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '人脸识别签到会议成员';

CREATE TABLE IF NOT EXISTS `mee_face_checkin_request` (
    `id`                 varchar(64) NOT NULL COMMENT '主键id',
    `request_id`         varchar(64) NOT NULL COMMENT '请求id',
    `device_id`          varchar(64) DEFAULT NULL COMMENT '设备id',
    `meeting_reserve_id` varchar(64) DEFAULT NULL COMMENT '会议预约id',
    `accept_time`        datetime DEFAULT NULL COMMENT '受理时间',
    `status`             tinyint DEFAULT '0' COMMENT '处理状态 0处理中 1完成',
    `result`             varchar(64) DEFAULT NULL COMMENT '处理结果',
    `person_id`          varchar(64) DEFAULT NULL COMMENT '识别人员id',
    `photo_path`         mediumtext COMMENT '照片文件路径',
    `create_time`        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device_request` (`device_id`, `request_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '人脸识别核验请求';

CREATE TABLE IF NOT EXISTS `mee_face_checkin_attendance` (
    `id`                 varchar(64) NOT NULL COMMENT '主键id',
    `meeting_reserve_id` varchar(64) NOT NULL COMMENT '会议预约id',
    `person_id`          varchar(64) NOT NULL COMMENT '人员id',
    `person_name`        varchar(100) DEFAULT NULL COMMENT '人员姓名',
    `checkin_time`       datetime DEFAULT NULL COMMENT '签到时间',
    `status`             tinyint DEFAULT NULL COMMENT '签到状态 1成功 2已签到 3无权限 4未识别 5未开始 6已结束 7处理失败',
    `request_id`         varchar(64) DEFAULT NULL COMMENT '请求id',
    `device_id`          varchar(64) DEFAULT NULL COMMENT '设备id',
    `photo_path`         mediumtext COMMENT '照片文件路径',
    `remark`             varchar(255) DEFAULT NULL COMMENT '备注',
    `create_time`        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`          varchar(64) DEFAULT NULL COMMENT '创建人',
    PRIMARY KEY (`id`),
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '人脸识别签到记录';

