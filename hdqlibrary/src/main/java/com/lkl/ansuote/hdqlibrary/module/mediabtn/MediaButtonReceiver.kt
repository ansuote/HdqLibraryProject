package com.lkl.ansuote.hdqlibrary.module.mediabtn

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent

/**
 *
 * 多媒体按钮广播。兼容 Android 5.0 之前版本
 * @author huangdongqiang
 * @date 15/04/2019
 */
class MediaButtonReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        if (Intent.ACTION_MEDIA_BUTTON == action) {
            val keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT) as? KeyEvent
            if (KeyEvent.ACTION_UP == keyEvent?.action) {
                MediaButtonManager.mClickListener?.onClick()
            }
        }
    }
}