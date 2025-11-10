package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.mapper.ArticleMapper;
import com.zw.zw_blog.mapper.CommentMapper;
import com.zw.zw_blog.mapper.NotifyMapper;
import com.zw.zw_blog.mapper.UserMapper;
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.bean.comment.Comment;
import com.zw.zw_blog.model.bean.notify.Notify;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.vo.article.ArticleSimpleVO;
import com.zw.zw_blog.model.vo.comment.CommentSimpleVO;
import com.zw.zw_blog.model.vo.notify.NotifyVO;
import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import com.zw.zw_blog.service.NotifyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotifyServiceImpl extends ServiceImpl<NotifyMapper, Notify> implements NotifyService {
    @Resource
    private NotifyMapper notifyMapper;


    @Resource
    private UserMapper userMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ArticleMapper articleMapper;

    private static final int IS_READ = 1;
    private static final int IS_UNREAD = 0;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Notify createNotify(Integer triggerUserId, Integer receiveUserId, Integer commentId, Integer articleId, Integer type) {
        Notify notify = new Notify();
        notify.setTriggerUserId(triggerUserId.longValue());
        notify.setReceiveUserId(receiveUserId.longValue());
        notify.setCommentId(commentId.longValue());
        notify.setArticleId(articleId.longValue());
        notify.setType(type);

        notify.setUserId(receiveUserId.longValue());
        notify.setIsRead(IS_UNREAD);
        notify.setCreatedAt(LocalDateTime.now());

        this.save(notify);
        return notify;
    }
    @Override
    public IPage<NotifyVO>  getNotifyList(Page<Notify> page, Long userId){
        LambdaQueryWrapper<Notify> wrapper = new LambdaQueryWrapper<Notify>()
                .eq(Notify::getUserId, userId)
                .orderByDesc(Notify::getCreatedAt);
        IPage<Notify> notifyPage = this.page(page, wrapper);

        return convertToNotifyVOPage(notifyPage);
    }

    @Override
    public long getUnReadNotifyCount(Long userId){
        return this.count(new LambdaQueryWrapper<Notify>().eq(Notify::getUserId, userId)
                .eq(Notify::getIsRead,IS_UNREAD));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean readNotify(List<Long> ids, Long userId){
        if(CollectionUtils.isEmpty(ids)){
            return true;
        }
        List<Integer> intIds = ids.stream()
                .map(Long::intValue).collect(Collectors.toList());
        return this.update(new LambdaUpdateWrapper<Notify>()
                .eq(Notify::getUserId, userId)
                .in(Notify::getId, intIds)
                .set(Notify::getIsRead,IS_READ));
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean readAllNotify(Long userId) {
        return this.update(new LambdaUpdateWrapper<Notify>()
                .eq(Notify::getUserId, userId.intValue())
                .eq(Notify::getIsRead, IS_UNREAD) // 只更新未读的
                .set(Notify::getIsRead, IS_READ)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeNotify(List<Long> ids, Long userId) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }

        // 注意：类型转换
        List<Integer> intIds = ids.stream().map(Long::intValue).collect(Collectors.toList());

        return this.remove(new LambdaQueryWrapper<Notify>()
                .eq(Notify::getUserId, userId.intValue()) // 确保只删除自己的
                .in(Notify::getId, intIds)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAllNotify(Long userId) {
        return this.remove(new LambdaQueryWrapper<Notify>()
                .eq(Notify::getUserId, userId.intValue())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeNotifyByCommentId(Long commentId) {
        // 注意：类型转换
        return this.remove(new LambdaQueryWrapper<Notify>()
                .eq(Notify::getCommentId, commentId.intValue())
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeNotifyByArticleId(Long articleId) {
        // 注意：类型转换
        return this.remove(new LambdaQueryWrapper<Notify>()
                .eq(Notify::getArticleId, articleId.intValue())
        );
    }
    /**
     * [已修正]
     * 私有辅助方法：将 Notify 分页结果转换为 NotifyVO 分页结果，并批量填充关联数据
     */
    private IPage<NotifyVO> convertToNotifyVOPage(IPage<Notify> notifyPage) {
        List<Notify> records = notifyPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            // 返回一个包含正确分页信息的空 VO 分页
            return new Page<NotifyVO>(notifyPage.getCurrent(), notifyPage.getSize(), notifyPage.getTotal())
                    .setRecords(Collections.emptyList());
        }

        // 1. 收集所有关联 ID (现在所有类型都应该是 Long)
        Set<Long> triggerUserIds = records.stream()
                .map(Notify::getTriggerUserId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Set<Long> articleIds = records.stream()
                .map(Notify::getArticleId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());
        Set<Long> commentIds = records.stream()
                .map(Notify::getCommentId)
                .filter(id -> id != null && id > 0)
                .collect(Collectors.toSet());

        // 2. 批量查询 (假设 User, Article, Comment 的 ID 也都是 Long)
        Map<Long, User> userMap = CollectionUtils.isEmpty(triggerUserIds) ? Collections.emptyMap() :
                userMapper.selectBatchIds(triggerUserIds).stream()
                        .collect(Collectors.toMap(User::getId, user -> user));

        Map<Long, Article> articleMap = CollectionUtils.isEmpty(articleIds) ? Collections.emptyMap() :
                articleMapper.selectBatchIds(articleIds).stream()
                        .collect(Collectors.toMap(Article::getId, article -> article));

        Map<Long, Comment> commentMap = CollectionUtils.isEmpty(commentIds) ? Collections.emptyMap() :
                commentMapper.selectBatchIds(commentIds).stream()
                        .collect(Collectors.toMap(Comment::getId, comment -> comment));

        // 3. 转换并填充
        return notifyPage.convert(notify -> {
            NotifyVO vo = new NotifyVO();
            // 假设 Notify.java 中的 "message" 和 "isView" 字段
            // 已经按照我之前的建议改名为 "content" 和 "isRead"
            BeanUtils.copyProperties(notify, vo);

            // 填充触发者信息
            if (notify.getTriggerUserId() != null) {
                User triggerUser = userMap.get(notify.getTriggerUserId());
                if (triggerUser != null) {
                    UserSimpleVO userVO = new UserSimpleVO();
                    BeanUtils.copyProperties(triggerUser, userVO);
                    vo.setTriggerUser(userVO); //
                }
            }

            // [修正] 填充嵌套的 ArticleSimpleVO
            if (notify.getArticleId() != null) {
                Article article = articleMap.get(notify.getArticleId());
                if (article != null) {
                    ArticleSimpleVO articleVO = new ArticleSimpleVO();
                    BeanUtils.copyProperties(article, articleVO);
                    vo.setArticle(articleVO); // <-- 正确
                }
            }

            // [修正] 填充嵌套的 CommentSimpleVO
            if (notify.getCommentId() != null) {
                Comment comment = commentMap.get(notify.getCommentId());
                if (comment != null) {
                    CommentSimpleVO commentVO = new CommentSimpleVO();
                    BeanUtils.copyProperties(comment, commentVO);
                    vo.setComment(commentVO); // <-- 正确
                }
            }

            return vo;
        });
    }
}
