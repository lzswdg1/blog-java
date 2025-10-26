package com.zw.zw_blog.model.bean.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data // Lombok 自动生成 Getter, Setter, toString 等
@NoArgsConstructor // Lombok 自动生成无参构造函数
@EqualsAndHashCode(callSuper = false)
@TableName("blog_comment") // 强制表名为 blog_comment
public class Comment implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // parent_id: 评论父级id
    @TableField("parent_id")
    private Integer parentId;
    
    // type: 评论类型 1 文章 2 说说 3 留言 ...
    private Integer type;
    
    // for_id: 评论的对象id
    @TableField("for_id")
    private Integer forId;
    
    // from_id: 评论人id
    @TableField("from_id")
    private Integer fromId;
    
    // from_name: 评论人昵称
    @TableField("from_name")
    private String fromName;
    
    // from_avatar: 评论人头像
    @TableField("from_avatar")
    private String fromAvatar;
    
    // to_id: 被回复的人id
    @TableField("to_id")
    private Integer toId;
    
    // to_name: 被回复人的昵称
    @TableField("to_name")
    private String toName;
    
    // to_avatar: 被回复人的头像
    @TableField("to_avatar")
    private String toAvatar;
    
    // content: 评论内容
    private String content;
    
    // thumbs_up: 评论点赞数，默认值 0
    @TableField("thumbs_up")
    private Integer thumbsUp = 0;
    
    // ip: ip地址
    private String ip;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}