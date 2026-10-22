/**
  入驻企业 新增数据来源字段
 */
ALTER TABLE `smart-park`.`sys_company`
ADD COLUMN `source` tinyint NULL COMMENT '数据来源：1-管理员录入 2-企业申请' AFTER `visitor_client_pic_url`;

/**
  字典表——企业/入驻审核状态
 */
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYS_COMPANY_AUDIT_1', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'AUDIT_RENDING', '待审核', 1, 1);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYS_COMPANY_AUDIT_2', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'AUDIT_PASS', '审核通过', 2, 2);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYS_COMPANY_AUDIT_3', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'AUDIT_FAIL', '审核不通过', 3, 3);
INSERT INTO sys_dictionary(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('SYS_COMPANY_AUDIT_4', NULL, 'SYS_COMPANY_AUDIT_STATUS', '企业/入驻审核状态', 'CANCEL', '已取消', 4, 4);

/**
  企业端表结构
 */
CREATE TABLE `sys_business_user` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '归属租户',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `mobile` varchar(64) DEFAULT NULL COMMENT '手机号',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除 0否，1 是',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='企业端用户表';

CREATE TABLE `sys_company_audit` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `business_user_id` varchar(64) DEFAULT NULL COMMENT '企业端用户id',
  `company_information_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业信息id',
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请入驻园区ID',
  `contact_person` varchar(255) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `apply_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '入驻提交时间',
  `audit_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核意见',
  `audit_status` tinyint DEFAULT NULL COMMENT '状态 1待审核 2通过 3拒绝 4已取消',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='企业入驻审核';

CREATE TABLE `sys_company_information` (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `head_org_id` varchar(64) DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统一社会信用代码',
  `licenses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '营业执照图片路径',
  `company_type` tinyint DEFAULT NULL COMMENT '企业类型:0-物业公司；1-普通公司；2-科研院所；3-事业单位；4-机关单位；5-科技型企业',
  `industry_field` tinyint DEFAULT NULL COMMENT '行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他',
  `company_taxes_type` tinyint DEFAULT NULL COMMENT '企业纳税类型:1-一般纳税人；2-小规模纳税人',
  `enterprise_size` tinyint DEFAULT NULL COMMENT '企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业',
  `atts` text COMMENT '附件路径，多个逗号隔开',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '注册地址',
  `business_user_id` varchar(64) DEFAULT NULL COMMENT '企业端用户id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='企业信息';

CREATE TABLE `sys_company_information_audit` (
   `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `head_org_id` varchar(64) DEFAULT NULL COMMENT '租户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '企业名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统一社会信用代码',
  `licenses` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '营业执照图片路径',
  `company_type` tinyint DEFAULT NULL COMMENT '企业类型:0-物业公司；1-普通公司；2-科研院所；3-事业单位；4-机关单位；5-科技型企业',
  `industry_field` tinyint DEFAULT NULL COMMENT '行业领域:1-信息软件；2-通行技术；3-生物医药；4-医疗器械；5-金融服务；6-新能源环保；7-其他',
  `company_taxes_type` tinyint DEFAULT NULL COMMENT '企业纳税类型:1-一般纳税人；2-小规模纳税人',
  `enterprise_size` tinyint DEFAULT NULL COMMENT '企业规模:0-中型企业；1-小型企业；2-微型企业；3-中小微型企业',
  `atts` text COMMENT '附件路径，多个逗号隔开',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `address` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '注册地址',
  `business_user_id` varchar(64) DEFAULT NULL COMMENT '企业端用户id',
  `audit_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核意见',
  `audit_status` tinyint DEFAULT NULL COMMENT '状态 1待审核 2通过 3拒绝 4已取消',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT='企业信息审核';

DROP TABLE IF EXISTS `sys_business_menu`;
CREATE TABLE `sys_business_menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `origin_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户菜单对应的原始菜单ID',
  `head_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织的顶级组织的id,即租户，0表示租户管理平台的模板菜单，-1表示租户管理平台的系统菜单',
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父节点id，0表示菜单最上级节点',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` double(10, 2) NOT NULL COMMENT '排序',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '状态：0：不显示，1：显示',
  `route` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由',
  `route_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由名',
  `api_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口路径',
  `type` tinyint(0) NULL DEFAULT NULL COMMENT '类型：0：菜单 1：按钮',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT = '企业端菜单/按钮表';

-- ----------------------------
-- Records of sys_business_menu
-- ----------------------------
INSERT INTO `sys_business_menu` VALUES ('1506470175973257218', '1506470175272808449', '1437679155114393601', '0', '园区入驻', 'brand-fill', 2.00, 1, '/wisdom-park-company/entry-record', 'entryRecord', '/smart-park', 0, '2022-03-25 10:21:59', '2022-03-30 10:19:20', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506470443699875841', '1506470442739380226', '1437679155114393601', '1506470175973257218', '入驻申请', '', 0.00, 1, '/wisdom-park-company/entry-record/entry-supply', 'entrySupply', '/smart-park', 0, '2022-03-25 10:23:45', '2022-03-30 10:18:55', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506470804351299586', '1506470803843788802', '1437679155114393601', '1506470175973257218', '入驻记录', NULL, 0.00, 1, '/wisdom-park-company/entry-record/entry-log', 'entryLog', '/smart-park', 0, '2022-03-25 10:24:49', '2022-03-30 10:19:03', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506471067837476866', '1506471067535486977', '1437679155114393601', '0', '供应服务', 'good', 3.00, 1, '/wisdom-park-company/supply-service', 'supplyService', '/smart-park', 0, '2022-03-25 10:25:55', '2022-03-30 10:19:44', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506471231545356289', '1506471231226589186', '1437679155114393601', '0', '采购服务', 'brand', 4.00, 1, '/wisdom-park-company/procurement-services', 'procurementServices', '/smart-park', 0, '2022-03-25 10:27:15', '2022-03-30 10:19:52', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506509169448660994', '1506509169113116673', '1437679155114393601', '0', '企业信息', 'form', 1.00, 1, '/wisdom-park-company/enterprise-information', 'enterpriseInformation', '/smart-park', 0, '2022-03-25 10:20:19', '2022-03-30 10:18:44', NULL, NULL, 0);
INSERT INTO `sys_business_menu` VALUES ('1506587258329444354', '1506587257880653825', '1437679155114393601', '1506471067837476866', '投标信息详情', NULL, 0.00, 1, '/wisdom-park-company/procurement-services/procurement-detail', 'procurementDetail', '/smart-park', 2, '2022-03-25 10:28:23', '2022-03-30 10:19:55', NULL, NULL, 0);
