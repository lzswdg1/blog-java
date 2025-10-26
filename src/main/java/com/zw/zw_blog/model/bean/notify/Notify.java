package com.zw.zw_blog.model.bean.notify;

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
@TableName("blog_notify") // 强制表名为 blog_notify
public class Notify implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // message: 通知内容
    private String message;
    
    // user_id: 通知给谁
    @TableField("user_id")
    private Integer userId;
    
    // type: 通知类型 1 文章 2 说说 3 留言 4 友链
    private Integer type;
    
    // to_id: 说说或者是文章的id 用于跳转
    @TableField("to_id")
    private Integer toId;
    
    // isView: 是否被查看 1 没有 2 已经查看, 默认值 1
    @TableField("isView")
    private Integer isView = 1;
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}