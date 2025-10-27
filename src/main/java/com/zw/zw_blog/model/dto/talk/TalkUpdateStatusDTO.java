package com.zw.zw_blog.model.dto.talk;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

/**
 * 对应: PUT /talk/update/status
 */
@Data
public class TalkUpdateStatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 updateStatus 中的 'id'
     */
    @NotNull(message = "说说ID不能为空")
    private Long id;

    /**
     * 对应 updateStatus 中的 'status'
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}