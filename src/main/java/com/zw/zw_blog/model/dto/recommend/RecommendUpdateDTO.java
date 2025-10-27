package com.zw.zw_blog.model.dto.recommend;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.io.Serializable;

/**
 * 对应: PUT /recommend/update
 * 替代: controller/recommend/index.js 中的 updateRecommend 方法参数
 */
@Data
public class RecommendUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'id'
     * 替代: if (!id) 校验
     */
    @NotNull(message = "推荐ID不能为空")
    private Long id;

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
     * 对应 'is_top'
     */
    private Integer isTop;
}