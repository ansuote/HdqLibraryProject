package com.lkl.ansuote.hdqlibrary.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lkl.ansuote.hdqlibrary.mvp.IBaseFragmentView;
import com.lkl.ansuote.hdqlibrary.widget.fragment.BaseHFragment;

import javax.inject.Inject;

/**
 * MVP模式的Base fragment
 */

public abstract class BaseMvpFragment<P extends BaseFragmentPresenter, C> extends BaseHFragment implements IBaseFragmentView {

    @Inject
    protected P mPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
            mPresenter.onViewCreated();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mPresenter) {
            mPresenter.onResume();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (null != mPresenter) {
            mPresenter.setUserVisibleHint(isVisibleToUser);
        }
    }

    /**
     * 由各个模块定义 Component
     * @return
     */
    protected abstract C getFragmentComponent();


    /**
     * 注入当前Fragment所需的依赖
     */
    protected abstract void initInject();

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();
}
