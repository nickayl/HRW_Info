package com.domenicoaiello.devicespecs.model.gpu;

import com.domenicoaiello.devicespecs.model.titlevaluepair.ValuePairProvider;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

/**
 * Created by Domenico on 08/10/2017.
 */

public interface GPU {// extends ValuePairProvider {

    String getOpenGLVersion();
    String getRenderer();
    String getVendor();
    String getCurrentLoadPercent();
    String getMaxClock();

    /**
     * Assign a listener to monitor changes in GPU Load (in percent)
     * @param newLoadValue The Listener to send the new values.
     */
    void setOnLoadChangeListener(onValueChangeListener newLoadValue);
    onValueChangeListener getOnLoadChangeListener();
}
