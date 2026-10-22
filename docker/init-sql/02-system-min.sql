CREATE TABLE IF NOT EXISTS `sys_dictionary` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  `label` varchar(128) DEFAULT NULL,
  `value` int DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_sys_dictionary_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
