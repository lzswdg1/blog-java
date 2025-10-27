package com.zw.zw_blog.model.dto.chat;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: utils/websocket.js 中 ws.on("message") 的消息体
 * 用于 Spring WebSocket 接收消息
 */
@Data
public class ChatMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 websocket.js 中的 'content'
     */
    @NotBlank(message = "消息内容不能为空")
    private String content;

    /**
     * 对应 websocket.js 中的 'to_user_id'
     */
    @NotNull(message = "接收用户ID不能为空")
    private Long toUserId;

    /**
     * 对应 websocket.js 中的 'chat_type' (1: 私聊 2: 群聊)
     */
    @NotNull(message = "聊天类型不能为空")
    private Integer chatType;

    // 'fromUserId' 将从 WebSocket 的 Session/Token 中获取，不由 DTO 传入
}