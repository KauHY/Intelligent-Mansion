package com.crlink.smartpark.device.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 */
@Data
public class DropDownDeviceDTO implements Serializable {
    private static final long serialVersionUID = 2037176522461413124L;
    private String parkOrgId;
    private List<Integer> deviceTypes;
}
