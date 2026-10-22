ALTER TABLE `smart-park`.`con_rent_contract_fee`
ADD COLUMN `house_messages` json DEFAULT NULL COMMENT '房源对象' AFTER `user_id`;

ALTER TABLE `smart-park`.`fee_billing_scheme`
ADD COLUMN `fee_type` int DEFAULT NULL COMMENT '费用类型' AFTER `status`;

UPDATE com_task SET bean_name = 'rentContractOutTask' where id = 'rentFeeUpdate';
INSERT INTO `smart-park`.`com_task`(`id`, `cron`, `task_name`, `description`, `closed`, `pool_size`, `thread_name_prefix`, `remove_on_cancel_policy`, `application_name`, `bean_name`, `method_name`) VALUES ('rentFeeLateCreate', '0 10 0 * * ?', '滞纳金生成', '每天凌晨0:10启动', 0, NULL, '', 1, 'park-contract', 'rentContractLateTask', 'rentFeeLateCreate');
