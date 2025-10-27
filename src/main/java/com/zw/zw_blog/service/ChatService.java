package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.chat.Chat;
import com.zw.zw_blog.model.dto.chat.ChatMessageDTO;
import com.zw.zw_blog.model.vo.chat.ChatVO;
import com.zw.zw_blog.model.vo.user.UserSimpleVO;

import java.util.List;

// 对应 service/chat/index.js
public interface ChatService extends IService<Chat> {

    /**
     * 保存聊天消息 (WebSocket 接收后调用)
     * (对应 addChat)
     */
    Chat saveChatMessage(ChatMessageDTO messageDTO, Long fromUserId, String ip);

    /**
     * 获取与指定用户的聊天记录
     * (对应 getChatUserList)
     */
    List<ChatVO> getChatUserList(Long userId, Long targetUserId);

    /**
     * 获取所有聊天会话列表 (包含最后一条消息和未读数)
     * (对应 getAllChatList)
     */
    List<UserSimpleVO> getAllChatList(Long userId); // 返回类型可能需要调整

    /**
     * 标记与某个用户的聊天为已读
     * (对应 readMessage)
     */
    boolean readMessage(Long userId, Long fromUserId);
}