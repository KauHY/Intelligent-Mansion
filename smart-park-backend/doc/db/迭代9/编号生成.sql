CREATE TABLE `com_number_seq`
(
    `number_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `date_time`   datetime DEFAULT CURRENT_TIMESTAMP,
    `value`       int      DEFAULT NULL,
    PRIMARY KEY (`number_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='编号生成器';

/*
存储过程
 */
DROP PROCEDURE IF EXISTS `getNumberSeq`;
DELIMITER;;
CREATE PROCEDURE getNumberSeq(IN numberName varchar (32))
BEGIN
UPDATE com_number_seq
SET value=value + 1
WHERE number_name = numberName;
SELECT *
FROM com_number_seq
WHERE number_name = numberName;
END;;
DELIMITER ;
