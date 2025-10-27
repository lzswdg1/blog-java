package com.zw.zw_blog.model.vo.chat;


import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于 WebSocket 推送实时聊天消息的 VO
 * 对应 utils/websocket.js [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/utils/websocket.js] 中发送给客户端的消息结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class ChatMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息发送者的用户信息
     */
    private UserSimpleVO fromUser;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 聊天类型 (1 私聊 2 群聊)
     */
    private Integer chatType;

    /**
     * 发送时间
     */
    private LocalDateTime createdAt;

    // 可能还需要一个 targetId (接收者ID或群聊ID)，取决于你的 WebSocket 实现
    // private Long targetId;
}