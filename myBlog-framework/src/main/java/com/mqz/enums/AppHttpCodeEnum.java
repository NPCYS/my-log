package com.mqz.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    NICKNAME_EXIST(501,"昵称已存在"),
     PHONENUMBER_EXIST(502,"手机号已存在"), EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505,"用户名或密码错误"),
    REQUIRE_COMMENT(506,"评论不能为空~"),
    FILE_TYPE_ERROR(507,"上传文件类型错误"),
    USER_CONTENT_IS_EMPTY(508,"用户名，密码，昵称，邮箱不能为空"),
    USER_ROLE_IS_EMPTY(509,"用户的角色为空！"),
    TAG_NOT_FOUND(510,"Tag没找着"),
    TAG_ADD_NOT_NAME(511,"标签的名称不能为空哦~"),
    TAG_ADD_NOT_REMARK(512,"标签的简介不能为空哦~"),
    OSS_IMG_UPLOAD_FAILED(513,"图片上传失败"),
    ARTICLE_ID_IS_EMPTY(514,"文章id为空"),
    MENU_CHILDREN_IS_NOT_EMPTY(500,"存在子菜单不允许删除");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}