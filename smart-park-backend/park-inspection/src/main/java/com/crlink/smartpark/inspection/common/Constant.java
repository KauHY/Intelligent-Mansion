package com.crlink.smartpark.inspection.common;

public interface Constant {

    String INSPECTION_POINT_EXPORT_FILENAME = "巡检点管理";

    String TASK_REDIS_LOCK_FORMAT = "inspectionTask:%s";
    /**
     * 巡检类型
     */
    String INSPECTION_TYPE = "INSPECTION_TYPE";

    public enum EmployeeRelationEnum {
        INSPECTION_PLAN(1, "巡检计划"), INSPECTION_TASK(2, "巡检任务");
        public int type;
        String desc;

        public int type() {
            return this.type;
        }

        EmployeeRelationEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }


    /**
     * 巡检计划状态
     */
    enum InspectionPlanStatus {
        DELETED(-1, "删除状态"), CLOSED(0, "关闭状态"), OPENING(1, "开启");
        int status;
        String desc;

        public int status() {
            return status;
        }

        InspectionPlanStatus(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    /**
     * 巡检任务状态
     */
    enum InspectionTaskStatus {
        NOT_EXECUTE(1, "待执行"), EXECUTING(2, "巡检中"), ENDED(3, "已结束");
        public int status;
        String desc;

        public int status() {
            return status;
        }

        InspectionTaskStatus(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum TaskPointDetailStatus {
        POINT_NOT_EXECUTE(1, "待执行"),
        //POINT_EXECUTING(2, "巡检中"),
        POINT_ENDED(3, "已完成"), INCOMPLETE(4, "未完成"), TIMEOUT_END(5, "超时完成");
        public int status;
        String desc;

        TaskPointDetailStatus(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

    }
}
