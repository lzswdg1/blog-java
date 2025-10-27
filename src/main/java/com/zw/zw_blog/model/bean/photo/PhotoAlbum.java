package com.zw.zw_blog.model.bean.photo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 对应 Sequelize 的 blog_photo_album 模型，相册信息表
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@TableName("blog_photo_album") // 强制表名为 blog_photo_album
public class PhotoAlbum implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // album_name: 相册名称
    @TableField("album_name")
    private String albumName;
    
    // album_cover: 相册封面
    @TableField("album_cover")
    private String albumCover;
    
    // description: 相册描述信息
    private String description;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}