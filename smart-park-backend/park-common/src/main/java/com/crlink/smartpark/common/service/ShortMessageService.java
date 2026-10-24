package com.crlink.smartpark.common.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crlink.smartpark.common.entity.DiskFile;
import com.crlink.smartpark.common.model.ShortMessageReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 * */
public interface ShortMessageService{

    Boolean getSmsCode(ShortMessageReqDTO reqDTO);
}
