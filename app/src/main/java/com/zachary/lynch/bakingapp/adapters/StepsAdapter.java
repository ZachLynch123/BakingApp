package com.zachary.lynch.bakingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.zachary.lynch.bakingapp.R;
import com.zachary.lynch.bakingapp.model.Steps;

import java.util.List;

public class StepsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Steps> mSteps;

    public StepsAdapter(Context context, List<Steps> steps){
        mContext = context;
        mSteps = steps;
    }


    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Override
    public Object getItem(int position) {
        return mSteps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.steps_layout, parent, false);
            holder = new ViewHolder();
            holder.shortDescription = view.findViewById(R.id.shortDescription);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Steps singleStep = mSteps.get(position);
        holder.shortDescription.setText(singleStep.getShortDescription());
        return view;
    }

    private static class ViewHolder{
        TextView shortDescription;

    }
}
