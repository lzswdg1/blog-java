package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.mapper.LinksMapper;
import com.zw.zw_blog.model.bean.links.Links;
import com.zw.zw_blog.model.dto.links.LinkQueryDTO;
import com.zw.zw_blog.model.vo.links.LinkVO;
import com.zw.zw_blog.service.LinkService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkServiceImpl extends ServiceImpl<LinksMapper, Links> implements LinkService {

    @Resource
    private LinksMapper linksMapper;

    @Override
    public IPage<LinkVO> getLinkList(LinkQueryDTO linkQueryDTO){

    }
}
