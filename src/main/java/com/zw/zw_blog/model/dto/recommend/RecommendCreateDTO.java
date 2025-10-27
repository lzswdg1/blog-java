package com.zw.zw_blog.model.dto.recommend;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.io.Serializable;

/**
 * 对应: POST /recommend/add
 * 替代: controller/recommend/index.js 中的 addRecommend 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class RecommendCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'title'
     * 替代: if (!title) 校验
     */
    @NotBlank(message = "推荐标题不能为空")
    private String title;

    /**
     * 对应 'content'
     * 替代: if (!content) 校验
     */
    @NotBlank(message = "推荐内容不能为空")
    private String content;

    /**
     * 对应 'cover'
     * 替代: if (!cover) 校验
     */
    @NotBlank(message = "推荐封面不能为空")
    @URL(message = "请输入合法的封面 URL")
    private String cover;

    /**
     * 对应 'is_top' (是否置顶)
     * 数据库是INTEGER，这里用 Integer
     */
    private Integer isTop; // 在 Java 中使用驼峰命名

    // userId 将从后端获取，不由 DTO 传入
}