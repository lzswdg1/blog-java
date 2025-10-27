package com.zw.zw_blog.model.dto.links;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.io.Serializable;// 导入 URL 校验？
/**
 * 对应: POST /links/add (友链申请)
 * 对应: controller/links/index.js 中的 addLink 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class LinkCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'name' 字段
     */
    @NotBlank(message = "网站名称不能为空")
    private String name;

    /**
     * 对应 'avatar' 字段
     */
    private String avatar;

    /**
     * 对应 'desc' 字段
     */
    @NotBlank(message = "网站描述不能为空")
    private String desc;

    /**
     * 对应 'url' 字段
     */
    @NotBlank(message = "网站地址不能为空")
    @URL(message = "请输入合法的网站 URL")
    private String url;
}
