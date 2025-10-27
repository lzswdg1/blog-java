package com.zw.zw_blog.model.dto.config;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: PUT /config/update
 * 用于接收前端发来的网站配置
 */
@Data
public class ConfigUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'blog_config' 表 中的 'json' 字段
     * 我们假设前端传来的是一个 JSON 字符串
     */
    @NotBlank(message = "配置内容不能为空")
    private String json;
}