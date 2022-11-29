package com.bhgeek.mishopapi.common;

public enum RespBeanEnum implements CommonResp{

    SUCCESS(200,"请求成功!"),
    ERROR(500,"服务器响应错误！"),

    /** 10XX 表示用户错误*/
    USER_REGISTER_FAILED(1001, "注册失败"),
    VALIDATION_ERROR(1016,"验证码错误"),
    USER_ACCOUNT_EXISTED(1002,"手机号已注册"),
    USER_ACCOUNT_NOT_EXIST(1003,"用户名不存在"),
    USERNAME_PASSWORD_ERROR(1004,"用户名或密码错误"),
    PASSWORD_ERROR(1005,"密码错误"),
    USER_ACCOUNT_EXPIRED(1006,"账号过期"),
    USER_PASSWORD_EXPIRED(1007,"密码过期"),
    USER_ACCOUNT_DISABLE(1008,"账号不可用"),
    USER_ACCOUNT_LOCKED(1009,"你的账号账号已被锁定,请联系管理员"),
    USER_NOT_LOGIN(1010,"用户未登陆"),
    USER_NO_PERMISSIONS(1011,"用户权限不足"),
    USER_SESSION_INVALID(1012,"会话已超时"),
    USER_ACCOUNT_LOGIN_IN_OTHER_PLACE(1013,"账号超时或账号在另一个地方登陆"),
    TOKEN_VALIDATE_FAILED(1014,"Token令牌验证失败"),
    LIKE_ALREADY_GICED(1015,"请勿重复点赞"),



    /** 20XX 表示服务器错误 */
    PICTURE_UPLOAD_FAILED(2001,"上传图片失败"),
    GIVE_LIKE_FAILED(2002,"点赞失败"),
    PICTURE_LOAD_FAILED(2003,"图片加载失败"),
    UPDATE_USER_INFO_FAILED(2004,"修改用户信息失败"),
    UPDATE_USER_PASSWORD_FAILED(2005,"修改密码失败"),
    ;

    private Integer code;
    private String msg;

    RespBeanEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public CommonResp setMsg(String msg) {
        this.msg=msg;
        return this;
    }
}
