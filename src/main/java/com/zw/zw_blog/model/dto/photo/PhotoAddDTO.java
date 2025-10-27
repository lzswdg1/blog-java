package com.zw.zw_blog.model.dto.photo;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 对应: POST /photo/add
 * 替代: controller/photo/index.js 中的 addPhoto 方法参数
 */
@Data
public class PhotoAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'album_id'
     * 替代: if (!album_id) 校验
     */
    @NotNull(message = "相册ID不能为空")
    private Long albumId;

    /**
     * 对应 'images' (一个 URL 数组)
     * 替代: if (!images.length) 校验
     */
    @NotEmpty(message = "图片列表不能为空")
    private List<String> images; // 假设前端传来的是图片 URL 列表
}