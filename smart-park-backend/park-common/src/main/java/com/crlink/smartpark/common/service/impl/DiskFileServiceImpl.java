package com.crlink.smartpark.common.service.impl;

import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.crlink.smartpark.common.entity.DiskFile;
import com.crlink.smartpark.common.mapper.DiskFileMapper;
import com.crlink.smartpark.common.service.DiskFileService;
import com.crlink.smartpark.common.service.IOssService;
import com.crlink.smartpark.core.utils.MyFileUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.Constants.DOT;

/**
 * <p>
 * 上传文件 服务实现类
 * </p>
 * */
@Service
public class DiskFileServiceImpl extends ServiceImpl<DiskFileMapper, DiskFile> implements DiskFileService {

    @Autowired
    private IOssService ossService;

    @Override
    public <R> R key2Value(String key, SFunction<DiskFile, R> sFunction) {
        R result = this.lambdaQuery().select(DiskFile::getId, DiskFile::getRelativePath, sFunction)
                .eq(DiskFile::getRelativePath, key).list().parallelStream()
                .collect(Collectors.toMap(DiskFile::getRelativePath, sFunction, (v1, v2) -> v2)).get(key);
        return result;

    }

    @Override
    public DiskFile uploadFile(String key, MultipartFile file) {
        LocalDate localDate = LocalDate.now();
        //String uuid = UuidUtils.generateUuid();
        String originalFilename = file.getOriginalFilename();
        String suffix = MyFileUtils.getFilenameSuffix(originalFilename);
        String fileName = UuidUtils.generateUuid() + DOT + suffix;
        // 自动生成key
        if (StringUtils.isBlank(key)) {
            key = localDate.getYear() + String.format("%02d", localDate.getMonthValue()) + "/" + fileName;
        }
        ossService.uploadFile(key, file);
        DiskFile diskFile = new DiskFile(fileName, originalFilename, suffix, file.getSize(), key);
        diskFile.setCreateBy(SystemUtil.getInstance().getCurrentUserId());
        save(diskFile);
        return diskFile;
    }

    @Override
    public List<DiskFile> getFileDetailByKeys(Set<String> keys) {
        List<DiskFile> list = this.lambdaQuery().in(DiskFile::getRelativePath, keys).list();
        return list;
    }
}
