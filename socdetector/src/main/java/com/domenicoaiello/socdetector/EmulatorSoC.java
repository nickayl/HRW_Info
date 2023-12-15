package com.domenicoaiello.socdetector;

import android.content.Context;

/**
 * Created by Domenico on 26/10/2017.
 */

public class EmulatorSoC extends HiSiliconSoC {

    private String detectedModel;

    @Override
    public boolean check() {
        String pattern = "ranchu";
        detectedModel = super.regexCheck(pattern);

        if(detectedModel == null)
            return false;


        setCode(detectedModel);
        return true;
    }

    @Override
    public String getPopupDescription(Context context) {
        return context.getResources().getString(R.string.arm_descr);
    }
}
