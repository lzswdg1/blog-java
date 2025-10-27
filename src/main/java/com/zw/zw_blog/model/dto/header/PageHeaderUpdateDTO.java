package com.zw.zw_blog.model.dto.header;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.io.Serializable;

/**
 * 对应: PUT /pageHeader/update
 * 替代: controller/header/index.js 中的 updateHeader 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class PageHeaderUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'route_name'
     * 替代: if (!route_name) 校验
     */
    @NotBlank(message = "路由名称不能为空")
    private String routeName; // Java 中使用驼峰命名

    /**
     * 对应 'bg_url'
     * 替代: if (!bg_url) 校验
     */
    @NotBlank(message = "背景图片URL不能为空")
    @URL(message = "请输入合法的背景图片 URL")
    private String bgUrl; // Java 中使用驼峰命名
}