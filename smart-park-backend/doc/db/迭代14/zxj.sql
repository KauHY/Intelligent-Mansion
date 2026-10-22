ALTER TABLE `mee_meeting_manage`
    ADD COLUMN `screen_device_id` varchar(32) NULL COMMENT '会议屏设备，对应device_info表的id' AFTER `need_review`;

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_11', NULL, 'DEVICE_TYPE', '设备类型', 'MEETING_SCREEN', '会议室信息屏', 11, 11, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);


INSERT INTO `com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`,
                        `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`,
                        `method_name`)
VALUES ('controlScreenLight', '0 0/30 * * * ?', '会议信息屏灯光控制', '每半小时触发一次', 0, NULL, NULL, NULL, 'park-meeting',
        'meetingAuditTask', 'controlScreenLight');


INSERT INTO `com_task` (`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`,
                        `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`,
                        `method_name`)
VALUES ('updateScreenDeviceStatus', '0 0/10 * * * ?', '会议信息屏状态更新', '每10分钟触发一次', 0, NULL, NULL, NULL, 'park-device',
        'deviceInfoTask', 'updateScreenDeviceStatus');

delete
from `sys_dictionary`
where `type` = 'DEVICE_TYPE';

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_1', NULL, 'DEVICE_TYPE', '设备类型', 'ACCESS_CONTROL', '公共门禁', 1, 1, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_2', NULL, 'DEVICE_TYPE', '设备类型', 'APARTMENT_ACCESS_CONTROL', '公寓门禁', 2, 2, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_3', NULL, 'DEVICE_TYPE', '设备类型', 'BARRIER_ALL_IN_ONE', '道闸一体机', 3, 3, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_4', NULL, 'DEVICE_TYPE', '设备类型', 'VIDEO_MONITORING', '视频监控', 4, 4, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);


INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_5', NULL, 'DEVICE_TYPE', '设备类型', 'WATER_METER', '水表', 5, 5, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_6', NULL, 'DEVICE_TYPE', '设备类型', 'ELECTRICITY_METER', '电表', 6, 6, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_7', NULL, 'DEVICE_TYPE', '设备类型', 'MEETING_SCREEN', '会议室信息屏', 7, 7, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_8', NULL, 'DEVICE_TYPE', '设备类型', 'SMOKE_SENSING', '消防设备-烟感设备', 8, 8, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('DEVICE_TYPE_9', NULL, 'DEVICE_TYPE', '设备类型', 'LIQUID_WATER_LEVEL', '消防设备-液位设备', 9, 9, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);


INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('OFFLINE', NULL, 'SCREEN_DEVICE_STATUS', '会议室信息屏状态', 'OFFLINE', '离线', 0, 0, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('SLEEP', NULL, 'SCREEN_DEVICE_STATUS', '会议室信息屏状态', 'SLEEP', '休眠', 1, 1, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('RUNNING', NULL, 'SCREEN_DEVICE_STATUS', '会议室信息屏状态', 'RUNNING', '运行', 2, 2, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);


INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('SUSPENDED', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'SUSPENDED', '暂停使用', 0, 0, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('FREE', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'FREE', '空闲中', 1, 1, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('AHEAD', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'AHEAD', '即将开始', 2, 2, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);

INSERT INTO `sys_dictionary` (`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`,
                              `create_time`, `update_time`, `deleted`)
VALUES ('USING', NULL, 'MEETING_USED_STATE', '会议室使用状态', 'USING', '使用中', 3, 3, '2021-09-07 14:22:03',
        '2022-04-20 16:37:02', 0);


