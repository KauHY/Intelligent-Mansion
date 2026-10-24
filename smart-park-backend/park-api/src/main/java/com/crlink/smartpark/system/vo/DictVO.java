package com.crlink.smartpark.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DictVO implements Serializable {

    public DictVO(Object value) {
        this.value = value;
    }

    public DictVO(String label) {
        this.label = label;
    }

    @ApiModelProperty(value = "名称")
    private String label;

    @ApiModelProperty(value = "值")
    private Object value;

    public static List<DictVO> mapToList(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return Collections.emptyList();
        }
        List<DictVO> result = new ArrayList<>(map.size());
        map.forEach((k, v) -> {
            result.add(new DictVO(v, k));
        });
        return result;
    }


}
