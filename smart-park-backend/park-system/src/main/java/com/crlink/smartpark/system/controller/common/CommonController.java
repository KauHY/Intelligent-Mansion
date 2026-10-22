package com.crlink.smartpark.system.controller.common;

import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.web.ResponseResult;
import com.crlink.smartpark.core.utils.MyCaptchaUtil;
import com.crlink.smartpark.core.utils.MyLineCaptcha;
import com.crlink.smartpark.core.utils.RedisUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * 系统管理
 *
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * @title 获取图形验证码
     */
    @ApiOperation(value = "获取验证码", tags = "公共服务")
    @RequestMapping(value = "/getImgCode", method = RequestMethod.GET)
    public ResponseResult getImgCode(@RequestParam String key, HttpServletResponse response) {
        // 取得输出流
        try (OutputStream out = response.getOutputStream()) {
            //定义图形验证码的长、宽、验证码字符数、干扰线宽度
            MyLineCaptcha lineCaptcha = MyCaptchaUtil.createMyLineCaptcha(112, 32, 4, 50);
            lineCaptcha.write(out);
            // 将生成的验证码code放入redis中
            String imgCodeKey = Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(key);
            redisUtil.setObject(imgCodeKey, lineCaptcha.getCode(), 2L, TimeUnit.MINUTES);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return ResponseResult.success();
    }

    @ApiOperation(value = "校验图形验证码", tags = "公共服务")
    @RequestMapping(value = "/validateImgCode", method = RequestMethod.GET)
    public ResponseResult<Boolean> validateImgCode(@RequestParam String key, @RequestParam String code) {
        String oldValidateCode = (String) redisUtil.getObject(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(key));
        if (!code.equalsIgnoreCase(oldValidateCode)) {
            redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(key));
            return ResponseResult.success(false);
        }
//        redisUtil.delete(Constants.Redis.IMG_AUTH_CODE_PREFIX.getBuildCode(key));
        return ResponseResult.success(true);
    }


}
