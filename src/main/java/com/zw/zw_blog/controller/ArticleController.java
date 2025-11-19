package com.zw.zw_blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zw.zw_blog.common.Result;
import com.zw.zw_blog.common.ResultCode;
import com.zw.zw_blog.model.bean.article.Article;
import com.zw.zw_blog.model.bean.user.User;
import com.zw.zw_blog.model.dto.article.ArticleCreateDTO;
import com.zw.zw_blog.model.dto.article.ArticleQueryDTO;
import com.zw.zw_blog.model.dto.article.ArticleUpdateDTO;
import com.zw.zw_blog.model.vo.article.ArticleDetailVO;
import com.zw.zw_blog.model.vo.article.ArticleSimpleVO;
import com.zw.zw_blog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // --- 公共接口 (Public) ---

    /**
     * (前台) 获取文章分页列表
     * 对应: GET /article/list
     * (调用通用的 getArticleList 方法)
     */
    @GetMapping("/list")
    public Result<IPage<ArticleSimpleVO>> getHomeArticleList(ArticleQueryDTO queryDTO) {
        // 前台默认只查询已发布的 (status=1)
        if (queryDTO.getStatus() == null) {
            queryDTO.setStatus(1);
        }
        IPage<ArticleSimpleVO> page = articleService.getArticleList(queryDTO);
        return Result.success(page);
    }

    /**
     * (前台) 获取文章详情
     * 对应: GET /article/detail/:id
     * (*** 修正: 增加了 password 参数, 对应 ServiceImpl ***)
     */
    @GetMapping("/detail/{id}")
    public Result<ArticleDetailVO> getArticleDetail(@PathVariable Long id,
                                                    @RequestParam(required = false) String password) {
        // 调用 ServiceImpl 中 getArticleDetail(Long id, String pwd)
        ArticleDetailVO detail = articleService.getArticleDetail(id, password);
        return Result.success(detail);
    }


    // --- 管理员接口 (Admin) ---

    /**
     * (后台) 分页获取文章列表
     * 对应: GET /article/admin/list
     * (调用通用的 getArticleList 方法)
     */
    @GetMapping("/admin/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<IPage<ArticleSimpleVO>> getAdminArticleList(ArticleQueryDTO queryDTO) {
        // 后台可以查询所有状态, DTO 中的 status (null, 0, 1) 会被 ServiceImpl 处理
        IPage<ArticleSimpleVO> page = articleService.getArticleList(queryDTO);
        return Result.success(page);
    }

    /**
     * (后台) 创建文章
     * 对应: POST /article/add
     * (*** 修正: 匹配 ServiceImpl 签名 ***)
     */
    @PostMapping("/admin/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<?> addArticle(@Valid @RequestBody ArticleCreateDTO createDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        // 调用 ServiceImpl 中 createArticle(ArticleCreateDTO, Long authorId)
        Article newArticle = articleService.createArticle(createDTO, userId);

        // 返回创建成功的文章对象 (包含新 ID)
        return Result.success("创建成功", newArticle);
    }

    /**
     * (后台) 更新文章
     * 对应: PUT /article/update
     * (*** 修正: 匹配 ServiceImpl 签名 ***)
     */
    @PutMapping("/admin/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<?> updateArticle(@Valid @RequestBody ArticleUpdateDTO updateDTO) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        // 调用 ServiceImpl 中 updateArticle(ArticleUpdateDTO, Long authorId)
        boolean success = articleService.updateArticle(updateDTO, userId);

        if (success) {
            return Result.success("更新成功");
        }
        return Result.error(500,"更新失败");
    }

    /**
     * (后台) 删除文章 (逻辑删除)
     * 对应: DELETE /article/delete/:id
     * (*** 修正: 匹配 ServiceImpl 签名 ***)
     */
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<?> deleteArticle(@PathVariable Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = currentUser.getId();

        // 调用 ServiceImpl 中 removeArticle(Long id, Long authorId)
        boolean success = articleService.removeArticle(id, userId);

        if (success) {
            return Result.success("删除成功");
        }
        return Result.error(505,"删除失败");
    }


    // --- 以下接口在您提供的 ArticleServiceImpl.java 中暂未实现 ---
    // --- (我暂时将它们注释掉) ---

    /*
    @GetMapping("/archives")
    public Result<List<ArticleArchiveVO>> getArticleArchives() {
        // ServiceImpl 中缺少 getArticleArchives()
        return Result.error(501, "接口 'getArticleArchives' 暂未实现");
    }

    @PostMapping("/like")
    @PreAuthorize("isAuthenticated()")
    public Result<?> likeArticle(@Valid @RequestBody LikeDTO likeDTO) {
        // ServiceImpl 中缺少 likeArticle()
        return Result.error(501, "接口 'likeArticle' 暂未实现");
    }

    @PutMapping("/admin/top/{id}/{isTop}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<?> updateArticleTop(@PathVariable Long id, @PathVariable Integer isTop) {
        // ServiceImpl 中缺少 updateArticleTop()
        return Result.error(501, "接口 'updateArticleTop' 暂未实现");
    }

    @PutMapping("/admin/publish/{id}/{status}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Result<?> updateArticlePublish(@PathVariable Long id, @PathVariable Integer status) {
        // ServiceImpl 中缺少 updateArticlePublish()
        return Result.error(501, "接口 'updateArticlePublish' 暂未实现");
    }
    */
}