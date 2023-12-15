package com.domenicoaiello.devicespecs.model;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.domenicoaiello.devicespecs.R;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextWithQuestionMarkTvp;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ValuePairProvider;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Domenico on 28/09/2017.
 */

public class Device  {

    private String model;
    private String brand;
    private String board;
    private String totalRAM;
    private String availRam;
    private String availRamPercent;
    private String totalStorage;
    private String availableStorage;
    private String availStoragePercent;
    private String commercialName;
    private Context context;
    private String hardware;

    public Device(Context context) {
        this.context = context;
        calculate();
    }

    private void calculate() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)  context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        double totalMegs = mi.totalMem / 0x100000L;

        //Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;
        availRamPercent = String.format("%.2f",percentAvail);

        if(availableMegs > 1024)
            availRam = String.format("%.2f",availableMegs/1024) + " GB";
        else
            availRam = String.valueOf(Math.round(availableMegs)) + " MB";

        if(totalMegs > 1024)
            totalRAM = String.format("%.2f",totalMegs/1024) + " GB";
        else
            totalRAM = String.valueOf(Math.round(totalMegs)) + " MB";

        model = Build.MODEL;
        brand = Utils.capitalize(Build.BRAND.toLowerCase())+"™";
        board = Build.BOARD;
        hardware = Build.HARDWARE;

        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        double bytesTotal = stat.getBlockSizeLong() * stat.getBlockCountLong();
        double megTotal = bytesTotal / 1048576;

        double bytesAvailable = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        double megAvailable = bytesAvailable / 1048576;

        totalStorage = String.format("%.2f",megTotal/1024) + " GB";

        if(megAvailable < 1024)
            availableStorage = String.format("%.0f",megAvailable) + "MB";
        else
            availableStorage = String.format("%.2f",megAvailable/1024) + "GB";

        availStoragePercent = String.format("%.2f", (megAvailable/ (double)megTotal) * 100.0);
    }

    public String getCommercialName() {
        if(commercialName != null)
            return commercialName;

        try {
            InputStream is = context.getAssets().open("text/text2.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line, str = "";

            while((line = br.readLine()) != null) {
                String[] splitted = line.split(",");

                if(splitted.length == 4) {
                    String retailBranding = splitted[0];
                    String marketingName = splitted[1];
                    String model = splitted[3];

                    if (model.equalsIgnoreCase(this.model) && retailBranding.equalsIgnoreCase(Build.BRAND))
                        return commercialName = retailBranding + "™ " + marketingName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
/*
    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {
        ArrayList<TextTitleValuePair> list = new ArrayList<>();

        if(commercialName.length() != 0)
            list.add(new StaticTextTitleValuePair("Commercial Name", commercialName));

        list.add(new StaticTextWithQuestionMarkTvp("Model", model, context.getString(R.string.device_model_description)));
        list.add(new StaticTextTitleValuePair("Brand", brand));
        list.add(new StaticTextWithQuestionMarkTvp("Hardware", hardware, context.getString(R.string.hardware_description) ));

        if(!hardware.equalsIgnoreCase(board))
            list.add(new StaticTextTitleValuePair("Board", board));

        list.add(new StaticTextTitleValuePair("Total RAM", totalRAM));
        list.add(new StaticTextTitleValuePair("Available RAM", availRam + " ("+availRamPercent+"%)"));
        list.add(new StaticTextTitleValuePair("Total Storage", totalStorage));
        list.add(new StaticTextTitleValuePair("Available Storage", availableStorage + " ("+availStoragePercent+"%)"));

        return list;
    }
 */

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getBoard() {
        return board;
    }

    public String getTotalRAM() {
        return totalRAM;
    }

    public String getAvailRam() {
        return availRam;
    }

    public String getAvailRamPercent() {
        return availRamPercent;
    }

    public String getTotalStorage() {
        return totalStorage;
    }

    public String getAvailableStorage() {
        return availableStorage;
    }

    public String getAvailStoragePercent() {
        return availStoragePercent;
    }

    public String getHardware() {
        return hardware;
    }
}
