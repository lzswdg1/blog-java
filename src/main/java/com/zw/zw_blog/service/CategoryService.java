package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.category.Category;
import com.zw.zw_blog.model.vo.category.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {

    Category createCategory(String name);

    boolean updateCategory(Long id, String name);

    boolean removeCategory(Long id);

    List<CategoryVO> getCategoryListWithCount();
}