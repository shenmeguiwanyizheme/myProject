package com.example.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtils {
    public static final String[] unNeededMatchesNumbersAsPhone = {};

    public static boolean isPhoneNumber(String phoneString) {
        if (ArrayUtils.contains(unNeededMatchesNumbersAsPhone, phoneString)) {
            return true;
        }
        final String regex = "[1|2]\\d{10}";
        return Pattern.matches(regex, phoneString);
    }

    public static boolean isEmail(String emailString) {
        String regex = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
        //pattern.matches函数的实现
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(emailString);
        return matcher.matches();
    }
}
//Pattern在java.util.regex包中，是正则表达式的编译表示形式，此类的实例是不可变的，可供多个并发线程安全使用。