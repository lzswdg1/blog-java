package com.zw.zw_blog.model.vo.photo;

import lombok.Data;
import java.io.Serializable;

/**
 * 用于展示照片信息的 VO
 * 对应 controller/photo/index.js getPhotoList 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class PhotoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Photo Model 的 id
     */
    private Long id;

    /**
     * 对应 Photo Model 的 url
     */
    private String url;

    // albumId 通常不需要返回给前端，因为前端请求时已经知道了 albumId
}