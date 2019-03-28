package com.lkl.ansuote.hdqlibrary.widget.dialog.base;

/**
 * Created by huangdongqiang on 10/08/2017.
 */
public interface IBaseDialog {
    /**
     * 显示对话框
     */
    void show();

    /**
     * 取消对话框
     */
    void dismiss();

    /**
     * 对象销毁
     */
    void onDestory();
}
