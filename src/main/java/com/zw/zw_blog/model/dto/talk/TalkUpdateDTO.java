package com.zw.zw_blog.model.dto.talk;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 说说更新 DTO
 * 对应 Node.js updateTalk 的参数
 */
@Data
public class TalkUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "说说ID不能为空")
    private Long id;

    @NotBlank(message = "说说内容不能为空")
    private String content;

    /**
     * 状态 (1公开 2私密 3回收站)
     */
    private Integer status;

    /**
     * 是否置顶 (1置顶 2不置顶)
     */
    private Integer isTop;

    /**
     * 图片 URL 列表
     */
    private List<String> images;
}