package com.zw.zw_blog.model.dto.talk;

import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对应: POST /talk/list (后台管理获取说说列表)
 */
@Data
@EqualsAndHashCode(callSuper = true) // 包含父类的字段
public class TalkQueryDTO extends PageQueryDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 getTalkList 中的 'status' 查询条件
     */
    private Integer status;
}