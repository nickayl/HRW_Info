package com.domenicoaiello.devicespecs.model.valuechangelistener;

public interface Listener {
    Runnable getRunnable();
    int getIdentifier();
    long getDelay();
}
