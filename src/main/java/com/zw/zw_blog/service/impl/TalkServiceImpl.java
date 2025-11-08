package com.zw.zw_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.exception.BusinessException;
import com.zw.zw_blog.mapper.TagMapper;
import com.zw.zw_blog.mapper.TalkMapper;
import com.zw.zw_blog.mapper.TalkPhotoMapper;
import com.zw.zw_blog.mapper.UserMapper;
import com.zw.zw_blog.model.bean.talk.Talk;
import com.zw.zw_blog.model.bean.talk.TalkPhoto;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.talk.TalkCreateDTO;
import com.zw.zw_blog.model.dto.talk.TalkQueryDTO;
import com.zw.zw_blog.model.dto.talk.TalkUpdateDTO;
import com.zw.zw_blog.model.vo.talk.TalkVO;
import com.zw.zw_blog.model.vo.user.UserSimpleVO;
import com.zw.zw_blog.service.LikeService;
import com.zw.zw_blog.service.TalkService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {

     @Resource
     private TalkMapper talkMapper;

     @Resource
     private TalkPhotoMapper talkPhotoMapper;

     @Resource
    private UserMapper userMapper;

     @Resource
    @Lazy
    private LikeService likeService;


     private static final Integer LIKE_TYPE_TALK = 2;


     @Override
    @Transactional(rollbackFor = Exception.class)
    public Talk publishTalk(TalkCreateDTO createDTO,Long userId) {
         Talk talk = new Talk();
         BeanUtils.copyProperties(createDTO,talk);
         talk.setUserId(userId.intValue());
         if(talk.getStatus() ==null){
             talk.setStatus(1);
         }

         this.save(talk);

         if(!CollectionUtils.isEmpty(createDTO.getImages())){
             List<TalkPhoto> photoList = createDTO.getImages().stream()
                     .map(url ->{
                         TalkPhoto photo = new TalkPhoto();
                         photo.setTalkId(talk.getId().intValue());
                         photo.setUrl(url);
                         return photo;
                     }).collect(Collectors.toList());
             photoList.forEach(talkPhotoMapper::insert);
             return talk;
         }
     }
     @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTalk(TalkUpdateDTO updateDTO){
         Talk talk = this.getById(updateDTO.getId());
         if(talk == null){
             throw new BusinessException(ResultCode.DATA_NOT_EXIST);
         }

         BeanUtils.copyProperties(updateDTO,talk);

         this.updateById(talk);

         List<String> newImages = updateDTO.getImages() != null ? updateDTO.getImages() : Collections.emptyList();
         List<TalkPhoto> oldPhotos = talkPhotoMapper.selectList(new LambdaQueryWrapper<TalkPhoto>().eq(TalkPhoto::getTalkId, talk.getId()));
         Set<String> oldUrls = oldPhotos.stream().map(TalkPhoto::getUrl).collect(Collectors.toSet());
         List<Long> toDeleteIds = oldPhotos.stream()
                 .filter(p -> !newImages.contains(p.getUrl()))
                 .map(TalkPhoto::getId).collect(Collectors.toList());
         List<TalkPhoto> toAddPhotos = newImages.stream()
                 .filter(url ->!oldUrls.contains(url))
                 .map(url ->{
                     TalkPhoto photo = new TalkPhoto();
                     photo.setTalkId(talk.getId().intValue());
                     photo.setUrl(url);
                     return photo;
                 }).collect(Collectors.toList());

         if(!toAddPhotos.isEmpty()){
             talkPhotoMapper.deleteBatchIds(toDeleteIds);
         }
         toAddPhotos.forEach(talkPhotoMapper::insert);
         return true;
     }

     @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTalk(Long id,Integer status){
         if(status==1||status==2){
             return this.update(new LambdaQueryWrapper<Talk>()
                     .eq(Talk::getId,id)
                     .set(Talk::getStatus,3));
         }else{
             talkPhotoMapper.deleteById(new LambdaQueryWrapper<TalkPhoto>().eq(TalkPhoto::getTalkId,id));
             return this.removeById(id);
         }
     }

     @Override
    public boolean togglePublic (Long id,Integer status){
         return this.update(new LambdaQueryWrapper<Talk>()
                 .eq(Talk::getId,id)
                 .set(Talk::getStatus,status));
    }
    @Override
    public boolean revertTalk(Long id){
         return this.update(new LambdaQueryWrapper<Talk>()
                 .eq(Talk::getId,id)
                 .set(Talk::getStatus,1));
    }

    @Override
    public boolean talkLike(Long id){
         return this.update(new LambdaQueryWrapper<Talk>()
                 .eq(Talk::getId,id)
                 .setSql("like_times = like_times + 1"));
    }

    @Override
    public boolean cancelTalkLike(Long id){
         return this.update(new LambdaQueryWrapper<Talk>()
                 .eq(Talk::getId,id)
                 .setSql("like_times = like_times - 1")
         )
    }

    @Override
    public boolean toggleTop(Long id,Integer isTop){
         return this.update(new LambdaQueryWrapper<Talk>()
                 .eq(Talk::getId,id)
                 .set(Talk::getIsTop,isTop));
    }

    @Override
    public IPage<TalkVO> getAdminTalkList(TalkQueryDTO talkQueryDTO){
        Page<Talk> page =new Page<>(talkQueryDTO.getCurrent(),talkQueryDTO.getSize());
        LambdaQueryWrapper<Talk> wrapper = new LambdaQueryWrapper<>();
        if(talkQueryDTO.getStatus()!=null){
            wrapper.eq(Talk::getStatus,talkQueryDTO.getStatus());
        }
        wrapper.orderByAsc(Talk::getIsTop).orderByDesc(Talk::getCreatedAt);
        IPage<Talk> talkPage = talkMapper.selectPage(page, wrapper);
        return convertToTalkVO(talkPage,null);
    }

    @Override
    public IPage<TalkVO> getHomeTalkList(long current,long size,Long useId){
         Page<Talk> page = new Page<>(current,size);
         LambdaQueryWrapper<Talk> wrapper = new LambdaQueryWrapper<>();
         wrapper.eq(Talk::getStatus,1);
         wrapper.orderByAsc(Talk::getIsTop).orderByDesc(Talk::getCreatedAt);
         IPage<Talk> talkPage = talkMapper.selectPage(page, wrapper);

         return convertToTalkVO(talkPage,useId);
    }

    @Override
    public TalkVO getTalkById(Long id,Long useId){
         Talk talk = this.getById(id);
         if(talk == null){
             throw new BusinessException(ResultCode.DATA_NOT_EXIST);
         }
         Page<Talk> page = new Page<>();
         page.setRecords(Collections.singletonList(talk));
         return convertToTalkVO(page,useId).getRecords().get(0);
    }

    private IPage<TalkVO> convertToTalkVO(IPage<Talk> talkPage,Long currentUserId){
         List<Talk> talks = talkPage.getRecords();
         if(CollectionUtils.isEmpty(talks)){
             return  new Page<>(currentUserId,talkPage.getSize(),0);
         }

         List<Integer>  talkIds = talks.stream()
                 .map(t ->t.getId().intValue()).collect(Collectors.toList());
         List<Long> talkLongIds = talks.stream().map(Talk::getId).collect(Collectors.toList());

         Set<Integer> userIds = talks.stream().map(Talk::getUserId).collect(Collectors.toSet());


         List<TalkPhoto> allPhotos = talkPhotoMapper.selectList(
                 new LambdaQueryWrapper<TalkPhoto>().in(TalkPhoto::getTalkId,talkIds)
         );

        Map<Integer,List<String>> photoMap =  allPhotos.stream()
                .collect(Collectors.groupingBy(TalkPhoto::getTalkId,
                        Collectors.mapping(TalkPhoto::getUrl, Collectors.toList())));


        Map<Long, UserSimpleVO> userMap = new HashMap<>();
        if(!userIds.isEmpty()){
            List<User> users = userMapper.selectBatchIds(userIds);
            for(User user : users){
                UserSimpleVO vo = new UserSimpleVO();`
                BeanUtils.copyProperties(user,vo);
                userMap.put(user.getId(),vo);
            }
        }

        List<TalkVO> voList = talks.stream()
                .map(talk ->{
                    TalkVO vo = new TalkVO();
                    BeanUtils.copyProperties(talk,vo);
                    vo.setImages(photoMap.getOrDefault(talk.getId().intValue(),Collections.emptyList()));
                    vo.setUser(userMap.get(talk.getUserId().longValue()));
                    vo.setLikeCount(talk.getLikeTimes());
                    return vo;
                }).collect(Collectors.toList());

        IPage<TalkVO> voPage = new Page<>(talkPage.getCurrent(),talkPage.getSize(),talkPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }
}
