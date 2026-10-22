-- Add separate attendance window for face check-in meetings.
ALTER TABLE mee_meeting_reserve
    ADD COLUMN attendance_start_time datetime DEFAULT NULL AFTER meeting_time_end,
    ADD COLUMN attendance_end_time datetime DEFAULT NULL AFTER attendance_start_time;
