ALTER TABLE `smart-park`.`con_rent_contract_trade`
    ADD COLUMN `contract_type` tinyint NULL DEFAULT NULL COMMENT '合同费用类型 3-房屋租赁合同；4-物业合同；5-公寓合同' AFTER `id`;

ALTER TABLE `smart-park`.`con_rent_contract_bill`
    ADD COLUMN `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  3-房屋租赁合同；4-物业合同；5-公寓合同；' AFTER `contract_no`,
    ADD COLUMN `fee_type` tinyint NULL DEFAULT NULL COMMENT '费用类型 1房屋租金 7履约保证金' AFTER `account_period`,
    ADD COLUMN `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公寓费用使用' AFTER `house_name`,
    ADD COLUMN `house_messages` json NULL COMMENT '房源对象' AFTER `house_name`,
ADD COLUMN `employee_ids` json NULL COMMENT '公寓人员中房屋所关联的员工' AFTER `house_id`;


ALTER TABLE `smart-park`.`con_rent_contract_leave`
    ADD COLUMN `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  3-房屋租赁合同；4-物业合同；5-公寓合同；' AFTER `contract_no`;

ALTER TABLE `smart-park`.`con_rent_contract_bill_record`
    ADD COLUMN `contract_type` tinyint NULL DEFAULT NULL COMMENT '费用类型  3-房屋租赁合同；4-物业合同；5-公寓合同；' AFTER `id`;

-- ALTER TABLE `smart-park`.`con_rent_contract_bill`
--     ADD COLUMN `house_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公寓费用使用' AFTER `house_name`,
-- ADD COLUMN `employee_ids` json NULL COMMENT '公寓人员中房屋所关联的员工' AFTER `house_id`;
-- ALTER TABLE `smart-park-dev`.`con_rent_contract_bill`
--     ADD COLUMN `house_messages` json NULL COMMENT '房源对象' AFTER `house_name`;

CREATE TABLE `vis_apartment_person_house_relation`
(
    `id`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `apartment_person_id` varchar(64)                                                  NOT NULL COMMENT '公寓人员Id',
    `employee_id`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联员工Id',
    `house_id`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联房屋id',
    `deleted`             tinyint(1) DEFAULT '0' COMMENT '删除状态，0未删除，1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='公寓人员管理房屋关系表';


ALTER TABLE `smart-park`.`con_rent_contract_leave`
    ADD COLUMN `type` tinyint NULL DEFAULT 1 COMMENT '离场结算类型：RENT_FEE_LEAVE_TYPE' AFTER `id`;

ALTER TABLE `smart-park`.`con_rent_contract_trade`
DROP
COLUMN `pay_time`;
