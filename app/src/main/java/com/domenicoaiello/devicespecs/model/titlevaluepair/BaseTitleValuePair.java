package com.domenicoaiello.devicespecs.model.titlevaluepair;

import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;

/**
 * Created by Domenico on 28/09/2017.
 */

public interface BaseTitleValuePair<T> {
    String TYPE_TEXT = "TEXT";
    String TYPE_IMAGE = "IMAGE";

    int OPTIONS_NONE = -1;
    int OPTIONS_HIDE_QUESTIONMARK_BOX = 0;

    String getTitle();
    T getValue();
    String getCode();

    void setValue(T value);
    void setCode(String code);

    @LayoutRes
    int getAssociatedLayout();

    int getOptions();



    /**
     *
     * @return The type of the pair (TYPE_TEXT or TYPE_IMAGE)
     */
    String getPairType();
}
