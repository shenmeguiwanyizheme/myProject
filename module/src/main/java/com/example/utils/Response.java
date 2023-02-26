package com.example.utils;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response {
    private Integer code;
    private String Msg;
    //result对应的必须是一个VO类或者是一个HashMap
    private Object result;

    public Response() {

    }

    public Response(int code) {
        this.setCode(code);
        this.setMsg(ResponseCode.getCodeMessage(code
        ));
    }
    //仅仅是为了维护之前的代码

    public Response setCodeAndMsgByCode(int code) {
        this.setCode(code);
        this.setMsg(ResponseCode.getCodeMessage(code
        ));
        result = null;
        return this;
    }

    public <T> Response(int code, T info) {
        this.code = code;
        Msg = ResponseCode.getCodeMessage(code);
        result = info;
    }
}
