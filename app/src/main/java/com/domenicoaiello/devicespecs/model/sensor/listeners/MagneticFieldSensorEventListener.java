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

public class MagneticFieldSensorEventListener implements SensorListener {

    private long lastUpdate = System.nanoTime();

    private onValueChangeListener mMagneticFieldListener;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mMagneticFieldListener == null)
            return ;

        long curtime = System.nanoTime() / 1000000000;
        long lasttime = lastUpdate / 1000000000;

        if(curtime - lasttime < 1)
            return ;
        else
            lastUpdate = System.nanoTime();

        Sensor sensor = event.sensor;

        String values = String.format(Locale.getDefault(), "X=%.2f Y=%.2f Z=%.2f uT", event.values[0], event.values[1], event.values[2]);
      //  Log.d("SENSOR-INFO", String.format("Magnetic field change: X=%.2f Y=%.2f Z=%.2f uT",event.values[0],event.values[1], event.values[2]));

        mMagneticFieldListener.onValueChange(values, sensor.getName());

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        Log.d("Sensor-DEBUG", String.format(Locale.getDefault(), "magnetic field accuracy change: %s: %d", arg0.getName(), arg1));
    }



    @Override
    public int getSensorType() {
        return Sensor.TYPE_MAGNETIC_FIELD;
    }

    @Override
    public void setOnValueChangeListener(onValueChangeListener listener) {
        this.mMagneticFieldListener = listener;
    }
}
