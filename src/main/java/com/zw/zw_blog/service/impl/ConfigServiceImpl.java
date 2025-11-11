package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.ConfigMapper;
import com.zw.zw_blog.model.bean.config.Config;
import com.zw.zw_blog.model.dto.config.ConfigUpdateDTO;
import com.zw.zw_blog.model.vo.config.ConfigVO;
import com.zw.zw_blog.service.ConfigService;
import com.zw.zw_blog.service.UploadService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Resource
    private ConfigMapper configMapper;

    @Resource
    @Lazy
    private UploadService uploadService;

    @Override
    public ConfigVO getConfig(){
        Config config = getInternalConfig();
        if(config == null){
            return null;
        }
        ConfigVO vo = new ConfigVO();
        BeanUtils.copyProperties(config,vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Config updateConfig(ConfigUpdateDTO updateDTO) {
        Config currentConfig = getInternalConfig();

        // 1. (复刻 Node.js 逻辑) 如果配置已存在，检查并删除被替换的云端图片
        if (currentConfig != null) {
            deleteOldFileIfChanged(updateDTO.getAvatarBg(), currentConfig.getAvatarBg());
            deleteOldFileIfChanged(updateDTO.getBlogAvatar(), currentConfig.getBlogAvatar());
            deleteOldFileIfChanged(updateDTO.getQqLink(), currentConfig.getQqLink());
            deleteOldFileIfChanged(updateDTO.getWeChatLink(), currentConfig.getWeChatLink());
            deleteOldFileIfChanged(updateDTO.getWeChatGroup(), currentConfig.getWeChatGroup());
            deleteOldFileIfChanged(updateDTO.getQqGroup(), currentConfig.getQqGroup());
            deleteOldFileIfChanged(updateDTO.getWeChatPay(), currentConfig.getWeChatPay());
            deleteOldFileIfChanged(updateDTO.getAliPay(), currentConfig.getAliPay());
        }

        // 2. 执行 "Upsert" (更新或创建)
        if (currentConfig != null) {
            // 更新 (Update)
            BeanUtils.copyProperties(updateDTO, currentConfig);
            currentConfig.setUpdatedAt(LocalDateTime.now());
            this.updateById(currentConfig);
            return currentConfig;
        } else {
            // 创建 (Create)
            Config newConfig = new Config();
            BeanUtils.copyProperties(updateDTO, newConfig);
            newConfig.setCreatedAt(LocalDateTime.now());
            newConfig.setUpdatedAt(LocalDateTime.now());
            // 确保 view_times 默认为 0
            if (newConfig.getViewTimes() == null) {
                newConfig.setViewTimes(0);
            }
            this.save(newConfig);
            return newConfig;
        }
    }

    /**
     * 增加网站访问量
     * 对应 Node: addView
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addView() {
        Config config = getInternalConfig();
        if (config != null) {
            // 使用 LambdaUpdateWrapper 实现原子自增
            this.update(new LambdaUpdateWrapper<Config>()
                    .eq(Config::getId, config.getId())
                    .setSql("view_times = view_times + 1"));
        } else {
            // 对应 Node.js 中的 "需要初始化"
            log.warn("addView 失败：网站配置尚未初始化。");
            throw new BusinessException("网站配置不存在，无法增加访问量");
        }
    }

    /**
     * 内部辅助方法：获取唯一的配置实体 (Config)
     * Node.js 中使用 findOne()
     */
    private Config getInternalConfig() {
        // 配置表通常只有一行数据
        List<Config> list = this.list(new LambdaQueryWrapper<Config>().last("LIMIT 1"));
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 内部辅助方法：如果 URL 发生变化，则删除旧的云端文件
     *
     */
    private void deleteOldFileIfChanged(String newUrl, String oldUrl) {
        // 必须同时有新旧 URL，且它们不相等
        if (StringUtils.hasText(newUrl) && StringUtils.hasText(oldUrl) && !newUrl.equals(oldUrl)) {
            String key = extractKeyFromUrl(oldUrl);
            if (key != null) {
                try {
                    // 调用 UploadService 删除文件
                    uploadService.deleteFile(key);
                } catch (Exception e) {
                    // 即使删除失败，也不应阻塞配置更新，记录日志即可
                    log.error("删除旧的云端文件失败: {}", key, e);
                }
            }
        }
    }

    /**
     * 内部辅助方法：从 URL 中提取文件名 (Key)
     * 对应 Node.js: url.split("/").pop()
     */
    private String extractKeyFromUrl(String url) {
        if (!StringUtils.hasText(url)) {
            return null;
        }
        try {
            int lastSlash = url.lastIndexOf('/');
            if (lastSlash != -1 && lastSlash < url.length() - 1) {
                return url.substring(lastSlash + 1);
            }
        } catch (Exception e) {
            log.error("从 URL 提取 Key 失败: {}", url, e);
        }
        return null;
    }
}
