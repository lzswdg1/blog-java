package com.zw.zw_blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zw.zw_blog.mapper")
public class ZwBlogApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ZwBlogApplication.class, args);
    }
    
}
