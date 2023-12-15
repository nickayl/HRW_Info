package com.domenicoaiello.socdetector;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;


import java.util.Locale;

/**
 * Created by Domenico on 26/10/2017.
 */

abstract class DefaultSoc implements SoCInfo, Checker {

    public static final String UNKNOWN = "UNKNOWN";

    private String model;
    private String code;
    private String brand;

    @DrawableRes
    private int drawableResource;

    DefaultSoc() {
        brand = UNKNOWN;
        model = UNKNOWN;
        code = UNKNOWN;
    }

    @Override
    public abstract boolean check();


    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    @DrawableRes
    public int getAssociatedImageDrawable() {
        return drawableResource;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getFullRetailBrandingName() {
        return String.format(Locale.getDefault(), "%s %s %s", getBrand(),getModel(),getCode());
    }

    @Override
    public abstract String getPopupDescription(Context context);

    /**
     * @param pattern A regular expression used to detect the soc model
     * @return The detected model string or null.
     */
    protected String regexCheck(String pattern) {
        String detectedModel = null;
        String hw = Build.HARDWARE.toLowerCase();
        String board = Build.BOARD.toLowerCase();

        if(hw.matches(pattern))
            detectedModel = hw;
        else if(board.matches(pattern))
            detectedModel = board;

        return detectedModel;
    }

    protected void setModel(String model) {
        this.model = model;
    }

    protected void setCode(String code) {
        this.code = code;
    }

    protected void setBrand(String brand) {
        this.brand = brand;
    }

    protected void setDrawableResource(@DrawableRes int drawableResource) {
        this.drawableResource = drawableResource;
    }
}
