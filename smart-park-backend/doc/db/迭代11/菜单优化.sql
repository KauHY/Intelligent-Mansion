/**
  菜单字段优化
 */
ALTER TABLE `smart-park`.`sys_menu`
    MODIFY COLUMN `route_name` varchar (128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由名' AFTER `route`;
