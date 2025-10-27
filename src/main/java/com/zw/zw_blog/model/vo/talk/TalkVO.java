package com.zw.zw_blog.model.vo.talk;


import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用于展示说说信息的 VO
 * 对应 controller/talk/index.js 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class TalkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Talk Model 的 id
     */
    private Long id;

    /**
     * 对应 Talk Model 的 content
     */
    private String content;

    /**
     * 说说发布者的用户信息 (嵌套 VO)
     * 对应 Controller 中关联查询的用户信息
     */
    private UserSimpleVO user; // 使用之前创建的简化 User VO

    /**
     * 说说包含的图片 URL 列表
     * 对应 Controller 中关联查询的 talk_photos
     */
    private List<String> images;

    /**
     * 对应 Talk Model 的 status (1 公开 2 私密)
     * 前端可能需要这个字段来决定是否显示编辑/删除按钮
     */
    private Integer status;

    /**
     * 对应 Controller 中处理的 IP 属地 (ip_address)
     * 注意：不是原始 IP
     */
    private String ipAddress;

    /**
     * 对应 Talk Model 的 createdAt
     */
    private LocalDateTime createdAt;

    /**
     * 点赞数量 (需要在 Service 层单独统计)
     */
    private Integer likeCount;

    /**
     * 评论数量 (需要在 Service 层单独统计)
     */
    private Integer commentCount;

    /**
     * 当前登录用户是否已点赞 (需要在 Service 层判断)
     */
    private Boolean isLiked;
}