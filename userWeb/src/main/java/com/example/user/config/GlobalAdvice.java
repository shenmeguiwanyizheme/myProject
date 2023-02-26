package com.example.user.config;

import com.alibaba.fastjson.JSONObject;
import com.example.utils.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalAdvice {

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public Response globalExceptionHandle(RuntimeException exception) {
        if (exception.getMessage() != null) {
            JSONObject jsonObject = JSONObject.parseObject(exception.getMessage());
            Response response = new Response();
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (entry.getKey().equals("code")) {
                    response.setCodeAndMsgByCode((int) entry.getValue());
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("resultVO", entry.getValue());
                    response.setResult(map);

                }

            }
            return response;
        } else {
            return new Response().setCodeAndMsgByCode(4011);
        }

    }
}
