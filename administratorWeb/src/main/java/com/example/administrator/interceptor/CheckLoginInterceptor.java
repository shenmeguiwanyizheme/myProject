package com.example.administrator.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截成功");
        String requestURI = request.getRequestURI();
        if (requestURI.contains("login") || requestURI.contains("js") || requestURI.contains("css") || requestURI.contains("jsp")) {
            log.info("豁免放行");
            return true;
            //登录界面放行
        }
        HttpSession session = request.getSession();
        //不应该存在context中，应该存在session中,对应一次生命周期完整的TCP连接
        Object cookieString = session.getAttribute("LoginCookie");
        if (cookieString != null) {
            log.info("登录成功放行");
            return true;
        } else {
            log.info("请登录后重试");
            response.setStatus(403);
            return false;
        }
    }

    //页面跳转的事情由前端控制
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
