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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
            log.info("点赞/取消点赞失败，参数不完整 -userId: {}, typeId: {}, type: {} ",userId,typeId,type);
            throw new  BusinessException(ResultCode.PARAM_VALIDATE_FAILED);
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
            log.warn("getLikeList 查询失败：typeId 或 type 为 null");
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

    @Override
    public boolean isLiked(Long userId, Long typeId,Integer type){
        //参数校验
        if(userId ==null|| typeId ==null||type==null){
            log.warn("isLiked 检查失败：参数不完整。userId={}, typeId={}, type={}", userId, typeId, type);
            return false; // 参数不全，不可能存在点赞记录
        }
        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        likeWrapper.eq("user_id",userId.intValue());
        likeWrapper.eq("type_id",typeId.intValue());
        likeWrapper.eq("type",type);

        return likeMapper.exists(likeWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLikes(Long typeId,Integer type){
        if(typeId ==null|| type==null){
            log.warn("removeLikes 失败：typeId 或 type 为 null");
            // 根据业务逻辑，参数不全可以认为 "删除成功" (没有东西可删)，或者抛出异常
            return false;
        }
        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        likeWrapper.eq("type_id",typeId.intValue());
        likeWrapper.eq("type",type);

        try{
            int deleteCount = likeMapper.delete(likeWrapper);
            log.info("删除关联点赞记录 - Type ID: {}, Type: {}, 成功删除 {} 条记录", typeId, type, deleteCount);

            // 无论 deletedRows 是 0 (本就没点赞) 还是 > 0 (删除成功)，操作都算成功
            return true;
        } catch(Exception e){
            log.error("删除关联点赞记录时出错 - Type ID: {}, Type: {}", typeId, type, e);
            // 抛出异常以触发事务回滚
            throw new BusinessException(ResultCode.INTERNAL_SERVER_ERROR.getCode(),e.getMessage());
        }
    }

    @Override
    public Map<Long,Integer> getLikeCounts(List<Long> typeIds,Integer type){
        if(CollectionUtils.isEmpty(typeIds)||type==null){
            log.warn("getLikeCounts 查询失败：typeIds 列表为空或 type 为 null");
            return Collections.emptyMap(); // 返回空 Map
        }

        List<Integer> intTypeIds = typeIds.stream()
                .mapToInt(Long::intValue)
                .boxed()
                .collect(Collectors.toList());

        QueryWrapper<Like> likeWrapper = new QueryWrapper<>();
        likeWrapper.select("type_id","count(*) as count");
        likeWrapper.eq("type",type);
        likeWrapper.in("type_id",intTypeIds);
        likeWrapper.groupBy("type_id");

        List<Map<String,Object>> results = likeMapper.selectMaps(likeWrapper);

        if(CollectionUtils.isEmpty(results)){
            return Collections.emptyMap();
        }
        try{
            Map<Long,Integer> countMap = results.stream()
                    .collect(Collectors.toMap(
                            map ->((Number) map.get("type_id")).longValue(),
                            map ->((Number)map.get("count")).intValue()
                    ));
            return countMap;
        } catch (Exception e) {
            log.error("getLikeCounts 转换 Map 失败", e);
            return Collections.emptyMap();
        }
    }
}
