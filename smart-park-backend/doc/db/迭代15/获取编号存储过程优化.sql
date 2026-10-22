DROP PROCEDURE IF EXISTS `getNumberSeq`;

DELIMITER |
CREATE PROCEDURE getNumberSeq (
    IN numberName VARCHAR ( 32 )) BEGIN
UPDATE com_number_seq
SET
    VALUE
        =
        IF
            ( VALUE >= 9999, 1000, VALUE + 1 )
WHERE
        number_name = numberName;
SELECT
    *
FROM
    com_number_seq
WHERE
        number_name = numberName;

END |
DELIMITER;