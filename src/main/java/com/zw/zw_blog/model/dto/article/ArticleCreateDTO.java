package com.zw.zw_blog.model.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ArticleCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // articleValidate 检查 title
    @NotBlank(message = "文章标题不能为空")
    private String title;

    // articleValidate 检查 content
    @NotBlank(message = "文章内容不能为空")
    private String content;

    // 对应 controller/article/index.js addArticle
    private Integer status;
    private Integer isStick;
    private Integer isOriginal;
    private Integer isOpenComment;
    private Long categoryId;
    private String cover;
    private String password;

    // 在 Node.js 项目中，你传入的是 tags (一个 ID 列表)
    private List<Long> tagIds;
}
