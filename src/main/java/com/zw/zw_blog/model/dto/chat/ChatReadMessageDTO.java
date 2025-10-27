package com.zw.zw_blog.model.dto.chat;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: POST /chat/read/message
 * 替代: controller/chat/index.js 中的 readMessage 方法参数
 */
@Data
public class ChatReadMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'from_user_id' (即消息发送方的ID)
     * 替代: if (!from_user_id) 校验
     */
    @NotNull(message = "发送方ID不能为空")
    private Long fromUserId;
}