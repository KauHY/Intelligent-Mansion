package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.system.dto.PermissionReqDTO;

import java.util.Set;

public interface ParkManagementPermissionService {


    Boolean dataPermission(PermissionReqDTO reqDTO);

    Set<String> echoData(String roleId);

    //
    //Set<String> echoParkMenu(String roleId);
}
