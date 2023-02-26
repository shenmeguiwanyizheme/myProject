package com.example.user.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
//用户端只有登录的时候需要校验，管理端的所有接口都需要校验
public @interface VerifierUser {

}
