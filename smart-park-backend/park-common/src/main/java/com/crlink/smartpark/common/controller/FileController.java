package com.crlink.smartpark.common.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.nacos.common.utils.UuidUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.crlink.smartpark.common.config.OssConfig;
import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.entity.DiskFile;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.service.DiskFileService;
import com.crlink.smartpark.common.service.IOssService;
import com.crlink.smartpark.common.utils.AssertUtil;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.MyBeanUtils;
import com.crlink.smartpark.core.utils.MyFileUtils;
import com.crlink.smartpark.core.utils.SystemUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.crlink.smartpark.common.constants.CacheConstant.CACHE_GROUP_FILE;
import static com.crlink.smartpark.common.constants.CacheConstant.CacheKeyGenerator;
import static com.crlink.smartpark.common.constants.Constants.DOT;

@Slf4j
@Api(value = "文件存储", tags = "文件存储")
@RestController
@RequestMapping("/file")
public class FileController {


    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private IOssService ossService;

    @Autowired
    private DiskFileService diskFileService;

    @ApiOperation(value = "文件-上传-返回key", tags = "文件存储")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseResult<String> upload(@ApiParam(value = "文件key") @RequestParam(value = "key", required = false) String key,
                                         @ApiParam(value = "文件") @RequestPart("file") MultipartFile file) {
        DiskFile diskFile = diskFileService.uploadFile(key, file);
        return ResponseResult.success(diskFile.getRelativePath());
    }

    @ApiOperation(value = "文件-上传-返回id", tags = "文件存储")
    @RequestMapping(value = "/uploadAngGetFileId", method = RequestMethod.POST)
    public ResponseResult<String> uploadAngGetFileId(@ApiParam(value = "文件key") @RequestParam(value = "key", required = false) String key,
                                                     @ApiParam(value = "文件") @RequestParam("file") MultipartFile file) {
        DiskFile diskFile = diskFileService.uploadFile(key, file);
        return ResponseResult.success(diskFile.getId());
    }

    @ApiOperation(value = "文件-上传", tags = "文件存储")
    @RequestMapping(value = "/uploadBytes", method = RequestMethod.POST)
    public String uploadBytes(@RequestBody FileDTO fileDTO) {

        LocalDate localDate = LocalDate.now();
        String originalFilename = fileDTO.getFileOriginalName();
        String suffix = MyFileUtils.getFilenameSuffix(originalFilename);
        String fileName = UuidUtils.generateUuid() + DOT + suffix;
        // 自动生成key
        String key = fileDTO.getRelativePath();
        if (StringUtils.isBlank(key)) {
            key = localDate.getYear() + String.format("%02d", localDate.getMonthValue()) + "/" + fileName;
        }
        ossService.uploadFileBytes(key, fileDTO.getBytes());
        DiskFile diskFile = new DiskFile(fileName, originalFilename, suffix, fileDTO.getFileSize(), key);
        diskFile.setCreateBy(SystemUtil.getInstance().getCurrentUserId());
        diskFileService.save(diskFile);
        return key;
    }

