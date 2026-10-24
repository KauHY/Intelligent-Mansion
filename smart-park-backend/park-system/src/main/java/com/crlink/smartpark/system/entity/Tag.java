package com.crlink.smartpark.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 标签表
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tag")
public class Tag implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 标签Id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 园区组织Id
     */
    private String parkOrgId;

    /**
     * 公司Id
     */
    private String companyId;

    /**
     * 0,禁用，1启用
     */
    private Integer status;

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    /**
     * 0未删除，1已删除
     */
    private Boolean deleted;


}
