package com.zw.zw_blog.model.dto.page;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "当前页码不能小于1")
    private int current = 1; // 默认值

    @Min(value = 1, message = "每页数量不能小于1")
    private int size = 10; // 默认值
}
