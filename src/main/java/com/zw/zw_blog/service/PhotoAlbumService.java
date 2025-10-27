package com.zw.zw_blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.photo.PhotoAlbum;
import com.zw.zw_blog.model.dto.photo.PhotoAlbumCreateDTO;
import com.zw.zw_blog.model.dto.photo.PhotoAlbumUpdateDTO;
import com.zw.zw_blog.model.vo.photo.PhotoAlbumVO;


import java.util.List;

// 对应 service/photoAlbum/index.js
public interface PhotoAlbumService extends IService<PhotoAlbum> {

    /**
     * 创建相册
     * (对应 createPhotoAlbum)
     */
    PhotoAlbum createPhotoAlbum(PhotoAlbumCreateDTO createDTO);

    /**
     * 更新相册
     * (对应 updatePhotoAlbum)
     */
    boolean updatePhotoAlbum(PhotoAlbumUpdateDTO updateDTO);

    /**
     * 删除相册 (需要同时删除相册下的照片)
     * (对应 deletePhotoAlbum)
     */
    boolean removePhotoAlbum(Long id);

    /**
     * 获取相册列表 (带照片数量)
     * (对应 getPhotoAlbumList)
     */
    List<PhotoAlbumVO> getPhotoAlbumListWithCount();
}