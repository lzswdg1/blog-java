package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.mapper.PhotoMapper;
import com.zw.zw_blog.model.bean.photo.Photo;
import com.zw.zw_blog.model.dto.photo.PhotoAddDTO;
import com.zw.zw_blog.model.vo.photo.PhotoVO;
import com.zw.zw_blog.service.PhotoService;
import com.zw.zw_blog.service.UploadService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    @Lazy
    private UploadService uploadService;

    private static final int STATUS_NORMAL = 1;
    private static final int STATUS_RECYCLED = 2;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPhotos(PhotoAddDTO photoAddDTO) {
          if(CollectionUtils.isEmpty(photoAddDTO.getImages())){
              return true;
          }

          List<Photo> photoList = photoAddDTO.getImages().stream()
                  .map(url ->{
                      Photo photo = new Photo();
                      photo.setAlbumId(photoAddDTO.getAlbumId().intValue());
                      photo.setUrl(url);
                      photo.setStatus(STATUS_NORMAL);
                      photo.setCreatedAt(LocalDateTime.now());
                      photo.setUpdatedAt(LocalDateTime.now());
                      return photo;
                  }).collect(Collectors.toList());
          return this.saveBatch(photoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removePhoto(Long id) {
        return this.update(new LambdaUpdateWrapper<Photo>()
                .eq(Photo::getId, id)
                .set(Photo::getStatus, STATUS_RECYCLED));
    }

    @Override
    public List<PhotoVO>  getPhotoListByAlbumId(Long albumId) {
        List<Photo> photos = this.list(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId).
                eq(Photo::getStatus, STATUS_NORMAL)
        .orderByAsc(Photo::getCreatedAt));

        if(CollectionUtils.isEmpty(photos)){
            return Collections.emptyList();
        }

        return photos.stream().map(photo ->  {
            PhotoVO photoVO = new PhotoVO();
            BeanUtils.copyProperties(photo,photoVO);
            return photoVO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removePhotosByAlbumId(Long albumId) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();

        List<Photo>  photos = this.list(queryWrapper);

        if(CollectionUtils.isEmpty(photos)){
            return  true;
        }

        for(Photo photo:photos){
            try{
                String key =extractKeyFromUrl(photo.getUrl());
                if(key!=null){
                    uploadService.deleteFile(key);
                }
            } catch (Exception e) {
                log.error("删除云端文件失败: {}",photo.getUrl(),e);
            }
        }

        return  this.remove(queryWrapper);
    }

    private String extractKeyFromUrl(String url){
        if(!StringUtils.hasText(url)){
            return null;
        }

        try{
            int lastSlash =url.lastIndexOf('/');
            if(lastSlash!=-1&&lastSlash<url.length()-1){
                return url.substring(lastSlash+1);
            }
        }catch (Exception e){
            log.error("提取URL Key失败 :{}",url,e);
        }
        return   null;
    }
}

