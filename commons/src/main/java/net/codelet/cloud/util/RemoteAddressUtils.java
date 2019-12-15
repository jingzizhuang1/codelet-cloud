package net.codelet.cloud.util;

import net.codelet.cloud.constant.HttpRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * 远程地址工具。
 */
public class RemoteAddressUtils {

    /**
     * 取得客户端远程 IP 地址。
     * @param request HTTP 请求
     * @return 客户端远程 IP 地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr;
        if (!StringUtils.isEmpty(remoteAddr = request.getHeader(HttpRequestAttributes.REAL_IP))) {
            return remoteAddr;
        }
        if (!StringUtils.isEmpty(remoteAddr = request.getHeader(HttpRequestAttributes.FORWARDED_FOR))) {
            return remoteAddr.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
