package com.zw.zw_blog.model.bean.links;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data // Lombok 自动生成 Getter, Setter, toString 等
@NoArgsConstructor // Lombok 自动生成无参构造函数
@EqualsAndHashCode(callSuper = false)
@TableName("blog_links") // 强制表名为 blog_links
public class Links implements Serializable {
    
    // 数据库主键 ID，MyBatis-Plus 自动递增
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    // site_name: 网站名称
    @TableField("site_name")
    private String siteName;
    
    // site_desc: 网站描述
    @TableField("site_desc")
    private String siteDesc;
    
    // site_avatar: 网站头像
    @TableField("site_avatar")
    private String siteAvatar;
    
    // url: 网站地址
    private String url;
    
    // status: 友链状态 1 待审核 2 审核通过
    private Integer status;
    
    // user_id: 申请者id
    @TableField("user_id")
    private String userId; // 注意：Sequelize 中是 STRING，因此 Java 也使用 String
    
    // createdAt 字段
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    // updatedAt 字段
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}