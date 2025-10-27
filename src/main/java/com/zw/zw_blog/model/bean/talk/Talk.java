package com.zw.zw_blog.model.bean.talk;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对应 Sequelize 的 blog_talk 模型，说说（动态）表
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@TableName("blog_talk") // 强制表名为 blog_talk
public class Talk implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // content: 说说内容
    private String content;
    
    // user_id: 发布说说的用户id
    @TableField("user_id")
    private Integer userId;
    
    // status: 说说状态 1 公开 2 私密 3 回收站, 默认值 1
    private Integer status = 1;
    
    // is_top: 是否置顶 1 置顶 2 不置顶, 默认值 2
    @TableField("is_top")
    private Integer isTop = 2;
    
    // like_times: 点赞次数, 默认值 0
    @TableField("like_times")
    private Integer likeTimes = 0;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}