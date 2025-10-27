package com.zw.zw_blog.model.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class TagCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 对应 tagValidate 中间件 对 'name' 的校验
     */
    @NotBlank(message = "标签名称不能为空")
    private String name;
}
