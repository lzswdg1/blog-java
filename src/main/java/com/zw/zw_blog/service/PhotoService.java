package com.zw.zw_blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.photo.Photo;
import com.zw.zw_blog.model.dto.photo.PhotoAddDTO;
import com.zw.zw_blog.model.vo.photo.PhotoVO;


import java.util.List;

// 对应 service/photo/index.js
public interface PhotoService extends IService<Photo> {

    /**
     * 批量添加照片到相册
     * (对应 addPhotos)
     */
    boolean addPhotos(PhotoAddDTO addDTO);

    /**
     * 删除照片
     * (对应 deletePhoto)
     */
    boolean removePhoto(Long id);

    /**
     * 根据相册 ID 获取照片列表
     * (对应 getPhotoListByAlbumId)
     */
    List<PhotoVO> getPhotoListByAlbumId(Long albumId);

    /**
     * 根据相册 ID 删除所有照片 (供 PhotoAlbumService 调用)
     * (Node.js 项目中可能隐式包含此逻辑)
     */
    boolean removePhotosByAlbumId(Long albumId);
}