package com.lkl.ansuote.hdqlibrary.widget.webview;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * webview封装
 * Created by huangdongqiang on 10/08/2017.
 */
public abstract class BaseWebView extends WebView {
    private WebSettings mSettings;
    /**
     * 是否进行安全性设置
     */
    private boolean mDoSafeWork = true;

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        webSetting();
    }

    private void webSetting() {
        mSettings = getSettings();
        if (null != mSettings) {
            // 网页内容的宽度是否可大于WebView控件的宽度
            mSettings.setLoadWithOverviewMode(false);
            // 保存表单数据
            mSettings.setSaveFormData(true);
            // 是否应该支持使用其屏幕缩放控件和手势缩放
            mSettings.setSupportZoom(true);
            mSettings.setBuiltInZoomControls(true);
            mSettings.setDisplayZoomControls(false);
            // 启动应用缓存
            mSettings.setAppCacheEnabled(true);
            // 设置缓存模式
            mSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            // setDefaultZoom  api19被弃用
            // 设置此属性，可任意比例缩放。
            mSettings.setUseWideViewPort(true);
            // 缩放比例 1
            this.setInitialScale(1);
            // 告诉WebView启用JavaScript执行。默认的是false。
            mSettings.setJavaScriptEnabled(true);
            //  页面加载好以后，再放开图片
            //mSettings.setBlockNetworkImage(false);
            // 使用localStorage则必须打开
            mSettings.setDomStorageEnabled(true);
            // 排版适应屏幕
            mSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            // WebView是否支持多个窗口。
            mSettings.setSupportMultipleWindows(true);

            // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            // 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)
            //ws.setTextZoom(100);
        }

        if (mDoSafeWork) {
            doSafeWork();
        }
        webSettingsImp(mSettings);
    }

    protected abstract void webSettingsImp(WebSettings webSettings);


    public void onDestory() {
        mSettings = null;
    }



    /**
     * 将cookie同步到WebView
     * @param url WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    public static boolean syncCookie(Context context, String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        return TextUtils.isEmpty(newCookie)?false:true;
    }

    /**
     * 进行安全性设置
     */
    private void doSafeWork() {
        //移除Android系统内部的默认内置接口，该接口存在安全隐患
        this.removeJavascriptInterface("searchBoxJavaBridge_");
        this.removeJavascriptInterface("accessibility");
        this.removeJavascriptInterface("accessibilityTraversal");
    }

}
