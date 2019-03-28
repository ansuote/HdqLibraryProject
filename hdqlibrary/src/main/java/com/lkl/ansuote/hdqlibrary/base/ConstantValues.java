package com.lkl.ansuote.hdqlibrary.base;

public class ConstantValues {
    /**
     * 应用被杀
     */
    public static final int STATUS_FORCE_KILLED = -1;
    /**
     * 注销,退出系统
     */
    //public static final int STATUS_LOGOUT = 0;
    /**
     * 未登录
     */
    public static final int STATUS_OFFLINE = 1;
    /**
     * 登录状态
     */
    public static final int STATUS_ONLINE = 2;
    /**
     * 用户被踢或者TOKEN失效
     */
    public static final int STATUS_KICK_OUT = 3;
    /**
     * 全部界面退出
     */
    public static final int STATUS_FINISH_ALL = 4;

    public static final String KEY_HOME_ACTION = "key_home_action";
    public static final int ACTION_BACK_TO_HOME = 0;
    public static final int ACTION_RESTART_APP = 1;
    public static final int ACTION_LOGOUT = 2;
    public static final int ACTION_KICK_OUT = 3;
    public static final String KEY_FRAGMENT_PAGE = "key_fragment_page";
    public static final String KEY_SAVE_LIST = "key_save_list";
}
