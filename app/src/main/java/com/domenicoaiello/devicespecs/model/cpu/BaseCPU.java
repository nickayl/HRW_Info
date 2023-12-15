package com.domenicoaiello.devicespecs.model.cpu;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.domenicoaiello.devicespecs.R;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ImageTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticImageTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextWithQuestionMarkTvp;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.titlevaluepair.VariableTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;
import com.domenicoaiello.socdetector.SoCInfo;
import com.domenicoaiello.socdetector.SocDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 08/10/2017.
 */

public abstract class BaseCPU implements CPU  {

    protected ArrayList<TextTitleValuePair> rawCpuInfo = new ArrayList<>();
    //private String clock;
    private Activity activity;
   // private ArrayList<TextTitleValuePair> titleValuePairs = new ArrayList<>(10);

    private List<Clusters> clusters = new ArrayList<>(4);

    public BaseCPU(Activity activity) {
        if(activity == null)
            throw new IllegalArgumentException("Activity cannot be null on DefaultCPU class constructor");

        this.activity = activity;
        getRawDataFromCpuInfoCommand();
        calculateClusters();
    }

    private void getRawDataFromCpuInfoCommand() {
        try {
            Process proc = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStream is = proc.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            while((line = br.readLine()) != null) {
                String[] splitted = line.split(":");
                if(splitted.length == 2)
                    rawCpuInfo.add(new StaticTextTitleValuePair(splitted[0], splitted[1]));
            }

            br.close();
            proc.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<Clusters> getClusters() {
        return clusters;
    }

    private void calculateClusters() {
        StringBuilder cpuMinCommand = new StringBuilder();
        StringBuilder cpuMaxCommand = new StringBuilder();

        for (int i = 0; i < getNumCores(); i++) {
            cpuMinCommand.append("/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_min_freq ");
            cpuMaxCommand.append("/sys/devices/system/cpu/cpu" + i + "/cpufreq/cpuinfo_max_freq ");
        }

        try {
            double[] mins = getValuesFromCommand(cpuMinCommand.toString());
            double[] maxs = getValuesFromCommand(cpuMaxCommand.toString());

            Clusters c2 = new Clusters();
            c2.setMaxFreq(maxs[0]);
            c2.setMinFreq(mins[0]);
            c2.setNumCore(1);
            clusters.add(c2);

            for (int i = 1; i < mins.length; i++) {
                if (mins[i] != 0 && mins[i] != mins[i - 1])
                    clusters.add(new Clusters());

                Clusters c = clusters.get(clusters.size() - 1);
                if (maxs[i] != 0)
                    c.setMaxFreq(maxs[i]);
                if (mins[i] != 0) {
                    c.setMinFreq(mins[i]);
                }
                c.setNumCore(c.getNumCore() + 1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private double[] getValuesFromCommand(String cpuFreqCommand) {
        double values[] = new double[getNumCores()];

        try {
            Process process = Runtime.getRuntime().exec("cat "+cpuFreqCommand);
            InputStream in = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            for(int i=0; i < getNumCores(); i++) {
                line = br.readLine();
                if (line != null && line.matches("[0-9]+")) {
                    values[i] = Double.valueOf(line)/1000;
                }
            }

            in.close();
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return values;
    }

    @Override
    public String getArchitecture() {
        return System.getProperty("os.arch");
    }

    @Override
    public String getScalingGovernor() {
        return Utils.readSystemFile("cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");
    }

    @Override
    public abstract Integer getNumCores();

    /*
    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {
        if(titleValuePairs.size() == 0) {

            // -- CPU -- //
            StaticTextWithQuestionMarkTvp stvp = new StaticTextWithQuestionMarkTvp("Processor", getName());
          //  stvp.setOption(BaseTitleValuePair.OPTIONS_HIDE_QUESTIONMARK_BOX);
            stvp.setTextDescription(SocDetector.getInstance().getSoC().getPopupDescription(activity));
            titleValuePairs.add(stvp);

            TextTitleValuePair model = getSoCModel();
            if(model != null)
                titleValuePairs.add(model);

            titleValuePairs.add(new StaticTextTitleValuePair("Cores", String.valueOf(getNumCores())));
        //    titleValuePairs.add(new StaticTextTitleValuePair("Clock Speed", getClock()));

            for(int i=0; i < clusters.size(); i++) {
                Clusters c = clusters.get(i);
                TextTitleValuePair tvp;
                if(i == 0) {
                    tvp = new StaticTextWithQuestionMarkTvp("Cluster " + (i + 1), String.format("%sx %s - %s", c.getNumCore(), c.getMinFreq(), c.getMaxFreq()));
                    ((StaticTextWithQuestionMarkTvp) tvp).setTextDescription(activity.getResources().getString(R.string.clusters_description));
                }
                else
                    tvp = new StaticTextTitleValuePair("Cluster " + (i + 1), String.format("%sx %s - %s", c.getNumCore(), c.getMinFreq(),c.getMaxFreq()));

                titleValuePairs.add(tvp);
            }
            StaticTextWithQuestionMarkTvp stvpScalingGovernor = new StaticTextWithQuestionMarkTvp("Scaling Governor", getScalingGovernor());
            stvpScalingGovernor.setTextDescription(activity.getString(R.string.scaling_governor_generic));
            titleValuePairs.add(stvpScalingGovernor);
        }

        return titleValuePairs;
    }

    @Override
    public String toString() {
        String str = "";

        if(rawCpuInfo.size() == 0)
            rawCpuInfo = getTitleValuePair();

        for(TextTitleValuePair tvp : rawCpuInfo)
            str += tvp.getTitle() + "  " + tvp.getValue() +"\n";

        return str;
    } */


   /* @Override
    public ArrayList<ImageTitleValuePair> getImageValuePairProvider() {
        ImageTitleValuePair itvp = getHardwareImageTitleValuePair();

        ArrayList<ImageTitleValuePair> arr = new ArrayList<>();

        if(itvp != null)
            arr.add(itvp);

        return arr;
    } */

   /*
    @Override
    public String getClock() {
        if(clock != null)
            return clock;

        try {
            Float cpu_max_freq = Float.valueOf(Utils.readSystemFile("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
            Float cpu_min_freq = Float.valueOf(Utils.readSystemFile("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"));

            cpu_max_freq /= 1000;
            cpu_min_freq /= 1000;

            String cpu_max_freq_string = String.format("%.0f MHz", cpu_max_freq);
            String cpu_min_freq_string = String.format("%.0f MHz", cpu_min_freq);

            if (cpu_max_freq > 1000) {
                cpu_max_freq /= 1000;
                cpu_max_freq_string = String.format("%.2f GHz", cpu_max_freq);
            }

            if (cpu_min_freq > 1000) {
                cpu_min_freq /= 1000;
                cpu_min_freq_string = String.format("%.2f GHz", cpu_min_freq);

            }
            clock = String.format("%s - %s", cpu_min_freq_string, cpu_max_freq_string);
            return clock;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return Utils.UNKNOWN;
    } */


}
