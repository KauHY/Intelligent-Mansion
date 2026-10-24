package com.crlink.smartpark.common.feign;

import com.crlink.smartpark.common.dto.FileDTO;
import com.crlink.smartpark.common.web.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 * */
@FeignClient(name = "park-common", path = "/smartPark/park-common", contextId = "FileProvider")
public interface FileProvider {


    @RequestMapping(value = "/file/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void upload(@RequestParam(value = "key") String key, @RequestPart("file") MultipartFile file);

    /**
     * 【上传文件】新增
     *
     * @param diskFileDTO 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/file/uploadBytes")
    String feignUploadBytes(@RequestBody FileDTO diskFileDTO);

    /**
     * 文件-上传-URL
     * @param fileUrl     * @return
     */
    @GetMapping("/file/uploadByUrl")
    String uploadByUrl(@RequestParam("fileUrl") String fileUrl);


    /**
     * 【上传文件】新增
     *
     * @param ids 实体对象
     * @return 返回保存成功的对象
     */
    @PostMapping("/file/listFileByIds")
    List<FileDTO> listFileByIds(@RequestBody Set<String> ids);

    /**
     * 获取文件byte
     */
    @GetMapping("/file/downloadByte")
    byte[] downloadByte(@RequestParam("key") String key);

    /**
     * 上传文件到oss返回key
     */
    /**
     * 上传文件到oss返回key
     */
    @PostMapping("/file/uploadFile2oss")
    void fileUpload2oss(@RequestBody FileDTO fileDTO);

    @RequestMapping(value = "/file/getFileDetailByKeys", method = RequestMethod.POST)
    ResponseResult<Map<String, FileDTO>> getFileDetailByKeys(@RequestBody Set<String> keys);

    @RequestMapping(value = "/file/getFileDetailByKeysSelf", method = RequestMethod.POST)
    Map<String, FileDTO> getFileDetailByKeysSelf(@RequestBody Set<String> keys);

}
