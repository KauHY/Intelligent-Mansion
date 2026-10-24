CREATE TABLE `cus_goods_release` (
                                     `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
                                     `park_org_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '园区',
                                     `goods_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '编号',
                                     `submitter_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '申请人姓名',
                                     `submitter_mobile` varchar(32) DEFAULT NULL COMMENT '申请人手机号',
                                     `release_code` varchar(16) DEFAULT NULL COMMENT '放行码',
                                     `company` varchar(32) DEFAULT NULL COMMENT '所在公司',
                                     `release_datetime_start` datetime DEFAULT NULL COMMENT '放行时间0',
                                     `release_datetime_end` datetime DEFAULT NULL COMMENT '放行时间1',
                                     `goods_list` json DEFAULT NULL COMMENT '放行物品',
                                     `car_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '车牌号',
                                     `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                     `employee_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核人',
                                     `check_opinion` varchar(255) DEFAULT NULL COMMENT '审核意见',
                                     `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态,1待审核2 审核通过3审核不通过4取消;',
                                     `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人',
                                     `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
                                     `deleted` tinyint(1) NOT NULL DEFAULT '0',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='物品放行';

/**
  字典表-物品放行状态
 */
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('GOODS_RELEASE_STATUS_1', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'NOT_CHECK', '待审核', 1, 1, '2022-01-24 15:08:28', '2022-01-24 15:12:00', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('GOODS_RELEASE_STATUS_2', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'CHECK_PASS', '审核通过', 2, 2, '2022-01-24 15:08:28', '2022-01-24 15:12:15', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('GOODS_RELEASE_STATUS_3', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'CHECK_REJECT', '审核不通过', 3, 3, '2022-01-24 15:08:28', '2022-01-24 15:12:16', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('GOODS_RELEASE_STATUS_4', NULL, 'GOODS_RELEASE_STATUS', '放行状态', 'CANCEL', '已取消', 4, 4, '2022-01-24 15:08:28', '2022-01-24 15:12:22', 0);
