package com.domenicoaiello.devicespecs.model;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.domenicoaiello.devicespecs.R;
import com.domenicoaiello.devicespecs.model.titlevaluepair.VariableTextWithQuestionMarkTvp;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;
import com.domenicoaiello.devicespecs.model.valuechangelistener.ListenerPoolExecutor;
import com.domenicoaiello.devicespecs.model.valuechangelistener.SystemInfoListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Domenico on 03/10/2017.
 */

public class SystemInfo {

    private final VariableTextWithQuestionMarkTvp vtp;
    private Activity activity;
    private static onValueChangeListener uptimeChangeListener;

    private String androidVersion;
    private String apiLevel;
    private String securityPatchLevel;
    private String bootLoader;
    private String buildId;
    private String javaVM;
    private String openGL;
    private String kernelArchitecture;
    private String kernelVersion;
    private String rootAccess;

    public SystemInfo(Activity activity) {
        this.activity = activity;
      //  calculateSystemUptime();
        vtp = new VariableTextWithQuestionMarkTvp("System Uptime","",Utils.SYSTEM_UPTIME_LISTENER,activity.getString(R.string.system_uptime_description));
        setUptimeChangeListener(vtp);

        try {
            ListenerPoolExecutor.getInstance().addListener(new SystemInfoListener(uptimeChangeListener));
        } catch(RuntimeException e) {
            e.printStackTrace();
            Log.e("SystemInfoException", e.getMessage());
        }

        initialize();
    }

    private void initialize() {

        androidVersion = Build.VERSION.RELEASE;
      //  apiLevel = String.valueOf(Build.VERSION.SDK_INT);
        apiLevel = Build.VERSION.SDK;
        securityPatchLevel = retriveSecurityPatchLevel();
        bootLoader = Build.BOOTLOADER;
        buildId = Build.ID;
        javaVM = System.getProperty("java.vm.name") + " " +System.getProperty("java.vm.version");
        openGL = HardwareDetector.getGPU(activity).getOpenGLVersion().replace("OpenGL ES","");
        kernelArchitecture = System.getProperty("os.arch");
        kernelVersion = System.getProperty("os.name")+ " "+ System.getProperty("os.version");

        String rootCheck = activity.getString(R.string.no);
        try {
            String root = Utils.readSystemFile("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq");
            Integer.valueOf(root);
            rootCheck = activity.getString(R.string.yes);
        } catch(Exception e) {
            e.printStackTrace();
        }

        rootAccess = rootCheck;
    }

    private String retriveSecurityPatchLevel() {
        String str = "";

        try {
            Process process = new ProcessBuilder()
                    .command("/system/bin/getprop")
                    .redirectErrorStream(true)
                    .start();

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                str += line + "\n";
                if(str.contains("security_patch")) {
                    String[] splitted = line.split(":");
                    if(splitted.length == 2) {
                        return splitted[1];
                    }
                    break;
                }
            }

            br.close();
            process.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public VariableTextWithQuestionMarkTvp getVtp() {
        return vtp;
    }

    private void setUptimeChangeListener(onValueChangeListener uptimeChangeListener) {
        SystemInfo.uptimeChangeListener = uptimeChangeListener;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public String getBootLoader() {
        return bootLoader;
    }

    public String getBuildId() {
        return buildId;
    }

    public String getJavaVM() {
        return javaVM;
    }

    public String getOpenGL() {
        return openGL;
    }

    public String getKernelArchitecture() {
        return kernelArchitecture;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getRootAccess() {
        return rootAccess;
    }

    public String getSecurityPatchLevel() {
        return securityPatchLevel;
    }


    /* @Override
     public ArrayList<TextTitleValuePair> getTitleValuePair() {
         ArrayList<TextTitleValuePair> list = new ArrayList<>();

         list.add(new StaticTextTitleValuePair("Android Version", Build.VERSION.RELEASE));
         list.add(new StaticTextTitleValuePair("API Level", Build.VERSION.SDK));


         if(pair != null) {
             list.add(new StaticTextTitleValuePair("Security Patch Level", pair.getValue()));
         }

         list.add(new StaticTextTitleValuePair("Bootloader", Build.BOOTLOADER));
         list.add(new StaticTextTitleValuePair("Build ID", Build.ID));
         list.add(new StaticTextTitleValuePair("Java VM",  System.getProperty("java.vm.name") + " " +System.getProperty("java.vm.version")));
         list.add(new StaticTextTitleValuePair("OpenGL ES", HardwareDetector.getGPU(activity).getOpenGLVersion().replace("OpenGL ES","")));
         list.add(new StaticTextTitleValuePair("Kernel Architecture", System.getProperty("os.arch")));
         list.add(new StaticTextTitleValuePair("Kernel version", System.getProperty("os.name")+ " "+ System.getProperty("os.version")));

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

         list.add(vtp);

         return list;
     }*/
}

