package com.lkl.ansuote.hdqlibrary.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.EditText
import com.blankj.utilcode.util.ConvertUtils
import com.lkl.ansuote.hdqlibrary.R


/**
 *
 * 自定义 editText 实现右下角显示字数
 *
 * 注意：maxLength 必须依照下面格式，在 integer 定义一个变量
 * android:maxLength="@integer/edit_text_count_max_length"  对应的值小于0，则表示没有现在输入长度
 *
 * @author huangdongqiang
 * @date 12/02/2019
 */
class EditTextCountView @JvmOverloads constructor(context: Context? = null, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.editTextStyle): EditText(context, attrs, defStyleAttr) {
    private var mCountPaint: Paint
    private var mWidth: Float = 0f
    private var mHeight: Float = 0f
    private var mCountBounds: Rect = Rect()
    /**
     * count 内边距
     */
    private var mCountPadding = 0f
    private var mMaxLength: Int = -1
    /**
     * Scroll 竖直方向滑动的距离从 0 开始
     */
    private var mVer: Int = 0

    init {
        mCountPaint = Paint().apply {
            this.style = Paint.Style.FILL
            val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.EditTextCountView, defStyleAttr, 0)
            typedArray?.let {
                this.textSize = it.getDimension(R.styleable.EditTextCountView_countSize, ConvertUtils.sp2px(12.0f).toFloat())
                this.color = it.getColor(R.styleable.EditTextCountView_countColor, Color.BLACK)
                mCountPadding = it.getDimension(R.styleable.EditTextCountView_countPadding, ConvertUtils.dp2px(16.0f).toFloat())
                mMaxLength = resources.getInteger(R.integer.edit_text_count_max_length)
                it.recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val currentLength = text.toString().length
        //数字文本如：3/30
        val countText = currentLength.toString() + "/" + if (mMaxLength <= 0) {"∞"} else mMaxLength

        //文本的真实高度
        val contentTextHeight = lineHeight * lineCount.toFloat()

        //测量文本的框高
        mCountPaint.getTextBounds(countText, 0, countText.length, mCountBounds)
        val countHeight = mCountBounds.height()
        val countWidth = mCountBounds.width()
        //设置底部内边距，为 count 预留显示空间
        val minCountHeight = (countHeight + mCountPadding * 2).toInt()
        if (paddingBottom < minCountHeight) {
            setPadding(paddingLeft, paddingTop, paddingRight , minCountHeight)
        }

        //如果需要翻页：文本高度 高于设置的高度（减去 预留高度），则使用文本高度（mVer 为竖直方向滑动的距离），否则使用设置高度
        val countY = if (contentTextHeight > mHeight - minCountHeight)  mHeight + mVer else mHeight

        canvas?.drawText(countText, mWidth - countWidth - mCountPadding, countY - countHeight - mCountPadding, mCountPaint)
    }

    override fun onScrollChanged(horiz: Int, vert: Int, oldHoriz: Int, oldVert: Int) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert)
        mVer = vert
    }
}