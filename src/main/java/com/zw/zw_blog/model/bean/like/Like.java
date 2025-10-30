package com.zw.zw_blog.model.bean.like;

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
@TableName("blog_like") // 强制表名为 blog_like
public class Like implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // type: 点赞类型 1 文章 2 说说 3 留言 4 评论
    private Integer type;
    
    // for_id: 点赞的id 文章id 说说id 留言id
    @TableField("for_id")
    private Integer typeId;
    
    // user_id: 点赞用户id
    @TableField("user_id")
    private Integer userId;
    
    // ip: 点赞ip
    private String ip;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}