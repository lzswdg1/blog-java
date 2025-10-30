package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.ArticleMapper;
import com.zw.zw_blog.mapper.CategoryMapper;
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.bean.category.Category;
import com.zw.zw_blog.model.vo.category.CategoryVO;
import com.zw.zw_blog.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;


    @Resource
    private ArticleMapper articleMapper;


    @Override
    public Category createCategory(String name){
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name",name);

        Category category = categoryMapper.selectOne(queryWrapper);

        if(category == null){
            Category newCategory = new Category();
            newCategory.setCategoryName(name);
            categoryMapper.insert(newCategory);
            return category;
        }else {
            throw new BusinessException(ResultCode.CATEGORY_EXIST);
        }
    }


    @Override
    public boolean updateCategory(Long id, String name){
       Category category = categoryMapper.selectById(id);
       if(category == null){
           throw new BusinessException(ResultCode.CATEGORY_NOT_EXIST);
       }else{
           QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("name",name);
           queryWrapper.ne("id",id);
           if(categoryMapper.exists(queryWrapper)){
               throw new BusinessException(ResultCode.CATEGORY_EXIST);
           }
           Category newCategory = new Category();
           newCategory.setId(id);
           newCategory.setCategoryName(name);
           return categoryMapper.updateById(newCategory) > 0;
       }
    }

    @Override
    public boolean removeCategory(Long id){
        Category category = categoryMapper.selectById(id);
        if(category == null){
            throw new BusinessException(ResultCode.CATEGORY_NOT_EXIST);
        }else{
            //检查该分类下是否有文章
            QueryWrapper<Article> articleQueryWrapper= new QueryWrapper<>();
            articleQueryWrapper.eq("category_id",id);
            if(articleMapper.exists(articleQueryWrapper)){
                throw new BusinessException(ResultCode.CATEGORY_HAS_ARTICLE);
            }
            return categoryMapper.deleteById(id) > 0;
        }
    }

    @Override
    public List<CategoryVO> getCategoryListWithCount(){
        List<Category> categoryList = categoryMapper.selectList(null);
        if(CollectionUtils.isEmpty(categoryList)){
            return Collections.emptyList();
        }
        //统计每个分类下面的文章数量
        List<Long> categoryIds = categoryList.stream().map(Category::getId).collect(Collectors.toList());

        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.select("category_id","count(*) as article_count");

        articleQueryWrapper.in("category_id",categoryIds);
        articleQueryWrapper.groupBy("category_id");
        List<Map<String, Object>> couns = articleMapper.selectMaps(articleQueryWrapper);

        Map<Long,Integer> countMap=couns.stream().
                collect(Collectors.toMap(
                        map-> ((Number) map.get("category_id")).longValue(), //key分类id
                        map-> ((Number) map.get("article_count")).intValue()) //value文章数量
                );

                return categoryList.stream().map(category -> {
                    CategoryVO categoryVO = new CategoryVO();
                    BeanUtils.copyProperties(category,categoryVO);
                    categoryVO.setArticleCount(countMap.getOrDefault(category.getId(),0));
                    return categoryVO;
                }).collect(Collectors.toList());
    }
}
