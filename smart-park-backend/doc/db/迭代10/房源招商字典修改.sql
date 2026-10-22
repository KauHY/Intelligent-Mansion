/**
  字典表-房源招商的房屋单位租金
 */
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'HOUSE_UNIT_RENT',
    `desc`        = '房源招商的房屋单位租金',
    `code`        = 'HouseUnitRent-1',
    `label`       = '不限',
    `value`       = 0,
    `sort`        = 1,
    `create_time` = '2021-11-18 17:19:51',
    `update_time` = '2022-02-16 17:24:50',
    `deleted`     = 0
WHERE `id` = 'HouseUnitRent-1';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'HOUSE_UNIT_RENT',
    `desc`        = '房源招商的房屋单位租金',
    `code`        = 'HouseUnitRent-2',
    `label`       = '50元/㎡/月以下',
    `value`       = 1,
    `sort`        = 2,
    `create_time` = '2021-11-18 17:19:51',
    `update_time` = '2022-02-16 17:24:52',
    `deleted`     = 0
WHERE `id` = 'HouseUnitRent-2';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'HOUSE_UNIT_RENT',
    `desc`        = '房源招商的房屋单位租金',
    `code`        = 'HouseUnitRent-3',
    `label`       = '50-100元/㎡/月',
    `value`       = 2,
    `sort`        = 3,
    `create_time` = '2021-11-18 17:19:51',
    `update_time` = '2022-02-16 17:24:54',
    `deleted`     = 0
WHERE `id` = 'HouseUnitRent-3';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'HOUSE_UNIT_RENT',
    `desc`        = '房源招商的房屋单位租金',
    `code`        = 'HouseUnitRent-4',
    `label`       = '100-200元/㎡/月',
    `value`       = 3,
    `sort`        = 4,
    `create_time` = '2021-11-18 17:19:51',
    `update_time` = '2022-02-16 17:24:55',
    `deleted`     = 0
WHERE `id` = 'HouseUnitRent-4';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'HOUSE_UNIT_RENT',
    `desc`        = '房源招商的房屋单位租金',
    `code`        = 'HouseUnitRent-5',
    `label`       = '200-500元/㎡/月',
    `value`       = 4,
    `sort`        = 5,
    `create_time` = '2021-11-18 17:19:51',
    `update_time` = '2022-02-16 17:24:58',
    `deleted`     = 0
WHERE `id` = 'HouseUnitRent-5';
UPDATE `smart-park`.`sys_dictionary`
SET `parent_id`   = NULL,
    `type`        = 'HOUSE_UNIT_RENT',
    `desc`        = '房源招商的房屋单位租金',
    `code`        = 'HouseUnitRent-6',
    `label`       = '500元/㎡/月以上',
    `value`       = 5,
    `sort`        = 6,
    `create_time` = '2021-11-18 17:19:51',
    `update_time` = '2022-02-16 17:25:02',
    `deleted`     = 0
WHERE `id` = 'HouseUnitRent-6';

