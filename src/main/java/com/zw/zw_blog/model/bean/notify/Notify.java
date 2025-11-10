package com.zw.zw_blog.model.bean.notify;

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
@TableName("blog_notify") // 强制表名为 blog_notify
public class Notify implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String message;

    @TableField("user_id")
    private Long userId; // <-- 必须从 Integer 修改为 Long

    private Integer type; // (类型可以保持 Integer)

    @TableField("to_id")
    private Long toId; // <-- 必须从 Integer 修改为 Long

    @TableField("trigger_user_id")
    private Long triggerUserId; // (已经是 Long)

    @TableField("receive_user_id")
    private Long receiveUserId; // (已经是 Long)

    @TableField("isView") // 确保这里的字段名 (isView) 和 ServiceImpl 中的 (isRead) 一致
    private Integer isRead; // (状态可以保持 Integer, 假设 isView 和 isRead 是一个东西)

    @TableField("article_id")
    private Long articleId; // (已经是 Long)

    @TableField("comment_id")
    private Long commentId; // (已经是 Long)

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}