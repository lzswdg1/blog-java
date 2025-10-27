package com.zw.zw_blog.model.bean.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对应 Sequelize 的 blog_config 模型，用于存储博客全局配置信息
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@TableName("blog_config") // 强制表名为 blog_config
public class Config implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增 (用于管理配置项，即使是单例表也建议保留)
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // blog_name: 博客名称
    @TableField("blog_name")
    private String blogName = "小张的博客";
    
    // blog_avatar: 博客头像
    @TableField("blog_avatar")
    private String blogAvatar = "";
    
    // avatar_bg: 博客头像背景图
    @TableField("avatar_bg")
    private String avatarBg;
    
    // personal_say: 个人签名
    @TableField("personal_say")
    private String personalSay;
    
    // blog_notice: 博客公告
    @TableField("blog_notice")
    private String blogNotice;
    
    // qq_link: qq链接
    @TableField("qq_link")
    private String qqLink;
    
    // we_chat_link: 微信链接
    @TableField("we_chat_link")
    private String weChatLink;
    
    // github_link: github链接
    @TableField("github_link")
    private String githubLink;
    
    // git_ee_link: git_ee链接
    @TableField("git_ee_link")
    private String gitEeLink;
    
    // bilibili_link: bilibili链接
    @TableField("bilibili_link")
    private String bilibiliLink;
    
    // view_time: 博客被访问的次数 (BIGINT -> Long)
    @TableField("view_time")
    private Long viewTime = 0L;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}