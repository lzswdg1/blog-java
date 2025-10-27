package com.zw.zw_blog.model.dto.tag;

import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagQueryDTO extends PageQueryDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 对应 controller/tag/index.js getTagList 中的 'name' 查询条件
     */
    private String name;
}
