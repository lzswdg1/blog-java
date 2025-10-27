package com.zw.zw_blog.model.vo.recommend;


import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于展示推荐信息的 VO
 * 对应 controller/recommend/index.js [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/recommend/index.js] 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class RecommendVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Recommend Model 的 id [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/recommend/recommend.js]
     */
    private Long id;

    /**
     * 对应 Recommend Model 的 title [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/recommend/recommend.js]
     */
    private String title;

    /**
     * 对应 Recommend Model 的 content [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/recommend/recommend.js]
     */
    private String content;

    /**
     * 对应 Recommend Model 的 cover [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/recommend/recommend.js]
     */
    private String cover;

    /**
     * 对应 Recommend Model 的 is_top [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/recommend/recommend.js]
     */
    private Integer isTop;

    /**
     * 推荐发布者的用户信息 (嵌套 VO)
     * 对应 Controller [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/recommend/index.js] 中关联查询的用户信息
     */
    private UserSimpleVO user; // 使用之前创建的简化 User VO

    /**
     * 对应 Recommend Model 的 createdAt [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/recommend/recommend.js]
     */
    private LocalDateTime createdAt;
}