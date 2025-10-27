package com.zw.zw_blog.model.dto.chat;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: POST /chat/list/user
 * 替代: controller/chat/index.js 中的 getChatUserList 方法参数
 */
@Data
public class ChatUserListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'user_id' (即聊天对象的用户ID)
     * 替代: if (!user_id) 校验
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId; // Java 中使用驼峰命名
}