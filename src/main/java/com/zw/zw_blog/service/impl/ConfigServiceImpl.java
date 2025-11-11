package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode; // [架构师重构] 导入 ResultCode
import com.zw.zw_blog.exception.BusinessException; // [架构师重构] 导入 BusinessException
import com.zw.zw_blog.mapper.ConfigMapper;
import com.zw.zw_blog.model.bean.config.Config;
import com.zw.zw_blog.model.dto.config.ConfigUpdateDTO;
import com.zw.zw_blog.model.vo.config.ConfigVO;
import com.zw.zw_blog.service.ConfigService;
import com.zw.zw_blog.service.UploadService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime; // [架构师重构] 导入时间类
import java.util.List;

@Service
@Slf4j
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {


    private static final String CONFIG_CACHE_VALUE = "config";
    private static final String CONFIG_CACHE_KEY = "'config:singleton'";

    @Resource
    private ConfigMapper configMapper;

    @Resource
    @Lazy
    private UploadService uploadService;

    /**
     * 获取配置信息
     * 高频读取，必须使用缓存
     */
    @Override
    // [架构师重构] @Cacheable:
    // 第一次调用此方法时，执行方法体从数据库查询，并将结果(ConfigVO)放入缓存。
    // 后续所有调用，直接从缓存返回，不再执行方法体，极大提升性能。
    @Cacheable(value = CONFIG_CACHE_VALUE, key = CONFIG_CACHE_KEY)
    public ConfigVO getConfig() {
        Config config = getInternalConfig();

        // [架构师重构] 增加健壮性检查
        // 不应返回 null，而应抛出异常，否则上游调用者会空指针
        if (config == null) {
            log.error("博客配置未初始化，请在数据库 `config` 表中添加数据");
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        ConfigVO vo = new ConfigVO();
        BeanUtils.copyProperties(config, vo);
        return vo;
    }

    /**
     * 更新配置信息
     * 低频写入，更新时必须清空缓存
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    // [架构师重构] @CacheEvict:
    // 当此方法成功执行后，Spring 会自动清除缓存中指定 Key 的数据。
    // 确保下一次 getConfig() 会从数据库拉取最新信息。
    @CacheEvict(value = CONFIG_CACHE_VALUE, key = CONFIG_CACHE_KEY)
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
            currentConfig.setUpdatedAt(LocalDateTime.now()); // [架构师重构] 补充更新时间
            this.updateById(currentConfig);
            return currentConfig;
        } else {
            // 创建 (Create)
            Config newConfig = new Config();
            BeanUtils.copyProperties(updateDTO, newConfig);
            newConfig.setCreatedAt(LocalDateTime.now()); // [架构师重构] 补充创建时间
            newConfig.setUpdatedAt(LocalDateTime.now()); // [架构师重构] 补充更新时间
            // 确保 view_times 默认为 0
            if (newConfig.getViewTimes() == null) {
                newConfig.setViewTimes(0L);
            }
            this.save(newConfig);
            return newConfig;
        }
    }

    /**
     * 增加网站访问量
     * 对应 Node: addView
     * [架构师重构] 注意：此方法不应清除缓存。
     * 访问量 (view_times) 是动态数据，而配置(ConfigVO)是静态数据。
     * 每次访问都清空缓存会导致性能雪崩。
     * 允许缓存中的 view_times 略微延迟（直到下次 updateConfig），
     * 是为了保证核心配置（如头像、标题）的高速读取。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addView() {
        Config config = getInternalConfig();
        if (config != null) {
            // 使用 LambdaUpdateWrapper 实现原子自增，防止并发问题
            this.update(new LambdaUpdateWrapper<Config>()
                    .eq(Config::getId, config.getId())
                    .setSql("view_times = view_times + 1"));
        } else {
            // 对应 Node.js 中的 "需要初始化"
            log.warn("addView 失败：网站配置尚未初始化。");
            // [架构师重构] 保持与 getConfig 一致的异常抛出
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
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
     */
    private void deleteOldFileIfChanged(String newUrl, String oldUrl) {
        // 必须同时有值，且它们不相等
        if (StringUtils.hasText(oldUrl) && !oldUrl.equals(newUrl)) {
            // [架构师重构] 优化逻辑：无论新 URL 是否为空，只要旧 URL 存在且与新 URL 不同，就该删除
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
            // [架构师重构] 增加对 URL 参数的兼容
            // 例如: https://.../image.png?auth=xxx
            int queryParamIndex = url.indexOf('?');
            if (queryParamIndex != -1) {
                url = url.substring(0, queryParamIndex);
            }

            int lastSlash = url.lastIndexOf('/');
            if (lastSlash != -1 && lastSlash < url.length() - 1) {
                return url.substring(lastSlash + 1);
            } else {
                // 兼容没有 / 的情况（虽然可能性很低）
                return url;
            }
        } catch (Exception e) {
            log.error("从 URL 提取 Key 失败: {}", url, e);
        }
        return null;
    }
}