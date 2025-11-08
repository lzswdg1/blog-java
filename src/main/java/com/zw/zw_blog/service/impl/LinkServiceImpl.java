package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.LinksMapper;
import com.zw.zw_blog.model.bean.links.Links;
import com.zw.zw_blog.model.dto.links.LinkCreateDTO;
import com.zw.zw_blog.model.dto.links.LinkQueryDTO;
import com.zw.zw_blog.model.dto.links.LinkUpdateDTO;
import com.zw.zw_blog.model.vo.links.LinkVO;
import com.zw.zw_blog.service.LinkService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LinkServiceImpl extends ServiceImpl<LinksMapper, Links> implements LinkService {
    /**
     * 添加友链
     * 对应接口: Links addLink(LinkCreateDTO createDTO)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Links addLink(LinkCreateDTO createDTO) {
        Links links = new Links();
        BeanUtils.copyProperties(createDTO, links);
        // 如果前端未传入状态，默认为 2 (待审核)
        if (links.getStatus() == null) {
            links.getStatus(); // 这里应该是 links.setStatus(2); 代码补全手误，修正如下
            links.setStatus(2);
        }
        // 执行插入
        this.save(links);
        return links;
    }

    /**
     * 更新友链信息 (包含审核通过等操作，通过 status 字段控制)
     * 对应接口: boolean updateLink(LinkUpdateDTO updateDTO)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLink(LinkUpdateDTO updateDTO) {
        Links links = this.getById(updateDTO.getId());
        if (links == null) {
            throw new BusinessException(ResultCode.DATA_NOT_EXIST);
        }
        BeanUtils.copyProperties(updateDTO, links);
        return this.updateById(links);
    }

    /**
     * 删除友链
     * 对应接口: boolean removeLinks(Long id, Integer type)
     * @param type 删除类型 (预留字段，可参照文章模块实现软/硬删除。目前暂用物理删除)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLinks(Long id, Integer type) {
        // 可以在这里根据 type 判断是逻辑删除还是物理删除
        // 暂时直接使用 MyBatis-Plus 默认删除 (若实体配置了 @TableLogic 则为逻辑删除，否则为物理删除)
        return this.removeById(id);
    }

    /**
     * 分页获取友链列表 (后台管理)
     * 对应接口: IPage<LinkVO> getAdminLinksList(LinkQueryDTO queryDTO)
     */
    @Override
    public IPage<LinkVO> getAdminLinksList(LinkQueryDTO queryDTO) {
        Page<Links> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());

        QueryWrapper<Links> wrapper = new QueryWrapper<>();
        // 网站名称模糊查询
        wrapper.like(StringUtils.hasText(queryDTO.getSiteName()), "site_name", queryDTO.getSiteName());
        // 状态精确查询
        wrapper.eq(queryDTO.getStatus() != null, "status", queryDTO.getStatus());
        // 时间范围查询
        if (queryDTO.getStartTime() != null && queryDTO.getEndTime() != null) {
            wrapper.between("create_time", queryDTO.getStartTime(), queryDTO.getEndTime());
        }
        // 默认按创建时间倒序
        wrapper.orderByDesc("create_time");

        IPage<Links> linkPage = this.page(page, wrapper);

        // 转换 Page<Links> 为 IPage<LinkVO>
        return linkPage.convert(link -> {
            LinkVO vo = new LinkVO();
            BeanUtils.copyProperties(link, vo);
            return vo;
        });
    }

    /**
     * 获取已通过审核的友链列表 (前台展示)
     * 对应接口: List<LinkVO> getHomeLinksList()
     */
    @Override
    public List<LinkVO> getHomeLinksList() {
        QueryWrapper<Links> wrapper = new QueryWrapper<>();
        // 状态 1 为已通过
        wrapper.eq("status", 1);
        // 按排序值升序
        wrapper.orderByAsc("`order`"); // 注意 order 是 MySQL 关键字

        List<Links> list = this.list(wrapper);

        return list.stream().map(link -> {
            LinkVO vo = new LinkVO();
            BeanUtils.copyProperties(link, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
