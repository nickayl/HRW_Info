package com.domenicoaiello.devicespecs.model.gpu;

import android.app.Activity;

import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

/**
 * Created by Domenico on 09/10/2017.
 */

public class MaliGPU extends BaseGPU implements GPU {

    private onValueChangeListener newLoadValue;

    public MaliGPU(Activity activity) throws IllegalArgumentException {
        super(activity);
    }

    @Override
    public String getCurrentLoadPercent() {
        return null;
    }

    @Override
    public String getMaxClock() {
        return null;
    }

    @Override
    public void setOnLoadChangeListener(onValueChangeListener newLoadValue) {
        this.newLoadValue = newLoadValue;
    }

    @Override
    public onValueChangeListener getOnLoadChangeListener() {
        return newLoadValue;
    }
}
