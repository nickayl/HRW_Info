package com.domenicoaiello.devicespecs.model;

import android.content.Context;

import com.domenicoaiello.devicespecs.model.titlevaluepair.ImageTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ImageValuePairProvider;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ValuePairProvider;
import com.domenicoaiello.simpletab.TextTab;
import com.domenicoaiello.simpletab.style.TabStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Domenico on 03/11/2017.
 */

public class TextTabWithTvp extends TextTab implements ValuePairProvider, ImageValuePairProvider {

    private List<TextTitleValuePair> pairs = new ArrayList<>();
    private List<ImageTitleValuePair> imagePairs = new ArrayList<>();

    public TextTabWithTvp(Context context, int position) throws IllegalArgumentException {
        super(context, position);
    }

    public TextTabWithTvp(Context context, int position, TabStyle style) {
        super(context, position, style);
    }

    public void setTitleValuePairs(List<TextTitleValuePair> pairs) {
        this.pairs.addAll(pairs);
    }

    public void setImageValuePairs(List<ImageTitleValuePair> pairs) {
        this.imagePairs.addAll(pairs);
    }

    @Override
    public List<TextTitleValuePair> getTitleValuePair() {
        return this.pairs;
    }


    @Override
    public List<ImageTitleValuePair> getImageValuePairProvider() {
        return imagePairs;
    }


}






















