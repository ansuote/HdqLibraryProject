package com.lkl.ansuote.hdqlibrary.mvp.fragment;

import com.lkl.ansuote.hdqlibrary.mvp.IBasePresenter;
import com.lkl.ansuote.hdqlibrary.mvp.IBaseFragmentView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author huangdongqiang
 * @date 13/04/2018
 */
public abstract class BaseFragmentPresenter<V extends IBaseFragmentView> implements IBasePresenter<V>{
    //protected T mView;
    protected Reference<V> mViewRef;
    private V mProxyView;
    private CompositeDisposable compositeDisposable;

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void attachView(V view) {
        //this.mView = view;

        mViewRef = new WeakReference<V>(view);
        ClassLoader loader = view.getClass().getClassLoader();
        Class<?>[] interfaces = view.getClass().getInterfaces();
        ViewInvocationHandler handler = new ViewInvocationHandler(mViewRef);
        mProxyView = (V) Proxy.newProxyInstance(loader, interfaces, handler);
    }

    /**
     * 运用动态代理，只在界面 Attached 的时候才调用界面的方法。
     * 外面就不用再每次判断 isViewAttached
     */
    private class ViewInvocationHandler implements InvocationHandler {

        private Reference<V>  view;

        public ViewInvocationHandler(Reference<V>  view){
            this.view = view;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if (null != this.view && null != this.view.get()){
                return method.invoke(view.get(), objects);
            }
            return null;
        }
    }

    /**
     * 获取View
     * @return
     */
    protected V getView() {
        /*if (null != mViewRef) {
            return mViewRef.get();
        }
        return null;*/
        return mProxyView;
    }

    @Override
    public void detachView() {
        //this.mView = null;
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }

        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    /**
     * 代理 fragment 生命周期
     */

    protected void onViewCreated() {

    }

    protected void onStart(){

    }

    protected void onStop() {

    }

    protected void onResume() {

    }

    protected void onDestroyView(){

    }

    protected void setUserVisibleHint(boolean isVisibleToUser) {

    }

    /**
     * 可以设置View的相关属性
     */
    public abstract  void setUpView();

    /**
     * 可以设置数据
     */
    public abstract void setUpData();
}
