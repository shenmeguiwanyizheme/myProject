package com.example.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtils {
    public static String getLocalAddress() {
        String addStr = "";
        try {
            addStr = InetAddress.getLocalHost().getHostAddress();//拿到的是正在使用的网卡的ip地址，因为ipconfig会显示多个网卡的地址
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return addStr;
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarder-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //request的attribute应该是java中的概念，attribute存在于web容器中，而且只在request阶段生效，适用于请求转发
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("127.0.0.1")) {
            return getLocalAddress();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
