package com.domenicoaiello.socdetector;

import android.content.Context;

/**
 * Created by Domenico on 26/10/2017.
 */

public class HiSiliconSoC extends DefaultSoc {

    private String detectedModel;

    public HiSiliconSoC() {
        super();
        setBrand("Huawei® HiSilicon™");
        setModel("Kirin™");
        setCode("");
        setDrawableResource(R.drawable.hisilicon);
    }

    @Override
    public boolean check() {
        String pattern = "hi[0-9]+.*";
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
