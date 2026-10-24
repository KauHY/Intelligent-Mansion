package com.crlink.smartpark.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 后台任务
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("com_task")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 描述
     */
    private String description;

    /**
     * 表达式
     */
    private String cron;

    /**
     * 是否关闭，0开启，1关闭
     */
    private Boolean closed;

    /**
     * 任务线程数
     */
    private Integer poolSize;

    /**
     * 线程名前缀
     */
    private String applicationName;
    private String beanName;
    private String methodName;


    private String threadNamePrefix;

    private Boolean removeOnCancelPolicy;


}
