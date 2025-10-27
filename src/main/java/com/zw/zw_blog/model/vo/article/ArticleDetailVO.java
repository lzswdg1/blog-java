package com.zw.zw_blog.model.vo.article;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用于文章详情展示的 VO (信息更全)
 */
@Data
@EqualsAndHashCode(callSuper = true) // 包含父类字段
public class ArticleDetailVO extends ArticleSimpleVO {

    private static final long serialVersionUID = 1L;

    private String content; // 文章内容
    private Integer isOriginal; // 是否原创
    private Integer isOpenComment; // 是否开启评论

    // 可以添加上一篇、下一篇文章的信息
    private ArticleSimpleVO prevArticle;
    private ArticleSimpleVO nextArticle;
}