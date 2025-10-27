package com.zw.zw_blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zw.zw_blog.model.bean.comment.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
