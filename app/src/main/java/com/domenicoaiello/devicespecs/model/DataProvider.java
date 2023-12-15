package com.domenicoaiello.devicespecs.model;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.domenicoaiello.devicespecs.IDataProvider;
import com.domenicoaiello.devicespecs.R;
import com.domenicoaiello.devicespecs.model.cpu.CPU;
import com.domenicoaiello.devicespecs.model.cpu.Clusters;
import com.domenicoaiello.devicespecs.model.cpu.DefaultCPU;
import com.domenicoaiello.devicespecs.model.gpu.GPU;
import com.domenicoaiello.devicespecs.model.titlevaluepair.BaseTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextWithQuestionMarkTvp;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.VariableTextTitleValuePair;
import com.domenicoaiello.socdetector.SocDetector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Domenico on 12/11/2017.
 */

public class DataProvider implements IDataProvider<List<TextTitleValuePair>> {

    private Activity activity;
    private Context context;

    public DataProvider(Activity activity) throws IllegalArgumentException {
        if(activity == null)
            throw new IllegalArgumentException("Activity cannot be null on "+this.getClass().getName());

        this.activity = activity;
        this.context = activity;
    }


    @Override
    public List<TextTitleValuePair> getProcessorData() {

        List<TextTitleValuePair> titleValuePairs = new ArrayList<>();
        DefaultCPU processor = new DefaultCPU(activity);

        // -- CPU -- //
        StaticTextWithQuestionMarkTvp stvp = new StaticTextWithQuestionMarkTvp("Processor", processor.getName());
        //  stvp.setOption(BaseTitleValuePair.OPTIONS_HIDE_QUESTIONMARK_BOX);
        stvp.setTextDescription(SocDetector.getInstance().getSoC().getPopupDescription(activity));
        titleValuePairs.add(stvp);

        String model = processor.getSoCModel();
        if(model != null)
            titleValuePairs.add(new StaticTextTitleValuePair("Branding name", model));

        titleValuePairs.add(new StaticTextTitleValuePair("Cores", String.valueOf(processor.getNumCores())));
        titleValuePairs.addAll(processor.getCpuLoadListener());
        if(processor.getCpuTempTvp() != null) {
            titleValuePairs.add(processor.getCpuTempTvp());
        }

        //    titleValuePairs.add(new StaticTextTitleValuePair("Clock Speed", getClock()));

        for(int i=0; i < processor.getClusters().size(); i++) {
            Clusters c = processor.getClusters().get(i);
            TextTitleValuePair tvp;
            if(i == 0) {
                tvp = new StaticTextWithQuestionMarkTvp("Cluster " + (i + 1), String.format("%sx %s - %s", c.getNumCore(), c.getMinFreq(), c.getMaxFreq()));
                ((StaticTextWithQuestionMarkTvp) tvp).setTextDescription(activity.getResources().getString(R.string.clusters_description));
            }
            else
                tvp = new StaticTextTitleValuePair("Cluster " + (i + 1), String.format("%sx %s - %s", c.getNumCore(), c.getMinFreq(),c.getMaxFreq()));

            titleValuePairs.add(tvp);
        }

        StaticTextWithQuestionMarkTvp stvpScalingGovernor = new StaticTextWithQuestionMarkTvp("Scaling Governor", processor.getScalingGovernor());
        stvpScalingGovernor.setTextDescription(activity.getString(R.string.scaling_governor_generic));
        titleValuePairs.add(stvpScalingGovernor);

        return titleValuePairs;
    }

