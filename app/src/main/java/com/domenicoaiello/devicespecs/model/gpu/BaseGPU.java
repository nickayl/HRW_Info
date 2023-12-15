package com.domenicoaiello.devicespecs.model.gpu;

import android.app.Activity;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.os.Handler;
import android.util.Log;

import com.domenicoaiello.devicespecs.model.titlevaluepair.StaticTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;

import java.util.ArrayList;

/**
 * Created by Domenico on 08/10/2017.
 */

public abstract class BaseGPU implements GPU  {

    protected Activity activity;

    private String renderer;
    private String vendor;
    private String openGLVersion;

    public BaseGPU(Activity activity) throws IllegalArgumentException {
        if (activity == null)
            throw new IllegalArgumentException("Activity cannot be null");

        this.activity = activity;
        initialize();
    }

    protected void initialize() {

        try {
            EGLDisplay dpy = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
            int[] vers = new int[2];
            EGL14.eglInitialize(dpy, vers, 0, vers, 1);

            int[] configAttr = {
                    EGL14.EGL_COLOR_BUFFER_TYPE, EGL14.EGL_RGB_BUFFER,
                    EGL14.EGL_LEVEL, 0,
                    EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                    EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                    EGL14.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfig = new int[1];
            EGL14.eglChooseConfig(dpy, configAttr, 0,
                    configs, 0, 1, numConfig, 0);
            if (numConfig[0] == 0) {
                // TROUBLE! No config found.
            }

            int[] surfAttr = {
                    EGL14.EGL_WIDTH, 64,
                    EGL14.EGL_HEIGHT, 64,
                    EGL14.EGL_NONE
            };
            EGLSurface surf = EGL14.eglCreatePbufferSurface(dpy, configs[0], surfAttr, 0);

            int[] ctxAttrib = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL14.EGL_NONE
            };
            EGLContext ctx = EGL14.eglCreateContext(dpy, configs[0], EGL14.EGL_NO_CONTEXT, ctxAttrib, 0);
            EGL14.eglMakeCurrent(dpy, surf, surf, ctx);

            Log.d("GL", "GL_RENDERER = " + GLES20.glGetString(GLES20.GL_RENDERER));
            Log.d("GL", "GL_VENDOR = " + GLES20.glGetString(GLES20.GL_VENDOR));
            Log.d("GL", "GL_VERSION = " + GLES20.glGetString(GLES20.GL_VERSION));
            Log.i("GL", "GL_EXTENSIONS = " + GLES20.glGetString(GLES20.GL_EXTENSIONS));


            this.renderer = GLES20.glGetString(GLES20.GL_RENDERER);
            this.vendor = GLES20.glGetString(GLES20.GL_VENDOR);
            this.openGLVersion = GLES20.glGetString(GLES20.GL_VERSION);

        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

/*
    protected void listenLoadChanges(Activity activity) {

        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String perc = getCurrentLoadPercent();

                if (onLoadChangeListener != null)
                    onLoadChangeListener.onValueChange(perc + "%", Utils.LISTENER_GPU_LOAD);

                Log.d("GPU-INFO", "notify listener (GPUInfo) gpu load:" + perc);

                if (Utils.cancelTask) {
                    handler.removeCallbacks(this);
                    Log.d("debug", "task canceled");
                } else
                    handler.postDelayed(this, 2000);
            }
        };

        activity.runOnUiThread(runnable);
    }*/

    @Override
    public String getOpenGLVersion() {
        return openGLVersion;
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public String getRenderer() {
        return renderer;
    }

/*
    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {
        ArrayList<TextTitleValuePair> list = new ArrayList<>();

        list.add(new StaticTextTitleValuePair("GPU Vendor", getVendor()));
        list.add(new StaticTextTitleValuePair("GPU Model", getRenderer()));

        return list;
    }
 */
}
