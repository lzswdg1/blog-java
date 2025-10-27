package com.zw.zw_blog.model.bean.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@TableName("blog-articleTags")
public class ArticleTags implements Serializable {
    @TableId(value = "article_id",type= IdType.AUTO)
    private Integer articleId;
    
    @TableField("tag_id")
    private Integer tagId;
    
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}
