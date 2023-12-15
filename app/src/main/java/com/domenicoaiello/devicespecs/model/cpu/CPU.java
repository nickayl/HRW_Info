package com.domenicoaiello.devicespecs.model.cpu;

import com.domenicoaiello.devicespecs.model.titlevaluepair.ImageValuePairProvider;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ValuePairProvider;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

import java.util.List;

/**
 * Created by Domenico on 08/10/2017.
 */

public interface CPU {//extends ValuePairProvider, ImageValuePairProvider {

    String getName();
    //String getClock();
    String[] getCpuLoad();
    String getCpuTemp();
    Integer getNumCores();
    String getArchitecture();
    String getScalingGovernor();
    String getSoCModel();
    List<Clusters> getClusters();

    void addOnCpuLoadChangeListener(onValueChangeListener valueChangeListener);
  //  void startCpuLoadObserver();
    //void stopCpuLoadObserver();

}
