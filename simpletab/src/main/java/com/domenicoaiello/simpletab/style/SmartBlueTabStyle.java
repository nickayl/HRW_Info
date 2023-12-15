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

public class SmartBlueTabStyle implements TabStyle {

    private Context context;
    private ViewGroup tabView;

    public SmartBlueTabStyle(Context context) {
        this.context = context;
    }

    @Override
    public int getStyle() {
        return Tab.STYLE_DEFAULT;
    }


    @Override
    @ColorInt
    public int getTextColor() {
        return getColorFromResource(R.color.smartblue_tab_textcolor);
    }

    @Override
    @ColorInt
    public int getBackgroundColor() {
        return getColorFromResource(R.color.smartblue_tab_background);
    }

    @Override
    @ColorInt
    public int getSelectedTabIndicatorColor() {
        return getColorFromResource(R.color.smartblue_tab_selected_background);
    }

    @Override
    public Typeface getTypeface() {
        return Typeface.createFromAsset(context.getAssets(), "fonts/OpenSansCondensed-Bold.ttf");
    }

    @Override
    public float getTextSize() {
        return 17;
    }

    @Override
    public String filterText(String text) {
        return text.toUpperCase();
    }


    private int getColorFromResource(@ColorRes int color) {
        return context.getResources().getColor(color);
    }

}
