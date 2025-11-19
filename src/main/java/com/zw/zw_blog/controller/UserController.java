package com.zw.zw_blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zw.zw_blog.common.Result;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.user.*;
import com.zw.zw_blog.model.vo.user.LoginResponseVO;
import com.zw.zw_blog.model.vo.user.UserInfoVO;
import com.zw.zw_blog.service.UserService;
import com.zw.zw_blog.util.JwtTokenUtil;
import com.zw.zw_blog.util.ToolUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${app.admin-password")
    private String ADMIN_PASSWORD;

    @PostMapping("/register")
    public Result<?>  register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO, HttpServletRequest httpServletRequest){
           User user = userService.register(registerRequestDTO);
           String ip =ToolUtils.getRequestIp(httpServletRequest);
           userService.updateIp(user.getId(), ip);
           return Result.success("用户注册成功", Map.of("id",user.getId(),"username",user.getUsername()));
    }


    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        String ip=ToolUtils.getRequestIp(request);
        String ipAddress = ToolUtils.getIpAddress(ip);
        // 1. admin 特殊登录逻辑
        if ("admin".equals(username)) {
            if (!StringUtils.hasText(ADMIN_PASSWORD)) {
                throw new BusinessException(ResultCode.CONFIG_ERROR.getCode(), "请在env配置文件里添加超级管理员密码");
            }
            if (ADMIN_PASSWORD.equals(password)) {
                User adminUser = new User();
                adminUser.setId(5201314L);
                adminUser.setUsername("admin");
                adminUser.setNickName("超级管理员");
                adminUser.setRole(1); // 1 = ADMIN

                String token = jwtTokenUtil.generateToken(adminUser);
                return Result.success("登录成功", new LoginResponseVO(token, "超级管理员", 1, 5201314L, ipAddress));
            } else {
                throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
            }
        } else {
            // 2. 普通用户登录
            User user = userService.getOneUserInfo(username); // 调用您已有的

            // 对应 verifyLogin 中间件
            if (user == null) {
                throw new BusinessException(ResultCode.USER_NOT_EXIST);
            }
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
            }

            userService.updateIp(user.getId(), ip); // 调用您已有的

            String token = jwtTokenUtil.generateToken(user);
            return Result.success("登录成功", new LoginResponseVO(token, user.getUsername(), user.getRole(), user.getId(), ipAddress));
        }
    }

    /**
     * 用户修改个人用户信息
     * 对应: PUT /updateOwnUserInfo
     */
    @PutMapping("/updateOwnUserInfo")
    @PreAuthorize("isAuthenticated()") // 对应 auth
    public Result<?> updateOwnUserInfo(@Valid @RequestBody UserUpdateInfoDTO infoDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 对应: isSuperAdmin 中间件 (禁止 admin 修改)
        if ("admin".equals(currentUser.getUsername())) {
            return Result.error(500, "管理员信息只可通过配置信息修改");
        }

        userService.updateOwnUserInfo(infoDTO, currentUser.getId());
        return Result.success("修改用户成功", null);
    }

    /**
     * 修改密码
     * 对应: PUT /updatePassword
     */
    @PutMapping("/updatePassword")
    @PreAuthorize("isAuthenticated()") // 对应 auth
    public Result<?> updatePassword(@Valid @RequestBody UserUpdatePasswordDTO passwordDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 对应: isSuperAdmin 检查 (admin 密码不能在此修改)
        if ("admin".equals(currentUser.getUsername())) {
            return Result.error(500, "admin密码只可以通过配置文件env修改");
        }

        // 对应: if (id == 2) ... (测试用户)
        if (currentUser.getId() == 2L) {
            return Result.error(500, "测试用户密码不可以修改哦");
        }

        // ServiceImpl.updatePassword 已包含 verifyUpdatePassword 逻辑
        userService.updatePassword(passwordDTO, currentUser.getId());
        return Result.success("修改用户密码成功", null);
    }

    /**
     * 管理员修改用户角色
     * 对应: PUT /updateRole/:id/:role
     */
    @PutMapping("/updateRole/{id}/{role}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 对应 auth 和 needAdminAuth
    public Result<?> updateRole(@PathVariable Long id, @PathVariable Integer role) {
        userService.updateRole(id, role);
        return Result.success("修改角色成功", null);
    }

    /**
     * 管理员修改用户信息
     * 对应: PUT /adminUpdateUserInfo
     */
    @PutMapping("/adminUpdateUserInfo")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 对应 auth 和 needAdminAuth
    public Result<?> adminUpdateUserInfo(@Valid @RequestBody AdminUpdateUser updateDTO) { //
        userService.adminUpdateUserInfo(updateDTO.getId(), updateDTO.getNickName(), updateDTO.getAvatar());
        return Result.success("修改用户信息成功", null);
    }

    /**
     * 分页获取用户列表
     * 对应: POST /getUserList
     */
    @PostMapping("/getUserList")
    @PreAuthorize("isAuthenticated()") // 对应 auth
    public Result<IPage<User>> getUserList(@RequestBody UserQueryDTO queryDTO) { //
        // 注意：您 ServiceImpl 的 getUserList 已经做了密码字段过滤，非常好
        IPage<User> userList = userService.getUserList(queryDTO);
        return Result.success("分页获取用户列表成功", userList);
    }

    /**
     * 根据用户id获取用户信息
     * 对应: GET /getUserInfoById/:id
     */
    @GetMapping("/getUserInfoById/{id}")
    public Result<UserInfoVO> getUserInfoById(@PathVariable Long id) {
        // 对应: if (ctx.params.id == 5201314) ...
        if (id == 5201314L) {
            UserInfoVO adminInfo = new UserInfoVO();
            adminInfo.setId(5201314L);
            adminInfo.setRole(1); // 1 = ADMIN
            adminInfo.setNickName("超级管理员");
            return Result.success("获取用户信息成功", adminInfo);
        }

        User user = userService.getById(id);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        // 对应: const { password, username, ip, ...resInfo } = res;
        UserInfoVO resInfo = new UserInfoVO(); //
        resInfo.setId(user.getId());
        resInfo.setNickName(user.getNickName());
        resInfo.setAvatar(user.getAvatar());
        resInfo.setRole(user.getRole());
        resInfo.setIpAddress(ToolUtils.getIpAddress(user.getIp())); // 对应 getIpAddress(ip)

        return Result.success("获取用户信息成功", resInfo);
    }
}
