package com.zw.zw_blog.model.dto.user;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "不能为空")
    private String nickname;


    @NotBlank(message = "不能为空")
    private String avatar;

    @NotBlank(message = "不能为空")
    private String qq;
}
