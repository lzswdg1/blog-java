package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.comment.Comment;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.comment.CommentCreateDTO;
import com.zw.zw_blog.model.dto.comment.CommentQueryDTO;
import com.zw.zw_blog.model.vo.comment.CommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {
    Comment getCommentById(Long id); // OK

    /**
     * 创建评论 (修正了方法签名)
     * @param createDTO DTO
     * @param loginUser 当前登录的用户 (用于获取 fromId, fromName, fromAvatar)
     * @param ip 评论者的 IP 地址
     * @return Comment
     */
    Comment createComment(CommentCreateDTO createDTO, User loginUser, String ip);

    /**
     * 删除评论
     * @param id 评论ID
     * @param loginUser 当前登录的用户 (用于权限检查)
     * @return boolean
     */
    boolean removeComment(Long id, User loginUser); // 修正了参数名

    IPage<CommentVO> getCommentList(CommentQueryDTO queryDTO); // OK

    /**
     * 删除某个用户的所有评论
     * @param authorId 作者ID (fromId)
     * @return boolean
     */
    boolean removeCommentList(Long authorId); // OK

}
