package com.crlink.smartpark.system.vo;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.text.Collator;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

@Data
public class TreeDictVO extends SimpleNode implements Comparable<TreeDictVO> {
    private Set<TreeDictVO> children = new TreeSet<>();

    private Integer hierarchy;

    public TreeDictVO() {
    }

    public TreeDictVO(Object value, String label, Integer type) {
        super(value, label, type);
    }

    public TreeDictVO(Object value, String label, Integer type, Integer hierarchy) {
        super(value, label, type);
        this.hierarchy = hierarchy;
    }

    @Override
    public int compareTo(@NotNull TreeDictVO o) {
        return Collator.getInstance(Locale.CHINA).compare(this.getLabel(), o.getLabel());
    }
}
