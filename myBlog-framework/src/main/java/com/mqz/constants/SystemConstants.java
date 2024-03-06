package com.mqz.constants;

public class SystemConstants
{
    /**
     *  文章是草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     *  文章是正常分布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;
    /**
     *分类状态时正常的
     */
    public static final int CATEGORY_STATUS_NORMAL = 0;
    public static final int TAG_STATUS_NORMAL = 0;
    public static final int MENU_STATUS_NORMAL = 0;
    /**
     *分类状态时不正常的
     */
    public static final int CATEGORY_STATUS_DRAFT = 1;
    /**
     * 友链是审核通过状态
     */
    public static final int LINK_STATUS_NORMAL = 0;
    /**
     * 友链是审核未通过状态
     */
    public static final int LINK_STATUS_FAIL = 1;
    /**
     * 友链是未审核状态
     */
    public static final int LINK_STATUS_UNAUDITED = 2;
    /**
     * 前台登录所用的查询redis的签注
     */
    public static final String LOGIN_REDIS_KEY_PREFIX = "bloglogin:id:";
    public static final String ADMIN_LOGIN_REDIS_KEY_PREFIX = "login:id:";
    public static final String PageView_REDIS_KEY_PREFIX = "pageView:id:";
    /**
     * token头
     */
    public static final String HTTP_HEADER = "token";
    /**
     * 根评论
     */
    public static final int COMMENT_ROOT_ID  = -1;
    /**
     * 该评论的根评论的发布者的id
     */
    public static final int COMMENT_TO_COMMENT_USERID = -1;
    /**
     * 文章评论type
     */
    public static final String COMMENT_TYPE = "0";
    /**
     * 友链Type
     */
    public static final String LINK_TYPE = "1";
    /**
     * 后台菜单的父节点id
     */
    public static final long BACKGROUND_PARENTID = 0L;
    /**
     * 确认删除的标志
     */
    public static final int CONFIRM_DELETE = 1;

    public static final String ADMIN = "1";

    public static final String TREE_SELECT="menu:allMenu";
    public static final String TREE_SELECT_ONE="menu:OneMenu";
}