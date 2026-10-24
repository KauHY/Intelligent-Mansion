package com.crlink.smartpark.common.constants;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @description: 字典类型及字典项，字典类型：枚举类名，字典项：枚举名
 */
public class DictConstant {

    /**
     * 是或否
     */
    /**
     * 车辆类型
     */
    public static final String YES_OR_NO = "YES_OR_NO";

    public static final String CAR_TYPE = "CAR_TYPE";
    public static final String PARK_TYPE = "PARK_TYPE";
    /**
     * 车辆状态
     */
    public static final String CAR_STATUS = "CAR_STATUS";
    /**
     * 设备类型
     */
    public static final String DEVICE_TYPE = "DEVICE_TYPE";
    /**
     * 设备状态
     */
    public static final String DEVICE_STATUS = "DEVICE_STATUS";
    /**
     * 入口出口类型
     */
    public static final String GATEWAY_TYPE = "GATEWAY_TYPE";
    /**
     * 车行通行结果
     */
    public static final String ACCESS_RESULT = "ACCESS_RESULT";
    /**
     * 入驻企业绑定客户端类型
     */
    public static final String COMPANY_BIND_CLIENT_TYPE = "COMPANY_BIND_CLIENT_TYPE";
    /**
     * 行人策略访客通行
     */
    public static final String ALLOW_VISITOR = "ALLOW_VISITOR";
    /**
     * 行人策略状态
     */
    public static final String POLICY_STATUS = "POLICY_STATUS";
    /**
     * 行人识别结果
     */
    public static final String RECOGNIZE_RESULT = "RECOGNIZE_RESULT";
    /**
     * 访客流程类型
     */
    public static final String VISIT_PROCESS_NODE = "VISIT_PROCESS_NODE";
    /**
     * 访客类型
     */
    public static final String VISITOR_TYPE = "VISITOR_TYPE";
    /**
     * 来访事项
     */
    public static final String VISIT_REASON = "VISIT_REASON";

    /**
     * 登入类型
     */
    public static final String SIGN_IN_STATUS = "SIGN_IN_STATUS";
    /**
     * 访客流程状态
     */
    public static final String VISIT_PROCESS_STATUS = "VISIT_PROCESS_STATUS";
    /**
     * 审核人类型
     */
    public static final String AUDIT_PERSON_TYPE = "AUDIT_PERSON_TYPE";
    /**
     * 登记类型
     */
    public static final String REGISTER_TYPE = "REGISTER_TYPE";

    /**
     * 线索来源
     */
    public static final String THREAD_SOURCE = "THREAD_SOURCE";

    /**
     * 商机阶段
     */
    public static final String BUSINESS_CHANCE_STAGE = "BUSINESS_CHANCE_STAGE";

    /**
     * 商机来源
     */
    public static final String BUSINESS_SOURCE = "BUSINESS_SOURCE";

    /**
     * 房源招商状态
     */
    public static final String BUSINESS_INFO_STATUS = "BUSINESS_STATUS";

    public static final String BUSINESS_INFO_TYPE = "EFFECT_TYPE";

    /**
     * 企业经营状态
     */
    public static final String COMPANY_MANAGEMENT_STATUS = "COMPANY_MANAGEMENT_STATUS";
    /**
     * 企业纳税类型
     */

    public static final String COMPANY_TAXES_TYPE = "COMPANY_TAXES_TYPE";
    /**
     * 客户状态
     */

    public static final String CUSTOMERINFO_STATUS = "CUSTOMERINFO_STATUS";
    /**
     * 客户类型
     */

    public static final String CUSTOMERINFO_TYPE = "CUSTOMERINFO_TYPE";
    /**
     * 行业领域
     */

    public static final String INDUSTRY_FIELD = "INDUSTRY_FIELD";

    /**
     * 打卡类型
     */

    public static final String PUNCH_TYPE = "PUNCH_TYPE";
    public static final String HEALTH_CODE = "HEALTH_CODE";


    /**
     * 是或否
     */
    public enum YesOrNo {
        YES, NO
    }

    /**
     * 车辆类型
     */
    public enum CarType {
        EMPLOYEE_CAR, OFFICIAL_CAR, EXTERNAL_CAR, RED_LIST_CAR
    }

