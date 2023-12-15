package com.domenicoaiello.devicespecs.model.titlevaluepair;

import com.domenicoaiello.devicespecs.R;

/**
 * Created by Domenico on 28/09/2017.
 */

public class StaticTextTitleValuePair implements TextTitleValuePair {

    public static final String CODE_NONE = "NONE";
    private String mTitle;
    private String mValue;
    private String mCode = CODE_NONE;

    public StaticTextTitleValuePair(String mTitle, String mValue) {
        this.mTitle = mTitle;
        this.mValue = mValue;
    }

    public StaticTextTitleValuePair(String mTitle, String mValue, String mCode) {
        this.mTitle = mTitle;
        this.mValue = mValue;
        this.mCode = mCode;
    }

    @Override
    public String getTitle() {
        return capitalize(mTitle);
    }

    @Override
    public String getValue() {
        return capitalize(mValue);
    }

    @Override
    public String getCode() {
        return mCode;
    }

    @Override
    public void setValue(String value) {
        this.mValue = value;
    }

    @Override
    public String getTextDescription() {
        return null;
    }

    @Override
    public void setCode(String code) {
        this.mCode = code;
    }

    @Override
    public int getAssociatedLayout() {
        return R.layout.line_item_recyclerview;
    }

    @Override
    public int getOptions() {
        return OPTIONS_NONE;
    }

    @Override
    public String getPairType() {
        return TYPE_TEXT;
    }

    private String capitalize(String str) {
        if(str != null && str.length() > 1)
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);

        return str;
    }
}
