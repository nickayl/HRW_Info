package com.domenicoaiello.devicespecs.model.cpu;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.domenicoaiello.devicespecs.model.valuechangelistener.CPULoadListener;
import com.domenicoaiello.devicespecs.model.valuechangelistener.ListenerPoolExecutor;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.titlevaluepair.VariableTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;
import com.domenicoaiello.socdetector.SoCInfo;
import com.domenicoaiello.socdetector.SocDetector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 08/10/2017.
 */

public class DefaultCPU extends BaseCPU {

    private Activity activity;
    private Integer numCores;
    private String pathToCpu="";
    private String name;

    private int secondCounter = 15;
    private Handler handler;
    private Runnable runnable;

    protected List<onValueChangeListener> onCPULoadChangeListener = new ArrayList<>(4);

    private boolean isARM = false;

    private boolean cpuLoadPaused = false;
    private onValueChangeListener cpuTempListener;
    private String[] cpuLoad;
    private String line="";

    private VariableTextTitleValuePair cpuTempTvp;
    private List<VariableTextTitleValuePair> cpuLoadListener = new ArrayList<>();


    public DefaultCPU(Activity activity) throws IllegalArgumentException {
       super(activity);
       this.activity = activity;

        try {
            initialize();

            ListenerPoolExecutor
                    .getInstance()
                    .addListener(new CPULoadListener(onCPULoadChangeListener, cpuTempListener, this));

        } catch(RuntimeException e) {
            e.printStackTrace();
            Log.e("DEFAULT_CPU", e.getMessage());
        }
    }

    private void initialize() {

        for (int i = 0; i < getNumCores(); i++)
            pathToCpu += "/sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq ";

        cpuLoad = new String[16];
      //  calculateClusters();

        if(getCpuTemp().length() != 0) {
            cpuTempTvp = new VariableTextTitleValuePair("CPU Temperature", "", Utils.LISTENER_CPU_TEMP);
            cpuTempListener = cpuTempTvp;
        }

        String[] cpuLoad = getCpuLoad();
        for (int i = 0; i < getNumCores(); i++) {
            VariableTextTitleValuePair vtp = new VariableTextTitleValuePair("Core #" + i, cpuLoad[i], Utils.LISTENER_CPU_LOAD + i);
            addOnCpuLoadChangeListener(vtp);
            cpuLoadListener.add(vtp);
        }
    }


    @Override
    public String getSoCModel() {
        SoCInfo soc = SocDetector.getInstance().getSoC();

        return soc.getFullRetailBrandingName();
    }


    @Override
    public String getName() {
        if(name == null) {
            name = Utils.UNKNOWN;
            String text = "model name";
            String arch = getArchitecture().toLowerCase();
            if (arch.contains("arm") || arch.contains("arch64"))
                text = "processor";

            for (TextTitleValuePair vp : rawCpuInfo) {
                if (vp.getTitle().toLowerCase().contains(text)) {
                    name = vp.getValue();
                    break;
                }
            }
            String v8a_64 = "ARMv8-A processor (64 bit)";
            String v8a_32 = "ARMv8-A processor (32 bit)";

            isARM = true;

            String v7a = "ARMv7-A processor (32 bit)";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                String[] suppabis64 = Build.SUPPORTED_64_BIT_ABIS;
                for(String s : suppabis64) {
                    if (s.toLowerCase().contains("v8a"))
                        return name = v8a_64;
                }

                String[] supabis32 = Build.SUPPORTED_32_BIT_ABIS;
                for(String s : supabis32) {
                    if (s.toLowerCase().contains("v8a"))
                        return name = v8a_32;
                    else if (s.toLowerCase().contains("v7a"))
                        return name = v7a;
                }

            } else {
                String suppabis = Build.CPU_ABI;
                if (suppabis.toLowerCase().contains("v7a"))
                    return name = v7a;
            }

        }

