package com.crlink.smartpark.visitor.test;


import com.crlink.smartpark.visitor.vo.VisitorRecordSubScribeListVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 开发测试使用，无任何业务功能
 */
@RestController
@RequestMapping("/visitor/test")
public class TestController {


    @GetMapping("")
    public void test(String type, String code) {

    }

}
