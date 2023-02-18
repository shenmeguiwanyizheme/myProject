package com.example;

import com.example.constSetting.ResponseCodeSetting;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response {
    private Integer code;
    private String Msg;
    private Object resultVO;

    public Response addCodeAndMsgByCode(int code) {
        this.setCode(code);
        this.setMsg(ResponseCodeSetting.getCodeMessage(code
        ));
        return this;
    }
}
