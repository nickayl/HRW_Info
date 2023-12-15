package com.domenicoaiello.devicespecs.model.gpu;

import android.app.Activity;
import android.util.Log;

import com.domenicoaiello.devicespecs.model.valuechangelistener.AdrenoGPUListener;
import com.domenicoaiello.devicespecs.model.valuechangelistener.ListenerPoolExecutor;
import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.titlevaluepair.VariableTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

import java.util.Locale;

/**
 * Created by Domenico on 08/10/2017.
 */

public class AdrenoGPU extends BaseGPU implements GPU {

    private String currentLoadPercent;
    private String maxClock;

    private onValueChangeListener onLoadChangeListener;

    public AdrenoGPU(Activity activity)  {
        super(activity);

        try {
            VariableTextTitleValuePair vtp = new VariableTextTitleValuePair("GPU Load", "", Utils.LISTENER_GPU_LOAD);
            setOnLoadChangeListener(vtp);

            ListenerPoolExecutor
                    .getInstance()
                    .addListener(new AdrenoGPUListener(this, getOnLoadChangeListener()));
        } catch(Exception e) {
            e.printStackTrace();
            Log.e("AdrenoGPU", e.getMessage());
        }

    }

    @Override
    public String getCurrentLoadPercent() {
        String values = Utils.readSystemFile("cat sys/class/kgsl/kgsl-3d0/gpubusy");

        if(values.length() == 0)
            return Utils.UNKNOWN;

        try {
            String[] splitted = values.split("[ ]+");

            float num1 = Float.valueOf(splitted[1]);
            float num2 = Float.valueOf(splitted[2]);
            float percent = (num1 / num2) * 100;

            if (num2 == 0)
                percent = 0;

            currentLoadPercent = String.format(Locale.getDefault(), "%.0f", percent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentLoadPercent;
    }

    @Override
    public String getMaxClock() {
        if(maxClock != null)
            return maxClock;

        try {
            String maxGpuClock = Utils.readSystemFile("cat sys/class/kgsl/kgsl-3d0/max_gpuclk");

            if (maxGpuClock.length() == 0)
                return Utils.UNKNOWN;

            Integer gpuClk = Integer.valueOf(maxGpuClock);
            float num = gpuClk / 1000000;

            maxClock = String.format(Locale.getDefault(), "%.0f MHz", num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxClock;
    }

    @Override
    public void setOnLoadChangeListener(onValueChangeListener newLoadValue) {
        this.onLoadChangeListener = newLoadValue;
    }

    @Override
    public onValueChangeListener getOnLoadChangeListener() {
        return onLoadChangeListener;
    }

    /*
    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {
        ArrayList<TextTitleValuePair> list = super.getTitleValuePair();

        if(!getMaxClock().equals(Utils.UNKNOWN))
            list.add(new StaticTextTitleValuePair("Max GPU Clock", getMaxClock()));

        if(!getCurrentLoadPercent().equals(Utils.UNKNOWN)) {
            VariableTextTitleValuePair vtp = new VariableTextTitleValuePair("GPU Load", getCurrentLoadPercent() + "%", Utils.LISTENER_GPU_LOAD);
            list.add(vtp);
            setOnLoadChangeListener(vtp);
        }

        return list;
    } */
}
