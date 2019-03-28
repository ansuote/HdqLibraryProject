package com.lkl.ansuote.hdqlibrary.mvp;

import android.content.Intent;
import android.os.Bundle;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * MVP 架构框架 基类 presenter
 *
 * @author huangdongqiang
 * @date 2018/3/30
 */
public abstract class BasePresenter<V> implements IBasePresenter<V>{
    protected Reference<V> mViewRef;
    private V mProxyView;
    private CompositeDisposable compositeDisposable;

    /**
     * 建立联系
     * @param view
     */
    @Override
    public void attachView(V view) {
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
     * 初始化其他Activity传过来的值
     * @param savedInstanceState
     * @param intent
     */
    public abstract void initVariables(Bundle savedInstanceState, Intent intent);

    /**
     * 让 View 可以设置默认状态
     */
    public abstract void onCreate();

    public void onStart() {

    }

    public void onNewIntent(Intent intent) {

    }

    public void onResume() {

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

    /**
     * 判断是否建立联系
     * 注意：因为使用了动态代理，外面在使用的时候，不需要手动用该方法进行判断。
     * @return
     */
    public boolean isViewAttached(){
        return null != mViewRef && null != mViewRef.get();
        //return null != mProxyView;
    }

    protected void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    /**
     * 销毁关联的view
     */
    @Override
    public void detachView() {
        if (null != mViewRef) {
            mViewRef.clear();
            mViewRef = null;
        }

        //此处不能销毁代理对象，因为界面消失后，仍然有可能存在异步调用界面的方法
        //mProxyView = null;

        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }
}
