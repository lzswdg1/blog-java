package com.zw.zw_blog.model.vo.links;


import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用于展示友链信息的 VO
 * 对应 controller/links/index.js getLinksList 返回的数据结构
 * VOs 是使用 @Data 的安全场景
 */
@Data
public class LinkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 Links Model 的 id
     */
    private Long id;

    /**
     * 对应 Links Model 的 name
     */
    private String name;

    /**
     * 对应 Links Model 的 avatar
     */
    private String avatar;

    /**
     * 对应 Links Model 的 desc
     */
    private String desc;

    /**
     * 对应 Links Model 的 url
     */
    private String url;

    /**
     * 对应 Links Model 的 status (友链状态 1 通过 2 待审核)
     */
    private Integer status;

    /**
     * 对应 Links Model 的 createdAt (申请时间)
     */
    private LocalDateTime createdAt;
}