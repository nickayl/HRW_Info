package com.domenicoaiello.simplepopup;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by Domenico on 27/10/2017.
 */

public interface Popup<T> {

    void toggle();
    void show();
    void show(int x, int y);
    void hide();

    void setMarginX(int marginX);
    void setMarginY(int marginY);
    void setAttachedView(View view);

    void setContent(T content);
}
