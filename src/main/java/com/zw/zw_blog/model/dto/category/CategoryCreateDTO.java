package com.zw.zw_blog.model.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // categoryValidate 检查 name
    @NotBlank(message = "分类名称不能为空")
    private String name;
}
