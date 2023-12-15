package com.domenicoaiello.simpletab;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by Domenico on 02/10/2017.
 */

public interface Tab {

    int STYLE_DEFAULT = 0;
    int STYLE_ELECTRIC_YELLOW = 1;

    String getName();
    int getPosition();
    View getTabView();
    Context getContext();

    /* void setName(String name);
    void setPosition(int position);*/
    void setOnTabClickListener(OnTabClickListener onTabClickListener);


    void setSelected();
    void setUnselected();
    boolean isSelected();
}
