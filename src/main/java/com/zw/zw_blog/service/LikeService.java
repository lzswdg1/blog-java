package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.like.Like;

import java.util.List;
import java.util.Map;

// 对应 service/like/index.js
public interface LikeService extends IService<Like> {

    /**
     * 点赞/取消点赞
     * (对应 toggleLike)
     * @param userId 用户ID
     * @param typeId 点赞对象ID (文章/评论/说说 ID)
     * @param type 类型 (1文章 2评论 3说说)
     * @return true 如果是点赞, false 如果是取消点赞
     */
    boolean toggleLike(Long userId, Long typeId, Integer type);

    /**
     * 获取指定对象的点赞列表 (用户ID列表)
     * (对应 getLikeList)
     */
    List<Integer> getLikeList(Long typeId, Integer type);

    /**
     * 检查当前用户是否已点赞
     * (Node.js 项目中可能在 Controller 实现)
     */
    boolean isLiked(Long userId, Long typeId, Integer type);

    /**
     * 批量获取点赞数量
     * @param typeIds 对象ID列表
     * @param type 类型
     * @return Map<Long, Integer> (对象ID -> 点赞数)
     */
    Map<Long, Integer> getLikeCounts(List<Long> typeIds, Integer type);

    /**
     * 批量检查当前用户是否已点赞
     * @param typeIds 对象ID列表
     * @param type 类型
     * @param userId 当前用户ID
     * @return Map<Long, Boolean> (对象ID -> 是否已赞)
     */
//    Map<Long, Boolean> getIsLikedBatch(List<Long> typeIds, Integer type, Long userId);

    /**
     * 根据对象ID和类型删除所有点赞记录
     * (供删除文章/评论/说说时调用)
     */
//    boolean removeLikes(Long typeId, Integer type);
}