        return name;
    }

    @Override
    public synchronized String[] getCpuLoad() {
        try {
            Process process = Runtime.getRuntime().exec("cat "+pathToCpu);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            for(int i=0; i < getNumCores(); i++) {
                line = br.readLine();
                if (line != null && line.matches("[0-9]+"))
                    cpuLoad[i] = String.valueOf(Integer.valueOf(line.trim()) / 1000) + " MHz";
                 else
                    cpuLoad[i] = "stopped";
            }

            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return cpuLoad;
    }

    @Override
    public Integer getNumCores() {
        if(numCores != null)
            return numCores;

        int num = 0;

        for(TextTitleValuePair pair : rawCpuInfo)
            if(pair.getTitle().toLowerCase().contains("processor") && pair.getValue().trim().matches("[0-9]+"))
                num++;

        numCores = num;
        return num;
    }

    @Override
    public String getCpuTemp() {
        String temp = Utils.readSystemFile("cat sys/class/thermal/thermal_zone0/temp").trim();

        try {
            int value = Integer.valueOf(temp);
            if(value >= 1000)
                value /= 1000;
            temp = String.valueOf(value);
        } catch(NumberFormatException e) {
            Log.e("CPU-TEMP", "Number format exception in cpu temp");
            e.printStackTrace();
        }

        return temp;
    }

    @Override
    public void addOnCpuLoadChangeListener(onValueChangeListener valueChangeListener) {
        this.onCPULoadChangeListener.add(valueChangeListener);
    }


    public VariableTextTitleValuePair getCpuTempTvp() {
        return cpuTempTvp;
    }

    public List<VariableTextTitleValuePair> getCpuLoadListener() {
        return cpuLoadListener;
    }

    /*
    @Override
    public void startCpuLoadObserver() {
        Log.d("PROCESSOR-INFO", "Start CPU Load observer");
        if(cpuLoadPaused) {
            cpuLoadPaused = false;
            handler.post(runnable);
        }
    }

    @Override
    public void stopCpuLoadObserver() {
        Log.d("PROCESSOR-INFO", "Stop CPU Load observer");
        this.cpuLoadPaused = true;
        handler.removeCallbacks(runnable);
    }

    private void observeCPULoad() {
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if(cpuLoadPaused)
                    return ;

                if (onCPULoadChangeListener == null || onCPULoadChangeListener.size() != getNumCores()) {
                    Log.d("CPU-OBSERVER-INFO", "cpu load error - skip");
                    handler.postDelayed(this, 1000);
                    return;
                }

                if (--secondCounter == 0) {
                    System.gc();
                    secondCounter = 15;
                    Log.d("DEBUG-INFO", "Elapsed 15 seconds, running garbage collection");
                }

                String[] cpuLoad = getCpuLoad();
                if(cpuTempListener != null) {
                    String cpuTemp = getCpuTemp() + " °C";
                    cpuTempListener.onValueChange(cpuTemp, Utils.LISTENER_CPU_TEMP);
                }

                for (int i = 0; i < getNumCores(); i++) {
                    onCPULoadChangeListener.get(i).onValueChange(cpuLoad[i], Utils.LISTENER_CPU_LOAD + i);
                }

                handler.postDelayed(this, 1000);

            }
        };

        activity.runOnUiThread(runnable);
    } */


    /*
    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {

        ArrayList<TextTitleValuePair> list = super.getTitleValuePair();
        list.add(cpuTempTvp);

        return list;
    }*/

     /*
    private ImageTitleValuePair getHardwareImageTitleValuePair() {
        SoCInfo soc = SocDetector.getInstance().getSoC();
        Bitmap image = BitmapFactory.decodeResource(activity.getResources(), soc.getAssociatedImageDrawable());

        return new StaticImageTitleValuePair("Powered by", image);
    } */
    /*  try {
                Process p = Runtime.getRuntime().exec("getprop | grep abi");
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while((line = br.readLine()) != null) {
                    String[] splitted = line.split(":");
                    if (splitted[0].contains("product.cpu.abi"))
                        if(splitted[1].toLowerCase().contains("v8a"))
                }
            } catch (IOException e) {
                e.printStackTrace();
            } */

}
