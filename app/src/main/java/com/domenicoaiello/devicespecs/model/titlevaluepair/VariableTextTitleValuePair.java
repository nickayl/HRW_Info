package com.domenicoaiello.devicespecs.model.titlevaluepair;

/**
 * Created by Domenico on 05/10/2017.
 */

public class VariableTextTitleValuePair extends StaticTextTitleValuePair implements onValueChangeListener {

    private onValueChangeListener listener;

    public VariableTextTitleValuePair(String mTitle, String mValue) {
        super(mTitle, mValue);
    }

    public VariableTextTitleValuePair(String mTitle, String mValue, String mCode) {
        super(mTitle, mValue, mCode);
    }

    public onValueChangeListener getOnValueChangeListener() {
        return listener;
    }

    public void setOnValueChangeListener(onValueChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onValueChange(String newValue, String valueCode) {
        if(listener != null)
            this.listener.onValueChange(newValue, valueCode);
    }
}
