ALTER TABLE sys_employee add COLUMN card_no VARCHAR(100) COMMENT '人员卡号';
ALTER TABLE sys_employee add COLUMN device_person_id bigint COMMENT '设备人员ID';








ALTER TABLE vis_visitor_record add COLUMN card_no VARCHAR(100) COMMENT '人员卡号';
ALTER TABLE vis_visitor_record add COLUMN device_person_id bigint COMMENT '设备人员ID';

ALTER TABLE vis_access_policy DROP INDEX idx_access_policy_company_id;
ALTER TABLE vis_access_policy MODIFY company_id varchar(1000) COMMENT '企业ID集合';

alter table vis_access_record add COLUMN health_code int COMMENT '健康码，0：绿色 1：黄色 2：红色' after is_pass;
alter table vis_access_record add COLUMN body_temperature DOUBLE(4,1) COMMENT '体温' after is_pass;
ALTER table vis_access_record ADD COLUMN employee_id VARCHAR(32) COMMENT '员工ID' AFTER scene_image_url;

INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('HEALTH_CODE_1', NULL, 'HEALTH_CODE', '健康码', 'GREEN', '绿色', 0, 0, '2022-04-06 13:59:24', '2022-04-06 13:59:39', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('HEALTH_CODE_2', NULL, 'HEALTH_CODE', '健康码', 'YELLOW', '黄色', 1, 1, '2022-04-06 13:59:24', '2022-04-06 14:00:21', 0);
INSERT INTO `smart-park`.`sys_dictionary`(`id`, `parent_id`, `type`, `desc`, `code`, `label`, `value`, `sort`, `create_time`, `update_time`, `deleted`) VALUES ('HEALTH_CODE_3', NULL, 'HEALTH_CODE', '健康码', 'RED', '红色', 2, 2, '2022-04-06 13:59:24', '2022-04-06 14:00:23', 0);

ALTER table vis_access_record MODIFY COLUMN `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';
ALTER table vis_access_record MODIFY COLUMN `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间';
