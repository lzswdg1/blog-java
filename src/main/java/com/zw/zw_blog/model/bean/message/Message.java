package com.zw.zw_blog.model.bean.message;

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
@TableName("blog_message") // 强制表名为 blog_message
public class Message implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // message: 留言内容
    private String message;
    
    // color: 字体颜色, 默认值 '#676767'
    private String color = "#676767";
    
    // font_size: 字体大小, 默认值 12
    @TableField("font_size")
    private Integer fontSize = 12;
    
    // font_weight: 字体宽度, 默认值 500
    @TableField("font_weight")
    private Integer fontWeight = 500;
    
    // bg_color: 背景颜色
    @TableField("bg_color")
    private String bgColor;
    
    // bg_url: 背景图片
    @TableField("bg_url")
    private String bgUrl;
    
    // user_id: 留言用户的id
    @TableField("user_id")
    private Integer userId;
    
    // nick_name: 游客用户的昵称
    @TableField("nick_name")
    private String nickName;
    
    // tag: 标签
    private String tag;
    
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