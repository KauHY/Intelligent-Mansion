//package com.crlink.smartpark.common.query;
//
//import com.crlink.smartpark.BaseQuery;
//import java.time.LocalDateTime;
//import java.io.Serializable;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.experimental.Accessors;
//import java.util.List;
//
///**
// * <p>
// * 上传文件
// * </p>
// *
////// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
//@ApiModel(value="DiskFile对象", description="上传文件")
//public class DiskFileQuery extends BaseQuery implements Serializable {
//
//    private static final long serialVersionUID=1L;
//
//    @ApiModelProperty(value = "主键")
//      private String id;
//
//    @ApiModelProperty(value = "文件名_唯一")
//    private String fileName;
//
//    @ApiModelProperty(value = "原文件名")
//    private String fileOriginalName;
//
//    @ApiModelProperty(value = "后缀")
//    private String fileSuffix;
//
//    @ApiModelProperty(value = "大小")
//    private Long fileSize;
//
//    @ApiModelProperty(value = "相对路径,oss的key")
//    private String relativePath;
//
//    private Boolean deleted;
//
//    @ApiModelProperty(value = "状态,2正常,1未启用,3已删除")
//    private Integer status;
//
//    @ApiModelProperty(value = "创建者")
//    private String createBy;
//
//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;
//    @ApiModelProperty(value = "ids")
//    private List<String> ids;
//
//
//}