    /**
     * 停车类型：1员工车辆,2公务车辆,3外部车辆
     */
    public enum ParkType {
        EMPLOYEE_CAR(1), OFFICIAL_CAR(2), EXTERNAL_CAR(3);

        private Integer code;

        ParkType(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }
    }

    /**
     * 车辆状态 0-未生效,1-已生效，2-已失效
     */
    public enum CarStatus {
        NOT_ACTIVE, ACTIVATING, INVALIDATION
    }

    /**
     * 设备类型
     */
    public enum DeviceType {
        ACCESS_CONTROL(1, "公共门禁"), APARTMENT_ACCESS_CONTROL(2, "公寓门禁"),
        BARRIER_ALL_IN_ONE(3, "道闸一体机"),
        VIDEO_MONITORING(4, "视频监控"),
        WATER_METER(5, "水表"), ELECTRICITY_METER(6, "电表"),
        MEETING_SCREEN(7, "会议信息屏"),
        SMOKE_SENSING(8, "消防设备-烟感设备"),
        LIQUID_WATER_LEVEL(9, "消防设备-液位设备"),
        SMART_LOCK(10, "智能锁");

        public Integer type;
        public String desc;

        DeviceType() {
        }

        DeviceType(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }

    public enum ScreenDeviceStatus {
        OFFLINE, SLEEP, RUNNING
    }

    /**
     * 设备状态
     */
    public enum DeviceStatus {
        OFFLINE(0), ONLINE(1);
        public int status;

        DeviceStatus(int status) {
            this.status = status;
        }
    }

    /**
     * 入口出口类型，0入口1出口
     */
    public enum GatewayType {
        ENTRANCE, EXIT
    }

    /**
     * 车行通行结果
     */
    public enum AccessResult {
        FAIL, SUCCESS
    }

    /**
     * 入驻企业绑定客户端类型
     */
    public enum CompanyBindClientType {
        UNBIND, APPID, H5
    }

    /**
     * 行人策略访客通行
     */
    public enum AllowVisitor {
        NOT_ALLOW, ALLOW
    }

    /**
     * 行人策略状态
     */
    public enum PolicyStatus {
        DISABLED, ENABLED
    }

    /**
     * 行人识别结果
     */
    public enum RecognizeResult {
        FAIL(0, "识别不通过"),
        SUCCESS(1, "识别通过");

        @Getter
        private Integer code;
        @Getter
        private String label;

