package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.ArticleMapper;
import com.zw.zw_blog.mapper.ArticleTagsMapper;
import com.zw.zw_blog.mapper.TagMapper;
import com.zw.zw_blog.model.bean.article.ArticleTags;
import com.zw.zw_blog.model.bean.tag.Tag;
import com.zw.zw_blog.model.dto.tag.TagQueryDTO;
import com.zw.zw_blog.model.vo.tag.TagVO;
import com.zw.zw_blog.service.TagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagsMapper articletagsMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag createTag(String name){
        if(existTag(name)){
            throw new BusinessException(ResultCode.TAG_EXIST);
        }

        Tag tag = new Tag();
        tag.setTagName(name);

        tag.setCreatedAt(LocalDateTime.now());
        tag.setUpdatedAt(LocalDateTime.now());

        this.save(tag);
        return tag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTag(Long id,String name){
        Tag tag = this.getById(id);
        if(tag == null){
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        if(!tag.getTagName().equals(name)&&existTag(name)){
            throw new BusinessException(ResultCode.TAG_EXIST);
        }

        tag.setTagName(name);
        tag.setUpdatedAt(LocalDateTime.now());
        return this.updateById(tag);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTag(Long id){
        Tag tag = this.getById(id);
        if(tag == null){
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        articletagsMapper.delete(new LambdaQueryWrapper<ArticleTags>().eq(ArticleTags::getTagId,id));

        return this.removeById(id);
    }

    @Override
    public IPage<Tag> getTagList(TagQueryDTO queryDTO){
        Page<Tag> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());


        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryDTO.getName()), Tag::getTagName, queryDTO.getName());
        wrapper.orderByDesc(Tag::getCreatedAt);

        return this.page(page, wrapper);
    }

    @Override
    public List<TagVO> getAllTagList(){
        List<Tag> list = this.list(new LambdaQueryWrapper<Tag>().select(Tag::getId, Tag::getTagName));
        return list.stream()
                .map(tag ->{
                    TagVO tagVO = new TagVO();
                    BeanUtils.copyProperties(tag,tagVO);
                    return tagVO;
                }).collect(Collectors.toList());
    }

    private boolean existTag(String name){
        return this.count(new LambdaQueryWrapper<Tag>().eq(Tag::getTagName,name)) >0;
    }
}
