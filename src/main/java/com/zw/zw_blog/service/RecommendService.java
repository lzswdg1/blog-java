package com.zw.zw_blog.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.recommend.Recommend;
import com.zw.zw_blog.model.dto.recommend.RecommendCreateDTO;
import com.zw.zw_blog.model.dto.recommend.RecommendUpdateDTO;
import com.zw.zw_blog.model.vo.recommend.RecommendVO;

import java.util.List;

// 对应 service/recommend/index.js
public interface RecommendService extends IService<Recommend> {

    /**
     * 添加推荐
     * (对应 addRecommend)
     */
    Recommend addRecommend(RecommendCreateDTO createDTO, Long userId);

    /**
     * 更新推荐
     * (对应 updateRecommend)
     */
    boolean updateRecommend(RecommendUpdateDTO updateDTO);

    /**
     * 删除推荐
     * (对应 deleteRecommend)
     */
    boolean removeRecommend(Long id);

    /**
     * 分页获取推荐列表 (后台管理)
     * (对应 getAdminRecommendList)
     */
    IPage<RecommendVO> getAdminRecommendList(Page<Recommend> page);

    /**
     * 获取推荐列表 (前台展示, 可能带置顶排序)
     * (对应 getHomeRecommendList)
     */
    List<RecommendVO> getHomeRecommendList();

    /**
     * 获取推荐详情
     * (对应 getRecommendDetail)
     */
    RecommendVO getRecommendDetail(Long id);
}