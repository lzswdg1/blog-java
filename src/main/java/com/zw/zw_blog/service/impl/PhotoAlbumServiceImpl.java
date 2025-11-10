package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.PhotoAlbumMapper;
import com.zw.zw_blog.mapper.PhotoMapper;
import com.zw.zw_blog.model.bean.photo.Photo;
import com.zw.zw_blog.model.bean.photo.PhotoAlbum;
import com.zw.zw_blog.model.dto.photo.PhotoAlbumCreateDTO;
import com.zw.zw_blog.model.dto.photo.PhotoAlbumUpdateDTO;
import com.zw.zw_blog.model.vo.photo.PhotoAlbumVO;
import com.zw.zw_blog.service.PhotoAlbumService;
import com.zw.zw_blog.service.PhotoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PhotoAlbumServiceImpl extends ServiceImpl<PhotoAlbumMapper, PhotoAlbum> implements PhotoAlbumService {
    @Resource
    private PhotoAlbumMapper photoAlbumMapper;

    @Resource
    private PhotoMapper photoMapper;

    @Resource
    @Lazy
    private PhotoService photoService;


    private static final int STATUS_NORMAL = 1;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PhotoAlbum createPhotoAlbum(PhotoAlbumCreateDTO updateDTO){
        if(checkNameExists(updateDTO.getAlbumName(),null)){
            throw new BusinessException("相册已经存在");
        }

        PhotoAlbum photoAlbum = new PhotoAlbum();
        BeanUtils.copyProperties(updateDTO,photoAlbum);
        photoAlbum.setCreatedAt(LocalDateTime.now());
        photoAlbum.setUpdatedAt(LocalDateTime.now());
        this.save(photoAlbum);
        return photoAlbum;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePhotoAlbum(PhotoAlbumUpdateDTO updateDTO){
        PhotoAlbum album = this.getById(updateDTO.getId());

        if(album == null){
            throw  new BusinessException("相册不存在");
        }

        if(!album.getAlbumName().equals(updateDTO.getAlbumName())&&checkNameExists(updateDTO.getAlbumName(),updateDTO.getId())){
            throw  new BusinessException("相册名字已经存在");
        }

        BeanUtils.copyProperties(updateDTO,album);
        album.setUpdatedAt(LocalDateTime.now());
        return this.updateById(album);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removePhotoAlbum(Long id){
        try{
            photoService.removePhotosByAlbumId(id);
        } catch (Exception e) {
            log.error("删除相册时，清理相册内照片失败 Album ID: {}",id,e);
            throw new BusinessException("清理相册照片失败",e);
        }

        return  this.removeById(id);
    }

    @Override
    public List<PhotoAlbumVO> getPhotoAlbumListWithCount(){
        List<PhotoAlbum> albums = this.list(new LambdaQueryWrapper<PhotoAlbum>()
                .orderByDesc(PhotoAlbum::getCreatedAt));

        if(CollectionUtils.isEmpty(albums)){
            return Collections.emptyList();
        }


        List<Long> albumIds = albums.stream().map(PhotoAlbum::getId).collect(Collectors.toList());

        LambdaQueryWrapper<Photo> countWrapper = new LambdaQueryWrapper<Photo>()
                .select(Photo::getAlbumId,"COUNT(id) as id_count")
                .eq(Photo::getStatus,STATUS_NORMAL)
                .in(Photo::getAlbumId,albumIds)
                .groupBy(Photo::getAlbumId);
        List<Map<String,Object>>  countMaps = photoMapper.selectMaps(countWrapper);
        Map<Long,Integer>  photoCountMap = countMaps.stream().
                collect(Collectors.toMap(
                        map ->(Long) map.get("album_id"),
                        map ->((Long) map.get("id_count")).intValue()
                ));


        return albums.stream()
                .map(album ->{
                    PhotoAlbumVO vo = new PhotoAlbumVO();
                    BeanUtils.copyProperties(album,vo);
                    vo.setPhotoCount(photoCountMap.getOrDefault(album.getId(),0));
                    return vo;
                }).collect(Collectors.toList());
    }


    private boolean checkNameExists(String name,Long id){
        LambdaQueryWrapper<PhotoAlbum> wrapper = new LambdaQueryWrapper<PhotoAlbum>()
                .eq(PhotoAlbum::getAlbumName,name);
        if(id!=null){
            wrapper.eq(PhotoAlbum::getId,id);
        }
        return this.count(wrapper)>0;
    }
}
