package com.crlink.smartpark.device.common;

/**
 * 微信常量类
 *
 */
public class MqttConstants {

    /**
     * 小程序前缀路径
     */
    public static final String DEVICE_CONTROL = "/pages/devControl/devControl?codeNo=";

    public static final String AREA_CONTROL = "/pages/areaControl/areaControl?codeNo=";

    /**
     * 网关上线上报通道(附带网关相关信息) 设备上行
     */
    public static final String GATEWAY_STATUS_UP = "vanhitech/up/online";
    /**
     * 网关上线上报通道(附带网关相关信息) 云端下行 +{gatewayId}
     */
    public static final String GATEWAY_STATUS_DOWN = "vanhitech/down/online/";


    /**
     * 网关设备添加删除修改和通知设备上线相关 设备上行 +{gatewayId}
     */
    public static final String DEVICE_UP = "vanhitech/up/property/5359/0/0/";
    /**
     * 网关设备添加删除修改和通知设备上线相关 云端下行 +{gatewayId}
     */
    public static final String DEVICE_DOWN = "vanhitech/down/property/5359/0/0/";

    /**
     * 设备状态上报和下发执行，设备远程控制,绑定解绑遥控器相关 设备上行 +{fcode}/{type}/{subtype}/{deviceId}
     */
    public static final String DEVICE_ALL_UP = "vanhitech/up/device/";
    /**
     * 设备状态上报和下发执行，设备远程控制,绑定解绑遥控器相关 云端下行 +{fcode}/{type}/{subtype}/{deviceId}
     */
    public static final String DEVICE_ALL_DOWN = "vanhitech/down/device/";

    /**
     * 情景增删改查执行服务 设备上行 +{gatewayId}
     */
    public static final String SCENE_UP = "vanhitech/up/scene/";
    /**
     * 情景增删改查执行服务 云端下行 +{gatewayId}
     */
    public static final String SCENE_DOWN = "vanhitech/down/scene/";

    /**
     * 定时器服务 设备上行 +{gatewayId}
     */
    public static final String TIMERTASK_UP = "vanhitech/up/timertask/";
    /**
     * 定时器服务 云端下行 +{gatewayId}
     */
    public static final String TIMERTASK_DOWN = "vanhitech/down/timertask/";

    /**
     * 触发器服务设备上行 +{gatewayId}
     */
    public static final String TRIGGER_UP = "vanhitech/up/trigger/";
    /**
     * 触发器服务 云端下行 +{gatewayId}
     */
    public static final String TRIGGER_DOWN = "vanhitech/down/trigger/";

    /**
     * 网关复制功能和权限操作 设备上行 +{gatewayId}
     */
    public static final String SERVICE_GATE_UP = "vanhitech/up/service/";
    /**
     * 网关复制功能和权限操作 云端下行 +{gatewayId}
     */
    public static final String SERVICE_GATE_DOWN = "vanhitech/down/service/";

    /*mqtt 方法类型*/
    /**
     * 设备状态上传
     */
    public static final String DEVICE_STATUS_METHOD = "report_status";

    public static final String DEVICE_ADD_METHOD = "report_add";

    public static final String DEVICE_DELETE_METHOD = "report_delete";

    //新增情景
    public static final String SCENE_ADD_DOWN = "add_scene";

    /**
     * 情景操作-下发
     */
    //新增情景
    public static final String SET_SYS_TIMER = "sys_timer_set";
    //修改情景
    public static final String SCENE_EDIT_DOWN = "edit_scene";
    //删除情景
    public static final String SCENE_DEL_DOWN = "del_scene";
    //获取单个情景
    public static final String SCENE_GET_ONE_DOWN = "get_scene";
    //获取情景列表
    public static final String SCENE_GET_LIST_DOWN = "get_all_scene";

