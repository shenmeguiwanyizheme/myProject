package com.example.administrator.interceptor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Component
@Slf4j
public class ErrorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info(ErrorInterceptor.class + "拦截成功");
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", 4011);
        HashMap<String, Object> resultVOMap = new HashMap<>();
        jsonMap.put("resultVO", resultVOMap);
        throw new RuntimeException(JSONObject.toJSONString(resultVOMap));
    }
}
