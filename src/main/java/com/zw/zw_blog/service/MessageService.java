package com.zw.zw_blog.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.message.Message;
import com.zw.zw_blog.model.dto.message.MessageCreateDTO;
import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import com.zw.zw_blog.model.vo.message.MessageVO;

import java.util.List;

// 对应 service/message/index.js
public interface MessageService extends IService<Message> {

    /**
     * 新增留言
     * 对应 node service: addMessage
     *
     * @param messageCreateDTO 留言 DTO
     * @return boolean
     */
    boolean addMessage(MessageCreateDTO messageCreateDTO);

    /**
     * 分页获取留言列表 (后台管理/前台展示)
     * 对应 node service: getMessageList
     *
     * @param pageQueryDTO 分页参数
     * @return IPage<MessageVO>
     */
    IPage<MessageVO> getMessageList(PageQueryDTO pageQueryDTO);

    /**
     * 获取所有留言 (用于弹幕等不分页场景，可选)
     * 对应 node service: getAllMessage
     * @return List<MessageVO>
     */
    List<MessageVO> getAllMessage();

    /**
     * 删除留言
     * 对应 node service: deleteMessage
     *
     * @param id 留言id
     * @return boolean
     */
    boolean deleteMessage(Long id);

    /**
     * 批量删除留言
     * @param ids 留言id列表
     * @return boolean
     */
    boolean deleteMessageBatch(List<Long> ids);
}