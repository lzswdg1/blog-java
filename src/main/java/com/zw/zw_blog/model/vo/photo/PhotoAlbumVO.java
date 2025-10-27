package com.zw.zw_blog.model.vo.photo;


import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于展示相册信息的 VO
 * 对应 controller/photoAlbum/index.js getPhotoAlbumList 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class PhotoAlbumVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 PhotoAlbum Model 的 id
     */
    private Long id;

    /**
     * 对应 PhotoAlbum Model 的 album_name
     */
    private String albumName;

    /**
     * 对应 PhotoAlbum Model 的 album_cover
     */
    private String albumCover;

    /**
     * 对应 PhotoAlbum Model 的 createdAt
     */
    private LocalDateTime createdAt;

    /**
     * 照片数量 (需要在 Service 层额外统计)
     * Node.js 项目中没有返回这个字段，但通常前端会需要
     */
    private Integer photoCount;
}