    /**
     * 把url的文件上传到Minio
     *
     * @param fileUrl
     * @param folder
     * @return 文件相对路径     */
    @ApiOperation(value = "文件-上传-URL", tags = "文件存储")
    @RequestMapping(value = "/uploadByUrl", method = RequestMethod.GET)
    public String uploadByUrl(@RequestParam("fileUrl") String fileUrl, @RequestParam(value="folder", required = false) String folder) {
        folder = StrUtil.isBlank(folder) ? Constants.CAR_ACCESS_DIR : folder;
        try {
            byte[] bytes = HttpUtil.downloadBytes(fileUrl);
            String fileName = StrUtil.subAfter(fileUrl, '/', true);
            String relativePath = folder + "/" +  LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.YEAR_MONTH_FORMAT)) + "/" + fileName;
            ossService.uploadFileBytes(relativePath, bytes);
            return relativePath;
        } catch (Exception e) {
            log.error("FileController#uploadByUrl() Error:", e);
            return "";
        }
    }

    @ApiOperation(value = "文件-查看", tags = "文件存储")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public void get(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key,
                    @ApiParam(value = "文件name") @RequestParam(value = "name", required = false) String name,
                    HttpServletResponse response) {
        this.download(key, name, response);
    }

    @ApiOperation(value = "文件-查看2", tags = "文件存储")
    @RequestMapping(value = "/getFile", method = RequestMethod.GET)
    public void getFile(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key,
                        @ApiParam(value = "文件name") @RequestParam(value = "name", required = false) String name,
                        HttpServletResponse response) {
        DiskFile diskFile = diskFileService.lambdaQuery().select(DiskFile::getId, DiskFile::getFileOriginalName, DiskFile::getFileSuffix).eq(DiskFile::getRelativePath, key).one();
        //若是图片，预览；不是图片进行下载
        if (diskFile != null && OssConfig.IMAGE_SUFFIX.contains(diskFile.getFileSuffix())) {
            this.imagePreview(key, response);
        } else {
            if (StringUtils.isBlank(name) && Objects.nonNull(diskFile)) {
                name = diskFile.getFileOriginalName();
            }
            this.download(key, name, response);
        }
    }

    @ApiOperation(value = "图片-查看", tags = "文件存储")
    @RequestMapping(value = "/imagePreview", method = RequestMethod.GET)
    public void imagePreview(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key, HttpServletResponse response) {
        DiskFile diskFile = diskFileService.lambdaQuery().select(DiskFile::getId, DiskFile::getFileOriginalName, DiskFile::getFileSuffix).eq(DiskFile::getRelativePath, key).one();
        if (diskFile != null && OssConfig.IMAGE_SUFFIX.contains(diskFile.getFileSuffix())) {
            try (InputStream is = ossService.downloadFile(key);
                 OutputStream os = response.getOutputStream()) {
                response.setContentType(MediaType.TEXT_PLAIN_VALUE);
//                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(diskFile.getFileOriginalName(), "UTF-8"));
                if (is != null) {
                    IOUtils.copy(is, os);
                }
            } catch (Exception e) {
                log.error("【图片预览失败】，key:{}", key, e);
                throw new ResolinkRuntimeException("10009");
            }
        }
    }

    @ApiOperation(value = "文件-下载")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key,
                         @ApiParam(value = "文件name") @RequestParam(value = "name", required = false) String name,
                         HttpServletResponse response) {
        try (InputStream is = ossService.downloadFile(key);
             OutputStream os = response.getOutputStream()) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            // 配置文件下载
            response.setHeader("content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
            if (StringUtils.isBlank(name)) {
                name = Optional.ofNullable(diskFileService.key2Value(key, DiskFile::getFileOriginalName)).orElse(key);
            }
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(FileUtil.getName(name), "UTF-8"));
            if (is != null) {
                IOUtils.copy(is, os);
            }
        } catch (Exception e) {
            log.error("【文件下载失败】，key:{}", key, e);
        }
    }

    @ApiOperation(value = "文件-下载")
    @RequestMapping(value = "/downloadByte", method = RequestMethod.GET)
    public byte[] downloadByte(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key) {
        try (InputStream is = ossService.downloadFile(key)) {
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("【文件下载失败】，key:{}", key, e);
        }
        return null;
    }

    @ApiOperation(value = "文件-删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseResult delete(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key) throws Exception {
        //移除记录
        diskFileService.remove(Wrappers.<DiskFile>lambdaQuery().eq(DiskFile::getRelativePath, key));
        //删除oss文件
        final Boolean result = ossService.deleteFile(key);
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "文件-获取url", tags = "文件存储")
    @RequestMapping(value = "/getFileUrl", method = RequestMethod.GET)
    public ResponseResult getFileUrl(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key, HttpServletRequest request) {
        String currentUrl = request.getRequestURL().toString();
        String gatewayUrl = StringUtils.replace(currentUrl, "30000", "8082");
        gatewayUrl = gatewayUrl.substring(0, gatewayUrl.lastIndexOf("/getFileUrl")) + "/imagePreview?key=" + key;
        return ResponseResult.success(gatewayUrl);

    }

    //@ApiOperation(value = "文件-删除")
    //@RequestMapping(value = "/test", method = RequestMethod.GET)
    //public ResponseResult test(@ApiParam(value = "文件key", required = true) @RequestParam("key") String key) throws Exception {
    //    //移除记录
    //    QueryWrapper<DiskFile> queryWrapper = new QueryWrapper<>();
    //    queryWrapper.select("a");
    //    String allSqlSelect = TableInfoHelper.getTableInfo(DiskFile.class).getAllSqlSelect();
    //    //COMMA
    //    List<DiskFile> list = diskFileService.list(queryWrapper);
    //    return ResponseResult.success(list);
    //}


    @ApiOperation(value = "文件-获取文件列表", tags = "文件存储")
    @RequestMapping(value = "/listFileByIds", method = RequestMethod.POST)
    public List<FileDTO> listFileByIds(@ApiParam(value = "文件id队列", required = true) @RequestBody Set<String> ids) {
        AssertUtil.requireTrue(CollectionUtils.isNotEmpty(ids), "10003");
        return diskFileService.listByIds(ids).stream().map(diskFile -> MyBeanUtils.copy(diskFile, FileDTO::new)).collect(Collectors.toList());
    }

    @ApiOperation(value = "文件-获取文件详情", tags = "文件存储")
    @RequestMapping(value = "/getFileDetailByKeys", method = RequestMethod.POST)
    @Cacheable(cacheNames = CACHE_GROUP_FILE, keyGenerator = CacheKeyGenerator)
    public ResponseResult<Map<String, FileDTO>> getFileDetailByKeys(@ApiParam(value = "文件keys", required = true) @RequestBody Set<String> keys) {
        AssertUtil.requireTrue(CollectionUtils.isNotEmpty(keys), "10003");
        Map<String, FileDTO> result = diskFileService.getFileDetailByKeys(keys).parallelStream().map(diskFile -> MyBeanUtils.copy(diskFile, FileDTO::new))
                .collect(Collectors.toMap(FileDTO::getRelativePath, Function.identity(), (v1, v2) -> v2));
        return ResponseResult.success(result);
    }

    @ApiOperation(value = "文件-获取文件详情-内部接口", tags = "文件存储")
    @RequestMapping(value = "/getFileDetailByKeysSelf", method = RequestMethod.POST)
//    @Cacheable(cacheNames = CACHE_GROUP_FILE, keyGenerator = CacheKeyGenerator)
    public Map<String, FileDTO> getFileDetailByKeysSelf(@ApiParam(value = "文件keys", required = true) @RequestBody Set<String> keys) {
        AssertUtil.requireTrue(CollectionUtils.isNotEmpty(keys), "10003");
        Map<String, FileDTO> result = diskFileService.getFileDetailByKeys(keys).parallelStream().map(diskFile -> MyBeanUtils.copy(diskFile, FileDTO::new))
                .collect(Collectors.toMap(FileDTO::getRelativePath, Function.identity(), (v1, v2) -> v2));
        return result;
    }

    /**
     * 上传文件到oss返回key
     */
    @PostMapping("/uploadFile2oss")
    void fileUpload2oss(@RequestBody FileDTO fileDTO) {
        ossService.uploadFileBytes(fileDTO.getRelativePath(), fileDTO.getBytes());
    }
}
