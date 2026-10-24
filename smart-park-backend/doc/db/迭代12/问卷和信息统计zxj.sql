CREATE TABLE `cus_editor`
(
    `id`                   varchar(32)  NOT NULL,
    `element_type`         varchar(16)  NOT NULL COMMENT '项类型',
    `name`                 varchar(128) NOT NULL COMMENT '题目',
    `value`                varchar(128) DEFAULT NULL COMMENT '组件辅助字段',
    `constraint_condition` json         NOT NULL COMMENT '组件约束条件',
    `element_content`      json         DEFAULT NULL COMMENT '选项内容',
    `create_by`            varchar(255) DEFAULT NULL,
    `create_time`          datetime     DEFAULT CURRENT_TIMESTAMP,
    `update_by`            varchar(32)  DEFAULT NULL,
    `update_time`          datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`              tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文本编辑器';

CREATE TABLE `cus_questionnaire`
(
    `id`            varchar(32) NOT NULL,
    `park_org_id`   varchar(32) NOT NULL COMMENT '园区id',
    `cover_photo`   varchar(128)  DEFAULT NULL COMMENT '封面图片',
    `title`         varchar(32) NOT NULL COMMENT '问卷标题',
    `description`   varchar(1024) DEFAULT NULL COMMENT '问卷描述',
    `company_ids`   json          DEFAULT NULL COMMENT '收集对象,公司',
    `employee_ids`  json          DEFAULT NULL COMMENT '收集对象,员工',
    `editor_ids`    json          DEFAULT NULL COMMENT '问卷题目编辑器ids',
    `start_time`    datetime    NOT NULL COMMENT '开始时间',
    `deadline_time` datetime      DEFAULT NULL COMMENT '截止时间',
    `anonymous`     tinyint     NOT NULL COMMENT '是否匿名，0否，1是',
    `status`        tinyint       DEFAULT '1' COMMENT '状态，1设计中，2收集中，3已结束',
    `create_by`     varchar(32)   DEFAULT NULL,
    `create_time`   datetime      DEFAULT CURRENT_TIMESTAMP,
    `update_by`     varchar(32)   DEFAULT NULL,
    `update_time`   datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`       tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='问卷';


CREATE TABLE `cus_questionnaire_submission`
(
    `id`               varchar(32) NOT NULL,
    `questionnaire_id` varchar(32) NOT NULL COMMENT '问卷id',
    `submission`       json        NOT NULL COMMENT '问卷提交结果',
    `employee_id`      varchar(32) NOT NULL COMMENT '提交人',
    `create_by`        varchar(32) DEFAULT NULL,
    `create_time`      datetime    DEFAULT CURRENT_TIMESTAMP,
    `update_by`        varchar(32) DEFAULT NULL,
    `update_time`      datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`          tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='问卷提交内容';

CREATE TABLE `cus_form_template`
(
    `id`          varchar(32) NOT NULL,
    `park_org_id` varchar(32) NOT NULL COMMENT '园区id',
    `title`       varchar(32) NOT NULL COMMENT '模板标题,模板名称',
    `description` varchar(1024) DEFAULT NULL COMMENT '描述',
    `editor_ids`  json          DEFAULT NULL COMMENT '编辑器ids',
    `status`      tinyint       DEFAULT NULL COMMENT '状态',
    `create_by`   varchar(32)   DEFAULT NULL,
    `create_time` datetime      DEFAULT CURRENT_TIMESTAMP,
    `update_by`   varchar(32)   DEFAULT NULL,
    `update_time` datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单模板';

CREATE TABLE `cus_form`
(
    `id`            varchar(32) NOT NULL,
    `park_org_id`   varchar(32) NOT NULL COMMENT '园区id',
    `title`         varchar(32) NOT NULL COMMENT '表单名称',
    `company_ids`   json        DEFAULT NULL COMMENT '收集对象,公司',
    `employee_ids`  json        DEFAULT NULL COMMENT '收集对象,员工',
    `cycle`         tinyint     NOT NULL COMMENT '周期，1每日、2每周、3每月、4自定义',
    `deadline_time` varchar(32) NOT NULL COMMENT '截止时间',
    `deadline_num`  int(4) DEFAULT NULL COMMENT '截止时间的周几或第几天',
    `reminded`      tinyint     NOT NULL COMMENT '是否提醒，0不提醒，1提醒',
    `template_id`   varchar(32) NOT NULL COMMENT '模板id',
    `status`        tinyint     DEFAULT '1' COMMENT '状态，1设计中，2收集中，3已结束',
    `create_by`     varchar(32) DEFAULT NULL,
    `create_time`   datetime    DEFAULT CURRENT_TIMESTAMP,
    `update_by`     varchar(32) DEFAULT NULL,
    `update_time`   datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`       tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='信息统计表单';


CREATE TABLE `cus_form_submission`
(
    `id`                    varchar(32) NOT NULL,
    `form_id`               varchar(32) NOT NULL COMMENT '信息统计表单id',
    `submission`            json        NOT NULL COMMENT '信息统计表单提交结果',
    `received_employee_ids` json        NOT NULL COMMENT '信息统计表单接收',
    `employee_id`           varchar(32) NOT NULL COMMENT '提交人',
    `create_by`             varchar(32) DEFAULT NULL,
    `create_time`           datetime    DEFAULT CURRENT_TIMESTAMP,
    `update_by`             varchar(32) DEFAULT NULL,
    `update_time`           datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`               tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单提交内容';

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('QUESTIONNAIRE_STATUS_1', NULL, 'QUESTIONNAIRE_STATUS', '问卷状态',
        'QUESTIONNAIRE_STATUS_1', '设计中', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('QUESTIONNAIRE_STATUS_2', NULL, 'QUESTIONNAIRE_STATUS', '问卷状态',
        'QUESTIONNAIRE_STATUS_2', '收集中', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('QUESTIONNAIRE_STATUS_3', NULL, 'QUESTIONNAIRE_STATUS', '问卷状态',
        'QUESTIONNAIRE_STATUS_3', '已结束', 3, 3);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FORM_STATUS_1', NULL, 'FORM_STATUS', '表单状态',
        'FORM_STATUS_1', '待发布', 1, 1);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FORM_STATUS_2', NULL, 'FORM_STATUS', '表单状态',
        'FORM_STATUS_2', '收集中', 2, 2);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FORM_STATUS_3', NULL, 'FORM_STATUS', '表单状态',
        'FORM_STATUS_3', '已结束', 3, 3);



INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('JANUARY', NULL, 'MONTH', '月份',
        'JANUARY', '1月', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FEBRUARY', NULL, 'MONTH', '月份',
        'FEBRUARY', '2月', 2, 2);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MARCH', NULL, 'MONTH', '月份',
        'MARCH', '3月', 3, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('APRIL', NULL, 'MONTH', '月份',
        'APRIL', '4月', 4, 4);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('MAY', NULL, 'MONTH', '月份',
        'MAY', '5月', 5, 5);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('JUNE', NULL, 'MONTH', '月份',
        'JUNE', '6月', 6, 6);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('JULY', NULL, 'MONTH', '月份',
        'JULY', '7月', 7, 7);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('AUGUST', NULL, 'MONTH', '月份',
        'AUGUST', '8月', 8, 8);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SEPTEMBER', NULL, 'MONTH', '月份',
        'SEPTEMBER', '9月', 9, 9);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('OCTOBER', NULL, 'MONTH', '月份',
        'OCTOBER', '10月', 10, 10);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('NOVEMBER', NULL, 'MONTH', '月份',
        'NOVEMBER', '11月', 11, 11);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('DECEMBER', NULL, 'MONTH', '月份',
        'DECEMBER', '12月', 12, 12);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FORM_CYCLE_1', NULL, 'FORM_CYCLE', '统计周期',
        'FORM_CYCLE_1', '每日', 1, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FORM_CYCLE_2', NULL, 'FORM_CYCLE', '统计周期',
        'FORM_CYCLE_2', '每周', 2, 2);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FORM_CYCLE_3', NULL, 'FORM_CYCLE', '统计周期',
        'FORM_CYCLE_3', '每月', 3, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('FORM_CYCLE_4', NULL, 'FORM_CYCLE', '统计周期',
        'FORM_CYCLE_4', '自定义', 4, 4);
















