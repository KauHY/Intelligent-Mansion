INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('DEVICE_TYPE_8', NULL, 'DEVICE_TYPE', '设备类型', 'APARTMENT_ACCESS_CONTROL', '公寓门禁', 8, 8, '2021-09-07 14:22:03', '2022-04-07 15:04:10', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('DEVICE_TYPE_9', NULL, 'DEVICE_TYPE', '设备类型', 'BARRIER_ALL_IN_ONE', '道闸一体机', 9, 9, '2021-09-07 14:22:03', '2022-04-07 15:28:31', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('DEVICE_TYPE_10', NULL, 'DEVICE_TYPE', '设备类型', 'VIDEO_MONITORING', '视频监控', 10, 10, '2021-09-07 14:22:03', '2022-04-07 15:28:31', 0);

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ACCESS_DEVICE_TYPE_1', NULL, 'ACCESS_DEVICE_TYPE', '门禁设备类型', 'ACCESS_CONTROL', '公共门禁', 1, 1, '2021-09-07 14:22:03', '2022-04-07 15:03:46', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('ACCESS_DEVICE_TYPE_2', NULL, 'ACCESS_DEVICE_TYPE', '门禁设备类型', 'APARTMENT_ACCESS_CONTROL', '公寓门禁', 8, 2, '2021-09-07 14:22:03', '2022-04-07 15:09:02', 0);
UPDATE `smart-park`.`sys_dictionary` SET label = '公共门禁' where id = 'DEVICE_TYPE_1';

alter TABLE `smart-park`.`dev_device_info` add COLUMN location VARCHAR(300) comment '设备位置';
alter TABLE `smart-park`.`dev_device_info` add COLUMN company_id VARCHAR(32) comment '企业ID';
alter TABLE `smart-park`.`dev_device_info` add COLUMN `channel_nos` json DEFAULT NULL COMMENT '通道号集合（json数组）';
ALTER TABLE `smart-park`.`dev_device_info` MODIFY device_type int DEFAULT NULL COMMENT '设备类型：1-公共门禁；2-车辆识别设备；3-道闸；4-智能盒子；5-NVR；6-人证核验机；7-抓拍机；8-公寓门禁 9-道闸一体机 10-视频监控';
ALTER TABLE `smart-park`.`dev_device_info` MODIFY park_org_id VARCHAR(64) DEFAULT NULL COMMENT '园区组织ID';
alter TABLE `smart-park`.`dev_device_info` add COLUMN device_category int comment '设备大类，0：门禁';
alter TABLE `smart-park`.`vis_access_policy` add COLUMN expire TINYINT NOT NULL DEFAULT 0 comment '已过期，0：否 1：是';

INSERT INTO `smart-park`.`com_task`(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('refreshAccessPolicy', '0 10 0 * * ?', '通行策略过期刷新', NULL, 0, NULL, NULL, NULL, 'park-visitor', 'accessPolicySchedule', 'refreshAccessPolicy');
INSERT INTO `smart-park`.`com_task`(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('refreshApartmentPerson', '0 20 0 * * ?', '公寓人员过期刷新', NULL, 0, NULL, NULL, NULL, 'park-visitor', 'apartmentPersonSchedule', 'refreshApartmentPerson');
