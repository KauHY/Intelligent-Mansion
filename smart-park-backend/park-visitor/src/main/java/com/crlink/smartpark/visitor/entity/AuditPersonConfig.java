package com.crlink.smartpark.visitor.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 访客审批人配置数据库实体
 * */
@Data
@TableName("vis_audit_person_config")
public class AuditPersonConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private String id;

    /**
     * 园区组织ID
     */
    private String parkOrgId;

    /**
     * 入驻企业ID
     */
    private String companyId;

    /**
     * 审批员工ID
     */
    private String auditEmployeeIds;

    /**
     * 被访人审批，0：否，1：是
     */
    private Integer invitorEmployeeAudit;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 删除状态
     */
    private Boolean deleted;


}
