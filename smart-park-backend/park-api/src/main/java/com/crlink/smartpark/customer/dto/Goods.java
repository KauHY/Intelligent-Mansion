package com.crlink.smartpark.customer.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Goods implements Serializable {
    private String name;
    private Integer count;
    private String picture;

    @Override
    public String toString() {
        return name + "*" + count;
    }
}
