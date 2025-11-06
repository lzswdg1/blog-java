package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.links.Links;
import com.zw.zw_blog.model.dto.links.LinkCreateDTO;
import com.zw.zw_blog.model.dto.links.LinkQueryDTO;
import com.zw.zw_blog.model.dto.links.LinkUpdateDTO;
import com.zw.zw_blog.model.vo.links.LinkVO;

import java.util.List;

public interface LinkService extends IService<Links> {

    /**
     * 添加友链 (用户申请或管理员添加)
     * (对应 addLink)
     */
    Links addLink(LinkCreateDTO createDTO);

    /**
     * 更新友链信息 (管理员操作)
     * (对应 updateLink)
     */
    boolean updateLink(LinkUpdateDTO updateDTO);

    /**
     * 删除友链 (管理员操作)
     * (对应 deleteLink)
     */
    boolean removeLinks(Long id,Integer type);

    /**
     * 分页获取友链列表 (后台管理)
     * (对应 getLinksList)
     */
    IPage<LinkVO> getAdminLinksList(LinkQueryDTO queryDTO);

    /**
     * 获取已通过审核的友链列表 (前台展示)
     * (对应 getHomeLinksList)
     */
    List<LinkVO> getHomeLinksList();
}