    @Override
    public List<TextTitleValuePair> getDeviceData() {
        Device d = new Device(activity);

        List<TextTitleValuePair> list = new ArrayList<>();

        if(d.getCommercialName() != null)
            list.add(new StaticTextTitleValuePair("Commercial Name", d.getCommercialName()));

        list.add(new StaticTextWithQuestionMarkTvp("Model", d.getModel(), context.getString(R.string.device_model_description)));
        list.add(new StaticTextTitleValuePair("Brand", d.getBrand()));
        list.add(new StaticTextWithQuestionMarkTvp("Hardware", d.getHardware(), context.getString(R.string.hardware_description) ));

        if(!d.getHardware().equalsIgnoreCase(d.getBoard()))
            list.add(new StaticTextTitleValuePair("Board", d.getBoard()));

        list.add(new StaticTextTitleValuePair("Total RAM", d.getTotalRAM()));
        list.add(new StaticTextTitleValuePair("Available RAM", d.getAvailRam() + " ("+d.getAvailRamPercent()+"%)"));
        list.add(new StaticTextTitleValuePair("Total Storage", d.getTotalStorage()));
        list.add(new StaticTextTitleValuePair("Available Storage", d.getAvailableStorage() + " ("+d.getAvailStoragePercent()+"%)"));

        return list;
    }

    @Override
    public List<TextTitleValuePair> getSystemData() {
        SystemInfo systemInfo = new SystemInfo(activity);

        List<TextTitleValuePair> list = new ArrayList<>();

        list.add(new StaticTextTitleValuePair("Android Version", systemInfo.getAndroidVersion()));
        list.add(new StaticTextTitleValuePair("API Level", systemInfo.getApiLevel()));

        if(systemInfo.getSecurityPatchLevel().length() != 0)
            list.add(new StaticTextTitleValuePair("Security Patch Level", systemInfo.getSecurityPatchLevel()));


        list.add(new StaticTextTitleValuePair("Bootloader", systemInfo.getBootLoader()));
        list.add(new StaticTextTitleValuePair("Build ID", systemInfo.getBuildId()));
        list.add(new StaticTextTitleValuePair("Java VM",  systemInfo.getJavaVM()));
        list.add(new StaticTextTitleValuePair("OpenGL ES", systemInfo.getOpenGL()));
        list.add(new StaticTextTitleValuePair("Kernel Architecture", systemInfo.getKernelArchitecture()));
        list.add(new StaticTextTitleValuePair("Kernel version", systemInfo.getKernelVersion()));

        // if cpuinfo_cur_freq is readable, root access is on. Otherwise, the device is not rooted. (This special file is read-only even to superuser)
        String rootCheck = activity.getString(R.string.no);
        try {
            String root = Utils.readSystemFile("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq");
            Integer.valueOf(root);
            rootCheck = activity.getString(R.string.yes);
        } catch(Exception e) {
            e.printStackTrace();
        }

        list.add(new StaticTextTitleValuePair("Root Access", rootCheck));

        list.add(systemInfo.getVtp());

        return list;
    }

    @Override
    public List<TextTitleValuePair> getScreenData() {
        Screen s = new Screen(context);

        List<TextTitleValuePair> list = new ArrayList<>();

        list.add(new StaticTextTitleValuePair("Screen Resolution", s.getResolution()));
        list.add(new StaticTextTitleValuePair("Screen Density", s.getDensityDpi()));
        list.add(new StaticTextTitleValuePair("Screen Size", s.getSizeInInches()));

        return list;
    }

    @Override
    public List<TextTitleValuePair> getBatteryData() {
        return null;
    }

    @Override
    public List<TextTitleValuePair> getGpuData() {
        GPU gpu = HardwareDetector.getGPU(activity);

        ArrayList<TextTitleValuePair> list = new ArrayList<>();

        list.add(new StaticTextTitleValuePair("GPU Vendor", gpu.getVendor()));
        list.add(new StaticTextTitleValuePair("GPU Model", gpu.getRenderer()));

        String maxClock = gpu.getMaxClock();
        String currentLoad = gpu.getCurrentLoadPercent();

        if(maxClock != null && !maxClock.equals(Utils.UNKNOWN))
            list.add(new StaticTextTitleValuePair("Max GPU Clock", maxClock));

        if(currentLoad != null && !currentLoad.equals(Utils.UNKNOWN)) {
           // list.add(gpu.getOnLoadChangeListener())
            if(gpu.getOnLoadChangeListener() instanceof TextTitleValuePair)
                list.add((TextTitleValuePair) gpu.getOnLoadChangeListener());
        }

        return list;
    }
}
