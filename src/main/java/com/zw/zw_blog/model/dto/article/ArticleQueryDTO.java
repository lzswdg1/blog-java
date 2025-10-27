package com.zw.zw_blog.model.dto.article;

import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryDTO extends PageQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 对应 controller/article/index.js getArticleList 的查询参数
    private String title;
    private Integer status;
    private Long categoryId;
    private Long tagId;
}
