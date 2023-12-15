package com.domenicoaiello.devicespecs.model.sensor.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;
import com.domenicoaiello.devicespecs.model.sensor.SensorListener;

import java.util.Locale;

/**
 * Created by Domenico on 13/10/2017.
 */

public class RelativeHumiditySensorEventListener implements SensorListener {

    private long lastUpdate = System.nanoTime();

    private onValueChangeListener mRelativeHumiditySensorListener;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mRelativeHumiditySensorListener == null)
            return ;

        long curtime = System.nanoTime() / 1000000000;
        long lasttime = lastUpdate / 1000000000;

        if(curtime - lasttime < 1)
            return ;
        else
            lastUpdate = System.nanoTime();

        Sensor sensor = event.sensor;

        String values = String.format(Locale.getDefault(), "%.2f ", event.values[0]);
    //    Log.d("SENSOR-INFO", String.format("relative humidity sensor change: %s", values+"%"));

        mRelativeHumiditySensorListener.onValueChange(values+"%", sensor.getName());

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        Log.d("Sensor-DEBUG", String.format(Locale.getDefault(), "relative humidity accuracy change: %s: %d", arg0.getName(), arg1));
    }

    @Override
    public int getSensorType() {
        return Sensor.TYPE_RELATIVE_HUMIDITY;
    }

    @Override
    public void setOnValueChangeListener(onValueChangeListener listener) {
        this.mRelativeHumiditySensorListener = listener;
    }
}
