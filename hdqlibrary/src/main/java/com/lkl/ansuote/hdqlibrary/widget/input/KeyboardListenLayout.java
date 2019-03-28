package com.lkl.ansuote.hdqlibrary.widget.input;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Android输入法遮挡问题的解决思路
 * https://www.jianshu.com/p/50c060edeaa8
 */
public class KeyboardListenLayout extends RelativeLayout {

    private onSizeChangedListener mChangedListener;

    public KeyboardListenLayout(Context context) {
        super(context);
    }

    public KeyboardListenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyboardListenLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (null != mChangedListener && 0 != oldw && 0 != oldh) {
            boolean showKeyboard = h < oldh;
            mChangedListener.onChanged(showKeyboard, h, oldh);
        }
    }

    public void setOnSizeChangedListener(onSizeChangedListener listener) {
        mChangedListener = listener;
    }

    public interface onSizeChangedListener {
        void onChanged(boolean showKeyboard, int h, int oldh);
    }
}
