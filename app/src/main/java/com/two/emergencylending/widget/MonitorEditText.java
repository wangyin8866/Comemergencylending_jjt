package com.two.emergencylending.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by User on 2016/8/4.
 */
public class MonitorEditText extends EditText{

    private OnImputCompleteListener mOnImputCompleteListener;
    private Context context;

    public MonitorEditText(Context context) {
        super(context);
        this.context = context;
    }

    public MonitorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MonitorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (!focused) {
            mOnImputCompleteListener.onImputComplete();
        }
    }

    public void setOnImputCompleteListener(OnImputCompleteListener l) {
        this.mOnImputCompleteListener = l;
    }

   public  interface OnImputCompleteListener {
        void onImputComplete();
    }

}





