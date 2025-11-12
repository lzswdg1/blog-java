package com.zw.zw_blog.model.vo.recommend;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecommendVO {

    private Long id;

    /**
     * 对应 'title'
     */
    private String title;

    /**
     * 对应 'content'
     */
    private String content;

    /**
     * 对应 'cover'
     */
    private String cover;

    /**
     * 对应 'is_top'
     */
    private Integer isTop;

    private LocalDateTime createdAt;
}