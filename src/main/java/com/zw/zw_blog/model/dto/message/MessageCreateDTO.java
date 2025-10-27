package com.zw.zw_blog.model.dto.message;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: POST /message/add
 * 替代: controller/message/index.js 中的 addMessage 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class MessageCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'content'
     * 替代: if (!content) 校验
     */
    @NotBlank(message = "留言内容不能为空")
    private String content;

    // userId 和 ip 将从后端获取，不由 DTO 传入
}