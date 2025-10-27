package com.zw.zw_blog.model.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends PageQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 对应 controller/user/index.js getUserList 方法
    private String nickName;
    private Integer role;
}
