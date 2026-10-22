INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FIELDWORK_APPLY_STATE_1', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'TO_BE_AUDITED', '待审批', 1, 1, '2022-02-21 14:41:14', '2022-02-21 16:53:55', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FIELDWORK_APPLY_STATE_2', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'APPROVE', '审核通过', 2, 2, '2022-02-21 14:41:14', '2022-02-21 14:54:18', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FIELDWORK_APPLY_STATE_3', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'REJECTED', '审核不通过', 3, 3, '2022-02-21 14:41:14', '2022-02-21 14:58:43', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FIELDWORK_APPLY_STATE_4', NULL, 'FIELDWORK_APPLY_STATE', '外勤申请状态', 'CANCEL', '已取消', 4, 4, '2022-02-21 14:41:14', '2022-02-21 14:59:15', 0);

CREATE TABLE `smart-park`.`att_fieldwork_apply_record`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `park_org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区',
  `park_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '园区名',
  `company_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公司名',
  `employee_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人id',
  `employee_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人姓名',
  `mobile` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人号码',
  `fieldwork_start_time` datetime NULL DEFAULT NULL COMMENT '外勤开始时间',
  `fieldwork_end_time` datetime NULL DEFAULT NULL COMMENT '外勤结束时间',
  `reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '事由',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '状态：1-待审批；2-审核通过；3-审批不通过；4-已取消',
  `situation` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地点',
  `audit_opinion` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审批意见',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新/审批时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人/审批人',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '外勤申请记录' ROW_FORMAT = DYNAMIC;