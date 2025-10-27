package com.zw.zw_blog.model.dto.talk;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 对应: POST /talk/add
 * 替代: controller/talk/index.js 中的 addTalk 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class TalkCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 addTalk 中的 'content'
     * 替代: if (!content) 校验
     */
    @NotBlank(message = "说说内容不能为空")
    private String content;

    /**
     * 对应 addTalk 中的 'status' (说说状态: 1 公开 2 私密)
     */
    private Integer status;

    /**
     * 对应 addTalk 中的 'images' (图片列表)
     */
    private List<String> images;
}