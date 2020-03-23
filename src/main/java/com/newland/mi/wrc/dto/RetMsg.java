package com.newland.mi.wrc.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newland.mi.wrc.enums.ResultCode;
import com.newland.mi.wrc.exception.ResultException;
import com.newland.mi.wrc.utils.TimeUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RetMsg implements Serializable {
    private Header header;
    private Object content;

    @JsonIgnore
    @JSONField(serialize = false)
    private Map<String, Object> contentMap = new HashMap<>();
    /**
     * 存放回话的seq
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public static ThreadLocal<String> myThreadLoad = new ThreadLocal<>();

    /**
     * 错误码前缀：平台标识+服务标识 todo 后期提取到配置文件
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public static String platformAndServiceCode = "10001";

    private RetMsg() {
    }

    /**
     * 关联reqMsg,填充retMsg的header中相关的参数
     */
    public static RetMsg newInstance() {
        RetMsg retMsg = new RetMsg();
        Header retMsgHeader = retMsg.new Header();
        retMsgHeader.setRetSeq(myThreadLoad.get());
        retMsgHeader.setRetCode(ResultCode.SUCCESS.getCode());
        retMsgHeader.setRetDateTime(TimeUtils.getSimpleDateTime());
        retMsgHeader.setRetMsg(ResultCode.SUCCESS.getMsg());
        retMsg.setHeader(retMsgHeader);
        retMsg.setContent(new JSONObject());
        //释放资源，重要
        RetMsg.myThreadLoad.remove();
        return retMsg;
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    /**
     * 将 retMsg 转换为 JsonStr
     */
    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    /**
     * 将 retMsg 转换为 jsonObj
     */
    public JSONObject toJSONObj() {
        return JSON.parseObject(this.toJSONString());
    }

    /**
     * 将content转换为jsonObj
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public JSONObject getContentJsonObj() {
        return JSON.parseObject(JSON.toJSONString(content));
    }

    public void checkRetMsgStatus() {
        if (!this.getHeader().getRetCode().equals(ResultCode.SUCCESS.getCode())) {
            throw new ResultException(this);
        }
    }

    public Header getHeader() {
        return header;
    }

    public RetMsg setHeader(Header header) {
        this.header = header;
        return this;
    }

    public Object getContent() {
        return content;
    }

    /**
     * 在content中直接放入json对象
     */
    public RetMsg setContent(Object content) {
        this.content = content;
        return this;
    }

    /**
     * 在content中添加key-value
     */
    public RetMsg putContent(String key, Object value) {
        contentMap.put(key, value);
        this.content = contentMap;
        return this;
    }


    /**
     * 结果代码和描述
     */
    public RetMsg setRespCodeAndDesc(ResultCode resultCode, String respDesc) {
        this.header.retCode = platformAndServiceCode + resultCode.getCode();
        this.header.retMsg = respDesc;
        return this;
    }

    /**
     * 结果代码和描述
     */
    public RetMsg setException(ResultException exception) {
        if (exception.getErrorCode().length() == 5) {
            this.header.retCode = platformAndServiceCode + exception.getErrorCode();
        } else {
            //来自其他服务传递来的错误码
            this.header.retCode = exception.getErrorCode();
        }
        this.header.retMsg = exception.getErrorMsg();
        return this;
    }


    /**
     * 返回体头部
     */
    public class Header implements Serializable {
        private static final long serialVersionUID = -2145617659146748941L;

        /**
         * 返回请求序列.
         */
        private String retSeq;

        /**
         * 响应时间.
         */
        private String retDateTime;

        /**
         * 响应状态码.
         */
        private String retCode;

        /**
         * 响应描述.
         */
        private String retMsg;

        public String getRetSeq() {
            return retSeq;
        }

        public void setRetSeq(String retSeq) {
            this.retSeq = retSeq;
        }

        public String getRetDateTime() {
            return retDateTime;
        }

        public void setRetDateTime(String retDateTime) {
            this.retDateTime = retDateTime;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetMsg() {
            return retMsg;
        }

        public void setRetMsg(String retMsg) {
            this.retMsg = retMsg;
        }
    }

}
