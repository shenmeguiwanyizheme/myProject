package com.example.administrator.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.annotations.VerifierUser;
import com.example.pojo.user.User;
import com.example.service.user.UserService;
import com.example.utils.BaseUtils;
import com.example.utils.Response;
import com.example.utils.SignUtils;
import com.example.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
public class LoginAndOutController {
    private static final String beginSalt = "sss";
    private static final String endSalt = "qqq";
    //不加responsebody就会返回当前视图，导致无限循环这个路径
    @Resource
    UserService userService;

    @RequestMapping("/login")
    public Response login(@VerifierUser User loginUser,
                          HttpSession session,
                          @RequestParam(name = "username") String username, @RequestParam("password") String password,
                          HttpServletRequest request, HttpServletResponse response) {
        if (!BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        User user = userService.extractByUsername(username);
        //加盐的策略是奇数下标为随机字符，这是还原为原始的md5加密后的密码
        String realPassword = SignUtils.marshal(password);
        if (!user.getPassword().equals(realPassword)) {
            log.info("username or password unmatched ,login  failed");
            return new Response().setCodeAndMsgByCode(4021);
        }
        String userJson = JSONObject.toJSONString(user);
        session.setAttribute(SpringUtils.getProperty("signkey"), userJson);
        return new Response(200);
    }

    @RequestMapping("/logout")
    public Response logout(@VerifierUser User loginUser,
                           HttpSession session) {
        if (!BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        session.invalidate();
        return new Response().setCode(200);
    }

    @RequestMapping("/register")
    public Response register(
            @VerifierUser User loginUser,
            @RequestParam("phone") String phoneNumber,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        if (!BaseUtils.isEmpty(loginUser)) {
            return new Response(200);
        }
        User user = userService.extractByUsername(phoneNumber);
        if (user == null) {
            return new Response(4022);
        }

        String signPassword = SignUtils.marshal(password);
        int affectedRow = userService.insert(new User().setUsername(username).setPassword(signPassword));
        return new Response(200);
    }
}
