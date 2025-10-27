package com.zw.zw_blog.model.dto.notify;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 对应: PUT /notify/read (批量已读)
 * 对应: DELETE /notify/delete (批量删除)
 * 替代: controller/notify/index.js 中的 readNotify 和 deleteNotify 方法参数
 * DTOs 是使用 @Data 的安全场景
 */
@Data
public class NotifyBatchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 'ids' (一个 ID 数组)
     * 替代: if (!ids.length) 校验
     */
    @NotEmpty(message = "ID列表不能为空")
    private List<Long> ids;
}