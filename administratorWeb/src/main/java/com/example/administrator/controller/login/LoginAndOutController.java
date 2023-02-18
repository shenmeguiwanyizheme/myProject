package com.example.administrator.controller.login;

import com.example.administrator.domain.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class LoginController {
    @RequestMapping("/login")
    public Response login(@RequestParam(name = "username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        // check username and password
        // then
        HttpSession session = request.getSession();

        session.setAttribute("user", username);
        Cookie cookie = new Cookie("code", UUID.randomUUID().toString());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
        return new Response().setCode(200).setMsg("ok");
    }
}