    /**
     * 情景操作-设备回传
     */
    //新增情景
    public static final String SCENE_ADD_UP = "report_add_scene";
    //修改情景
    public static final String SCENE_EDIT_UP = "report_edit_scene";
    //删除情景
    public static final String SCENE_DEL_UP = "report_del_scene";
    //获取单个情景
    public static final String SCENE_GET_ONE_UP = "get_scene_reply";
    //获取情景列表
    public static final String SCENE_GET_LIST_UP = "get_all_scene_reply";

    /**
     * 情景设备操作-云端下发
     */
    //添加情景设备
    public static final String SCENE_DEVICE_ADD_DOWN = "add_scene_device";
    //根据情景码获取情景设备列表
    public static final String SCENE_DEVICE_QUERY_DOWN = "query_scene_device";
    //删除情景设备
    public static final String SCENE_DEVICE_DEL_DOWN = "del_scene_device";

    //执行情景
    public static final String SCENE_EXECUTE_DOWN = "execute_scene";

    //绑定情景面板
    public static final String SCENE_MATCH_PANEL_DOWN = "match_scene_panel";

    /**
     * 情景设备操作-设备回传
     */
    //添加情景设备
    public static final String SCENE_DEVICE_ADD_UP = "report_add_scene_device";
    //根据情景码获取情景设备列表
    public static final String SCENE_DEVICE_QUERY_UP = "query_scene_device_reply";
    //删除情景设备
    public static final String SCENE_DEVICE_DEL_UP = "report_del_scene_device";

    //绑定情景面板
    public static final String SCENE_MATCH_PANEL_UP = "match_scene_panel_reply";

    /**
     * 定时器操作-云端下发
     */
    //添加定时器
    public static final String TASK_TIME_ADD_DOWN = "add_timer_task";
    //修改定时器
    public static final String TASK_TIME_EDIT_DOWN = "edit_timer_task";
    //删除定时器
    public static final String TASK_TIME_DEL_DOWN = "del_timer_task";
    //获取定时器
    public static final String TASK_TIME_QUERY_UP = "query_timer_task";

    /**
     * 设备状态
     */
    public enum DeviceMqMethod {
        REPORT_STATUS (1, "状态", "report_status"),
        REPORT_ADD (2, "添加", "report_add"),
        REPORT_DELETE (3, "删除", "report_delete");

        private Integer code;
        private String name;
        private String value;

        DeviceMqMethod(Integer code, String name, String value) {
            this.code = code;
            this.name = name;
            this.value = value;
        }

        public static String getNameByCode(Integer code) {
            for (DeviceMqMethod ele : values ()) {
                if (ele.getCode ().equals (code)) {
                    return ele.getName ();
                }
            }
            return null;
        }

