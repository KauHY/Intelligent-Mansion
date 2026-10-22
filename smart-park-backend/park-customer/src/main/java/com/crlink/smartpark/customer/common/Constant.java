package com.crlink.smartpark.customer.common;

public interface Constant {

    String GOODS_HOUSE_NAME = "公寓仓库";

    String GOODS_FILE_NAME = "物品管理";

    String STOCK_RECORD_FILE_NAME = "出入库记录";

    String INVENTORY_FILE_NAME = "月度盘点管理";

    String GOODS_RELEASE_EXPORT_FILENAME = "物品放行";

    String MEETING_RESERVE_EXPORT_FILENAME = "订单管理";

    Integer ZERO = 0;
    String GOODS_MANAGER_NUMBER = "WP";

    String GOODS_RECEIVE_NO = "LY";

    String GOODS_STOCK_IN = "RK";

    String GOODS_STOCK_OUT = "CK";

    String GOODS_STOCK_UPDATE = "XG";

    String MEETING_RESERVE = "YD";

    /**
     * Order表的orderNo前缀，BS表示投诉，BX表示报修
     */
    String ORDER_NO_PREFIX_BX = "BX";
    String ORDER_NO_PREFIX_TS = "BS";

    /**
     * 处理异常工单时的创建人、原因
     */
    String CREATE_BY_SYSTEM = "0";
    String SYSTEM_RETURN_ORDER_REASON = "因员工不存在/岗位调整，系统自动退单";
    String CREATE_BY_SYSTEM_NAME = "系统自动操作";

    enum GoodsReleaseStatusEnum {
        NOT_CHECK(1, "待审核"), CHECK_PASS(2, "审核通过"), CHECK_REJECT(3, "审核不通过"), CANCEL(4, "取消"), AUTO_CANCEL(5, "超时取消");
        public int state;
        String desc;

        GoodsReleaseStatusEnum(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }
    }

    enum WxGoodsReleaseTagStatusEnum {
        ALL_TAG(0, "全部"), NOT_CHECK_TAG(1, "待审核"), CHECKED(2, "已审核");
        public int state;
        String desc;

        WxGoodsReleaseTagStatusEnum(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }
    }

    enum ProReceiveAuditState {
        NOT_RECEIVE(1, "领用待审核"),
        RECEIVE_PASS(2, "领用审核通过"),
        RECEIVE_REJECT(3, "领用审核不通过"),
        NOT_RETURN(4, "归还待审核"),
        RETURN_PASS(5, "归还审核通过"),
        RETURN_REJECT(6, "归还审核不通过"),
        CANCEL(7, "已取消");
        public int state;
        String desc;

        ProReceiveAuditState(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }
    }

    enum ProReceiveStockState {
        NOT_OUT(1, "未出库"),
        OUT_PASS(2, "已出库"),
        PART_IN(3, "部分已入库"),
        ALL_IN(4, "全部已入库");
        public int state;
        String desc;

        ProReceiveStockState(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }
    }

    enum MeetingPermissionType {
        COMPANY(1, "公司"),
        EMPLOYEE(2, "员工");

        public int state;
        String desc;

        MeetingPermissionType(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }
    }

    /**
     * 资讯状态
     */
    enum InfoStatusEnum {
        UN_EFFECT(0, "待生效"),
        EFFECTING(1, "已生效");
        public int status;
        String desc;

        InfoStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    /**
     * 公告状态
     */
    enum NoticeStatusEnum {
        UN_EFFECT(0, "待生效"),
        EFFECTING(1, "生效中"),
        OUT_OF_DATE(2, "已过期");

        public int status;
        String desc;

        NoticeStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum NoticePermissionTypeEnum {
        COMPANY(1, "公司"),
        EMPLOYEE(2, "员工");

        public int type;
        String desc;

        NoticePermissionTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    /**
     * 公告、资讯投放至小程序类型
     */
    enum putIntoTypeEnum {
        PUT_INTO_WX_C(1, "C端"),
        PUT_INTO_WX_B(2, "运营端"),
        PUT_INTO_WX_C_AND_B(3, "C端,运营端");

        public int type;
        String desc;

        putIntoTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    /**
     * 出入库方式
     */
    enum ProStockType {
        MANUAL_WAREHOUSE, //手动入库
        RETURN_WAREHOUSE, //归还入库
        IMPORT_WAREHOUSE, //导入入库
        MANUAL_DELIVERY, //手动出库
        RECEIPT_DELIVERY, //领用出库
        MODIFY_INVENTORY //修改库存
    }

    /**
     * 出入库操作类型
     */
    enum ProStockMode {
        STOCK_IN, //入库
        STOCK_OUT, //出库
        MODIFY_INVENTORY //修改库存
    }

    /**
     * 租赁会议室审核状态
     */
    enum CusMeetingAuditStatus {
        AUDIT_RENDING, //待审核
        AUDIT_PASS, //审核通过
        AUDIT_FAIL, //审核不通过
        CANCEL //已取消
    }

    enum EditorElementTypeEnum {
        INPUT, TEXTAREA, RADIO, CHECKBOX, UPLOAD;
    }

}
