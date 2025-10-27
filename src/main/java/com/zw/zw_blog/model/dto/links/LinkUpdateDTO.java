package com.zw.zw_blog.model.dto.links;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对应: PUT /links/update
 */
@Data
@EqualsAndHashCode(callSuper = true) // 包含父类的字段
public class LinkUpdateDTO extends LinkCreateDTO {
    // 自动继承 LinkCreateDTO 中的所有字段和校验
    private static final long serialVersionUID = 1L;

    /**
     * 对应 updateLink 中的 'id'
     */
    @NotNull(message = "友链ID不能为空")
    private Long id;

    /**
     * 对应 updateLink 中的 'status'
     */
    private Integer status;
}