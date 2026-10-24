package com.crlink.smartpark.customer.constants;

import java.time.LocalDateTime;

public interface Constant {
    LocalDateTime DEFAULT_TOPPING_TIME = LocalDateTime.of(1970, 1, 1, 0, 0);


    /**
     * 报事投诉类型
     */
    int ORDER_TYPE_COMPLAINT = 1;
    /**
     * 报修服务类型
     */
    int ORDER_TYPE_REPAIR = 2;
}
