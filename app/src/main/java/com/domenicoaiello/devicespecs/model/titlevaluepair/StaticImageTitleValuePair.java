package com.domenicoaiello.devicespecs.model.titlevaluepair;

import android.graphics.Bitmap;

import com.domenicoaiello.devicespecs.R;

/**
 * Created by Domenico on 28/09/2017.
 */

public class StaticImageTitleValuePair implements ImageTitleValuePair {

    public static final String CODE_NONE = "NONE";
    private String mTitle;
    private Bitmap mValue;
    private String mCode = CODE_NONE;

    public StaticImageTitleValuePair(String mTitle, Bitmap mValue) {
        this.mTitle = mTitle;
        this.mValue = mValue;
    }

    public StaticImageTitleValuePair(String mTitle, Bitmap mValue, String mCode) {
        this.mTitle = mTitle;
        this.mValue = mValue;
        this.mCode = mCode;
    }

    @Override
    public String getTitle() {
        return capitalize(mTitle);
    }

    @Override
    public Bitmap getValue() {
        return mValue;
    }

    @Override
    public String getCode() {
        return mCode;
    }

    @Override
    public void setValue(Bitmap value) {
        this.mValue = value;
    }

    @Override
    public void setCode(String code) {
        this.mCode = code;
    }

    @Override
    public int getAssociatedLayout() {
        return R.layout.line_item_poweredby;
    }

    @Override
    public int getOptions() {
        return OPTIONS_NONE;
    }

    @Override
    public String getPairType() {
        return TYPE_IMAGE;
    }

    private String capitalize(String str) {
        if(str != null && str.length() > 1)
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);

        return str;
    }
}
