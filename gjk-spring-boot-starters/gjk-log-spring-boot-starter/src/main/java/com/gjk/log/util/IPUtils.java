package com.gjk.log.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * IPUtils
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:36
 * @description:
 */
@Slf4j
public class IPUtils {


    private static final String LOCAL_IP = "127.0.0.1";
    private static final String BASE_HOST = "0:0:0:0:0:0:0:1";

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            if (request == null) {
                return "";
            }
            ip = request.getHeader("x-forwarded-for");
            if (checkIp(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (checkIp(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (checkIp(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (checkIp(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (checkIp(ip)) {
                ip = request.getRemoteAddr();
                if (LOCAL_IP.equals(ip) || BASE_HOST.equals(ip)) {
                    // 根据网卡取本机配置的IP
                    ip = getLocalAddr();
                }
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR, {}", e.getMessage());
        }

        //使用代理，则获取第一个IP地址
        if (StringUtils.hasText(ip) && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(","));
        }

        return ip;
    }

    /**
     * 校验 IP 是否非法
     *
     * @param ip
     * @return
     */
    private static boolean checkIp(String ip) {
        String unknown = "unknown";
        return !StringUtils.hasText(ip) || ip.length() == 0 || unknown.equalsIgnoreCase(ip);
    }

    /**
     * 获取本机的IP地址
     */
    private static String getLocalAddr() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("InetAddress.getLocalHost()-error, {}", e.getMessage());
        }
        return null;
    }
}
