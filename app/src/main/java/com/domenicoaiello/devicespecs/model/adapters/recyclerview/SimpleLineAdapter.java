package com.domenicoaiello.devicespecs.model.adapters.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.domenicoaiello.devicespecs.R;
import com.domenicoaiello.devicespecs.model.titlevaluepair.BaseTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.ImageTitleValuePair;
import com.domenicoaiello.devicespecs.model.titlevaluepair.TextTitleValuePair;
import com.domenicoaiello.devicespecs.model.Utils;
import com.domenicoaiello.simplepopup.Popup;
import com.domenicoaiello.simplepopup.TextPopup;

import java.util.ArrayList;

import static com.domenicoaiello.devicespecs.model.titlevaluepair.BaseTitleValuePair.OPTIONS_HIDE_QUESTIONMARK_BOX;

/**
 * Created by Domenico on 28/09/2017.
 */
public class SimpleLineAdapter extends RecyclerView.Adapter<SimpleLineAdapter.BaseViewHolder> {

    private ArrayList<BaseTitleValuePair> mTitleValuePair = new ArrayList<>();
    private Activity activity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public SimpleLineAdapter(ArrayList<BaseTitleValuePair> mTitleValuePair, Activity activity) {
        this.activity = activity;
        this.mTitleValuePair.addAll(mTitleValuePair);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, @LayoutRes int viewType) {
        // create a new view
        ViewGroup v = (ViewGroup) LayoutInflater
                .from(parent.getContext())
                .inflate(viewType, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return BaseViewHolder.getAssociatedViewHolder(viewType,activity,v);
    }


    @Override
    public int getItemViewType(@LayoutRes int position) {
        return mTitleValuePair.get(position).getAssociatedLayout();
    }

    // Replace the contents of a view (invoked by the layout manager)

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        BaseTitleValuePair btvp = mTitleValuePair.get(position);
        viewHolder.setActivity(activity);
        viewHolder.Draw(btvp, btvp.getOptions());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTitleValuePair.size();
    }






    private static class ViewHolder extends BaseViewHolder {
        private TextView mContent;

        ViewHolder(View v, Context context) {
            super(v,context);
            mContent = (TextView) v.findViewById(R.id.lineItemTextContent);
            mContent.setTypeface(Utils.getGlobalTypeface(context));
        }

        @Override
        void Draw(BaseTitleValuePair titleValuePair, int options) {
            super.Draw(titleValuePair, options);
            String value = ((TextTitleValuePair) titleValuePair).getValue();
            mContent.setText(value);
        }

        @Override
        boolean check(@LayoutRes int layoutRes) {
            return R.layout.line_item_recyclerview == layoutRes;
        }
    }






    private static class QuestionmarkViewHolder extends ViewHolder {

        private final TextView mQuestionmarkBox;
        private final Popup<String> popup;
        private Activity activity;
        //     private final View questionmarkView;

        QuestionmarkViewHolder(View v, final Activity activity) {
            super(v, activity);
            this.activity = activity;

            mQuestionmarkBox = (TextView) v.findViewById(R.id.questionmark_box);
            mQuestionmarkBox.setText("?");

            popup = new TextPopup(activity, R.id.mainActivityRootView, mQuestionmarkBox);

            mQuestionmarkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popup.toggle();
                }
            });
        }

        @Override
        boolean check(@LayoutRes int layoutRes) {
            return R.layout.line_item_questionmark == layoutRes;
        }

        @Override
        void Draw(BaseTitleValuePair titleValuePair, int options) {
            super.Draw(titleValuePair,options);

            if(options == OPTIONS_HIDE_QUESTIONMARK_BOX)
                mQuestionmarkBox.setVisibility(View.INVISIBLE);

            popup.setContent( ((TextTitleValuePair) titleValuePair).getTextDescription() );
        }

        @Override
        protected void doCustomStuff() {
            super.doCustomStuff();
        }
    }







    private static class ViewHolderImage extends BaseViewHolder {
        private ImageView mContentImage;

        ViewHolderImage(View v, Context context) {
            super(v,context);
            mContentImage = (ImageView) v.findViewById(R.id.poweredby);
        }

        @Override
        void Draw(BaseTitleValuePair titleValuePair, int options) {
            super.Draw(titleValuePair,options);
            Bitmap b = ((ImageTitleValuePair) titleValuePair).getValue();
            mContentImage.setImageBitmap(b);
        }

        @Override
        boolean check(@LayoutRes int layoutRes) {
            return R.layout.line_item_poweredby == layoutRes;
        }
    }






    static abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        private Activity activity;

        BaseViewHolder(View v, Context context) {
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.line_item_textView);
            mTitleTextView.setTypeface(Utils.getGlobalTypeface(context));
        }

        void Draw(BaseTitleValuePair titleValuePair) {
            Draw(titleValuePair, -1);
        }

        /**
         * Performs all the operations needed to display the view content. <br> Each specialized version of the base class must do it's own view manipulations operations.
         * @param titleValuePair The Key-Value pair to display.
         * @param options Optional parameter. Can be OPTIONS_HIDE_QUESTIONMARK_BOX or OPTIONS_NONE
         */
        void Draw(BaseTitleValuePair titleValuePair, int options) {
            mTitleTextView.setText(titleValuePair.getTitle());
            doCustomStuff();
        }

        boolean check(@LayoutRes int layoutRes) {
            return false;
        }

        /**
         * Ovverride this method to do additional tasks that will be executed after the Draw method is called.
         */
        protected void doCustomStuff() {

        }

        void setActivity(Activity activity) {
            this.activity = activity;
        }

        static BaseViewHolder getAssociatedViewHolder(@LayoutRes int viewType, Activity activity, ViewGroup inflatedView) {

            BaseViewHolder vh;

            if(viewType == R.layout.line_item_recyclerview)
                vh = new ViewHolder(inflatedView, activity);
            else if(viewType == R.layout.line_item_questionmark)
                vh = new QuestionmarkViewHolder(inflatedView, activity);
            else
                vh = new ViewHolderImage(inflatedView, activity);

            vh.setActivity(activity);
            return vh;
        }

        public Activity getActivity() {
            return activity;
        }
    }

}