package com.zw.zw_blog.model.dto.header;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;

@Data
public class PageHeaderCoverUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "路由名称不能为空")
    private String routeName;

    @NotBlank(message = "背景图片URL不能为空")
    // @URL(message = "请输入合法的背景图片 URL") // @URL 校验比较宽松，如果你需要严格校验，可以保留
    private String bgUrl;
}