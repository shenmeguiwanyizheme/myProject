package com.example.administrator.controller.login;

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
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@Slf4j
public class LoginAndOutController {
    private static final String beginSalt = "sss";
    private static final String endSalt = "qqq";
    //不加responsebody就会返回当前视图，导致无限循环这个路径
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
        HttpSession session = request.getSession();
        String cookieString = UUID.randomUUID().toString();
        System.out.println(cookieString);
        session.setAttribute("LoginCookie", cookieString);
        return new Response().addCodeAndMsgByCode(200);
    }

    @RequestMapping("/logout")
    public Response logout(HttpSession session) {
        session.invalidate();
        return new Response().setCode(200);
    }
}
