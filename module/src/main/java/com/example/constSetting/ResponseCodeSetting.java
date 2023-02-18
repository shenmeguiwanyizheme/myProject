package com.example.constSetting;

import java.util.HashMap;

public class ErrorCodeSetting {
    private static final HashMap<Integer, String> errorCodeMap = new HashMap<>();

    static {
        errorCodeMap.put(4010, "用户输入的数据不存在或者用户所需的数据不存在");
        errorCodeMap.put(4012, "该文件类型禁止上传");
        errorCodeMap.put(4013, "文件或文件目录创建失败，请检查文件，稍后再试");
        errorCodeMap.put(4014, "文件存储失败，请检查文件，稍后再试");
        errorCodeMap.put(4015, "文件的后缀名有误");
        errorCodeMap.put(4021, "用户认证失败，账号与密码不匹配或者账号不存在");
        errorCodeMap.put(4022, "用户凭证错误/被伪造");
        errorCodeMap.put(4011, "未知的异常，请检查网址是否输入正确，若正确请联系管理员或客服处理错误");
    }

    public static String getCodeMessage(int code) {
        return errorCodeMap.get(code);
    }
}
