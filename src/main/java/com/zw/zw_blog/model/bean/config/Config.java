package com.zw.zw_blog.model.bean.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 博客配置表
 * 1:1 完整映射 Node.js sequelize 模型 'blog_config'
 *
 */
@Data
@EqualsAndHashCode(callSuper = false) // 继承 ServiceImpl<M, T> 时建议使用
@TableName("config") // 数据库表名
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 博客名称
     * 对应 Node.js: blog_name
     */
    @TableField("blog_name")
    private String blogName;

    /**
     * 博客头像
     * 对应 Node.js: blog_avatar
     */
    @TableField("blog_avatar")
    private String blogAvatar;

    /**
     * 博客头像背景图
     * 对应 Node.js: avatar_bg
     */
    @TableField("avatar_bg")
    private String avatarBg;

    /**
     * 博客公告
     * 对应 Node.js: blog_notice
     */
    @TableField("blog_notice")
    private String blogNotice;

    /**
     * 博客简介
     * 对应 Node.js: introduction
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 作者
     * 对应 Node.js: author
     */
    @TableField("author")
    private String author;

    /**
     * 作者简介
     * 对应 Node.js: author_info
     */
    @TableField("author_info")
    private String authorInfo;

    /**
     * 备案号
     * 对应 Node.js: record_num
     */
    @TableField("record_num")
    private String recordNum;

    /**
     * QQ 链接
     * 对应 Node.js: qq_link
     */
    @TableField("qq_link")
    private String qqLink;

    /**
     * 微信链接
     * 对应 Node.js: we_chat_link
     */
    @TableField("we_chat_link")
    private String weChatLink;

    /**
     * Gitee 链接
     * 对应 Node.js: git_ee_link
     */
    @TableField("git_ee_link")
    private String giteeLink; // [架构师修复] 统一 Java 命名为 giteeLink

    /**
     * Github 链接
     * 对应 Node.js: github_link
     */
    @TableField("github_link")
    private String githubLink;

    /**
     * Bilibili 链接
     * 对应 Node.js: bilibili_link
     */
    @TableField("bilibili_link")
    private String bilibiliLink;

    /**
     * 微信群
     * 对应 Node.js: we_chat_group
     */
    @TableField("we_chat_group")
    private String weChatGroup;

    /**
     * QQ 群
     * 对应 Node.js: qq_group
     */
    @TableField("qq_group")
    private String qqGroup;

    /**
     * 微信收款码
     * 对应 Node.js: we_chat_pay
     */
    @TableField("we_chat_pay")
    private String weChatPay;

    /**
     * 支付宝收款码
     * 对应 Node.js: ali_pay
     */
    @TableField("ali_pay")
    private String aliPay;

    /**
     * 是否显示音乐 (1: 是, 0: 否)
     * 对应 Node.js: show_music
     */
    @TableField("show_music")
    private Integer showMusic;

    /**
     * 网站访问量
     * 对应 Node.js: view_times
     * [架构师修复] 纠正：你之前误用为 viewTime (单数)
     */
    @TableField("view_times")
    private Long viewTimes;

    /**
     * 文章总数
     * 对应 Node.js: article_count
     */
    @TableField("article_count")
    private Integer articleCount;

    /**
     * 分类总数
     * 对应 Node.js: category_count
     */
    @TableField("category_count")
    private Integer categoryCount;

    /**
     * 标签总数
     * 对应 Node.js: tag_count
     */
    @TableField("tag_count")
    private Integer tagCount;

    /**
     * 个人签名
     * 对应 Node.js: personal_say
     */
    @TableField("personal_say")
    private String personalSay;

    // createdAt 和 updatedAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}