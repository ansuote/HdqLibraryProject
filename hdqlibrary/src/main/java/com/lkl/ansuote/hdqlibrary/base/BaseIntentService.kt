package com.lkl.ansuote.hdqlibrary.base

import android.app.IntentService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * IntentService 基类
 * @author huangdongqiang
 * @date 25/10/2018
 */
abstract class BaseIntentService(name: String) :IntentService(name) {
    private var compositeDisposable: CompositeDisposable? = null

    fun addSubscribe(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    override fun onDestroy() {

        compositeDisposable?.clear()
        super.onDestroy()
    }
}

