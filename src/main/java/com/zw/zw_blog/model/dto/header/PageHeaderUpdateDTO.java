package com.zw.zw_blog.model.dto.header;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PageHeaderUpdateDTO {

    @NotBlank(message = "ID 不能为空")
    private String id;

    @NotBlank(message = "Label 不能为空")
    private String label;

    @NotBlank(message = "Path 不能为空")
    private String path;

    @NotBlank(message = "Cover 不能为空")
    private String cover; // (此字段将映射到 实体类(Entity) 的 bgUrl)

    @NotBlank(message = "Sort 不能为空")
    private String sort; // (String 类型, service 中会转为 Integer)

    @NotBlank(message = "RouteName 不能为空")
    private String routeName;
}