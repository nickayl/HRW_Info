package com.domenicoaiello.socdetector;

import android.content.Context;
import android.os.Build;

/**
 * Created by Domenico on 26/10/2017.
 */

public class QualcommSoC extends DefaultSoc {

    private String detectedModel;

    public QualcommSoC() {
        super();
        setBrand("Qualcomm®");
        setModel("Snapdragon™");
        setCode("");
        setDrawableResource(R.drawable.qualcomm);
    }


    @Override
    public boolean check() {
        String pattern = "msm[0-9]+.*";
        detectedModel = super.regexCheck(pattern);

        if(detectedModel == null) {
            return Build.HARDWARE.equalsIgnoreCase("qcom");
        }

        setCode(detectedModel);
        return true;
    }

    @Override
    public String getPopupDescription(Context context) {
        return context.getResources().getString(R.string.arm_descr);
    }
}
