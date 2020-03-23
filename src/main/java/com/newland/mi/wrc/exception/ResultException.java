package com.newland.mi.wrc.exception;


import com.newland.mi.wrc.dto.RetMsg;
import com.newland.mi.wrc.enums.ResultCode;

/**
 * 结果异常，会被 ExceptionHandler 捕捉并返回
 */
public class ResultException extends RuntimeException {
    private String errorCode;
    private String errorMsg;

    public ResultException(ResultCode resultCode, String extraMsg) {
        super(resultCode.getMsg());
        this.errorMsg = resultCode.getMsg() + "-" + extraMsg;
        this.errorCode = resultCode.getCode();
    }

    public ResultException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.errorMsg = resultCode.getMsg();
        this.errorCode = resultCode.getCode();
    }

    public ResultException(RetMsg retMsg) {
        super(retMsg.getHeader().getRetMsg());
        this.errorMsg = retMsg.getHeader().getRetMsg();
        this.errorCode = retMsg.getHeader().getRetCode();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
