package com.lkl.ansuote.hdqlibrary.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity 管理类
 *
 * 随时随地退出活动
 * @author huangdongqiang
 * @date 04/06/2018
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        try {
            activities.remove(activity);
        } catch (Exception e) {

        }
    }


    /**
     * 移除所有界面
     */
    public static void removeAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }


}
