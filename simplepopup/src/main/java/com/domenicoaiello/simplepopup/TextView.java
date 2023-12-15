package com.domenicoaiello.simplepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Domenico on 01/11/2017.
 */

public class TextView extends android.support.v7.widget.AppCompatTextView {

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
       // Log.d("Popup-debug", "Textview size changed: w="+getWidth() + " h="+getHeight());
    }
}
