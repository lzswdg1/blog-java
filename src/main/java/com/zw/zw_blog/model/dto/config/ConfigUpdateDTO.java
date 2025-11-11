package com.zw.zw_blog.model.dto.config;

import lombok.Data;

@Data
public class ConfigUpdateDTO {
    // --- 你已有的字段 ---
    private String blogName;
    private String blogAvatar;
    private String blogNotice;
    private String introduction;
    private String author;
    private String authorInfo;
    private String recordNum;
    private Integer articleCount;
    private Integer categoryCount;
    private Integer tagCount;
    private String giteeLink;
    private String githubLink;
    private String bilibiliLink;
    private String weChatPay;
    private String aliPay;


    private String avatarBg;
    private String qqLink;
    private String weChatLink;
    private String weChatGroup;
    private String qqGroup;
    private Integer showMusic; // 对应数据库的 Integer (0 或 1)
}