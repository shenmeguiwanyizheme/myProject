package com.example.utils;

import com.example.constSetting.DateConsts;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class BaseUtils {
    public static int CurrentSeconds() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof List<?>) {
            return ((List<?>) obj).size() == 0;
        }
        if (obj instanceof String) {
            return "".equals(obj);
        }
        if (obj instanceof Number) {
            DecimalFormat decimalFormat = new DecimalFormat();
            try {
                return decimalFormat.parse(obj.toString()).doubleValue() == 0;
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static String md5(String text) {
        String encodeStr = "";
        try {
            encodeStr = DigestUtils.md5Hex(text);
        } catch (Exception e) {
            return encodeStr;
        }
        return encodeStr;
    }

    public static double getFileSize(Long len, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        return fileSize;
    }

    public static boolean isNumeric(String str) {
        if (null == str) return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static Date getDateByString(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(DateConsts.PATTERN);
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        return date;
    }

    public static String getRandomString() {
        String originStr = "qwertyuiop_asdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //???????????????????????????????????????????????????
        int defaultSize = 30;
        return RandomStringUtils.random(defaultSize, originStr);
    }

    public static String getRandomString(int size) {
        if (size < 1) {
            return getRandomString();
        }
        String originStr = "qwertyuiop_asdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //???????????????????????????????????????????????????
        return RandomStringUtils.random(size, originStr);
    }

    public static String formatPriceNum(int price) {
        int points = price % 100;
        if (price == 0) {
            return price + ".00";
        } else {
            return price + "." + new DecimalFormat("00").format(points);
        }
    }

    public static String formatPrice(int price) {
        String prefix = "???";
        return prefix + formatPriceNum(price);
    }

    public static String formatPrice(BigInteger price) {//price ???????????????
        int priceInt = price.intValue();
        return formatPrice(priceInt);
    }

    public static String getRandomNumber(int num) {
        Random random = new Random();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < num; i++) {
            sbf.append((random.nextInt(9)) + 1);
        }
        return sbf.toString();
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param seconds
     * @return
     */
    public static String timeStamp2Date(int seconds) {
        SimpleDateFormat format = new SimpleDateFormat(DateConsts.PATTERN);
        return format.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static String timeStamp2DateGMT(int seconds) {
        SimpleDateFormat format = new SimpleDateFormat(DateConsts.PATTERN, Locale.US);
        return format.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static String timeStamp2Date(int seconds, String pattern) {
        if (isEmpty(seconds)) return "";
        if (pattern == null || Pattern.matches("\\s", pattern)) {
            pattern = DateConsts.PATTERN;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static String timeStamp2DateGMT(int seconds, String format) {
        if (isEmpty(seconds)) {
            return "";
        }
        if (format == null || Pattern.matches("\\s", format)) {
            format = DateConsts.PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * ????????????????????????????????????
     *
     * @param date_str
     * @return
     */
    public static int date2TimeStamp(String date_str) {
        return date2TimeStamp(date_str, null);
    }

    public static int date2TimeStamp(String date_str, String format) {
        if (format == null || Pattern.matches("\\s", format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(date_str + "000");
            int timeStamp = (int) (date.getTime() / 1000);
            if (timeStamp >= 1000) {
                return (int) (date.getTime() / 1000);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatWeight(int weight) {
        String endFix = "Kg";
        int points = (weight % 1000);
        if (points == 0) {
            return (weight / 1000) + ".00" + endFix;
        } else {
            return (weight / 1000) + "." + points + endFix;
        }
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param param1
     * @param param2
     * @return
     */
    public static String implodeSearchParam(String param1, String param2) {
        String result;
        if (BaseUtils.isEmpty(param1) && BaseUtils.isEmpty(param2)) {
            result = null;
        } else if (!BaseUtils.isEmpty(param1) && !BaseUtils.isEmpty(param2)) {
            result = param1 + "," + param2;
        } else {
            result = BaseUtils.isEmpty(param1) ? param2 : param1;
        }
        return result;
    }

    public static String getShortTime(Integer dateline) {
        String shortstring;
        String time = timeStamp2Date(dateline);//????????????
        Date date = getDateByString(time);//??????????????????????????????????????????????????????????????????
        long now = System.currentTimeMillis();
        long deltime = (now - date.getTime()) / 1000;
        if (deltime > 365 * 24 * 60 * 60) {
            shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "??????";
        } else if (deltime > 24 * 60 * 60) {
            shortstring = (int) (deltime / (24 * 60 * 60)) + "??????";
        } else if (deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "?????????";
        } else if (deltime > 60) {
            shortstring = (int) (deltime / (60)) + "??????";
        } else if (deltime > 1) {
            shortstring = deltime + "??????";
        } else {
            shortstring = "1??????";
        }
        return shortstring;
    }
    
}
