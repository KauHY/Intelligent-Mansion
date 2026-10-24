package com.crlink.smartpark.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ValidListDTO<E> {

    @Valid
    List<E> list;

    public ValidListDTO() {
        list = new ArrayList<>();
    }

}
