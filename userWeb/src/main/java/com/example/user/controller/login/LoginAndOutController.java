package com.example.user.controller.login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Response;
import com.example.pojo.user.User;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
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
        User user = userService.extractByUsername(username);
        if (user == null) {
            log.error("用户不存在");
            return new Response().addCodeAndMsgByCode(4010);
        }
        char[] chars = user.getPassword().toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if ((i & 1) == 0) {
                builder.append(chars[i]);
            }
        }//加盐的策略是奇数下标为随机字符，这是还原为原始的md5加密后的密码
        String realMd5Password = builder.toString();
        String paramMd5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!realMd5Password.equals(paramMd5Password)) {
            log.info("username or password unmatched ,login  failed");
            return new Response().addCodeAndMsgByCode(4021);
        }
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET_KEY);
        String jwtToken = JWT.create().withClaim("username", username).
                withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .sign(algorithm);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", jwtToken);
        return new Response().addCodeAndMsgByCode(200).setResultVO(map);
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
