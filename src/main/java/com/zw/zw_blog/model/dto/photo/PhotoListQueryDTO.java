package com.zw.zw_blog.model.dto.photo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: POST /photo/list
 * 替代: controller/photo/index.js 中的 getPhotoList 方法参数
 */
@Data
public class PhotoListQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'album_id'
     */
    @NotNull(message = "相册ID不能为空")
    private Long albumId;
}