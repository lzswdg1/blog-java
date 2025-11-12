package com.zw.zw_blog.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher; // 假设使用 ip2region
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ToolUtils {

    private static Searcher searcher = null;

    // 静态代码块，用于初始化 IP 查询器
    static {
        try {
            // 注意：你需要将 ip2region.xdb 文件放到 resources 目录下
            String dbPath = "ip2region.xdb";
            // 从 ClassPath 加载资源
            ClassPathResource resource = new ClassPathResource(dbPath);
            InputStream is = resource.getInputStream();
            byte[] dbBin = FileCopyUtils.copyToByteArray(is);
            searcher = Searcher.newWithBuffer(dbBin);
            log.info("ip2region 查询器初始化成功。");
        } catch (Exception e) {
            log.error("初始化 ip2region 查询器失败", e);
        }
    }

    /**
     * 将 IP 转换为归属地
     * (对应 Node.js: tool.js -> getIpAddress)
     * @param ip IP 地址
     * @return 归属地字符串 (例如: 中国|内网IP|0|广东省|深圳市|电信)
     */
    public static String getIpAddress(String ip) {
        if (searcher == null || ip == null) {
            return "未知";
        }
        try {
            long startTime = System.nanoTime();
            String region = searcher.search(ip);
            long cost = TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - startTime);
            log.debug("IP 归属地查询 - IP: {}, Region: {}, 耗时: {} μs", ip, region, cost);

            // 返回原始的复杂字符串，或者你可以解析它
            // 例如: "中国|0|广东省|深圳市|电信"
            if (region != null) {
                // 你可以根据需要解析这个字符串，只返回城市或省份
                String[] parts = region.split("\\|");
                if (parts.length >= 5 && !"0".equals(parts[3])) { // 返回城市
                    return parts[3];
                } else if (parts.length >= 4 && !"0".equals(parts[2])) { // 返回省份
                    return parts[2];
                } else if (!"0".equals(parts[0])) { // 返回国家
                    return parts[0];
                }
            }
            return "未知"; // 默认
        } catch (Exception e) {
            log.error("查询 IP 归属地失败 - IP: {}", ip, e);
            return "未知";
        }
    }
    public static String getRequestIp(HttpServletRequest request) {
        if (request == null) {
            return "未知";
        }
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 对于 X-Forwarded-For，可能存在多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // 对应 Node.js ctx.ip.split(":").pop()
        if (ip != null && ip.contains(":")) {
            // 简单处理 IPv6 本机回环地址
            if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
                return "127.0.0.1";
            }
        }

        return ip;
    }
    /**
     * 生成随机昵称
     * (对应 Node.js: tool.js -> randomNickname)
     * @param prefix 昵称前缀
     * @return "前缀" + 8位随机数
     */
    public static String randomNickname(String prefix) {
        Random random = new Random();
        int suffix = 10000000 + random.nextInt(90000000); // 生成 8 位随机数
        return prefix + suffix;
    }
}