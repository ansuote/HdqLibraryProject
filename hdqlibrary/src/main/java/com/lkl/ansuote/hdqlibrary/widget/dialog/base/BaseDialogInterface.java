package com.lkl.ansuote.hdqlibrary.widget.dialog.base;

/**
 * Created by huangdongqiang on 10/08/2017.
 */
public interface BaseDialogInterface {

    /**
     * 点击确定按钮
     */
    interface OnConfirmClickListener {
        void onConfirm(BaseDialog baseDialog);
    }

    /**
     * 点击关闭按钮
     */
    interface OnCancelClickListener {
        void onCancel(BaseDialog baseDialog);
    }
}
