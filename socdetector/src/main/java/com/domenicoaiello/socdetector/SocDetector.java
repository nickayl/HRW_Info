package com.domenicoaiello.socdetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 26/10/2017.
 */

public class SocDetector {

    private static SocDetector instance;
    private SoCInfo soc;

    private SocDetector() {

    }
    public SoCInfo getSoC() {
        if(soc != null)
            return soc;

        List<Checker> list = new ArrayList<>(8);
        list.add(new EmulatorSoC());
        list.add(new QualcommSoC());
        list.add(new SamsungSoc());
        list.add(new MediaTekSoC());
        list.add(new HiSiliconSoC());
        list.add(new IntelSoC());
        list.add(new NvidiaSoc());

        for(Checker soc : list)
            if(soc.check())
                return this.soc = (SoCInfo) soc;

        list.clear();
        return null;
    }

    public static SocDetector getInstance() {
        if(instance == null)
            instance = new SocDetector();
        return instance;
    }

}
