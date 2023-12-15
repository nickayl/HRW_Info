package com.domenicoaiello.devicespecs.model.valuechangelistener;

import android.util.Log;

import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

/**
 * Created by Domenico on 12/11/2017.
 */

public class SystemInfoListener extends ListenerPoolExecutor.ListenerRunnable {

    private final onValueChangeListener uptimeChangeListener;
    private Runnable runnable;

    public SystemInfoListener(onValueChangeListener uptimeChangeListener) {
        super();
        this.uptimeChangeListener = uptimeChangeListener;
    }

    @Override
    public Runnable getRunnable() {
        if(runnable == null)
            runnable = new Runnable() {

                @Override
                public void run() {

                    try {
                        String uptime = Utils.readSystemFile("cat proc/uptime");
                        Log.d("SYSTEM-INFO", "System uptime: "+uptime);
                        String[] splitted = uptime.split(" ");
                        double currentSeconds = Double.valueOf(splitted[0]);
                        double divideByDays = currentSeconds / 86400;
                        double divideByHours = (divideByDays % 1) * 24;
                        double divideByMinutes = (divideByHours % 1) * 60;
                        double divideBySeconds = (divideByMinutes % 1) * 60;

                        String days = String.valueOf((int) divideByDays);
                        String hours = String.valueOf((int) divideByHours);
                        String minutes = String.valueOf((int) divideByMinutes);
                        String seconds = String.valueOf(Math.round(divideBySeconds));

                        if (hours.length() == 1)
                            hours = "0" + hours;

                        if (minutes.length() == 1)
                            minutes = "0" + minutes;

                        if (seconds.length() == 1)
                            seconds = "0" + seconds;

                        String value = String.format("%s:%s:%s", hours, minutes, seconds);
                        if(!days.equals("0"))
                            value = days + " days " + value;

                        uptimeChangeListener.onValueChange(value, Utils.SYSTEM_UPTIME_LISTENER);
                        repeat();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            };

        return runnable;
    }

    @Override
    public int getIdentifier() {
        return Utils.SISTEMINFO_UPTIME_CHANGE_LISTENER;
    }

    @Override
    public long getDelay() {
        return 1000;
    }
}
