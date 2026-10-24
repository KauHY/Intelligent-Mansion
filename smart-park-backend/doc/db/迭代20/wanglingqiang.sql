-- 跟产品确认后修改“成功放行”为“识别通过”20221008
UPDATE sys_dictionary SET label='识别通过' WHERE id='ACCESS_RESULT_2';

-- 公寓门禁=2,20221009
UPDATE sys_dictionary SET `value`=2 WHERE id='ACCESS_DEVICE_TYPE_2';