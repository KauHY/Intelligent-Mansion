package com.crlink.smartpark.customer.controller.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FormListDTO implements Serializable {
    private String formId;

    private String title;

    private String employeeId;
    
    private String employeeName;


    private LocalDateTime createTime;
}
