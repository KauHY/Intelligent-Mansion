/**
  字典表——会议室可预定时段
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_RESERVE_TIME_1', NULL, 'MEETING_RESERVE_TIME', '会议室可预定时段', 'NOON', '上午', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_RESERVE_TIME_2', NULL, 'MEETING_RESERVE_TIME', '会议室可预定时段', 'AFTERNOON', '下午', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_RESERVE_TIME_3', NULL, 'MEETING_RESERVE_TIME', '会议室可预定时段', 'EVENING', '晚上', 3, 3);

/**
  字典表——会议室物资类型
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_GOODS_TYPE_1', NULL, 'MEETING_GOODS_TYPE', '会议室物资类型', 'PURCHASE', '购买型', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_GOODS_TYPE_2', NULL, 'MEETING_GOODS_TYPE', '会议室物资类型', 'RENTAL', '租用型', 2, 2);

/**
  字典表——会议室设备类型
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_DEVICE_TYPE_1', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'VIDEO_CAMERA', '摄像机', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_DEVICE_TYPE_2', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'TELECONFERENCE_EQUIPMENT', '远程会议设备', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_DEVICE_TYPE_3', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'PROJECTOR', '投影仪', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MEETING_DEVICE_TYPE_4', NULL, 'MEETING_DEVICE_TYPE', '会议室设备类型', 'ELECTRONIC_WHITEBOARD', '电子白板', 4, 4);

/**
  字典表——租赁会议室审核状态
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('CUS_MEETING_AUDIT_1', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'AUDIT_RENDING', '待审核', 1, 1);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('CUS_MEETING_AUDIT_2', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'AUDIT_PASS', '审核通过', 2, 2);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('CUS_MEETING_AUDIT_3', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'AUDIT_FAIL', '审核不通过', 3, 3);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('CUS_MEETING_AUDIT_4', NULL, 'CUS_MEETING_AUDIT_STATUS', '租赁会议室审核状态', 'CANCEL', '已取消', 4, 4);

/**
  编码表——租赁会议室订单编码
 */
INSERT INTO `smart-park`.`com_number_seq`(`number_name`,  `value`) VALUES ('YD', 1000);


CREATE TABLE `cus_meeting_goods`
(
    `id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `goods_type`  tinyint                                                      DEFAULT NULL COMMENT '物资类型 1购买型、2租用型',
    `goods_name`  varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物资名称',
    `unit_price`  decimal(12, 2)                                               DEFAULT NULL COMMENT '价格(元)',
    `unit`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '计量单位',
    `goods_image` varchar(255)                                                 DEFAULT NULL COMMENT '物资图片',
    `remark`      varchar(200)                                                 DEFAULT NULL COMMENT '说明',
    `create_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) COMMENT='会议室物资管理';

CREATE TABLE `cus_meeting_rent`
(
    `id`                varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属园区',
    `building_id`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属楼栋单元',
    `house_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼层房号',
    `meeting_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室名称',
    `telephone`         varchar(50)                                                    DEFAULT NULL COMMENT '咨询电话',
    `capacity`          int                                                            DEFAULT NULL COMMENT '容纳人数',
    `meeting_equipment` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '会议室设备 1摄像机、2远程会议设备、3投影仪、4电子白板',
    `cover_image`       varchar(255)                                                   DEFAULT NULL COMMENT '封面图片（单张）',
    `detail_image`      varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详情图片（最多五张）',
    `status`            tinyint                                                        DEFAULT '1' COMMENT '状态：1开启 0关闭',
    `remark`            varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '会议室介绍',
    `reserve_instruct`  varchar(1000)                                                  DEFAULT NULL COMMENT '预定须知',
    `charge`            tinyint                                                        DEFAULT NULL COMMENT '是否收费 1是 0否',
    `rent_price`        decimal(10, 2)                                                 DEFAULT NULL COMMENT '租赁价格',
    `goods_id`          varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物资id 逗号分割',
    `use_permission`    tinyint                                                        DEFAULT NULL COMMENT '使用权限设置 0不限 1指定公司/人员',
    `create_time`       datetime                                                       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '创建人',
    `update_time`       datetime                                                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '更新人',
    `deleted`           tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) COMMENT='会议室租赁管理';

CREATE TABLE `cus_meeting_permission`
(
    `id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `meeting_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
    `relation_type` tinyint                                                      NOT NULL COMMENT '关联类型 1公司 2员工',
    `company_id`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联公司id',
    `employee_id`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联员工id',
    `create_time`   datetime                                                     DEFAULT NULL COMMENT '创建时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `update_time`   datetime                                                     DEFAULT NULL COMMENT '更新时间',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
    `deleted`       tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) COMMENT='会议室权限关联';

