package com.zw.zw_blog.service;


import com.zw.zw_blog.model.vo.statistic.StatisticVO;

// 对应 controller/statistic/index.js 的逻辑
public interface StatisticService {

    /**
     * 获取网站统计数据
     */
    StatisticVO getStatistics();
}