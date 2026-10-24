USE smart-park;

ALTER TABLE mee_face_checkin_attendance
    ADD COLUMN participant_category tinyint DEFAULT NULL COMMENT '人员识别分类 1与会人员 2非与会人员 3非记录人员' AFTER remark;

ALTER TABLE mee_face_checkin_attendance
    MODIFY COLUMN person_id varchar(64) DEFAULT NULL;
