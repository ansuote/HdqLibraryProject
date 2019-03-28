package com.lkl.ansuote.hdqlibrary.mode;

/**
 * 用于数据处理完的回调
 *
 * @author huangdongqiang
 * @date 2018/3/30
 */
public class CallBack<T> {

    public void onStart() {
    }

    public void onSuccess(T t) {
    }

    public void onFailure(String msg, Exception e) {
    }
}
