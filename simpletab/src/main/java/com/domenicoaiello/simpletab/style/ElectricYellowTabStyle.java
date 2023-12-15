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

public class ElectricYellowTabStyle implements TabStyle {

    private Context context;
    private ViewGroup tabView;

    public ElectricYellowTabStyle(Context context) {
        this.context = context;
    }

    @Override
    public int getStyle() {
        return Tab.STYLE_DEFAULT;
    }

    /*
    @Override
    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {

        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setSelectedTabIndicatorColor(int selectedTabIndicatorColor) {

        this.selectedTabIndicatorColor = selectedTabIndicatorColor;
    } */

    @Override
    @ColorInt
    public int getTextColor() {
        return getColorFromResource(R.color.question_mark_box_text);
    }

    @Override
    @ColorInt
    public int getBackgroundColor() {
        return getColorFromResource(R.color.question_mark_box);
    }

    @Override
    @ColorInt
    public int getSelectedTabIndicatorColor() {
        return getColorFromResource(R.color.tabViewBackground);
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
