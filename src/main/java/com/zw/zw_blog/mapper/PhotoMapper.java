package com.zw.zw_blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zw.zw_blog.model.bean.photo.Photo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PhotoMapper extends BaseMapper<Photo> {
}
