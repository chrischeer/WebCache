package com.newland.mi.wrc.aspect;

import com.newland.mi.wrc.annotation.ParamsExist;
import com.newland.mi.wrc.dto.ReqMsg;
import com.newland.mi.wrc.utils.ReqVerifyUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 参数校验切面
 */
@Aspect
@Component
public class ValidAspect {
    private static final String POST = "POST";
    private static final String FORM = "FORM";

    @Before(value = "@annotation(paramsExist)")
    public void doBefore(JoinPoint joinPoint, ParamsExist paramsExist) {
        String validParamType = paramsExist.type().toUpperCase();
        switch (validParamType) {
            case POST: {
                ReqMsg reqMsg = (ReqMsg) joinPoint.getArgs()[0];
                ReqVerifyUtils.verifyHasNeedParams(reqMsg, paramsExist.value());
                break;
            }
            case FORM: {
                MultipartHttpServletRequest multReq = (MultipartHttpServletRequest) joinPoint.getArgs()[0];
                ReqVerifyUtils.verifyFormParams(multReq.getParameterMap(), paramsExist.value());
                ReqVerifyUtils.verifyFormFiles(multReq.getFileMap(), paramsExist.file());
                break;
            }
            default:
                break;
        }
    }

}
