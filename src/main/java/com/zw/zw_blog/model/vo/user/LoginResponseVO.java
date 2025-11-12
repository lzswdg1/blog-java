package com.zw.zw_blog.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录成功响应 视图对象
 * 对应 Node.js controller/user/index.js 中的 login 成功返回
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseVO {

    /**
     * 对应: token
     */
    private String token;

    /**
     * 对应: username
     */
    private String username;

    /**
     * 对应: role
     */
    private Integer role;

    /**
     * 对应: id
     */
    private Long id;

    /**
     * 对应: ipAddress
     */
    private String ipAddress;
}