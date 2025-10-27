package com.zw.zw_blog.model.dto.photo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL; // 导入 URL 校验
import java.io.Serializable;

/**
 * 对应: POST /photoAlbum/add
 * 替代: controller/photoAlbum/index.js 中的 addPhotoAlbum 方法参数
 */
@Data
public class PhotoAlbumCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'album_name'
     * 替代: if (!album_name) 校验
     */
    @NotBlank(message = "相册名称不能为空")
    private String albumName;

    /**
     * 对应 'album_cover'
     * 替代: if (!album_cover) 校验
     */
    @NotBlank(message = "相册封面不能为空")
    @URL(message = "请输入合法的封面 URL") // 使用我们讨论过的 URL 校验
    private String albumCover;
}
