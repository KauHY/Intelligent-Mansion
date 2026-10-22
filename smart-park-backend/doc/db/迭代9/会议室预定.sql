/****
  字典表-会议室权限类型
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_RELATION_TYPE_1', NULL, 'MEE_RELATION_TYPE', '会议室权限类型', 'COMPANY', '公司', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_RELATION_TYPE_2', NULL, 'MEE_RELATION_TYPE', '会议室权限类型', 'EMPLOYEE', '员工', 2, 2);

/*****
  字典表-会议室设备
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_EQUIPMENT_1', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'VIDEO_CAMERA', '摄像机', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_EQUIPMENT_2', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'WEB_CONFERENCE', '远程会议设备', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_EQUIPMENT_3', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'DVR', 'DVR', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_EQUIPMENT_4', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'TELPHONE', '电话', 4, 4);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_EQUIPMENT_5', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'PROJECTOR', '投影仪', 5, 5);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_EQUIPMENT_6', NULL, 'MEETING_EQUIPMENT', '会议室设备', 'ELECTRONIC_WHITEBOARD', '电子白板', 6, 6);

/**
  字典表-会议室类型
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_ROOM_TYPE_1', NULL, 'MEETING_ROOM_TYPE', '会议类型', 'ORDINARY_MEETING', '普通会议', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEE_ROOM_TYPE_2', NULL, 'MEETING_ROOM_TYPE', '会议类型', 'VIDEO_MEETING', '视频会议', 2, 2);

/**
  字典表-会议室预定状态
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_RESERVE_1', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'RESERVE_NO', '不可预定', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_RESERVE_2', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'RESERVE_YES', '可预订', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_RESERVE_3', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'RESERVE_OVER', '已预定', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_RESERVE_4', NULL, 'MEETING_RESERVE_STATUS', '会议室预定状态', 'CANDIDATE_YES', '可候补', 4, 4);

/**
  字典表-会议室审核状态
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_AUDIT_1', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_1', '预定待审核', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_AUDIT_2', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_2', '预定成功', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_AUDIT_3', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_3', '候补待审核', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_AUDIT_4', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_4', '候补中', 4, 4);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_AUDIT_5', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_5', '手动取消', 5, 5);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_AUDIT_6', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_6', '超时取消', 6, 6);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_AUDIT_7', NULL, 'MEETING_AUDIT_STATUS', '会议室审核状态', 'AUDIT_7', '审核失败', 7, 7);

/**
  字典表-预定审核状态
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('REVIEW_STATUS_1', NULL, 'REVIEW_STATUS', '预定审核状态', 'REVIEW_STATUS_NON', '未审核', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('REVIEW_STATUS_2', NULL, 'REVIEW_STATUS', '预定审核状态', 'REVIEW_STATUS_YES', '通过', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('REVIEW_STATUS_3', NULL, 'REVIEW_STATUS', '预定审核状态', 'REVIEW_STATUS_NO', '拒绝', 3, 3);

CREATE TABLE `mee_meeting_manage`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `building_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属楼栋单元',
    `floor_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在楼层',
    `house_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼层房号',
    `meeting_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室名称',
    `meeting_type`      tinyint                                                               DEFAULT NULL COMMENT '会议室类型 1：普通会议 2：视频会议',
    `open_time_start`   time                                                                  DEFAULT NULL COMMENT '开始开放时间',
    `open_time_end`     time                                                                  DEFAULT NULL COMMENT '结束开放时间',
    `capacity`          int                                                                   DEFAULT NULL COMMENT '容纳人数',
    `meeting_equipment` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '会议室设备 1摄像机 2远程会议设备 3DVR 4电话 5投影仪 6电子白板',
    `need_review`       tinyint(1)                                                            DEFAULT '0' COMMENT '是否需要审核 1是 0否',
    `status`            tinyint                                                               DEFAULT '1' COMMENT '状态：1开启 0关闭',
    `remark`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '会议室介绍',
    `create_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `update_time`       datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`           tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='会议室管理';



CREATE TABLE `mee_default_floor`
(
    `id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `company_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '绑定的企业ID',
    `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工id',
    `building_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设置默认楼栋单元',
    `floor_num`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设置默认楼层',
    `create_time` datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                                                              DEFAULT NULL COMMENT '更新时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='会议室默认楼层';



CREATE TABLE `mee_meeting_permission`
(
    `id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `meeting_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
    `relation_type` tinyint                                                      NOT NULL COMMENT '关联类型 1公司 2员工',
    `company_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '关联公司id',
    `employee_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '关联员工id',
    `create_time`   datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `update_time`   datetime                                                              DEFAULT NULL COMMENT '更新时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`       tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='会议室权限关联';


CREATE TABLE `mee_meeting_plane_figure`
(
    `id`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `park_org_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '所属园区',
    `building_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '所属楼栋单元',
    `floor_id`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '所在楼层',
    `meeting_plane_figure` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室平面图',
    `create_time`          datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `create_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '创建人',
    `update_time`          datetime                                                               DEFAULT NULL COMMENT '更新时间',
    `update_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '更新人',
    `deleted`              tinyint(1)                                                    NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='会议室平面图';


CREATE TABLE `mee_meeting_reserve`
(
    `id`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `employee_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请人id',
    `meeting_subject`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议主题',
    `meeting_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '会议室id',
    `meeting_type`         tinyint                                                      NOT NULL COMMENT '会议类型 1：普通会议 2：视频会议',
    `type`                 tinyint                                                               DEFAULT '1' COMMENT '预订类型 1预订 2候补',
    `meeting_people_num`   int                                                                   DEFAULT NULL COMMENT '与会人数',
    `attendees`            varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        DEFAULT NULL COMMENT '与会人list',
    `meeting_time_start`   datetime                                                              DEFAULT NULL COMMENT '会议开始时间',
    `meeting_time_end`     datetime                                                              DEFAULT NULL COMMENT '会议结束时间',
    `meeting_real_start`   datetime                                                              DEFAULT NULL COMMENT '会议实际开始时间',
    `meeting_real_end`     datetime                                                              DEFAULT NULL COMMENT '会议实际结束时间',
    `meeting_summary`      varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci        DEFAULT NULL COMMENT '会议纪要',
    `meeting_summary_file` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '会议纪要附件',
    `remark`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '备注',
    `create_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `create_time`          datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`              tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='会议室预订';


CREATE TABLE `mee_meeting_review`
(
    `id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `meeting_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
    `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '关联审批员工id',
    `create_time` datetime                                                              DEFAULT NULL COMMENT '创建时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                                                              DEFAULT NULL COMMENT '更新时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='会议室审核人关联';


CREATE TABLE `mee_reserve_audit`
(
    `id`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `meeting_reserve_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '会议室预定id',
    `meeting_subject`     varchar(100)                                                          DEFAULT NULL COMMENT '会议主题',
    `meeting_id`          varchar(64)                                                           DEFAULT NULL COMMENT '会议室id',
    `employee_id`         varchar(64)                                                           DEFAULT NULL COMMENT '申请人id',
    `now_date`            date                                                                  DEFAULT NULL COMMENT '当前日期',
    `start_time`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '开始时间',
    `end_time`            varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '结束时间',
    `meeting_state`       tinyint                                                               DEFAULT '1' COMMENT '会议预定状态：1不可预定 2可预订 3已预定 4可候补',
    `meeting_status`      tinyint                                                               DEFAULT NULL COMMENT '会议审核状态 1预定待审核、2预定成功、3候补待审核、4候补中、5手动取消 6超时取消',
    `review_id`           varchar(64)                                                           DEFAULT NULL COMMENT '审核人',
    `review_status`       tinyint                                                               DEFAULT NULL COMMENT '审核状态 0未审核 1通过 2拒绝',
    `review_msg`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         DEFAULT NULL COMMENT '审核意见',
    `review_time`         datetime                                                              DEFAULT NULL COMMENT '审核时间',
    `apply_time`          datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `meeting_cancel_time` datetime                                                              DEFAULT NULL COMMENT '会议取消时间',
    `create_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '创建人',
    `create_time`         datetime                                                              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         datetime                                                              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '更新人',
    `deleted`             tinyint(1)                                                   NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='会议室预订审核';

/**
  预约会议室超时自动取消
 */
INSERT INTO `smart-park`.`com_task`(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`,
                                    `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`,
                                    `method_name`)
VALUES ('autoCancelReserve', '0 1,31 * * * ?', '预约会议室超时自动取消', '每半小时触发一次', 0, NULL, NULL, NULL, 'park-meeting',
        'meetingAuditTask', 'autoCancelReserve');


