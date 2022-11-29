package com.bhgeek.mishopapi.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DealExceptionHandler {

    //指定出现什么异常值执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody//为了返回数据
    public RespBean defaultExceptionHandler(Exception ex) {
        ex.printStackTrace();
        return new RespBean("发现全局异常，正在处理，请刷新再试");
    }

    //指定TokenException异常值执行这个方法
    @ExceptionHandler(ServicesException.class)
    @ResponseBody//为了返回数据
    public RespBean tokenExceptionHandler(ServicesException ex) {
        ex.printStackTrace();
        return new RespBean("错误:"+ex.getCode()+ "."+ex.getMsg());
    }
}
