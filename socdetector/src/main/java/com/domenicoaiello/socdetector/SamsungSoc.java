package com.domenicoaiello.socdetector;

import android.content.Context;

/**
 * Created by Domenico on 26/10/2017.
 */

public class SamsungSoc extends DefaultSoc {

    private String detectedModel;

    public SamsungSoc() {
        super();
        setBrand("Samsung®");
        setModel("Exynos™");
        setDrawableResource(R.drawable.exynos);
    }

    @Override
    public boolean check() {
        String pattern = ".*exynos[0-9]+.*";
        detectedModel = super.regexCheck(pattern);

        if(detectedModel == null)
            return false;

        setCode(decodeModelCode());
        return true;
    }

    private String decodeModelCode() {
        return detectedModel.replaceAll("[^0-9]*","");
    }

    @Override
    public String getPopupDescription(Context context) {
        return context.getResources().getString(R.string.arm_descr);
    }
}
