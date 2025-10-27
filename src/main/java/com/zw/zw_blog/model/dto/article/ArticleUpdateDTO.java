package com.zw.zw_blog.model.dto.article;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleUpdateDTO extends ArticleCreateDTO implements Serializable {
    // ArticleCreateDTO 中已包含 Serializable，这里会自动拥有
    private static final long serialVersionUID = 1L;

    @NotNull(message = "文章ID不能为空")
    private Long id;
}
