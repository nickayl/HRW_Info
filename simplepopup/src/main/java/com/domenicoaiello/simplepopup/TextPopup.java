package com.domenicoaiello.simplepopup;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Domenico on 27/10/2017.
 */

public class TextPopup extends BasePopup<String> {

    private TextView textView;

    public TextPopup(Activity activity, @IdRes int mainActivityRootView, View attachedView) {
        super(activity, R.layout.questionmark_popup, mainActivityRootView, attachedView);
    }

    @Override
    protected void initialize() {
        super.initialize();

        textView = (TextView) getPopupView().findViewById(R.id.textViewPopup);
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Log.d("popup-debug","text view onlayout: w="+textView.getWidth() + " h: "+textView.getHeight());
                int width = textView.getWidth();
                int height = textView.getHeight();

                if(width != 0 && height != 0) {
                    if(popupView.getWidth() == width && popupView.getHeight() == height)
                        return ;
                    if(height > displayHeight/2) {
                        height = (displayHeight/2) - 30;
                    }

                    if(width > displayWidth/2) {
                        width = (displayWidth/2);
                    }

                    popupView.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

            }
        });
    }

    @Override
    public void setContent(String content) {
        textView.setText(content);
    }








      /*  int height = Math.round(content.length() / 20)*55;
        if(height > displayHeight/2)
            height = (displayHeight/2) - 30;

        Paint p = new TextPaint();
        float textWidth =  p.measureText(content);
        int textHeight = pxFromDp(50);

        if(textWidth > displayWidth/2) {
            float tmp = (textWidth / (displayWidth/2));
            textWidth = (displayWidth / 2) - 10;
            textHeight = Math.round(pxFromDp(75) * tmp);
        }

        if(textHeight > displayHeight/2)
            textHeight = (displayHeight/2) - 30;
/*
        popupView.setLayoutParams(new RelativeLayout.LayoutParams((int)textWidth,height));
        popupView.requestLayout();
        popupView.forceLayout(); */

}
