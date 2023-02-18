package com.example.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Response;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

@RestController
@Slf4j
public class LoginAndOutController {
    private static final String beginSalt = "sss";
    private static final String endSalt = "qqq";
    private static final String TOKEN_SECRET_KEY = "my-secret-key";
    @Resource
    UserService userService;

    @RequestMapping("/login")
    public Response login(@RequestParam(name = "username") String username, @RequestParam("password") String password,
                          HttpServletRequest request, HttpServletResponse response) {
//        User user = userService.extractByUsername(username);
//        String md5Password = DigestUtils.md5DigestAsHex(new StringBuilder(beginSalt).append(password)
//                .append(endSalt).toString().getBytes(StandardCharsets.UTF_8));
//        if (!md5Password.equals(user.getPassword())) {
//            log.info("username or password unmatched ,login  failed");
//            return new Response().setCode(403).setMsg("username or password unmatched ,login  failed");
//        }
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET_KEY);
        String jwtToken = JWT.create().withClaim("username", username).
                withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .sign(algorithm);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", jwtToken);
        return new Response().setCode(200).setResultVO(map);
    }

    @RequestMapping("/token_renew")
    public Response tokenRenew(@RequestParam("token") String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verifyResult;
        String username;
        Date date;
        try {
            verifyResult = verifier.verify(token);
            username = verifyResult.getClaim("username").asString();
        } catch (Exception exception) {
            log.error("token is wrong");
            return new Response().setCode(4022);
        }
        date = verifyResult.getExpiresAt();
        if (date.getTime() > System.currentTimeMillis()) {
            String newToken = JWT.create().withClaim("username", username).
                    withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 100L))
                    .sign(algorithm);
            log.info("user:" + username + "has got the new token");
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", newToken);
            return new Response().setCode(200).setResultVO(map);
        } else {
            log.error("token has been invalid");
            return new Response().setCode(4022);
        }
    }

}
