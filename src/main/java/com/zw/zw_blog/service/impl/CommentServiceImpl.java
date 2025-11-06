package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.ArticleMapper;
import com.zw.zw_blog.mapper.CommentMapper;
import com.zw.zw_blog.mapper.LikeMapper;
import com.zw.zw_blog.mapper.UserMapper;
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.bean.comment.Comment;
import com.zw.zw_blog.model.bean.like.Like;
import com.zw.zw_blog.model.bean.talk.Talk;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.comment.CommentCreateDTO;
import com.zw.zw_blog.model.dto.comment.CommentQueryDTO;
import com.zw.zw_blog.model.vo.comment.CommentVO;
import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import com.zw.zw_blog.service.*;
import com.zw.zw_blog.util.ToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeMapper likeMapper;

   @Autowired(required = false)
   @Lazy
   private ArticleService articleService;

   @Autowired(required = false)
    @Lazy
    private TalkService talkService;

   @Autowired(required = false)
    @Lazy
    private NotifyService notifyService;
    @Autowired
    private LikeService likeService;


    @Override
    public Comment getCommentById(Long id){
       return commentMapper.selectById(id);
   }


   @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment createComment(CommentCreateDTO commentCreateDTO, User loginUser, String ip) {
       if(loginUser == null){
           throw new BusinessException(ResultCode.TOKEN_INVALID.getCode(),"用户未登入");
       }
       Comment comment = new Comment();
       BeanUtils.copyProperties(commentCreateDTO,comment);
       comment.setForId(loginUser.getId().intValue());
       comment.setFromName(loginUser.getNickName());
       comment.setFromAvatar(loginUser.getAvatar());

       //填充被回复者的信息
       if(commentCreateDTO.getToId() != null){
           User toUser = userMapper.selectById(commentCreateDTO.getToId());
           if(toUser!=null){
               comment.setToName(toUser.getNickName());
               comment.setToAvatar(toUser.getAvatar());
               comment.setToId(toUser.getId().intValue());
           }else{
               log.warn("创建评论：找不到被回复者(toUser) - User ID: {}", commentCreateDTO.getToId());
               // toId 设为 null，或根据业务抛异常
               comment.setToId(null);
           }
       }

       //填充其他信息
       comment.setIp(ip);
       comment.setThumbsUp(0);
       comment.setCreatedAt(LocalDateTime.now());
       comment.setUpdatedAt(LocalDateTime.now());


       commentMapper.insert(comment);
       log.info("评论创建成功 - ID: {}, FromUser: {}", comment.getId(), loginUser.getId());
       sendCommentNotification(comment, loginUser.getId());

       return comment;
   }

   @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeComment(Long id,User loginUser){
       Comment comment = commentMapper.selectById(id);
       if(loginUser==null){
           log.warn("尝试删除不存在的评论 - ID: {}", id);
           return false; // 评论不存在
       }

       if(loginUser==null||(loginUser.getRole()!=1&&loginUser.getId().equals(comment.getForId().longValue()))){
           log.warn("无权限删除评论 - Comment ID: {}, User ID: {}", id, (loginUser != null ? loginUser.getId() : "null"));
           throw new BusinessException(ResultCode.NO_PERMISSION);
       }
      //递归删除评论自身及其字评论
       int deletedCount = deleteCommentRecursively(id);
       log.info("删除了 {} 条评论记录 (包括子评论) - Root Comment ID: {}", deletedCount, id);

       if(likeService!=null){
           try{
               likeService.removeLikes(id,2);
           } catch (Exception e) {
               log.error("删除评论关联通知时出错 - Comment ID: {}", id, e);
           }
       }
       return deletedCount > 0;
   }

   @Override
    public IPage<CommentVO> getCommentList(CommentQueryDTO queryDTO) {
        if(queryDTO.getType() ==null||queryDTO.getForId()==null){
            throw new BusinessException(ResultCode.PARAM_VALIDATE_FAILED.getCode(), "查询评论必须指定 type 和 forId");
        }
        //分页查询根评论
       Page<Comment> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
       QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("type",queryDTO.getType());
       queryWrapper.eq("for_id",queryDTO.getForId());
       queryWrapper.isNull("parent_id");
       queryWrapper.orderByDesc("created_at");
       IPage<Comment> pageResult = commentMapper.selectPage(page, queryWrapper);
       List<Comment> rootComments = pageResult.getRecords();
       if(CollectionUtils.isEmpty(rootComments)){
           return new Page<CommentVO>(queryDTO.getCurrent(), queryDTO.getSize(),0).setRecords(Collections.emptyList());
        }


       //获取根评论id
       List<Long> rootCommentIds = rootComments.stream()
               .map(Comment::getId)
               .collect(Collectors.toList());
       //一次性查询所有子孙评论
       List<Comment> allSubComments =getAllSubCommentsForRoots(queryDTO.getType(), queryDTO.getForId(), rootCommentIds);
       //合并列表
       List<Comment> allCommentsInTree = new ArrayList<>(rootComments);
       allSubComments.addAll(allCommentsInTree);

       List<CommentVO> allCommentVOs = convertCommentsToVO(allCommentsInTree);

       // 6. 构建评论树结构 (现在传入的是正确的 List<CommentVO>)
       List<CommentVO> commentTree = buildCommentTree(allCommentVOs);
       // 7. (可选) 对根评论下的子评论按时间升序排序
       sortChildrenRecursively(commentTree);

       //创建并返回vo分页结果
       IPage<CommentVO> voPage= new Page<>(pageResult.getCurrent(), pageResult.getSize(), pageResult.getTotal());
       voPage.setRecords(commentTree);
       return voPage;
   }

   @Override
   @Transactional(rollbackFor = Exception.class)
   public boolean removeCommentList(Long userId){
        if(userId==null){
            return  false;
        }

        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("for_id",userId);
        queryWrapper.select("id");
        List<Long> commentIds = commentMapper.selectList(queryWrapper)
                .stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(commentIds)){
            log.info("用户 {} 没有任何评论，无需删除", userId);
            return true;
        }
        commentMapper.deleteBatchIds(commentIds);

        if(likeService!=null){
            QueryWrapper<Like> queryWrapperLike = new QueryWrapper<>();
            queryWrapperLike.eq("type",2);
            queryWrapperLike.in("type_id",commentIds);
            likeService.remove(queryWrapperLike);
        }
        return true;
   }
   //内部方法
    private int deleteCommentRecursively(Long id){
           int count =0;
           QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("parent_id",id);
           List<Comment> children = commentMapper.selectList(queryWrapper);

           if(!CollectionUtils.isEmpty(children)){
               for(Comment comment : children){
                   count += deleteCommentRecursively(comment.getId()); //递归删除
               }
           }
           int deleted = commentMapper.deleteById(id);
           if(deleted>0){
               count++;
           }
           return count;
    }

    //获取子孙评论递归
    private List<Comment> getAllSubCommentsForRoots(Integer type,Integer forId,List<Long> parentIds){
        List<Comment> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(parentIds)){
            return result;
        }
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id",forId);
        queryWrapper.eq("for_id",forId);
        queryWrapper.in("parent_id",parentIds);
        List<Comment> directChildren = commentMapper.selectList(queryWrapper);

        if(!CollectionUtils.isEmpty(directChildren)){
            result.addAll(directChildren);
            List<Long> childrenIds = directChildren.stream().map(Comment::getId).collect(Collectors.toList());
            result.addAll(getAllSubCommentsForRoots(type,forId,childrenIds));
        }
        return result;
    }

    //内部方法
    private List<CommentVO> convertCommentsToVO(List<Comment> comments){
        if(CollectionUtils.isEmpty(comments)){
            return Collections.emptyList();
        }
        return comments.stream().map(comment ->{
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(comment,vo);
            if(comment.getForId()!=null){
                UserSimpleVO userVo = new UserSimpleVO();
                userVo.setId(comment.getForId().longValue());
                userVo.setNickName(comment.getFromName());
                userVo.setAvatar(comment.getFromAvatar());
                vo.setUser(userVo);
            }

            //填充被回复者的信息
            if(comment.getToId()!=null){
                UserSimpleVO replyUserVo = new UserSimpleVO();
                replyUserVo.setId(comment.getToId().longValue());
                replyUserVo.setNickName(comment.getToName());
                replyUserVo.setAvatar(comment.getToAvatar());
                vo.setReplyUser(replyUserVo);
            }
            //填充ip属地
            if(StringUtils.hasText(comment.getIp())){
                try{
                    String ipAddress = ToolUtils.getIpAddress(comment.getIp().split(",")[0].trim());
                    vo.setIpAddress(ipAddress !=null ? ipAddress: "未知");
                } catch (Exception e) {
                    log.error("解析 IP 地址时出错 - IP: {}", comment.getIp(), e);
                    vo.setIpAddress("未知");
                }
            }else {
                vo.setIpAddress("未知");
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private List<CommentVO> buildCommentTree(List<CommentVO> comments){
        if(CollectionUtils.isEmpty(comments)){
            return Collections.emptyList();
        }

        Map<Long,CommentVO> map = comments.stream().collect(Collectors.toMap(CommentVO::getId, vo->vo));
        List<CommentVO> rootResluts = new ArrayList<>();

        for(CommentVO vo : comments){
             if(vo.getParentId()==null){
                 rootResluts.add(vo);
             }else{
                 CommentVO parent = map.get(vo.getParentId().longValue());
                 if(parent!=null){
                     if(parent.getChildren()==null){
                         parent.setChildren(new ArrayList<>());
                     }
                     parent.getChildren().add(vo);
                 }else{
                     log.warn("评论树构建：找不到父评论 - Child ID: {}, Parent ID: {}", vo.getId(), vo.getParentId());
                     rootResluts.add(vo); // 孤儿评论也作为根评论
                 }
             }
        }
        rootResluts.sort(Comparator.comparing(CommentVO::getCreatedAt).reversed());
        return rootResluts;
    }

    //递归排序子评论
    private void sortChildrenRecursively(List<CommentVO> comments){
           if(CollectionUtils.isEmpty(comments)){
               return;
           }
           comments.sort(Comparator.comparing(CommentVO::getCreatedAt));

           for(CommentVO vo : comments){
               sortChildrenRecursively(vo.getChildren());
           }
    }

    //发送评论通知
      private void sendCommentNotification(Comment comment,Long triggerUserId){
        if(notifyService==null){
            log.warn("NotifyService 未注入，无法发送评论通知");
            return;
        }

        try{
            Integer targetUserId = null;
            Integer articleId = (comment.getType() !=null && comment.getType() ==1) ?  comment.getForId() :null;
            Integer commentId = comment.getId().intValue();
            int notifyType = 0;
            if(comment.getToId() != null){
                targetUserId = comment.getToId().intValue();
                notifyType = 2;  //回复评论
            }

            else if(comment.getParentId() ==null) {
                if(comment.getType() ==1 && articleService!=null){
                    Article article= articleService.getById(comment.getForId().longValue());
                    if(article!=null){
                        targetUserId=article.getAuthorId();
                    }
                }
                else if(comment.getType() ==2 && talkService!=null){
                    Talk talk = talkService.getById(comment.getToId().longValue());
                    if(talk!=null){
                        targetUserId=talk.getUserId();
                    }
                }
                notifyType =1; //评论对象
            }
            //回复跟评论
            else{
                Comment parentComment =commentMapper.selectById(comment.getParentId().longValue());
                if(parentComment!=null){
                    targetUserId=parentComment.getForId();
                    notifyType=2;
                }
            }
            //发送通知
            if(targetUserId!=null&&!targetUserId.equals(triggerUserId.intValue())){
                notifyService.createNotify(triggerUserId.intValue(),targetUserId,commentId,articleId,notifyType);
                log.info("评论通知已发送 - Trigger User: {}, Receive User: {}, Type: {}, Comment ID: {}", triggerUserId, targetUserId, notifyType, commentId);
            } else if (targetUserId != null) {
                log.info("用户回复自己，不发送通知 - User ID: {}", triggerUserId);
            }

        } catch (Exception e) {
            log.error("发送评论通知时出错 - Comment ID: {}", comment.getId(), e);
        }
      }
}
