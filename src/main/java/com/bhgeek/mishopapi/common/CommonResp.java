package com.bhgeek.mishopapi.common;

/**
 * @Author LZDWTL
 * @Date 2021-12-06 15:59
 * @ClassName CommonResp
 * @Description 返回对象的接口，装饰者模式
 */
public interface CommonResp {
    Integer getCode();

    String getMsg();

    CommonResp setMsg(String msg);
}
