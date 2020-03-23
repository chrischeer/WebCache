package com.newland.mi.wrc.aspect;

import com.newland.mi.wrc.dto.ReqMsg;
import com.newland.mi.wrc.dto.RetMsg;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;


/**
 * @author Miracle
 * 对controller层日志切片处理
 */
@Aspect
@Component
public class LogAspect {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private String verifyUserTokenPath = "user/token/verify";

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping)||"
            + "@annotation(org.springframework.web.bind.annotation.PutMapping)||"
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping)||"
            + "@annotation(org.springframework.web.bind.annotation.PatchMapping)||"
            + "@annotation(org.springframework.web.bind.annotation.Mapping)||"
            + "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void mappingAspect() {
    }


    @Before("mappingAspect()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String contentType = request.getContentType();
        if(request.getRequestURL().toString().contains(verifyUserTokenPath)){
            return;
        }
        LOGGER.info("\n--------------------------请求报文----------------------------\n"
                + "请求类型 : " + request.getMethod() + "，\t请求URL : " + request.getRequestURL().toString() + "\n"
                + "调用方法 : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "\n"
                + "请求参数 : " + Arrays.toString(joinPoint.getArgs()) + "\n"
                + "--------------------------------------------------------------\n");

        if ("POST".equals(request.getMethod()) && joinPoint.getArgs().length > 0) {
            if (contentType.contains("application/json") && (joinPoint.getArgs()[0]) instanceof ReqMsg) {
                ReqMsg reqMsg = (ReqMsg) joinPoint.getArgs()[0];
                //记录seq，为了抛出错误时，对应上具体req
                RetMsg.myThreadLoad.set(reqMsg.getHeader().getReqSeq());
            } else if (contentType.contains("multipart/form-data") && (joinPoint.getArgs()[0]) instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multReq = (MultipartHttpServletRequest) joinPoint.getArgs()[0];
                Map<String, String[]> parameterMap = multReq.getParameterMap();
                Map<String, MultipartFile> fileMap = multReq.getFileMap();
                for (Map.Entry entry : parameterMap.entrySet()) {
                    LOGGER.info("paramKey:" + entry.getKey() + "\t\tparamValue():" + ((String[]) entry.getValue())[0]);
                }
                for (Map.Entry entry : fileMap.entrySet()) {
                    MultipartFile multipartFile = (MultipartFile) entry.getValue();
                    LOGGER.info("fileKey:" + entry.getKey()
                            + "\t\tisEmpty():" + multipartFile.isEmpty()
                            + "\t\tgetName():" + multipartFile.getName()
                            + "\t\tgetContentType():" + multipartFile.getContentType()
                            + "\t\tgetOriginalFilename():" + multipartFile.getOriginalFilename()
                            + "\t\tgetSize():" + multipartFile.getSize());
                }
            }
        }
    }

    @AfterReturning(returning = "ret", pointcut = "mappingAspect()")
    public void doAfterReturning(Object ret) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if(request.getRequestURL().toString().contains(verifyUserTokenPath)){
            return;
        }
        LOGGER.info("\n--------------------------返回报文----------------------------\n"
                + "请求URL : " + request.getRequestURL().toString() + "\n"
                + "请求返回值 : " + ret + "\n"
                + "--------------------------------------------------------------\n");
    }

}
