package com.crlink.smartpark.common.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.entity.DiskFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 * */
public interface DiskFileService extends IService<DiskFile> {

    <R> R key2Value(String key, SFunction<DiskFile, R> sFunction);

    DiskFile uploadFile(String key, MultipartFile file);

    List<DiskFile> getFileDetailByKeys(Set<String> keys);
}
