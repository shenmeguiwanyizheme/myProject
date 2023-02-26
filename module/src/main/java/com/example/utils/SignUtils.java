package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.pojo.user.UserSign;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
public class SignUtils {
    private static String USER_SIGH_SALT = "USER";
    private static String PASSWORD_SALT = "PASSWORD";
    private static int EXPIRE_TIME = 60 * 60;

    public static String makeSign(BigInteger id) {
        UserSign userSign = new UserSign();
        userSign.setId(id);
        userSign.setExpireTime(BaseUtils.CurrentSeconds() + EXPIRE_TIME);
        byte[] bytes = Base64.getUrlEncoder().encode(
                JSONObject.toJSONString(userSign)
                        .getBytes(StandardCharsets.UTF_8));
        return (new String(bytes, StandardCharsets.UTF_8) + USER_SIGH_SALT).trim();
    }

    public static BigInteger parseSign(String sign) {
        if (sign == null) return null;
        sign = sign.substring(0, sign.length() - 1 - USER_SIGH_SALT.length());
        byte[] decode = Base64.getUrlDecoder().decode(sign.getBytes(StandardCharsets.UTF_8));
        //对于sign错误的标准处理方法如下，先异常捕获但是忽略，主要是判断sign解出来是不是空
        UserSign userSign = null;
        try {
            userSign = (UserSign) JSONObject.parseObject(decode, UserSign.class);
        } catch (Exception exception) {
            //ignore
        }
        if (userSign == null) {
            return null;
        }

        if (userSign.getExpireTime() < BaseUtils.CurrentSeconds()) {
            return null;
        }
        return userSign.getId();

    }

    public static String marshal(String password) {//???着什么意思
        String encodeStr = "";
        try {
            encodeStr = DigestUtils.md5Hex(password.getBytes(StandardCharsets.UTF_8));

        } catch (Exception exception) {
            return encodeStr;
        }
        return encodeStr;
    }


}
