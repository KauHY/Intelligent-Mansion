package com.crlink.smartpark.system.dto.organization;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Data
public class OrgStructureVO implements Comparable<OrgStructureVO>, Serializable {
    private String id;
    private String orgName;
    private Integer orgType;
    private String parentId;
    //private Set<OrgStructureVO> children = new TreeSet<>((v1, v2) -> Collator.getInstance(Locale.CHINA).compare(v1.getOrgName(), v2.getOrgName()));
    private Set<OrgStructureVO> children = new TreeSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrgStructureVO that = (OrgStructureVO) o;
        return Objects.equals(id, that.id) && Objects.equals(orgName, that.orgName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orgName);
    }

    @Override
    public int compareTo(@NotNull OrgStructureVO o) {
        return Collator.getInstance(Locale.CHINA).compare(this.getOrgName(), o.getOrgName());
    }
}
