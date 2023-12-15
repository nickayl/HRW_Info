package com.domenicoaiello.socdetector;

import android.content.Context;

/**
 * Created by Domenico on 26/10/2017.
 */

public class MediaTekSoC extends DefaultSoc {

    private String detectedModel;

    public MediaTekSoC() {
        super();
        setBrand("MediaTek®");
        setModel("");
        setDrawableResource(R.drawable.mediatek);
    }

    @Override
    public boolean check() {
        String pattern = "mt[0-9]+.*";
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
