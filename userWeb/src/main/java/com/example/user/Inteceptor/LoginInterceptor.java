package com.example.user.Inteceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final String TOKEN_SECRET_KEY = "my-secret-key";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String url = request.getRequestURI();
        log.info(LoginInterceptor.class + "拦截成功");
        if (url.contains("login")) {
            log.info("登录请求,放行");
            return true;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    log.info("token:" + cookie.getValue()
                    );
                    Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET_KEY);
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    try {
                        DecodedJWT verifyResult = verifier.verify(cookie.getValue());
                        Date expiresAt = verifyResult.getExpiresAt();
                        if (expiresAt.getTime() > System.currentTimeMillis()) {
                            log.info("token is right");
                            return true;
                        }
                    } catch (Exception exception) {
                        log.error("token is wrong");
                        response.setHeader("Connection", "close");
                        HashMap<String, Object> responseMap = new HashMap<>();
                        responseMap.put("code", 4022);
                        throw new RuntimeException(JSONObject.toJSONString(responseMap));
                    }
                }
            }
        }
        response.setStatus(403);
        response.setHeader("Connection", "close");
        log.error("no token available");
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("code", 4022);
        throw new RuntimeException(JSONObject.toJSONString(responseMap));
    }

//    private String generateErrorJson() {
//
//    }
}
