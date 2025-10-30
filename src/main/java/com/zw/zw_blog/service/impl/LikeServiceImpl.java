package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.LikeMapper;
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.bean.comment.Comment;
import com.zw.zw_blog.model.bean.like.Like;
import com.zw.zw_blog.model.bean.talk.Talk;
import com.zw.zw_blog.service.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    @Autowired
    private LikeMapper likeMapper;


    @Autowired(required = false)
    @Lazy
    private NotifyService notifyService;

    @Autowired(required = false)
    @Lazy
    private CommentService commentService;

    @Autowired(required = false)
    @Lazy
    private ArticleService articleService;


    @Autowired(required = false)
    @Lazy
    private TalkService talkService;

    @Override
    public  boolean toggleLike(Long userId, Long typeId, Integer type){
        if(userId == null || typeId == null || type == null){
            throw new BusinessException(ResultCode.PARAM_VALIDATE_FAILED);
            log.info("点赞/取消点赞失败，参数不完整 -userId: {} typeId: {} type: {} ",userId,typeId,type);
            return false;
        }
        QueryWrapper<Like> liekWrapper = new QueryWrapper<>();
        liekWrapper.eq("type_id",typeId);
        liekWrapper.eq("type",type);
        liekWrapper.eq("user_id",userId);
        Like like = likeMapper.selectOne(liekWrapper);
        if(like!=null){
            int deleteNum = likeMapper.delete(liekWrapper);
            if(deleteNum>0){
                log.info("取消点赞成功");
                return true;
            }else{
                log.warn("取消点赞失败 -User Id:{},type Id: {},type: {} ",userId,typeId,type);
                return false;
            }
        }else{
            Like  newLike = new Like();
            newLike.setUserId(userId.intValue());
            newLike.setType(type);
            newLike.setTypeId(typeId.intValue());
            int insertNum = likeMapper.insert(newLike);
            if(insertNum>0){
                log.info("点赞成功");
                //发送通知
                if(notifyService!=null){
                    Integer targetUserId = null;
                    Integer relatedArticleId = null;
                    Integer relatedcommentId = null;

                    try{
                        switch (type){
                            case 1:
                                if(articleService!=null){
                                    Article article = articleService.getById(typeId);
                                    if(article!=null){
                                        targetUserId = article.getAuthorId();
                                        relatedArticleId = article.getId().intValue();
                                    }else {
                                        log.warn("点赞通知，找不到点赞的文章 -Article Id: {}",typeId);
                                    }
                                }else {
                                    log.warn("articleService 未注入");
                                }
                                break;
                            case 2:
                                if(commentService!=null){
                                    Comment comment = commentService.getById(typeId);
                                    if(comment!=null){
                                        targetUserId =comment.getUserId();
                                        relatedArticleId = comment.getId().intValue();
                                        relatedcommentId = comment.getId().intValue();
                                    }else{
                                        log.warn("点赞通知：找不到被点赞的评论 - Comment ID: {}", typeId);
                                    }
                                }else{
                                    log.warn("CommentService 未注入，无法获取评论作者信息");
                                }
                                break;
                            case 3:
                                if(talkService!=null){
                                    Talk talk = talkService.getById(typeId);
                                    if(talk!=null){
                                        targetUserId =talk.getUserId();
                                    }else{
                                        log.warn("点赞通知：找不到被点赞的说说 - Talk ID: {}", typeId);
                                    }
                                }else{
                                    log.warn("TalkService 未注入，无法获取说说作者信息");
                                }
                                break;
                            default:
                                log.warn("未知的点赞类型 - Type: {}", type);
                                break;
                        }
                        if(targetUserId!=null &&!targetUserId.equals(userId.intValue())){
                            notifyService.createNotify(userId.intValue(),targetUserId,relatedcommentId,relatedArticleId,3);
                            log.info("点赞通知已发送 - Trigger User: {}, Receive User: {}, Type: {}, Type ID: {}", userId, targetUserId, type, typeId);
                        }else if(targetUserId!=null&&targetUserId.equals(userId.intValue())){
                            log.info("用户给自己点赞，不发送通知 - User ID: {}, Type: {}, Type ID: {}", userId, type, typeId);
                        }
                    } catch (Exception e) {
                        // 记录通知发送失败的日志，但不应中断点赞流程
                        log.error("发送点赞通知时出错 - User ID: {}, Type ID: {}, Type: {}", userId, typeId, type, e);
                    }
                }
                return true;
            }else{
                log.error("点赞失败 -User Id:{},type Id: {},type: {} ",userId,typeId,type);
                return false;
            }
        }
    }

    @Override
    public List<Integer> getLikeList(Long typeId,Integer type){
        //参数校验
        if(typeId == null || type == null){
            log.warn(log.warn("getLikeList 查询失败：typeId 或 type 为 null");
            return Collections.emptyList(); // 返回一个不可变的空列表);
        }

        //构建查询条件
        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        likeWrapper.select("user_id");
        likeWrapper.eq("type_id",typeId.intValue());
        likeWrapper.eq("type",type);

        List<Like> likeRecords = likeMapper.selectList(likeWrapper);
        if(CollectionUtils.isEmpty(likeRecords)){
            return Collections.emptyList();
        }

        //提取userId列表
        List<Integer> userIdList=likeRecords.stream()
                .map(Like::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        return  userIdList;
    }
}
