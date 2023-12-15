package com.domenicoaiello.devicespecs.model;

import android.app.Activity;

import com.domenicoaiello.devicespecs.model.cpu.CPU;
import com.domenicoaiello.devicespecs.model.cpu.DefaultCPU;
import com.domenicoaiello.devicespecs.model.gpu.AdrenoGPU;
import com.domenicoaiello.devicespecs.model.gpu.GPU;
import com.domenicoaiello.devicespecs.model.gpu.MaliGPU;

/**
 * Created by Domenico on 08/10/2017.
 */

public class HardwareDetector {

    private static GPU gpu;
    private static CPU cpu;

    public static GPU getGPU(Activity activity) {
        if(gpu == null) {
            gpu = new AdrenoGPU(activity);
            if(gpu.getRenderer().toLowerCase().contains("mali"))
                gpu = new MaliGPU(activity);
        }

        return gpu;
    }

    public static CPU getCPU(Activity activity) {
        if(cpu == null)
            cpu = new DefaultCPU(activity);

        return cpu;
    }
}
