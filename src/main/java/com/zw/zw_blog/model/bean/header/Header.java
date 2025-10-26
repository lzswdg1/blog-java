package com.zw.zw_blog.model.bean.header;

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
@TableName("blog_header") // 强制表名为 blog_header
public class Header implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // route_name: 路由名称
    @TableField("route_name")
    private String routeName;
    
    // bg_url: 背景图
    @TableField("bg_url")
    private String bgUrl;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}