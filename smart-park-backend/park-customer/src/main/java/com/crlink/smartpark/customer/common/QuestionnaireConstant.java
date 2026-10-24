package com.crlink.smartpark.customer.common;

public interface QuestionnaireConstant {
    String ANSWER_DETAILS_FILE_NAME = "分析报告答案详情";

    enum QuestionnaireStatusEnum {
        DESIGNED(1, "设计中"), COLLECTING(2, "收集中"), END(3, "已结束");
        public Integer status;
        private String desc;

        QuestionnaireStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum FormStatusEnum {
        DESIGNED(1, "待发布"), COLLECTING(2, "收集中"), END(3, "已结束");
        public Integer status;
        private String desc;

        FormStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }

    enum EmployeeAnsweredStatusEnum {
        NOT_FINISHED(0, "未完成"), FINISHED(1, "已完成"), ANSWERED_END(2, "已结束");
        public Integer status;
        private String desc;

        EmployeeAnsweredStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }
    }
}
