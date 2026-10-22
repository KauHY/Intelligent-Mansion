package com.crlink.smartpark.customer.controller.response;

import com.crlink.smartpark.customer.dto.ActivityDTO;
import com.crlink.smartpark.customer.dto.ActivityRegisterRecordDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MyRegisterRecord implements Serializable {
    private ActivityDTO activityDTO;
    private ActivityRegisterRecordDTO activityRegisterRecordDTO;

    public MyRegisterRecord(ActivityDTO activityDTO, ActivityRegisterRecordDTO activityRegisterRecordDTO) {
        this.activityDTO = activityDTO;
        this.activityRegisterRecordDTO = activityRegisterRecordDTO;
    }
}
