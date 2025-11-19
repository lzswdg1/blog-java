package com.zw.zw_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 告诉 Spring 这是一个配置类
public class SecurityConfig {

    /**
     * 定义 PasswordEncoder 的 Bean
     * Spring 会自动寻找这个 Bean，并将其注入到你需要的地方 (如 UserController)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 返回 BCryptPasswordEncoder (目前最推荐的加密算法)
        return new BCryptPasswordEncoder();
    }

    // 注意：你将来还需要在这里配置 SecurityFilterChain 来放行
    // /user/login, /user/register 等接口，但目前这已能解决你的 Bean 缺失问题
}