CREATE TABLE `cus_meeting_schedule_time`
(
    `id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `meeting_id`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议室id',
    `start_time`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '开始时间',
    `end_time`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '结束时间',
    `time_type`   tinyint                                                      DEFAULT NULL COMMENT '时间段类型 1上午 2下午 3晚上',
    `create_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `update_time` datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
    `deleted`     tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) COMMENT='可预定时段管理';

CREATE TABLE `cus_meeting_reserve`
(
    `id`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `park_org_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '申请人当前园区',
    `employee_id`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请人id',
    `reserve_no`           varchar(64)                                                    DEFAULT NULL COMMENT '申请单号',
    `meeting_subject`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议主题',
    `meeting_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '会议室id',
    `applicant_unit`       varchar(30)                                                    DEFAULT NULL COMMENT '申请单位（当前员工所属企业名称）',
    `applicant_name`       varchar(30)                                                    DEFAULT NULL COMMENT '联系人（当前员工姓名）',
    `mobile`               varchar(20)                                                    DEFAULT '1' COMMENT '手机号码（当前员工手机号码）',
    `meeting_people_num`   int                                                            DEFAULT NULL COMMENT '预计人数',
    `estimated_amount`     decimal(30, 2)                                                 DEFAULT NULL COMMENT '预计总金额=所选时段租赁金额+已选物资金额；每一物资金额=单价*数量；',
    `applicant_time`       datetime                                                       DEFAULT NULL COMMENT '申请时间',
    `state_change_time`    datetime                                                       DEFAULT NULL COMMENT '状态变更时间（审核或取消时间）',
    `review_status`        tinyint                                                        DEFAULT NULL COMMENT '审核状态 1未审核 2通过 3拒绝 4已取消',
    `review_amount`        decimal(30, 2)                                                 DEFAULT NULL COMMENT '审核（实际金额）',
    `review_msg`           varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '审批意见',
    `remark`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '备注',
    `old_park_org_id`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '会议室所在园区（备份）',
    `old_meeting_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '会议室名字（备份）',
    `old_capacity`         int                                                            DEFAULT NULL COMMENT '会议室容纳人数（备份）',
    `old_cover_image`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '封面图片（单张）（备份）',
    `old_reserve_instruct` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '预定须知（备份）',
    `create_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '创建人',
    `create_time`          datetime                                                       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   DEFAULT NULL COMMENT '更新人',
    `update_time`          datetime                                                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`              tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租赁会议室预订';

CREATE TABLE `cus_meeting_reserve_goods`
(
    `id`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `meeting_id`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '会议室id',
    `meeting_reserve_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '会议室预定id',
    `goods_id`           varchar(64)                                                   DEFAULT NULL COMMENT '对应物资ID',
    `number`             int                                                           DEFAULT NULL COMMENT '物品数量',
    `old_goods_type`     tinyint                                                       DEFAULT NULL COMMENT '物资类型 1购买型、2租用型（备份）',
    `old_goods_name`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '物资名称（备份）',
    `old_unit_price`     decimal(12, 2)                                                DEFAULT NULL COMMENT '价格(元)（备份）',
    `old_unit`           varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '计量单位（备份）',
    `old_goods_image`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '物资图片（备份）',
    `remark`             varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '说明（备份）',
    `create_time`        datetime                                                      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '创建人',
    `update_time`        datetime                                                      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  DEFAULT NULL COMMENT '更新人',
    `deleted`            tinyint(1) DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租赁会议室预定物资管理';


CREATE TABLE `cus_meeting_reserve_time`
(
    `id`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键id',
    `meeting_reserve_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '会议室预定id',
    `meeting_id`         varchar(64)                                                  DEFAULT NULL COMMENT '会议室id',
    `employee_id`        varchar(64)                                                  DEFAULT NULL COMMENT '申请人id',
    `now_date`           date                                                         DEFAULT NULL COMMENT '当前日期',
    `time_type`          tinyint                                                      DEFAULT NULL COMMENT '时间段类型 1上午 2下午 3晚上',
    `start_time`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '开始时间',
    `end_time`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '结束时间',
    `meeting_state`      tinyint                                                      DEFAULT '3' COMMENT '会议可用状态：0不可预定 1可预订',
    `create_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `create_time`        datetime                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
    `update_time`        datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1是 0否',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租赁会议室预订时间管理';