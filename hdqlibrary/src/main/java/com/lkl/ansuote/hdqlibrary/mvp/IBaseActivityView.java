package com.lkl.ansuote.hdqlibrary.mvp;


/**
 * MVP 架构框架 界面基类接口
 *
 * @author huangdongqiang
 * @date 2018/3/30
 */
public interface IBaseActivityView extends BaseView{

    /**
     * 传递给上一个界面结果的，之后接收当前界面
     * @param data
     * @param resultCode
     */
    void finishWithResult(Object data, int resultCode);

    /**
     * 传递给上一个界面结果的，之后接收当前界面，默认成功
     * @param data
     */
    void finishWithResult(Object data);

    /**
     * 结束当前 Activity
     */
    void finish();
}
