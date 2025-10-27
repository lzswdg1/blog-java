package com.zw.zw_blog.model.vo.user;


import lombok.Data;
import java.io.Serializable;

/**
 * 用于展示用户基本信息的 VO
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username; // 有时需要返回用户名，比如登录成功时
    private String nickName;
    private String avatar;
    private Integer role;
    private String ipAddress; // IP 属地，而不是原始 IP
}