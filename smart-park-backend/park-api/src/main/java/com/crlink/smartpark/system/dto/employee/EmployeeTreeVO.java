package com.crlink.smartpark.system.dto.employee;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EmployeeTreeVO implements Serializable {
    private String label;
    private Object value;
    private List<EmployeeTreeVO> children = Lists.newArrayList();

    public EmployeeTreeVO() {
    }

    public EmployeeTreeVO(String label) {
        this.label = label;
    }

    public EmployeeTreeVO(String label, Object value) {
        this.label = label;
        this.value = value;
    }
}
