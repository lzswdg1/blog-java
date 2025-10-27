package com.zw.zw_blog.model.bean.tag;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对应 Sequelize 的 blog_tag 模型，标签表
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@TableName("blog_tag") // 强制表名为 blog_tag
public class Tag implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // tag_name: 标签名称 唯一，String(55)
    @TableField("tag_name")
    // 唯一性（unique: true）通常通过数据库DDL约束或在 Service 层通过查询校验实现。
    private String tagName;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}