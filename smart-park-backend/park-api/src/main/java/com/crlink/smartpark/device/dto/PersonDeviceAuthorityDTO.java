package com.crlink.smartpark.device.dto;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/** * @desc
 */
@Data
public class PersonDeviceAuthorityDTO {

    /**
     * key:设备ID value:卡号集合
     */
    private Map<String, Set<String>> deviceCardNosPushMap;

    /**
     * key:卡号 value:设备ID集合
     */
    private Map<String, Set<String>> cardNoDevicesPushMap;

    /**
     * key:通道编码 value:卡号集合
     */
    private Map<String, Set<String>> channelCardNosPushMap;

}
