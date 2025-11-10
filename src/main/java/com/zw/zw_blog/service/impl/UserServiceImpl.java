package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.UserMapper;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.user.RegisterRequestDTO;
import com.zw.zw_blog.model.dto.user.UserQueryDTO;
import com.zw.zw_blog.model.dto.user.UserUpdateInfoDTO;
import com.zw.zw_blog.model.dto.user.UserUpdatePasswordDTO;
import com.zw.zw_blog.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;


    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 默认角色ID (3 = 普通用户)
     * 对应 Node.js: role: 3
     */
    private static final int DEFAULT_USER_ROLE = 3;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterRequestDTO registerRequestDTO){
        if(this.getOneUserInfo(registerRequestDTO.getUsername()) != null){
            throw new BusinessException(ResultCode.USER_ALREADY_EXIST);
        }
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRole(DEFAULT_USER_ROLE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        this.save(user);
        return user;
    }



    @Override
    public User getOneUserInfo(String username){
        return this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername,username));
    }
    @Override
    public IPage<User> getUserList(UserQueryDTO userQueryDTO) {
              Page<User> page = new Page<>(userQueryDTO.getCurrent(), userQueryDTO.getSize());
              LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
              queryWrapper.like(StringUtils.hasText(userQueryDTO.getUsername()),User::getUsername,userQueryDTO.getUsername());

              queryWrapper.eq(userQueryDTO.getRole()!=null,User::getRole,userQueryDTO.getRole());
//              queryWrapper.select(User.class,info->getColumn().equals("password"));
                queryWrapper.select(
                        User::getId,
                        User::getUsername,
                        User::getNickName,
                        User::getAvatar,
                        User::getRole,
                        User::getIp,
                        User::getCreatedAt,
                        User::getUpdatedAt
                );
              return  this.page(page,queryWrapper);
    }
     @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserUpdatePasswordDTO passwordDTO,Long userId){
        User user = this.getById(userId);
        if(user==null){
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        if(!passwordEncoder.matches(passwordDTO.getOldPassword(),user.getPassword())){
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        this.updateById(user);
     }


     @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOwnUserInfo(UserUpdateInfoDTO infoDTO,Long userId){
        User user = this.getById(userId);
        if(user==null){
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        user.setNickName(infoDTO.getNickname());
        user.setAvatar(infoDTO.getAvatar());
        user.setUpdatedAt(LocalDateTime.now());

        return this.updateById(user);
     }


     @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Long userId,Integer role){
        return this.update(new LambdaUpdateWrapper<User>()
        .eq(User::getId,userId)
                .set(User::getRole,role)
                .set(User::getUpdatedAt,LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adminUpdateUserInfo(Long id,String nickName,String avatar){
        return this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId,id)
                .set(User::getNickName,nickName)
                .set(User::getAvatar,avatar)
                .set(User::getUpdatedAt,LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIp(Long userId,String ip){
        this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId,userId)
                .set(User::getIp,ip)
                .set(User::getUpdatedAt,LocalDateTime.now()));
    }
}
