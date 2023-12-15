package com.domenicoaiello.devicespecs;

/**
 * Created by Domenico on 12/11/2017.
 */

public interface IDataProvider<T> {

    T getProcessorData();
    T getDeviceData();
    T getSystemData();
    T getScreenData();
    T getBatteryData();
    T getGpuData();
}
