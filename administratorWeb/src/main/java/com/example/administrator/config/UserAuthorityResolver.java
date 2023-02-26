package com.example.administrator.config;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.annotations.VerifierUser;
import com.example.pojo.user.User;
import com.example.service.user.UserService;
import com.example.utils.BaseUtils;
import com.example.utils.SpringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;

public class UserAuthorityResolver implements HandlerMethodArgumentResolver {
    @Resource
    private UserService userService;
    private boolean isCheckAuthority;

    public UserAuthorityResolver(ApplicationArguments arguments) {
        String[] sourceArguments = arguments.getSourceArgs();
        if (sourceArguments.length < 3) {
            isCheckAuthority = true;
            return;
        }
        if (BaseUtils.isEmpty(sourceArguments[2])) {
            isCheckAuthority = true;
        } else {
            isCheckAuthority = Boolean.parseBoolean(sourceArguments[2]);
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return type.isAssignableFrom(User.class) && parameter.hasParameterAnnotation(VerifierUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        if (isCheckAuthority) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest;
            HttpSession session = httpServletRequest.getSession(false);
            if (session == null) return null;
            String signkey = SpringUtils.getProperty("signkey");
            String sign = (String) session.getAttribute(signkey);
            if (BaseUtils.isEmpty(sign)) return null;
            //这个不存在attribute错误的问题，只要有，就是正确，所以直接jsonParse并返回就行
            return JSONObject.parseObject(sign, User.class);
        }
        return userService.getById(BigInteger.valueOf(1));
    }
}
