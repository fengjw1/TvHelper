package com.fengjw.tvhelper.recenttask.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by fengjw on 2017/11/20.
 */

public class ScrollTextView extends AppCompatTextView {

    private boolean canFocused = false ;

    public ScrollTextView(Context context) {
        super(context);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
        // TODO Auto-generated method stub
        super.onFocusChanged(arg0, arg1, arg2);
    }
    @Override
    public boolean isFocused() {
        return isCanFocused();
    }

    @Override
    public boolean hasFocus() {
        return isCanFocused();
    }

    public boolean isCanFocused() {
        return canFocused;
    }

    public void setCanFocused(boolean canFocused) {
        this.canFocused = canFocused;
    }

}