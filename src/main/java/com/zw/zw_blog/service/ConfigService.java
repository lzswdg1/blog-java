package com.zw.zw_blog.service;



import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.config.Config;
import com.zw.zw_blog.model.dto.config.ConfigUpdateDTO;
import com.zw.zw_blog.model.vo.config.ConfigVO;

// 对应 service/config/index.js
public interface ConfigService extends IService<Config> {
    void addView();
    /**
     * 获取网站配置 (返回解析后的 VO)
     * (对应 getConfig)
     */
    ConfigVO getConfig();

    /**
     * 更新网站配置
     * (对应 updateConfig)
     * @param jsonConfig JSON 字符串
     */
    Config updateConfig(ConfigUpdateDTO jsonConfig);
}