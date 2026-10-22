package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.system.dto.park.ParkSaveDTO;
import com.crlink.smartpark.system.query.ParkListQuery;
import com.crlink.smartpark.system.vo.ParkDTO;
import com.crlink.smartpark.system.vo.ParkVO;
import com.github.pagehelper.PageInfo;

public interface ParkManagementParkService {


    Boolean completeParkInfo(ParkSaveDTO park);

    PageInfo<ParkDTO> listParkPageByOrgId(ParkListQuery query);

    ParkVO getParkInfoByOrgId(String id);
}
