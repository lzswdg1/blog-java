package com.zw.zw_blog.model.dto.links;


import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 对应: POST /links/list (后台管理获取友链列表)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkQueryDTO extends PageQueryDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 getLinksList 中的 'status' 查询条件
     */
    private Integer status;

    private String siteName;

    private Date startTime;
    private Date endTime;
}