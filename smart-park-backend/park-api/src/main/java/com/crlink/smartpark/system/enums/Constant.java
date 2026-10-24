package com.crlink.smartpark.system.enums;

import java.util.HashMap;
import java.util.Map;

public interface Constant {
    //    String CacheKeyGenerator = "cacheKeyGenerator";
//    String CACHE_GROUP_ORGANIZATION = "organization";
//    String CACHE_GROUP_MENU = "menu";
//    String CACHE_GROUP_USER = "user";
//    String CACHE_GROUP_ROLE = "role";
//    String CACHE_GROUP_COMMON = "common";
//    String CACHE_GROUP_EMPLOYEE = "employee";
//    String CACHE_GROUP_FILE = "file";
//
    String HOUSE_EXPORT_FILENAME = "房屋管理";


    //字典type
    //有无
    String HAVE_OR_NOT_HAVE = "HAVE_OR_NOT_HAVE";
    String BUILDING_PROPERTY = "BUILDING_PROPERTY";
    //楼层属性
    String FLOOR_TYPE = "FLOOR_TYPE";
    //房屋功能
    String HOUSE_FUNCTION = "HOUSE_FUNCTION";
    //房屋朝向
    String HOUSE_ORIENTATION = "HOUSE_ORIENTATION";

    //房屋属性
    String HOUSE_PROPERTY = "HOUSE_PROPERTY";

    enum SysCompanyAuditState {
        AUDIT_RENDING(1, "待审核"),
        AUDIT_PASS(2, "审核通过"),
        AUDIT_FAIL(3, "审核不通过"),
        CANCEL(4, "已取消");
        public int state;
        String desc;

        SysCompanyAuditState(int state, String desc) {
            this.state = state;
            this.desc = desc;
        }
    }

    //房屋属性
    enum HousePropertyEnum {

        FOR_SELF(0, "自用"), RENT(1, "租赁"), SELL(2, "销售");
        private static Map<Integer, Integer> houseDefaultStatusMap;
        public int code;
        String name;

        static {
            initHouseDefaultStatus();
        }

        //房屋属性是自用，则房屋默认状态为自用；房屋属性是租赁，房屋默认状态为待租；房屋属性是销售，房屋默认状态为待售
        private static void initHouseDefaultStatus() {
            houseDefaultStatusMap = new HashMap<>();
            houseDefaultStatusMap.put(FOR_SELF.code, HouseStatusEnum.FOR_SELF.getCode());
            houseDefaultStatusMap.put(RENT.code, HouseStatusEnum.RENTING.getCode());
            houseDefaultStatusMap.put(SELL.code, HouseStatusEnum.SELLING.getCode());
        }

        HousePropertyEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Map<Integer, Integer> getHouseDefaultStatusMap() {
            return houseDefaultStatusMap;
        }
    }

}