        public static Integer getCodeByValue(String value) {
            for (DeviceMqMethod ele : values ()) {
                if (ele.getValue ().equals (value)) {
                    return ele.getCode ();
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * MQTT消息类型
     */
    public enum MqttMsgType {
        PUBLISH (1, "发送"),
        SUBSCRIBE (2, "接收"),
        AUTOREPORTYPE (3, "主动上报");

        private Integer code;
        private String name;

        MqttMsgType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getNameByCode(Integer code) {
            for (MqttMsgType ele : values ()) {
                if (ele.getCode ().equals (code)) {
                    return ele.getName ();
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public Integer getCode() {
            return code;
        }
    }

    public static final String REPORT = "report";

    public static boolean checkReport(String method) {
        if (REPORT.equals (method.substring (0, 6))) {
            return true;
        }
        return false;
    }

    public enum MqttTopic {
        ONLINE ("online", "网关上线上报通道(附带网关相关信息)", 1),
        PROPERTY ("property", "网关设备添加删除修改和通知设备上线相关", 2),
        DEVICE ("device", "设备状态上报和下发执行相关", 3),
        SCENE ("scene", "情景增删改查执行服务", 4),
        TIMERTASK ("timertask", "定时器服务", 5),
        TRIGGER ("trigger", "触发器服务", 6),
        SERVICE ("service", "网关复制功能和权限操作", 7),
        ;
        private String code;
        private String name;
        //优先级
        private Integer level;

        MqttTopic(String code, String name, Integer level) {
            this.code = code;
            this.name = name;
            this.level = level;
        }

        public static String getNameByCode(String code) {
            for (MqttTopic ele : values ()) {
                if (ele.getCode ().equals (code)) {
                    return ele.getName ();
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public Integer getLevel() {
            return level;
        }
    }


    /**
     * @Description 网关指令映射
     */
    public enum MqttReplyMethod {
        ADD_SCENE (1, "add_scene", "add_scene_reply", "client_scene", "用户进行添加情景操作"),
        EDIT_SCENE (2, "edit_scene", "edit_scene_reply", "client_scene", "用户进行修改情景操作"),
        DEL_SCENE (3, "del_scene", "del_scene_reply", "client_scene", "用户进行删除情景操作"),
        GET_SCENE (4, "get_scene", "get_scene_reply", "client_scene", "用户获取单个情景"),
        GET_ALL_SCENE (5, "get_all_scene", "get_all_scene_reply", "client_scene", "用户获取情景列表"),
        ADD_SCENE_DEVICE (6, "add_scene_device", "add_scene_device_reply", "fk_scene_device", "根据情景ID进行添加情景设备"),
        QUERY_SCENE_DEVICE (7, "query_scene_device", "query_scene_device_reply", "fk_scene_device", "根据情景码获取情景设备列表"),
        DEL_SCENE_DEVICE (8, "del_scene_device", "del_scene_device_reply", "fk_scene_device", "根据情景设备ID进行删除情景设备"),
        MATCH_SCENE_PANEL (9, "match_scene_panel", "match_scene_panel_reply", "client_scene", "把情景面板上的按钮和情景进行绑定操作"),
        EXECUTE_SCENE (10, "execute_scene", "execute_scene_reply", "client_scene", "根据情景码执行情景"),

        ADD_TIMER_TASK (11, "add_timer_task", "add_timer_task_reply", "fk_task_mqtt", "用户进行添加定时器操作"),
        EDIT_TIMER_TASK (12, "edit_timer_task", "edit_timer_task_reply", "fk_task_mqtt", "用户进行修改定时器操作"),
        DEL_TIMER_TASK (13, "del_timer_task", "del_timer_task_reply", "fk_task_mqtt", "用户进行删除定时器操作"),
        QUERY_TIMER_TASK (14, "query_timer_task", "query_timer_task_reply", "fk_task_mqtt", "用户获取单个情景"),

        SYS_TIMER_SET (21, "sys_timer_set", "sys_timer_set_reply", null, "设置网关时间参数 "),

        ADD_TRIGGER (31, "trigger_add", "trigger_add_reply", "client_trigger", "添加触发器"),
        EDIT_TRIGGER (32, "trigger_edit", "trigger_edit_reply", "client_trigger", "编辑触发器"),
        DEL_TRIGGER (33, "trigger_delete", "trigger_delete_reply", "client_trigger", "删除触发器"),
        QUERY_TRIGGER (34, "trigger_query", "trigger_query_reply", "client_trigger", "获取触发器"),
        ADD_TRIGGER_CONDITION (35, "trigger_condition_add", "trigger_condition_add_reply", "client_trigger", "添加触发器"),
        EDIT_TRIGGER_CONDITION (36, "trigger_condition_edit", "trigger_condition_edit_reply", "client_trigger", "编辑触发器"),
        DEL_TRIGGER_CONDITION (37, "trigger_condition_delete", "trigger_condition_delete_reply", "client_trigger", "删除触发器"),
        QUERY_TRIGGER_CONDITION (38, "trigger_condition_query", "trigger_condition_query_reply", "client_trigger", "获取触发器"),

        ;

        private Integer code;
        //下发类型
        private String method;
        //上传类型
        private String reply;
        //说明
        private String message;
        //关联业务表
        private String prefix;

        MqttReplyMethod(Integer code, String method, String reply, String prefix, String message) {
            this.code = code;
            this.method = method;
            this.prefix = prefix;
            this.reply = reply;
            this.message = message;
        }

        public static String getReplyByMethod(String method) {
            for (MqttReplyMethod ele : values ()) {
                if (ele.getMethod ().equals (method)) {
                    return ele.getReply ();
                }
            }
            return null;
        }

        public static String getPrefixByMethod(String method) {
            for (MqttReplyMethod ele : values ()) {
                if (ele.getMethod ().equals (method)) {
                    return ele.getPrefix ();
                }
            }
            return null;
        }


        public static String getMethodByReply(String reply) {
            for (MqttReplyMethod ele : values ()) {
                if (ele.getReply ().equals (reply)) {
                    return ele.getMethod ();
                }
            }
            return null;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getMethod() {
            return method;
        }

        public Integer getCode() {
            return code;
        }

        public String getReply() {
            return reply;
        }

        public String getMessage() {
            return message;
        }

    }

    /**
     * MQTT消息类型
     */
    public enum MqttMethodReportType {
        ADD_SCENE (1, "report_add_scene", "设备上报新增情景通知"),
        EDIT_SCENE (2, "report_edit_scene", "设备上报修改情景通知"),
        DEL_SCENE (3, "report_del_scene", "设备上报删除情景通知"),
        ADD_SCENE_DEVICE (4, "report_add_scene_device", "设备上报添加情景设备通知"),
        DEL_SCENE_DEVICE (5, "report_del_scene_device", "设备上报删除情景设备通知"),
        EXECUTE_SCENE (6, "report_execute_scene", "设备通知执行情景"),

        ADD_TIMERTASK (11, "report_add_timer_task", "设备上报新增定时器通知"),
        EDIT_TIMERTASK (12, "report_edit_timer_task", "设备上报修改定时器通知"),
        DEL_TIMERTASK (13, "report_del_timer_task", "设备上报删除定时器通知"),


        ;

        private Integer code;
        private String name;
        private String value;

        MqttMethodReportType(Integer code, String value, String name) {
            this.code = code;
            this.name = name;
            this.value = value;
        }

        public static String getNameByCode(Integer code) {
            for (MqttMethodReportType ele : values ()) {
                if (ele.getCode ().equals (code)) {
                    return ele.getName ();
                }
            }
            return null;
        }

        public static String getValueByCode(Integer code) {
            for (MqttMethodReportType ele : values ()) {
                if (ele.getCode ().equals (code)) {
                    return ele.getValue ();
                }
            }
            return null;
        }


        public static Integer getCodeByValue(String value) {
            for (MqttMethodReportType ele : values ()) {
                if (ele.getValue ().equals (value)) {
                    return ele.getCode ();
                }
            }
            return 0;
        }


        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }
    }

    /**
     * 设备类型
     */
    public enum DeviceType {
        ONE(13, "灯具"),
        TWO(3, "开关器"),
        THREE(4, "窗帘"),
        THREE_(2, "有线开关"),
        FOUR(41, "控制设备"),
        FIVE(26, "感应设备"),
        SIX(193, "面板遥控器"),
        SIX_DOOR(26, "智能门磁"),
        SIX_TEM(18, "温湿度传感器"),
        SIX_POWER(51, "光感传感器"),
        SEVEN(42, "空调面板");


        private Integer code;
        private String name;

        DeviceType(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getNameByCode(Integer code) {
            for (DeviceType ele : values()) {
                if (ele.getCode().equals(code)) {
                    return ele.getName();
                }
            }
            return null;
        }

        public String getName() {
            return name;
        }

        public Integer getCode() {
            return code;
        }
    }


}
