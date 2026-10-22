/**
  客户建表
 */
CREATE TABLE `bus_customer_info`
(
    `id`                        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `customer_code`             varchar(64)                                                  DEFAULT NULL COMMENT '客户编码',
    `name`                      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '客户名称',
    `park_org_id`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '园区id',
    `code`                      varchar(64)                                                  DEFAULT NULL COMMENT '统一社会信用代码',
    `type`                      tinyint                                                      DEFAULT NULL COMMENT '客户类型 0：物业公司，1：普通企业',
    `status`                    tinyint                                                      DEFAULT NULL COMMENT '客户状态,1，已成交，2未成交，3历史成交',
    `contacts`                  varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人',
    `contacts_tel`              varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
    `contacts_mail`             varchar(64)                                                  DEFAULT NULL COMMENT '联系人邮箱',
    `contacts_department`       varchar(128)                                                 DEFAULT NULL COMMENT '联系人部门',
    `contacts_position`         varchar(128)                                                 DEFAULT NULL COMMENT '联系人职位',
    `customer_tag_id`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '客户标签',
    `employee_id`               varchar(64)                                                  DEFAULT NULL COMMENT '跟进人id',
    `business_contacts_list`    json                                                         DEFAULT NULL COMMENT '业务联系人',
    `industry_field`            tinyint                                                      DEFAULT NULL COMMENT '行业领域',
    `company_scale`             int                                                          DEFAULT NULL COMMENT '企业规模(人数)\r\n\r\n',
    `legal_representative`      varchar(32)                                                  DEFAULT NULL COMMENT '法人代表',
    `company_mail`              varchar(64)                                                  DEFAULT NULL COMMENT '企业邮箱',
    `company_taxes_type`        tinyint                                                      DEFAULT NULL COMMENT '企业纳税类型',
    `company_management_status` tinyint                                                      DEFAULT NULL COMMENT '企业经营状态',
    `address`                   varchar(32)                                                  DEFAULT NULL COMMENT '地址',
    `registered_capital`        int                                                          DEFAULT NULL COMMENT '注册资本',
    `business_term`             tinyint                                                      DEFAULT NULL COMMENT '\r\n营业期限',
    `deleted`                   tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    `create_by`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
    `update_by`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
    `create_time`               datetime                                                     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`               datetime                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户表';


/**
  字典表-企业经营状态
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('COMPANY_MANAGEMENT_STATUS_1', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'IN_INDUSTRY', '在业', 1, 1,
        '2021-12-08 15:53:42', '2022-01-04 10:59:02', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('COMPANY_MANAGEMENT_STATUS_2', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'SUBSIST', '存续', 2, 2,
        '2021-12-08 15:53:42', '2022-01-04 10:59:03', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('COMPANY_MANAGEMENT_STATUS_3', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'CANCELLATION', '注销', 3, 3,
        '2021-12-08 15:53:42', '2022-01-04 10:59:04', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('COMPANY_MANAGEMENT_STATUS_4', NULL, 'COMPANY_MANAGEMENT_STATUS', '企业经营状态', 'OTHER', '其他', 4, 4,
        '2021-12-08 15:53:42', '2022-01-04 10:59:05', 0);

/**
  字典表-企业纳税类型
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('COMPANY_TAXES_TYPE_1', NULL, 'COMPANY_TAXES_TYPE', '企业纳税类型', 'FIRST_CLASS_TAXPAYERS', '一般纳税人', 1, 1,
        '2021-12-08 15:52:22', '2022-01-04 10:59:07', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('COMPANY_TAXES_TYPE_2', NULL, 'COMPANY_TAXES_TYPE', '企业纳税类型', 'SMALL_SCALE_TAXPAYER', '小规模纳税人', 2, 2,
        '2021-12-08 15:52:22', '2022-01-04 10:59:08', 0);

/**
  字典表-客户状态
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CUSTOMERINFO_STATUS_1', NULL, 'CUSTOMERINFO_STATUS', '客户状态', 'DEALED', '已成交', 1, 1, '2021-12-08 15:40:40',
        '2022-01-04 10:58:39', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CUSTOMERINFO_STATUS_2', NULL, 'CUSTOMERINFO_STATUS', '客户状态', 'NO_DEAL', '未成交', 2, 2, '2021-12-08 15:40:40',
        '2022-01-04 10:58:40', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CUSTOMERINFO_STATUS_3', NULL, 'CUSTOMERINFO_STATUS', '客户状态', 'HISTORICAL_TRANSACTION', '历史成交', 3, 3,
        '2021-12-08 15:40:40', '2022-01-04 10:58:41', 0);

/**
  字典表-客户类型
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CUSTOMERINFO_TYPE_1', NULL, 'CUSTOMERINFO_TYPE', '客户类型', 'PROPERTY_COMPANY', '物业公司', 0, 1,
        '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CUSTOMERINFO_TYPE_2', NULL, 'CUSTOMERINFO_TYPE', '客户类型', 'ORDINARY_COMPANY', '普通企业', 1, 2,
        '2021-12-08 15:40:40', '2022-01-04 10:58:51', 0);

/**
  字典表-行业领域
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INDUSTRY_FIELD_1', NULL, 'INDUSTRY_FIELD', '行业领域', 'INFORMATION_SOFTWARE', '信息软件', 1, 1, '2021-12-08 15:50:27',
        '2022-01-04 10:57:10', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INDUSTRY_FIELD_2', NULL, 'INDUSTRY_FIELD', '行业领域', 'COMMUNICATIONS_TECHNOLOGY', '通信技术', 2, 2,
        '2021-12-08 15:50:27', '2022-01-04 10:57:10', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INDUSTRY_FIELD_3', NULL, 'INDUSTRY_FIELD', '行业领域', 'BIOMEDICINE', '生物医药', 3, 3, '2021-12-08 15:50:27',
        '2022-01-04 10:57:11', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INDUSTRY_FIELD_4', NULL, 'INDUSTRY_FIELD', '行业领域', 'MEDICAL_APPARATUS_AND_INSTRUMENTS', '医疗器械', 4, 4,
        '2021-12-08 15:50:27', '2022-01-04 10:57:12', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INDUSTRY_FIELD_5', NULL, 'INDUSTRY_FIELD', '行业领域', 'FINANCIAL_SERVICE', '金融服务', 5, 5, '2021-12-08 15:50:27',
        '2022-01-04 10:57:14', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INDUSTRY_FIELD_6', NULL, 'INDUSTRY_FIELD', '行业领域', 'NEW_ENERGY_AND_ENVIRONMENTAL_PROTECTION', '新能源环保', 6, 6,
        '2021-12-08 15:50:27', '2022-01-04 10:57:15', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('INDUSTRY_FIELD_7', NULL, 'INDUSTRY_FIELD', '行业领域', 'OTHER', '其他', 7, 6, '2021-12-08 15:50:27',
        '2022-01-04 10:57:16', 0);


/**
  编号生成-客户（XF）
 */
UPDATE `smart-park`.`com_number_seq`
SET `number_name` = 'XF'
WHERE `number_name` = 'CM'
