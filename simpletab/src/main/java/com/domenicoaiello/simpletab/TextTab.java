package com.domenicoaiello.simpletab;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.domenicoaiello.simpletab.style.DefaultTabStyle;
import com.domenicoaiello.simpletab.style.TabStyle;

/**
 * Created by Domenico on 02/10/2017.
 */
public class TextTab implements Tab  {

    protected String name="Unnamed";
    protected int position=0;
    private Context context;

    private OnTabClickListener onTabClickListener;
    private TextViewHolder viewHolder;

    private TextTab() { }

    public TextTab(Context context, int position) throws IllegalArgumentException {
        this(context, position, new DefaultTabStyle(context));
    }

    public TextTab(Context context, int position, TabStyle style) {
        this.position = position;
        this.context = context;

        viewHolder = new TextViewHolder(style);
    }


    @Override
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
        viewHolder.setContent(name);
    }

    public void setTypeface(Typeface typeface) {
       // viewHolder.setTypeface(typeface);
    }

    @Override
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public View getTabView() {
        return viewHolder.getTabView();
    }


    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setSelected() {
        if(viewHolder.getSelectedTabIndicator() != null)
            viewHolder.getSelectedTabIndicator().setVisibility(View.VISIBLE);
    }

    @Override
    public void setUnselected() {
        if(viewHolder.getSelectedTabIndicator() != null)
            viewHolder.getSelectedTabIndicator().setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean isSelected() {
        return viewHolder.getSelectedTabIndicator().getVisibility() == View.VISIBLE;
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;

        viewHolder.getTabView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("INFO", "TextTab clicked at position: "+position);
                if(TextTab.this.onTabClickListener != null) TextTab.this.onTabClickListener.onClick(TextTab.this, position);
            }
        });

    }


    public static Tab[] createFromArray(Context context, int[] position, String[] name) throws IllegalArgumentException {
        if(position.length != name.length)
            throw new IllegalArgumentException("Position array and names array must have the same length.");

        TextTab[] tabArray = new TextTab[position.length];
        for(int i=0; i < position.length; i++) {
            tabArray[i] = new TextTab(context, position[i]);
            tabArray[i].setName(name[i]);
        }

        return tabArray;
    }

    public static Tab[] createFromArray(Context context,  String[] name) {
        int[] pos = new int[name.length];

        for(int i=0; i < name.length; i++)
            pos[i] = i;

        return createFromArray(context,pos,name);
    }

    private abstract class BaseViewHolder<T> {

        private ViewGroup tabView;
        private View selectedTabIndicator;

        public BaseViewHolder(TabStyle style) {
            tabView = (ViewGroup) LayoutInflater
                    .from(context)
                    .inflate(R.layout.tab_default, null);

            tabView.setLayoutParams(new FrameLayout.LayoutParams(pxFromDp(100), ViewGroup.LayoutParams.MATCH_PARENT));
            selectedTabIndicator = tabView.findViewById(R.id.bottomLineFrameLayout);
            selectedTabIndicator.setVisibility(View.INVISIBLE);
            applyStyle(style);
        }

        protected void applyStyle(TabStyle style) {
            tabView.setBackgroundColor(style.getBackgroundColor());
            selectedTabIndicator.setBackgroundColor(style.getSelectedTabIndicatorColor());
        }

        ViewGroup getTabView() {
            return tabView;
        }

        View getSelectedTabIndicator() {
            return selectedTabIndicator;
        }

        public abstract void setContent(T content);
    }

    private class TextViewHolder extends BaseViewHolder<String> {
        private TextView mTextView;
        private TabStyle style;

        TextViewHolder(TabStyle style) {
            super(style);
            this.style = style;
            mTextView = (TextView) getTabView().findViewById(R.id.textView);

            mTextView.setTextColor(style.getTextColor());
            mTextView.setTypeface(style.getTypeface());
            mTextView.setTextSize(style.getTextSize());
        }


        @Override
        public void setContent(String content) {
            mTextView.setText(style.filterText(content));
        }

        void setTypeface(Typeface t) {
            mTextView.setTypeface(t);
        }

    }

    protected int dpFromPx(float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    protected int pxFromDp(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }



/*
    public static class Builder {
        private TextTab tabInstance;

        public Builder() {
            tabInstance = new TextTab();
        }

        public Builder setName(String name) {
            tabInstance.setName(name);
            return this;
        }

        public Builder setActivity(Activity activity) {
            tabInstance.setActivity(activity);
            return this;
        }

        public Builder setPosition(int position) {
            tabInstance.setPosition(position);
            return this;
        }

        public Builder setViewId(@IdRes int viewId) {
            tabInstance.setViewId(viewId);
            return this;
        }

        public Builder setSelectedTabViewId(@IdRes int viewId) {
            tabInstance.setSelectedViewId(viewId);
            return this;
        }

        public TextTab build() {
            tabInstance.initialize();
            return tabInstance;
        }
    }*/
}

