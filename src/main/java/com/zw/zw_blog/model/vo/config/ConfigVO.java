package com.zw.zw_blog.model.vo.config;


import com.fasterxml.jackson.annotation.JsonProperty; // 用于 JSON 字段名映射
import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * 用于展示网站配置信息的 VO
 * 对应 controller/config/index.js [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/config/index.js] getConfig 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class ConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 JSON 中的 'website_name'
     * 使用 @JsonProperty 将 JSON 的 snake_case 映射到 Java 的 camelCase
     */
    @JsonProperty("website_name")
    private String websiteName;

    /**
     * 对应 JSON 中的 'website_avatar'
     */
    @JsonProperty("website_avatar")
    private String websiteAvatar;

    /**
     * 对应 JSON 中的 'website_notice'
     */
    @JsonProperty("website_notice")
    private String websiteNotice;

    /**
     * 对应 JSON 中的 'social_urls' (一个包含社交链接的对象)
     * 使用 Map<String, String> 来接收不确定的键值对
     */
    @JsonProperty("social_urls")
    private Map<String, String> socialUrls;

    /**
     * 对应 JSON 中的 'show_music'
     */
    @JsonProperty("show_music")
    private Boolean showMusic;
}