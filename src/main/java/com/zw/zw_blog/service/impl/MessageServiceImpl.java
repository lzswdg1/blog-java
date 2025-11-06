package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.mapper.MessageMapper;
import com.zw.zw_blog.model.bean.message.Message;
import com.zw.zw_blog.model.dto.message.MessageCreateDTO;
import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import com.zw.zw_blog.model.vo.message.MessageVO;
import com.zw.zw_blog.service.MessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {


    @Resource
    private MessageMapper messageMapper;
    @Override
    public boolean addMessage(MessageCreateDTO messageCreateDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageCreateDTO,message);
        message.setCreatedAt(LocalDateTime.now());
        message.setUpdatedAt(LocalDateTime.now());

        int rows = messageMapper.insert(message);
        return rows > 0;
    }

    @Override
    public IPage<MessageVO> getMessageList(PageQueryDTO pageQueryDTO) {
       Page<Message> page = new Page<>(pageQueryDTO.getCurrent(), pageQueryDTO.getSize());

       QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
       queryWrapper.orderByDesc("createdAt");
       IPage<Message> messageIPage = messageMapper.selectPage(page, queryWrapper);

       return messageIPage.convert(this::convertToVO);
    }

    @Override
    public List<MessageVO> getAllMessage() {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("createdAt");
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(messageList)){
            return Collections.emptyList();
        }

        return messageList.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteMessage(Long id) {
       return messageMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteMessageBatch(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return false;
        }
        return messageMapper.deleteBatchIds(ids) > 0;
    }

    private MessageVO convertToVO(Message message){
        MessageVO messageVO = new MessageVO();
        BeanUtils.copyProperties(message,messageVO);
        return messageVO;
    }
}
