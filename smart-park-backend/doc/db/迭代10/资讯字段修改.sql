/**
  cus_information表字段修改
 */
ALTER TABLE `smart-park`.`cus_information`
    CHANGE COLUMN `info_type` `type` tinyint NOT NULL COMMENT '类型' AFTER `title`,
    MODIFY COLUMN `content_type` tinyint NOT NULL COMMENT '内容类型：0 内容, 1超链接' AFTER `content`,
    COMMENT = '资讯信息表';
