package com.zw.zw_blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zw.zw_blog.model.bean.talk.Talk;
import com.zw.zw_blog.model.dto.talk.TalkCreateDTO;
import com.zw.zw_blog.model.dto.talk.TalkQueryDTO;
import com.zw.zw_blog.model.dto.talk.TalkUpdateDTO;
import com.zw.zw_blog.model.vo.talk.TalkVO;

public interface TalkService extends IService<Talk> {
    /**
     * 发布说说
     * (对应 publishTalk)
     */
    Talk publishTalk(TalkCreateDTO createDTO, Long userId);

    /**
     * 修改说说
     * (对应 updateTalk)
     */
    boolean updateTalk(TalkUpdateDTO updateDTO);

    /**
     * 删除说说
     * (对应 deleteTalkById)
     * @param id 说说ID
     * @param status 当前说说状态 (用于判断是软删除还是物理删除)
     */
    boolean deleteTalk(Long id, Integer status);

    /**
     * 切换说说公开性
     * (对应 togglePublic)
     */
    boolean togglePublic(Long id, Integer status);

    /**
     * 恢复说说
     * (对应 revertTalk)
     */
    boolean revertTalk(Long id);

    /**
     * 切换置顶状态
     * (对应 toggleTop)
     */
    boolean toggleTop(Long id, Integer isTop);

    /**
     * 说说点赞
     * (对应 talkLike)
     */
    boolean talkLike(Long id);

    /**
     * 取消说说点赞
     * (对应 cancelTalkLike)
     */
    boolean cancelTalkLike(Long id);

    /**
     * 后台分页获取说说列表
     * (对应 getTalkList)
     */
    IPage<TalkVO> getAdminTalkList(TalkQueryDTO queryDTO);

    /**
     * 前台分页获取说说列表
     * (对应 blogGetTalkList)
     * @param userId 当前登录用户ID (可选，用于判断是否已点赞)
     */
    IPage<TalkVO> getHomeTalkList(long current, long size, Long userId);

    /**
     * 根据ID获取说说详情
     * (对应 getTalkById)
     * @param userId 当前登录用户ID (可选)
     */
    TalkVO getTalkById(Long id, Long userId);
}
