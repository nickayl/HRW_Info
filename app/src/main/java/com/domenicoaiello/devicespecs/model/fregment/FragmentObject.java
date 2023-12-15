package com.domenicoaiello.devicespecs.model.fregment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.domenicoaiello.devicespecs.R;
import com.domenicoaiello.devicespecs.model.titlevaluepair.BaseTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ImageValuePairProvider;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.VariableTextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ValuePairProvider;
import com.domenicoaiello.devicespecs.model.titlevaluepair.onValueChangeListener;
import com.domenicoaiello.devicespecs.model.adapters.recyclerview.SimpleLineAdapter;
import com.domenicoaiello.simpletab.Tab;

import java.util.ArrayList;

/**
 * Created by Domenico on 29/09/2017.
 */

// Instances of this class are fragments representing a single
// object in our collection.
public class FragmentObject extends Fragment implements onValueChangeListener {

    private Tab tab;
    private ArrayList<BaseTitleValuePair> pairs;
    private SimpleLineAdapter mAdapter;
    private Activity activity;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_object, container, false);
        loadData(rootView);

        Log.d("FragmentObject-INFO","onCreateView");
        return rootView;
    }

    private void loadData(View rootView) {
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        Log.d("FragmentObject-INFO","loadData "+tab.getName());
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(null);


        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        pairs = new ArrayList<>();

        if(tab instanceof ImageValuePairProvider)
            pairs.addAll(((ImageValuePairProvider) tab).getImageValuePairProvider());
        if(tab instanceof ValuePairProvider)
            pairs.addAll(((ValuePairProvider) tab).getTitleValuePair());

        mAdapter = new SimpleLineAdapter(pairs,activity);

        mRecyclerView.setAdapter(mAdapter);

        for(BaseTitleValuePair tvp : pairs) {
            if (tvp instanceof VariableTextTitleValuePair) {
                ((VariableTextTitleValuePair) tvp).setOnValueChangeListener(this);
            }
        }
    }


    @Override
    public void onValueChange(String newValue, String valueCode) {
      //  Log.d("Debug", String.format("Position: Fragment Object, onValueChange: %s code: %s",newValue,valueCode));

        for(int i=0; i < pairs.size(); i++) {
            if (pairs.get(i).getCode().equalsIgnoreCase(valueCode)) {
                ((TextTitleValuePair) pairs.get(i)).setValue(newValue);
                mAdapter.notifyItemChanged(i);
                break;
            }
        }

    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}