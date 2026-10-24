package com.crlink.smartpark.system.vo.builiding;

import com.crlink.smartpark.system.vo.SimpleNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BuildingTreeVO extends SimpleNode {
    private List<BuildingTreeVO> children;

    public BuildingTreeVO(Object value, String label, Integer type) {
        super(value, label, type);
    }
}
