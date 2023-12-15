package com.domenicoaiello.devicespecs.model.titlevaluepair;

import com.domenicoaiello.devicespecs.R;

/**
 * Created by Domenico on 01/11/2017.
 */

public class VariableTextWithQuestionMarkTvp extends VariableTextTitleValuePair {

    private int option = OPTIONS_NONE;
    private String textDescription;

    public VariableTextWithQuestionMarkTvp(String mTitle, String mValue) {
        super(mTitle, mValue);
    }

    public VariableTextWithQuestionMarkTvp(String mTitle, String mValue, String mCode) {
        super(mTitle, mValue,mCode);
    }

    public VariableTextWithQuestionMarkTvp(String mTitle, String mValue, String mCode, String mDescription) {
        super(mTitle, mValue,mCode);
        textDescription = mDescription;
    }

    @Override
    public int getAssociatedLayout() {
        return R.layout.line_item_questionmark;
    }

    public void setOption(int option) {
        this.option = option;
    }

    @Override
    public int getOptions() {
        return option;
    }


    @Override
    public String getTextDescription() {
        return textDescription;
    }

    public void setTextDescription(String textDescription) {
        this.textDescription = textDescription;
    }


}
