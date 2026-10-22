package com.crlink.smartpark.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 上传文件
 * </p>
 * */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("com_disk_file")
@NoArgsConstructor
public class DiskFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文件名_唯一
     */
    private String fileName;

    /**
     * 原文件名
     */
    private String fileOriginalName;

    /**
     * 后缀
     */
    private String fileSuffix;

    /**
     * 大小
     */
    private Long fileSize;

    /**
     * 相对路径,oss的key
     */
    private String relativePath;

    private Boolean deleted;

    private Integer status;


    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public DiskFile(String fileName, String fileOriginalName, String fileSuffix, Long fileSize, String relativePath) {
        this.fileName = fileName;
        this.fileOriginalName = fileOriginalName;
        this.fileSuffix = fileSuffix;
        this.fileSize = fileSize;
        this.relativePath = relativePath;
    }
}
