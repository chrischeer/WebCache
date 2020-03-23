package com.newland.mi.wrc.advice;

import com.newland.mi.wrc.dto.RetMsg;
import com.newland.mi.wrc.enums.ResultCode;
import com.newland.mi.wrc.exception.ResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Miracle
 * 抛出到controller的未处理异常统一捕获处理
 */
@ControllerAdvice
@ResponseBody
public class ExceptionHandlerAdvice {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getSimpleName());

    /**
     * 处理参数效验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RetMsg handleException(MethodArgumentNotValidException e) {
        LOGGER.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMesssage = new StringBuilder("请求参数错误:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage.append(fieldError.getDefaultMessage()).append(",");
        }
        RetMsg retMsg = RetMsg.newInstance();
        retMsg.setRespCodeAndDesc(ResultCode.PARAMETER_ERROR_10002, errorMesssage.toString());
        return retMsg;
    }

    /**
     * 处理参数效验异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public RetMsg handleException(IllegalArgumentException e) {
        LOGGER.error(e.getMessage(), e);
        RetMsg retMsg = RetMsg.newInstance();
        retMsg.setRespCodeAndDesc(ResultCode.PARAMETER_ERROR_10002, "[参数错误]" + e.getMessage());
        return retMsg;
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ResultException.class)
    public RetMsg handleResultException(ResultException resultException) {
        LOGGER.error(resultException.getErrorMsg(), resultException);
        return RetMsg.newInstance()
                .setException(resultException);
    }

    /**
     * 其他未捕获异常统一处理
     */
    @ExceptionHandler(Exception.class)
    public RetMsg handleException(Exception e) {
        LOGGER.error(e.getMessage(), e);
        return RetMsg.newInstance()
                .setException(new ResultException(ResultCode.SYSTEM_ERROR_10000));
    }

}
