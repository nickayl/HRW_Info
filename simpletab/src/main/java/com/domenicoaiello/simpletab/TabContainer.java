package com.domenicoaiello.simpletab;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.domenicoaiello.simpletab.style.TabStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 04/11/2017.
 */

public class TabContainer extends HorizontalScrollView {

    private Context context;
    private LinearLayout linearLayout;
    private int screenWidth;
    private List<Tab> tabs = new ArrayList<>();
    private OnTabClickListener onTabClickListener;

    private int oldPosition=0;

    public TabContainer(Context context) {
        super(context);
        this.context = context;
        initialize();
    }

    public TabContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize();
    }

    public TabContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialize();
    }

    private void initialize() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);

        linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        addView(linearLayout);
        setBackgroundColor(getResources().getColor(R.color.tabViewBackground));

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        screenWidth = size.x;
    }

    private void addTab(Tab tab) {
        this.tabs.add(tab);
        linearLayout.addView(tab.getTabView());
    }

    public void addTabs(Tab... tablist) {
        for(Tab t : tablist)
            addTab(t);

      //  refreshContainerView();
    }

    public Tab getTab(int position) {
        Tab tab = null;

        for(Tab t : tabs) {
            if (t.getPosition() == position) {
                tab = t;
                break;
            }
        }
        return tab;
    }

    public void setSelectedTab(int position) {
        Tab t = tabs.get(position);

        for(Tab tab : tabs)
            tab.setUnselected();

        t.setSelected();
        performScroll(oldPosition, position);
        this.oldPosition = position;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    private void performScroll(int oldPosition, int position) {
        View tabView = tabs.get(position).getTabView();
        int viewWidth = tabView.getWidth();

        if (oldPosition < position) {
            if (screenWidth < viewWidth * (position + 1))
                smoothScrollTo(tabView.getRight() + viewWidth, 0);
        } else {
            if (screenWidth > viewWidth * (position + 1))
                smoothScrollTo(tabView.getLeft() - viewWidth, 0);
        }
    }

   /* public void setGlobalTypeface(Typeface t) {
        for(Tab t : tabs)
            t
    } */

   public void setTabStyle(TabStyle style) {

   }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;

        for(Tab t : tabs)
            t.setOnTabClickListener(this.onTabClickListener);
    }

    public int getTabCount() {
        return tabs.size();
    }

    public void refreshContainerView() {
        forceLayout();
        requestLayout();
    }

    protected int dpFromPx(float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    protected int pxFromDp(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

}
