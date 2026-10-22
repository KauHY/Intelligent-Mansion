UPDATE `smart-park`.`sys_dictionary` SET `deleted` = 1 WHERE `id` = 'FEE_TYPE-2';

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FEE_TYPE-17', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'APARTMENT_RENT_SINGLE', '公寓租金-单间', 17, 17, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FEE_TYPE-18', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'APARTMENT_RENT_TWIN', '公寓租金-标间', 18, 18, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FEE_TYPE-19', 'FEE_TYPE-0', 'FEE_TYPE', '费用类型', 'APARTMENT_RENT_SUITE', '公寓租金-套间', 19, 19, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);

UPDATE `smart-park`.`sys_dictionary` SET `deleted` = 1 WHERE `id` = 'FEE_TYPE-6';

INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FEE_TYPE-20', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'DEPOSIT_SINGLE', '公寓押金-单间', 20, 20, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FEE_TYPE-21', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'DEPOSIT_TWIN', '公寓押金-标间', 21, 21, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);
INSERT INTO `smart-park`.`sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('FEE_TYPE-22', 'FEE_TYPE-5', 'FEE_TYPE', '费用类型', 'DEPOSIT_SUITE', '公寓押金-套间', 22, 22, '2021-09-10 16:34:08', '2021-12-07 15:37:44', 0);

UPDATE `smart-park`.`sys_dictionary` SET `value` = 0 WHERE `id` = 'FEE_RESERVED_DIGITS_1';
UPDATE `smart-park`.`sys_dictionary` SET `value` = 1 WHERE `id` = 'FEE_RESERVED_DIGITS_2';
UPDATE `smart-park`.`sys_dictionary` SET `value` = 2 WHERE `id` = 'FEE_RESERVED_DIGITS_3';
