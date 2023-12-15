package com.domenicoaiello.devicespecs.model.valuechangelistener;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 12/11/2017.
 */

public class ListenerPoolExecutor {

    private List<Listener> listeners = new ArrayList<>();

    private static ListenerPoolExecutor instance;
    private static Handler handler;

    private ListenerPoolExecutor() {
        handler = new Handler();
    }

    public static ListenerPoolExecutor getInstance() {
        if(instance == null)
            instance = new ListenerPoolExecutor();

        return instance;
    }

    public void addListener(Listener listener) throws RuntimeException {
        for(Listener l : listeners)
            if(l.getIdentifier() == listener.getIdentifier())
                throw new RuntimeException("Attempt to add a listener that is already attached to the listener pool executor");

        listeners.add(listener);
    }

    // TODO : Carefully test this  method. (Array modification inside for-each loop)
    public void removeListener(int identifier) {
        for(Listener l : listeners) {
            if (l.getIdentifier() == identifier) {
                listeners.remove(l);
                break;
            }
        }
    }

    public void executeAll() {
        for(Listener l : listeners)
            handler.postDelayed(l.getRunnable(), l.getDelay());
    }

    public void suspendAll() {
        for(Listener l : listeners)
            handler.removeCallbacks(l.getRunnable());

        System.gc();
    }

    public void removeAll() {
        listeners.clear();
        System.gc();
    }

    public static abstract class ListenerRunnable implements Listener {

        @Override
        public abstract Runnable getRunnable();

        @Override
        public abstract int getIdentifier();

        @Override
        public long getDelay() {
            return 1000;
        }


        protected synchronized void repeat() {
            ListenerPoolExecutor.handler.postDelayed(getRunnable(), getDelay());
        }

      /*  @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Listener)
                if(((Listener) obj).getIdentifier() == getIdentifier())
                    return true;

            return false;
        } */
    }
}

