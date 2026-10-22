/**
  员工表新增字段
 */
ALTER TABLE `smart-park`.`sys_employee`
    ADD COLUMN `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注' AFTER `pics`;

/**
  房屋表新增字段
 */
ALTER TABLE `smart-park`.`sys_house`
    ADD COLUMN `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注' AFTER `house_status`;


/**
  更新字典表房屋功能字段
 */
DELETE
FROM sys_dictionary
WHERE type = 'HOUSE_FUNCTION';

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HOUSE_FUNCTION_0', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_0', '办公', 0, 1);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HOUSE_FUNCTION_1', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_1', '商铺', 1, 2);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HOUSE_FUNCTION_2', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_2', '公寓-单间', 2, 3);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HOUSE_FUNCTION_3', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_3', '公寓-标间', 3, 4);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HOUSE_FUNCTION_4', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_4', '公寓-套间', 4, 5);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HOUSE_FUNCTION_5', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_5', '仓库', 5, 6);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`)
VALUES ('HOUSE_FUNCTION_6', NULL, 'HOUSE_FUNCTION', '房屋功能', 'HOUSE_FUNCTION_6', '其他', 6, 7);

/**
 企业信息—— 历史数据初始化同步 只执行一次
 */
update sys_company set source = '1'