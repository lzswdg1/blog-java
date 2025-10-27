package com.zw.zw_blog.model.vo.user;


import lombok.Data;
import java.io.Serializable;

/**
 * 用于在其他 VO (如 CommentVO, ArticleVO) 中嵌套展示用户基本信息的简化 VO
 */
@Data
public class UserSimpleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应 User Model 的 id
     */
    private Long id;

    /**
     * 对应 User Model 的 nick_name
     */
    private String nickName;

    /**
     * 对应 User Model 的 avatar
     */
    private String avatar;
}