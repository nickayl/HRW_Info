package com.domenicoaiello.devicespecs.model.sensor;

import android.hardware.SensorEventListener;

import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

/**
 * Created by Domenico on 13/10/2017.
 */

public interface SensorListener extends SensorEventListener {

    /**
     *
     * @return returns the sensor type. One of the Sensor.TYPE_* types.
     */
    int getSensorType();

    void setOnValueChangeListener(onValueChangeListener listener);
}
