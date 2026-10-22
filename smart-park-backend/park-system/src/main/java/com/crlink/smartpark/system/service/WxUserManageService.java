package com.crlink.smartpark.system.service;

import com.crlink.smartpark.system.dto.wxuser.WxUserDTO;
import com.crlink.smartpark.system.dto.wxuser.WxUserVO;

public interface WxUserManageService {


    /**
     * 园区运营小程序
     *
     * @param wxUserDTO
     * @return
     */
    WxUserVO authParkManagementLogin(WxUserDTO wxUserDTO);


    WxUserVO authParkVisitorLogin(WxUserDTO wxUserDTO);

    WxUserVO queryWxUserInfo();
    
}
