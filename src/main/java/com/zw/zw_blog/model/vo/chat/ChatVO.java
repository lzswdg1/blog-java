package com.zw.zw_blog.model.vo.chat;


import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于展示聊天记录的 VO (HTTP API 返回)
 * 对应 controller/chat/index.js [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/chat/index.js] 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class ChatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Chat Model 的 id [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/chat/chat.js]
     */
    private Long id;

    /**
     * 消息发送者的用户信息
     * 对应 Controller [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/chat/index.js] 中关联查询的用户信息
     */
    private UserSimpleVO fromUser;

    /**
     * 消息接收者的用户信息 (私聊)
     * 对应 Controller [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/chat/index.js] 中关联查询的用户信息
     */
    private UserSimpleVO toUser; // 如果是群聊，此字段可能为 null

    /**
     * 对应 Chat Model 的 content [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/chat/chat.js]
     */
    private String content;

    /**
     * 对应 Chat Model 的 chat_type [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/chat/chat.js] (1 私聊 2 群聊)
     */
    private Integer chatType;

    /**
     * 对应 Controller [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/chat/index.js] 中处理的 IP 属地 (ip_address)
     * 注意：不是原始 IP [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/chat/chat.js]
     */
    private String ipAddress;

    /**
     * 对应 Chat Model 的 createdAt [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/chat/chat.js]
     */
    private LocalDateTime createdAt;
}