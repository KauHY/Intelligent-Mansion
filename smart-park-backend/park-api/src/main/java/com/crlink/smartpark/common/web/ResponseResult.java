package com.crlink.smartpark.common.web;

import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.Message;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.PropertyKey;

import java.io.Serializable;

/**
 * <p>
 * 前端响应实体
 * </p>
 * */
@Data
@ApiModel(value = "接口返回对象", description = "接口返回对象")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 返回代码
     */
    @ApiModelProperty(value = "返回代码")
    private Integer code = Constants.SUCCESS_CODE;

    /**
     * 返回处理消息
     */
    @ApiModelProperty(value = "返回处理消息")
    private String message = Constants.SUCCESS_MESSAGE;

    /**
     * 成功标志
     */
    @ApiModelProperty(value = "成功标志")
    private boolean success = true;

    /**
     * 返回数据对象 data
     */
    @ApiModelProperty(value = "返回数据对象")
    private T data;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private long timestamp = System.currentTimeMillis();

    public ResponseResult() {
    }

    public ResponseResult(String message) {
        this.message = message;
    }

    public ResponseResult(T data) {
        this.data = data;
    }

    public ResponseResult(String message, T data) {
        this.message = message;
        this.data = data;
    }

    private ResponseResult(int code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public static ResponseResult success() {
        return new ResponseResult();
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult(data);
    }

    public static ResponseResult successMessage(String message) {
        return new ResponseResult(message);
    }

    public static <T> ResponseResult<T> successMessage(String message, T data) {
        return new ResponseResult<T>(message, data);
    }

    public static ResponseResult errorMsg() {
        return errorMsg(StringUtils.EMPTY);
    }

    public static ResponseResult errorMsg(String msg) {
        return errorMessage(Constants.FAIL_CODE, msg);
    }

    public static ResponseResult errorMessage(int code, String msg) {
        return new ResponseResult(code, msg, false);
    }

    public static ResponseResult errorMessage(@PropertyKey(resourceBundle = Constants.GLOBALIZATION_RESOURCE_PREFIX) String key, Object... params) {
        Message message = Message.code(key, params);
        int code = NumberUtils.isDigits(message.getCode()) ? Integer.parseInt(message.getCode()) : Constants.FAIL_CODE;
        return new ResponseResult(code, message.getDisplayText(), false);
    }

}