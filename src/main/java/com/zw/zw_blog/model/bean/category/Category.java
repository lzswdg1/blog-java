package com.zw.zw_blog.model.bean.category;

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
@TableName("blog_category") // 强制表名为 blog_category
public class Category implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    // 假设 id 是被 Sequelize 自动管理的
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // category_name: 分类名称 唯一，String(55)
    @TableField("category_name")
    // 唯一性（unique: true）通常在数据库DDL中设置，在Java Service层通过查询进行校验。
    private String categoryName;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}