package com.crlink.smartpark.system.service.parkmanagement;

import com.crlink.smartpark.device.dto.PersonDeviceDTO;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 * */
public interface AsyncService {

    void uploadMinio(String fileKey, File file);

    Map<Long, String> uploadIcc(PersonDeviceDTO dto, File file);

    void uploadIcc(List<PersonDeviceDTO> personList);
}
