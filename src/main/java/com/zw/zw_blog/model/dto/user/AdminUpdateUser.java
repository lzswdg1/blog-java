package com.zw.zw_blog.model.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;


@Data
public class AdminUpdateUser implements Serializable {

    @NotNull(message = "用户ID不能为空")
    private Long id;
    private String nickName;
    private String avatar;
}
