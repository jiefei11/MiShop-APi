package com.bhgeek.mishopapi.common;

public class ServicesException extends RuntimeException implements CommonResp {

    private CommonResp commonResp;

    //直接接收RespBeanEnum的传参用于构造业务异常
    public ServicesException(CommonResp commonResp) {
        super();    //调用父类的无参构造方法
        this.commonResp = commonResp;
    }

    //接收自定义msg的方式构造业务异常
    public ServicesException(String msg, CommonResp commonResp) {
        super();
        this.commonResp = commonResp;
        this.commonResp.setMsg(msg);
    }


    @Override
    public Integer getCode() {
        return this.commonResp.getCode();
    }

    @Override
    public String getMsg() {
        return this.commonResp.getMsg();
    }

    @Override
    public CommonResp setMsg(String msg) {
        this.commonResp.setMsg(msg);
        return this;
    }

}
