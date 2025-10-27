package com.zw.zw_blog.model.vo.comment;


import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用于展示评论信息的 VO
 * 对应 controller/comment/index.js 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class CommentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Comment Model 的 id
     */
    private Long id;

    /**
     * 对应 Comment Model 的 content
     */
    private String content;

    /**
     * 评论发布者的用户信息 (嵌套 VO)
     * 对应 Controller 中关联查询的用户信息
     */
    private UserSimpleVO user; // 使用一个简化的 User VO

    /**
     * 被回复的评论 ID (对应 Comment Model 的 parent_id)
     */
    private Long parentId;

    /**
     * 被回复的用户信息 (嵌套 VO)
     * 对应 Controller 中关联查询的 reply_user 信息
     */
    private UserSimpleVO replyUser; // 回复谁

    /**
     * 对应 Comment Model 的 createdAt
     * (LocalDateTime 会被 Jackson 自动格式化)
     */
    private LocalDateTime createdAt;

    /**
     * 对应 Controller 中处理的 IP 属地 (ip_address)
     * 注意：不是原始 IP
     */
    private String ipAddress;

    /**
     * 子评论列表 (用于前端递归展示)
     * 这个通常在 Service 层递归查询并组装
     */
    private List<CommentVO> children;
}