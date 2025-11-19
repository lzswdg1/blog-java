package com.zw.zw_blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zw.zw_blog.common.Result;
import com.zw.zw_blog.model.dto.article.*;
import com.zw.zw_blog.model.dto.category.*;
import com.zw.zw_blog.model.dto.config.ConfigUpdateDTO;
import com.zw.zw_blog.model.dto.header.PageHeaderUpdateDTO;
import com.zw.zw_blog.model.dto.links.*;
import com.zw.zw_blog.model.dto.page.PageQueryDTO;
import com.zw.zw_blog.model.dto.photo.*;
import com.zw.zw_blog.model.dto.recommend.*;
import com.zw.zw_blog.model.dto.tag.*;
import com.zw.zw_blog.model.dto.talk.*;
import com.zw.zw_blog.model.dto.user.AdminUpdateUser;
import com.zw.zw_blog.model.vo.links.LinkVO;
import com.zw.zw_blog.model.vo.message.MessageVO;
import com.zw.zw_blog.model.vo.statistic.StatisticVO;
import com.zw.zw_blog.service.*; // 导入所有 Service
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员专属控制器
 * 集中处理所有需要 needAdminAuth 的路由
 */
@RestController
@RequestMapping("/admin") // 统一的 /admin 前缀
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // 整个类都需要管理员权限
public class AdminController {

    // 注入您已有的所有相关 Service
    @Autowired private UserService userService;
    @Autowired private ArticleService articleService;
    @Autowired private RecommendService recommendService;
    @Autowired private CategoryService categoryService;
    @Autowired private TagService tagService;
    @Autowired private TalkService talkService;
    @Autowired private LinkService linkService;
    @Autowired private MessageService messageService;
    @Autowired private PhotoAlbumService photoAlbumService;
    @Autowired private PhotoService photoService;
    @Autowired private ConfigService configService;
    @Autowired private PageHeaderService pageHeaderService;
    @Autowired private StatisticService statisticService;

    // --- User Admin Routes (来自 router/user.js) ---

    @PutMapping("/user/updateRole/{id}/{role}")
    public Result<?> updateRole(@PathVariable Long id, @PathVariable Integer role) {
        userService.updateRole(id, role);
        return Result.success("修改角色成功", null);
    }

    @PutMapping("/user/updateInfo")
    public Result<?> adminUpdateUserInfo(@Valid @RequestBody AdminUpdateUser updateDTO) { //
        userService.adminUpdateUserInfo(updateDTO.getId(), updateDTO.getNickName(), updateDTO.getAvatar());
        return Result.success("修改用户信息成功", null);
    }

    // --- Article Admin Routes (来自 router/article.js) ---

    /**
     * 对应 needAdminAuthNotNeedSuper (必须是 admin，但不能是 'admin' 账号)
     */
    @PostMapping("/article/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') and !authentication.name.equals('admin')")
    public Result<?> createArticle(@Valid @RequestBody ArticleCreateDTO articleDTO) { //
        articleService.createArticle(articleDTO);
        return Result.success("发布文章成功", null);
    }

    @PutMapping("/article/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') and !authentication.name.equals('admin')") //
    public Result<?> updateArticle(@Valid @RequestBody ArticleUpdateDTO articleDTO) { //
        articleService.updateArticle(articleDTO);
        return Result.success("修改文章成功", null);
    }

