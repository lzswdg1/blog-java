package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zw.zw_blog.mapper.ArticleMapper;
import com.zw.zw_blog.mapper.CategoryMapper;
import com.zw.zw_blog.mapper.MessageMapper;
import com.zw.zw_blog.mapper.TagMapper;
import com.zw.zw_blog.model.bean.message.Message;
import com.zw.zw_blog.model.vo.statistic.StatisticVO;
import com.zw.zw_blog.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 获取博客后台统计数据
     */
    @Override
    public StatisticVO getStatistics() {
        // 1. 查询文章数量
        // (使用 null 作为查询条件，表示查询所有)
        Long articleCount = articleMapper.selectCount(null);

        // 2. 查询分类数量
        Long categoryCount = categoryMapper.selectCount(null);

        // 3. 查询标签数量
        Long tagCount = tagMapper.selectCount(null);

        // 4. 查询留言数量
        // (注意：这里我们假设 Message 表有 isDelete 字段进行逻辑删除)
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        // (如果你的 Message 实体类中定义了 @TableLogic，
        //  MyBatis-Plus 的 selectCount 会自动排除已删除的，
        //  但为了保险起见，我们显式指定)
        messageQueryWrapper.eq("is_delete", 0);
        Long messageCount = messageMapper.selectCount(messageQueryWrapper);

        // 5. 组装 VO
        StatisticVO vo = new StatisticVO();
        vo.setArticleCount(articleCount);
        vo.setCategoryCount(categoryCount);
        vo.setTagCount(tagCount);
        vo.setMessageCount(messageCount);

        return vo;
    }
}