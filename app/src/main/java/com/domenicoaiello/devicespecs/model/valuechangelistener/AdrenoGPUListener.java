package com.domenicoaiello.devicespecs.model.valuechangelistener;

import android.util.Log;

import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.gpu.GPU;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

/**
 * Created by Domenico on 13/11/2017.
 */

public class AdrenoGPUListener extends ListenerPoolExecutor.ListenerRunnable {

    private Runnable runnable;
    private GPU gpuInstance;
    private onValueChangeListener onLoadChangeListener;

    public AdrenoGPUListener(GPU gpuInstance, onValueChangeListener onLoadChangeListener) throws IllegalArgumentException {
        this.gpuInstance = gpuInstance;
        this.onLoadChangeListener = onLoadChangeListener;

        if(onLoadChangeListener == null)
            throw new IllegalArgumentException("onLoadChangeListener cannot be null on "+getClass().getName());
    }

    @Override
    public Runnable getRunnable() {
        if(runnable == null)
            runnable = new Runnable() {

                @Override
                public void run() {
                    String perc = gpuInstance.getCurrentLoadPercent();

                    onLoadChangeListener.onValueChange(perc + "%", Utils.LISTENER_GPU_LOAD);
                    Log.d("GPU-INFO", "notify listener (GPUInfo) gpu load:" + perc);

                    repeat();
                }
            };

        return runnable;
    }

    @Override
    public int getIdentifier() {
        return Utils.ADRENO_GPU_LOAD_CHANGE_LISTENER;
    }

    @Override
    public long getDelay() {
        return 1000;
    }
}
