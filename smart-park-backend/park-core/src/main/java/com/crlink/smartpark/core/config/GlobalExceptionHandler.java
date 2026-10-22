package com.crlink.smartpark.core.config;

import com.crlink.smartpark.common.constants.Constants;
import com.crlink.smartpark.common.message.EmptyPageException;
import com.crlink.smartpark.common.message.Message;
import com.crlink.smartpark.common.message.ResolinkRuntimeException;
import com.crlink.smartpark.common.web.ResponseResult;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseResult handlerException(Exception e) {
        log.error("系统异常：", e);
        return ResponseResult.errorMessage("10001");
//        todo 2022/4/2 方便开发联调使用，上线时恢复！
//        String causeBy = Arrays.stream(e.getStackTrace()).findFirst().toString();
//        return ResponseResult.errorMessage("10001", e.getMessage() + ";cause by:" + causeBy);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult handlerException(MethodArgumentNotValidException e) {
        log.error("参数异常：", e);
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage()).append(",");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return ResponseResult.errorMessage(Integer.parseInt(Message.code("10003").getCode()), sb.toString());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseResult handlerException(ConstraintViolationException e) {
        log.error("参数异常：", e);
        return ResponseResult.errorMessage(Integer.parseInt(Message.code("10003").getCode()), e.getMessage().substring(e.getMessage().indexOf(":") + 2));
    }

    @ExceptionHandler(ResolinkRuntimeException.class)
    public ResponseResult handlerResolinkRuntimeException(ResolinkRuntimeException re) {
        Message errorMessage = re.getErrorMessage();
        log.error(errorMessage.getDisplayText(), re);
        int code = NumberUtils.isDigits(errorMessage.getCode()) ? Integer.parseInt(errorMessage.getCode()) : Constants.FAIL_CODE;
        return ResponseResult.errorMessage(code, errorMessage.getDisplayText());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseResult handlerHttpMessageNotReadableException(HttpMessageNotReadableException re) {
        log.error("参数异常", re);
        return ResponseResult.errorMessage("10003");
    }

    @ExceptionHandler(EmptyPageException.class)
    public ResponseResult handlerEmptyPageException(EmptyPageException re) {
        return ResponseResult.success(new PageInfo<>(Collections.emptyList()));
    }
}
