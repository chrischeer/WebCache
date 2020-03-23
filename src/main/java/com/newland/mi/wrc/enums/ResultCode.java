package com.newland.mi.wrc.enums;


/**
 * @author Miracle
 * 返回code
 */

public enum ResultCode {
    //平台分类(2位)+服务标识(3位)+一级分类(1位)+二级分类(4位)

    /**
     * 请求成功
     */
    SUCCESS("0", "请求成功"),
    //================系统错误码=====================
    SYSTEM_ERROR_10000("10000", "未知系统错误"),
    PARAMETER_MISS_10001("10001", "参数缺失"),
    PARAMETER_ERROR_10002("10002", "参数错误"),
    ERROR_SQL_10003("10003", "数据库操作错误"),
    ERROR_NET_RPC_10004("10004", "内部网络请求失败"),
    TOKEN_VERIFY_ERROR_10005("10005", "token验证不通过"),

    //================业务错误码=====================
    USER_OFFLINE_00000("00000", "用户不在线");

    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
