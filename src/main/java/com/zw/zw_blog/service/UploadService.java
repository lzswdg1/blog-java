package com.zw.zw_blog.service;


import org.springframework.web.multipart.MultipartFile;

// 对应 controller/utils/index.js 和 utils 下的上传逻辑
public interface UploadService {

    /**
     * 上传文件
     * @param file Spring 的 MultipartFile 对象
     * @return 文件访问 URL
     */
    String uploadFile(MultipartFile file);

    /**
     * 删除文件 (主要用于七牛云/MinIO)
     * @param fileName 文件名 (或 key)
     * @return boolean
     */
    boolean deleteFile(String fileName); // Node.js 项目在 controller 中有删除逻辑
}