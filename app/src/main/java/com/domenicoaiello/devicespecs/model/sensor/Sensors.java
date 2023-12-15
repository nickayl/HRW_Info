package com.domenicoaiello.devicespecs.model.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.domenicoaiello.devicespecs.model.sensor.listeners.AmbientTemperatureSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.GameRotationVectorSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.GyroscopeUncalibratedSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.MagneticFieldSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.MagneticFieldSensorEventListenerUncalibrated;
import com.domenicoaiello.devicespecs.model.sensor.listeners.OrientationSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.PressureSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.RelativeHumiditySensorEventListener;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ValuePairProvider;
import com.domenicoaiello.devicespecs.model.titlevaluepair.VariableTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.sensor.listeners.AccelerometerSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.GyroscopeSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.LightSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.LinearAccelerationSensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.ProximitySensorEventListener;
import com.domenicoaiello.devicespecs.model.sensor.listeners.RotationVectorSensorEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Domenico on 10/10/2017.
 */

public class Sensors implements ValuePairProvider {

    private final SensorManager mSensorManager;
    private Activity activity;

    private List<SensorListener> sensorEventListeners;

    public Sensors(Activity activity) throws IllegalArgumentException {
        if(activity == null)
            throw new IllegalArgumentException("Activity cannot be null on Sensor class");

        this.activity = activity;
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorEventListeners = new ArrayList<>(4);
        sensorEventListeners.addAll(Arrays.asList(
                new AccelerometerSensorEventListener(),
                new AmbientTemperatureSensorEventListener(),
                new GyroscopeSensorEventListener(),
                new MagneticFieldSensorEventListener(),
                new MagneticFieldSensorEventListenerUncalibrated(),
                new LightSensorEventListener(),
                new PressureSensorEventListener(),
                new ProximitySensorEventListener(),
                new OrientationSensorEventListener(),
                new RelativeHumiditySensorEventListener(),
                new LinearAccelerationSensorEventListener(),
                new RotationVectorSensorEventListener(),
                new GameRotationVectorSensorEventListener(),
                new GyroscopeUncalibratedSensorEventListener()
        ));
    }

    public void registerListeners() {
        for(SensorListener sensorListener : sensorEventListeners)
            mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(sensorListener.getSensorType()), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners() {
        for(SensorListener sensorListener : sensorEventListeners)
            mSensorManager.unregisterListener(sensorListener);
    }


    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {

        ArrayList<TextTitleValuePair> pairs = new ArrayList<>();
        List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < mList.size(); i++) {
            Sensor s = mList.get(i);
         //   Log.d("Sensor-INFO", String.format(Locale.getDefault(), "Sensor name: %s, \nVendor: %s, \nVersion: \n%s, Resolution: %s\n\n",s.getName(),s.getVendor(), s.getVersion(), s.getResolution()));
            pairs.add(new StaticTextTitleValuePair("Sensor #"+i, s.getName()));
            String tm = "™";
            if(s.getVendor().equalsIgnoreCase("aosp"))
                tm = "";
            pairs.add(new StaticTextTitleValuePair("   Vendor ", s.getVendor() + tm));
            pairs.add(new StaticTextTitleValuePair("   Version ", String.valueOf(s.getVersion())));

            VariableTextTitleValuePair vtp = new VariableTextTitleValuePair("    Values", "", s.getName());
            pairs.add(vtp);

            for(SensorListener sensorListener : sensorEventListeners) {
                if(s.getType() == sensorListener.getSensorType()) {
                    sensorListener.setOnValueChangeListener(vtp);
                    break;
                }
            }
        }
        return pairs;
    }
}
