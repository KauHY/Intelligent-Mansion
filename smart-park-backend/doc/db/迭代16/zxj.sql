ALTER TABLE `dev_meter_consumption`
    ADD COLUMN `contract_id` varchar(32) NOT NULL COMMENT '合同ID' AFTER `id`;


ALTER TABLE `dev_meter_warning_record`
    CHANGE COLUMN `meter_id` `device_id` varchar (32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL AFTER `id`;
