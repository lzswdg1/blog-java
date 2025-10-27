package com.zw.zw_blog.model.dto.like;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: POST /like
 * 对应: GET /like/list
 * 替代: controller/like/index.js 中的 like 和 getLikeList 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class LikeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'type_id' (例如: 文章ID, 评论ID)
     * 替代: if (!type_id) 校验
     */
    @NotNull(message = "点赞ID不能为空")
    private Long typeId;

    /**
     * 对应 'type' (例如: 1-文章, 2-评论, 3-说说)
     * 替代: if (!type) 校验
     */
    @NotNull(message = "点赞类型不能为空")
    private Integer type;
}