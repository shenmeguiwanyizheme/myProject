package com.example.user.config;

import com.example.pojo.user.User;
import com.example.service.user.UserService;
import com.example.user.annotations.VerifierUser;
import com.example.utils.BaseUtils;
import com.example.utils.SignUtils;
import com.example.utils.SpringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
                                  WebDataBinderFactory binderFactory) throws Exception {
        if (isCheckAuthority) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest;
            String sign = httpServletRequest.getHeader(SpringUtils.getProperty("signkey"));
            if (BaseUtils.isEmpty(sign)) return null;
            BigInteger id = SignUtils.parseSign(sign);
            if (id == null) return null;
            return userService.getById(id);
        }
        return userService.getById(BigInteger.valueOf(1));
    }
}
