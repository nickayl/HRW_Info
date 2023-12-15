package com.domenicoaiello.devicespecs.model.cpu;

/**
 * Created by Domenico on 18/10/2017.
 */

public class Clusters {

    private int numCore=0;
    private double minFreq=0;
    private double maxFreq=0;

    public Clusters() {

    }

    public Clusters(int numCore, int minFreq, int maxFreq) {
        this.numCore = numCore;
        this.minFreq = minFreq;
        this.maxFreq = maxFreq;
    }

    public int getNumCore() {
        return numCore;
    }

    public String getMinFreq() {
        if(minFreq > 1000)
            return String.format("%.2f GHz",minFreq/1000);

        return String.format("%.0f MHz",minFreq);
    }

    public String getMaxFreq() {
        if(maxFreq > 1000)
            return String.format("%.2f GHz",maxFreq/1000);

        return String.format("%.0f MHz",maxFreq);
    }

    public void setNumCore(int numCore) {
        this.numCore = numCore;
    }

    public void setMinFreq(double minFreq) {
        this.minFreq = minFreq;
    }

    public void setMaxFreq(double maxFreq) {
        this.maxFreq = maxFreq;
    }
}