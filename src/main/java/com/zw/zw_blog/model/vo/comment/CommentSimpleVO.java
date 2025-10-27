package com.zw.zw_blog.model.vo.comment;


import lombok.Data;
import java.io.Serializable;

/**
 * 用于在 NotifyVO 中嵌套展示评论简要信息的 VO
 */
@Data
public class CommentSimpleVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String content; // 可能只需要部分内容预览
}