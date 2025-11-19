package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.header.Header;
import com.zw.zw_blog.model.dto.header.PageHeaderUpdateDTO;

import java.util.List;

public interface PageHeaderService extends IService<Header> {

    /**
     * (逻辑 A) 更新页面头部背景 (封面)
     */
    boolean updatePageHeaderCover(String routeName, String bgUrl); // (对应 PageHeaderCoverUpdateDTO)

    /**
     * 获取所有页面头部背景
     */
    List<Header> getAllPageHeaders();

    /**
     * (逻辑 B) (新) 更新页眉 (后台管理)
     */
    boolean updatePageHeaderAdmin(PageHeaderUpdateDTO headerDTO); // (对应 PageHeaderUpdateDTO)
}