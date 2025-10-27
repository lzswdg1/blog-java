package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.user.RegisterRequestDTO;
import com.zw.zw_blog.model.dto.user.UserQueryDTO;
import com.zw.zw_blog.model.dto.user.UserUpdateInfoDTO;
import com.zw.zw_blog.model.dto.user.UserUpdatePasswordDTO;

public interface UserService extends IService<User> {

    User register(RegisterRequestDTO registerDTO);

    User getOneUserInfo(String username);

    IPage<User> getUserList(UserQueryDTO queryDTO);

    void updatePassword(UserUpdatePasswordDTO passwordDTO, Long userId);

    boolean updateOwnUserInfo(UserUpdateInfoDTO infoDTO, Long userId);

    boolean updateRole(Long id, Integer role);

    boolean adminUpdateUserInfo(Long id, String nickName, String avatar);

    void updateIp(Long userId, String ip);
}