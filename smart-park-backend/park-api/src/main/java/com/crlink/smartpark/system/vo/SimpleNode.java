package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class SimpleNode extends DictVO {

    @ApiModelProperty(value = "节点类型，1：园区，2：楼栋，3：房屋，4：入驻企业")
    private Integer type;

    @ApiModelProperty("额外信息")
    private Object extraInfo;

    public SimpleNode(String label) {
        super(label);
    }

    public SimpleNode(Object value, Integer type) {
        super(value);
        this.type = type;
    }

    public SimpleNode(Object value, String label) {
        super(label, value);
    }

    public SimpleNode(Object value, String label, Integer type) {
        super(label, value);
        this.type = type;
    }

    public SimpleNode() {
    }

    @Getter
    public enum TypeEnum {
        PARK(1, "园区"),
        BUILDING(2, "楼栋"),
        HOUSE(3, "房屋"),
        COMPANY(4, "入驻企业"),
        Tag(5, "员工标签"),
        EMPLOYEE(6, "员工姓名"),
        USER(7, "用户姓名"),
        CATEGORY(8, "分类"),
        BUILDING_HOUSE(9, "楼栋房屋");
        public Integer code;
        public String info;

        TypeEnum(Integer code, String info) {
            this.code = code;
            this.info = info;
        }
    }
}
