package com.newland.mi.wrc.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.mi.wrc.dto.ReqMsg;
import com.newland.mi.wrc.enums.ResultCode;
import com.newland.mi.wrc.exception.ResultException;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miracle
 * @date 2018/10/18
 * 用于验证请求体content的内容
 */
public class ReqVerifyUtils {
    public static void verifyHasNeedParams(ReqMsg reqMsg, String... needParams) {
        //验证header
        verifyHeader(reqMsg);
        //验证content
        JSONObject reqJsonObj = reqMsg.getContent();
        List<String> missParams = new ArrayList<>();
        for (String needParam : needParams) {
            String[] jsonParams = needParam.split("\\.");
            JSONObject tempJsonObj = reqJsonObj;
            for (String jsonParam : jsonParams) {
                if (tempJsonObj.containsKey(jsonParam)) {
                    if (isJsonObject(tempJsonObj.get(jsonParam))) {
                        tempJsonObj = tempJsonObj.getJSONObject(jsonParam);
                    }
                } else {
                    missParams.add(needParam);
                    break;
                }
            }
        }
        if (missParams.size() > 0) {
            throw new ResultException(ResultCode.PARAMETER_MISS_10001, "" + missParams);
        }
    }

    private static void verifyHeader(ReqMsg reqMsg) {
        ReqMsg.Header reqHeader = reqMsg.getHeader();
//        Assert.notNull(reqHeader.getReqSeq(), "reqSeq不能为null");
//        Assert.notNull(reqHeader.getClientType(), "clientType不能为null");
//        Assert.notNull(reqHeader.getDeviceId(), "deviceId不能为null");
//        Assert.notNull(reqHeader.getReqDateTime(), "reqDateTime不能为null");
    }

    public static void verifyFormParams(Map<String, String[]> parameterMap, String... needParams) {
        List<String> missParams = new ArrayList<>();
        for (String needParam : needParams) {
            if (!parameterMap.containsKey(needParam)) {
                missParams.add(needParam);
            }
        }
        if (missParams.size() > 0) {
            throw new ResultException(ResultCode.PARAMETER_MISS_10001, "" + missParams);
        }
    }

    public static void verifyFormFiles(Map<String, MultipartFile> fileMap, String... needFiles) {
        List<String> missFiles = new ArrayList<>();
        for (String needParam : needFiles) {
            if (!fileMap.containsKey(needParam)) {
                missFiles.add(needParam);
            }
        }
        if (missFiles.size() > 0) {
            throw new ResultException(ResultCode.PARAMETER_MISS_10001, "" + missFiles);
        }
    }

    /**
     * 判断是JsonObject
     */
    private static boolean isJsonObject(Object obj) {
        if(null==obj){
            return false;
        }
        String content = obj.toString();
        try {
            JSONObject.parseObject(content);
            return content.startsWith("{");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是JsonArray
     */
    private static boolean isJsonArray(Object obj) {
        String content = obj.toString();
        try {
            JSONArray.parseArray(content);
            return content.startsWith("[");
        } catch (Exception e) {
            return false;
        }
    }


    private static void main(String[] args) {
        ReqMsg reqMsg = ReqMsg.newInstance();
        JSONObject testJsonObj = new JSONObject();
        JSONObject testJsonObj2 = new JSONObject();
        JSONObject testJsonObj3 = new JSONObject();
        testJsonObj.put("a", 1);
        testJsonObj.put("b", testJsonObj2);
        testJsonObj2.put("c", 2);
        testJsonObj2.put("d", testJsonObj3);
        testJsonObj3.put("e", 3);

        reqMsg.setContent(testJsonObj);
        verifyHasNeedParams(reqMsg, "a.c", "a", "b", "b.d.e", "d.d");
    }
}
