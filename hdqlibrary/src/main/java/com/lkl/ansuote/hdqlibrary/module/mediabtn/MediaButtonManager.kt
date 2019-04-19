package com.lkl.ansuote.hdqlibrary.module.mediabtn


import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.blankj.utilcode.util.DeviceUtils


/**
 *
 * 多媒体按钮管理类。Android 5.0 以下用 AudioManager， 5.0及其以上版本用：MediaSession
 * @author huangdongqiang
 * @date 15/04/2019
 */
object MediaButtonManager {
    private var mAudioManager: AudioManager? = null
    private var mComponentName: ComponentName? = null
    private var mPendingIntent: PendingIntent? = null
    private var mHandler: Handler? = null
    private var mMediaSession: MediaSessionCompat? = null
    /**
     * 外部可以监听该回调，实现点击操作
     */
    var mClickListener: MediaButtonClickListener? = null

    /**
     * 初始化
     * @param context Context?
     */
    fun init(context: Context?) {
        if (null != context) {
            if (isAbove5()) {

                //这里同样要指明相应的MediaBottonReceiver，用来接收处理线控信息
                //Android5.0之前的版本线控信息直接通过BroadcastReceiver处理
                mComponentName = ComponentName(context.getPackageName(), MediaButtonReceiver::class.java.name)
                context.packageManager.setComponentEnabledSetting(mComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)

                val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                mediaButtonIntent.component = mComponentName
                mPendingIntent = PendingIntent.getBroadcast(context, 0, mediaButtonIntent, PendingIntent.FLAG_CANCEL_CURRENT)

                //由于非线程安全，这里要把所有的事件都放到主线程中处理，使用这个handler保证都处于主线程
                mHandler = Handler(Looper.getMainLooper())

                mMediaSession = MediaSessionCompat(context, "mbr", mComponentName, null).apply {

                    //指明支持的按键信息类型
                    this.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
                    this.setMediaButtonReceiver(mPendingIntent)

                    //这里指定可以接收的来自锁屏页面的按键信息
                    val state = PlaybackStateCompat.Builder().setActions(
                            PlaybackStateCompat.ACTION_FAST_FORWARD or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY
                                    or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_STOP).build()
                    this.setPlaybackState(state)

                    //在Android5.0及以后的版本中线控信息在这里处理
                    this.setCallback(object : MediaSessionCompat.Callback() {
                        override fun onMediaButtonEvent(intent: Intent): Boolean {
                            //通过Callback返回按键信息，为复用MediaButtonReceiver，直接调用它的onReceive()方法
                            val mMediaButtonReceiver = MediaButtonReceiver()
                            mMediaButtonReceiver.onReceive(context, intent)
                            return true
                        }
                    }, mHandler)    //把mHandler当做参数传入，保证按键事件处理在主线程

                    //把MediaSession置为active，这样才能开始接收各种信息
                    if (!this.isActive) {
                        this.isActive = true
                    }
                }
            } else {

                mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                mComponentName = ComponentName(context.packageName, MediaButtonReceiver::class.java.name)
                context.getPackageManager().setComponentEnabledSetting(mComponentName,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
                val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                mediaButtonIntent.component = mComponentName
                mPendingIntent = PendingIntent.getBroadcast(context, 0, mediaButtonIntent, PendingIntent.FLAG_CANCEL_CURRENT)
                mAudioManager?.registerMediaButtonEventReceiver(mComponentName)
            }
        }

    }

    /**
     * Android  5.0 及其以上
     * @return Boolean
     */
    private fun isAbove5() : Boolean {
        return DeviceUtils.getSDKVersionCode() >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * 设置监听回调
     * @param listener MediaButtonClickListener
     */
    fun setMediaButtonClickListener(listener: MediaButtonClickListener) {
        mClickListener = listener
    }

    /**
     * 移除监听回调
     */
    fun removeMediaButtonClickListener() {
        mClickListener = null
    }

    /**
     * 手动销毁
     * @param context Context?
     */
    fun onDestory() {
        try {
            if (isAbove5()) {
                mMediaSession?.release()
                mPendingIntent = null
                mHandler?.removeCallbacksAndMessages(null)
                mHandler = null

            } else {
                mAudioManager?.unregisterMediaButtonEventReceiver(mComponentName)
            }
            mComponentName = null
            mClickListener = null
        } catch (ex: Exception) {

        }
    }

}