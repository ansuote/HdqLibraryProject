package com.lkl.ansuote.hdqlibrary.util;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

/**
 * @author huangdongqiang
 * @date 2018/4/30
 */
public class ViewUtils {
    /**
     * 默认的左右偏移量
     */
    private static int DEFAULT_VIEW_SHAKE_DELTA = 20;

    /**
     * 默认的偏移时间（ms）
     */
    private static int DEFAULT_VIEW_SHAKE_DURATION = 1000;


    /**
     * 指定时间内进行左右晃动
     * @param view
     * @param delta 左右偏移
     * @param duration
     * @return
     */
    public static ObjectAnimator shakeKeyframe(View view, int delta, int duration) {

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                //Keyframe是一个时间/值对，用于定义在某个时刻动画的状态。
                //比如Keyframe.ofFloat(.10f, -10)定义了当动画进行了50%的时候，x轴的偏移值应该是-10。
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.1f, -delta),
                Keyframe.ofFloat(.2f, delta),
                Keyframe.ofFloat(.3f, -delta),
                Keyframe.ofFloat(.5f, delta),
                Keyframe.ofFloat(.7f, -delta),
                Keyframe.ofFloat(.9f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(duration);
    }

    /**
     * 指定时间内进行左右晃动
     * @param view
     * @param delta 左右偏移
     * @param duration
     * @return
     */
    public static void startShakeHorizontal(View view, int delta, int duration) {

        ObjectAnimator objectAnimator = shakeKeyframe(view, delta, duration);
        if (null != objectAnimator) {
            objectAnimator.start();
        }
    }

    /**
     * 指定时间内进行左右晃动
     * @param view
     * @return
     */
    public static void startShakeHorizontal(View view) {
        startShakeHorizontal(view, DEFAULT_VIEW_SHAKE_DELTA, DEFAULT_VIEW_SHAKE_DURATION);
    }

    /**
     * 判断通知栏是否打开
     * @param context
     * @return
     */
    public static boolean areNotificationsEnabled(Context context) {
        try {
            NotificationManagerCompat from = NotificationManagerCompat.from(context);
            return from.areNotificationsEnabled();
        } catch (Exception e) {

        }

        return true;
    }

    /**
     * 打开允许通知的设置页
     */
    public static void goToNotificationSetting(Context context) {
        try {

            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT >= 26) {
                // android 8.0引导
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
            } else if (Build.VERSION.SDK_INT >= 21) {
                // android 5.0-7.0
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", context.getPackageName());
                intent.putExtra("app_uid", context.getApplicationInfo().uid);
            } else {
                // 其他
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", context.getPackageName(), null));
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {

        }

    }
}
