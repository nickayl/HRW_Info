package com.domenicoaiello.socdetector;

import android.content.Context;

/**
 * Created by Domenico on 26/10/2017.
 */

public class NvidiaSoc extends DefaultSoc {

    private String detectedModel;

    public NvidiaSoc() {
        super();
        setBrand("Nvidia®");
        setCode("");
        setDrawableResource(R.drawable.nvidia_tegra);
    }

    @Override
    public boolean check() {
        String pattern = ".*nvidia.*tegra.*";
        detectedModel = super.regexCheck(pattern);

        if(detectedModel == null)
            return false;

        setModel(decodeModelCode());
        return true;
    }

    private String decodeModelCode() {
        return detectedModel.replaceAll("nvidia","");
    }


    @Override
    public String getPopupDescription(Context context) {
        return context.getResources().getString(R.string.nvidia_descr);
    }
}
