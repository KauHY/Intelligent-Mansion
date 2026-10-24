ALTER TABLE `smart-park`.`sys_company`
ADD COLUMN `industry_field` tinyint NULL COMMENT '行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他' AFTER `company_type`,
ADD COLUMN `company_taxes_type` tinyint NULL COMMENT '企业纳税类型:1-一般纳税人；2-小规模纳税人' AFTER `industry_field`,
ADD COLUMN `enterprise_size` tinyint NULL COMMENT '企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业' AFTER `company_taxes_type`,
ADD COLUMN `atts` text NULL COMMENT '附件路径，多个逗号隔开' AFTER `enterprise_size`,
ADD COLUMN `remark` varchar(200) NULL COMMENT '备注' AFTER `atts`;

CREATE TABLE `ind_enterprise_service` (
  `id` varchar(64) NOT NULL,
  `name` varchar(30) DEFAULT NULL COMMENT '服务名',
  `push_range` json DEFAULT NULL COMMENT '推送范围',
  `group_type` tinyint(1) DEFAULT NULL COMMENT '所在分组：0-服务指南；1-政策申报；2-其他',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `cover` varchar(128) DEFAULT NULL COMMENT '封面',
  `banner` varchar(1000) DEFAULT NULL COMMENT 'banner',
  `summary` varchar(255) DEFAULT NULL COMMENT '摘要',
  `detail` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详情',
  `url` varchar(255) DEFAULT NULL COMMENT '链接',
  `tel` varchar(20) DEFAULT NULL COMMENT '咨询电话',
  `att` varchar(1000) DEFAULT NULL COMMENT '附件',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `state` tinyint(1) DEFAULT '0' COMMENT '状态：0-待发布；1-已发布；2-停用',
  `create_by` varchar(32) DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='企业服务';

CREATE TABLE `com_multistage_category_relation` (
  `id` varchar(64) NOT NULL,
  `category_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '多级分类表id',
  `business_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务id',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `deleted` tinyint(1) DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(64) DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='多级分类关联表';

CREATE TABLE `ind_purchase_service` (
  `id` varchar(64) NOT NULL,
  `park_org_id` varchar(64) DEFAULT NULL COMMENT '园区Id',
  `company_id` varchar(64) DEFAULT NULL COMMENT '公司Id',
  `name` varchar(30) DEFAULT NULL COMMENT '服务名',
  `purchase_type` tinyint(1) DEFAULT NULL COMMENT '采购类型：0-货物；1-服务；2-其他',
  `detail` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详情',
  `effective_end_time` datetime DEFAULT NULL COMMENT '有效截止时间',
  `att_key` varchar(1000) DEFAULT NULL COMMENT '附件',
  `jump_type` tinyint(1) DEFAULT NULL COMMENT '跳转类型：0-无；1-URL；2-小程序',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '跳转地址',
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人',
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `state` tinyint(1) DEFAULT '0' COMMENT '状态：0-待发布；1-进行中；2-已下架；3-已过期；',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_by` varchar(32) DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购服务';

CREATE TABLE `ind_supply_service` (
  `id` varchar(64) NOT NULL,
  `park_org_id` varchar(64) DEFAULT NULL COMMENT '园区Id',
  `company_id` varchar(64) DEFAULT NULL COMMENT '公司Id',
  `name` varchar(30) DEFAULT NULL COMMENT '服务名',
  `supply_type` tinyint(1) DEFAULT NULL COMMENT '供应类型：0-产品供应；1-服务供应；2-其他供应',
  `detail` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '详情',
  `jump_type` tinyint(1) DEFAULT NULL COMMENT '跳转类型：0-无；1-URL；2-小程序',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '跳转地址',
  `contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系人',
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `state` tinyint(1) DEFAULT '0' COMMENT '状态：0-待发布；1-已发布；2-下架',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_by` varchar(32) DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应服务';

CREATE TABLE `ind_tender_record` (
  `id` varchar(64) NOT NULL,
  `purchase_id` varchar(64) DEFAULT NULL COMMENT '招标id',
  `company_name` varchar(30) DEFAULT NULL COMMENT '企业名称',
  `contact` varchar(30) DEFAULT NULL COMMENT '联系人',
  `tel` varchar(11) DEFAULT NULL COMMENT '电话',
  `mail` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `bidding_documents_paid` tinyint(1) DEFAULT '0' COMMENT '支付招标文件费：0-未支付；1-已支付',
  `margin_paid` tinyint(1) DEFAULT '0' COMMENT '保证金缴纳：0未缴纳；1-已缴纳',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(64) DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='投标记录';

/**
  字典表-企业类型
 */
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('COMPANY_TYPE_1', NULL, 'COMPANY_TYPE', '企业类型', 'PROPERTY_COMPANY', '物业公司', 0, 4);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('COMPANY_TYPE_2', NULL, 'COMPANY_TYPE', '企业类型', 'ORDINARY_COMPANY', '其他', 1, 5);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('COMPANY_TYPE_3', NULL, 'COMPANY_TYPE', '企业类型', 'RESEARCH_INSTITUTES', '科研院所',2,0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('COMPANY_TYPE_4', NULL, 'COMPANY_TYPE', '企业类型', 'BUSINESS_UNIT', '事业单位', 3, 1);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('COMPANY_TYPE_5', NULL, 'COMPANY_TYPE', '企业类型', 'ORGANS', '机关单位', 4, 2);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('COMPANY_TYPE_6', NULL, 'COMPANY_TYPE', '企业类型', 'TECHNOLOGY_ENTERPRISE', '科技型企业', 5, 3);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ENTERPRISE_SIZE_1', NULL, 'ENTERPRISE_SIZE', '企业规模', 'MEDIUM_ENTERPRISE', '中型企业', 0, 0, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ENTERPRISE_SIZE_2', NULL, 'ENTERPRISE_SIZE', '企业规模', 'SMALL_COMPANIES', '小型企业', 1, 1, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ENTERPRISE_SIZE_3', NULL, 'ENTERPRISE_SIZE', '企业规模', 'MICRO_ENTERPRISES', '微型企业', 2, 2, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ENTERPRISE_SIZE_4', NULL, 'ENTERPRISE_SIZE', '企业规模', 'MEDIUM_AND_SMALL_SIZED_ENTERPRISES', '中小微型企业', 3, 3, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SERVICE_GROUPING_1', NULL, 'SERVICE_GROUPING', '企业服务分组', 'SERVICE_GUIDE', '服务指南', 0, 0, '2021-12-08 15:40:40', '2022-03-19 17:56:50', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SERVICE_GROUPING_2', NULL, 'SERVICE_GROUPING', '企业服务分组', 'POLICY_DECLARATION', '政策申报', 1, 1, '2021-12-08 15:40:40', '2022-03-19 17:56:52', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SERVICE_GROUPING_3', NULL, 'SERVICE_GROUPING', '企业服务分组', 'OTHER', '其他', 2, 2, '2021-12-08 15:40:40', '2022-03-19 17:56:55', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ENTERPRISE_PUBLISH_STATUS_1', NULL, 'ENTERPRISE_PUBLISH_STATUS', '企业服务状态', 'TO_BE_PUBLISHED', '待发布', 0, 0, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ENTERPRISE_PUBLISH_STATUS_2', NULL, 'ENTERPRISE_PUBLISH_STATUS', '企业服务状态', 'PUBLISHED', '已发布', 1, 1, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ENTERPRISE_PUBLISH_STATUS_3', NULL, 'ENTERPRISE_PUBLISH_STATUS', '企业服务状态', 'DISABLE', '禁用', 2, 2, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SUPPLY_PUBLISH_STATUS_1', NULL, 'SUPPLY_PUBLISH_STATUS', '供应服务状态', 'TO_BE_PUBLISHED', '待发布', 0, 0, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SUPPLY_PUBLISH_STATUS_2', NULL, 'SUPPLY_PUBLISH_STATUS', '供应服务状态', 'PUBLISHED', '已发布', 1, 1, '2021-09-10 14:49:04', '2022-01-19 14:32:31', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SUPPLY_PUBLISH_STATUS_3', NULL, 'SUPPLY_PUBLISH_STATUS', '供应服务状态', 'HAS_BEEN_REMOVED', '已下架', 2, 2, '2021-09-10 14:49:04', '2022-03-16 19:50:19', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SUPPLY_TYPE_1', NULL, 'SUPPLY_TYPE', '供应类型', 'PRODUCT_SUPPLY', '产品供应', 0, 0, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SUPPLY_TYPE_2', NULL, 'SUPPLY_TYPE', '供应类型', 'SERVICE_SUPPLY', '服务供应', 1, 1, '2021-12-08 15:40:40', '2022-03-13 23:48:22', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('SUPPLY_TYPE_3', NULL, 'SUPPLY_TYPE', '供应类型', 'OTHER_SUPPLY', '其他供应', 2, 2, '2021-12-08 15:40:40', '2022-01-04 10:58:45', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PURCHASE_STATE_1', NULL, 'PURCHASE_STATE', '采购服务状态', 'TO_BE_PUBLISHED', '待发布', 0, 0, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PURCHASE_STATE_2', NULL, 'PURCHASE_STATE', '采购服务状态', 'IN_PROGRESS', '进行中', 1, 1, '2022-01-04 10:11:24', '2022-03-17 17:19:47', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PURCHASE_STATE_3', NULL, 'PURCHASE_STATE', '采购服务状态', 'HAS_BEEN_REMOVED', '已下架', 2, 2, '2022-01-04 10:11:24', '2022-03-17 17:04:09', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PURCHASE_STATE_5', NULL, 'PURCHASE_STATE', '采购服务状态', 'EXPIRED', '已过期', 3, 3, '2022-01-04 10:11:24', '2022-03-17 17:19:57', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PURCHASE_TYPE_1', NULL, 'PURCHASE_TYPE', '采购类型', 'GOODS', '货物', 0, 0, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PURCHASE_TYPE_2', NULL, 'PURCHASE_TYPE', '采购类型', 'SERVICES', '服务', 1, 1, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PURCHASE_TYPE_3', NULL, 'PURCHASE_TYPE', '采购类型', 'OTHERS', '其他', 2, 2, '2022-01-04 10:11:24', '2022-01-04 11:01:02', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PAYMENT_1', NULL, 'PAYMENT', '支付情况', 'UNPAID', '未支付', 0, 0, '2021-09-09 15:32:49', '2022-03-17 15:54:53', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('PAYMENT_2', NULL, 'PAYMENT', '支付情况', 'PAID', '已支付', 1, 1, '2021-09-09 15:32:49', '2022-03-17 15:54:56', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('CONTRIBUTE_1', NULL, 'CONTRIBUTE', '缴纳情况', 'UNPAID', '未缴纳', 0, 0, '2021-09-09 15:32:49', '2022-03-17 15:54:35', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('CONTRIBUTE_2', NULL, 'CONTRIBUTE', '缴纳情况', 'PAID', '已缴纳', 1, 1, '2021-09-09 15:32:49', '2022-03-17 15:54:39', 0);

INSERT INTO `smart-park`.`com_number_seq` (`number_name`, `date_time`, `value`) VALUES ('TZ', '2022-03-22 15:52:18', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('EQUIPMENT_LEDGER_RESULT_1', NULL, 'LEDGER_STATE', '设备台账状态', 'NORMAL', '正常', 1, 1, '2021-09-09 15:32:49', '2022-03-22 17:43:27', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('EQUIPMENT_LEDGER_RESULT_2', NULL, 'LEDGER_STATE', '设备台账状态', 'SCRAPED', '已报废', 0, 0, '2021-09-09 15:32:49', '2022-03-22 17:43:31', 0);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('ADD_DATA_SOURCE_1', NULL, 'ADD_DATA_SOURCE', '新增数据来源', 'ENTERPRISE_ADDED', '企业申请', 2, 2);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`) VALUES ('ADD_DATA_SOURCE_2', NULL, 'ADD_DATA_SOURCE', '新增数据来源', 'ADMIN_ADDED', '管理员录入', 1, 1);