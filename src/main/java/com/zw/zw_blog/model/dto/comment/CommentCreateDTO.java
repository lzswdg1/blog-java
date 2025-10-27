package com.zw.zw_blog.model.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: POST /comment/add
 * 替代: controller/comment/index.js 中的 addComment 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class CommentCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 addComment 中的 'content'
     * 替代 if (!content) 校验
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /**
     * 对应 addComment 中的 'article_id'
     */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    /**
     * 对应 addComment 中的 'parent_id' (父评论ID)
     * 这个字段是可选的，所以不需要校验
     */
    private Long parentId;

    /**
     * 对应 addComment 中的 'reply_user_id' (回复的用户ID)
     * 这个字段也是可选的
     */
    private Long replyUserId;
}