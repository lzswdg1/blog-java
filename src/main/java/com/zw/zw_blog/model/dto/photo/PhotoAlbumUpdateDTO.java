package com.zw.zw_blog.model.dto.photo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import java.io.Serializable;

/**
 * 对应: PUT /photoAlbum/update
 * 替代: controller/photoAlbum/index.js 中的 updatePhotoAlbum 方法参数
 */
@Data
public class PhotoAlbumUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'id'
     */
    @NotNull(message = "相册ID不能为空")
    private Long id;

    /**
     * 对应 'album_name'
     */
    @NotBlank(message = "相册名称不能为空")
    private String albumName;

    /**
     * 对应 'album_cover'
     */
    @NotBlank(message = "相册封面不能为空")
    @URL(message = "请输入合法的封面 URL")
    private String albumCover;
}