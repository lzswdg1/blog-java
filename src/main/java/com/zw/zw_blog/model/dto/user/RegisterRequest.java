package com.zw.zw_blog.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterRequest implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6,message = "密码不能小于6位")
    private String password;

    // 对应 controller/user/index.js 中的 register 方法
    private String nickName; // 昵称（可选）

    private String qq; // QQ（可选）
}
