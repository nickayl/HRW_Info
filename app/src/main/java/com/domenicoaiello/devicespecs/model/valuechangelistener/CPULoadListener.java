package com.domenicoaiello.devicespecs.model.valuechangelistener;

import android.util.Log;

import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.cpu.CPU;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

import java.util.List;

/**
 * Created by Domenico on 13/11/2017.
 */

public class CPULoadListener extends ListenerPoolExecutor.ListenerRunnable {

    private Runnable runnable;
    private List<onValueChangeListener> onCPULoadChangeListener;
    private onValueChangeListener cpuTempListener;
    private CPU cpuInstance;

    public CPULoadListener(List<onValueChangeListener> onCPULoadChangeListener, onValueChangeListener cpuTempListener, CPU cpuInstance) {
        this.onCPULoadChangeListener = onCPULoadChangeListener;
        this.cpuTempListener = cpuTempListener;
        this.cpuInstance = cpuInstance;

        if(cpuInstance == null)
            throw new IllegalArgumentException("Cpu instance cannot be null on "+getClass().getName());
    }

    @Override
    public Runnable getRunnable() {
        if(runnable == null)
            runnable = new Runnable() {
                @Override
                public void run() {

                    if(cpuTempListener != null) {
                        String cpuTemp = cpuInstance.getCpuTemp() + " °C";
                        cpuTempListener.onValueChange(cpuTemp, Utils.LISTENER_CPU_TEMP);
                    }

                    String[] cpuLoad = cpuInstance.getCpuLoad();
                    for (int i = 0; i < cpuInstance.getNumCores() && i < cpuLoad.length; i++) {
                        onCPULoadChangeListener.get(i).onValueChange(cpuLoad[i], Utils.LISTENER_CPU_LOAD + i);
                    }

                    Log.d("CPU-OBSERVER-INFO", "cpu load");
                    repeat();
                }
            };

        return runnable;
    }

    @Override
    public int getIdentifier() {
        return Utils.CPU_LOAD_CHANGE_LISTENER;
    }

    @Override
    public long getDelay() {
        return 500;
    }
}
