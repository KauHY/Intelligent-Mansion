package com.crlink.smartpark.device.dto;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/** * @desc
 */
@Data
public class PersonDeviceAddAndAuthorityDTO {

    /**
     * 人员信息
     */
    private PersonDeviceDTO personDTO;

    /**
     * 门禁授权信息
     */
    private PersonDeviceAuthorityDTO personDeviceAuthorityDTO;


}
