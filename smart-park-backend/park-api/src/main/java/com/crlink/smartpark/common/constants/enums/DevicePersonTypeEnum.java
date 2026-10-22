package com.crlink.smartpark.common.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** * @desc
 */
@Getter
@AllArgsConstructor
public enum DevicePersonTypeEnum {
    NORMAL(0, "普通人员"),
    VISITOR(2, "访客"),
    ;
    Integer code;
    String desc;

}
