package com.zw.zw_blog.model.bean.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@TableName("blog_article")
public class Article implements Serializable {
    @TableId(value="id",type = IdType.AUTO)
    private Long id;
    
    @TableField("article_title")
    private String articleTitle;
    
    @TableField("author_id")
    private Integer authorId;
    
    @TableField("category_id")
    private Integer categoryId;
    
    @TableField("article_content")
    private String articleContent;
    
    @TableField("article_cover")
    private String articleCover="";
    
    @TableField("is_top")
    private Integer isTop=2;
    
    private Integer orderNum;
    
    private Integer status;
    
    private Integer type=1;
    
    @TableField("origin_url")
    private String originUrl;
    
    @TableField("view_times")
    private Integer viewTimes=0;
    
    @TableField("reading_duration")
    private Double readingDuration=0.0;
    
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
