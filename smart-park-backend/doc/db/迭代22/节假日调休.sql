-- 20240523 节假日调休表
DROP TABLE IF EXISTS `att_attendance_exchange_holiday`;
CREATE TABLE `att_attendance_exchange_holiday`
(
    `id`            int(10) NOT NULL AUTO_INCREMENT,
    `exchange_date` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节假日调休日期',
    `is_holiday`    tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否是休息日，0：否 1：是',
    `holiday_type`  int(4) NOT NULL DEFAULT 0 COMMENT '假期类型，0：工作日，1：元旦，2：春节，3：清明节，4：劳动节，5：端午节，6：中秋节，7：国庆节',
    `create_time`   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
    `update_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
    `deleted`       tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='节假日调休表';


INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2023-01-30', 1, 1);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2023-01-31', 1, 1);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-01-01', 1, 1);

INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-04', 0, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-10', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-11', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-12', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-13', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-14', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-15', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-16', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-17', 1, 2);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-02-18', 0, 2);

INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-04-04', 1, 3);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-04-05', 1, 3);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-04-06', 1, 3);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-04-07', 0, 3);

INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-04-28', 0, 4);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-05-01', 1, 4);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-05-02', 1, 4);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-05-03', 1, 4);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-05-04', 1, 4);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-05-05', 1, 4);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-05-11', 0, 4);

INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-06-08', 1, 5);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-06-09', 1, 5);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-06-10', 1, 5);

INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-09-14', 0, 6);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-09-15', 1, 6);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-09-16', 1, 6);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-09-17', 1, 6);

INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-09-29', 0, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-01', 1, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-02', 1, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-03', 1, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-04', 1, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-05', 1, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-06', 1, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-07', 1, 7);
INSERT INTO `att_attendance_exchange_holiday` (`exchange_date`, `is_holiday`, `holiday_type`) VALUES ('2024-10-12', 0, 7);
