package com.lkl.ansuote.hdqlibrary.mvp;

/**
 * @author huangdongqiang
 * @date 2018/4/3
 */
public interface IBasePresenter<V> {

    /**
     * 建立联系
     * @param view
     */
    void attachView(V view);



    /**
     * 销毁关联的view
     */
    void detachView();
}
