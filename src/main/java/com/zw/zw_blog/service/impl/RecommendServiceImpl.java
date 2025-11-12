package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.ArticleMapper;
import com.zw.zw_blog.mapper.RecommendMapper; //
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.bean.recommend.Recommend; //
import com.zw.zw_blog.model.dto.recommend.RecommendCreateDTO; //
import com.zw.zw_blog.model.dto.recommend.RecommendUpdateDTO; //
import com.zw.zw_blog.model.vo.article.ArticleSimpleVO;
import com.zw.zw_blog.model.vo.recommend.RecommendVO; //
import com.zw.zw_blog.service.RecommendService; //
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend> implements RecommendService {
    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private ArticleMapper articleMapper;
    /**
     * 添加推荐
     * (实现我们修改后的接口，不再需要 userId)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Recommend addRecommend(RecommendCreateDTO createDTO) {
        Recommend recommend = new Recommend();
        // 从 DTO 拷贝 title, content, cover, isTop
        BeanUtils.copyProperties(createDTO, recommend);

        recommend.setCreatedAt(LocalDateTime.now());
        recommend.setUpdatedAt(LocalDateTime.now());

        // (将使用 Recommend.java 中的 @TableLogic 逻辑删除)
        recommend.setIsDelete(0);

        this.save(recommend);
        return recommend;
    }

    /**
     * 更新推荐
     * (实现接口)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRecommend(RecommendUpdateDTO updateDTO) {
        Recommend recommend = this.getById(updateDTO.getId());
        if (recommend == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        // 从 DTO 拷贝 title, content, cover, isTop
        BeanUtils.copyProperties(updateDTO, recommend);
        recommend.setUpdatedAt(LocalDateTime.now());

        return this.updateById(recommend);
    }

    /**
     * 删除推荐
     * (实现接口)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecommend(Long id) {
        // (因为 Recommend.java 中有 @TableLogic, removeById 会自动执行逻辑删除)
        return this.removeById(id);
    }

    /**
     * 获取推荐列表 (前台展示, 可能带置顶排序)
     * (实现接口)
     */
    @Override
    public List<RecommendVO> getHomeRecommendList() {
        LambdaQueryWrapper<Recommend> queryWrapper = new LambdaQueryWrapper<>();
        // 前台展示，按 isTop 降序，然后按创建时间降序
        queryWrapper.orderByDesc(Recommend::getIsTop)
                .orderByDesc(Recommend::getCreatedAt);

        List<Recommend> list = this.list(queryWrapper);

        // (*** 注意 ***)
        // 你的 VO 似乎有关联文章标题(title)，这里调用 convertToVOListWithArticle 才能获取到标题
        return convertToVOListWithArticle(list);
    }



    /**
     * 将 实体类分页 转换为 VO分页 (不查询关联文章)
     */
    private IPage<RecommendVO> convertToVOPage(IPage<Recommend> page) {
        List<RecommendVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        IPage<RecommendVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    /**
     * 分页获取推荐列表 (后台管理)
     * (实现接口)
     */
    @Override
    public IPage<RecommendVO> getAdminRecommendList(Page<Recommend> page) {
        //
        // 实体类 和 VO 都有 sort, 我们按 sort 排序
        IPage<Recommend> recommendPage = this.page(page,
                new LambdaQueryWrapper<Recommend>().orderByDesc(Recommend::getSort));

        // 后台管理需要显示文章标题，所以调用 WithArticle
        return convertToVOPageWithArticle(recommendPage);
    }


    /**
     * 获取推荐详情
     * (实现接口)
     */
    @Override
    public RecommendVO getRecommendDetail(Long id) {
        //
        Recommend recommend = this.getById(id);
        if (recommend == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        // 详情需要显示文章标题
        return convertToVOWithArticle(recommend);
    }



    /**
     * 私有辅助方法：将 Recommend 实体 转换为 VO (不含文章信息)
     * (VO 中只包含 articleId, sort, remark, createdAt)
     */
    private RecommendVO convertToVO(Recommend recommend) {
        if (recommend == null) return null;
        RecommendVO vo = new RecommendVO(); //

        // BeanUtils 会自动拷贝匹配的字段: id, articleId, sort, remark, createdAt
        BeanUtils.copyProperties(recommend, vo);

        // 实体类 中的 title, link, content, isTop, isDelete 将被忽略
        return vo;
    }

    /**
     * 私有辅助方法：转换并填充关联的 Article
     * (因为 VO 中有关联字段 article)
     */
    private RecommendVO convertToVOWithArticle(Recommend recommend) {
        RecommendVO vo = convertToVO(recommend);
        if (vo == null) return null;

        if (recommend.getArticleId() != null) { //
            Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                    .eq(Article::getId, recommend.getArticleId())
                    .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)); //

            if (article != null) {
                // (*** 已修正 ***)
                // 直接将 Article 实体的 "articleTitle" 赋给 VO 的 "title" 字段
                vo.setTitle(article.getArticleTitle());
            }
        }
        return vo;
    }

    /**
     * 私有辅助方法：批量转换并填充关联的 Article
     * (*** 已修复 "effectively final" 错误 ***)
     */
    private List<RecommendVO> convertToVOListWithArticle(List<Recommend> recommends) {
        if (recommends == null || recommends.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 提取所有 articleIds
        List<Long> articleIds = recommends.stream()
                .map(Recommend::getArticleId) //
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 2. 一次性查询所有关联的文章
        // (*** 修复点 ***)
        final Map<Long, Article> articleMap; // 声明为 final (或 effectively final)

        if (articleIds.isEmpty()) {
            articleMap = Collections.emptyMap(); // 赋值一次 (空 Map)
        } else {
            List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .in(Article::getId, articleIds)
                    .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)); //
            articleMap = articles.stream().collect(Collectors.toMap(Article::getId, a -> a)); // 赋值一次 (有数据的 Map)
        }
        // (*** 修复结束 ***)


        // 3. 组装 VO
        return recommends.stream().map(recommend -> {
            RecommendVO vo = convertToVO(recommend);
            if (recommend.getArticleId() != null) {
                // Lambda 表达式现在引用的 articleMap 是 (有效)final 的
                Article article = articleMap.get(recommend.getArticleId());
                if (article != null) {
                    // (*** 已修正 ***)
                    // 直接将 Article 实体的 "articleTitle" 赋给 VO 的 "title" 字段
                    vo.setTitle(article.getArticleTitle());
                }
            }
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 私有辅助方法：批量转换分页 (带文章信息)
     */
    private IPage<RecommendVO> convertToVOPageWithArticle(IPage<Recommend> page) {
        List<RecommendVO> voList = convertToVOListWithArticle(page.getRecords());
        IPage<RecommendVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }
}