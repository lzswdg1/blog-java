package com.zw.zw_blog.model.bean.header;

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
@TableName("blog_header")
public class Header implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("route_name")
    private String routeName;

    @TableField("bg_url")
    private String bgUrl;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

    // (*** 新增字段 ***)
    @TableField("sort")
    private Integer sort;

    // (*** 新增字段 ***)
    @TableField("label")
    private String label;

    // (*** 新增字段 ***)
    @TableField("path")
    private String path;
}