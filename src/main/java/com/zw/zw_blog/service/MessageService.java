package com.zw.zw_blog.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.message.Message;
import com.zw.zw_blog.model.dto.message.MessageCreateDTO;
import com.zw.zw_blog.model.vo.message.MessageVO;

// 对应 service/message/index.js
public interface MessageService extends IService<Message> {

    /**
     * 添加留言
     * (对应 addMessage)
     */
    Message addMessage(MessageCreateDTO createDTO, Long userId, String ip);

    /**
     * 删除留言
     * (对应 deleteMessage)
     */
    boolean removeMessage(Long id, Long userId, Integer userRole);

    /**
     * 分页获取留言列表 (带用户信息)
     * (对应 getMessageList)
     */
    IPage<MessageVO> getMessageList(Page<Message> page);
}