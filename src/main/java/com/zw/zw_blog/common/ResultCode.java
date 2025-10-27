package com.zw.zw_blog.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    // === 通用 ===
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"), // 通用失败
    PARAM_VALIDATE_FAILED(400, "参数校验失败"), // HTTP 400
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"), // HTTP 500

    // === 认证与授权 (1xxxx) ===
    // 对应 ERRORCODE.AUTH = "10001"
    NO_PERMISSION(10001, "您没有权限访问"), // HTTP 403
    // 对应 ERRORCODE.AUTHTOKEN = "10019"
    TOKEN_INVALID(10019, "无效的token"), // HTTP 401
    TOKEN_EXPIRED(10020, "token已过期"), // HTTP 401 (新增一个与 INVALID 区分)

    // === 用户模块 (2xxxx) ===
    // 对应 ERRORCODE.USER = "10003"
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_ALREADY_EXIST(20002, "用户已存在"),
    USER_LOGIN_ERROR(20003, "用户名或密码错误"),
    USER_PASSWORD_ERROR(20004, "旧密码错误"), // 对应 UserUpdatePasswordDTO
    USER_TEST_CANT_MODIFY(20005, "测试用户密码不可以修改哦"), // 对应 controller/user/index.js

    // === 文章模块 (3xxxx) ===
    // 对应 ERRORCODE.ARTICLE = "10004"
    ARTICLE_NOT_EXIST(30001, "文章不存在"),
    ARTICLE_PASSWORD_ERROR(30002, "文章密码错误"),

    // === 分类模块 (4xxxx) ===
    // 对应 ERRORCODE.CATEGORY = "10005"
    CATEGORY_EXIST(40001, "分类已存在"),
    CATEGORY_NOT_EXIST(40002, "分类不存在"),
    CATEGORY_HAS_ARTICLE(40003, "该分类下存在文章，无法删除"), // Service 层逻辑

    // === 标签模块 (5xxxx) ===
    // 对应 ERRORCODE.TAG = "10006"
    TAG_EXIST(50001, "标签已存在"),
    TAG_NOT_EXIST(50002, "标签不存在"),
    TAG_HAS_ARTICLE(50003, "该标签下存在文章，无法删除"), // Service 层逻辑

    // === 评论模块 (6xxxx) ===
    // 对应 ERRORCODE.COMMENT = "10007"
    COMMENT_NOT_EXIST(60001, "评论不存在"),

    // === 文件上传 (7xxxx) ===
    // 对应 ERRORCODE.UPLOAD = "10008"
    UPLOAD_ERROR(70001, "文件上传失败"),
    UPLOAD_TYPE_ERROR(70002, "文件类型不支持"), // 新增

    // === 友链模块 (8xxxx) ===
    // 对应 ERRORCODE.LINKS = "10009"
    LINK_EXIST(80001, "友链已存在"),
    LINK_NOT_EXIST(80002, "友链不存在"),

    // === 说说模块 (9xxxx) ===
    // 对应 ERRORCODE.TALK = "10010"
    TALK_NOT_EXIST(90001, "说说不存在"),

    // === 留言模块 (11xxx) ===
    // 对应 ERRORCODE.MESSAGE = "10011"
    MESSAGE_NOT_EXIST(11001, "留言不存在"),

    // === 相册/照片 (12xxx/13xxx) ===
    // 对应 ERRORCODE.PHOTOALBUM = "10012"
    PHOTO_ALBUM_EXIST(12001, "相册已存在"),
    PHOTO_ALBUM_NOT_EXIST(12002, "相册不存在"),
    // 对应 ERRORCODE.PHOTO = "10013"
    PHOTO_NOT_EXIST(13001, "照片不存在"),

    // === 聊天/通知/推荐/配置/头部 (14xxx - 18xxx) ===
    // 对应 ERRORCODE.CHAT = "10014"
    CHAT_ERROR(14001, "聊天服务错误"),
    // 对应 ERRORCODE.NOTIFY = "10015"
    NOTIFY_ERROR(15001, "通知服务错误"),
    // 对应 ERRORCODE.RECOMMEND = "10016"
    RECOMMEND_NOT_EXIST(16001, "推荐不存在"),
    // 对应 ERRORCODE.CONFIG = "10017"
    CONFIG_ERROR(17001, "配置服务错误"),
    // 对应 ERRORCODE.HEADER = "10018"
    HEADER_ERROR(18001, "头部服务错误");


    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // (可选) 增加一个根据 code 查找枚举的方法
    public static ResultCode getByCode(int code) {
        for (ResultCode rc : ResultCode.values()) {
            if (rc.getCode() == code) {
                return rc;
            }
        }
        return null;
    }
}