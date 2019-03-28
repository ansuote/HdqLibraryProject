package com.lkl.ansuote.hdqlibrary.util.view

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText

/**
 *
 * @author huangdongqiang
 * @date 16/01/2019
 */
object EditTextUtil {

    /**
     * 设置光标在最后
     * @param editText EditText?
     */
    fun setSelectionEnd(editText: EditText?) {
        editText?.setSelection(editText.text.length)
        editText?.requestFocus()
    }

    /**
     * 设置是否是密码那种密文形式
     * @param editText EditText?
     * @param isCiphertext Boolean 是否是密文
     */
    fun setPwdMethod(editText: EditText?, isCiphertext: Boolean) {
        editText?.transformationMethod = if (isCiphertext) PasswordTransformationMethod.getInstance()  else HideReturnsTransformationMethod.getInstance()
    }

    /**
     * 设置输入不能输入空格
     * @param editText EditText?
     */
    fun setInputExceptBlank(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                //不能输入空格
                if (s.toString().contains(" ")) {
                    editText.setText(editText.text.toString().replace(" ", ""))
                    editText.setSelection(editText.text.length)
                    return
                }
            }
        })

    }

    /**
     * 动态设置最多输入的字符数
     * @param edit_other EditText?
     */
    fun setMaxSize(editText: EditText?, max: Int) {
        //new InputFilter[]{new InputFilter.LengthFilter(MAX_SIZE)}
        editText?.filters = arrayOf(InputFilter.LengthFilter(max))
    }

    /**
     * 是否是只读
     *
     * @param editText EditText
     * @param enable Boolean
     */
    fun setOnlyRead(editText: EditText, enable: Boolean) {
        editText.isFocusable = enable
        editText.isFocusableInTouchMode = enable
    }

    /**
     * 设置可以复制，
     *  false：需要在只读模式下
     *
     * @param editText EditText
     * @param canCopy Boolean
     */
    fun setCanCopy(editText: EditText, canCopy: Boolean) {
        editText.setTextIsSelectable(canCopy)
    }

}