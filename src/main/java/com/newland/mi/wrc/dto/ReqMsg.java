package com.newland.mi.wrc.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.newland.mi.wrc.utils.TimeUtils;
import com.newland.mi.wrc.utils.UUIDUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Miracle
 * 封装请求报文
 */
public class ReqMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "header为空")
    private Header header;
    @NotNull(message = "content为空")
    private JSONObject content;


    private ReqMsg() {
    }

    /**
     * 用于服务内部请求
     */
    public static ReqMsg newInstance() {
        ReqMsg reqMsg = new ReqMsg();
        Header reqHeader = reqMsg.new Header();
        reqHeader.setReqSeq(UUIDUtils.create());
        reqHeader.setReqDateTime(TimeUtils.getSimpleDateTime());
        reqHeader.setDeviceId("server");
        reqHeader.setClientType("999");
        reqMsg.setHeader(reqHeader);
        reqMsg.setContent(new JSONObject());
        return reqMsg;
    }

    @NotNull
    public Header getHeader() {
        return header;
    }

    public void setHeader(@NotNull Header header) {
        this.header = header;
    }

    @NotNull
    public JSONObject getContent() {
        return content;
    }

    public ReqMsg setContent(@NotNull JSONObject content) {
        this.content = content;
        return this;
    }

    /**
     * 将content转换为对应实体对象
     */
    public <T> T getContentObject(Class<T> clazz) {
        return JSON.parseObject(content.toJSONString(), clazz);
    }

    /**
     * 将jsonStr转换为ReqMsg
     */
    public static ReqMsg parseJSONString(String jsonString) {
        return JSON.parseObject(jsonString, ReqMsg.class);
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    /**
     * 将ReqMsg转换为jsonStr
     */
    public String toJSONString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 将ReqMsg转换为jsonObj
     */
    public JSONObject toJSONObj() {
        return JSON.parseObject(this.toJSONString());
    }


    /**
     * 请求体的Header
     */
    public class Header implements Serializable {
        private static final long serialVersionUID = -3469684736333563837L;

        /**
         * 请求序列，每个 HTTP/HTTPS 请求的标识
         */
        private String reqSeq;

        /**
         * 格式为 yyyy-mm-dd hh:mm:ss（年-月-日 时:分:秒）
         */
        private String reqDateTime;

        /**
         * 发起请求的设备唯一标识，取不到唯一标识制空
         */
        private String deviceId;

        /**
         * 发起请求的客户端类型（100：Android; 101：IOS;102：H5; 999:服务端内部调用）
         */
        private String clientType;

        public Header() {
        }

        /**
         * 效验用户的Token
         */
        private String userToken;

        /**
         * 用户ID
         */
        private String userId;


        public String getReqSeq() {
            return reqSeq;
        }

        public void setReqSeq(String reqSeq) {
            this.reqSeq = reqSeq;
        }

        public String getReqDateTime() {
            return reqDateTime;
        }

        public void setReqDateTime(String reqDateTime) {
            this.reqDateTime = reqDateTime;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

}
