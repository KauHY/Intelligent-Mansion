package com.crlink.smartpark.system.controller;

import com.crlink.smartpark.common.feign.FileProvider;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.HolidayUtil;
import com.crlink.smartpark.system.service.parkmanagement.BuildingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

/**
 * @description: 开发测试使用，无任何业务功能
 */
@RestController
@Api(tags = "测试使用")
@RequestMapping("/test")
public class TestController {

    @Resource
    private BuildingService buildingService;

    @Autowired
    private FileProvider fileProvider;


    @ApiOperation(value = "测试文件上传", tags = "测试使用-文件上传")
    @GetMapping("/file/upload")
    public ResponseResult<String> test(String type, String code) throws Exception {
        File file = new File("C:\\Users\\caoding\\Desktop\\1.jpg");
//        FileInputStream fis = new FileInputStream(file);
        FileItem fileItem = new DiskFileItemFactory().createItem("file", MediaType.MULTIPART_FORM_DATA_VALUE, true, file.getName());
        try (InputStream input = new FileInputStream(file); OutputStream os = fileItem.getOutputStream()) {
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }
        MultipartFile multiFile = new CommonsMultipartFile(fileItem);
        fileProvider.upload(file.getName(), multiFile);

        return ResponseResult.success(file.getName());
    }

    @ApiOperation(value = "测试", tags = "测试使用")
    @GetMapping("/test")
    public ResponseResult test11() throws Exception {
        System.out.println(HolidayUtil.isHoliday(LocalDate.now()));
        return ResponseResult.success();
    }

}
