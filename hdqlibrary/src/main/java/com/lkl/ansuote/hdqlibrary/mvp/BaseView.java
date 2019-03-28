package com.lkl.ansuote.hdqlibrary.mvp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.ImageView;

/**
 * @author huangdongqiang
 * @date 17/04/2018
 */
public interface BaseView {

    /**
     * 初始化标题栏
     */
    void initTitle();

    /**
     * 初始化标题栏
     * @param contentRes
     */
    void initTitle(@StringRes int contentRes);

    /**
     * 初始化标题栏
     * @param contentText
     */
    void initTitle(String contentText);

    /**
     * 初始化刷新控件
     */
    void initRefreshLayout();

    /**
     * 自动刷新
     */
    void autoRefresh();

    /**
     * 设置是否启用上啦加载更多（默认启用）
     */
    void setEnableLoadMore(boolean enable);

    /**
     * 是否启用下拉刷新（默认启用）
     */
    void setEnableRefresh(boolean enable);

    /**
     * 完成刷新
     */
    void finishRefresh();

    /**
     * 完成加载
     */
    void finishLoadMore();

    /**
     * 加载完最后一页数据，没有数据了
     */
    void finishLoadMoreWithNoMoreData();

    /**
     * 加载完成的时候显示底部布局
     * @param enable
     */
    void setEnableFooterFollowWhenLoadFinished(boolean enable);

    /**
     * 显示加载对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏加载对话框
     */
    void hideLoadingDialog();

    /**
     * 显示信息
     * @param errorMsg
     */
    void showMsg(String errorMsg);

    /**
     * 显示信息
     */
    void showMsg(@StringRes final int resId);

    /**
     * 注册相关监听
     * @param b
     */
    void regEvent(boolean b);

    /**
     * 设置状态栏颜色
     * @param color
     */
    //void setStatusColor(int color);

    /**
     * 开启只定界面
     * @param cls
     */
    void actionStart(Class<? extends Activity> cls);

    /**
     * 开启只定界面
     * @param cls
     * @param flags
     */
    //void actionStart(Class<? extends Activity> cls, int flags);

    /**
     * 开启只定界面
     * @param cls
     * @param data
     * @param requestCode
     */
    void actionStart(Class<? extends Activity> cls, Object data, int requestCode);

    /**
     * 开启只定界面
     * @param cls
     * @param data
     */
    void actionStart(Class<? extends Activity> cls, Object data);

    /**
     * 显示主体布局
     */
    void showContentView();

    /**
     * 显示网络错误界面
     */
    void showNetworkErrorView();

    /**
     * 显示空数据布局
     */
    void showEmptyDataView();

    /**
     * 加载圆形图片
     * @param context
     * @param url
     * @param iv
     */
    void loadCircleCrop(Context context, String url, ImageView iv);

    /**
     * 隐藏输入法
     */
    void hideSoftInputFromWindow();
}
