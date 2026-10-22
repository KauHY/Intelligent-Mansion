package com.crlink.smartpark.meeting.common;

/** * @Description
 * @Date
 */
public interface Constant {

    Integer ZERO = 0;

    Integer ONE = 1;

    Integer TWO = 2;

    public enum ReserveTypeEnum {
        RESERVE(1, "预订"), CANDIDATE(2, "候补");
        public int type;
        String desc;

        public int type() {
            return this.type;
        }

        ReserveTypeEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    public enum ReserveStateEnum {
        RESERVE_NO(1, "不可预定"), RESERVE_YES(2, "可预订"), RESERVE_OVER(3, "已预定"), CANDIDATE_YES(4, "可候补");
        public int type;
        String desc;

        public int type() {
            return this.type;
        }

        ReserveStateEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    public enum ReserveStatusEnum {
        RESERVE_1(1, "预定待审核"),
        RESERVE_2(2, "预定成功"),
        RESERVE_3(3, "候补待审核"),
        CANDIDATE_4(4, "候补中"),
        CANDIDATE_5(5, "手动取消"),
        CANDIDATE_6(6, "超时取消"),
        RESERVE_7(7, "审核失败");
        public int type;
        String desc;

        public int type() {
            return this.type;
        }

        ReserveStatusEnum(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    public enum ReviewStatusEnum {
        RESERVE_NOT(0, "未审核"), REVIEW_YES(1, "已审核");
        public int type;
        String desc;

        public int type() {
            return this.type;
        }

        ReviewStatusEnum(int type, String desc) {
            this.type = type;
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

    enum MeetingManageStatus {
        CLOSED(0), OPENED(1);
        public int status;

        MeetingManageStatus(int status) {
            this.status = status;
        }
    }

    enum MeetingUsedState {
        SUSPENDED(0, "暂停使用"), FREE(1, "空闲中"), AHEAD(2, "即将开始"), USING(3, "使用中");
        public int state;
        private String desc;


        MeetingUsedState(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }
    }

}
