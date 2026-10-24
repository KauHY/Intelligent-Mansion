ALTER TABLE `smart-park`.`con_meter_consumption_leave`
    ADD COLUMN `house_id` varchar(64) NULL COMMENT '结算房间id' AFTER `checkout_date`;
ALTER TABLE `smart-park`.`bus_business_chance`
    MODIFY COLUMN `remark` varchar (1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注' AFTER `latest_follow_time`,
    MODIFY COLUMN `remind_content` varchar (1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提醒内容' AFTER `remind_time`;

ALTER TABLE `smart-park`.`bus_business_chance_record`
    MODIFY COLUMN `follow_content` varchar (1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跟进内容' AFTER `follow_time`;

ALTER TABLE `smart-park`.`bus_customer_info`
    MODIFY COLUMN `type` tinyint NULL DEFAULT NULL COMMENT '客户类型（COMPANY_TYPE） 0物业公司、1其他、2科研院所、3事业单位、4机关单位、5科技型企业' AFTER `code`;


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_TYPE_FOR_HOUSE_3', NULL, 'CONTRACT_TYPE_FOR_HOUSE', '房源状态查询合同类型', 'RENTAL_CONTRACT', '租赁合同', 3, 3,
        '2021-12-07 14:35:34', '2022-05-06 16:19:07', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_TYPE_FOR_HOUSE_5', NULL, 'CONTRACT_TYPE_FOR_HOUSE', '房源状态查询合同类型', 'APARTMENT_CONTRACT', '公寓合同', 5, 5,
        '2021-12-07 14:35:34', '2022-04-08 17:05:29', 0);

ALTER TABLE `smart-park`.`cus_activity`
    MODIFY COLUMN `conf_register_limit_count` bigint NULL DEFAULT NULL COMMENT '报名人数上限' AFTER `conf_register_limit`;


ALTER TABLE `smart-park`.`sys_company`
    ADD COLUMN `status` tinyint NOT NULL DEFAULT 1 COMMENT '企业状态:0-已禁用,1-正常' AFTER `source`;
