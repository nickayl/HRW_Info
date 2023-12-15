package com.domenicoaiello.devicespecs.model.adapters.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.domenicoaiello.devicespecs.model.fregment.FragmentObject;
import com.domenicoaiello.simpletab.Tab;
import com.domenicoaiello.simpletab.TabContainer;


/**
 * Created by Domenico on 28/09/2017.
 */

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class DeviceSpecPagerAdapter extends FragmentPagerAdapter {

    private TabContainer tabController;
    private Activity activity;

    public DeviceSpecPagerAdapter(FragmentManager fm, TabContainer tabController, Activity activity) {
        super(fm);
        this.tabController = tabController;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int i) {

        Tab tab = tabController.getTab(i);

        FragmentObject fragment = new FragmentObject();
        fragment.setActivity(activity);
        fragment.setTab(tab);

        return fragment;
    }

    @Override
    public int getCount() {
        return tabController.getTabCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabController.getTab(position).getName();
    }

}
