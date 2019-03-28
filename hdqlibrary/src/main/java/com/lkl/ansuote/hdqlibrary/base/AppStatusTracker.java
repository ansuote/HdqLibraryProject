package com.lkl.ansuote.hdqlibrary.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


/**
 * 跟踪全部 Activity 的生命周期
 * 可以用于判断 APP 是否在前台，锁屏等操作
 *
 * @author huangdongqiang
 * @date 2018/3/30
 */
public class AppStatusTracker implements Application.ActivityLifecycleCallbacks {
    /**
     * 锁屏最大时间
     */
    private static final long MAX_INTERVAL = 5 * 60 * 1000;

    /**
     * 默认值为 killed 状态，可用于检查进程是否被杀
     */
    private int mAppStatus = ConstantValues.STATUS_FORCE_KILLED;
    private Application mApplication;
    private static AppStatusTracker sAppstatusTracker;
    private long mTimestamp;
    /**
     * 是否点击了电源键系统锁屏
     */
    private boolean mIsScreenOff;
    //private DaemonReceiver mReceiver;

    /**
     * 应用是否处于前台
     */
    private boolean mIsForground;
    private List<String> mActivitySimpleNameList = new ArrayList<>();

    /**
     * 前台界面计数
     */
    private int mForgroundCount;

    private OnAppStatusChangedListener mOnAppStatusChangedListener;

    private AppStatusTracker(Application application) {
        mApplication = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    /**
     * Application初始化的时候操作
     * @param application
     */
    public static void init(Application application) {
        sAppstatusTracker = new AppStatusTracker(application);

    }

    public static AppStatusTracker getInstance() {
        return sAppstatusTracker;
    }

    /**
     * 应用是否处于前台
     * @return
     */
    public boolean isForground() {
        return mIsForground;
    }



    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //Logger.i("onActivityCreated: " + activity);
        mActivitySimpleNameList.add(activity.getClass().getSimpleName());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Logger.i("onActivityStarted: " + activity + "; mForgroundCount = " + mForgroundCount);
        mForgroundCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        //Logger.i("onActivityResumed: " + activity);
        mIsForground = true;
        mTimestamp = 0L;
        mIsScreenOff = false;

        if (getAppStatus() == ConstantValues.STATUS_FINISH_ALL) {
            activity.finish();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        //Logger.i( "onActivityPaused: " + activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //Logger.i("onActivityStopped: " + activity + "; mForgroundCount = " + mForgroundCount);
        mForgroundCount--;
        if (mForgroundCount == 0) {
            mIsForground = false;
            mTimestamp = System.currentTimeMillis();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        //Logger.i("onActivitySaveInstanceState: ");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //Logger.i("onActivityDestroyed: ");
        if (mActivitySimpleNameList.contains(activity.getClass().getSimpleName())) {
            mActivitySimpleNameList.remove(activity.getLocalClassName());
        }
    }

    /**
     * 检查是否需要显示手势
     * @return
     */
    public boolean checkIfShowGesture() {
        if (mAppStatus == ConstantValues.STATUS_ONLINE) {
            //是否需要系统锁屏的时候，显示手势／指纹验证界面
//            if (mIsScreenOff) {
//                return true;
//            }
            if (mTimestamp != 0L && System.currentTimeMillis() - mTimestamp > MAX_INTERVAL) {
                return true;
            }
        }
        return false;
    }

    private void onScreenOff(boolean isScreenOff) {
        this.mIsScreenOff = isScreenOff;
    }

    public void setAppStatus(int status) {
        if (null != mOnAppStatusChangedListener &&  this.mAppStatus != status) {
            mOnAppStatusChangedListener.onAppStatusChanged(mAppStatus, status);
        }

        this.mAppStatus = status;
//        if (status == ConstantValues.STATUS_ONLINE) {
//            if (mReceiver == null) {
//                IntentFilter filter = new IntentFilter();
//                filter.addAction(Intent.ACTION_SCREEN_OFF);
//                mReceiver = new DaemonReceiver();
//                mApplication.registerReceiver(mReceiver, filter);
//            }
//        } else if (mReceiver != null) {
//            mApplication.unregisterReceiver(mReceiver);
//            mReceiver = null;
//        }
    }
    public int getAppStatus() {
        return this.mAppStatus;
    }



    public List<String> getActivitySimpleNameList() {
        return mActivitySimpleNameList;
    }

    /**
     * 获取最新一个Activity名
     * @return
     */
    public String getLastActivitySimpleName() {
        String lastActivitySimpleName = "";
        int size = mActivitySimpleNameList.size();
        if (size > 0) {
            lastActivitySimpleName = mActivitySimpleNameList.get(size - 1);
        }
        return lastActivitySimpleName;
    }

    /**
     * 获取指定位置的简易 Activity 名称
     * @param position
     * @return
     */
    public String getActivitySimpleName(int position) {
        String simpleName = "";
        int size = mActivitySimpleNameList.size();
        if (position >=0 && size > position) {
            simpleName = mActivitySimpleNameList.get(position);
        }

        return simpleName;
    }

    /**
     * 登录状态改变的监听
     */
    public interface OnAppStatusChangedListener {

        /**
         * app状态变化
         * @param beforeStatus 变化之前的状态
         * @param nowStatus    变化之后的状态
         */
        void onAppStatusChanged(int beforeStatus, int nowStatus);
    }


    public void setOnAppStatusChangedListener(OnAppStatusChangedListener listener) {
        mOnAppStatusChangedListener = listener;
    }

    /**
     * 监听系统屏幕关闭的广播
     */
//    private class DaemonReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
//                AppStatusTracker.getInstance().onScreenOff(true);
//            }
//        }
//    }


}
