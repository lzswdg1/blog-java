package com.zw.zw_blog.model.vo.statistic;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 用于展示网站统计数据的 VO
 * 对应 controller/statistic/index.js getStatistic 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class StatisticVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章总数
     * 对应 getStatistic 中的 articleCount
     */
    @JsonProperty("article_count")
    private Long articleCount;

    /**
     * 评论总数
     * 对应 getStatistic 中的 commentCount
     */
    @JsonProperty("comment_count")
    private Long commentCount;

    /**
     * 用户总数
     * 对应 getStatistic 中的 userCount
     */
    @JsonProperty("user_count")
    private Long userCount;

    /**
     * 标签总数
     * 对应 getStatistic 中的 tagCount
     */
    @JsonProperty("tag_count")
    private Long tagCount;

    /**
     * 分类总数
     * 对应 getStatistic 中的 categoryCount
     */
    @JsonProperty("category_count")
    private Long categoryCount;

    /**
     * 说说总数
     * 对应 getStatistic 中的 talkCount
     */
    @JsonProperty("talk_count")
    private Long talkCount;

    /**
     * 留言总数
     * 对应 getStatistic 中的 messageCount
     */
    @JsonProperty("message_count")
    private Long messageCount;

    // 你可以根据 getStatistic 方法实际返回的其他字段继续添加
}