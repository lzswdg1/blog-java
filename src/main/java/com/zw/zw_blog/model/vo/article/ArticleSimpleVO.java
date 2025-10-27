package com.zw.zw_blog.model.vo.article;


import com.zw.zw_blog.model.vo.tag.TagVO;
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
}