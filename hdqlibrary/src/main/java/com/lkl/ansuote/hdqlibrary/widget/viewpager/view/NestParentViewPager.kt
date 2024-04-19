package ecall.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 解决viewpager滑动冲突 父viewpager
 * Created by huangdongqiang on 2019/4/24.
 */
class NestParentViewPager(context: Context, attrs: AttributeSet? = null) :
    ViewPager(context, attrs) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return when (ev?.action) {
            MotionEvent.ACTION_DOWN -> false
            else -> super.onInterceptTouchEvent(ev)
        }
    }

}