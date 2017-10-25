package com.edusys.front.common;

/**
 * upms系统接口结果常量枚举类
 * Created by Gary on 2017/2/18.
 */
public enum SysResultConstant {

    FAILED(0, "failed"),
    SUCCESS(1, "success"),

    INVALID_LENGTH(10001, "Invalid length"),

    EMPTY_USERNAME(10101, "账号不能为空！"),
    EMPTY_PASSWORD(10102, "密码不能为空！"),
    INVALID_USERNAME(10103, "该学员尚未注册！"),
    INVALID_PASSWORD(10104, "密码错误！"),
    INVALID_ACCOUNT(10105, "Invalid account");

    public int code;

    public String message;

    SysResultConstant(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