    @DeleteMapping("/article/delete/{id}")
    public Result<?> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success("删除文章成功", null);
    }

    @PostMapping("/article/toggleTop/{id}/{isTop}")
    public Result<?> toggleTop(@PathVariable Long id, @PathVariable Integer isTop) {
        articleService.toggleTop(id, isTop);
        return Result.success("操作成功", null);
    }

    // --- Recommend Admin Routes (来自 router/article.js) ---

    @PostMapping("/recommend/add")
    public Result<?> addRecommend(@Valid @RequestBody RecommendCreateDTO recommendDTO) { //
        recommendService.addRecommend(recommendDTO);
        return Result.success("添加推荐成功", null);
    }

    @PutMapping("/recommend/update")
    public Result<?> updateRecommend(@Valid @RequestBody RecommendUpdateDTO recommendDTO) { //
        recommendService.updateRecommend(recommendDTO);
        return Result.success("修改推荐成功", null);
    }

    @DeleteMapping("/recommend/delete/{id}")
    public Result<?> deleteRecommend(@PathVariable Long id) {
        recommendService.deleteRecommend(id);
        return Result.success("删除推荐成功", null);
    }

    // --- Category Admin Routes (来自 router/category.js) ---

    @PostMapping("/category/create")
    public Result<?> createCategory(@Valid @RequestBody CategoryCreateDTO categoryDTO) { //
        categoryService.createCategory(categoryDTO);
        return Result.success("创建分类成功", null);
    }

    @DeleteMapping("/category/delete/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除分类成功", null);
    }

    @PutMapping("/category/update")
    public Result<?> updateCategory(@Valid @RequestBody CategoryUpdateDTO categoryDTO) { //
        categoryService.updateCategory(categoryDTO);
        return Result.success("修改分类成功", null);
    }

    // --- Tag Admin Routes (来自 router/tag.js) ---

    @PostMapping("/tag/create")
    public Result<?> createTag(@Valid @RequestBody TagCreateDTO tagDTO) { //
        tagService.createTag(tagDTO);
        return Result.success("创建标签成功", null);
    }

    @DeleteMapping("/tag/delete/{id}")
    public Result<?> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success("删除标签成功", null);
    }

    @PutMapping("/tag/update")
    public Result<?> updateTag(@Valid @RequestBody TagUpdateDTO tagDTO) { //
        tagService.updateTag(tagDTO);
        return Result.success("修改标签成功", null);
    }

    // --- Talk Admin Routes (来自 router/talk.js) ---

    @PostMapping("/talk/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') and !authentication.name.equals('admin')") //
    public Result<?> createTalk(@Valid @RequestBody TalkCreateDTO talkDTO) { //
        talkService.createTalk(talkDTO);
        return Result.success("发布说说成功", null);
    }

    @PutMapping("/talk/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') and !authentication.name.equals('admin')") //
    public Result<?> updateTalk(@Valid @RequestBody TalkUpdateDTO talkDTO) { //
        talkService.updateTalk(talkDTO);
        return Result.success("修改说说成功", null);
    }

    @DeleteMapping("/talk/delete/{id}")
    public Result<?> deleteTalk(@PathVariable Long id) {
        talkService.deleteTalk(id);
        return Result.success("删除说说成功", null);
    }

    @PutMapping("/talk/updateStatus")
    public Result<?> updateTalkStatus(@Valid @RequestBody TalkUpdateStatusDTO statusDTO) { //
        talkService.updateTalkStatus(statusDTO);
        return Result.success("修改说说状态成功", null);
    }

    // --- Links Admin Routes (来自 router/links.js) ---

    @PostMapping("/links/create")
    public Result<?> createLink(@Valid @RequestBody LinkCreateDTO linkDTO) { //
        linkService.createLink(linkDTO);
        return Result.success("创建友链成功", null);
    }

    @PutMapping("/links/update")
    public Result<?> updateLink(@Valid @RequestBody LinkUpdateDTO linkDTO) { //
        linkService.updateLink(linkDTO);
        return Result.success("修改友链成功", null);
    }

    @DeleteMapping("/links/delete/{id}")
    public Result<?> deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
        return Result.success("删除友链成功", null);
    }

    @PostMapping("/links/getList")
    public Result<IPage<LinkVO>> getLinkList(@RequestBody LinkQueryDTO queryDTO) { //
        IPage<LinkVO> list = linkService.adminGetList(queryDTO);
        return Result.success("获取友链列表成功", list);
    }

    // --- Message Admin Routes (来自 router/message.js) ---

    @GetMapping("/message/getList")
    public Result<IPage<MessageVO>> getMessageList(@Valid PageQueryDTO queryDTO) { //
        IPage<MessageVO> list = messageService.adminGetList(queryDTO);
        return Result.success("获取留言列表成功", list);
    }

    // --- PhotoAlbum Admin Routes (来自 router/photoAlbum.js) ---

    @PostMapping("/photoAlbum/create")
    public Result<?> createPhotoAlbum(@Valid @RequestBody PhotoAlbumCreateDTO albumDTO) { //
        photoAlbumService.createAlbum(albumDTO);
        return Result.success("创建相册成功", null);
    }

    @PutMapping("/photoAlbum/update")
    public Result<?> updatePhotoAlbum(@Valid @RequestBody PhotoAlbumUpdateDTO albumDTO) { //
        photoAlbumService.updateAlbum(albumDTO);
        return Result.success("修改相册成功", null);
    }

    @DeleteMapping("/photoAlbum/delete/{id}")
    public Result<?> deletePhotoAlbum(@PathVariable Long id) {
        photoAlbumService.deleteAlbum(id);
        return Result.success("删除相册成功", null);
    }

    // --- Photo Admin Routes (来自 router/photo.js) ---

    @PostMapping("/photo/add")
    public Result<?> addPhoto(@Valid @RequestBody PhotoAddDTO photoDTO) { //
        photoService.addPhoto(photoDTO);
        return Result.success("添加照片成功", null);
    }

    @DeleteMapping("/photo/delete") // Node.js 用的是 :id，但您的 Service 是 List<Long>
    public Result<?> deletePhoto(@RequestBody List<Long> ids) {
        photoService.deletePhoto(ids);
        return Result.success("删除照片成功", null);
    }

    // --- Config Admin Routes (来自 router/config.js) ---

    @PutMapping("/config/update")
    public Result<?> updateConfig(@Valid @RequestBody ConfigUpdateDTO configDTO) { //
        configService.updateConfig(configDTO);
        return Result.success("修改配置成功", null);
    }

    // --- PageHeader Admin Routes (来自 router/pageHeader.js) ---

    @PutMapping("/pageHeader/update")
    public Result<?> updatePageHeader(@Valid @RequestBody PageHeaderUpdateDTO headerDTO) { //
        pageHeaderService.updatePageHeaderAdmin(headerDTO);
        return Result.success("修改页头成功", null);
    }

    // --- Statistic Admin Routes (来自 router/statistic.js) ---

    @GetMapping("/statistic/getStatistic")
    public Result<StatisticVO> getStatistic() {
        // 注意：您的 StatisticService 接口是空的，您需要先实现 getStatistic 方法
        StatisticVO statistic = statisticService.getStatistics();
        return Result.success("获取统计数据成功", statistic);
    }
}