package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.HeaderMapper;
import com.zw.zw_blog.model.bean.header.Header;
import com.zw.zw_blog.model.dto.header.PageHeaderUpdateDTO;
import com.zw.zw_blog.service.PageHeaderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageHeaderServiceImpl extends ServiceImpl<HeaderMapper, Header> implements PageHeaderService {

    /**
     * (逻辑 A) 更新页面头部背景 (封面)
     */
    @Override
    public boolean updatePageHeaderCover(String routeName, String bgUrl) {
        LambdaUpdateWrapper<Header> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Header::getRouteName, routeName)
                .set(Header::getBgUrl, bgUrl);
        return this.update(updateWrapper);
    }

    /**
     * 获取所有页面头部背景
     */
    @Override
    public List<Header> getAllPageHeaders() {
        LambdaQueryWrapper<Header> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Header::getSort);
        return this.list(queryWrapper);
    }

    /**
     * (逻辑 B) (新) 更新页眉 (后台管理)
     */
    @Override
    public boolean updatePageHeaderAdmin(PageHeaderUpdateDTO headerDTO) {
        // ... (省略 ID 校验和转换) ...
        Long id;
        try {
            id = Long.parseLong(headerDTO.getId());
        } catch (NumberFormatException e) {
            throw new BusinessException(ResultCode.PARAM_TYPE_ERROR);
        }

        Header header = this.getById(id);
        if (header == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }

        BeanUtils.copyProperties(headerDTO, header, "cover", "sort");

        header.setBgUrl(headerDTO.getCover());
        try {
            header.setSort(Integer.parseInt(headerDTO.getSort()));
        } catch (NumberFormatException e) {
            throw new BusinessException(ResultCode.PARAM_TYPE_ERROR);
        }
        return this.updateById(header);
    }
}