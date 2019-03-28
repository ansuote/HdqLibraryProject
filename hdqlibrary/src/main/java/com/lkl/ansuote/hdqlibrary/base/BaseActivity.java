package com.lkl.ansuote.hdqlibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity 基类
 * @author huangdongqiang
 * @date 2018/3/30
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ActivityCollector.addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ActivityCollector.removeActivity(this);
    }

    /**
     * 开启只定界面
     * @param context
     * @param cls
     */
    public static void actionStrat(Context context, Class<? extends Activity> cls) {
        if (null != context) {
            Intent intent = new Intent(context, cls);
            if (null != intent) {
                context.startActivity(intent);
            }
        }
    }
}
