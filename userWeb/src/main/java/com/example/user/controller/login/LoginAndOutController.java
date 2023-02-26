package com.example.user.controller.login;

import com.example.pojo.user.User;
import com.example.service.user.UserService;
import com.example.user.annotations.VerifierUser;
import com.example.utils.BaseUtils;
import com.example.utils.Response;
import com.example.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public Response login(
            @VerifierUser User loginUser,
            @RequestParam(name = "username") String username, @RequestParam("password") String password) {
        if (!BaseUtils.isEmpty(loginUser)) {
            //既然已经有sign了，就没必要再制作一个了
            return new Response(200);
        }
        //加盐可以在加密之前加盐，也可以选择在加密之后加盐，甚至还可以两个都这么弄
        //md5固定32位是我们加密后加盐的前提条件

        int checkResult = userService.checkLogin(username, password);
        if (checkResult == 4010) {
            return new Response(4010);
        }
        if (checkResult == 4021) {
            return new Response(4021);
        }
        String realPassword = SignUtils.marshal(password);
        User user = userService.getByUsername(username);
        if (realPassword != user.getPassword()) {
            return new Response(4022);
        }
        String resultSign = SignUtils.makeSign(user.getId());
        HashMap<String, String> result = new HashMap<>();
        result.put("sign", resultSign);
        //如果result字段没有对应的vo对象的话，一定是传进去key-value形式的hashmap，方便人们通过灵活的key取值
        return new Response(200, result);
    }


}
