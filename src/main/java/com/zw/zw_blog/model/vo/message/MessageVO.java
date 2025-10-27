package com.zw.zw_blog.model.vo.message;


import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于展示留言信息的 VO
 * 对应 controller/message/index.js getMessageList 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class MessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Message Model 的 id
     */
    private Long id;

    /**
     * 对应 Message Model 的 content
     */
    private String content;

    /**
     * 留言发布者的用户信息 (嵌套 VO)
     * 对应 Controller 中关联查询的用户信息
     */
    private UserSimpleVO user; // 使用之前创建的简化 User VO

    /**
     * 对应 Controller 中处理的 IP 属地 (ip_address)
     * 注意：不是原始 IP
     */
    private String ipAddress;

    /**
     * 对应 Message Model 的 createdAt
     */
    private LocalDateTime createdAt;
}