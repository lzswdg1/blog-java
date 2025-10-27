package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.header.Header;

import java.util.List;

// 对应 service/header/index.js
public interface PageHeaderService extends IService<Header> {

    /**
     * 更新或创建页面头部背景
     * (对应 updateHeader)
     */
    boolean updatePageHeader(String routeName, String bgUrl);

    /**
     * 获取所有页面头部背景
     * (对应 getHeaderList)
     */
    List<Header> getAllPageHeaders();
}