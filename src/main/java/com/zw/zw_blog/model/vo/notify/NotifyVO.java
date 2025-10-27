package com.zw.zw_blog.model.vo.notify;


import com.zw.zw_blog.model.vo.article.ArticleSimpleVO;
import com.zw.zw_blog.model.vo.comment.CommentSimpleVO;
import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于展示通知信息的 VO
 * 对应 controller/notify/index.js [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/notify/index.js] getNotifyList 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class NotifyVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Notify Model 的 id [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/notify/notify.js]
     */
    private Long id;

    /**
     * 通知内容 (可能需要在 Service 层根据 type 动态生成)
     * 对应 Notify Model 的 content [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/notify/notify.js]
     */
    private String content;

    /**
     * 对应 Notify Model 的 type [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/notify/notify.js] (例如: 1-评论, 2-回复, 3-点赞)
     */
    private Integer type;

    /**
     * 对应 Notify Model 的 is_read [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/notify/notify.js] (0 未读 1 已读)
     */
    private Integer isRead;

    /**
     * 触发通知的用户信息 (例如: 谁评论了你)
     * 对应 Controller [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/notify/index.js] 中关联查询的用户信息
     */
    private UserSimpleVO triggerUser;

    /**
     * 关联的文章信息 (可选, 如果 type 是文章相关)
     * 对应 Notify Model 的 article_id [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/notify/notify.js]
     */
    private ArticleSimpleVO article; // 使用之前创建的文章简要 VO

    /**
     * 关联的评论信息 (可选, 如果 type 是评论相关)
     * 对应 Notify Model 的 comment_id [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/notify/notify.js]
     */
    private CommentSimpleVO comment; // 需要创建一个简化的 Comment VO

    /**
     * 对应 Notify Model 的 createdAt [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/notify/notify.js]
     */
    private LocalDateTime createdAt;
}