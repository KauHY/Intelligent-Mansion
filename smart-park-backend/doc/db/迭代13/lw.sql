CREATE TABLE `sys_wx_oa_mobile_relation`
(
    `id`          varchar(64) NOT NULL,
    `open_id`     varchar(32) DEFAULT NULL COMMENT '公众号用户openId',
    `mobile`      varchar(11) DEFAULT NULL COMMENT '手机号',
    `app_id`      varchar(64) DEFAULT NULL COMMENT '公众号appId',
    `create_time` datetime    DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint(1)  DEFAULT '0' COMMENT '删除状态',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='公众号用户openId与手机号关联表';

INSERT INTO `smart-park`.`com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`,
                                     `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`,
                                     `method_name`)
VALUES ('stateUpdate', '0 0 1 * * ?', '合同状态值更新', NULL, 0, NULL, '', 1, 'park-contract', 'contractStateTask',
        'stateUpdate');


INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('PROTOCOL_ATTRIBUTES_1', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'NEW_PROTOCOL', '新签协议', 1, 1,
        '2021-12-07 14:29:04', '2021-12-09 16:56:52', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('PROTOCOL_ATTRIBUTES_2', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'RENEW_PROTOCOL', '续签协议', 2, 2,
        '2021-12-07 14:29:04', '2021-12-09 16:56:54', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('PROTOCOL_ATTRIBUTES_3', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'CHANGE_PROTOCOL', '变更协议', 3, 3,
        '2021-12-07 14:29:04', '2021-12-09 16:56:56', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('PROTOCOL_ATTRIBUTES_4', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'WITHDRAWAL_PROTOCOL', '退约协议', 4, 4,
        '2021-12-07 14:29:04', '2021-12-09 16:56:58', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('PROTOCOL_ATTRIBUTES_5', NULL, 'PROTOCOL_ATTRIBUTES', '协议属性', 'HISTORICAL_PROTOCOL', '历史协议', 5, 5,
        '2021-12-07 14:29:04', '2021-12-09 16:57:00', 0);

DELETE
FROM sys_dictionary
WHERE type = 'CONTRACT_TYPE';
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_TYPE_1', NULL, 'CONTRACT_TYPE', '合同类型', 'ADMISSION_AGREEMENT', '入园协议', 1, 1, '2021-12-07 14:35:34',
        '2022-04-08 17:05:19', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_TYPE_2', NULL, 'CONTRACT_TYPE', '合同类型', 'INVITATION_BID', '招引合同', 2, 2, '2021-12-07 14:35:34',
        '2022-04-08 17:05:15', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_TYPE_3', NULL, 'CONTRACT_TYPE', '合同类型', 'RENTAL_CONTRACT', '房屋租赁合同', 3, 3, '2021-12-07 14:35:34',
        '2022-04-08 17:05:21', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_TYPE_4', NULL, 'CONTRACT_TYPE', '合同类型', 'PROPERTY_CONTRACT', '物业合同', 4, 4, '2021-12-07 14:35:34',
        '2022-04-08 17:05:24', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_TYPE_5', NULL, 'CONTRACT_TYPE', '合同类型', 'APARTMENT_CONTRACT', '公寓合同', 5, 5, '2021-12-07 14:35:34',
        '2022-04-08 17:05:29', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_STATE_9', NULL, 'CONTRACT_STATE', '合同状态', 'UNDER_REVIEW_WITHDRAW', '审核中', 9, 9, '2021-12-07 14:41:14',
        '2022-04-11 13:56:45', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('AUDIT_RESULT_1', NULL, 'AUDIT_RESULT', '审核结果', 'PASS', '通过', 1, 1, '2021-12-07 14:35:34',
        '2022-04-08 17:05:29', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('AUDIT_RESULT_2', NULL, 'AUDIT_RESULT', '审核结果', 'NOT_PASS', '不通过', 2, 2, '2021-12-07 14:35:34',
        '2022-04-08 17:05:29', 0);

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('CONTRACT_OPERATE_9', NULL, 'CONTRACT_OPERATE', '合同操作', 'AUDIT', '审核', 8, 8, '2021-12-07 14:36:59',
        '2021-12-09 16:56:08', 0);

ALTER TABLE `smart-park`.`con_contract`
    ADD COLUMN `total_area`      double        NULL COMMENT '总面积（㎡）' AFTER `park_org_id`,
    ADD COLUMN `house_messages`  json          NULL COMMENT '关联房屋信息' AFTER `extension`,
    ADD COLUMN `park_org_name`   varchar(50)   NULL COMMENT '园区名' AFTER `park_org_id`,
    ADD COLUMN `withdraw_date`   date          NULL COMMENT '退约日期' AFTER `house_messages`,
    ADD COLUMN `withdraw_reason` varchar(1000) NULL COMMENT '退约原因' AFTER `withdraw_date`,
    MODIFY COLUMN `attributes` tinyint(1) NULL DEFAULT NULL COMMENT '合同/协议属性：1-新签；2-续签；3-变更；4-退约；5-历史' AFTER `employee_id`,
    MODIFY COLUMN `type` tinyint(1) NULL DEFAULT NULL COMMENT '合同类型：1-入园协议；2-招引合同；3-房屋租赁合同；4-物业合同；5-公寓合同；' AFTER `attributes`,
    MODIFY COLUMN `state` tinyint(1) NULL DEFAULT 1 COMMENT '合同状态：1-待提交；2-审核中；3-待生效；4-生效中；5-已退约；6-已失效；7-已作废；8-已过期；9-退约审核中' AFTER `attachment_key`,
    MODIFY COLUMN `extension` json NULL COMMENT '扩展字段' AFTER `process_no`,
    MODIFY COLUMN `no` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '合同编号' AFTER `id`;

ALTER TABLE `smart-park`.`con_contract_billing_plan`
    ADD COLUMN `free_rent_day` int NULL COMMENT '免租期（天）' AFTER `billing_end_date`;
ALTER TABLE `smart-park`.`con_contract_billing_plan`
    ADD COLUMN `charge_item_message` json NULL COMMENT '收费项详细属性' AFTER `free_rent_day`;
ALTER TABLE `smart-park`.`con_contract_billing_plan`
    ADD COLUMN `fee_billing_scheme_name` varchar(255) NULL COMMENT '收费方案名称' AFTER `fee_billing_scheme_id`;

ALTER TABLE `smart-park`.`con_contract_process_record`

    MODIFY COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER `create_by`,
    MODIFY COLUMN `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' AFTER `update_by`,
    ADD COLUMN `extension` json NULL COMMENT '拓展' AFTER `remark`,
    MODIFY COLUMN `process_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程no' AFTER `operate`,
    MODIFY COLUMN `pre_process_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '上一节点流程no' AFTER `process_no`;

INSERT INTO `smart-park`.`com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`,
                                     `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`,
                                     `method_name`)
VALUES ('purchaseStateUpdate', '0 0/5 * * * ?', '采购服务状态值更新', NULL, 0, NULL, '', 1, 'park-industrial',
        'purchaseStateTask', 'stateUpdate');

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FEE_CALCULATION_STANDARD-5', NULL, 'FEE_CALCULATION_STANDARD', '计算标准', 'NUMBER_OF_ROOMS_MULTIPLY_UNIT_PRICE', '房间数*单价', 5, 3, '2021-12-14 10:57:30', NULL, 0);

UPDATE `smart-park`.`sys_dictionary`
SET sort = 5
WHERE id = 'FEE_CALCULATION_STANDARD-4';
UPDATE `smart-park`.`sys_dictionary`
SET sort = 4
WHERE id = 'FEE_CALCULATION_STANDARD-3';

ALTER TABLE `smart-park`.`com_multistage_category_relation`
ADD COLUMN `park_org_id` varchar(32) NULL COMMENT '园区Id' AFTER `business_id`;

