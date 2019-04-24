package ecall.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 解决viewpager滑动冲突 子viewpager
 * Created by huangdongqiang on 2019/4/24.
 */
class NestChildViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val count = adapter?.count ?: 0
                //滑动到最后一页，由父控件处理
                if (currentItem == count - 1 || currentItem == 0) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * TODO BETTER 可以完善未支持滑动最后一个，交给父 viewpager 处理事件
     */
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return super.onTouchEvent(ev)
    }
}