package com.zw.zw_blog.model.dto.comment;

import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对应: POST /comment/list/page
 * 也可用于: GET /comment/list
 */
@Data
@EqualsAndHashCode(callSuper = true) // 包含父类的字段
public class CommentQueryDTO extends PageQueryDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 getCommentListByPage 和 getCommentList 中的 'article_id'
     */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;
}