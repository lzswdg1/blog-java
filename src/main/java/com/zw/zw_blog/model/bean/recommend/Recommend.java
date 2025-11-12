package com.zw.zw_blog.model.bean.recommend;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@ToString
@TableName("blog_recommend") // 强制表名为 blog_recommend
public class Recommend implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // title: 推荐网站标题
    private String title;
    private String cover;

    private String content;
    @TableField("article_id")
    private Long articleId;

    private Integer isTop;
    private Integer sort;
    private String remark;

    @TableLogic
    @JsonIgnore
    @TableField("is_delete")
    private Integer isDelete;

    // link: 网站地址
    private String link;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}