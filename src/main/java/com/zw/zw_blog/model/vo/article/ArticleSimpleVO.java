package com.zw.zw_blog.model.vo.article;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zw.zw_blog.model.vo.tag.TagVO;
import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用于文章列表展示的 VO (信息较少)
 */
@Data
public class ArticleSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String cover;
    private Integer isStick; // 是否置顶
    private LocalDateTime createdAt;

    // --- 关联数据 ---
    private String authorName;
    private String categoryName;
    private List<TagVO> tags;

    /**
     * 作者 ID (内部使用，不返回给前端)
     */
    @JsonIgnore // 2. 添加 @JsonIgnore 注解，防止 Jackson 将其序列化到 JSON 中
    private Integer authorId;

    /**
     * 分类 ID (内部使用，不返回给前端)
     */
    @JsonIgnore // 2. 添加 @JsonIgnore 注解
    private Integer categoryId;

    private UserSimpleVO author;
}