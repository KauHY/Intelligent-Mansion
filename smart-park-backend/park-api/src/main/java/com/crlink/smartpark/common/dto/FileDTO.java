package com.crlink.smartpark.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@ApiModel(value = "DiskFile对象", description = "上传文件")
public class FileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "文件名_唯一")
    private String fileName;

    @ApiModelProperty(value = "原文件名")
    private String fileOriginalName;

    @ApiModelProperty(value = "后缀")
    private String fileSuffix;

    @ApiModelProperty(value = "大小")
    private Long fileSize;

    @ApiModelProperty(value = "相对路径,oss的key")
    private String relativePath;

    private byte[] bytes;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
