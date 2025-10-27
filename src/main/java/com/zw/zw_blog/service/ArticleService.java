package com.zw.zw_blog.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.dto.article.ArticleCreateDTO;
import com.zw.zw_blog.model.dto.article.ArticleQueryDTO;
import com.zw.zw_blog.model.dto.article.ArticleUpdateDTO;
import com.zw.zw_blog.model.vo.article.ArticleDetailVO;
import com.zw.zw_blog.model.vo.article.ArticleSimpleVO;

public interface ArticleService extends IService<Article> {

    Article createArticle(ArticleCreateDTO createDTO, Long authorId);

    boolean updateArticle(ArticleUpdateDTO updateDTO, Long authorId);

    boolean removeArticle(Long id, Long authorId);

    IPage<ArticleSimpleVO> getArticleList(ArticleQueryDTO queryDTO);

    ArticleDetailVO getArticleDetail(Long id, String password);

    // ... 其他文章相关接口 ...
}