package com.zw.zw_blog.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.notify.Notify;
import com.zw.zw_blog.model.vo.notify.NotifyVO;

import java.util.List;

// 对应 service/notify/index.js
public interface NotifyService extends IService<Notify> {

    /**
     * 创建通知
     * (对应 createNotify)
     */
    Notify createNotify(Integer triggerUserId, Integer receiveUserId, Integer commentId, Integer articleId, Integer type);

    /**
     * 分页获取当前用户的通知列表
     * (对应 getNotifyList)
     */
    IPage<NotifyVO> getNotifyList(Page<Notify> page, Long userId);

    /**
     * 获取当前用户未读通知数量
     * (对应 getUnReadNotifyCount)
     */
    long getUnReadNotifyCount(Long userId);

    /**
     * 批量标记通知为已读
     * (对应 readNotify)
     */
    boolean readNotify(List<Long> ids, Long userId);

    /**
     * 标记所有通知为已读
     * (对应 readAllNotify)
     */
    boolean readAllNotify(Long userId);

    /**
     * 批量删除通知
     * (对应 deleteNotify)
     */
    boolean removeNotify(List<Long> ids, Long userId);

    /**
     * 删除所有通知
     * (对应 deleteAllNotify)
     */
    boolean removeAllNotify(Long userId);

    /**
     * 根据评论ID删除通知 (供 CommentService 调用)
     */
    boolean removeNotifyByCommentId(Long commentId);

    /**
     * 根据文章ID删除通知 (供 ArticleService 调用)
     */
    boolean removeNotifyByArticleId(Long articleId);
}