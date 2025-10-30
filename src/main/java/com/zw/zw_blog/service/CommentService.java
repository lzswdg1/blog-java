package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.comment.Comment;
import com.zw.zw_blog.model.dto.comment.CommentCreateDTO;
import com.zw.zw_blog.model.dto.comment.CommentQueryDTO;
import com.zw.zw_blog.model.vo.comment.CommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {

    Comment getCommentById(Long id);
    Comment createComment(CommentCreateDTO commentCreateDTO);
    boolean removeComment(Long id, Long authorId);
    IPage<CommentVO> getCommentList(CommentQueryDTO queryDTO);

    boolean removeCommentList(Long authorId);

}
