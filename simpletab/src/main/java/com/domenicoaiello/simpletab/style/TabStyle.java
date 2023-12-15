package com.domenicoaiello.simpletab.style;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Domenico on 05/11/2017.
 */

public interface TabStyle {
    int getStyle();

    float TEXT_SIZE_DEFAULT = 15;
    /*
    void setTextColor(int color);
    void setBackgroundColor(int color);
    void setSelectedTabIndicatorColor(int color);*/

    @ColorInt
    int getTextColor();
    @ColorInt
    int getBackgroundColor();
    @ColorInt
    int getSelectedTabIndicatorColor();

    Typeface getTypeface();
    float getTextSize();

    String filterText(String text);
}