        RecognizeResult(Integer code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getRecognizeResultNameByCode(int code) {
            for (RecognizeResult value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    /**
     * 访客流程类型
     */
    public enum VisitSourceType {
        SUBSCRIBE, INVITE, REGISTER
    }

    /**
     * 访客流程节点
     */
    public enum VisitProcessNode {
        VISIT_SUBSCRIBE, AUDIT, INVITE, COMPLETE_INFO, ID_CARD_VERIFY
    }

    /**
     * 来访事项
     */
    public enum VisitReason {
        BUSINESS, INTERVIEW, OTHER
    }

    /**
     * 访客登记类型
     */
    public enum RegisterType {
        ONSITE_REG, DOOR_ACCESS
    }

    /**
     * 登入类型
     */
    public enum SignInStatus {
        NOT_LOGIN_IN, LOGIN_IN, LOGIN_OUT
    }

    /**
     * 0审核中/邀请中，1审核通过/邀请成功，2审核不通过，3已取消，4登记/门禁核验，5签出
     */
    public enum ProcessStatus {
        ZERO, ONE, TWO, THREE, FOUR, FIVE
    }

    /**
     * 预约访客流程状态
     */
    public enum SubscribeProcessStatus {
        AUDITING, AUDIT_PASS, AUDIT_NOT_PASS, CANCEL
    }

    /**
     * 邀请访客流程状态
     */
    public enum InviteProcessStatus {
        INVITING, INVITING_SUCCESS
    }

    /**
     * 考勤规则关联类型
     */
    public enum AttendanceRuleApplyType {
        COMPANY, EMPLOYEE
    }

    /**
     * 考勤记录结果
     */
    public enum AttendanceResultType {
        NORMAL, EXCEPTION
    }

    /**
     * 考勤规则关联工作日类型
     */
    public enum AttendanceWorkdayType {
        LEGAL, CUSTOM
    }

    /**
     * 合同类型
     */
    public enum ContractType {
        ADMISSION_AGREEMENT(1, "入园协议"),//入园协议
        INVITATION_BID(2, "招引合同"),//招引合同
        RENTAL_CONTRACT(3, "房屋租赁合同"),//房屋租赁合同
        PROPERTY_CONTRACT(4, "物业合同"),//物业合同
        APARTMENT_CONTRACT(5, "公寓合同");//公寓合同

        @Getter
        private final int code;
        @Getter
        private final String label;

        ContractType(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getContractTypeNameByCode(int code) {
            for (ContractType value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }

        public static ContractType getContractTypeByCode(int code) {
            for (ContractType value : values()) {
                if (code == value.code) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * 协议属性
     */
    public enum ProtocolAttributes {
        NEW_PROTOCOL(1, "新签协议"),//新签协议
        RENEW_PROTOCOL(2, "续签协议"),//续签协议
        CHANGE_PROTOCOL(3, "变更协议"),//变更协议
        WITHDRAWAL_PROTOCOL(4, "退约协议"),//退约协议
        HISTORICAL_PROTOCOL(5, "历史协议");//历史协议

        @Getter
        private final int code;
        @Getter
        private final String label;

        ProtocolAttributes(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getProtocolAttributesNameByCode(int code) {
            for (ProtocolAttributes value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    /**
     * 合同属性
     */
    public enum ContractAttributes {
        NEW_CONTRACT(1, "新签合同"),//新签合同
        RENEW_CONTRACT(2, "续签合同"),//续签合同
        CHANGE_CONTRACT(3, "变更合同"),//变更合同
        WITHDRAWAL_CONTRACT(4, "退约合同"),//退约合同
        HISTORICAL_CONTRACT(5, "历史合同");//历史合同

        @Getter
        private final int code;
        @Getter
        private final String label;

        ContractAttributes(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getContractAttributesNameByCode(int code) {
            for (ContractAttributes value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    /**
     * 合同操作
     */
    public enum ContractOperate {
        EDIT(1, "编辑"),//编辑
        DELETE(2, "删除"),//删除
        NULLIFY(3, "作废"),//作废
        INITIATE_REVIEW(4, "发起审核"),//发起审核
        CHANGE(5, "变更"),//变更
        EXTEND(6, "续约"),//续约
        WITHDRAW(7, "退约"),//退约
        TERMINATION(8, "终止"),//终止
        AUDIT(9, "审批");//审批

        @Getter
        private final int code;
        @Getter
        private final String label;

        ContractOperate(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 房屋关联类型
     */
    public enum HouseRelationType {
        CONTRACT(1, "合同关联房屋"),//合同关联房屋
        FEE_BILLING(2, "计费信息应用房源");//计费信息应用房源

        @Getter
        private final int code;
        @Getter
        private final String label;

        HouseRelationType(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 合同状态
     */
    public enum ContractState {
        TO_BE_SUBMITTED(1, "待提交"),//待提交
        UNDER_REVIEW(2, "审核中"),//审核中
        TO_BE_EFFECTIVE(3, "待生效"),//待生效
        IN_EFFECT(4, "生效中"),//生效中
        WITHDRAWN(5, "已退约"),//已退约
        INVALIDATION(6, "已失效"),//已失效
        ABOLISHED(7, "已作废"),//已作废
        EXPIRED(8, "已过期"),//已过期
        UNDER_REVIEW_WITHDRAW(9, "退约审核中");//退约审核中

        @Getter
        private final int code;
        @Getter
        private final String label;

        ContractState(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getContractStateNameByCode(int code) {
            for (ContractState value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    /**
     * 合同变更类型
     */
    public enum ContractChangeType {
        SUBJECT_CHANGE(1, "主体变更"),//主体变更
        LEASE_ADJUSTMENT(2, "租期调整"),//租期调整
        ASSET_CHANGES(3, "资产变更"),//资产变更
        BILLING_CHANGES(4, "计费变更");//计费变更

        @Getter
        private final int code;
        @Getter
        private final String label;

        ContractChangeType(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 计费周期
     */
    public enum FeeBillingCycle {
        ONE_TIME(1, "一次性"),//一次性
        MONTHLY(2, "月"),//月
        QUARTERLY(3, "季"),//季
        YEARLY(4, "年");//年

        @Getter
        private final int code;
        @Getter
        private final String label;

        FeeBillingCycle(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 保留位数
     */
    public enum FeeReservedDigits {
        ROUNDING(0, "取整数"),//取整数
        KEEP_ONE_DECIMAL_PLACE(1, "保留一位小数"),//保留一位小数
        KEEP_TWO_DECIMAL_PLACES(2, "保留两位小数");//保留两位小数

        @Getter
        private final int code;
        @Getter
        private final String label;

        FeeReservedDigits(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 进位方式
     */
    public enum FeeCarryMode {
        ROUND_HALF_UP(4, "四舍五入"),//四舍五入
        ROUND_UP(0, "向上取整"),//向上取整
        ROUND_DOWN(1, "向下取整");//向下取整

        @Getter
        private final int code;
        @Getter
        private final String label;

        FeeCarryMode(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 公式类型
     */
    public enum FeeFormulaType {
        FIXED_AMOUNT(1, "固定金额"),//固定金额
        CALCULATION_FORMULA(2, "计算公式");//计算公式

        @Getter
        private final int code;
        @Getter
        private final String label;

        FeeFormulaType(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }


    /**
     * 收费类型
     */
    public enum FeeChargeType {
        PREPAID(1, "预付费"),//预付费
        POSTPAID(2, "后付费");//后付费

        @Getter
        private final int code;
        @Getter
        private final String label;

        FeeChargeType(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }


    /**
     * 收费周期
     */
    public enum FeeChargingCycle {
        CONSISTENT_WITH_THE_BILLING_CYCLE(1, "与计费周期一致"),//与计费周期一致
        MULTIPLE_BILLING_CYCLES(2, "多计费周期");//多计费周期

        @Getter
        private final int code;
        @Getter
        private final String label;

        FeeChargingCycle(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 计算标准：1-建筑面积*月单价；2-套内面积*月单价；3-用量*单价；4-天单价；5-房间数*单价
     */
    public enum FeeCalculationStandard {
        BUILDING_AREA_MULTIPLY_MONTHLY_UNIT_PRICE(1, "建筑面积*月单价"),//建筑面积*月单价
        COVER_AREA_MULTIPLY_MONTHLY_UNIT_PRICE(2, "套内面积*月单价"),//套内面积*月单价
        DOSAGE_MULTIPLY_UNIT_PRICE(3, "用量*单价"),//用量*单价
        PRICE_PER_DAY(4, "天单价"),//天单价
        NUMBER_OF_ROOMS_MULTIPLY_UNIT_PRICE(5, "房间数*单价");//天单价

        @Getter
        private final int code;
        @Getter
        private final String label;

        public static int[] getAllCode() {
            int[] codes = new int[values().length];
            for (int i = 0; i < values().length; i++) {
                codes[i] = values()[i].code;
            }
            return codes;
        }

        public static FeeCalculationStandard getStandardByCode(int code) {
            for (FeeCalculationStandard value : values()) {
                if (code == value.code) {
                    return value;
                }
            }
            return null;
        }

        public static List<FeeCalculationStandard> getStandardByCode(int[] code) {
            List<FeeCalculationStandard> feeCalculationStandards = Lists.newArrayList();
            for (FeeCalculationStandard value : values()) {
                for (int i : code) {
                    if (i == value.code) {
                        feeCalculationStandards.add(value);
                    }
                }

            }
            return feeCalculationStandards;
        }

        FeeCalculationStandard(int code, String label) {
            this.code = code;
            this.label = label;
        }
    }

    /**
     * 打卡类型
     */
    public enum PunchType {
        NORMAL(1, "上下班打卡"),
        FIELDWORK(2, "外勤打卡");

        @Getter
        private int code;
        @Getter
        private String label;

        PunchType(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getPunchTypeNameByCode(int code) {
            for (PunchType value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    /**
     * 外勤申请状态
     */
    public enum FieldworkApplyState {
        TO_BE_AUDITED(1, "待审批"),
        APPROVE(2, "审核通过"),
        REJECTED(3, "审核不通过"),
        CANCEL(4, "已取消");

        @Getter
        private int code;
        @Getter
        private String label;

        FieldworkApplyState(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getFieldworkApplyStateByCode(int code) {
            for (FieldworkApplyState value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum AttendanceResult {
        NORMAL(1, "考勤正常"),
        EXCEPTION(2, "考勤异常");

        @Getter
        private int code;
        @Getter
        private String label;

        AttendanceResult(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getAttendanceResultByCode(int code) {
            for (AttendanceResult value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum EquipmentLedgerResult {
        NORMAL(1, "正常"),
        SCRAPED(0, "已报废");

        @Getter
        private Integer code;
        @Getter
        private String label;

        EquipmentLedgerResult(Integer code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getEquipmentLedgerResultByCode(Integer code) {
            for (EquipmentLedgerResult value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum EnterprisePublishState {
        TO_BE_PUBLISHED(0, "待发布"),
        PUBLISHED(1, "已发布"),
        DISABLE(2, "停用");

        @Getter
        private int code;
        @Getter
        private String label;

        EnterprisePublishState(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getPublishStateByCode(int code) {
            for (EnterprisePublishState value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum SupplyPublishState {
        TO_BE_PUBLISHED(0, "待发布"),
        PUBLISHED(1, "已发布"),
        HAS_BEEN_REMOVED(2, "已下架");

        @Getter
        private int code;
        @Getter
        private String label;

        SupplyPublishState(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getPublishStateByCode(int code) {
            for (SupplyPublishState value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum PurchaseState {
        TO_BE_PUBLISHED(0, "待发布"),
        IN_PROGRESS(1, "进行中"),
        HAS_BEEN_REMOVED(2, "已下架"),
        EXPIRED(3, "已过期");

        @Getter
        private int code;
        @Getter
        private String label;

        PurchaseState(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getPurchaseStateByCode(int code) {
            for (PurchaseState value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum Payment {
        UNPAID(0, "未支付"),
        PAID(1, "已支付");

        @Getter
        private int code;
        @Getter
        private String label;

        Payment(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getPaymentByCode(int code) {
            for (Payment value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum Contribute {
        UNPAID(0, "未缴纳"),
        PAID(1, "已缴纳");

        @Getter
        private int code;
        @Getter
        private String label;

        Contribute(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getContributeByCode(int code) {
            for (Contribute value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    public enum AddDataSource {
        ENTERPRISE_ADDED(2, "企业申请"),
        ADMIN_ADDED(1, "管理员录入");

        @Getter
        private int code;
        @Getter
        private String label;

        AddDataSource(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getAddDataSourceByCode(int code) {
            for (AddDataSource value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    @Getter
    @AllArgsConstructor
    public enum HealthCode {
        GREEN(0, "绿色"),
        YELLOW(1, "黄色"),
        RED(2, "红色");
        Integer code;
        String label;
    }

    @Getter
    @AllArgsConstructor
    public enum DeviceCategory {
        DOOR_ACCESS(0, "门禁");
        Integer code;
        String label;
    }

    public enum AuditResult {
        PASS(1, "通过"),
        NOT_PASS(2, "不通过");

        @Getter
        private int code;
        @Getter
        private String label;

        AuditResult(int code, String label) {
            this.code = code;
            this.label = label;
        }

        public static String getAuditResultByCode(int code) {
            for (AuditResult value : values()) {
                if (code == value.code) {
                    return value.label;
                }
            }
            return "";
        }
    }

    /**
     * 费用类型
     */
    public enum FeeType {
        RENT,//租金
        HOUSE_RENT,//房屋租金
        WATER_METER,//水表
        SHARED_ELECTRICITY_CHARGE,//公摊电费
        SHARED_WATER_CHARGE,//公摊水费
        OTHERS,//其他
        ADVERTISING_EXPENSES,//广告费
        PARKING_EXPENCES,//停车费
        DECORATION_EXPENCES,//装修费
        APARTMENT_RENT_SINGLE,//公寓租金-单间
        APARTMENT_RENT_TWIN,//公寓租金-标间
        APARTMENT_RENT_SUITE,//公寓租金-套间
        APARTMENT_RENT,//公寓租金
        DEPOSIT_SINGLE,//公寓押金-单间
        DEPOSIT_TWIN,//公寓押金-标间
        DEPOSIT_SUITE,//公寓押金-套间
        PROPERTY,//物业
        PROPERTY_FEE,//物业费
        DEPOSIT,//押金
        FRONT_MONEY,//定金
        PROMISED_MONEY,//履约保证金
        METER_READING,//抄表
        ELECTRICITY_METER//电表
    }
}
