package com.domenicoaiello.socdetector;

import android.content.Context;
import android.support.annotation.DrawableRes;

/**
 * Created by Domenico on 26/10/2017.
 */

public interface SoCInfo {

    String getBrand();

    String getModel();
    String getCode();
    String getFullRetailBrandingName();

    String getPopupDescription(Context context);

    @DrawableRes
    int getAssociatedImageDrawable();
}
