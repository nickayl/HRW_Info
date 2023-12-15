package com.domenicoaiello.simplepopup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Domenico on 27/10/2017.
 */

abstract class BasePopup<T> implements Popup<T> {

    private Activity activity;

    @LayoutRes
    private int popupLayoutResource;
    @IdRes
    private int mainActivityRootView;

    protected View popupView;
    private View attachedView;
    private ViewGroup rootView;

    private int marginX=0;
    private int marginY=10;

    protected final int displayWidth;
    protected final int displayHeight;

    BasePopup(Activity activity, @LayoutRes int popupLayoutResource, @IdRes int mainActivityRootView, View attachedView) throws IllegalArgumentException {
        if(activity == null)
            throw new IllegalArgumentException("Activity cannot be null.");
        else if(attachedView == null)
            throw new IllegalArgumentException("attachedView cannot be null.");

        this.activity = activity;
        this.popupLayoutResource = popupLayoutResource;
        this.mainActivityRootView = mainActivityRootView;
        this.attachedView = attachedView;
        initialize();

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();

        wm.getDefaultDisplay().getSize(size);
        displayWidth = size.x;
        displayHeight = size.y;
    }

    public BasePopup(Activity activity, @IdRes int mainActivityRootView, View attachedView) {
        this(activity,R.layout.questionmark_popup,mainActivityRootView, attachedView);
    }

    @Override
    public abstract void setContent(T content);

    protected void initialize() {
        try {
            popupView = LayoutInflater
                    .from(activity)
                    .inflate(popupLayoutResource, null);

            popupView.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(pxFromDp(180),pxFromDp(220));
          //  ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupView.setLayoutParams(params);

            rootView = (ViewGroup) activity.findViewById(mainActivityRootView);
            if(rootView == null)
                throw new NullPointerException("rootView is null (invalid id resource)");

            rootView.addView(popupView);
        } catch(InflateException e) {
            e.printStackTrace();
            Log.e("POPUP-ERROR", "Exception initializing popup: LayoutInflater error - cannot inflate the provided layout resource id"+e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("POPUP-ERROR", "Exception initializing popup: "+e.getMessage());
        }
    }

    private void setTouchListener() {
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("POPUP-DEBUG", "onTouchDetected");
                float x = event.getX();
                float y = event.getY();

                float popX = popupView.getX();
                float popY = popupView.getY();

                if(x >= popX && x <= (popX + popupView.getWidth())
                        && y >= popY && y <= (popY + popupView.getHeight()))
                    return false;

                if(isVisible()) {
                    hide();
                    return true;
                }

                return false;
            }
        });
    }

    // ----- Interface methods ------//
    @Override
    public void toggle() {
        if(isVisible())
            hide();
        else
            show();
    }

    @Override
    public void show() {
        setTouchListener();
        int[] location = new int[2];
        attachedView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        int xPos;
        int yPos;

        if(x + (popupView.getWidth() + attachedView.getWidth() + marginX) < displayWidth)
            xPos = x + attachedView.getWidth() + marginX;
        else
            xPos = x - (popupView.getWidth() + marginX);


        if(y + (popupView.getHeight() + attachedView.getHeight() + marginY) < displayHeight)
            yPos = y + marginY;
        else
            yPos = y - (popupView.getHeight() + attachedView.getHeight() + marginY);

        popupView.setX(xPos);
        popupView.setY(yPos);
        popupView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        rootView.setOnTouchListener(null);
        popupView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void show(int x, int y) {
        setTouchListener();
        popupView.setX(x);
        popupView.setY(y);
        popupView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setMarginX(int marginX) {
        this.marginX = marginX;
        show();
    }

    @Override
    public void setMarginY(int marginY) {
        this.marginY = marginY;
        show();
    }

    private boolean isVisible() {
        return this.popupView.getVisibility() == View.VISIBLE;
    }

    public void setAttachedView(View view) {
        attachedView = view;
    }

    public View getPopupView() {
        return popupView;
    }

    protected int dpFromPx(float px) {
        return (int) (px / activity.getResources().getDisplayMetrics().density);
    }

    protected int pxFromDp(float dp) {
        return (int) (dp * activity.getResources().getDisplayMetrics().density);
    }


    // ------------------------------------- //
}
