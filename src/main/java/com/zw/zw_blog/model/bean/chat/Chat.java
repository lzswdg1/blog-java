package com.zw.zw_blog.model.bean.chat;


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
@TableName("blog_chat") // 强制表名为 blog_chat
public class Chat implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // user_id: 用户id 用于判断是谁发送的
    @TableField("user_id")
    private Integer userId;
    
    // content: 聊天内容，String(555)
    private String content;
    
    // content_type: 聊天的内容格式 如果是文本就是text 图片就是 img
    @TableField("content_type")
    private String contentType;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}