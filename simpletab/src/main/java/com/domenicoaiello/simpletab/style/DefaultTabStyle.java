package com.domenicoaiello.simpletab.style;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.view.ViewGroup;

import com.domenicoaiello.simpletab.R;
import com.domenicoaiello.simpletab.Tab;

/**
 * Created by Domenico on 05/11/2017.
 */

public class DefaultTabStyle implements TabStyle {

    private Context context;
    private ViewGroup tabView;

    public DefaultTabStyle(Context context) {
        this.context = context;
    }

    @Override
    public int getStyle() {
        return Tab.STYLE_DEFAULT;
    }


    @Override
    @ColorInt
    public int getTextColor() {
        return getColorFromResource(R.color.tabViewTextColor);
    }

    @Override
    @ColorInt
    public int getBackgroundColor() {
        return getColorFromResource(R.color.tabViewBackground);
    }

    @Override
    @ColorInt
    public int getSelectedTabIndicatorColor() {
        return getColorFromResource(R.color.tabViewSelectedColor);
    }

    @Override
    public Typeface getTypeface() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-Regular.ttf");
    }

    @Override
    public float getTextSize() {
        return TEXT_SIZE_DEFAULT;
    }

    @Override
    public String filterText(String text) {
        return text;
    }


    private int getColorFromResource(@ColorRes int color) {
        return context.getResources().getColor(color);
    }

}
