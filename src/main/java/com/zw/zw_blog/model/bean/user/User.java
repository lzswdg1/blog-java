package com.zw.zw_blog.model.bean.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
@TableName("blog_user")
public class User implements Serializable {

    @TableId
    private Long id;

    @TableField(value = "username", select = false) // 默认不查询用户名
    private String username;

    private String password;

    private Integer role;

    @TableField("nick_name")
    private String nickName;

    private String qq;

    private String ip;

    private String avatar;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}