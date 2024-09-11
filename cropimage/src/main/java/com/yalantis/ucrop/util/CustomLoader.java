package com.yalantis.ucrop.util;

import android.app.ProgressDialog;
import android.content.Context;

public class CustomLoader extends ProgressDialog {

    public static CustomLoader mCustomLoader;

    OnDismissListener mOnDissmissListener;

    public CustomLoader(Context context) {
        super(context);
        setMessage("Saving Image...");
        setProgressStyle(STYLE_SPINNER);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        if (mOnDissmissListener != null && mCustomLoader.isShowing())
            mOnDissmissListener.onDismiss(this);
    }

    public static void showProgressBar(Context context, boolean cancelable) {

        if (mCustomLoader != null && mCustomLoader.isShowing()) {
            mCustomLoader.cancel();
        }
        mCustomLoader = new CustomLoader(context);
        mCustomLoader.setCancelable(cancelable);
//		mCustomLoader.setCancelable(false); // TODO hardcoded
        mCustomLoader.show();
    }

    public static void showProgressBar(Context context, OnDismissListener listener) {

        if (mCustomLoader != null && mCustomLoader.isShowing()) {
            mCustomLoader.cancel();
        }

        if (listener == null) {

        }

        mCustomLoader = new CustomLoader(context);
        mCustomLoader.setListener(listener);
        mCustomLoader.setCancelable(Boolean.TRUE);
        mCustomLoader.show();
    }

    public static void hideProgressBar() {
        if (mCustomLoader != null && mCustomLoader.isShowing()) {
            mCustomLoader.dismiss();
        }
    }

    private void setListener(OnDismissListener listener) {
        mOnDissmissListener = listener;

    }

}

