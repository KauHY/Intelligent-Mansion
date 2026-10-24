ALTER TABLE `smart-park`.`sys_building`
    ADD COLUMN `remark` varchar(255) NULL COMMENT '备注' AFTER `park_org_id`,
    ADD COLUMN `property` tinyint NULL COMMENT '属性: 字典 BUILDING_PROPERTY 1办公、2食堂、3公寓、4其他' AFTER `remark`;

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('BUILDING_PROPERTY_1', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'OFFICE', '办公', 1, 1, '2022-03-22 11:23:16',
        '2022-03-22 11:28:29', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('BUILDING_PROPERTY_2', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'CANTEEN', '食堂', 2, 2, '2022-03-22 11:23:16',
        '2022-03-22 11:28:36', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('BUILDING_PROPERTY_3', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'APARTMENT', '公寓', 3, 3, '2022-03-22 11:23:16',
        '2022-03-22 11:28:43', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('BUILDING_PROPERTY_4', NULL, 'BUILDING_PROPERTY', '楼栋属性', 'OTHER', '其他', 4, 4, '2022-03-22 11:23:16',
        '2022-03-22 11:28:52', 0);


ALTER TABLE `smart-park`.`bus_business_info`
    MODIFY COLUMN `highlight` json NULL COMMENT '亮点描述 字典HIGHLIGHT 0智慧办公,1限时入住,2配套齐全,3交通便利,4临近商圈' AFTER `rent_or_sale`;

UPDATE `smart-park`.`bus_business_info`
SET `park_org_id`    = '1480794049307742210',
    `title`          = '招商啦！',
    `effect_type`    = 1,
    `deadline`       = NULL,
    `rent_or_sale`   = 1,
    `highlight`      = '[1]',
    `employee_id`    = '1483272971136012289',
    `employee_phone` = '15235243542',
    `title_picture`  = 'dmMtdXBsb2FkLTE2NDc2MTAyNDMzNjktMl8xNjQ3NjEwMzEwMDY0',
    `content`        = '<p>招商</p>',
    `address`        = '{\"name\": \"广东省深圳市福田区4号线/龙华线;9号线/梅林线\", \"latitude\": \"22.570459\", \"longitude\": \"114.059415\"}',
    `status`         = 1,
    `deleted`        = 0,
    `create_by`      = '1437679155261194242',
    `create_time`    = '2022-03-18 21:32:41',
    `update_by`      = '1437679155261194242',
    `update_time`    = '2022-03-18 21:32:55'
WHERE `id` = '1504813040786145281';



ALTER TABLE `smart-park`.`cus_order`
    MODIFY COLUMN `order_type_item` tinyint NOT NULL COMMENT '报事投诉类型 ORDERTYPE-1：11-环境、12-安全、13-秩序、14-工作人员、15-设备设施、16-供水、17-供电、18-消防、19-便民服务、20-其他	\r\n报修服务类型 ORDERTYPE-2：21-水电、22-门禁、23-电梯、24-公共设施、25-物业设备、26-其他\r\n' AFTER `order_type`;

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ORDER_STATE_1', NULL, 'ORDER_STATE', '报事报修状态', 'WAIT_ASSIGN', '待派单', 1, 1, '2022-03-22 16:51:53',
        '2022-03-22 17:41:05', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ORDER_STATE_2', NULL, 'ORDER_STATE', '报事报修状态', 'WAIT_HANDLE', '待处理', 2, 2, '2022-03-22 16:51:53',
        '2022-03-22 17:41:13', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ORDER_STATE_3', NULL, 'ORDER_STATE', '报事报修状态', 'FINISH', '已完成', 8, 4, '2022-03-22 16:51:53',
        '2022-03-22 17:41:23', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ORDER_STATE_4', NULL, 'ORDER_STATE', '报事报修状态', 'CANCEL', '已撤单', 9, 5, '2022-03-22 16:51:53',
        '2022-03-22 17:41:27', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('ORDER_STATE_5', NULL, 'ORDER_STATE', '报事报修状态', '10', '转办中', 10, 3, '2022-03-22 16:51:53',
        '2022-03-22 17:21:50', 0);


ALTER TABLE `smart-park`.`dev_meter`
    ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注' AFTER `status`;
ALTER TABLE `smart-park`.`dev_meter`
    MODIFY COLUMN `effect_type` tinyint NULL COMMENT '表计类别' AFTER `effect_property`;

ALTER TABLE `smart-park`.`bus_customer_info`
    ADD COLUMN `remark` varchar(255) NULL COMMENT '备注' AFTER `employee_id`;

ALTER TABLE `smart-park`.`dev_meter_reading_everyday_record`
    ADD COLUMN `import_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导入备注' AFTER `remark`;
ALTER TABLE `smart-park`.`dev_meter_reading_everyday_record`
    MODIFY COLUMN `corrected_reading` decimal (8, 2) NULL COMMENT '修正后读数' AFTER `reading_time`,
    ADD COLUMN `create_by` varchar (32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `remark`;

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                                           `create_time`, `update_time`, `deleted`)
VALUES ('JOB_CODE_15', NULL, 'JOBCODE', '岗位', 'TRANSFER_MAN', '转办员', 15, 15, '2022-02-21 15:26:27',
        '2022-02-21 16:17:55', 0);

ALTER TABLE `smart-park`.`cus_order_process_record`
    ADD COLUMN `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转办处理结果' AFTER `remark`;


ALTER TABLE `smart-park`.`cus_order_process_record`
DROP
COLUMN `process_no`,
MODIFY COLUMN `order_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工单id' AFTER `id`;


ALTER TABLE `smart-park`.`cus_order`
    ADD COLUMN `transfer_man` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转办员对应employeeId' AFTER `employee_id`;



