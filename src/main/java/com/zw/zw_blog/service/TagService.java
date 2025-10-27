package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.tag.Tag;
import com.zw.zw_blog.model.dto.tag.TagQueryDTO;
import com.zw.zw_blog.model.vo.tag.TagVO;

import java.util.List;

public interface TagService extends IService<Tag> {

    Tag createTag(String name);

    boolean updateTag(Long id, String name);

    boolean removeTag(Long id);

    IPage<Tag> getTagList(TagQueryDTO queryDTO);

    List<TagVO> getAllTagList();
}