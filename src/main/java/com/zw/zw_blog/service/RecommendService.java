package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.recommend.Recommend; //
import com.zw.zw_blog.model.dto.recommend.RecommendCreateDTO; //
import com.zw.zw_blog.model.dto.recommend.RecommendUpdateDTO; //
import com.zw.zw_blog.model.vo.recommend.RecommendVO; //

import java.util.List;

public interface RecommendService extends IService<Recommend> {

    Recommend addRecommend(RecommendCreateDTO createDTO);

    /**
     * 更新推荐
     */
    boolean updateRecommend(RecommendUpdateDTO updateDTO);

    /**
     * 删除推荐
     */
    boolean deleteRecommend(Long id);

    /**
     * 分页获取推荐列表 (后台管理)
     */
    IPage<RecommendVO> getAdminRecommendList(Page<Recommend> page);

    /**
     * 获取推荐列表 (前台展示, 可能带置顶排序)
     */
    List<RecommendVO> getHomeRecommendList();

    /**
     * 获取推荐详情
     */
    RecommendVO getRecommendDetail(Long id);
}