package com.zw.zw_blog.model.vo.category;


import lombok.Data;
import java.io.Serializable;

/**
 * 用于展示分类信息的 VO
 * 对应 controller/category/index.js [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/category/index.js] getCategoryList 和 getCategoryDetailById 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class CategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Category Model 的 id [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/category/category.js]
     */
    private Long id;

    /**
     * 对应 Category Model 的 name [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/model/category/category.js]
     */
    private String name;

    /**
     * 该分类下的文章数量
     * 对应 Controller [cite: uploaded:lzswdg1/blog/blog-03cab1b5da27ae2b4e76f48a578f73924bbd9183/blog-server/src/controller/category/index.js] 中的 article_count
     * 需要在 Service 层额外查询计算
     */
    private Integer articleCount;
}