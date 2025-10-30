package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.*;
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.bean.article.ArticleTags;
import com.zw.zw_blog.model.bean.category.Category;
import com.zw.zw_blog.model.bean.tag.Tag;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.article.ArticleCreateDTO;
import com.zw.zw_blog.model.dto.article.ArticleQueryDTO;
import com.zw.zw_blog.model.dto.article.ArticleUpdateDTO;
import com.zw.zw_blog.model.vo.article.ArticleDetailVO;
import com.zw.zw_blog.model.vo.article.ArticleSimpleVO;
import com.zw.zw_blog.model.vo.tag.TagVO;
import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import com.zw.zw_blog.service.ArticleService;
import com.zw.zw_blog.service.CommentService;
import com.zw.zw_blog.service.LikeService;
import com.zw.zw_blog.service.TagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    @Lazy
    private LikeMapper likeMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    @Lazy
    private TagService tagService;

    @Resource
    @Lazy
    private LikeService likeService;

    @Resource
    @Lazy
    private CommentService commentService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private ArticleTagsMapper articleTagsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Article createArticle(ArticleCreateDTO articleCreateDTO, Long authorId) {
         Article article = new Article();
         BeanUtils.copyProperties(articleCreateDTO, article);
         article.setAuthorId(authorId.intValue());
         if(articleCreateDTO.getPassword()!=null){
              article.setArticlePassword(passwordEncoder.encode(articleCreateDTO.getPassword()));
         }
         articleMapper.insert(article);
         if(articleCreateDTO.getTagIds()!=null&&articleCreateDTO.getTagIds().size()>0){
             List<Long> tagIds = articleCreateDTO.getTagIds();
             for(Long tagId: tagIds){
                 Tag tag =tagMapper.selectById(tagId);
                 if(tag==null){
                     log.warn("尝试关联不存在的标签 - Tag ID: {}", tagId);
                     continue; // 跳过无效的 tagId
                 }
                 //创建ArticleTag对象
                 ArticleTags articleTags = new ArticleTags();
                 articleTags.setArticleId(article.getId().intValue());
                 articleTags.setTagId(tagId.intValue());

                 try{
                    articleTagsMapper.insert(articleTags);
                 } catch (Exception e){
                     log.error("插入文章标签关联记录失败 - Article ID: {}, Tag ID: {}", article.getId(), tagId, e);
                     throw new BusinessException(ResultCode.TAG_ERRORWITH_ARTICLE); // 抛出异常让事务回滚
                 }
             }
         }else {
             log.info("创建文章时未指定标签- Article ID: {}", article.getId());
         }
         return article;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticle(ArticleUpdateDTO articleUpdateDTO,Long authorId){
            Article existArticle = articleMapper.selectById(articleUpdateDTO.getId());
            if(existArticle==null){
                throw new BusinessException(ResultCode.ARTICLE_NOT_EXIST);
            }
            User currentUser = userMapper.selectById(authorId);
            if(currentUser==null){
                throw new BusinessException(ResultCode.USER_NOT_EXIST);
            }
            Integer userRole = currentUser.getRole();
            if(userRole!=1 && !existArticle.getAuthorId().equals(authorId.intValue())){
                log.warn("无权限更新文章 - Article ID: {}, User ID: {}", articleUpdateDTO.getId(), authorId);
                    throw new BusinessException(ResultCode.NO_PERMISSION);
            }
            Article articleToUpdate = new Article();
            BeanUtils.copyProperties(articleUpdateDTO,articleToUpdate);

            //密码处理
        if(StringUtils.hasText(articleToUpdate.getArticlePassword())){
            articleToUpdate.setArticlePassword(passwordEncoder.encode(articleToUpdate.getArticlePassword()));
        }else{
            articleToUpdate.setArticlePassword(null);
        }
        // 处理标签关联 (先删旧，再加新) (对应 service/article/articleTag.js updateArticeTag)
        // 1. 删除旧关联
        QueryWrapper<ArticleTags> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("article_id",articleUpdateDTO.getId());
        int deleteCount = articleTagsMapper.delete(deleteWrapper);
        log.info("删除旧文章关联标签 - Article ID: {}, Rows deleted: {}", articleUpdateDTO.getId(), deleteCount);

        //添加新关联
        int updatedRows=updateArticleTags(articleUpdateDTO.getId().intValue(), articleUpdateDTO.getTagIds());


        return updatedRows > 0;
    }

    @Override
    public IPage<ArticleSimpleVO> getArticleList(ArticleQueryDTO articleQueryDTO) {
        Page<Article> page = new Page<>(articleQueryDTO.getCurrent(), articleQueryDTO.getSize());

        QueryWrapper<Article> wrapper = buildArticleQueryWrapper(articleQueryDTO);
        wrapper.orderByDesc("is_stick","create_time");
        IPage<Article> articlePage = articleMapper.selectPage(page, wrapper);
        return  convertArticlePageToSimpleVO(articlePage);
    }


    @Override
    public ArticleDetailVO getArticleDetail(Long id,String pwd){
        Article article = articleMapper.selectById(id);
        if(article==null){
            throw new BusinessException(ResultCode.ARTICLE_NOT_EXIST);
        }

        //密码校验
        if(StringUtils.hasText(article.getArticlePassword())){
            if(!StringUtils.hasText(pwd)||!passwordEncoder.matches(pwd,article.getArticlePassword())){
                log.warn("文章密码错误 -Article ID: {}",id);
                throw new BusinessException(ResultCode.ARTICLE_PASSWORD_ERROR);
            }
        }

        //model->vo转换

        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(article,articleDetailVO);
        articleDetailVO.setAuthorId(article.getAuthorId());
        articleDetailVO.setCategoryId(article.getCategoryId());


        //填充关联数据
        fillArticleVOWithRelatedData(Collections.singletonList(articleDetailVO));

        //填充上一篇/下一篇
        //查询上一篇(创建时间比当前文章发布时间早，status =1 为已发布)
        QueryWrapper<Article> preWrapper = new QueryWrapper<>();
        preWrapper.eq("status",1);
        preWrapper.lt("createdAt",article.getCreatedAt());
        preWrapper.orderByDesc("createdAt");
        preWrapper.last("LIMIT 1");
        Article preArticle = articleMapper.selectOne(preWrapper);
        if(preArticle==null){
            ArticleSimpleVO prevVO = new ArticleSimpleVO();
            BeanUtils.copyProperties(preArticle,prevVO);
            // 为上一篇填充关联数据 (可选，会增加查询)
             prevVO.setAuthorId(preArticle.getAuthorId());
             prevVO.setCategoryId(preArticle.getCategoryId());
             fillArticleVOWithRelatedData(Collections.singletonList(prevVO));
            articleDetailVO.setPrevArticle(prevVO);
        }


        //查询下一篇
        QueryWrapper<Article> nextWrapper = new QueryWrapper<>();
        nextWrapper.eq("status",1);
        nextWrapper.gt("createdAt",article.getCreatedAt());
        nextWrapper.orderByDesc("createdAt");
        nextWrapper.last("LIMIT 1");
        Article nextArticle = articleMapper.selectOne(nextWrapper);
        if(nextArticle==null){
            ArticleSimpleVO nextVO = new ArticleSimpleVO();
            BeanUtils.copyProperties(nextArticle,nextVO);
            nextVO.setAuthorId(nextArticle.getAuthorId());
            nextVO.setCategoryId(nextArticle.getCategoryId());
            fillArticleVOWithRelatedData(Collections.singletonList(nextVO));
            articleDetailVO.setNextArticle(nextVO);
        }
        return articleDetailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeArticle(Long id, Long authorId) {

        Article article = articleMapper.selectById(id);
        if(article==null){
            log.warn("尝试删除不存在的文章 - Article ID: {}", id);
            throw new BusinessException(ResultCode.ARTICLE_NOT_EXIST);
        }
        /*
         权限检查




        */

        //删除文章
        int deleArticleRows = articleMapper.deleteById(id);
        log.info("删除文章主体 -ID: {}, Rows deleted: {}", id, deleArticleRows);

        if(deleArticleRows>0){
            QueryWrapper<ArticleTags> tagsQueryWrapper = new QueryWrapper<>();
            tagsQueryWrapper.eq("article_id",id);
            int deletedTags = articleTagsMapper.delete(tagsQueryWrapper);
            log.info("删除文章标签关联 -ID: {}, Rows deleted: {}", id, deletedTags);

            //删除评论
            if(commentService != null){
                try{
                    commentService.removeCommentList(authorId);
                    log.info("删除了文章有关评论 -Article Id {}",id);
                }catch (Exception e){
                    log.error("删除评论出错 -Article id {}",id,e);
                }
            }

            //删除点赞
            if(likeService != null){
                try{
                    boolean likesRemoved = likeService.removeLikes(id,1);
                    log.info("删除文章关联点赞 -Article Id {},Success {}",id,likesRemoved);
                }catch (Exception e){
                    log.error("删除文章关联点赞失败 -Article id {}",id,e);
                }
            }
        }
        return deleArticleRows > 0;
    }
   //内部方法
    private int updateArticleTags(Integer articleId, List<Long> tagIds){
         if(articleId==null){
             return 0;
         }
         QueryWrapper<ArticleTags> deleteWrapper = new QueryWrapper<>();
         deleteWrapper.eq("article_id",articleId);
         int deleteCount = articleTagsMapper.delete(deleteWrapper);
        log.debug("删除旧文章标签关联 - Article ID: {}, Rows deleted: {}", articleId, deleteCount);
        if(!CollectionUtils.isEmpty(tagIds)){
            int addCount =0;
            for(Long tagId: tagIds){
                if(!tagMapper.exists(new  QueryWrapper<Tag>().eq("tag_id",tagId))){
                    log.warn("尝试关联不存在的标签 -Article ID: {}, Tag ID: {}", articleId, tagId);
                    continue;
                }
                ArticleTags articleTags = new ArticleTags();
                articleTags.setArticleId(articleId.intValue());
                articleTags.setTagId(tagId.intValue());
                try{
                    articleTagsMapper.insert(articleTags);
                    addCount++;
                }catch (Exception e){
                    log.error("插入文章关联标签失败 - Article ID: {}, Tag ID: {}", articleId, tagId, e);
                }
            }
            log.info("添加新文章标签关联 -Article ID: {}, Rows added: {}", articleId, addCount);
        }
        return deleteCount;
    }

    //内部方法
    private QueryWrapper<Article> buildArticleQueryWrapper(ArticleQueryDTO articleQueryDTO){
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleQueryDTO.getTitle()),"title",articleQueryDTO.getTitle());
        queryWrapper.eq(articleQueryDTO.getStatus() != null,"status",articleQueryDTO.getStatus());
        queryWrapper.eq(articleQueryDTO.getCategoryId() != null,"category_id",articleQueryDTO.getCategoryId());

        if(articleQueryDTO.getTagId()!=null){
            queryWrapper.inSql("id","SELECT article_id FROM blog_article_tag WhERE tag_id =" +  articleQueryDTO.getTagId());
            log.debug("添加Tag ID 的查询条件 -Tag ID: {}", articleQueryDTO.getTagId());
        }
        return queryWrapper;
    }

    //内部方法
    private IPage<ArticleSimpleVO> convertArticlePageToSimpleVO(IPage<Article> articlePage){
        List<Article> articles = articlePage.getRecords();
        List<ArticleSimpleVO> voList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(articles)){
            List<ArticleSimpleVO> simpleVos = articles.stream()
                    .map(article ->{
                        ArticleSimpleVO vo = new ArticleSimpleVO();
                        BeanUtils.copyProperties(article,vo);

                        vo.setCategoryId(article.getCategoryId());
                        vo.setAuthorId(article.getAuthorId());

                        return vo;
                    }).collect(Collectors.toList());
            //填充关联数据
            fillArticleVOWithRelatedData(simpleVos);
            voList.addAll(simpleVos);
        }
       IPage<ArticleSimpleVO> voPage =new Page<>(articlePage.getCurrent(),articlePage.getSize(),articlePage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    //内部方法
    private <T extends ArticleSimpleVO> void fillArticleVOWithRelatedData(List<T> voList){
        if(CollectionUtils.isEmpty(voList)){
            return;
        }
        //搜集查询的关联id
        List<Integer> authorIds = voList.stream()
                .map(ArticleSimpleVO::getAuthorId)
                .filter(id -> id!=null)
                .distinct()
                .collect(Collectors.toList());

        List<Integer> categoryIds= voList.stream()
                .map(ArticleSimpleVO::getCategoryId)
                .filter(id -> id!=null)
                .distinct()
                .collect(Collectors.toList());

        List<Long> articleIds = voList.stream()
                .map(ArticleSimpleVO::getId)
                .collect(Collectors.toList());


        //批量查询关联数据
        Map<Long,User> userMap = CollectionUtils.isEmpty(authorIds) ? Collections.emptyMap() :
                userMapper.selectBatchIds(authorIds.stream().mapToLong(Integer::longValue).boxed().collect(Collectors.toList()))
                        .stream().collect(Collectors.toMap(User::getId, u->u));

        Map<Long, Category> categoryMap = CollectionUtils.isEmpty(categoryIds) ? Collections.emptyMap() :
                categoryMapper.selectBatchIds(categoryIds.stream().mapToLong(Integer::longValue).boxed().collect(Collectors.toList()))
                        .stream().collect(Collectors.toMap(Category::getId, c->c));

        Map<Integer,List<ArticleTags>> articleTagMap = CollectionUtils.isEmpty(articleIds) ? Collections.emptyMap() :
                articleTagsMapper.selectList(new QueryWrapper<ArticleTags>().in("article_id",articleIds))
                        .stream().collect(Collectors.groupingBy(ArticleTags::getArticleId));

        List<Integer> alltagIds = articleTagMap.values().stream().flatMap(List::stream).map(ArticleTags::getTagId)
                .filter(id -> id!=null)
                .distinct()
                .collect(Collectors.toList());

        Map<Long,Tag> tagMap =CollectionUtils.isEmpty(alltagIds) ? Collections.emptyMap() :
                tagMapper.selectBatchIds(alltagIds.stream().mapToLong(Integer::longValue).boxed().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Tag::getId, tag->tag));
        //填充到vo列表中
        for(ArticleSimpleVO vo: voList){
            if(vo.getAuthorId() !=null){
                User user = userMap.get(vo.getAuthorId().longValue());
                if(user!=null){
                    UserSimpleVO authorVo = new UserSimpleVO();
                    BeanUtils.copyProperties(user,authorVo);
                    vo.setAuthor(authorVo);
                }else{
                    log.warn("填充文章VO： 找不到作者信息 -User ID: {}", vo.getAuthorId());
                }
            }
            //填充分类名
            if(vo.getCategoryId() !=null){
                Category category = categoryMap.get(vo.getCategoryId().longValue());
                if(category!=null){
                    vo.setCategoryName(category.getCategoryName());
                }else{
                    log.warn("填充文章VO：找不到分类信息 - Category ID: {}", vo.getCategoryId());
                }
            }

            //填充标签
            List<ArticleTags> articleTags = articleTagMap.getOrDefault(vo.getId().intValue(),Collections.emptyList());
            List<TagVO> tagVOs = articleTags.stream()
                    .map(at -> {
                        if(at.getTagId() ==null) return  null;
                        return tagMap.get(at.getTagId().longValue());
                    })
                    .filter(tag ->tag!=null)
                    .map(tag-> {
                        TagVO tagVO = new TagVO();
                        BeanUtils.copyProperties(tag,tagVO);
                        return tagVO;
                    })
                    .collect(Collectors.toList());
            vo.setTags(tagVOs);
        }
    }
}
