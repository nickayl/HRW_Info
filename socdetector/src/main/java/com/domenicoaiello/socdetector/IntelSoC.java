package com.domenicoaiello.socdetector;

import android.content.Context;

/**
 * Created by Domenico on 26/10/2017.
 */

public class IntelSoC extends DefaultSoc {

    private String detectedModel;

    public IntelSoC() {
        super();
        setBrand("Intel®");
        setModel("Atom™");
        setDrawableResource(R.drawable.intel_atom);
    }

    @Override
    public boolean check() {
        String pattern = ".*intel.*atom.*";
        detectedModel = super.regexCheck(pattern);

        if(detectedModel == null)
            return false;

        setCode(decodeModelCode());
        return true;
    }

    private String decodeModelCode() {
        return detectedModel.replaceAll("intel|atom","");
    }

    @Override
    public String getPopupDescription(Context context) {
        return context.getResources().getString(R.string.intel_descr);
    }
}
